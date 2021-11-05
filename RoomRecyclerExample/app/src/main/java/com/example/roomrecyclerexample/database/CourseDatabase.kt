package com.example.roomrecyclerexample.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomrecyclerexample.Entity.Course
import com.example.roomrecyclerexample.database.dao.CourseDao

@Database (entities = [Course::class], version = 1)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun CourseDao() : CourseDao

    // 싱글톤
    companion object {
        private var instance: CourseDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : CourseDatabase? {
            if (instance == null) {
                synchronized(CourseDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CourseDatabase::class.java,
                        "course-database"
                    ).build()
                }
            }
            return instance
        }
    }
}