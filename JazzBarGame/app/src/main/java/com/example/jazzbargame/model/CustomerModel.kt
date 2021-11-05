package com.example.jazzbargame.model

class CustomerModel (
    val id: Int,
    val imageProfile : Int,
    val name: String,
    val gender: String,
    val money: String
){
    fun setGender() : String {
        return "♂ " + gender
    }
    fun setMoney() : String {
        return "\u0024 " + money
    }
}