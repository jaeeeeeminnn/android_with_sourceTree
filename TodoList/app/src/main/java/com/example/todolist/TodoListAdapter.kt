package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import android.text.format.DateFormat
import android.widget.TextView
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoListAdapter(realmResult: OrderedRealmCollection<Todo>) : RealmBaseAdapter<Todo>(realmResult){

    /*  getView(position: Int, convertView: View?, parent: ViewGroup?)

        position    : list view의 아이템 위치
        converView  : 재활용되는 아이템의 뷰
                       (아이템이 작성되기 전에는 null, 한 번 작성되면 이전에 작성했던 뷰)
        parent      : 리스트 뷰의 참조. (부모 뷰)

     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val vh : ViewHolder
        val view : View

        // convertView가 null 이면 레이아웃을 작성
        // 즉, 아이템이 작성되기 전이라면 레이아웃을 작성
        if (convertView == null) {
            // item_todo.xml을 inflate
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_todo, parent, false)
            // item_todo.xml을 view 홀더에 넣어서 텍스트 뷰들의 참조를 저장함. 
            // 이런식으로 뷰 홀더를 재활용
            vh = ViewHolder(view)
            // view holder 객체는 tag 속성으로 view에 저장.
            // tag 속성은 Any 형으로 어떠한 객체도 저장할 수 있다.
            // 그래서 view 타입이 tag가 될 수 있는 거. 
            view.tag = vh
        }
        // convertView가 null이 아니라면
        else {
            // 이전에 작성했던 convertView를 재사용
            view = convertView
            // view holder 객체를 tag 속성에서 꺼냄.
            vh = view.tag as ViewHolder
        }

        // RealmBaseAdapter는 adapterData 속성을 제공.
        // adapterData에 값이 있다면,
        // 다시말해 list로 adapter에 들어온 게 하나라도 있다면
        if (adapterData != null) {
            // 해당위치의 데이터를 item 변수에 담는다.
            val item = adapterData!![position]
            // 할 일 텍스트와 날짜를 각각 텍스트 뷰에 표시한다.
            vh.textTextView.text = item.title
            vh.dateTextview.text = DateFormat.format("yyyy/mm/dd", item.date)
        }

        // 완성된 view를 반환
        // 이 뷰는 다음 번에 호출되면 convertView로 재사용됨.
        return view
    }

    // list view를 클릭하여 이벤트를 처리할 때 인자로 position, id 등이 넘어오게 됨.
    // 이때 넘어오는 id 값을 결정
    // 데이터베이스를 다룰 때 레코드마다 고유한 아이디를 반환하도록 함.
    override fun getItemId(position: Int): Long {
        if (adapterData != null) {
            return adapterData!![position].id
        }
        return super.getItemId(position)
    }

    // view holder 클래스는 별로도 먼저 작성
    // 전달받은 view에서 text1과 text2 아이디를 가진 텍스트 뷰들의 참조를 저장.
    class ViewHolder(v: View) {
        val view: View = v
        val dateTextview: TextView = view.text1
        val textTextView: TextView = view.text2
    }
}