package com.example.restpoints.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restpoints.DEFINES
import com.example.restpoints.R
import com.example.restpoints.activity.MainActivity
import com.example.restpoints.adapter.RestpointAdapter
import com.example.restpoints.database.RestpointDatabase
import com.example.restpoints.entity.RestpointEntity
import com.example.restpoints.model.RestpointModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.nio.file.DirectoryStream

class FavoriteFragment : Fragment() {

    private lateinit var deleteAllButton : ImageButton
    private lateinit var progress : ProgressBar
    private lateinit var favoriteListView : RecyclerView

    private lateinit var favoriteList : ArrayList<RestpointModel>

    private lateinit var mAdapter : RestpointAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        deleteAllButton = view.findViewById(R.id.fragment_favorite_delete)
        progress = view.findViewById(R.id.fragment_favorite_progress)
        favoriteListView = view.findViewById(R.id.fragment_favorite_list)

        favoriteList = arrayListOf()

        mAdapter = RestpointAdapter()
        mAdapter.setList(favoriteList)
        favoriteListView.adapter = mAdapter
        favoriteListView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load Recycler List
        loadFavoriteList()

        // Delete All Button
        deleteAllButton.setOnClickListener {
            showPopup(isDeleteAll = true, position = -1)
        }

        // Delete an Item (Long button)
        mAdapter.setItemLongClickListener(object : RestpointAdapter.OnItemLongClickListener {
            override fun onLongClick(view: View, position: Int) {
                showPopup(isDeleteAll = false, position = position)
            }
        })

        // Select an Item (Go to MapsActivity)


    }

    /**
     * deleteAllOper
     *
     * DB 모든 내용 삭제하는 메소드
     * 
     * favoriteList 를 empty 리스트로 새로 선언 후 리스트 갱신
     */
    private fun deleteAllOper() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                if (DEFINES.DBTASK.DATABASE_RP != null) {
                    DEFINES.DBTASK.DATABASE_RP!!.RestpointDao().deleteAll()
                } else {
                    Log.d("REST_POINTS", "DB is null")
                }
            }
            favoriteList = arrayListOf()
            mAdapter.setList(favoriteList)
        }
    }

    /**
     * deleteItemOper
     *
     * 선택한 DB item 삭제하는 메소드
     * 
     * favoriteList 중 position index 원소 삭제 후 리스트 갱신
     */
    private fun deleteItemOper(position : Int) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                if (DEFINES.DBTASK.DATABASE_RP != null) {
                    DEFINES.DBTASK.DATABASE_RP!!.RestpointDao().delete(favoriteList[position].unitCode)
                } else {
                    Log.d("REST_POINTS", "DB is null")
                }
            }
            favoriteList.removeAt(position)
            mAdapter.setList(favoriteList)
        }
    }

    /**
     * showPopup
     *
     * @param isDeleteAll : Boolean
     *
     * 전체 삭제 / 아이템 삭제 행동 구분
     */
    private fun showPopup(isDeleteAll : Boolean, position : Int) {
        val view = DEFINES.POPUP.inflater?.inflate(R.layout.alert_popup, null)
        val popupText : TextView = view!!.findViewById(R.id.alert_title)
        val alertDialog = AlertDialog.Builder(requireContext())

        when (isDeleteAll) {
            // true = Delete All
            true -> {
                popupText.text = DEFINES.POPUP.DELETEALL_TEXT
                alertDialog.apply {
                    setTitle(DEFINES.POPUP.POPUPTITLE_TEXT)
                    setPositiveButton(DEFINES.POPUP.OK_TEXT,
                        DialogInterface.OnClickListener { dialog, which ->
                            deleteAllOper()
                        })
                    setNegativeButton(DEFINES.POPUP.NO_TEXT,
                        DialogInterface.OnClickListener { dialog, which ->
                            // Don't do AnyThing -> Just Close the popup
                        })
                    create()
                }
            }
            // else = false = Delete an Item
            else -> {
                popupText.text = DEFINES.POPUP.DELETE_TEXT
                alertDialog.apply {
                    setTitle(DEFINES.POPUP.POPUPTITLE_TEXT)
                    setPositiveButton(DEFINES.POPUP.OK_TEXT,
                        DialogInterface.OnClickListener { dialog, which ->
                            deleteItemOper(position)
                        })
                    setNegativeButton(DEFINES.POPUP.NO_TEXT,
                        DialogInterface.OnClickListener { dialog, which ->
                            // Don't do AnyThing -> Just Close the popup
                        })
                    create()
                }
            }
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    /**
     * loadFavoriteList
     *
     * DB에 저장한 내용을 List로 추출
     * List를 ArrayList로 변환 및 반환
     * adapter에 적용
     */
    private fun loadFavoriteList() {
        CoroutineScope(Dispatchers.Main).launch {
            var dbList : List<RestpointEntity>? = null
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                // Database Null Check
                if (DEFINES.DBTASK.DATABASE_RP == null) {
                    dbList = listOf()
                } else {
                    dbList = DEFINES.DBTASK.DATABASE_RP!!.RestpointDao().findAll()
                }
            }
            for (i in 0 until dbList!!.size) {
                val item = dbList!![i]
                favoriteList.add(
                    RestpointModel(
                        item.unitCode, item.unitName, item.routeName, item.xValue, item.yValue,
                        item.addr, item.weatherContents, item.tempValue, item.highestTemp, item.lowestTemp,
                        item.rainfallValue, item.snowValue, item.windContents, item.windValue
                    )
                )
            }
            Log.d("REST_POINTS", "dbList -> size : ${dbList!!.size}")
            Log.d("RESP_POINTS", "reuslt -> size : ${favoriteList.size}")

            mAdapter.setList(favoriteList)
        }
    }

    /**
     * isEmpty
     *
     * favoriteList가 empty면, fragment_favorite_isEmpty를 visible
     */
    private fun isEmtpyOper(view : View) {
        val emptyText = view.findViewById<TextView>(R.id.fragment_favorite_isEmpty)
        if (favoriteList.isEmpty()) {
            emptyText.visibility = View.VISIBLE
        } else {
            emptyText.visibility = View.GONE
        }
    }
}
