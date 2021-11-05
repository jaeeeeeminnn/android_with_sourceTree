package com.example.flashlight

import android.app.Service
import android.content.Intent
import android.os.IBinder

// TorchService 클래스(서비스)는 Service 클래스를 상속받는다.
class TorchService : Service() {

    // TorchService 서비스는 Torch 클래스를 사용해야 함.
    // Torch 클래스의 인스턴스를 얻는 방법은
    // 1. onCreate() 메소드를 사용하는 방법
    // 2. by lazy 를 사용하는 방법
    // by lazy는 초기화 지연 방법으로 torch 객체를 처음 사용할 때 초기화 됨.
    private val torch : Torch by lazy {
        Torch(this)
    }

    // TorchSErvice는 인텐트에 on / off 액션을 지정하여 켜거나 껐음.
    // 그러나 위젯에서는 어떤 경우가 on인지 off인지 알 수 없기 때문에 액션 지정이 안 됨.
    // 액션이 지정되지 않아도 플래시가 동작하도록 TorchSercvice.kt 파일 수정.
    // 플래시가 켜졌는지 꺼졌는지 알기 위해 isRunning 변수 추가.
    private var isRunning = false

    // 외부에서 startService() 메소드로 TorchService 서비를 호출하면
    // onStartCommand() 콜백 메소드가 호출됨. 
    // 보통 intent에 action 값을 설정하여 호출
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            // 앱에서 실행할 경우
            "on" -> {
                torch.flashOn()
                isRunning = true
            }
            "off" -> {
                torch.flashOff()
                isRunning = false
            }
            // 서비스에서 실행할 경우
            else -> {
                isRunning = !isRunning
                if (isRunning) {
                    torch.flashOn()
                } else {
                    torch.flashOff()
                }
            }
        }
        /*  서비스가 시스템에 의해 강제 종료되었을 때 onStartCommand() 메소드 반환값

            START_STICKY
            : null 인텐트로 다시 시작.
              명령을 실행하지는 않지만 무기한으로 실행 중.
              작업을 기다리고 있는 미디어 플레이어와 비슷한 경우에 적합.
            START_NOT_STICKY
            : 다시 시작하지 않음.
            START_REDELIVER_INTENT
            : 마지막 인텐트로 다시 시작.
              능동적으로 수행 중인 파일 다운로드와 같은 서비스에 적합.

            일반적으로 super.onStartCommand() 호출인 경우에는
            내부적으로 START_STICKY를 반환.

         */
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}