package com.example.weatherrest.model

class InfoModel(
    val index: Int?,
    val thema: String?,
    val spotName: String?,
    val pop: Int?,
    val sky: String?,
    val th3: Int?
) {
    fun getIndex(): String? {
        if (this.index != null) {
            return "No." + (this.index + 1).toString()
        }
        return null
    }

    fun getPop() : String? {
        if (this.pop != null) {
            return "강우확률 : " + this.pop.toString() + "%"
        }
        return null
    }

    fun getSkyCondi() : String? {
        when (this.sky) {
            null -> return null
            "1" -> return "맑음"
            "2" -> return "구름"
            "3" -> return "흐림"
            "4" -> return "비"
            "5" -> return "소나기"
            "6" -> return "비오고 눈옴"
            "7" -> return "눈오고 비옴"
            "8" -> return "눈"
        }
        return null
    }

    fun getTh3() : String? {
        if (this.th3 != null) {
            return this.th3.toString() + "°C"
        }
        return null
    }
}