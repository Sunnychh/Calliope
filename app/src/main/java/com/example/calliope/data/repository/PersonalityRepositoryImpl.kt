package com.example.calliope.data.repository

import com.example.calliope.data.dao.PersonalityDao
import com.example.calliope.data.entity.PersonalityEntity
import com.example.calliope.utils.Result
import com.example.calliope.utils.safeCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Sunny
 * @date 2024/11/20/上午11:13
 */
class PersonalityRepositoryImpl @Inject constructor(
    private val personalityDao: PersonalityDao
) : PersonalityRepository {

    override fun getAllPersonalities(): Flow<List<PersonalityEntity>> {
        return personalityDao.getAllPersonalities()
    }

    override suspend fun getPersonalityById(id: Int): Result<PersonalityEntity> = safeCall {
        personalityDao.getPersonalityById(id) ?: throw Exception("Personality not found")
    }

    override suspend fun insertPersonality(personality: PersonalityEntity): Result<Long> = safeCall {
        personalityDao.insertPersonality(personality)
    }

    override suspend fun updatePersonality(personality: PersonalityEntity): Result<Unit> = safeCall {
        personalityDao.updatePersonality(personality)
    }

    override suspend fun deletePersonality(personality: PersonalityEntity): Result<Unit> = safeCall {
        personalityDao.deletePersonality(personality)
    }
}
