package com.example.neverdyingservice_alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // this method is called when the BroadcastReceiver is receving an Intent broadcast.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Oreo 이상이면 RestartService를 foreground service로 실행
            val intent = Intent(context, RestartService::class.java)
            context?.startForegroundService(intent)
        } else {
            // Oreo 이전이면 그냥 startService
            val intent = Intent(context, RestartService::class.java)
            context?.startService(intent)
        }
    }
}