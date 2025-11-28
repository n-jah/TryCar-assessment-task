package com.example.trycar_assessment_task.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoritePostDao {

    @Query("SELECT * FROM favorite_posts ORDER BY id DESC")
    fun getAllFavorites(): Flow<List<FavoritePostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(post: FavoritePostEntity)

    @Query("DELETE FROM favorite_posts WHERE id = :postId")
    suspend fun deleteFavorite(postId: Int)

    @Query("UPDATE favorite_posts SET isSynced = :isSynced WHERE id = :postId")
    suspend fun updateSyncStatus(postId: Int, isSynced: Boolean)

    @Query("SELECT * FROM favorite_posts WHERE isSynced = 0")
    suspend fun getUnsyncedFavorites(): List<FavoritePostEntity>

    @Query("SELECT * FROM favorite_posts WHERE id = :postId")
    suspend fun getFavoriteById(postId: Int): FavoritePostEntity?

}