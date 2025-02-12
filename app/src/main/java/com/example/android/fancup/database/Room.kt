package com.example.android.fancup.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.android.fancup.database.dao.UserDao
import com.example.android.fancup.database.entity.DatabaseUser

@Database(entities = [DatabaseUser::class], version = 2)
abstract class FanCupDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}

private lateinit var INSTANCE: FanCupDatabase

fun getDatabase(context: Context): FanCupDatabase {
    synchronized(FanCupDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                FanCupDatabase::class.java,
                "fan_cup"
            ).fallbackToDestructiveMigrationFrom().build()
        }
    }
    return INSTANCE
}