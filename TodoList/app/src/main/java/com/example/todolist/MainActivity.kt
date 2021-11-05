package com.example.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    // 첫 번째 액티비티에 Realm 객체를 초기화.
    val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        // 할 일 정보를 sort() 메소드를 이용하여 날짜순으로 내림차순 정렬하여 얻음.
        // sort(fieldName: String, sortOrder: Sort)
        // fieldName : 정렬할 col
        // sortOrder : 정렬 방법 (DESCENDING / ASCENDING)
        val realmResult = realm.where<Todo>()
            .findAll()
            .sort("date", Sort.DESCENDING)

        // TodoListAdapter 클래스에 할 일 목록인 realmResult를 전달하여 어댑터 인스턴스를 생성.
        val adapter = TodoListAdapter(realmResult)
        // 생성한 어댑터를 list view에 설정
        listView.adapter = adapter

        // 데이터가 변경되면 어댑터에 적용 (Realm 장점 : 데이터가 변경되는지 모니터링 가능)
        // addChangeListener    : 데이터가 변경될 때마다 어댑터에 알려줄 수 있음.
        // notifyDataSetChange(): 데이터 변경을 통지하여 리스트를 다시 표시
        realmResult.addChangeListener { _ -> adapter.notifyDataSetChanged() }

        // list view의 아이템을 클릭했을 때의 처리를 setOnItemClickListener 메소드에 구현
        listView.setOnItemClickListener { parent, view, position, id ->
            // 할 일 수정
            // EditActivity에 선택한 아이템의 id값을 전달.
            // 이제 기존 id가 있는지 여부에 따라 새 할 일을 추가하거나 기존 할 일을 수정.
            startActivity<EditActivity>("id" to id)
        }

        // 새 할 일 추가
        // fab을 클릭했을 때 EditActivity를 시작하도록 설정.
        fab.setOnClickListener {
            startActivity<EditActivity>()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 만들어둔 Realm 객체를 해제
        realm.close()
    }
}