package com.example.firebase

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.firebase.data.NotificationHelper
import kotlinx.coroutines.*

class MyLocationForegroundService: Service() {

    private var job = Job()
    private var scope = CoroutineScope(job)
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP_SERVICE_ACTION){
           stopForeground(true)
        } else {
            startForeground(1,NotificationHelper.createNotification(applicationContext))
            test()
        }
        return START_REDELIVER_INTENT
    }

    private fun test(){
        scope.launch(Dispatchers.Default) {
            for (i in 0..10_000_000){
                Log.d("___test", i.toString())
                delay(2000)
            }

        }

    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    companion object{
        const val STOP_SERVICE_ACTION = "STOP_SERVICE_ACTION"
    }
}