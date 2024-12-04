package com.example.calliope.data.converter

/**
 * @author Sunny
 * @date 2024/11/20/上午10:38
 */
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class JsonConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(value: String): Map<String, Any> {
        val mapType = object : TypeToken<Map<String, Any>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun toJson(value: Map<String, Any>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
