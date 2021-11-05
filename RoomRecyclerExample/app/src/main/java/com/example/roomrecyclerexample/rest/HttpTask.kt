package com.example.roomrecyclerexample.rest

import android.util.Log
import android.view.View
import com.example.roomrecyclerexample.model.CourseModel
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_course.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    // json 요청 url
    val REQ_MESSAGE: String = "http://apis.data.go.kr/1360000/TourStnInfoService/getTourStnVilageFcst?serviceKey=$DEVELOP_KEY&pageNo=1&numOfRows=10&dataType=JSON&CURRENT_DATE=2019122010&HOUR=24&COURSE_ID=1"

    fun getCourseName(dataArray: JsonArray): String {
        return dataArray[0].asJsonObject.get("courseName").asString
    }

    fun getModelList(dataArray: JsonArray) : ArrayList<CourseModel> {
        val list: ArrayList<CourseModel> = arrayListOf()
        for (i in 0 until dataArray.size()) {
            val curr = dataArray[i] as JsonObject
            val course = CourseModel(
                i + 1,
                curr.get("thema").asString,
                curr.get("spotName").asString,
                curr.get("pop").asString,
                curr.get("sky").asString,
                curr.get("th3").asString
            )
            list.add(course)
        }
        return list
    }

    fun getDataArray(response: String?) : JsonArray {
        val data = (((JsonParser.parseString(response) as JsonObject)
            .getAsJsonObject("response") as JsonObject)
            .getAsJsonObject("body") as JsonObject)
            .getAsJsonObject("items") as JsonObject
        return data.getAsJsonArray("item")

    }

    fun getJsonResponse(courseNum : String) : String? {
        var response: String? = null

        // url을 원하는 형태로 바꿈.
        val url = replaceUrl(courseNum)
        response = getUrlorNull(url)

        return response
    }

    fun checkNoData(response: String?) : Boolean{
        var result :Boolean = true
        if (response != null){
            val resultCode = (((JsonParser.parseString(response) as JsonObject)
                .getAsJsonObject("response") as JsonObject)
                .getAsJsonObject("header") as JsonObject)
                .getAsJsonPrimitive("resultCode").asString
            Log.d("RESULTCODE", "$resultCode")
            if (resultCode == "03") {
                result = false
            }
        } else {
            result = false
        }
        return result
    }

    private fun getUrlorNull(src : String) : String? {
        var result: String? = null

        try {
            // url 읽어서 result에 붙여넣기
            val url = URL(src)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            val inputStream: InputStream = conn.inputStream
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            result = builder.toString()

        } catch (e : Exception) {
            Log.e("HTTP_ERR", "http connection failed"+ e.message)
            e.printStackTrace()
        }

        return result
    }

    private fun replaceUrl(courseNum: String) : String {
        var request = REQ_MESSAGE
        return request.replace("COURSE_ID=1", "COURSE_ID=$courseNum")
    }

}

