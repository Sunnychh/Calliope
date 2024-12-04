package com.example.calliope.data.dao

import androidx.room.*
import com.example.calliope.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Sunny
 * @date 2024/11/20/上午11:01
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM user WHERE name LIKE :query")
    fun searchUsers(query: String): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}
