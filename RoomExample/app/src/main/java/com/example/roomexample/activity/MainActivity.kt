package com.example.roomexample.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.roomexample.R
import com.example.roomexample.dataBase.UserDatabase
import com.example.roomexample.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    lateinit var mDb : UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDb = UserDatabase.getInstance(applicationContext)!!

        /**
         * 5. 데이터베이스 사용 (강제 실행 / 코루틴 비동기)
         *
         * 5-1. 싱글톤 사용 여부
         * 5-2. 코루틴 사용 여부
         * 
         */

        // Entity 추가 데이터.
        var newUser = User("1","홍길동", "20", "1")
        var newUser2 = User("2","가", "21", "2")
        var newUser3 = User("3","나", "22", "3")
        var newUser4 = User("4","다", "23", "4")
        var newUser5 = User("5","라", "24", "5")


        // 5-1. 싱글톤 사용 여부
        // 5-2. 코루틴 사용 여부
        /* 싱글톤 패턴을 사용하지 않는 경우 + 코루틴 사용하지 않는 경우
        
            val db = Room.databaseBuilder(
                applicationContext, 
                AppDatabase::class.java,
                "user-database"
            ).allowMainThreadQueries().build()
            // allowMainThreadQueries() 로 강제로 Main Thread에게 실행시킬 수 있다.
            db.UserDao().insert(newUser)
        
         */
        // 싱글톤 사용하는 경우
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                mDb.userDao().insert(newUser)
                mDb.userDao().insert(newUser2)
                mDb.userDao().delete(newUser.name)
                mDb.userDao().insert(newUser2)
                mDb.userDao().update(newUser2.name, newUser5.name)
            }.await()

            val dbList = CoroutineScope(Dispatchers.IO).async {
                mDb.userDao().getAll()
            }.await()

            if (dbList.isEmpty()) {
                Log.d("dbList", "is Empty")
            } else {
                Log.d("dbList", "$dbList")
                dbList.forEach {
                    Log.d("ID", "${it.id}")
                }
            }
        }
    }
}