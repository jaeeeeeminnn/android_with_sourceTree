package com.example.vaccinationcenter.thread

import com.example.vaccinationcenter.network.HttpTask

class HttpThread(val i: Int, val stateName: String, val cityName: String) : Thread() {
    override fun run() {
        super.run()

        val url = HttpTask.getUrl(i.toString())
        var response = HttpTask.getResponse(url)
        val jsonArray = HttpTask.getJsonArray(response)
        HttpTask.setWantedItem(jsonArray, stateName, cityName)
    }
}