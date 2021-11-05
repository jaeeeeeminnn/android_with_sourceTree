package com.example.restpoints.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.restpoints.model.RestpointModel

@Entity
class RestpointEntity (
    @PrimaryKey
    val unitCode : String,
    val unitName : String,
    val routeName : String,
    val xValue : String,
    val yValue : String,
    val addr : String,
    val weatherContents : String,
    val tempValue : String,
    val highestTemp : String,
    val lowestTemp : String,
    val rainfallValue : String,
    val snowValue : String,
    val windContents : String,
    val windValue : String
) {}