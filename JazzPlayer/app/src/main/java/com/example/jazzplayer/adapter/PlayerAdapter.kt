package com.example.jazzplayer.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzplayer.R
import com.example.jazzplayer.model.PlayerModel
import kotlinx.android.synthetic.main.item_list.view.*

class PlayerAdapter : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    private var mList : ArrayList<PlayerModel> = arrayListOf()
    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(view : View, position: Int)
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.onItemClickListener = itemClickListener
    }

    fun setList(list : ArrayList<PlayerModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view = v

        val itemImgProfile : ImageView = view.item_image
        val itemTitle : TextView = view.item_title
        val itemAlbum : TextView = view.item_album
        val itemSinger : TextView = view.item_singer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
            val item : PlayerModel = mList!![position]

            holder.itemImgProfile.setImageResource(item.imgProfile!!)
            holder.itemTitle.text = item.title
            holder.itemAlbum.text = item.album
            holder.itemSinger.text = item.singer

            holder.itemView.setOnClickListener {
                onItemClickListener.onClick(it, position)
            }
        } else {
            Log.d("LOG", "JazzPlayer List is empty")
        }
    }

    override fun getItemCount(): Int {
        return if (mList != null) mList!!.size else 0
    }

}