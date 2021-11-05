package com.example.serviceexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class StartedService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SERVICE_TEST", "Started Service get started.")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("SERVICE_TEST", "Started Service get Stopped")
        super.onDestroy()
    }

}