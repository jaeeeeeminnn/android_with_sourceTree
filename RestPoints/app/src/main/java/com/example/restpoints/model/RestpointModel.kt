package com.example.restpoints.model

import android.util.Log
import com.example.restpoints.DEFINES.ICON as I

/**
 * RestpointModel
 *
 * @param unitCode          휴게소 코드 E
 * @param unitName          휴게소 이름 M
 * @param routeName         도로명 M
 * @param xValue            x 좌표 MAP
 * @param yValue            y 좌표 MAP
 * @param addr              주소 M
 * @param weatherContents   현재 일기 내용 M
 * @param tempValue         현재 기온 M
 * @param highestTemp       최고 기온 M
 * @param lowestTemp        최저 기온 M
 * @param rainfallValue     일 강수값 M
 * @param snowValue         적설량값  M
 * @param windContents      풍향내용 M
 * @param windValue         풍속값 M
 *
 * M -> 13
 * E -> 14
 *
 * 1. 휴게소 이름
 * 2. 도로명
 * 3. 주소
 * 4. 현재 일기 내용
 */
class RestpointModel (
        val unitCode : String,
        val unitName : String,
        val routeName : String,
        val xValue : String,
        val yValue : String,
        val addr : String, 
        val weatherContents : String,
        val tempValue : String, 
        val highestTemp : String,
        val lowestTemp : String,
        val rainfallValue : String,
        val snowValue : String, 
        val windContents : String, 
        val windValue : String
        ) {

        /**
         * getWeatherIcon
         *
         * weatherContents에 따라 icon drawable 값을 반환.
         */
        fun getWeatherIcon() : Int {
                var icon : Int? = null

                when (weatherContents) {
                        "맑음" -> { icon = I.SUNNY }
                        "비" -> { icon = I.RAINY }
                        "눈" -> { icon = I.SNOW }
                        "흐림" -> { icon = I.CLOUDY }
                        "강풍" -> { icon = I.WINDY }
                        "안개" -> { icon = I.FOGGY }
                        "무더위" -> { icon = I.HOT }
                        "낙뢰" -> { icon = I.LIGHTENNING }
                        else -> {
                                Log.d("REST_POINTS", "새로운 날씨 : $weatherContents")
                                icon = I.SYMBOL
                        }
                }

                Log.d("REST_POINTS", "Model -> weatherContents : $weatherContents, icon : $icon")

                return icon
        }
//
//        fun getTestModelList() : ArrayList<RestpointModel> {
//                val list : ArrayList<RestpointModel> = arrayListOf()
//                list.add(
//                        RestpointModel(
//                                "0", "가", "경춘선", "0", "0",
//                                "춘천", "맑음", "10", "11", "9",
//                                "10", "10", "남동", "10")
//                )
//                list.add(
//                        RestpointModel(
//                                "1", "나", "경춘선", "0", "0",
//                                "가평", "흐림", "2", "4", "95",
//                                "10", "120", "동", "10")
//                )
//                list.add(
//                        RestpointModel(
//                                "2", "다", "경인선", "0", "0",
//                                "인천", "비", "10", "11", "9",
//                                "10", "10", "남동", "10")
//                )
//                list.add(
//                        RestpointModel(
//                                "3", "가", "경부선", "0", "0",
//                                "부산", "눈", "10", "11", "9",
//                                "10", "10", "남북", "10")
//                )
//                list.add(
//                        RestpointModel(
//                                "5", "가", "경인선", "0", "0",
//                                "서울", "낙뢰", "10", "11", "9",
//                                "10", "10", "서동", "10")
//                )
//                return list
//        }

}