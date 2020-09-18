package com.example.firebase.ui.main



import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.firebase.MyLocationForegroundService
import com.example.firebase.MyLocationForegroundService.Companion.STOP_SERVICE_ACTION
import com.example.firebase.R
import com.example.firebase.base.BaseMapActivity
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseMapActivity() {
    override fun getResId() = R.layout.activity_main
    override fun getMapViewId() = R.id.mapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupListeners()
        stopForeground()


    }


    private fun setupListeners() {
        fab.setOnClickListener {
            map?.setStyle(Style.SATELLITE_STREETS)
        }
        BtnStart.setOnClickListener {
           startForegroundService()
        }
         BtnStop.setOnClickListener {
             stopForeground()
         }
    }

    private fun stopForeground(){
        val intent = Intent(this, MyLocationForegroundService::class.java)
       // intent.putExtra("blabla", "blabla")
        stopService(intent)
    }


   private fun startForegroundService(){
        val intent = Intent(this, MyLocationForegroundService::class.java)
        intent.putExtra("blabla", "blabla")
        startService(intent)
    }




}