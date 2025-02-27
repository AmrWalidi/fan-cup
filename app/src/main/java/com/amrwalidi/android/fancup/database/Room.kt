package com.amrwalidi.android.fancup.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.amrwalidi.android.fancup.database.dao.CategoryDao
import com.amrwalidi.android.fancup.database.dao.UserDao
import com.amrwalidi.android.fancup.database.entity.DatabaseCategory
import com.amrwalidi.android.fancup.database.entity.DatabaseUser

@Database(entities = [DatabaseUser::class, DatabaseCategory::class], version = 4)
abstract class FanCupDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val categoryDao : CategoryDao
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