package com.example.recyclerview3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_friends.view.*

// Recycler View는 list와 같이 여러개의 view를 가지고 있는 정보를 생성하고 넘겨 줄 수 있는 클래스가 필요
// 그 클래스가 adaptor 클래스
// adaptor 클래스는 ViewHolder(뷰홀더) 클래스를 가지고 있고,
// RecyclerView 클래스의 Adaptor를 반환.
class RecyclerAdaptorFriends (private val items : ArrayList<DataFriends>)
    : RecyclerView.Adapter<RecyclerAdaptorFriends.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerAdaptorFriends.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_friends, parent, false)
        return RecyclerAdaptorFriends.ViewHolder(inflatedView)
    }

    // adapter를 통해 전달받은 list에서 현재 position에 해당하는 item을 추출.
    // 해당 item과 listener를 ViewHolder 클래스를 통해 binding
    override fun onBindViewHolder(holder: RecyclerAdaptorFriends.ViewHolder, position: Int) {
        val item = items[position]
        // 현재 View에 클릭이 있다면 현재 item 정보를 toast
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked: " + item.strName, Toast.LENGTH_SHORT).show()
        }
        // ViewHolder를 binding하고 색인할 수 있는 tag를 설정.
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Recycler View에 현재 보고 있는 View (DataFriends 타입의 list_friends.xml 파일 정보 포함)을 바인딩하는 클래스
    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private var view : View = v
        fun bind(listener: View.OnClickListener, item: DataFriends) {
            view.xml_lstfrd_img_profile.setImageDrawable(item.imgProfile)
            view.xml_lstfrd_txt_name.text = item.strName
            view.xml_lstfrd_txt_phone.text = item.strPhone
        }
    }
}