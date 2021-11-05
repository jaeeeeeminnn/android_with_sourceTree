package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    // 0.01 초마다 1증가
    private var time: Int = 0
    // start할 건지 / pause할 건지에 관한 변수
    private var isRunning: Boolean = false
    private var timeTask: Timer? = null
    // 몇 번째 랩
    private var lap: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 시작 / 일시정지 이벤트 구현
        fab.setOnClickListener {
            // fab 버튼이 클릭되었다면

            // 현재 실행중임으로 초기화
            isRunning = !isRunning

            // 실행할 거면 start / 멈출거면 pause
            if (isRunning) {
                start()
            } else {
                pause()
            }
        }

        lapButton.setOnClickListener {
            recordLapTime()
        }

        resetFab.setOnClickListener {
            reset()
        }
    }

    private fun start() {
        // 타이머를 시작시키는 fab을 누르면 fab의 이미지를 일시정지 이미지로 변경.
        fab.setImageResource(R.drawable.ic_baseline_pause_24)

        // timer : 일정시간 주기로 반복하는 동작 수행할 때 사용
        // period = 1000 : 1초(1000ms)간격으로 동작 수행

        // timer 취소 시 Timer 객체를 변수에 저장하여 timer 실행과 반환을 보관
        timeTask = timer(period = 1000) {
            time++
            val sec = time / 100
            val milli = time % 100
            // runOnUiThread : 워커 스레드
            // 오래걸리는 작업을 보이지 않는 곳에서 처리하는 스레드 (메인 스레드와 대척점)
            // 워커 스레드는 UI 조작이 불가능하므로 runOnUiThread로 감싸서 UI 조작을 가능하게 함.
            runOnUiThread {
                // UI 조작
                secTextView.text = "$sec"
                milliTextView.text = "$milli"
            }
        }
    }

    private fun pause() {
        // fab 이미지를 다시 시작 이미지로 변경
        fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        // 실행중인 timer를 취소
        timeTask?.cancel()
    }

    private fun recordLapTime() {
        // Scroll View 내부에 있는 LinearLayout에 동적으로 Text View를 추가
        // addView 메소드를 사용하여 추가 가능

        // 현재 시간을 기록
        val lapTime = this.time
        // 현 linear layout에 동적으로 text view를 만들기 위해 text view 초기화
        val textView = TextView(this)
        textView.text = "$lap LAP : ${lapTime / 100}.${lapTime%100}"


        // linear layout은 기본적으로 위에서 아래로 쌓임.
        // 가장 최근 기록이 위에 쌓일 수 있도록 addView 파라미터를 설정 (0 으로)

        // 맨 위에 랩 타입 추가
        lapLayout.addView(textView, 0)
        lap++
    }

    // 타이머 초기화 메소드
    private fun reset() {
        // 실행 중인 타이머 취소
        timeTask?.cancel()

        // 모든 변수 초기화
        time = 0
        isRunning = false
        timeTask = null
        lap = 1

        // 화면에 표시되는 항목 초기화
        fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        secTextView.text = "0"
        milliTextView.text = "00"

        // 모든 랩타임 제거
        lapLayout.removeAllViews()
    }
}