package com.example.calliope.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.calliope.data.converter.JsonConverter
import com.example.calliope.data.dao.*
import com.example.calliope.data.entity.*

/**
 * @author Sunny
 * @date 2024/11/20/上午11:04
 */
@Database(
    entities = [
        PersonalityEntity::class,
        DialogEntity::class,
        ChatRecordEntity::class,
        UserEntity::class,
        AIChatThreadEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(JsonConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personalityDao(): PersonalityDao
    abstract fun dialogDao(): DialogDao
    abstract fun chatRecordDao(): ChatRecordDao
    abstract fun userDao(): UserDao
    abstract fun aiChatThreadDao(): AIChatThreadDao

    companion object {
        const val DATABASE_NAME = "calliope_db"
    }
}
