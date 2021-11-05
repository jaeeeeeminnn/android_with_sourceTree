package com.example.weatherrest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherrest.R
import com.example.weatherrest.model.InfoModel
import org.w3c.dom.Text

class InfoAdapter : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {

    private var mList: ArrayList<InfoModel>? = null

    fun setList(list: ArrayList<InfoModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private val view : View = v

        val itemIndex: TextView = view.findViewById(R.id.item_index)
        val itemThema: TextView = view.findViewById(R.id.item_thema)
        val itemSpotName : TextView = view.findViewById(R.id.item_spotName)
        val itemPop : TextView = view.findViewById(R.id.item_pop)
        val itemSky : TextView = view.findViewById(R.id.item_sky)
        val itemTh3 : TextView = view.findViewById(R.id.item_th3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (mList != null) {
            return mList!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
            val item = mList!![position]

            holder.itemIndex.text = item.getIndex()
            holder.itemThema.text = item.thema
            holder.itemSpotName.text = item.spotName
            holder.itemPop.text = item.getPop()
            holder.itemSky.text = item.getSkyCondi()
            holder.itemTh3.text = item.getTh3()
        }
    }
}