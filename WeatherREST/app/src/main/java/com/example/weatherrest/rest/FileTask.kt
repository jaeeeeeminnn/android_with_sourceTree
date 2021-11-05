package com.example.weatherrest.rest

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.Exception
import kotlin.text.StringBuilder

object FileTask {

    // app은 휴대전화의 내부/외부 저장소만 접근할 수 있으므로 데스크탑 경로로 설정하면 안 됨.
    // 에뮬레이터는 vm에서 구동하기 때문에 된다.
    // private const val PATH: String = "C:\\Users\\telecons\\AndroidStudioProjects\\WeatherREST\\app\\src\\main\\java\\com\\example\\weatherrest\\resource\\items.txt"

    // 외부저장소 사용자 권한 설정 메소드
    fun exteranlStorageAuth() {
        // 권한 허용 x
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this))
//        }
    }
    // 사용자의 권한허용 응답 전달 메소드


    fun fileToArray(sArray: Array<String>) : Array<String> {
        var outSArray: Array<String> = arrayOf()

        try {
            CoroutineScope(Dispatchers.Main).launch {
                val file = File(Environment.getExternalStorageDirectory(), "items.txt")

                val reader = FileReader(file)
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {

                    val buffer = BufferedReader(reader)
                    var temp = ""
                    val result = StringBuilder()
                    while (true) {
                        temp = buffer.readLine()
                        if (temp == null) {
                            break
                        } else {
                            result.append(buffer)
                        }
                    }
                    buffer.close()

                    var inpuText = result.toString()
                }
            }

            val s = " "
        } catch (e : Exception) {
            Log.e("FILE_ERR", "${e.message}")
            e.printStackTrace()
        }

        return outSArray
    }
}