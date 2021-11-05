package com.example.roomexample.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 1. Entity 생성
 *
 * 논리적 데이터베이스의 개체.
 * 물리적 데이터베이스의 테이블.
 *
 * 모든 entity는 하나의 Primarykey가 있어야 한다.
 *
 */
@Entity (tableName = "User")
data class User(
    @PrimaryKey
    var id : String,
    var name : String,
    var age : String,
    var phone : String
) {
//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0
}