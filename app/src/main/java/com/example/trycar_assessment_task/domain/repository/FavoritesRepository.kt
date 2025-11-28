package com.example.trycar_assessment_task.domain.repository

import com.example.trycar_assessment_task.data.local.entity.FavoritePostEntity
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository{
    fun getAllFavorites(): Flow<List<FavoritePostEntity>>
    suspend fun addToFavorites(post: FavoritePostEntity)
    suspend fun removeFromFavorites(postId: Int)
    suspend fun isFavorite(postId: Int): Boolean
    suspend fun syncFavorites()

}