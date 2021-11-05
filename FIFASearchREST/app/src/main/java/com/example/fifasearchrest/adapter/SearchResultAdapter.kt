package com.example.fifasearchrest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fifasearchrest.R
import com.example.fifasearchrest.model.SearchResultModel
import kotlinx.android.synthetic.main.item_list.view.*

class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private var mList: ArrayList<SearchResultModel>? = null

    fun setList(list : ArrayList<SearchResultModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view = v

        // 여기서 실질적 바인딩
        val itemImage : ImageView = view.findViewById(R.id.item_image)
        val itemName : TextView = view.findViewById(R.id.item_name)
        val itemPrice : TextView = view.findViewById(R.id.item_price)
    }

    // inflate 작업
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    // 바인딩 작업
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
            val item = mList!![position]
            holder.itemImage.setImageBitmap(item.image)
            holder.itemName.text = item.name
            holder.itemPrice.text = item.price
        }
    }

    override fun getItemCount(): Int {
        // fragment로부터 받아온 리스트가 null이 아니라면 (뭐라도 요소가 있다면)
        if (mList != null) {
            // mList는 null이 아님이 확실
            return mList!!.size
        } else {
            return 0
        }
    }


}