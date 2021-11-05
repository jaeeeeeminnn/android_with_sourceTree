package com.example.neverdyingservice

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActiviy"

    lateinit var mIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = ClipboardService::class.java
        mIntent = Intent(applicationContext, service)
        if (!this.isServiceRunning(service)) {
            Log.e(TAG, "Service is not running - START SERVICE")
            // App 실행 시 서비스(Clipboard Service)시작
            // App 실행 시 foreground 이므로 startService로 호출
            startService(mIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // App을 종료할 때 서비스(ClipboardService)를 종료
        stopService(mIntent)
    }

    fun Context.isServiceRunning(serviceClass : Class<*>) :Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.e(TAG, "Service is running - isServiceRunning")
                return true
            }
        }

        return false
    }
}