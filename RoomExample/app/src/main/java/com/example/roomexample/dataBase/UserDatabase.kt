package com.example.roomexample.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomexample.entity.User

/**
 * 3. RoomDatabase 상속
 *
 * 데이터베이스를 생성하고 관리하는 데이터베이스 객체
 * RoomDatabase 객체를 상속받아야 하므로 abstract class로 선언.
 *
 * @Database annotation에 데이터베이스에 들어갈 entities를 선언.
 * 들어갈 객체가 많다면 ','를 통해 열거해주면 된다.
 * version은 처음 데이터베이스를 생성한다면 1로 설정하면 된다.
 *
 */
@Database (entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao() : UserDao

    /**
     * 4. 데이터베이스 사용 (singleton)
     *
     * 안드로이드 스튜디오는 공식적으로 데이터베이스 객체 인스턴스를 싱글톤으로 하도록 권장.
     *
     * companion object{} : 싱글톤 선언
     * databaseBuilder()  : 데이터베이스 객체 선언 (인스턴스)
     *
     * databaseBuilder(Context, DataBase 클래스, database 이름)
     *
     */
    companion object {
        private var instance: UserDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : UserDatabase? {
            if (instance == null) {
                synchronized(UserDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user-database"
                    ).build()
                }
            }
            return instance
        }
    }
}