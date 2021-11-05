package com.example.restpoints.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restpoints.DEFINES
import com.example.restpoints.HTTPTASK
import com.example.restpoints.R
import com.example.restpoints.adapter.RestpointAdapter
import com.example.restpoints.database.RestpointDatabase
import com.example.restpoints.entity.RestpointEntity
import com.example.restpoints.model.RestpointModel
import com.example.restpoints.thread.JsonObjModelingThread
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.lang.Exception
import java.net.HttpCookie
import java.nio.file.DirectoryStream
import kotlin.concurrent.thread

class RestpointFragment : Fragment() {

    private lateinit var destinationText : TextView
    private lateinit var calendarText : TextView
    private lateinit var listView : RecyclerView
    private lateinit var progress : ProgressBar

    private lateinit var mAdapter : RestpointAdapter

    private lateinit var mList : ArrayList<RestpointModel>

    private lateinit var DESTINATION : String
    private lateinit var CALENDARDATE : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restpoint, container, false)

        destinationText = view.findViewById(R.id.fragment_main_destination)
        calendarText = view.findViewById(R.id.fragment_main_date)
        listView = view.findViewById(R.id.fragment_main_list)
        progress = view.findViewById(R.id.fragment_main_progress)

        DESTINATION = DEFINES.DEST.getDestination()
        CALENDARDATE = DEFINES.DATE.getDate()

        progress.visibility = View.VISIBLE
        mAdapter = RestpointAdapter()
        mList = arrayListOf()
        listView.adapter = mAdapter
        listView.layoutManager = LinearLayoutManager(requireContext())


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // HttpConnection
        initRestpoint()
        progress.visibility = View.GONE

        Log.d("REST_POINTS",
            "DATE -> year : ${DEFINES.DATE.year}, month : ${DEFINES.DATE.month}, day : ${DEFINES.DATE.day}")

        destinationText.text = DESTINATION
        calendarText.text = CALENDARDATE

        initRestpoint()
    }

    override fun onStart() {
        super.onStart()

        // 리사이클러 뷰 클릭 리스너
        // 숏 클릭 시 map activity로 전환
        mAdapter.setItemClickListener(object : RestpointAdapter.OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.d("REST_POINTS", "onClick -> position : $position")
                // model x/y Value로 전환
            }
        })
        // 롱 클릭 시 DB에 추가
        mAdapter.setItemLongClickListener(object : RestpointAdapter.OnItemLongClickListener {
            override fun onLongClick(view: View, position: Int) {
                Log.d("REST_POINTS", "onLongClick -> position : $position")
                if (DEFINES.DBTASK.DATABASE_RP == null) {
                    Log.d("REST_POINTS", "DB instance is made in Restpoints Fragment")
                    DEFINES.DBTASK.DATABASE_RP = RestpointDatabase.getInstance(requireContext())
                }
                showFavAddpopup(position)
            }
        })
    }

    /**
     * showFavAddpopup
     * 
     * RestpointFragment (검색 결과)에서 롱 클릭 시 DB에 추가할 지 물어보는 팝업
     */
    private fun showFavAddpopup(position : Int) {
        try {
            val view = DEFINES.POPUP.inflater?.inflate(R.layout.alert_popup, null)
            val popupText : TextView = view!!.findViewById(R.id.alert_title)
            popupText.text = DEFINES.POPUP.ADD_TEXT

            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle(DEFINES.POPUP.POPUPTITLE_TEXT)
                setPositiveButton(DEFINES.POPUP.OK_TEXT) { dialog, which ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val entity : RestpointEntity = mteConverter(mList[position])
                        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                            DEFINES.DBTASK.DATABASE_RP!!.RestpointDao().insert(entity)
                        }
                    }
                    Toast.makeText(requireContext(), "즐겨찾기 목록에 추가되었습니다", Toast.LENGTH_LONG).show()
                }
                setNeutralButton(DEFINES.POPUP.NO_TEXT, null)
                create()
            }
            alertDialog.setView(view)
            alertDialog.show()
        } catch (e : Exception) {
            Log.e("REST_POINTS", "Popup fragment is not inflated " + e.message)
            e.printStackTrace()
        }
    }

    /**
     * etmConverter
     *
     * Entity List -> Model List
     */
    private fun etmConverter(eList : List<RestpointEntity>) : ArrayList<RestpointModel> {
        val result : ArrayList<RestpointModel> = arrayListOf()
        for (i in 0 until eList.size) {
            val item : RestpointEntity = eList[i]
            result.add(
                RestpointModel(
                    item.unitCode, item.unitName, item.routeName, item.xValue, item.yValue, item.addr,
                    item.weatherContents, item.tempValue, item.highestTemp, item.lowestTemp,
                    item.rainfallValue, item.snowValue, item.windContents, item.windValue
                )
            )
        }
        return result
    }

    /**
     * mteConverter
     *
     * Model To Entity
     */
    private fun mteConverter(model: RestpointModel): RestpointEntity {
        val result =
            RestpointEntity(
                model.unitCode, model.unitName, model.routeName , model.xValue, model.yValue,
                model.addr, model.weatherContents, model.tempValue, model.highestTemp, model.lowestTemp,
                model.rainfallValue, model.snowValue, model.windContents, model.windValue
            )
        return result
    }

    /**
     * initRestpoint
     *
     * src -> url -> jsonArray -> JsonObject -> RestpointModel -> Model List
     */
    private fun initRestpoint() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                var jsonArray : JsonArray? = null
                try {
                    val src : String = HTTPTASK.getSrc(DEFINES.DATE.getUrlDate())
                    val response : String? = HTTPTASK.getResponse(src)
                    jsonArray = HTTPTASK.getJsonArray(response)
                } catch (e : Exception) {
                    Log.d("REST_POINTS", "Maybe response variation should be null.")
                }
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    try {

                    } catch (e : Exception) {
                        Log.d("REST_POINTS", "DB I/O should be problem")
                    }
                }
            }
        }
        mAdapter.setList(etmConverter())
    }
}