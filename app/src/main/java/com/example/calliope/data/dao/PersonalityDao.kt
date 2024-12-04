package com.example.calliope.data.dao

import androidx.room.*
import com.example.calliope.data.entity.PersonalityEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Sunny
 * @date 2024/11/20/上午11:00
 */
@Dao
interface PersonalityDao {
    @Query("SELECT * FROM personality")
    fun getAllPersonalities(): Flow<List<PersonalityEntity>>

    @Query("SELECT * FROM personality WHERE id = :id")
    suspend fun getPersonalityById(id: Int): PersonalityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonality(personality: PersonalityEntity): Long

    @Update
    suspend fun updatePersonality(personality: PersonalityEntity)

    @Delete
    suspend fun deletePersonality(personality: PersonalityEntity)

    @Query("DELETE FROM personality")
    suspend fun deleteAllPersonalities()
}
