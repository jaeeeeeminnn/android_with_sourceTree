package com.example.autobahnrestpoints

import com.example.autobahnrestpoints.entity.RestPointEntity
import com.example.autobahnrestpoints.model.RestPointModel
import org.json.JSONObject

object DEFINES {

    var searchResultList : ArrayList<RestPointModel> = arrayListOf()
    var clickedList : ArrayList<RestPointModel> = arrayListOf()
    var entityList : List<RestPointEntity> = listOf()

    val yearList : ArrayList<String> = arrayListOf()
    val monthList : ArrayList<String> = arrayListOf()
    val dayList : ArrayList<String> = arrayListOf()
    val hourList : ArrayList<String> = arrayListOf()

    fun getYearList() : ArrayList<String> {
        makeYearList()
        return this.yearList
    }
    fun getMonthList() : ArrayList<String> {
        makeMonthList()
        return this.monthList
    }
    fun getDayList() : ArrayList<String> {
        makeDayList()
        return this.dayList
    }
    fun getHourList() : ArrayList<String> {
        makeHourList()
        return this.hourList
    }

    private fun makeYearList() {
        for (i in 2019..2021) {
            yearList.add(i.toString())
        }
    }

    private fun makeMonthList() {
        for (i in 1..12) {
            monthList.add(i.toString())
        }
    }
    private fun makeDayList() {
        for (i in 1..31) {
            dayList.add(i.toString())
        }
    }

    private fun makeHourList() {
        for (i in 1..23) {
            hourList.add(i.toString())
        }
    }
}