package com.example.trycar_assessment_task.data.repository

import com.example.trycar_assessment_task.Network.INetworkConnectivityObserver
import com.example.trycar_assessment_task.data.local.dao.FavoritePostDao
import com.example.trycar_assessment_task.data.local.entity.FavoritePostEntity
import com.example.trycar_assessment_task.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoritePostDao: FavoritePostDao,
    private val networkObserver: INetworkConnectivityObserver
) : FavoritesRepository {

    override fun getAllFavorites(): Flow<List<FavoritePostEntity>> {
        return favoritePostDao.getAllFavorites()
    }

    override suspend fun addToFavorites(post: FavoritePostEntity) {
        val isNetworkAvailable = networkObserver.isNetworkAvailable()
        // If offline, mark as unsynced; if online, mark as synced
        val postToInsert = post.copy(isSynced = isNetworkAvailable)
        favoritePostDao.insertFavorite(postToInsert)
    }

    override suspend fun removeFromFavorites(postId: Int) {
        favoritePostDao.deleteFavorite(postId)
    }

    override suspend fun isFavorite(postId: Int): Boolean {
        return favoritePostDao.getFavoriteById(postId) != null
    }

    override suspend fun syncFavorites() {
        if (!networkObserver.isNetworkAvailable()) {
            return
        }

        // Get all unsynced favorites
        val unsyncedFavorites = favoritePostDao.getUnsyncedFavorites()

        // Simulate sync by marking them as synced
        unsyncedFavorites.forEach { favorite ->
            favoritePostDao.updateSyncStatus(favorite.id, isSynced = true)
        }
    }
}
