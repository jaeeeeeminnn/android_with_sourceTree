package com.example.fifasearchrest.rest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

object HttpTask {

    // open api를 불러올 수 있게 하는 개발자 키
    // open api 사용 등록을 하고 받아와야 한다.
    private const val DEVELOP_KEY: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiOTg5ODgxMjYzIiwiYXV0aF9pZCI6IjIiLCJ0b2tlbl90eXBlIjoiQWNjZXNzVG9rZW4iLCJzZXJ2aWNlX2lkIjoiNDMwMDExNDgxIiwiWC1BcHAtUmF0ZS1MaW1pdCI6IjUwMDoxMCIsIm5iZiI6MTYxODY0MTA0NywiZXhwIjoxNjM0MTkzMDQ3LCJpYXQiOjE2MTg2NDEwNDd9.0uG4p2PPQsW-ADDWyJHWgBVnQtbNqyleTfERgNS7MOY"

    // 각각 open api에서 제공하는 데이터 습득 json 형식
    val DEAL_INFO_URL: String = "https://api.nexon.co.kr/fifaonline4/v1.0/users/{accessid}/markets?tradetype={tradetype}&offset={offset}&limit={limit}"
    val USER_INFO_URL: String = "https://api.nexon.co.kr/fifaonline4/v1.0/users?nickname={nickname}"
    var IMAGE_SPID_URL: String = "https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p{spid}.png"
    val SPID_META_URL: String = "https://static.api.nexon.co.kr/fifaonline4/latest/spid.json"
    
    // 이번 예제에서는 GET으로 통신.
    // POST는 추후 추가
    
    // src로 url을 얻어 요청한 내용을 json으로 받는 메소드
    fun getOrNull(src: String) : String? {
        var result: String? = null
        
        try {
            val url = URL(src)
            // URL 객체를 이용하여 tcp 연결을 하고 그걸 HttpURLConnection 타입으로 형변환
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

            // 현재 연결의 요청은 "GET"
            conn.requestMethod = "GET"
            // open api에서 원하는 대로 연결 헤더에 권한에 대한 속성을 보내 권한 인증.
            conn.setRequestProperty("Authorization", DEVELOP_KEY)

            // conn(연결)을 통해 오는 녀석을 StringBuilder로 얻어올 수 있게 하는 것.
            val `is`: InputStream = conn.inputStream
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
            var line : String?
            // StringBuilder로 현재 conn String인 line이 builder에 계속 이어붙임.
            // 들어오는 conn(json)이 모두 string이기 때문에 가능.
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            result = builder.toString()
        } catch (e: Exception) {
            Log.e("REST_API", "GET Method failed" + e.message)
            // 에러메시지 발생 근원지를 찾아서 단계별로 에러를 출력
            e.printStackTrace()
        }

        return result
    }

    // item_list.xml에 있는 item_image를 얻기 위한 메소드
    // Drawable이 아니라 Bitmap으로 얻음.
    // url과 open api에게서 받은 spid 필드를 이용하여 bitmap을 얻음.
    fun getBitmapOrNull(src:String, subSpid: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("Authorization", DEVELOP_KEY)
            // doInput : 연결이 데이터 수신을 허용하는지 여부
            connection.doInput = true
            // Http 연결
            connection.connect()

            // http 연결을 통해 image에 대한 inputstream을 얻음. 
            val input = connection.inputStream

            // BitmapFactory    : 파일 / 스트림 / 바이트 배열 과 같은 다양한 소스에서 Bitmap 객체를 만듬. 
            // decodeStream     : 입력 스트림을 비트맵으로 디코딩
            BitmapFactory.decodeStream(input)

        } catch(e : IOException) {
            // inputStream에 문제가 생기는 거니까 io Exception
            var nomalimage =
                "https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/players/p{spid}.png"
            var spid: String = subSpid.substring(3);

            if (spid.substring(0, 1).indexOf("0") != -1) {
                spid = subSpid.substring(4);
            }

            if (spid.substring(0, 1).indexOf("0") != -1 && spid.substring(0, 2)
                    .indexOf("0") != -1
            ) {
                spid = subSpid.substring(5);
            }

            if (spid.substring(0, 1).indexOf("0") != -1 && spid.substring(0, 2)
                    .indexOf("0") != -1 && spid.substring(0, 3).indexOf("0") != -1
            ) {
                spid = subSpid.substring(6);
            }

            nomalimage = nomalimage.replace("{spid}", spid)

            val url = URL(nomalimage)
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("Authorization", DEVELOP_KEY)
            connection.doInput = true
            connection.connect()

            val input = connection.inputStream

            BitmapFactory.decodeStream(input)
        } finally {
            // finally  : 해당 스택이 종료되어도 마지막에 호출된다.
            null
        }
    }
}