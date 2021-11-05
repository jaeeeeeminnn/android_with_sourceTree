package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.Calendar

class EditActivity : AppCompatActivity() {

    // realm 인스턴스 얻기
    val realm = Realm.getDefaultInstance()

    // 오늘 날짜로 Calendar 객체 생성
    val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        /*  업데이트 조건

            id 값이 0 이상이면 업데이트 모드.
            id 값이 아무것도 넘어오지 않아 기본값 그대로 -1이면 추가모드.

            getLongExtra(name: String, defaultValue: Long)
            - name : 아이템을 가리키는 key
            - defaultValue : 반환되는 값이 없을 때 기본값 설정
         */
        val id = intent.getLongExtra("id", -1L)
        if (id == -1L) {
            insertMode()
        } else {
            updateMode(id)
        }

        /*  캘린더 뷰의 날짜를 선택했을 대 Calendar 객체에 설정

            Calendar View에서 날짜를 선택하면 수행할 처리를 구현.
            변경된 년, 월, 일이 year, month, dayOfMonth로 넘어오는 Calendar 객체엣
            년월일을 설정해주면 데이터베이스의 추가, 수정 설정한 날짜가 반영.
         */
        calendarView.setOnDateChangeListener {
                view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_YEAR, dayOfMonth)
        }
    }

    private fun insertTodo() {
        // 트랜잭션 시작
        // Realm에서 데이터 추가, 삭제, 갱신 시에는 beginTransaction() 메소드로 트랜잭션 시작.
        realm.beginTransaction()

        // 새 객체 생성
        // createObject<T:RealmModel>(primaryKeyValue: Any?)
        val newItem = realm.createObject<Todo>(nextId())

        // 값 설정
        // 객체를 생성했으면 할 일과 시간을 설정. 
        newItem.title = todoEditText.text.toString()
        newItem.date = calendar.timeInMillis
        
        // 트랜잭션 종료 반영
        // beginTransaction() ~ commitTransaction() 사이의 코드들은
        // 전체가 하나의 작업이기 때문에 도중에 에러가 나면 전체가 취소됨.
        realm.commitTransaction()

        // 다이얼로그 표시
        alert("내용이 추가되었습니다. ") {
            yesButton { finish() }
        }.show()
    }

    // 다음 id를 반환.
    // insert에 쓰는 거기 때문에 현재 가장 큰 번호보다 1이 더 높은 번호를 반환.
    private fun nextId(): Int {
        // max(fieldName: String) : fieldname 열 값 중 가장 큰 값을 number 형으로 반환
        // Todo 테이블의 모든 값을 얻으려며 where<Todo>() 메소드를 사용.
        val maxId = realm.where<Todo>().max("id")
        if (maxId != null) {
            return maxId.toInt() + 1
        }
        return 0

    }

    // id를 인자로 받아야 찾아서 update를 할 수 있음.
    private fun updateTodo(id : Long) {
        // 트랜잭션 시작
        realm.beginTransaction()

        // Realm 객체의 where<T>() a메소드가 반환하는 T 타입 객체로부터 데이터를 얻음.
        // equalTo() 메소드를 통해 "id"컬럼에 id 값이 있다면 findFirst() 메소드를 실행.
        val updateItem = realm.where<Todo>().equalTo("id", id).findFirst()!!

        // 값 수정
        updateItem.title = todoEditText.text.toString()
        updateItem.date = calendar.timeInMillis

        // 트랜잭션 종료 반영
        realm.commitTransaction()

        // 다이얼로그 표시
        alert("내용이 변경되었습니다. ") {
            yesButton { finish() }
        }.show()
    }

    // 할 일 삭제하는 메소드
    private fun deleteTodo(id: Long) {
        realm.beginTransaction()

        // 삭제할 객체
        val deleteItem = realm.where<Todo>().equalTo("id", id).findFirst()!!
        
        // deleteFromRealm() : realm 객체 삭제
        deleteItem.deleteFromRealm()

        realm.commitTransaction()

        // 다이얼로그
        alert("내용이 삭제되었습니다. ") {
            yesButton { finish() }
        }.show()
    }

    // 추가모드 초기화
    private fun insertMode() {
        // 삭제 버튼을 감추기
        deleteFab.hide()
        /*  setVisibility()

            view를 보이거나 안 보이게 하는 메소드
            코틀린에서는 visibility 속성으로 사용할 수 있음.
            - VISIBLE   : 보이게 함.
            - INVISIBLE : 영역은 차지하지만 보이지 않음.
            - GONE      : 완전히 보이지 않음.

         */

        // 완료버튼을 클리하면 추가
        doneFab.setOnClickListener { insertTodo() }
    }

    // 수정모드 초기화
    private fun updateMode(id: Long) {
        // id에 해당하는 객체를 화면에 표시
        val todo = realm.where<Todo>().equalTo("id", id).findFirst()!!

        todoEditText.setText(todo.title)
        calendarView.date = todo.date

        // 완료 버튼을 클릭하면 수정.
        doneFab.setOnClickListener { updateTodo(id) }
        
        // 삭제 버튼을 클릭하면 삭제
        deleteFab.setOnClickListener { deleteTodo(id) }
    }

    // 액티비티 소멸 생명주기
    override fun onDestroy() {
        super.onDestroy()
        // 인스턴스 해제
        realm.close()
    }


}