package com.example.fifasearchrest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fifasearchrest.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // task를 백그라운드로 이동
        moveTaskToBack(true)
        // activity 종료 + task list에서 지우기
        finishAndRemoveTask()
        System.exit(0)
    }
}