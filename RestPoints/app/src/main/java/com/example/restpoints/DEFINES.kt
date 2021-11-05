package com.example.restpoints

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.restpoints.database.RestpointDatabase
import com.example.restpoints.entity.RouteNameEntity
import com.example.restpoints.fragment.DestinationFragment
import com.example.restpoints.model.RestpointModel

object DEFINES {

    // CalendarFragment 발 정보
    object DATE {
        var ISNOON : Boolean = false
        val AM : String = "AM "
        val PM : String = "PM "

        var hour : String = "10"
        var year : String = "2020"
        var month : String = "01"
        var day : String = "01"

        fun getDate(year : Int, month : Int, day: Int) : String {

            return "${year}년 ${month}월 ${day}일"
        }
        fun getDate() : String {
            if (this.month[0] == '0') {
                this.month = this.month.substring(1)
                Log.d("REST_POINTS", "DATE -> month : ${this.month}")
            }
            return "${this.year}년 ${this.month}월 ${this.day}일"
        }

        fun getUrlDate() : String {
            var result : String = this.year
            var month : String = this.month
            var day : String = this.day
            if (this.month.length < 2) {
                month = "0${this.month}"
            }
            if (this.day.length < 2) {
                day = "0${this.day}"
            }
            result = result + month + day
            return result
        }
    }

    object DEST {
        var spinnerAnswer : String? = null
        var editorAnswer : String? = null

        // 경춘선이라고 되어있는 부분은 나중에 팝업으로 처리
        fun getDestination() : String {
            if (spinnerAnswer != null && editorAnswer != null) {
                if (spinnerAnswer.equals(editorAnswer)) {
                    return spinnerAnswer as String
                } else {
                    return "경춘선"
                }
            } else if (spinnerAnswer == null && editorAnswer == null) {
                return "경춘선"
            } else {
                if (spinnerAnswer != null) {
                    return spinnerAnswer as String
                } else {
                    return editorAnswer as String
                }
            }
        }
    }

    object POPUP {
        var isHome : Boolean = true
        val HONETOFAV_TEXT : String = "즐겨찾기 목록으로 이동할까요?"
        val FAVTOHOME_TEXT : String = "선택 화면으로 이동할까요?"
        val DELETEALL_TEXT : String = "모든 즐겨찾기를 삭제할까요?"
        val DELETE_TEXT : String = "해당 즐겨찾기를 삭제할까요?"
        val ADD_TEXT : String = "해당 즐겨찾기를 추가할까요?"

        val POPUPTITLE_TEXT : String = "Q."
        val OK_TEXT : String = "네"
        val NO_TEXT : String = "아뇨"

        var inflater : LayoutInflater? = null
    }

    object DBTASK {
        val DATABASE_NAME_RN : String = "Route Name DB"
        val DATABASE_NAME_RP : String = "Rest Point DB"
        var DATABASE_RP : RestpointDatabase? = null

        var ROUTE_LIST : ArrayList<RouteNameEntity> = arrayListOf()
    }

    object ICON {
        val CALENDAR: Int = R.drawable.icon_calendar
        val CHECK: Int = R.drawable.icon_check
        val DESTINATION: Int = R.drawable.icon_destination
        val FAVORITE: Int = R.drawable.icon_favorite
        val HOME: Int = R.drawable.icon_home
        val SYMBOL: Int = R.drawable.icon_symbol

        val CLOUDY: Int = R.drawable.icon_cloudy
        val FOGGY: Int = R.drawable.icon_foggy
        val HOT: Int = R.drawable.icon_hot
        val LIGHTENNING: Int = R.drawable.icon_lightenning
        val RAINY: Int = R.drawable.icon_rainy
        val RAINY_CANDI: Int = R.drawable.icon_rainy_candi
        val SNOW: Int = R.drawable.icon_snow
        val SUNNY: Int = R.drawable.icon_sunny
        val SUNNY_CANDI: Int = R.drawable.icon_sunny_candi
        val WINDY: Int = R.drawable.icon_windy
    }
}