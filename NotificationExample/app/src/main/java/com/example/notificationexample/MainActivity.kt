package com.example.notificationexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id"
    private val CHANNEL_NAME= "channel_name"
    private var NOTIFICATION_ID = 0
    private val KEY_TEXT_REPLY = "key_text_reply"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        item_button.setOnClickListener {
            sendNotification()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun getMessageText(intent : Intent) : CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(KEY_TEXT_REPLY)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = CHANNEL_ID
            val channelName = CHANNEL_NAME
            val channel = NotificationChannel(channelId, channelName, IMPORTANCE_HIGH).apply {
                description = "Channel Description"
                enableLights(true)
                lightColor = Color.GREEN
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    // 버튼 클릭시 실행할 메소드
    private fun sendNotification() {
        val title = item_Title.text.toString()
        val message = item_message.text.toString()

        // 알림을 탭에 표시하기 위한 intent와 pendingIntent
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        createNotificationChannel(notificationManager)
        // notification 생성
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_phone_android_24)
            setContentTitle(title)
            setContentText(message)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        // noti 표시 + 진행률 표시
        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(this).apply {
            notification.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            notify(NOTIFICATION_ID, notification.build())

            val after = title + " " + message
            notification.apply {
                setContentTitle(after)
                setContentText("is Goooood")
            }

            for (i in 0..100) {
                thread(start = true) {
                    notification.setProgress(PROGRESS_MAX, i, false)
                    Thread.sleep(5000)
                }
            }

            // 이걸 해줘야 진행률 표시를 삭제해줌.
            notification.setProgress(0, 0, false)
            notify(NOTIFICATION_ID++, notification.build())
        }
    }
}