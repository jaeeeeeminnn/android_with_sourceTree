package com.example.neverdyingservice_alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi

class RestartService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    @RequiresApi (Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId : String, channelName: String) : String {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN)
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Start Restart Service", Toast.LENGTH_LONG).show()

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("clipboard", "clipboardService")
            } else {
                ""
            }

        val notification : Notification.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(this, channelId)
            } else {
                Notification.Builder(this)
            }

        notification.setContentTitle("ClipboardService Title")
        notification.setContentText("ClipboardService Text")
        notification.setSmallIcon(R.mipmap.ic_launcher)

        val foregroundNoti = notification.build()

        // RestartService를 startForeground로 실행
        startForeground(0, foregroundNoti)
        stopSelf()


        return START_NOT_STICKY
    }
}