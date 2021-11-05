package com.example.vaccinationcenter.network

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

object HttpTask {

    private const val DEVELOP_KEY = "zua1imi%2F74fkzPmdzhWhKPCRxrtbYbDwrvZO4H11utjZs7AgGAqQBvLQeChxhoPptfpFILnup3yobdLtC1Nmzg%3D%3D"
    private const val SERVICE_KEY = "data-portal-test-key"
    private val REQ_MESSAGE = "https://api.odcloud.kr/api/15077586/v1/centers?page=1&perPage=10&serviceKey=$DEVELOP_KEY"
    const val TOTAL_COUNT = 284
    // 시/도 + 시/군/구 와 일치하는 애들만 넣어줌.
    val searchList: ArrayList<JsonObject> = arrayListOf()

    fun getUrl(pageIndex: String) : String {
        var result: String = REQ_MESSAGE
        return result.replace("page=1", "page=$pageIndex")
    }

    fun getResponse(src: String): String? {
        var response: String? = null
        try {
            val url = URL(src)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", DEVELOP_KEY)

            val inputStream: InputStream = conn.inputStream
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            response = builder.toString()


        } catch (e: Exception) {
            Log.e("HTTP_FAILED", "이유가 무엇인고 : " + e.message)
            e.printStackTrace()
        }
        return response
    }

    fun getJsonArray(jsonRes: String?) : JsonArray? {
        val jsonObject = JsonParser.parseString(jsonRes) as JsonObject
        return jsonObject.getAsJsonArray("data")
    }

    fun setWantedItem(jsonArray: JsonArray?, stateName: String, cityName: String) {
        for(i in 0 until jsonArray!!.size()) {
            val item = jsonArray!![i] as JsonObject
            val sido: String? = item.get("sido").asString
            val sigungu: String? = item.get("sigungu").asString

            if (sido == stateName && sigungu == cityName) {
                searchList.add(item)
            }
        }
    }

}