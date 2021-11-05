package com.example.neverdyingservice

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log

class BootCompleteReceiver : BroadcastReceiver() {

    private val TAG : String = "BootCompleteReceiver"

    // onReceive() 메소드는 BroadcastReceiver가 pendingIntent broadcast를 받을 때 호출
    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action

        Log.e(TAG, "Receive ACTION $action")
        if (action == null) {
            Log.e(TAG, "action is null")
            return
        }

        if (TextUtils.equals(action, Intent.ACTION_BOOT_COMPLETED)) {
            Log.e(TAG, "boot complete received")

            val serviceClass = ClipboardService::class.java
            val intent = Intent(context, serviceClass)

            // service가 실행 중이 아니라면
            if (!context!!.isServiceRunning(serviceClass)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.e(TAG, "Service is not running - START FORGROUND SERVICE")
                    // oreo 버전 이상이면 startForegroundService로 다시 서비스
                    context?.startForegroundService(intent)
                } else {
                    Log.e(TAG, "Service is not running - START SERVICE")
                    // oreo 버전 이하면 그냥 startService로 intent 서비스를 다시 시작해도 됨.
                    context?.startService(intent)
                }
            }
        }
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
