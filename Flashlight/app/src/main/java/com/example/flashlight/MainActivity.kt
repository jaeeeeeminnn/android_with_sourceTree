package com.example.flashlight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 3.c 서비스로 손전등 기능 옮기기
        /*  액티비티에서 직접 플래시를 켜는 구조
        
            Activity -> Torch 클래스
        
        // 작성한 Torch 클래스를 인스턴스화.
        val torch = Torch(this)

        flashSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> torch.flashOn()
                false -> torch.flashOff()
            }
        }
        */
        
        /*  서비스를 사용해 플래시를 켜는 구조
        
            Activity -> Service -> Torch 클래스

            서비스를 시작하려면 startService() 메소드를 사용.
            TorchService를 사용해 플래시를 켜는 인텐트에 "on" 액션을 설정하여 서비스를 시작.

            Intent(Context, Class)

         */
        val intent = Intent(this, TorchService::class.java)

        // 3.d 액티비티에서 서비스를 사용해 손전등 켜기
        flashSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> intent.action = "on"
                false -> intent.action = "off"
            }
            startService(intent)
        }
    }
}