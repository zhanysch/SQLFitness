package com.ok.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.icu.number.Precision
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.BuildConfig.API_KEY_MAPS
import com.example.firebase.R
import com.example.firebase.utils.PermissionUtlis
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point

import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseMapActivity: AppCompatActivity() {

    abstract fun getResId():Int
    abstract fun getMapViewId(): Int

    private var mapView: MapView? = null
    protected var map: MapboxMap? = null
    protected var symbolManager : SymbolManager? = null
    private var symbol: Symbol? = null
    private var client: MapboxDirections? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext,  API_KEY_MAPS)


        setContentView(getResId())
        mapView= findViewById(getMapViewId())

        mapView?.onCreate(savedInstanceState)  // 1)чтоб карта функционир при перевороте карты
        mapView?.getMapAsync{ mapBoxMap->
            map = mapBoxMap
            mapBoxMap.setStyle(Style.MAPBOX_STREETS){ style->
                setupListeners(mapBoxMap)
                loadImages(style)
                initSource(style)
                initLayer(style)
                mapView?.let {
                    symbolManager = SymbolManager(it, mapBoxMap,style)
                }
                if (PermissionUtlis.requestLocationPermission(this))             //3)геолакац
                    showUserLocation() //   //3) геолакац
            }
        }
    }

    private fun initLayer(style: Style) {
        val layer = LineLayer(LINE_LAYER,LINE_SOURCE)
        layer.setProperties(
            PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
            PropertyFactory.lineWidth(7f),
            PropertyFactory.lineColor(Color.parseColor("#009688"))
        )
        style.addLayer(layer)
    }

    private fun initSource(style: Style) {  // кординаты
        style.addSource(GeoJsonSource(LINE_SOURCE))
    }

    protected fun getDirections(latLng: LatLng) {  // по клик на карту расчитыва путь из точки А в точку Б
        val location = map?.locationComponent?.lastKnownLocation
        client = MapboxDirections.builder()
            .accessToken(API_KEY_MAPS)
            .origin(Point.fromLngLat(location?.longitude?:0.0, location?.latitude?:0.0))
            .destination(Point.fromLngLat(latLng.longitude,latLng.latitude))
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .build()

        client?.enqueueCall(object :Callback<DirectionsResponse>{
            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                val currentRoute =  response.body()?.routes()?.first()
                Toast.makeText(applicationContext, currentRoute?.distance().toString(), Toast.LENGTH_LONG).show()
                val source = map?.style?.getSourceAs<GeoJsonSource>(LINE_SOURCE)
                source?.let {
                    it.setGeoJson(currentRoute?.geometry()
                        ?.let { it1 ->
                            LineString.fromPolyline(it1, PRECISION_6)
                        })
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                Log.d("GDGDGDg","gfdfgdfgdfg")
            }

        })
    }

    private fun setupListeners(mapBoxMap: MapboxMap) {
       mapBoxMap.addOnMapClickListener {
           getDirections(it)
           addMarker(it)
           return@addOnMapClickListener true
       }
    }

    private fun loadImages(style: Style) {
        style.addImageAsync(
            MARKER_IMAGE,
            BitmapUtils.getBitmapFromDrawable(resources.getDrawable(R.drawable.ic_baseline_add_location_24))!!,true)
    }

    protected fun addMarker(latLng: LatLng) {
        symbol?.let {   symbolManager?.delete(it) }
        val symbolOptions = SymbolOptions()
            .withLatLng(latLng)
            .withIconImage(MARKER_IMAGE)
        symbol = symbolManager?.create(symbolOptions)
    }

    override fun onRequestPermissionsResult(  //возвращ результ с диалога на разрешен геолакац  2)
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtlis.LOCATION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showUserLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "Range")
    private fun showUserLocation(){   //3) геолакац
        map?.style?.let {
            val locationComponents = map?.locationComponent
            locationComponents?.activateLocationComponent(
                LocationComponentActivationOptions.builder(applicationContext, it)
                    .build()
            )
            locationComponents?.isLocationComponentEnabled = true
            locationComponents?.cameraMode = CameraMode.TRACKING
            locationComponents?.renderMode = RenderMode.COMPASS
            val location =  locationComponents?.lastKnownLocation

            val latlng = LatLng(location?.latitude?:0.0 , location?.longitude?:0.0)

            val cmpos = CameraPosition.Builder()
                .target(latlng).zoom(15.0).build()

            map?.animateCamera( CameraUpdateFactory.newCameraPosition(cmpos),3000)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    companion object{
        private const val MARKER_IMAGE = "MARKER_IMAGE"
        private const val LINE_SOURCE = "LINE_SOURCE"
        private const val LINE_LAYER = "LINE_LAYER"
    }

}