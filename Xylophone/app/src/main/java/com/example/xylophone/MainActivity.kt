package com.example.xylophone

import android.content.pm.ActivityInfo
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    // SoundPool 객체 초기화
    private val soundPool = SoundPool.Builder().setMaxStreams(8).build()

    // listOf() 함수를 사용하여 textview의 id와 음원파일의 리소스 id를 연관지은 Pair 객체 8개를
    // 리스트 객체 sounds로 만듬.
    // Pair 클래스는 두 개의 연관된 객체를 저장
    private val sounds = listOf(
        Pair(R.id.do1, R.raw.do1),
        Pair(R.id.do2, R.raw.do2),
        Pair(R.id.re, R.raw.re),
        Pair(R.id.mi, R.raw.mi),
        Pair(R.id.fa, R.raw.fa),
        Pair(R.id.sol, R.raw.sol),
        Pair(R.id.la, R.raw.la),
        Pair(R.id.si, R.raw.si),
    )

    /*  load(Context, resId, priority)

        음원을 준비하여 id를 반환
         - context  : 액티비티
         - resId    : 재생할 raw 디렉터리의 소리 파일 리소스
         - priority : 우선순위 지정. 숫자가 클수록 우선순위 높음

        play(soundId, leftVolume, rightVolume, priority, loop, rate)

        음원을 재생
        - soundId   : load() 메소드에서 반환된 음원의 id
        - leftVolume: 왼쪽 볼륨을 0.0 ~ 1.0 사이에서 지정
        - rightVolume:오른쪽 볼륨을 0.0 ~ 1.0 tkdldptj wlwjd
        - priority  : 우선순위 지정.
        - loop      : 반복 지정. 0이면 반복 x, -1이면 반복
        - rate      : 재생속도 지정. 1.0이면 보통, 0.5이면 0.5배속
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1.a 가로모드 고정하기
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // sounds 리스트를 forEach() 함수를 이용하여 요소를 하나씩 꺼내서 tune() 메소드에 전달
        sounds.forEach{ tune(it) }

    }

    // tune() 메소드는 Pair 객체를 받아서 load() 메소드로 음원의 id를 얻음.
    private fun tune(pitch: Pair<Int, Int>) {
        // load() 메소드로 음원의 id를 얻음
        val soundId = soundPool.load(this, pitch.second, 1)
        // findViewById() 메소드로 텍스트 뷰의 id에 해당하는 뷰를 얻음
        findViewById<TextView>(pitch.first).setOnClickListener {
            //텍스트 뷰를 클릭했을 때 음원을 재생
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}