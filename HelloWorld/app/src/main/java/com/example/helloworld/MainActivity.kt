package com.example.helloworld

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var index: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clickButton.setOnClickListener {
            Toast.makeText(this, "공룡세상에 오신것을 환영합니다", Toast.LENGTH_LONG).show()

            when(index) {
                false -> {
                    replacePicture(true, R.drawable.dinosour)
                }
                true -> {
                    replacePicture(false, R.drawable.nodinosour)
                }
            }

        }
    }

    /**
     * 이 함수는 공룡을 바꿀때 쓰는 함수이다.
     *
     * */
    private fun replacePicture( isChange: Boolean, drawableIndex: Int) {
        runOnUiThread {
            index = isChange
            dinosourImg.setImageResource(drawableIndex)
        }
    }
}
