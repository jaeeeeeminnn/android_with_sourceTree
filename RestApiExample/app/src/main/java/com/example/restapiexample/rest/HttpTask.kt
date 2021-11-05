package com.example.restapiexample.rest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


object HttpTask {

    private const val DEVELOP_KEY: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiOTg5ODgxMjYzIiwiYXV0aF9pZCI6IjIiLCJ0b2tlbl90eXBlIjoiQWNjZXNzVG9rZW4iLCJzZXJ2aWNlX2lkIjoiNDMwMDExNDgxIiwiWC1BcHAtUmF0ZS1MaW1pdCI6IjUwMDoxMCIsIm5iZiI6MTYxODY0MTA0NywiZXhwIjoxNjM0MTkzMDQ3LCJpYXQiOjE2MTg2NDEwNDd9.0uG4p2PPQsW-ADDWyJHWgBVnQtbNqyleTfERgNS7MOY"

    val DEAL_INFO_URL: String = "https://api.nexon.co.kr/fifaonline4/v1.0/users/{accessid}/markets?tradetype={tradetype}&offset={offset}&limit={limit}"
    val USER_INFO_URL: String = "https://api.nexon.co.kr/fifaonline4/v1.0/users?nickname={nickname}"
    var IMAGE_SPID_URL: String = "https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p{spid}.png"
    val SPID_META_URL: String = "https://static.api.nexon.co.kr/fifaonline4/latest/spid.json"

    //이번 예제는 Get으로 통신 Post는 추후 추가해보길바람
    fun getOrNull(src: String): String? {
        var result: String? = null

        try {
            val url = URL(src)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", DEVELOP_KEY)

            val `is`: InputStream = conn.inputStream
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }

            result = builder.toString()
        } catch (e: Exception) {
            Log.e("REST_API", "GET method failed: " + e.message)
            e.printStackTrace()
        }

        return result
    }

    fun getBitmapOrNull(src: String, subSpid: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("Authorization", DEVELOP_KEY)
            connection.doInput = true
            connection.connect()

            val input = connection.inputStream

            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
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
            null
        }
    }
}