package com.example.bmicalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import android.preference.PreferenceManager as PreferenceManager1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 이전에 입력한 값을 읽어오기
        loadData()

        resultButton.setOnClickListener {
            // activity_main.xml에서 만든 결과 버튼이 클릭되면 실행되는 부분

            // 다음 엑티비티로 전환하기 전에 현재의 키/몸무게를 저장
            saveData(heightEditText.text.toString().toInt(), weightEditText.text.toString().toInt())

            // 다음 엑티비티로 넘어가는 코드
            startActivity<ResultActivity>(
                "weight" to weightEditText.text.toString(),
                "height" to heightEditText.text.toString()
            )
        }
    }
    // MainActivity.kt에 키와 몸무게를 저장하는 메소드
    private fun saveData (height: Int, weight: Int) {
        val pref = android.preference.PreferenceManager.getDefaultSharedPreferences(this)
        // pref 객체의 editor 객체를 얻음
        // editor 객체를 이용해 pref에 데이터를 담을 수 있다.
        val editor = pref.edit()

        editor.putInt("KEY_HEIGHT", height)
        editor.putInt("KEY_WEIGHT", weight)
        editor.apply()
    }
    // 저장한 데이터를 불러오는 메소드
    private fun loadData() {
        // preference 객체를 얻음
        val pref = android.preference.PreferenceManager.getDefaultSharedPreferences(this)
        val height = pref.getInt("KEY_HEIGHT", 0)
        val weight = pref.getInt("KEY_HEIGHT", 0)

        if (height != 0 && weight != 0) {
            heightEditText.setText(height.toString())
            weightEditText.setText(weight.toString())
        }
    }
}