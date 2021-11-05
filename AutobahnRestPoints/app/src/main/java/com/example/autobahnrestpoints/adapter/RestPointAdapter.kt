package com.example.autobahnrestpoints.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.autobahnrestpoints.R
import com.example.autobahnrestpoints.model.RestPointModel

class RestPointAdapter : RecyclerView.Adapter<RestPointAdapter.ViewHolder>() {

    private lateinit var mList : ArrayList<RestPointModel>
    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(view : View, position: Int)
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.onItemClickListener = itemClickListener
    }

    fun setList(list :ArrayList<RestPointModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        // map fragment 추가해야 함.
        val itemUnitName : TextView = view.findViewById(R.id.item_unitname)
        val itemAddrName : TextView = view.findViewById(R.id.item_addrName)
        val itemTemper : TextView = view.findViewById(R.id.item_temper)
        val itemDetail : TextView = view.findViewById(R.id.item_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
            val item = mList!![position]

            holder.itemUnitName.text = item.unitName
            holder.itemAddrName.text = item.addr
            holder.itemTemper.text = item.getTemper()
            holder.itemDetail.text = item.getDetail()

            holder.itemView.setOnClickListener {
                onItemClickListener.onClick(it, position)
            }
        } else {
            Log.e("LOG", "RestPointAdapter list is empty")
        }
    }

    override fun getItemCount(): Int {
        return if (mList != null) mList!!.size else 0
    }
}