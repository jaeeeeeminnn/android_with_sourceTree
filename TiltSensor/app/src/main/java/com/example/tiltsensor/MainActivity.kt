package com.example.tiltsensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast

/*  센서 사용 준비

    1. SensorManager 인스턴스를 얻는다.
    2. 센서 중 하나를 getDefaultSensor() 메소드에 지정하여 Sensor 객체를 얻는다.
    3. onResume() 메소드에서 registerListener() 메소드로 센서 감지 등록.
    4. onPause() 메소드에서 unregisterListener() 메소드로 센서 감지 해제.

 */
// MainActivity에 이미 listener가 있어야 sensor registerlistener가 가능.
class MainActivity : AppCompatActivity(),SensorEventListener {
    
    // TiltView를 생성자를 사용해 인스턴스화하여 화면 배치
    // 늦은 초기화로 메모리 절약
    private lateinit var tiltView: TiltView

    // 1. SensorManager 인스턴스를 얻는다.
    // sensor_service에 대한 문맥으로 system service가 sensor_service를 수행한다는 것을 알아낸다.
    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 화면이 꺼지지 않게 고정
        // window.addFlags() : 액티비티 상태를 지정 (flag 이용)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 가로모드로 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        super.onCreate(savedInstanceState)

        // onCreate() 메소드에서 생성자에 this를 넘겨 TiltView를 초기화
        tiltView = TiltView(this)
        // 기존의 R.layout.activity_main 대신에 tiltView를 setContentView() 메소드에 전달
        // setContentView(R.layout.activity_main)
        // 이제 tiltView가 전체 레이아웃이 됨.
        setContentView(tiltView)
    }

    // 3. onResume() 메소드에서 registerListener() 메소드로 센서 감지 등록
    // 액티비티가 동작할 때만 센서가 동작해야 배터리 효율이 좋으므로
    // onCreate() 메소드가 아니라 onResume() 메소드에서 센서감지 등록.
    override fun onResume() {
        super.onResume()
        // 2. 센서 하나를 getDefaultSensor() 메소드에 지정하여 Sensor 객체를 얻음
        // registerListener(listener, Sensor, Sampling period)
        // listener를 this로 지정했기 때문에 액티비티에서 센서값을 받아야 함.
        // 그래서 MainActivity의 타입 중 하나가 SensorListener인 것.
        val accSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    // 4. onPause() 메소드에서 unregisterListener() 메소드로 센서 감지 등록 해제
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    
    

    // 센서값이 변경되면 호출.
    // SensorEvent 객체에 센서가 측정한 값과 정보가 넘어옴.
    /*
        values[0] : x축 값    ; 위 기울임(-10~0)      /   아래 기울임(0~10)
        values[1] : y축 값    ; 왼쪽 기울임 (-10~0)    /   오른쪽 기울임(0~10)
        values[2] : z축 값    ; 미사용
     */
    override fun onSensorChanged(event: SensorEvent?) {
        // let {} : 함수를 호출하는 객체를 이어지는 블록의 인자로 넘기고, 블록의 결과값 반환
        // apply() : 함수를 호출하는 객체를 이어지는 블록의 리시버로 전달하고, 객체 자체를 반환
        // run()   : 인자가 없는 익명함수로 동작 / 객체에서 호출
        event?.let {
            // Log.e()  : 에러 표시
            // Log.w()  : 경고 표시
            // Log.d()  : 정보성 로그 표시
            // Log.v()  : 모든 로그 표시
            // tag      : 필터링을 위해 사용
            // massage  : 출력할 메시지
            Log.d("MainActivity", "OnSensorChanged: x = " + "${event.values[0]}," +
                    " y = ${event.values[1]}, z = ${event.values[2]}")

            // 액티비티에서 센서값이 변경되면 onSensorChanged() 메소드가 호출되므로
            // 여기서 TiltView에 센서값을 전달.
            tiltView.onSensorEvent(event)
        }
    }

    // 센서 정밀도가 변경되면 호출.
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //Toast.makeText(this,"hellow world", Toast.LENGTH_SHORT).show()
    }
}
