package com.ok.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.BuildConfig.API_KEY_MAPS
import com.mapbox.mapboxsdk.BuildConfig

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

abstract class BaseMapActivity: AppCompatActivity() {

    abstract fun getResId():Int
    abstract fun getMapViewId(): Int

    private var mapView: MapView? = null
    protected var map: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext,  API_KEY_MAPS)



        setContentView(getResId())
        mapView= findViewById(getMapViewId())

        mapView?.onCreate(savedInstanceState)  // 1)чтоб карта функционир при перевороте карты
        mapView?.getMapAsync{
            map = it
            it.setStyle(Style.MAPBOX_STREETS){
                if (PermissionUtlis.requestLocationPermission(this))             //3)геолакац
                    showUserLocation() //   //3) геолакац
            }
        }
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
                .target(latlng).zoom(10.0).build()

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

}