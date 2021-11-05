package com.example.restpoints.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.restpoints.DEFINES
import com.example.restpoints.R

class CalendarFragment : Fragment() {

    private lateinit var calendarView : CalendarView
    private lateinit var dateText : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.fragment_calendar_calendarView)
        calendarView.dateTextAppearance = R.style.TextAppearance_AppCompat_Button
        calendarView.weekDayTextAppearance = R.style.TextAppearance_AppCompat_Button

        calendarView.minDate = SimpleDateFormat("yyyyMMdd").parse("20190101").time
        calendarView.maxDate = SimpleDateFormat("yyyyMMdd").parse("20210601").time

        dateText = view.findViewById(R.id.fragment_calendar_text)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            DEFINES.DATE.year = year.toString()
            DEFINES.DATE.month = month.toString()
            DEFINES.DATE.day = dayOfMonth.toString()

            // Log.d("REST_POINTS", "year : ${DEFINES.DATE.year}, month : ${DEFINES.DATE.month}, day : ${DEFINES.DATE.day}")

            if (year < 2019) {
                Toast.makeText(requireContext(), "2019년보다 이전의 데이터는 없습니다.", Toast.LENGTH_LONG).show()
            }
            else if (year > 2021) {
                Toast.makeText(requireContext(), "지금은 2021년인데요? 확실합니까?", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "선택한 날짜가 맞나 확인해주세요.", Toast.LENGTH_LONG).show()
                dateText.text = DEFINES.DATE.getDate(year, month, dayOfMonth)
                // Log.d("REST_POINTS", "DATE -> date : ${dateText.text}")
            }
        }

        Log.d("REST_POINTS", "DATE -> hour : ${DEFINES.DATE.hour} year : ${DEFINES.DATE.year}, month : ${DEFINES.DATE.month}, day : ${DEFINES.DATE.day}")
    }

}