package com.example.calliope.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.calliope.data.converter.JsonConverter

/**
 * @author Sunny
 * @date 2024/11/20/上午10:50
 */
@Entity(
    tableName = "ai_chat_thread",
    indices = [Index(value = ["name"], name = "idx_thread_name")]
)
data class AIChatThreadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dialogId: Int,
    val name: String,
    @TypeConverters(JsonConverter::class)
    val history: Map<String, Any>,
    @TypeConverters(JsonConverter::class)
    val extend: Map<String, Any>
)
