package com.example.restapiexample.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.restapiexample.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true); // 태스크를 백그라운드로 이동
        finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
        System.exit(0);
    }

}