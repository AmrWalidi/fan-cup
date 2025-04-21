package com.amrwalidi.android.fancup.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.amrwalidi.android.fancup.database.dao.CategoryDao
import com.amrwalidi.android.fancup.database.dao.QuestionDao
import com.amrwalidi.android.fancup.database.dao.UserDao
import com.amrwalidi.android.fancup.database.entity.DatabaseCategory
import com.amrwalidi.android.fancup.database.entity.DatabaseQuestion
import com.amrwalidi.android.fancup.database.entity.DatabaseUser
import com.amrwalidi.android.fancup.database.entity.QuestionCategoryCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(
    entities = [DatabaseUser::class, DatabaseCategory::class, DatabaseQuestion::class, QuestionCategoryCrossRef::class],
    version = 10
)
@TypeConverters(Converters::class)
abstract class FanCupDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val categoryDao: CategoryDao
    abstract val questionDao: QuestionDao

    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        clearAllTables()
    }
}

private lateinit var INSTANCE: FanCupDatabase


fun getDatabase(context: Context): FanCupDatabase {
    synchronized(FanCupDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                FanCupDatabase::class.java,
                "fan_cup"
            ).fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}