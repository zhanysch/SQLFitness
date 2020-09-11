package com.example.firebase.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.firebase.R


object NotificationUtils {

    private const val CHANNEL_ID = "my_channel"
    private const val CHANNEL_NAME= "CHANNEL_NAME"
    private const val CHANNEL_DESK= "CHANNEL_DESCRIPTION"


     fun createNotification(context: Context){
        creaateNotificationChanel(context)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID )
            .setContentTitle("First")
            .setContentText("Haloo!")
            .setSmallIcon(R.drawable.ic_baseline_add_comment_24)
            .build()

        NotificationManagerCompat.from(context).notify(1,builder)
    }

    private fun creaateNotificationChanel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { /// если версия андроид 8 или выше

            val importance =   NotificationManager.IMPORTANCE_HIGH      //!1       // при уведомл , отсутств звук на phone  все ровно появит звук о уведомлен
            val channel = NotificationChannel(CHANNEL_ID , CHANNEL_NAME, importance)
            channel.description = CHANNEL_DESK

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager  // !1
            notificationManager.createNotificationChannel(channel)

        }

    }


}