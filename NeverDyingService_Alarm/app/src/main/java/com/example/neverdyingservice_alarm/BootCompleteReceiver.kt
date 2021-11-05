package com.example.neverdyingservice_alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log

class BootCompleteReceiver :BroadcastReceiver() {

    private val TAG = "BootCompletedReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        Log.e(TAG, "Receive ACTION $action")
        if (action == null) {
            Log.e(TAG, "action is null")
            return
        }

        if (TextUtils.equals(action, Intent.ACTION_BOOT_COMPLETED)) {
            Log.e(TAG, "boot complete received")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context?.startForegroundService(Intent(context, RestartService::class.java))
            } else {
                context?.startService(Intent(context, RestartService::class.java))
            }
        }
    }
}