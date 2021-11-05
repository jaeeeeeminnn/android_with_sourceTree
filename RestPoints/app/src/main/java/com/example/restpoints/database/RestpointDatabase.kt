package com.example.restpoints.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.restpoints.DEFINES
import com.example.restpoints.database.dao.RestpointDao
import com.example.restpoints.entity.RestpointEntity
import com.example.restpoints.entity.RouteNameEntity

@Database (entities = [RestpointEntity::class], version = 1)
abstract class RestpointDatabase : RoomDatabase() {
    abstract fun RestpointDao() : RestpointDao

    companion object {
        private var instance : RestpointDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : RestpointDatabase? {
            if (instance == null) {
                synchronized(RestpointEntity::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RestpointDatabase::class.java,
                        DEFINES.DBTASK.DATABASE_NAME_RP
                    ).build()
                }
            }
            return instance
        }
    }
}