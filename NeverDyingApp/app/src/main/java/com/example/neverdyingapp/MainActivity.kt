package com.example.neverdyingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.BundleCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        findViewById<Button>(R.id.main_stop).setOnClickListener {
            Log.d("NEVER_DIE", "This is onStart()")
            onStop()
        }
    }

    /**
     * 1. onPause -> onResume() -> onPause -> ...
     */
    override fun onResume() {
        Log.d("NEVER_DIE", "This is onResume()")
        super.onResume()
        findViewById<Button>(R.id.main_pause).setOnClickListener {
            onPause()
        }
    }

    override fun onPause() {
        super.onResume()
        Log.d("NEVER_DIE", "This is onPause()")
        super.onPause()
    }

    override fun onStop() {
        onRestart()
        Log.d("NEVER_DIE", "This is onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("NEVER_DIE", "This is onDestroy()")
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("NEVER_DIE", "This is onRestart()")
    }
}