package com.example.serviceexample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.Switch
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var switch: Switch

    /**
     * Started Service
     */

    // Service 시작하는 메소드
    fun serviceStart() {
        // intent : 수행할 작업에 대한 추상적인 설명. (activity 사이의 접착제)
        val intent = Intent(this, StartedService::class.java)
        startService(intent)
    }

    // Service 종료하는 메소드
    fun serviceStop() {
        val intent = Intent(this, StartedService::class.java)
        stopService(intent)
    }

    /**
     * Binding Service
     */
    private var bindingService : BindingService? = null
    private var isConService = false

    // 바인딩 서비스와의 연결을 위한 connection 생성.
    private val serviceConnection = object: ServiceConnection {
        // onServiceConnected() : 서비스와 연결이 이뤄지면 호출되는 메소드
        // 여기에 연결된 서비스를 가져올 수 있도록 해야 함.
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val b = service as BindingService.ServiceBinder
            bindingService = b.getService()
            isConService = true
        }

        // onServiceDisconnected() : 서비스가 비정상적으로 종료되는 경우만 호출
        // 따라서 종료 제어를 위해서 임의의 변수(isConService ; 서비스와 연결중?)를 생성.
        override fun onServiceDisconnected(name: ComponentName?) {
            isConService = false
        }
    }
    // 서비스의 바인딩을 수행하는 메소드 추가.
    // BIND_AUTO_CREATE : 메소드 호출 시점에 서비스 생성되어 있으면 생성된 서비스로 바인딩 수행.
    //                    생성된 서비스가 없으면 새로운 서비스 생성 후 해당 서비스로 바인딩 수행.
    fun serviceBind() {
        val intent = Intent(this, BindingService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    // 서비스 연결 종료 메소드
    // isConService로 생성되지 않은 서비스를 죽이는 일이 없도록 함.
    fun serviceUnBind() {
        if (isConService == true) {
            unbindService(serviceConnection)
            isConService = false
        }
    }
    
    // 테스트 메소드
    fun callBindServiceTest() {
        if (isConService == true) {
            val result = bindingService?.bindServiceTest()
            Log.d("SERVICE_TEST", "value = $result")
        } else {
            Log.d("SERVICE_TEST", "No Service")
        }
    }

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switch = startedOrBinding

        if (switch.isChecked) {

            val btnStart: Button = btnServiceStart
            val btnStop : Button = btnServiceStop

            btnStart.setOnClickListener {
                serviceStart()
            }
            btnStop.setOnClickListener {
                serviceStop()
            }
        } else {

            val btnBinding: Button = btnServiceBinding
            val btnCalling: Button = btnServiceCall
            val btnUnBinding : Button = btnServiceUnBinding

            btnServiceBinding.setOnClickListener {
                serviceBind()
            }
            btnServiceCall.setOnClickListener {
                callBindServiceTest()
            }
            btnServiceUnBinding.setOnClickListener {
                serviceUnBind()
            }
        }

    }
}