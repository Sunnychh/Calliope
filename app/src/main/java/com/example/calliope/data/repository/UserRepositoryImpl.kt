package com.example.calliope.data.repository

import com.example.calliope.data.dao.UserDao
import com.example.calliope.data.entity.UserEntity
import com.example.calliope.utils.Result
import com.example.calliope.utils.safeCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Sunny
 * @date 2024/11/22/下午2:17
 */
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getAllUsers(): Flow<List<UserEntity>> {
        return userDao.getAllUsers()
    }

    override suspend fun getUserById(id: Int): Result<UserEntity> = safeCall {
        userDao.getUserById(id) ?: throw Exception("User not found")
    }

    override fun searchUsers(query: String): Flow<List<UserEntity>> {
        return userDao.searchUsers("%$query%") // 添加通配符以进行模糊搜索
    }

    override suspend fun insertUser(user: UserEntity): Result<Long> = safeCall {
        userDao.insertUser(user)
    }

    override suspend fun updateUser(user: UserEntity): Result<Unit> = safeCall {
        userDao.updateUser(user)
    }

    override suspend fun deleteUser(user: UserEntity): Result<Unit> = safeCall {
        userDao.deleteUser(user)
    }
}
