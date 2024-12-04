package com.example.calliope.data.repository

import com.example.calliope.data.entity.ChatRecordEntity
import com.example.calliope.data.entity.DialogEntity
import com.example.calliope.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * @author Sunny
 * @date 2024/11/20/上午11:14
 */
interface ChatRepository {
    fun getChatRecordsByDialog(dialogId: Int): Flow<List<ChatRecordEntity>>
    fun getChatRecordsByDialogAndUser(dialogId: Int, userId: Int, limit: Int = 50): Flow<List<ChatRecordEntity>>
    suspend fun insertChatRecord(chatRecord: ChatRecordEntity): Result<Long>
    suspend fun updateChatRecord(chatRecord: ChatRecordEntity): Result<Unit>
    suspend fun deleteChatRecord(chatRecord: ChatRecordEntity): Result<Unit>
    suspend fun deleteAllChatRecordsByDialog(dialogId: Int): Result<Unit>
}
