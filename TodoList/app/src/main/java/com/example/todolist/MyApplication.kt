package com.example.todolist

import android.app.Application
import io.realm.Realm

// Application 클래스를 상속받는 MyApplication 클래스를 선언
// Application 클래스가 앱 실행 시 가장 먼저 수행되는 객체임.
class MyApplication: Application() {
    // onCreate() 메소드를 오버드라이브.
    // onCreate() 메소드는 액티비티가 생성되기 전에 호출됨.
    override fun onCreate() {
        super.onCreate()
        // init() 으로 Realm 초기화
        Realm.init(this)
    }
}