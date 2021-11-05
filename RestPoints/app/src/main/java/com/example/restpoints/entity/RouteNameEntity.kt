package com.example.restpoints.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RouteNameEntity (
    @PrimaryKey
    val routeNo : Int,
    val routeName : String
) {}