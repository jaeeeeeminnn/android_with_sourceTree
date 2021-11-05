package com.example.jazzbargame.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.jazzbargame.DEFINES
import com.example.jazzbargame.Database.CustomerDatabase
import com.example.jazzbargame.R
import com.example.jazzbargame.entity.Customer
import com.example.jazzbargame.model.CustomerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SplashActivity : AppCompatActivity() {

    private val CHANNEL_ID = "Jazz_Bar_Open"
    private val CHANNEL_NAME = "Jazz_Bar_Channel"
    private val TITLE = "Sepp's Bar"
    private val NOTIFICATION_ID = 0
    private var IN_OUT: Boolean = false
    private val D = DEFINES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // DB 작업
        D.customerDatabase = CustomerDatabase.getInstance(this)!!

        // DB에 뭔가를 넣는 작업은 처음에만 할 거임.
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                D.loadDatabase()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // image button 클릭하면 mainActivity 로 전환.
        val startButton: ImageButton = findViewById(R.id.splash_button)
        startButton.setOnClickListener {
            sendNotification()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        sendNotification()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        finishAndRemoveTask()
        System.exit(0)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Jazz Bar Game"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        var text: String? = null
        if (IN_OUT) {
            text = "is Closed Today"
            IN_OUT = false
        } else {
            text = "is Now On"
            IN_OUT = true
        }

        // intent + pendingIntent 설정
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        // channel 설정
        val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager
        createNotificationChannel(notificationManager)
        // notification 설정
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            setContentTitle(TITLE)
            setContentText(text)
            setAutoCancel(true)
            setPriority(NotificationManager.IMPORTANCE_HIGH)
            setContentIntent(pendingIntent)
        }
        // notification 표현
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }
}