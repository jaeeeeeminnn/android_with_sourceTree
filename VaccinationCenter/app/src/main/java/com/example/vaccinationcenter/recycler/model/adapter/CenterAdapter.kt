package com.example.vaccinationcenter.recycler.model.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinationcenter.R
import com.example.vaccinationcenter.recycler.model.model.CenterModel
import com.google.android.gms.maps.MapView
import kotlinx.android.synthetic.main.fragment_maps.view.*
import kotlinx.android.synthetic.main.item_list.view.*

class CenterAdapter : RecyclerView.Adapter<CenterAdapter.ViewHolder>() {

    private var cList: ArrayList<CenterModel> = arrayListOf()
    private lateinit var itemClickListener: ItemClickListener

    fun setList(list : ArrayList<CenterModel>) {
        this.cList = list
        notifyDataSetChanged()
    }

    // recycler view item을 클릭할 수 있게 해주는 listener interface
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view = v

        var itemCenterName: TextView = view.item_centerName
        var itemFacilityName: TextView = view.item_facilityName
        var itemAddress: TextView = view.item_address
        var itemPhoneNumber: TextView = view.item_phoneNumber
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (cList != null) {
            val item = cList[position]

            holder.itemCenterName.text = item.centerName
            holder.itemFacilityName.text = item.facilityName
            holder.itemAddress.text = item.address
            holder.itemPhoneNumber.text = item.phoneNumber

            // view에 onClickListener를 달고, 그 안에서 직접 만든 itemClickListener를 연결
            holder.itemView.setOnClickListener {
                itemClickListener.onClick(it, position)
            }
        } else {
            Log.d("CLIST", "cList is empty")
        }
    }

    override fun getItemCount(): Int {
        return if (cList != null) return cList!!.size else return 0
    }
}