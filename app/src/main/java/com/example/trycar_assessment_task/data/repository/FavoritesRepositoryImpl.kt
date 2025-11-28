package com.example.trycar_assessment_task.data.repository

import android.util.Log
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
        Log.d(TAG, "getAllFavorites called")
        return favoritePostDao.getAllFavorites()
    }

    override suspend fun addToFavorites(post: FavoritePostEntity) {
        val isNetworkAvailable = networkObserver.isNetworkAvailable()
        Log.d(TAG, "addToFavorites - postId: ${post.id}, network: $isNetworkAvailable")
        // If offline, mark as unsynced; if online, mark as synced
        val postToInsert = post.copy(isSynced = isNetworkAvailable)
        favoritePostDao.insertFavorite(postToInsert)
        Log.d(TAG, "Post ${post.id} added to favorites (synced: $isNetworkAvailable)")
    }

    override suspend fun removeFromFavorites(postId: Int) {
        Log.d(TAG, "removeFromFavorites - postId: $postId")
        favoritePostDao.deleteFavorite(postId)
        Log.d(TAG, "Post $postId removed from favorites")
    }

    override suspend fun isFavorite(postId: Int): Boolean {
        val isFav = favoritePostDao.getFavoriteById(postId) != null
        Log.d(TAG, "isFavorite - postId: $postId, result: $isFav")
        return isFav
    }

    override suspend fun syncFavorites() {
        Log.d(TAG, "syncFavorites called")
        if (!networkObserver.isNetworkAvailable()) {
            Log.d(TAG, "Network not available, skipping sync")
            return
        }

        // Get all unsynced favorites
        val unsyncedFavorites = favoritePostDao.getUnsyncedFavorites()
        Log.d(TAG, "Found ${unsyncedFavorites.size} unsynced favorites")

        // Simulate sync by marking them as synced
        unsyncedFavorites.forEach { favorite ->
            favoritePostDao.updateSyncStatus(favorite.id, isSynced = true)
            Log.d(TAG, "Synced favorite: ${favorite.id}")
        }
        Log.d(TAG, "Sync completed")
    }

    companion object {
        private const val TAG = "FavoritesRepository"
    }
}
