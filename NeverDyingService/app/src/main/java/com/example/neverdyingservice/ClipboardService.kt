package com.example.neverdyingservice

import android.app.*
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import java.util.*

class ClipboardService : Service(), ClipboardManager.OnPrimaryClipChangedListener {

    companion object {
        var service : Intent? = null
        var normalExit: Boolean = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId : String, channelName: String) : String {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Start Service", Toast.LENGTH_LONG).show()

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("ClipboardChannelId", "ClipboardService")
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(123, notification.build())

            Thread.sleep(2000)

            //remove notification
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val emptyNoti : Notification.Builder =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Notification.Builder(this, channelId)
                } else {
                    Notification.Builder(this)
                }
            emptyNoti.setSmallIcon(R.drawable.notification_bg)

            nm.notify(123, emptyNoti.build())
            nm.cancel(123)
        }

        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onPrimaryClipChanged() {
        TODO("Not yet implemented")
    }
}