package com.example.neverdyingservice_alarm

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    lateinit var mIntent : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceClass = ClipboardService::class.java
        mIntent = Intent(this, serviceClass)
        if (!this.isServiceRunning(serviceClass)) {
            Log.e(TAG, "Service is not running - START SERVICE")
            // App 실행 시 서비스 시작
            // App 실행 시 foreground이므로 startService 호출
            startService(mIntent)
        }
    }

    fun Context.isServiceRunning(serviceClass: Class<ClipboardService>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.e(TAG, "Service is running")
                return true
            }
        }
        return false
    }
}