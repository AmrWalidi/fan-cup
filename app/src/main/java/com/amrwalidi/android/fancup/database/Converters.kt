package com.amrwalidi.android.fancup.database

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
