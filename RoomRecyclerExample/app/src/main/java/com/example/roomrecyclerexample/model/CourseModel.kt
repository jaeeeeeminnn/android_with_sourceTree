package com.example.roomrecyclerexample.model

class CourseModel (
    val index: Int?,
    val thema: String?,
    val spotName: String?,
    val pop: String?,
    val sky: String?,
    val th3: String?
) {
    fun getIndex() : String {
        return "#" + (this.index!!).toString()
    }

    fun showPop() : String {
        return "강수확률 " + this.pop + "%"
    }

    fun showTh3() : String {
        return this.th3 + "°C"
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
}