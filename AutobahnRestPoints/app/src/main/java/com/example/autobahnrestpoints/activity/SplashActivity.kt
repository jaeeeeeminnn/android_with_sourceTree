package com.example.autobahnrestpoints.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.autobahnrestpoints.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // DB에 count가 0인지 여부 판단
        // DB 작업

        Log.d("LOG", "All DB Ready is done")
        // main acitivity 실행
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        finishAndRemoveTask()
        System.exit(0)
    }
}