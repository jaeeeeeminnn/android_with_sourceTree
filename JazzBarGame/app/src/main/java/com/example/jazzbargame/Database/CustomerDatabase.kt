package com.example.jazzbargame.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jazzbargame.Database.dao.CustomerDao
import com.example.jazzbargame.entity.Customer

@Database (entities = [Customer::class], version = 1)
abstract class CustomerDatabase : RoomDatabase() {
    abstract fun CustomerDao() : CustomerDao

    // 싱글톤
    companion object {
        private var instance: CustomerDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : CustomerDatabase? {
            if (instance == null) {
                synchronized(CustomerDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CustomerDatabase::class.java,
                        "customer-database"
                    ).build()
                }
            }
            return instance
        }
    }
}