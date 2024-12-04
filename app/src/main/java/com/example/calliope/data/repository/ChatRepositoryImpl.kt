package com.example.calliope.data.repository

import com.example.calliope.data.dao.ChatRecordDao
import com.example.calliope.data.entity.ChatRecordEntity
import com.example.calliope.utils.Result
import com.example.calliope.utils.safeCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Sunny
 * @date 2024/11/20/上午11:14
 */
class ChatRepositoryImpl @Inject constructor(
    private val chatRecordDao: ChatRecordDao
) : ChatRepository {

    override fun getChatRecordsByDialog(dialogId: Int): Flow<List<ChatRecordEntity>> {
        return chatRecordDao.getChatRecordsByDialog(dialogId)
    }

    override fun getChatRecordsByDialogAndUser(dialogId: Int, userId: Int, limit: Int): Flow<List<ChatRecordEntity>> {
        return chatRecordDao.getChatRecordsByDialogAndUser(dialogId, userId, limit)
    }

    override suspend fun insertChatRecord(chatRecord: ChatRecordEntity): Result<Long> = safeCall {
        chatRecordDao.insertChatRecord(chatRecord)
    }

    override suspend fun updateChatRecord(chatRecord: ChatRecordEntity): Result<Unit> = safeCall {
        chatRecordDao.updateChatRecord(chatRecord)
    }

    override suspend fun deleteChatRecord(chatRecord: ChatRecordEntity): Result<Unit> = safeCall {
        chatRecordDao.deleteChatRecord(chatRecord)
    }

    override suspend fun deleteAllChatRecordsByDialog(dialogId: Int): Result<Unit> = safeCall {
        chatRecordDao.deleteAllChatRecordsByDialog(dialogId)
    }
}
