package com.example.autobahnrestpoints.http

import android.util.Log
import com.example.autobahnrestpoints.DEFINES
import com.example.autobahnrestpoints.model.RestPointModel
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

/**
 * @property AUTH_KEY
 * @property REQ_MESSAGE
 *
 * @function getUrl         : str(default) -> str(wanted)
 * @function getResponse    : str -> json Object
 */

object HttpTask {

    private const val AUTH_KEY = "5986964392"
    private val REQ_MESSAGE =
        "http://data.ex.co.kr/openapi/restinfo/restWeatherList?key=$AUTH_KEY&type=json&sdate=20210101&stdHour=10"

    // request message replacing
    fun getUrl(date: String, hour: String): String {
        var result: String = REQ_MESSAGE
        result = result.apply {
            replace("sdate=20210101", "sdate=$date")
            replace("stdHour=10", "stdHour=$hour")
        }
        return result
    }

    // GET json object
    fun getResponse(src: String): String? {
        var response: String? = null

        try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val inputStream: InputStream = connection.inputStream
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            response = builder.toString()

        } catch (e: Exception) {
            Log.e("LOG", "Http_Failed : " + e.message)
            e.printStackTrace()
        }
        return response
    }

    // json parsing
    fun getJsonArray(jsonRes: String?): JSONArray? {
        val jsonObject = JsonParser.parseString(jsonRes) as JSONObject
        return jsonObject.getJSONArray("list")
    }

    // get routeName from json array
    fun getRouteName(jsonObject: JSONObject): String? {
        val result: String? = jsonObject.getString("routeName")
        if (result == null) {
            Log.e("LOG", "route name is null")
        }
        return result
    }

    // json object -> model
    fun addModelList(jsonObject: JSONObject) {
        DEFINES.searchResultList.add(
            RestPointModel(
                jsonObject.getString("unitCode"),
                jsonObject.getString("unitName"),
                jsonObject.getString("addr"),
                jsonObject.getString("routeName"),
                jsonObject.getString("weatherContents"),
                jsonObject.getString("tempValue"),
                jsonObject.getString("highestTemp"),
                jsonObject.getString("lowestTemp"),
                jsonObject.getString("rainfallValue"),
                jsonObject.getString("snowValue"),
                jsonObject.getString("windValue")
            )
        )
    }
}