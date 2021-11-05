package com.example.vaccinationcenter.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Selected(
    @PrimaryKey
    val id: Int,
    val centerName: String,
    val sido: String,
    val sigungu: String,
    val facilityName : String,
    val address : String,
    val phoneNumber : String
) { }