package com.example.serviceexample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class BindingService : Service() {
    override fun onBind(intent: Intent): IBinder {
        Log.d("SERVICE_TEST", "Binding Service get started")
        return binder
    }

    inner class ServiceBinder: Binder() {
        fun getService() : BindingService {
            return this@BindingService
        }
    }

    fun bindServiceTest() : String {
        return "binding Service is going well"
    }

    val binder = ServiceBinder()

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Log.d("SERVICE_TEST", "Binding Service get started")
//        return super.onStartCommand(intent, flags, startId)
//    }

    override fun onDestroy() {
        Log.d("SERVICE_TEST", "Binding Service get stopped")
        super.onDestroy()
    }
}