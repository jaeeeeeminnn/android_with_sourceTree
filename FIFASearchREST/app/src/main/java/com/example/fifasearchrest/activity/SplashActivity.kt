package com.example.fifasearchrest.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fifasearchrest.DEFINES
import com.example.fifasearchrest.R
import com.example.fifasearchrest.rest.HttpTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONTokener

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        
        // 필요한 메타 데이터를 미리 받는다. 
        // Dispatchers.Main + launch = 동기
        CoroutineScope(Dispatchers.Main).launch { 
            var response: String? = null
            
            // withContext + Dispatchers.Default = 비동기
            // 비동기로 src를 주고 http 연결로 원하는 메타 데이터를 가져옴.
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                response = HttpTask.getOrNull(HttpTask.SPID_META_URL)
            }

            // 받은 json 배열을 처리하는 json 규칙
            val jsonArray = JSONTokener(response).nextValue() as JSONArray
            val staticMap : MutableMap<String, String> = mutableMapOf()

            // 얻은 메타데이터를 map으로 저장
            // spid랑 name이랑 찾기 쉽도록
            for (i in 0 until jsonArray.length()) {
                staticMap.put(
                    jsonArray.getJSONObject(i).getString("id"),
                    jsonArray.getJSONObject(i).getString("name")
                )
            }

            // 메타데이터 object로 바인딩
            DEFINES.SPID_META_DATA = staticMap
            // 받은 메타데이터로 뷰를 변형
            nextAcitivity()
            
        }
    }

    // 메타데이터가 적용된 intent를 이용하여 view 반영
    private fun nextAcitivity() {
        // 실행중에 ui를 변형할 수 있도록 함.
        runOnUiThread {
            // mainActivity의 ui를 변형할 수 있음.
            val intent = Intent(this, MainActivity::class.java)
            // 바꾼걸 반영해서 activity를 재실행
            startActivity(intent)
            // 해당 블록을 탈출함.
            this.finish()
        }
    }
}

