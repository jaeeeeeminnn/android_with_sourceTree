package com.example.restapiexample.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.restapiexample.DEFINES
import com.example.restapiexample.R
import com.example.restapiexample.rest.HttpTask
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

        //필요한 메타 데이터를 미리 받는다
        CoroutineScope(Dispatchers.Main).launch {
            var response: String? = null

            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                response = HttpTask.getOrNull(HttpTask.SPID_META_URL)
            }

            val jsonArray = JSONTokener(response).nextValue() as JSONArray
            val staticMap: MutableMap<String, String> = mutableMapOf()

            for (i in 0 until jsonArray.length()) {
                staticMap.put(
                    jsonArray.getJSONObject(i).getString("id"),
                    jsonArray.getJSONObject(i).getString("name")
                )
            }

            DEFINES.SPID_META_DATA = staticMap
            nextActivity()
        }
    }

    private fun nextActivity() {
        runOnUiThread {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}