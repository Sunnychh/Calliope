package com.example.calliope.data.repository

import com.example.calliope.data.entity.PersonalityEntity
import com.example.calliope.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * @author Sunny
 * @date 2024/11/20/上午11:12
 */
interface PersonalityRepository {
    fun getAllPersonalities(): Flow<List<PersonalityEntity>>
    suspend fun getPersonalityById(id: Int): Result<PersonalityEntity>
    suspend fun insertPersonality(personality: PersonalityEntity): Result<Long>
    suspend fun updatePersonality(personality: PersonalityEntity): Result<Unit>
    suspend fun deletePersonality(personality: PersonalityEntity): Result<Unit>
}
