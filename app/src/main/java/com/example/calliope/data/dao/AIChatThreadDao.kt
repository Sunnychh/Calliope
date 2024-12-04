package com.example.calliope.data.dao

import androidx.room.*
import com.example.calliope.data.entity.AIChatThreadEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Sunny
 * @date 2024/11/20/上午11:01
 */
@Dao
interface AIChatThreadDao {
    @Query("SELECT * FROM ai_chat_thread WHERE dialogId = :dialogId")
    fun getThreadsByDialog(dialogId: Int): Flow<List<AIChatThreadEntity>>

    @Query("SELECT * FROM ai_chat_thread WHERE id = :id")
    suspend fun getThreadById(id: Int): AIChatThreadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThread(thread: AIChatThreadEntity): Long

    @Update
    suspend fun updateThread(thread: AIChatThreadEntity)

    @Delete
    suspend fun deleteThread(thread: AIChatThreadEntity)

    @Query("DELETE FROM ai_chat_thread WHERE dialogId = :dialogId")
    suspend fun deleteAllThreadsByDialog(dialogId: Int)
}
