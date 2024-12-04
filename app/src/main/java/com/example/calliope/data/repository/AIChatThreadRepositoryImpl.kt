package com.example.calliope.data.repository

import com.example.calliope.data.dao.AIChatThreadDao
import com.example.calliope.data.entity.AIChatThreadEntity
import com.example.calliope.utils.Result
import com.example.calliope.utils.safeCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Sunny
 * @date 2024/11/22/下午2:18
 */
class AIChatThreadRepositoryImpl @Inject constructor(
    private val aiChatThreadDao: AIChatThreadDao
) : AIChatThreadRepository {

    override fun getThreadsByDialog(dialogId: Int): Flow<List<AIChatThreadEntity>> {
        return aiChatThreadDao.getThreadsByDialog(dialogId)
    }

    override suspend fun getThreadById(id: Int): Result<AIChatThreadEntity> = safeCall {
        aiChatThreadDao.getThreadById(id) ?: throw Exception("Thread not found")
    }

    override suspend fun createThread(thread: AIChatThreadEntity): Result<Long> = safeCall {
        aiChatThreadDao.insertThread(thread)
    }

    override suspend fun updateThread(thread: AIChatThreadEntity): Result<Unit> = safeCall {
        aiChatThreadDao.updateThread(thread)
    }

    override suspend fun deleteThread(thread: AIChatThreadEntity): Result<Unit> = safeCall {
        aiChatThreadDao.deleteThread(thread)
    }

    override suspend fun deleteAllThreadsByDialog(dialogId: Int): Result<Unit> = safeCall {
        aiChatThreadDao.deleteAllThreadsByDialog(dialogId)
    }
}
