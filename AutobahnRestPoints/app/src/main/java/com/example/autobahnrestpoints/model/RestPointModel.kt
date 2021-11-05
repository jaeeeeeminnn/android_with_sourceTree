package com.example.autobahnrestpoints.model

/**
 * RestPointModel
 *
 * 휴게소 정보 모델
 *
 * @param unitCode      : 휴게소 코드 (entity랑 연동할 때 사용)
 * @param unitName      : 휴게소명
 * @param addr          : 주소
 * @param routeName     : 도로명
 * @param weatherContents   : 기상상태
 * @param tempValue     : 현재 온도
 * @param highestTemp   : 최고온도
 * @param lowestTemp    : 최저온도
 * @param rainfallValue : 강수량
 * @param snowValue     : 적설량
 * @param windValue     : 풍속량
 */
class RestPointModel (
    val unitCode : String,
    val unitName : String,
    val addr : String,
    val routeName : String,
    val weatherContents : String,
    val tempValue : String,
    val highestTemp : String,
    val lowestTemp : String,
    val rainfallValue : String,
    val snowValue : String,
    val windValue : String
    ) {

    fun getTemper() : String {
        return "$lowestTemp \u003C- $tempValue \u003E- $highestTemp"
    }

    fun getDetail() : String {
        val w = weatherContents
        when {
            w.contains("맑음") -> {
                return windValue
            }
            w.contains("비") -> {
                return rainfallValue
            }
            w.contains("눈") -> {
                return snowValue
            }
            // 흐림
            else -> {
                return windValue
            }
        }
    }

}
