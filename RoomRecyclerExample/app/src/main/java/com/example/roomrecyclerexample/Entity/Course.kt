package com.example.roomrecyclerexample.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Course (
  @PrimaryKey
  val courseId : Int,
  val courseName : String
){}