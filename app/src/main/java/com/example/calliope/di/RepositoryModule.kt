package com.example.calliope.di

import com.example.calliope.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Sunny
 * @date 2024/11/22/下午2:23
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPersonalityRepository(
        personalityRepositoryImpl: PersonalityRepositoryImpl
    ): PersonalityRepository

    @Binds
    @Singleton
    abstract fun bindDialogRepository(
        dialogRepositoryImpl: DialogRepositoryImpl
    ): DialogRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindAIChatThreadRepository(
        aiChatThreadRepositoryImpl: AIChatThreadRepositoryImpl
    ): AIChatThreadRepository
}
