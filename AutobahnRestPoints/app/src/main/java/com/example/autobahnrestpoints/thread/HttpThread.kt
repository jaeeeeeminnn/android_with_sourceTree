package com.example.autobahnrestpoints.thread

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import com.example.autobahnrestpoints.http.HttpTask as H

/**
 * HttpThread
 *
 * @param route : 도로명
 *
 * 1. json 요청을 원하는 대로 수정
 * 2. json 응답을 받음
 * 3. json 응답으로부터 jsonArray를 추출
 * 4. jsonArray로부터 route와 대조하여 원하는 결과만 추출
 * 5. DEFINE에 RestPointModel 타입의 arrayList 하나를 만들어서 거기다가 추가.
 */
class HttpThread(val route : String, val date : String, val hour : String) : Thread() {
    override fun run() {
        super.run()

        // 1~3번
        val request : String = H.getUrl(date, hour)
        val response : String? = H.getResponse(request)
        val jsonArray : JSONArray? = H.getJsonArray(response)

        // 4~5번
        if (jsonArray != null) {
            for (i in 0 until jsonArray.length()) {
                if (route.equals(H.getRouteName(jsonArray[i] as JSONObject))) {
                    // 도로명 조건에 맞는 결과를 model로 만들어 둠.
                    H.addModelList(jsonArray[i] as JSONObject)
                }
            }
        } else {
            Log.e("LOG", "jsonArray is null")
        }
    }
}