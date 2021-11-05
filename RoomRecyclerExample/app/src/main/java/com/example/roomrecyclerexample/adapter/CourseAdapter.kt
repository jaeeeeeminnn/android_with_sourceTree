package com.example.roomrecyclerexample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomrecyclerexample.R
import com.example.roomrecyclerexample.model.CourseModel

class CourseAdapter() : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    private var mList: ArrayList<CourseModel> = arrayListOf()

    fun setList(list : ArrayList<CourseModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    // 뷰에 들어갈 애들 정의
    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private val view = v

        val itemIndex: TextView = view.findViewById(R.id.item_index)
        val itemThema: TextView = view.findViewById(R.id.item_thema)
        val itemSpotName: TextView = view.findViewById(R.id.item_spotName)
        val itemPop: TextView = view.findViewById(R.id.item_pop)
        val itemSky: TextView = view.findViewById(R.id.item_sky)
        val itemTemper: TextView = view.findViewById(R.id.item_temperature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
            val item = mList!![position]

            holder.itemIndex.text = item.getIndex()
            holder.itemThema.text = item.thema.toString()
            holder.itemSpotName.text = item.spotName.toString()
            holder.itemPop.text = item.showPop()
            holder.itemSky.text = item.getSkyCondi()
            holder.itemTemper.text = item.showTh3()
        }
    }

    override fun getItemCount(): Int {
        return if(mList != null) mList!!.size else 0
    }
}