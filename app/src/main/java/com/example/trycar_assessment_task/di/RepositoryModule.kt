package com.example.trycar_assessment_task.di

import com.example.trycar_assessment_task.data.repository.FavoritesRepositoryImpl
import com.example.trycar_assessment_task.data.repository.PostsRepositoryImpl
import com.example.trycar_assessment_task.domain.repository.FavoritesRepository
import com.example.trycar_assessment_task.domain.repository.PostsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPostsRepository(
        postsRepositoryImpl: PostsRepositoryImpl
    ): PostsRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository
}
