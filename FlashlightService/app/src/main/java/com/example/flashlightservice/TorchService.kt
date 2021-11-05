package com.example.flashlightservice

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TorchService : Service() {

    // by lazy  : Torch 클래스의 인스턴스를 얻는 방법
    // torch 객체를 처음 사용할 때 인스턴스화 됨.
    private val torch: Torch by lazy {
        Torch(this)
    }

    private var isRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // intent에 내가 원하는 동작을 설정해서 onStartCommand()(서비스 시작)을 수행함.
        // 따라서 intent.action 속성을 이용하여 동작 정의
        when (intent?.action) {
            "on" -> {
                torch.flashOn()
                isRunning = true
            }
            "off" -> {
                torch.flashOff()
                isRunning = false
            }
            else -> {
                // 상태 바꿔주고
                isRunning = !isRunning
                if (isRunning) {
                    torch.flashOn()
                } else {
                    torch.flashOff()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
        /**
         * onStartCommand() 메소드는 서비스가 강제종료될 때 복구하는 방식을 return
         *
         * - START_STICKY           : null intent로 다시 시작.   (미디어 플레이어 적합)
         * - START_NOT_STICKY       : 다시 시작하지 않음.
         * - START_REDELIVER_INTENT : 마지막 intent로 다시 시작함. (파일 다운로드 적합)
         */
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}