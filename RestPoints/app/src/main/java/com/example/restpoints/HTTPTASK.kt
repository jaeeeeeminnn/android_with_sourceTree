package com.example.restpoints

import android.util.Log
import com.example.restpoints.entity.RestpointEntity
import com.example.restpoints.entity.RouteNameEntity
import com.example.restpoints.model.RestpointModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

object HTTPTASK {

    object INFO {
        private val AUTH_KEY : String = "1761329781"
        val REQUEST : String = "http://data.ex.co.kr/openapi/restinfo/restWeatherList?key=$AUTH_KEY&type=json&sdate=USER_DATE&stdHour=10"
    }

    fun getSrc(date : String) : String {
        var src : String = INFO.REQUEST
        src = src.replace("USER_DATE", date)
        return src
    }

    fun getResponse(src : String) : String? {
        var response : String? = null

        try {
            val url = URL(src)
            val conn : HttpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream : InputStream = conn.inputStream
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var line : String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            response = builder.toString()
        } catch (e : Exception) {
            Log.e("REST_POINTS", "${e.message}")
            e.printStackTrace()
        }
        return response
    }

    fun getJsonArray(response : String?) : JsonArray? {
        if (response == null) {
            Log.d("REST_POINTS", "Json Response is null")
        } else {
            val jsonObj : JsonObject = JsonParser.parseString(response) as JsonObject
            return jsonObj.getAsJsonArray("list")
        }
        return null
    }

    /**
     * getEntityList
     *
     * @param jsonArray
     *
     * jsonArray에서 도로명과 일치하는 결과를 추출하여 entity에 넣고 list 반환
     */
    fun getEntityList(jsonArray : JsonArray, destination : String) : List<RestpointEntity> {
        for (i in 0 until jsonArray.size()) {
            val jsonObj : JsonObject = jsonArray[i] as JsonObject
            if (jsonObj.get("routeName").equals(destination)) {
                if (DEFINES.DBTASK.DATABASE_NAME_RP != null) {
                    DEFINES.DBTASK.DATABASE_RP!!.RestpointDao().insert(
                        RestpointEntity(
                            jsonObj.get("unitCode").asString,
                            jsonObj.get("unitName").asString,
                            jsonObj.get("routeName").asString,
                            jsonObj.get("xValue").asString,
                            jsonObj.get("yValue").asString,
                            jsonObj.get("addr").asString,
                            jsonObj.get("weatherContents").asString,
                            jsonObj.get("tempValue").asString,
                            jsonObj.get("highestTemp").asString,
                            jsonObj.get("lowestTemp").asString,
                            jsonObj.get("rainfallValue").asString,
                            jsonObj.get("snowValue").asString,
                            jsonObj.get("windContents").asString,
                            jsonObj.get("windValue").asString,
                        )
                    )
                } else {
                    Log.d("REST_POINTS", "getEntityList -> DB_RP is null")
                }
            }
        }
        return DEFINES.DBTASK.DATABASE_RP!!.RestpointDao().findAll()
    }
}