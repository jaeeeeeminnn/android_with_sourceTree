package com.example.threadexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 두 개의 runnable은 동시에 실행되는 것인가?
        // 그렇다. 근데 순서는 계속 바뀐다.
        thread(start = true) {
            val runnableTest1 = RunnableTest().run()
            val runnableTest2 = RunnableTest().run()
        }

        // thread와 runnable 차이
        // 이 때는 차이가 없다.
        thread(start = true) {
            val threadTest = ThreadTest().run()
            runOnUiThread {
                main_textview.text = "과일"
            }
            Thread.sleep(2500)
            val runnableTest = RunnableTest().run()
            runOnUiThread {
                main_textview.text = "음료"
            }
        }
    }
}