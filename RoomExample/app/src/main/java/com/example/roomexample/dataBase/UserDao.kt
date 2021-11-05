package com.example.roomexample.dataBase

import androidx.room.*
import com.example.roomexample.entity.User

/**
 * 2. DAO (Data Access Objects) 생성
 *
 * 데이터에 접근할 수 있는 메소드를 정의한 인터페이스
 *
 * insert, update, delete로 데이터베이스가 기본적으로 수행해야하는 메소드를 annotation한다.
 *
 * INSERT INTO [entity] ([col1], [col2], ...) VALUE ([item1], [item2], ...)
 * UPDATE [entity] SET [col] = [item] WHERE [condition]
 * DELETE FROM [entity] WHERE [condition]
 *
 * 추가로 필요한 기능은 @Query annotation으로 선언하고 SQLite를 사용하면 된다.
 */

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user : User)

    @Query("UPDATE User SET name = :before WHERE name = :after")
    fun update(after : String, before: String)

    @Query("DELETE FROM User WHERE name = :name")
    fun delete(name : String)

    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("DELETE FROM User WHERE name = :name")
    fun deleteUserByName(name: String)

    @Query("DELETE FROM User")
    fun deleteAll()
}