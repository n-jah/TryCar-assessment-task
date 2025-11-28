package com.example.trycar_assessment_task.data.repository

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
        emit(Resource.Loading())

        val isNetworkAvailable = networkObserver.isNetworkAvailable()

        // Try to get cached posts first
        val cachedPosts = postDao.getAllPosts().first()

        if (!isNetworkAvailable && cachedPosts.isNotEmpty()) {
            // Offline mode - return cached data
            emit(Resource.Success(cachedPosts.map { it.toPost() }))
            return@flow
        }

        if (!isNetworkAvailable && cachedPosts.isEmpty()) {
            // No network and no cache
            emit(Resource.Error("No internet connection and no cached data available"))
            return@flow
        }

        // Network is available - fetch from API
        try {
            val posts = apiService.getPosts()

            // Cache the posts
            postDao.clearAllPosts()
            postDao.insertPosts(posts.map { it.toEntity() })

            emit(Resource.Success(posts))
        } catch (e: Exception) {
            // API call failed - fallback to cache if available
            if (cachedPosts.isNotEmpty()) {
                emit(Resource.Success(cachedPosts.map { it.toPost() }))
            } else {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred while fetching posts"))
            }
        }
    }

    override suspend fun getComments(postId: Int): Resource<List<Comment>> {
        return try {
            if (!networkObserver.isNetworkAvailable()) {
                return Resource.Error("No internet connection")
            }

            val comments = apiService.getComments(postId)
            Resource.Success(comments)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred while fetching comments")
        }
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
