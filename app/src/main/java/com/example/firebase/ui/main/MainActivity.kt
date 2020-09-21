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
import com.example.firebase.data.events.UserLocationEvent
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


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

    private fun stopForeground() {
        val intent = Intent(this, MyLocationForegroundService::class.java)
        // intent.putExtra("blabla", "blabla")
        stopService(intent)
    }


    private fun startForegroundService() {
        val intent = Intent(this, MyLocationForegroundService::class.java)
        intent.putExtra("blabla", "blabla")
        startService(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  // в главном потоке
    fun getUserData(event: UserLocationEvent) {
        getDirections(event.list)
    }

    override fun onStart() {  // получаем событие
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {// не получаем событие
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}


