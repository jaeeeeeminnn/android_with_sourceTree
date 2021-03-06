package com.example.vaccinationcenter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaccinationcenter.R
import com.example.vaccinationcenter.database.SelectedDatabase
import com.example.vaccinationcenter.database.entity.Selected
import com.example.vaccinationcenter.recycler.model.adapter.CenterAdapter
import com.example.vaccinationcenter.recycler.model.model.CenterModel
import com.example.vaccinationcenter.network.HttpTask
import com.example.vaccinationcenter.thread.HttpThread
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.internal.IGoogleMapDelegate
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_center.view.*
import kotlinx.android.synthetic.main.fragment_maps.view.*
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class CenterFragment : Fragment() {

    private lateinit var cAdapter: CenterAdapter
    lateinit var cDb: SelectedDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_center, container, false)

        cAdapter = CenterAdapter()

        // Progress bar + Searching info textview
        // view.fragment_searching.visibility = View.VISIBLE
        // doProgress(view, 30)

        // recycler view layout management
        view.fragment_list.layoutManager = LinearLayoutManager(this.requireContext())
        view.fragment_list.adapter = cAdapter

        // Spinner Applying
        val spinner = view.framgent_spinner
        val sList: ArrayList<String> = arrayListOf(
            "???????????????", "?????????????????????", "?????????????????????",
            "???????????????", "???????????????", "???????????????", "???????????????", "???????????????", "???????????????",
            "?????????", "?????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????"
        )
        val sAdapter = ArrayAdapter(this.requireContext(), R.layout.spinner_item, sList)
        spinner.adapter = sAdapter
        var spinnerAnswer: String? = null
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerAnswer = sList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context, "???????????? ???????????? ????????????", Toast.LENGTH_LONG).show()
            }

        }

        // Searching Task
        // spinner ???????????? edit text ???????????? ??? ??? ?????? ????????? ??????...
        view.framgent_button.setOnClickListener {
            findCenter(spinnerAnswer, view.fragment_text.text.toString())
        }

        // DB Task
        // Database ????????????
        cDb = SelectedDatabase.getInstance(this.requireContext())!!

        cAdapter.setItemClickListener(object : CenterAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("RECYCLER", "MainFragment??? $position ??? ?????? ??????")
                    // ?????? view??? ?????? ????????? Selected entity??? ????????? ??????
                    val temp: JsonObject = HttpTask.searchList[position]
                    val selected = getEntity(temp)
                    // position??? ???????????? ??????(HttpTask.list : ArrayList<JsonObject>)???
                    // DB??? ??????.
                    withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                        cDb.SelectedDao().insert(selected)
                    }
                    // doProgress(view, 50)
                    var list: List<Selected> = listOf()
                    withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                        list = cDb.SelectedDao().getAll()
                    }
                    // doProgress(view, 100)
                    for (i in 0 until list.size) {
                        Log.d("DB_PRT", "id : ${list[i].id}, name : ${list[i].facilityName}")
                    }
                }
            }
        })

        // Searching info text view invisible
        // view.fragment_searching.visibility = View.GONE

        return view
    }

    private fun findCenter(stateName: String?, cityName: String?) {
        if (cityName == null || stateName == null) {
            Toast.makeText(this.requireContext(), "??????/?????? ????????? ???????????????", Toast.LENGTH_LONG).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                // ?????? httpUrlconnection ???????????? ????????? ????????? ??????
                val pageList: ArrayList<Thread> = arrayListOf()
                for (i in 1..HttpTask.TOTAL_COUNT) {
                    pageList.add(HttpThread(i, stateName, cityName))
                }
                for (i in 0 until pageList.size) {
                    withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                        thread(start = true) {
                            pageList[i].run()
                        }
                    }
                }
                val list = HttpTask.searchList
                for (i in 0 until list.size) {
                    Log.d("SEARCH_LST", "${list[i]}")
                }
                val cList = getModelList(list)
                cAdapter.setList(cList)
            }
        }
    }

    private fun getModelList(list: ArrayList<JsonObject>): ArrayList<CenterModel> {
        val mArray: ArrayList<CenterModel> = arrayListOf()
        for (i in 0 until list.size) {
            Log.d("LIST", "size : ${list.size}, i : $i")
            val curr = list[i]

            val center = CenterModel(
                curr.get("centerName").asString,
                curr.get("facilityName").asString,
                curr.get("address").asString,
                curr.get("phoneNumber").asString
            )
            mArray.add(center)
        }
        return mArray
    }

    private fun getEntity(jsonObject: JsonObject): Selected {
        return Selected(
            jsonObject.get("id").asInt,
            jsonObject.get("centerName").asString,
            jsonObject.get("sido").asString,
            jsonObject.get("sigungu").asString,
            jsonObject.get("facilityName").asString,
            jsonObject.get("address").asString,
            jsonObject.get("phoneNumber").asString
        )
    }

//    private fun doProgress(view: View, percent: Int) {
//        var i = view.fragment_loading.progress
//        when (percent) {
//            25 -> view.fragment_searching.text = "???"
//            50 -> view.fragment_searching.text = "???\t???"
//            75 -> view.fragment_searching.text = "???\t???\t???"
//        }
//        if (i <= percent) {
//            i++
//            view.fragment_loading.setProgress(i)
//            Thread.sleep(500)
//        }
//    }
}