package com.example.calliope.data.entity

import androidx.room.*
import com.example.calliope.data.converter.JsonConverter
import java.util.Date
/**
 * @author Sunny
 * @date 2024/11/20/上午10:49
 */
@Entity(
    tableName = "chat_record",
    indices = [
        Index(value = ["dialogId", "userId"], name = "idx_dialog_user"),
        Index(value = ["userId"], name = "idx_user")
    ]
)
data class ChatRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dialogId: Int,
    val userId: Int,
    val message: String,
    @TypeConverters(JsonConverter::class)
    val time: Date,
    @TypeConverters(JsonConverter::class)
    val extend: Map<String, Any>
)
