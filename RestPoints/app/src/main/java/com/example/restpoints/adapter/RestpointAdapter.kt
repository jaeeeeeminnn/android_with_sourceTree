package com.example.restpoints.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.restpoints.R
import com.example.restpoints.activity.MainActivity
import com.example.restpoints.model.RestpointModel

class RestpointAdapter : RecyclerView.Adapter<RestpointAdapter.RestpointViewHolder>() {

    private lateinit var mList : ArrayList<RestpointModel>

    // 클릭 리스너 (default, long)
    private lateinit var onItemClickListener : OnItemClickListener
    private lateinit var onItemLongClickListener : OnItemLongClickListener

    fun setList(list : ArrayList<RestpointModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(view : View, position : Int)
    }
    interface OnItemLongClickListener {
        fun onLongClick(view : View, position : Int)
    }

    fun setItemClickListener(itemClickListener : OnItemClickListener) {
        this.onItemClickListener = itemClickListener
    }
    fun setItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.onItemLongClickListener = itemLongClickListener
    }

    inner class RestpointViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val itemImage : ImageView = view.findViewById(R.id.item_image)
        val itemUnitcode : TextView = view.findViewById(R.id.item_unitname)
        val itemRoutename : TextView = view.findViewById(R.id.item_routename)
        val itemAddr : TextView = view.findViewById(R.id.item_addr)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestpointViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return RestpointViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestpointViewHolder, position: Int) {
        if (mList != null) {
            val item = mList!![position]
            Log.d("REST_POINTS", "restpointAdatper : ${item.getWeatherIcon()}")
            holder.itemImage.setImageResource(item.getWeatherIcon())
            holder.itemUnitcode.text = item.unitCode
            holder.itemRoutename.text = item.routeName
            holder.itemAddr.text = item.addr

            // default click
            holder.itemView.setOnClickListener {
                onItemClickListener.onClick(it, position)
            }
            holder.itemView.setOnLongClickListener {
                onItemLongClickListener.onLongClick(it, position)
                return@setOnLongClickListener true
            }
        } else {
            Log.d("REST_POINTS", "mList가 null임")
        }
    }

    override fun getItemCount(): Int {
        return if (mList != null) mList!!.size else 0
    }

}
