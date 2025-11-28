package com.example.trycar_assessment_task.data.repository

import android.util.Log
import com.example.trycar_assessment_task.Network.INetworkConnectivityObserver
import com.example.trycar_assessment_task.data.local.dao.PostDao
import com.example.trycar_assessment_task.data.local.entity.PostEntity
import com.example.trycar_assessment_task.data.model.Comment
import com.example.trycar_assessment_task.data.model.Post
import com.example.trycar_assessment_task.data.remote.api.PostsApiService
import com.example.trycar_assessment_task.domain.repository.PostsRepository
import com.example.trycar_assessment_task.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class PostsRepositoryImpl @Inject constructor(
    private val apiService: PostsApiService,
    private val postDao: PostDao,
    private val networkObserver: INetworkConnectivityObserver
) : PostsRepository {

    override suspend fun getPosts(forceRefresh: Boolean): Flow<Resource<List<Post>>> = flow {
        Log.d(TAG, "getPosts called - forceRefresh: $forceRefresh")
        emit(Resource.Loading())

        val isNetworkAvailable = networkObserver.isNetworkAvailable()
        Log.d(TAG, "Network available: $isNetworkAvailable")

        // Try to get cached posts first
        val cachedPosts = postDao.getAllPosts().first()
        Log.d(TAG, "Cached posts count: ${cachedPosts.size}")

        if (!isNetworkAvailable && cachedPosts.isNotEmpty()) {
            // Offline mode - return cached data
            Log.d(TAG, "Offline mode - returning ${cachedPosts.size} cached posts")
            emit(Resource.Success(cachedPosts.map { it.toPost() }))
            return@flow
        }

        if (!isNetworkAvailable && cachedPosts.isEmpty()) {
            // No network and no cache
            Log.e(TAG, "No network and no cache available")
            emit(Resource.Error("No internet connection and no cached data available"))
            return@flow
        }

        // Network is available - fetch from API
        try {
            Log.d(TAG, "Fetching posts from API...")
            val posts = apiService.getPosts()
            Log.d(TAG, "Successfully fetched ${posts.size} posts from API")

            // Cache the posts
            Log.d(TAG, "Caching posts to database...")
            postDao.clearAllPosts()
            postDao.insertPosts(posts.map { it.toEntity() })
            Log.d(TAG, "Posts cached successfully")

            emit(Resource.Success(posts))
        } catch (e: Exception) {
            // API call failed - fallback to cache if available
            Log.e(TAG, "API call failed: ${e.message}", e)
            if (cachedPosts.isNotEmpty()) {
                Log.d(TAG, "Falling back to ${cachedPosts.size} cached posts")
                emit(Resource.Success(cachedPosts.map { it.toPost() }))
            } else {
                Log.e(TAG, "No cache available for fallback")
                emit(Resource.Error(e.localizedMessage ?: "An error occurred while fetching posts"))
            }
        }
    }

    override suspend fun getComments(postId: Int): Resource<List<Comment>> {
        Log.d(TAG, "getComments called for postId: $postId")
        return try {
            if (!networkObserver.isNetworkAvailable()) {
                Log.e(TAG, "No network available for fetching comments")
                return Resource.Error("No internet connection")
            }

            Log.d(TAG, "Fetching comments from API for post $postId...")
            val comments = apiService.getComments(postId)
            Log.d(TAG, "Successfully fetched ${comments.size} comments")
            Resource.Success(comments)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch comments: ${e.message}", e)
            Resource.Error(e.localizedMessage ?: "An error occurred while fetching comments")
        }
    }

    companion object {
        private const val TAG = "PostsRepository"
    }

    // Extension functions for mapping
    private fun Post.toEntity() = PostEntity(
        id = id,
        userId = userId,
        title = title,
        body = body
    )

    private fun PostEntity.toPost() = Post(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}
