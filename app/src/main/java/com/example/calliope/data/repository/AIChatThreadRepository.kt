package com.example.calliope.data.repository

import com.example.calliope.data.entity.AIChatThreadEntity
import com.example.calliope.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * @author Sunny
 * @date 2024/11/22/下午2:18
 */
interface AIChatThreadRepository {
    fun getThreadsByDialog(dialogId: Int): Flow<List<AIChatThreadEntity>>
    suspend fun getThreadById(id: Int): Result<AIChatThreadEntity>
    suspend fun createThread(thread: AIChatThreadEntity): Result<Long>
    suspend fun updateThread(thread: AIChatThreadEntity): Result<Unit>
    suspend fun deleteThread(thread: AIChatThreadEntity): Result<Unit>
    suspend fun deleteAllThreadsByDialog(dialogId: Int): Result<Unit>
}
