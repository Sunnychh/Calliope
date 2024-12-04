package com.example.calliope.di

import android.content.Context
import androidx.room.Room
import com.example.calliope.data.database.AppDatabase
import com.example.calliope.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Sunny
 * @date 2024/11/20/上午11:07
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun providePersonalityDao(database: AppDatabase): PersonalityDao {
        return database.personalityDao()
    }

    @Provides
    fun provideDialogDao(database: AppDatabase): DialogDao {
        return database.dialogDao()
    }

    @Provides
    fun provideChatRecordDao(database: AppDatabase): ChatRecordDao {
        return database.chatRecordDao()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideAiChatThreadDao(database: AppDatabase): AIChatThreadDao {
        return database.aiChatThreadDao()
    }
}
