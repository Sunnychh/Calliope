package com.example.calliope.data.repository

import com.example.calliope.data.entity.UserEntity
import com.example.calliope.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * @author Sunny
 * @date 2024/11/22/下午2:16
 */
interface UserRepository {
    fun getAllUsers(): Flow<List<UserEntity>>
    suspend fun getUserById(id: Int): Result<UserEntity>
    fun searchUsers(query: String): Flow<List<UserEntity>>
    suspend fun insertUser(user: UserEntity): Result<Long>
    suspend fun updateUser(user: UserEntity): Result<Unit>
    suspend fun deleteUser(user: UserEntity): Result<Unit>
}
