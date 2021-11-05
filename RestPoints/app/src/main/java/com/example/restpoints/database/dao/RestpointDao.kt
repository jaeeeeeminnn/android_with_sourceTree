package com.example.restpoints.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restpoints.entity.RestpointEntity

@Dao
interface RestpointDao {

    /**
     * insert
     *
     * RestpointFragment
     * 1. unitCode 먼저 추출
     * 2. Model -> Entity
     *
     * FavoriteFragment
     * 1. Entity -> Model
     */
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(restpointEntity: RestpointEntity?)

    @Query ("SELECT * FROM RestpointEntity")
    fun findAll() : List<RestpointEntity>

    @Query ("DELETE FROM RestpointEntity")
    fun deleteAll()

    @Query ("DELETE FROM RestpointEntity WHERE unitCode = :unitCode")
    fun delete(unitCode : String)

    @Query ("SELECT COUNT(*) FROM RestpointEntity")
    fun getSize() : Int
}