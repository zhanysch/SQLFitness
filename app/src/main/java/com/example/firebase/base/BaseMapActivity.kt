package com.example.firebase.base

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import com.example.firebase.R
import com.example.firebase.utils.ConstansLatlng.Bishkek
import com.example.firebase.utils.MapUtils
import com.example.firebase.utils.PermissionUtlis
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource


abstract class BaseMapActivity: SupportMapActivity() {


    protected var symbolManager: SymbolManager? = null
    private var symbol: Symbol? = null
    protected var fusedLocation: FusedLocationProviderClient? = null


    override fun onMapLoaded(mapBoxMap: MapboxMap, style: Style) {
        setupListeners(mapBoxMap)
        loadImages(style)
        initSource(style)
        initLayer(style)
        mapView?.let {
            symbolManager = SymbolManager(it, mapBoxMap, style)
        }
        if (PermissionUtlis.requestLocationPermission(this))             //3)геолакац
            showUserLocation() //   //3) геолакац
    }

    private fun initLayer(style: Style) {
        val layer = MapUtils.createLayer(
            layerName = LINE_LAYER,
            sourceName = LINE_SOURCE
        )
        style.addLayer(layer)
    }

    private fun initSource(style: Style) {  // кординаты
        style.addSource(MapUtils.getSource(LINE_SOURCE))

    }


    protected fun getDirections(featureCollection: FeatureCollection) {  // разрисовыв на карте перемещение
        val source = map?.style?.getSourceAs<GeoJsonSource>(LINE_SOURCE)
        source.let { geoJsonsource ->
            geoJsonsource?.setGeoJson(featureCollection)
        }

    }


    private fun setupListeners(mapBoxMap: MapboxMap) {
        mapBoxMap.addOnMapClickListener {
            // getDirections(it)
            addMarker(it)
            return@addOnMapClickListener true
        }
    }

    private fun loadImages(style: Style) {
        MapUtils.addImage(
            style,
            MARKER_IMAGE,
            resources.getDrawable(R.drawable.ic_baseline_add_location_24)
        )
    }

    protected fun addMarker(latLng: LatLng) {
        symbol?.let { symbolManager?.delete(it) }
        val symbolOptions = MapUtils.createSymbol(latLng, MARKER_IMAGE)
        symbol = symbolManager?.create(symbolOptions)
    }

    override fun onRequestPermissionsResult(  //возвращ результ с диалога на разрешен геолакац  2)
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtlis.LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showUserLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "Range")
    private fun showUserLocation() {   //3) геолакац
        map?.style?.let {

            fusedLocation = LocationServices.getFusedLocationProviderClient(this)

            val locationComponentOptions = LocationComponentOptions.builder(this)
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions
                .builder(this, it)
                .locationComponentOptions(locationComponentOptions)
                .build()


            val locationComponent = map?.locationComponent
            locationComponent?.activateLocationComponent(
                LocationComponentActivationOptions.builder(applicationContext, it)
                    .build()
            )

            locationComponent?.activateLocationComponent(locationComponentActivationOptions)

            locationComponent?.isLocationComponentEnabled = true
            locationComponent?.cameraMode = CameraMode.TRACKING
            locationComponent?.renderMode = RenderMode.COMPASS
            fusedLocation?.lastLocation?.addOnSuccessListener {
                if (it == null) {
                    animateCamera(Bishkek)  // в переменка constansLatlng class
                } else {
                    animateCamera(LatLng(it.latitude, it.longitude))
                }
            }
        }
    }


    private fun animateCamera(latLng: LatLng, zoom: Double = CAMERA_ZOOM) {

        Handler(Looper.getMainLooper()).postDelayed({
            map?.animateCamera(
                MapUtils.getCameraPosition(latLng, zoom),
                DURATION_CAMERA
            )
        }, 6000)
    }

    companion object {
        private const val MARKER_IMAGE = "MARKER_IMAGE"
        private const val LINE_SOURCE = "LINE_SOURCE"
        private const val DURATION_CAMERA = 3000
        private const val LINE_LAYER = "LINE_LAYER"
        private const val CAMERA_ZOOM = 17.0
    }
}