package com.example.ticketbox

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ticket.view.*

/*  Recycler Adapter 클래스

    MainActivity.kt에서 선언한 ArrayList를 매개변수로 받음.
    onBindViewHolder() 메소드로 현재 position(index)에 해당하는 item(MovieInfo 클래스) 추출.
    viewHolder 클래스의 bind() 메소드를 호출, 해당하는 정보를 뷰홀더에 바인딩.

    View Holder를 생성하는 onCreateViewHolder() 메소드에서
    Recycler view에 list item 표현하는 ticket.xml 파일을 메모리에 적재(inflate).
    - inflate(R.layout.ticket, parent, attachToRoot)
    이때 Recycler View는 parent(view group)의 context(객체의 상태)를 기반으로 inflate 수행.
    attachToRoot는 false. (true이면 error)
    이유는 Recycler view는 View Holder를 단지 가져다 쓰는 것이기 때문에 자체만으로는 의미가 없기 때문.
    root는 단지 inflate된 xml의 layout param을 알맞게 만들기 위한 존재.
 */
class RecyclerAdapter (private val items: ArrayList<MovieInfo>)
    : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        // recycler view를 위한 xml 파일을 parent(view Group)에서 inflate하고
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.ticket, parent, false)
        // inflate된 recycler view를 view holder를 이용하여 사용한다.
        // 다시 말해, adapter를 이용하여 정보를 view에 줄 수 있다는 것이다.
        return RecyclerAdapter.ViewHolder(inflatedView)
    }

    // 현재 position에 해당하는 MovieInfo 타입 객체를 추출하여 listener와 함께 binding
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked: "+ item.strName, Toast.LENGTH_LONG).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // onBindViewHolder() 메소드를 통해 현재 adapter가 표현하고 있는 MovieInfo 객체를 binding
    // 실제 바인딩은 여기서 이뤄진다.
    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: MovieInfo) {
            view.movie_cover.setImageDrawable(item.imgProfile)
            view.movie_name.text = item.strName
            view.price.text = item.intPrice.toString()
            view.Director.text = item.strDirec
            view.genre.text = item.strgenr
        }
    }

}