package com.example.threadexample

import android.util.Log

class RunnableTest : Runnable {
    override fun run() {
        val beverage: ArrayList<String> = arrayListOf("콜라", "사이다", "환타", "커피")

        for (i in 0 until beverage.size){
            Log.d("RUNNABLETEST", "${beverage[i]}")
        }
    }
}