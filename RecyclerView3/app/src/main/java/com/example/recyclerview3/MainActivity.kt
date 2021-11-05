package com.example.recyclerview3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 사용자 정보를 담는 DataFriends 타입의 인스턴스를 생성해서 Array List에 추가
        val list = ArrayList<DataFriends>()
        list.add(DataFriends(null, "Sarah", "0"))
        list.add(DataFriends(getDrawable(R.drawable.ic_android_black_24dp)!!, "Jason", "1"))
        list.add(DataFriends(null, "Rio", "2"))
        list.add(DataFriends(null, "Mathew", "3"))
        list.add(DataFriends(null, "Angela", "4"))
        list.add(DataFriends(null, "Ross", "5"))
        list.add(DataFriends(null, "Joy", "6"))
        list.add(DataFriends(null, "Chloe", "7"))

        // 정보를 전달할 수 있도록 adapter를 만듬
        // adapter에게 list를 전달. 
        val adapter = RecyclerAdaptorFriends(list)
        // Recycler View의 adapter로 지정.
        xml_main_rv_friends.adapter = adapter
    }
}