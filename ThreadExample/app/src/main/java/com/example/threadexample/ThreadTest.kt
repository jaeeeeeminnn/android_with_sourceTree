package com.example.threadexample

import android.util.Log

class ThreadTest : Thread() {
    override fun run() {
        super.run()

        val fruit: ArrayList<String> = arrayListOf("사과", "딸기", "포도", "배")

        for (i in 0 until fruit.size) {
            Log.d("THREADTEST", "${fruit[i]}")
        }
    }
}