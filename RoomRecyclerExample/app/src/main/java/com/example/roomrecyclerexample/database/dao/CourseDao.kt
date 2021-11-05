package com.example.roomrecyclerexample.database.dao

import androidx.room.*
import com.example.roomrecyclerexample.Entity.Course

@Dao
interface CourseDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(course : Course)

    @Update
    fun update(course : Course)

    @Query ("DELETE FROM Course WHERE courseId = :courseId")
    fun deleteById(courseId : String)

    @Query("DELETE FROM Course WHERE courseName = :courseName")
    fun deleteByName(courseName: String)

    @Query ("SELECT * FROM Course")
    fun getAll() : List<Course>

    @Query ("DELETE FROM Course")
    fun deleteAll()

}