package com.example.trycar_assessment_task.presentation.posts

import com.example.trycar_assessment_task.Network.INetworkConnectivityObserver
import com.example.trycar_assessment_task.data.model.Comment
import com.example.trycar_assessment_task.data.model.Post
import com.example.trycar_assessment_task.domain.repository.PostsRepository
import com.example.trycar_assessment_task.util.Resource
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Test

/**
 *  unit tests for Repository pattern
 * 
 * These tests verify that our fake implementations work correctly
 * In a real project, you would test actual repository logic
 */
class PostsRepositoryTest {

    @Test
    fun `fake repository returns success with posts`() {
        // Given: A repository with test data
        val testPosts = listOf(
            Post(1, 1, "Test Post 1", "Body 1"),
            Post(2, 1, "Test Post 2", "Body 2")
        )
        val repository = FakePostsRepository(shouldSucceed = true, posts = testPosts)
        
        // When: We get posts (this would normally be async, but fake is simple)
        var result: Resource<List<Post>>? = null
        kotlinx.coroutines.runBlocking {
            repository.getPosts().collect { result = it }
        }
        
        // Then: Result should be success with 2 posts
        assertTrue("Result should be Success", result is Resource.Success)
        assertEquals("Should have 2 posts", 2, (result as Resource.Success).data?.size)
    }

    @Test
    fun `fake repository returns error when configured`() {
        // Given: A repository configured to fail
        val repository = FakePostsRepository(shouldSucceed = false)
        
        // When: We get posts
        var result: Resource<List<Post>>? = null
        kotlinx.coroutines.runBlocking {
            repository.getPosts().collect { result = it }
        }
        
        // Then: Result should be error
        assertTrue("Result should be Error", result is Resource.Error)
        assertEquals("Error message should match", "Test error", (result as Resource.Error).message)
    }

    @Test
    fun `fake network observer returns correct status`() {
        // Given: Network observer with offline status
        val networkObserver = FakeNetworkObserver(isAvailable = false)
        
        // When: We check network status
        val isAvailable = networkObserver.isNetworkAvailable()
        
        // Then: Should return false
        assertFalse("Network should be unavailable", isAvailable)
    }

    @Test
    fun `fake network observer observes connectivity`() {
        // Given: Network observer with online status
        val networkObserver = FakeNetworkObserver(isAvailable = true)
        
        // When: We observe connectivity
        var status = false
        kotlinx.coroutines.runBlocking {
            networkObserver.observeNetworkConnectivity().collect { status = it }
        }
        
        // Then: Should emit true
        assertTrue("Network should be available", status)
    }
}

/**
 * Fake repository for testing
 * This simulates the real repository without needing actual network/database
 */
class FakePostsRepository(
    private val shouldSucceed: Boolean = true,
    private val posts: List<Post> = emptyList()
) : PostsRepository {
    
    override suspend fun getPosts(forceRefresh: Boolean) = flowOf(
        if (shouldSucceed) {
            Resource.Success(posts)
        } else {
            Resource.Error("Test error")
        }
    )
    
    override suspend fun getComments(postId: Int): Resource<List<Comment>> {
        return Resource.Success(emptyList())
    }
}

/**
 * Fake network observer for testing
 */
class FakeNetworkObserver(
    private val isAvailable: Boolean = true
) : INetworkConnectivityObserver {
    
    override fun isNetworkAvailable() = isAvailable
    
    override fun observeNetworkConnectivity() = flowOf(isAvailable)
}
