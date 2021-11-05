package com.example.roomrecyclerexample.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.roomrecyclerexample.Entity.Course
import com.example.roomrecyclerexample.R
import com.example.roomrecyclerexample.database.CourseDatabase
import com.example.roomrecyclerexample.fragment.CourseFragment
import com.example.roomrecyclerexample.rest.HttpTask
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.fragment_course.*
import kotlinx.coroutines.*
import kotlin.collections.listOf as listOf

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        finishAndRemoveTask()
        System.exit(0)
    }
}