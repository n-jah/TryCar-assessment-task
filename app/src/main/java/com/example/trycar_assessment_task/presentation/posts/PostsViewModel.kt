package com.example.trycar_assessment_task.presentation.posts

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
        observeNetworkConnectivity()
        loadPosts()
    }
    
    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkObserver.observeNetworkConnectivity()
                .collect { isAvailable ->
                    _isNetworkAvailable.value = isAvailable
                }
        }
    }
    
    fun loadPosts(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            postsRepository.getPosts(forceRefresh).collect { resource ->
                _postsState.value = resource
                if (resource !is Resource.Loading) {
                    _isRefreshing.value = false
                }
            }
        }
    }
    
    fun refresh() {
        _isRefreshing.value = true
        loadPosts(forceRefresh = true)
    }
}
