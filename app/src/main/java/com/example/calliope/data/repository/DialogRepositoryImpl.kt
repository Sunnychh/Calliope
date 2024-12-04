package com.example.calliope.data.repository

import com.example.calliope.data.dao.DialogDao
import com.example.calliope.data.entity.DialogEntity
import com.example.calliope.utils.Result
import com.example.calliope.utils.safeCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Sunny
 * @date 2024/11/22/下午2:20
 */
class DialogRepositoryImpl @Inject constructor(
    private val dialogDao: DialogDao
) : DialogRepository {

    override fun getAllDialogs(): Flow<List<DialogEntity>> {
        return dialogDao.getAllDialogs()
    }

    override fun getDialogsByUser(userId: Int): Flow<List<DialogEntity>> {
        return dialogDao.getDialogsByUser(userId)
    }

    override suspend fun getDialogById(id: Int): Result<DialogEntity> = safeCall {
        dialogDao.getDialogById(id) ?: throw Exception("Dialog not found")
    }

    override suspend fun createDialog(dialog: DialogEntity): Result<Long> = safeCall {
        dialogDao.insertDialog(dialog)
    }

    override suspend fun updateDialog(dialog: DialogEntity): Result<Unit> = safeCall {
        dialogDao.updateDialog(dialog)
    }

    override suspend fun deleteDialog(dialog: DialogEntity): Result<Unit> = safeCall {
        dialogDao.deleteDialog(dialog)
    }

    override suspend fun searchDialogs(query: String): Result<List<DialogEntity>> = safeCall {
        dialogDao.searchDialogs("%$query%")
    }
}
