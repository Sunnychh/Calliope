package com.example.calliope.data.repository

/**
 * @author Sunny
 * @date 2024/11/20/上午11:15
 */
import com.example.calliope.data.entity.DialogEntity
import com.example.calliope.utils.Result
import kotlinx.coroutines.flow.Flow

interface DialogRepository {
    fun getAllDialogs(): Flow<List<DialogEntity>>
    fun getDialogsByUser(userId: Int): Flow<List<DialogEntity>>
    suspend fun getDialogById(id: Int): Result<DialogEntity>
    suspend fun createDialog(dialog: DialogEntity): Result<Long>
    suspend fun updateDialog(dialog: DialogEntity): Result<Unit>
    suspend fun deleteDialog(dialog: DialogEntity): Result<Unit>
    suspend fun searchDialogs(query: String): Result<List<DialogEntity>>
}
