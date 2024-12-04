package com.example.calliope.data.dao

import androidx.room.*
import com.example.calliope.data.entity.DialogEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Sunny
 * @date 2024/11/20/上午11:01
 */
@Dao
interface DialogDao {
    @Query("SELECT * FROM dialog")
    fun getAllDialogs(): Flow<List<DialogEntity>>

    @Query("SELECT * FROM dialog WHERE id = :id")
    suspend fun getDialogById(id: Int): DialogEntity?

    // 新增方法
    @Query("SELECT d.* FROM dialog d JOIN chat_record cr ON d.id = cr.dialogId WHERE cr.userId = :userId GROUP BY d.id")
    fun getDialogsByUser(userId: Int): Flow<List<DialogEntity>>

    // 新增方法
    @Query("SELECT * FROM dialog WHERE name LIKE :query")
    suspend fun searchDialogs(query: String): List<DialogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDialog(dialog: DialogEntity): Long

    @Update
    suspend fun updateDialog(dialog: DialogEntity)

    @Delete
    suspend fun deleteDialog(dialog: DialogEntity)
}
