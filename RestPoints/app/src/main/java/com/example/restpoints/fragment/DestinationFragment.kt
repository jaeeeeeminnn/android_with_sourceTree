package com.example.restpoints.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.restpoints.DEFINES
import com.example.restpoints.R

class DestinationFragment : Fragment() {

    private lateinit var destinationSpinner : Spinner
    private lateinit var destinationAuto : AutoCompleteTextView

    private lateinit var spinnerList : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_destination, container, false)

        destinationSpinner = view.findViewById(R.id.fragment_destination_spinner)
        destinationAuto = view.findViewById(R.id.fragment_destination_auto)

        loadSpinnerList()

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, spinnerList)
        destinationSpinner.adapter = spinnerAdapter

        val autoAdatper = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, spinnerList)
        destinationAuto.setAdapter(autoAdatper)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinationSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    DEFINES.DEST.spinnerAnswer = spinnerList[position]
                    Log.d(
                        "REST_POINTS",
                        "spinner -> position : $position, value : ${spinnerList[position]} answer : ${DEFINES.DEST.spinnerAnswer}"
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d("REST_POINTS", "스피너 아무것도 안 눌렀을 때 실행")
                }

            }

        destinationAuto.setOnItemClickListener { parent, view, position, id ->
            DEFINES.DEST.editorAnswer = spinnerList[position]
            Log.d(
                "REST_POINTS",
                "auto -> position : $position, value : ${spinnerList[position]}, answer : ${DEFINES.DEST.editorAnswer}"
            )
        }
    }

    private fun loadSpinnerList() {
        spinnerList = arrayListOf()

        spinnerList.add("경춘선")
        spinnerList.add("경부선")
        spinnerList.add("경인선")
        spinnerList.add("중부선")
        spinnerList.add("비 내리는 호남선")
    }
}