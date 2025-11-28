package com.example.trycar_assessment_task.presentation.posts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trycar_assessment_task.Network.INetworkConnectivityObserver
import com.example.trycar_assessment_task.data.model.Post
import com.example.trycar_assessment_task.domain.repository.PostsRepository
import com.example.trycar_assessment_task.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Posts screen
 */
@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val networkObserver: INetworkConnectivityObserver
) : ViewModel() {
    
    private val _postsState = MutableStateFlow<Resource<List<Post>>>(Resource.Loading())
    val postsState: StateFlow<Resource<List<Post>>> = _postsState.asStateFlow()
    
    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    init {
        Log.d(TAG, "PostsViewModel initialized")
        observeNetworkConnectivity()
        loadPosts()
    }
    
    private fun observeNetworkConnectivity() {
        Log.d(TAG, "Starting network connectivity observation")
        viewModelScope.launch {
            networkObserver.observeNetworkConnectivity()
                .collect { isAvailable ->
                    Log.d(TAG, "Network connectivity changed: $isAvailable")
                    _isNetworkAvailable.value = isAvailable
                }
        }
    }
    
    fun loadPosts(forceRefresh: Boolean = false) {
        Log.d(TAG, "loadPosts called - forceRefresh: $forceRefresh")
        viewModelScope.launch {
            postsRepository.getPosts(forceRefresh).collect { resource ->
                when (resource) {
                    is Resource.Loading -> Log.d(TAG, "Posts loading...")
                    is Resource.Success -> Log.d(TAG, "Posts loaded successfully: ${resource.data?.size} posts")
                    is Resource.Error -> Log.e(TAG, "Posts loading failed: ${resource.message}")
                }
                _postsState.value = resource
                if (resource !is Resource.Loading) {
                    _isRefreshing.value = false
                }
            }
        }
    }
    
    fun refresh() {
        Log.d(TAG, "Manual refresh triggered")
        _isRefreshing.value = true
        loadPosts(forceRefresh = true)
    }

    companion object {
        private const val TAG = "PostsViewModel"
    }
}
