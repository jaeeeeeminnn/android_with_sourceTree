package com.example.jazzbargame.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Customer(
   @PrimaryKey
   val id: Int,
   val imageProfile : Int,
   val name : String,
   val gender : String,
   val money : Int
) {}

