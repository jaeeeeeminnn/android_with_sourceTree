package com.example.musicservicenoti.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.musicservicenoti.R
import com.example.musicservicenoti.service.MusicService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id"
    private val CHANNEL_NAME = "channel_name"
    private val NOTIFICATION_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var isPlaying: Boolean = false

        item_button.setOnClickListener {
            pushNotification()
            if (!isPlaying) {
                val intent = Intent(this, MusicService::class.java)
                intent.action = "play"
                isPlaying = true
                startService(intent)
            } else {
                val intent = Intent(this, MusicService::class.java)
                intent.action = "pause"
                isPlaying = false
                startService(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Channel Description"
                enableLights(true)
                lightColor = R.color.level_4
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun pushNotification() {
        val title = "Salt Peanuts"
        val text = "is playing"

        // 알림을 탭에 표시하기 위한 intent와 pendingintent를 준비
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        // channel 등록
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        createNotificationChannel(notificationManager)

        // notification 생성
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_library_music_24)
            setContentTitle(title)
            setContentText(text)
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }
}