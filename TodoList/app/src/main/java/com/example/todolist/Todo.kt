package com.example.todolist

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/*  Todo.kt

    데이터베이스명 : todolist
    테이블 명 : todo
    column
        - id    : Long, 자동 증가, 고유한 값
        - title : String, 할 일 내용
        - date  : Long, 시간
 */

// 2.e Realm 초기화

// 코틀린에서는 Realm에서 사용하는 클래스에 open 키워드를 추가.
open class Todo(
    @PrimaryKey var id: Long = 0,
    var title: String = "",
    var date: Long = 0)
    : RealmObject() {
    // id는 유일한 값이 되어야 하기 때문에 기본키 제약을 주석으로 추가.
    // 기본키 제약은 Realm에서 제공하는 주석.
    // 주석이 부여된 속성값은 중복허용 x

    // RealmObject 클래스를 상속받아 Realm 데이터베이스에서 다룰 수 있다.
}