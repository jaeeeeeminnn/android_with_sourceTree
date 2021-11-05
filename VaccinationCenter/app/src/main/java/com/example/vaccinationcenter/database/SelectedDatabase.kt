package com.example.vaccinationcenter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vaccinationcenter.database.dao.SelectedDao
import com.example.vaccinationcenter.database.entity.Selected

@Database (entities = [Selected::class], version = 1)
abstract class SelectedDatabase : RoomDatabase() {
    abstract fun SelectedDao() : SelectedDao

    // singleton
    companion object {
        private var instance : SelectedDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : SelectedDatabase? {
            if (instance == null) {
                synchronized(SelectedDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SelectedDatabase::class.java,
                        "selected-database"
                    ).build()
                }
            }
            return instance
        }
    }
}