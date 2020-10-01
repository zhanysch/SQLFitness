package com.example.firebase

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent

import android.os.IBinder
import android.os.Looper

import com.example.firebase.data.NotificationHelper
import com.example.firebase.data.events.TraningEndedEvent
import com.example.firebase.data.events.UserLocationEvent
import com.google.android.gms.location.*
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.turf.TurfMeasurement
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus

class MyLocationForegroundService: Service() {

    private var fusedLocation : FusedLocationProviderClient? = null
    private var list = arrayListOf<Point>()
    private var totalLineDistance : Double = 0.0

    private var job = Job()
    private var scope = CoroutineScope(job)
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP_SERVICE_ACTION){
           stopForeground(true)
            stopSelf()
        } else {

            fusedLocation = LocationServices.getFusedLocationProviderClient(applicationContext) // fusedlocati  данные передает последн данн о нашем местонахожден
            val locationRequest = LocationRequest()
                locationRequest.interval = 10_000
            locationRequest.fastestInterval = 3_000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //fusellocati


            fusedLocation?.requestLocationUpdates(locationRequest, fLocListener, Looper.getMainLooper())

            startForeground(1,NotificationHelper.createNotification(applicationContext))
            test()
        }
        return START_REDELIVER_INTENT
    }


    private val fLocListener = object  : LocationCallback(){  //эта функц вызывает callback  // записывает в список кординаты
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let {  // last location добавляем в список
                list.add(Point.fromLngLat(it.longitude, it.latitude))
                calculateDistance()
                EventBus.getDefault().post(UserLocationEvent(list, totalLineDistance))  //even это событие
            }
        }
    }

    private fun calculateDistance(){  // функция при получении новых кординат подсчитывает между последн и предпоследн кординатами
        val distanceBetweenLastAndSecondToLastPoint: Double
        if (list.size >= 2) {   //если размер списка кординат ,  >= 2 можно высчитывать расстояние size возвращает размер списка
            distanceBetweenLastAndSecondToLastPoint =  TurfMeasurement.distance(
                list[list.size - 2], list[list.size- 1],   // расчитывает дистанци между последним и передпоследним

            )
            if (distanceBetweenLastAndSecondToLastPoint > 0 ){
                totalLineDistance += distanceBetweenLastAndSecondToLastPoint
            }
        }
    }

    private fun test(){
        scope.launch(Dispatchers.Default) {
            for (i in 0..10_000_000){
                delay(2000)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        fusedLocation?.removeLocationUpdates(fLocListener)
        EventBus.getDefault().post(TraningEndedEvent(true))  // TraningEndedEvent из data class UserLocationEvent
        // true т.к значние  TraningEndedEvent boolean в dataclass
    }

    companion object{
        const val STOP_SERVICE_ACTION = "STOP_SERVICE_ACTION"
    }
}