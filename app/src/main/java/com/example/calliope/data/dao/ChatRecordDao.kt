package com.example.calliope.data.dao

import androidx.room.*
import com.example.calliope.data.entity.ChatRecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Sunny
 * @date 2024/11/20/上午11:01
 */
@Dao
interface ChatRecordDao {
    @Query("SELECT * FROM chat_record WHERE dialogId = :dialogId ORDER BY time DESC")
    fun getChatRecordsByDialog(dialogId: Int): Flow<List<ChatRecordEntity>>

    @Query("""
        SELECT * FROM chat_record 
        WHERE dialogId = :dialogId AND userId = :userId 
        ORDER BY time DESC 
        LIMIT :limit
    """)
    fun getChatRecordsByDialogAndUser(
        dialogId: Int,
        userId: Int,
        limit: Int = 50
    ): Flow<List<ChatRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRecord(chatRecord: ChatRecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRecords(chatRecords: List<ChatRecordEntity>)

    @Update
    suspend fun updateChatRecord(chatRecord: ChatRecordEntity)

    @Delete
    suspend fun deleteChatRecord(chatRecord: ChatRecordEntity)

    @Query("DELETE FROM chat_record WHERE dialogId = :dialogId")
    suspend fun deleteAllChatRecordsByDialog(dialogId: Int)
}
