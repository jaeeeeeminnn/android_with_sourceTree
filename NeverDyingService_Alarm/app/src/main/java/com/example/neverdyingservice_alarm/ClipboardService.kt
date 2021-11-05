package com.example.neverdyingservice_alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.lang.UnsupportedOperationException
import java.util.*

class ClipboardService : Service(), ClipboardManager.OnPrimaryClipChangedListener {

    private lateinit var mManager: ClipboardManager

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate")
        mManager = getSystemService(Context.CLIPBOARD_SERVICE)  as ClipboardManager
        mManager.addPrimaryClipChangedListener(this)
    }

    private val TAG = "ClipboardService"

    override fun onPrimaryClipChanged() {
        if (mManager.primaryClip != null) {
            val data : ClipData = mManager.primaryClip!!

            val dataCount = data.itemCount
            for (i in 0 until dataCount) {
                val item = data.getItemAt(i).coerceToText(this)

                Log.e(TAG, "clip data = item $item")
                Toast.makeText(this, "[Clipboard] $item", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yt implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")

        mManager.removePrimaryClipChangedListener(this)
        // 서비스 종료 시 1초 뒤 알람이 실행되도록 호출
        setAlarmTimer()
    }

    private fun setAlarmTimer() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.SECOND, 1)
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Start Service", Toast.LENGTH_LONG).show()
        return START_STICKY
    }
}