package com.example.jazzbargame.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzbargame.R
import com.example.jazzbargame.model.CustomerModel

class CustomerAdapter : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    private lateinit var mList: ArrayList<CustomerModel>
    private lateinit var itemClickListener : OnItemClickListener

    fun setList(list : ArrayList<CustomerModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(view: View, position: Int)
    }
    fun setItemClickListener(onitemClickListener: OnItemClickListener) {
        this.itemClickListener = onitemClickListener
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view = v

        val itemProfile : ImageView = view.findViewById(R.id.item_image)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemGender: TextView = view.findViewById(R.id.item_gender)
        val itemMoney: TextView = view.findViewById(R.id.item_money)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
            val item = mList!![position]

            holder.itemProfile.setImageResource(item.imageProfile)
            holder.itemName.text = item.name
            holder.itemGender.text = item.setGender()
            holder.itemMoney.text = item.setMoney()

            holder.itemView.setOnClickListener {
                itemClickListener.onClick(it, position)
            }
        } else {
            Log.e("JAZZ", "mList is empty : ")
        }
    }

    override fun getItemCount(): Int {
        return if (mList != null) mList.size else 0
    }
}