package com.example.vaccinationcenter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaccinationcenter.R
import com.example.vaccinationcenter.activity.SelectedActivity
import com.example.vaccinationcenter.database.SelectedDatabase
import com.example.vaccinationcenter.database.entity.Selected
import com.example.vaccinationcenter.network.HttpTask
import com.example.vaccinationcenter.recycler.model.adapter.SelectedAdapter
import com.example.vaccinationcenter.recycler.model.model.SelectedModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_maps.view.*
import kotlinx.android.synthetic.main.fragment_selected.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectedFragment : Fragment(), OnMapReadyCallback {

    private lateinit var dAdapter: SelectedAdapter
    lateinit var dDb: SelectedDatabase
    private lateinit var cMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_selected, container, false)
        var dList: List<Selected> = listOf()

        dAdapter = SelectedAdapter()
        dDb = SelectedDatabase.getInstance(requireContext())!!

        CoroutineScope(Dispatchers.Main).launch {

            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                // db에 있는 selected를 데려옴.
                dList = dDb.SelectedDao().getAll()
                // selected를 selectedModel로 다시 만들어줘
                val list: ArrayList<SelectedModel> = getList(dList)
                dAdapter.setList(list)


                activity?.runOnUiThread {
                    view.fragment_list.layoutManager = LinearLayoutManager(requireContext())
                    view.fragment_list.adapter = dAdapter
                }
            }
        }

        dAdapter.setItemClickListener(object : SelectedAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("RECYCLER", "SelectedFragment의 $position 번 항목 선택")
                    // db에서 제거
                    withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                        val deleteId: Int = dList[position].id
                        dDb.SelectedDao().delete(deleteId)
                    }
                }
            }
        })

        return view
    }

    private fun getList(dList: List<Selected>): ArrayList<SelectedModel> {
        val list: ArrayList<SelectedModel> = arrayListOf()
        for(i in 0 until dList.size) {
            val item = dList[i]
            val selectedModel = SelectedModel(
                item.id, item.centerName, item.facilityName,
                item.address, item.phoneNumber
            )
            list.add(selectedModel)
        }
        return list
    }

    override fun onMapReady(googleMap: GoogleMap) {
        cMap = googleMap

        val center = LatLng(35.0, 128.0)
        cMap.addMarker(
            MarkerOptions().position(center)
                .title("서울 노원")
        )
        cMap.moveCamera(CameraUpdateFactory.newLatLng(center))
    }
}