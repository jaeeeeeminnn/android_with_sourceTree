package com.example.jazzbargame.Database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jazzbargame.entity.Customer

@Dao
interface CustomerDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(customer : Customer)

    @Query ("UPDATE Customer SET name = :name WHERE id = :id")
    fun updateName(name : String, id : Int)

    @Query ("UPDATE Customer SET gender = :gender WHERE id = :id")
    fun updateGender(gender : String, id : Int)

    @Query ("UPDATE Customer SET money = :money WHERE id = :id")
    fun updateMoney(money : String, id : Int)

    @Query ("SELECT * FROM Customer")
    fun getAll() : List<Customer>

    @Query ("DELETE FROM Customer")
    fun deleteAll()

    @Query ("DELETE FROM Customer WHERE id = :id")
    fun deleteById(id : Int)

    @Query ("DELETE FROM customer WHERE name = :name")
    fun deleteByName(name : String)

    @Query ("SELECT COUNT(*) FROM Customer")
    fun getCount() : Int

}