package com.example.vaccinationcenter.recycler.model.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinationcenter.R
import com.example.vaccinationcenter.recycler.model.model.SelectedModel
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectedAdapter : RecyclerView.Adapter<SelectedAdapter.ViewHolder>() {

    private var dList: ArrayList<SelectedModel> = arrayListOf()
    private lateinit var itemClickListener: ItemClickListener

    fun setList(list : ArrayList<SelectedModel>) {
        this.dList = list
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view = v

        val itemCenterName: TextView = view.item_centerName
        val itemFacilityName : TextView = view.item_facilityName
        val itemAddress : TextView = view.item_address
        val itemPhoneNumber: TextView = view.item_phoneNumber
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onBindViewHolder(holder: SelectedAdapter.ViewHolder, position: Int) {
        if (dList != null) {
            val item = dList[position]

            holder.itemCenterName.text = item.centerName
            holder.itemFacilityName.text = item.facilityName
            holder.itemAddress.text = item.address
            holder.itemPhoneNumber.text = item.phoneNumber


            holder.itemView.setOnClickListener {
                itemClickListener.onClick(it, position)
            }

        } else {
            Log.d("DB-LIST", "dList is empty")
        }
    }


    override fun getItemCount(): Int {
        return if (dList != null) dList.size else 0
    }
}