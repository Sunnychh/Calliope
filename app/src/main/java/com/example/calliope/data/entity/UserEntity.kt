package com.example.calliope.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.calliope.data.converter.JsonConverter

/**
 * @author Sunny
 * @date 2024/11/20/上午10:49
 */
@Entity(
    tableName = "user",
    indices = [Index(value = ["name"], name = "idx_user_name")]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @TypeConverters(JsonConverter::class)
    val extend: Map<String, Any>
)
