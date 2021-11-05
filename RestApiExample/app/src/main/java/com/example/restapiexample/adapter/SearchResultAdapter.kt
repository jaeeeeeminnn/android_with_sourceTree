package com.example.restapiexample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restapiexample.R
import com.example.restapiexample.model.SearchResultModel

class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private var mList: ArrayList<SearchResultModel>? = null

    fun setList(list: ArrayList<SearchResultModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.item_image)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemPrice: TextView = view.findViewById(R.id.item_price)
    }

    override fun getItemCount() = if (mList == null) {
        0
    } else {
        mList!!.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_view, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (mList != null) {

            if (mList!![position].image != null) {
                viewHolder.itemImage.setImageBitmap(mList!![position].image)
            }

            viewHolder.itemName.text = mList!![position].name
            viewHolder.itemPrice.text = mList!![position].price
        }
    }
}