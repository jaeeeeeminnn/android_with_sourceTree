package com.example.weatherrest.rest

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

object HttpTask {

    // 개발자 인증키 (encoding)
    private const val DEVELOP_KEY: String = "zua1imi%2F74fkzPmdzhWhKPCRxrtbYbDwrvZO4H11utjZs7AgGAqQBvLQeChxhoPptfpFILnup3yobdLtC1Nmzg%3D%3D"

    val REQ_MESSAGE: String = "http://apis.data.go.kr/1360000/TourStnInfoService/getTourStnVilageFcst?serviceKey=$DEVELOP_KEY&numOfRows=10&pageNo=1&CURRENT_DATE=2016121010&HOUR=24&COURSE_ID=1"
    val REQ_MESSAGE2: String = "http://apis.data.go.kr/1360000/TourStnInfoService/getTourStnVilageFcst?serviceKey=$DEVELOP_KEY&pageNo=1&numOfRows=10&dataType=JSON&CURRENT_DATE=2019122010&HOUR=24&COURSE_ID=1"

    // HTTP GET요청으로 원하는 정보 (테마명, 지역명, 관광지명, 하늘상태, 기온, 강수확률)을
    // 얻을 수 있는 URL을 만들거임.
    fun getUrlOrNull(src: String) : String? {
        var result: String? = null

        try {
            // URL 객체로부터 url을 얻어옴.
            val url = URL(src)
            // 연결은 HTTPURLConnection 객체를 이용하여 연결
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

            // 연결 설정
            conn.requestMethod = "GET"

            // 연결과 url을 이용하여 원하는 json을 String으로 읽어들임.
            val `is`: InputStream = conn.inputStream
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            result = builder.toString()

        } catch(e : Exception) {
            Log.e("REST_API", "GET METHOD FAILED " + e.message)
            e.printStackTrace()
        }

        return result
    }
}