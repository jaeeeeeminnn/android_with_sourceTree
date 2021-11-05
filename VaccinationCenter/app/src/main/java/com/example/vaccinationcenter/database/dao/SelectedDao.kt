package com.example.vaccinationcenter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vaccinationcenter.database.entity.Selected

@Dao
interface SelectedDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(selected: Selected)

    @Query ("DELETE FROM Selected WHERE id = :id")
    fun delete(id : Int)

    @Query ("SELECT * FROM Selected")
    fun getAll() : List<Selected>

    @Query ("DELETE FROM Selected")
    fun deleteAll()
}