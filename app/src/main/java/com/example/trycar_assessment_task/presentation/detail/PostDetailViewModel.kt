package com.example.trycar_assessment_task.presentation.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trycar_assessment_task.data.local.entity.FavoritePostEntity
import com.example.trycar_assessment_task.data.model.Comment
import com.example.trycar_assessment_task.data.model.Post
import com.example.trycar_assessment_task.domain.repository.FavoritesRepository
import com.example.trycar_assessment_task.domain.repository.PostsRepository
import com.example.trycar_assessment_task.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Post Detail screen
 */
@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val favoritesRepository: FavoritesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val postId: Int = savedStateHandle.get<Int>("postId") ?: 0
    
    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post.asStateFlow()
    
    private val _commentsState = MutableStateFlow<Resource<List<Comment>>>(Resource.Loading())
    val commentsState: StateFlow<Resource<List<Comment>>> = _commentsState.asStateFlow()
    
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    
    private val _showSuccessMessage = MutableSharedFlow<String>()
    val showSuccessMessage: SharedFlow<String> = _showSuccessMessage.asSharedFlow()
    
    init {
        Log.d(TAG, "PostDetailViewModel initialized for postId: $postId")
        loadPost()
        loadComments()
        checkIfFavorite()
    }
    
    private fun loadPost() {
        Log.d(TAG, "Loading post $postId")
        viewModelScope.launch {
            postsRepository.getPosts().collect { resource ->
                if (resource is Resource.Success) {
                    _post.value = resource.data?.find { it.id == postId }
                    Log.d(TAG, "Post loaded: ${_post.value?.title}")
                }
            }
        }
    }
    
    private fun loadComments() {
        Log.d(TAG, "Loading comments for post $postId")
        viewModelScope.launch {
            _commentsState.value = Resource.Loading()
            val result = postsRepository.getComments(postId)
            when (result) {
                is Resource.Success -> Log.d(TAG, "Comments loaded: ${result.data?.size} comments")
                is Resource.Error -> Log.e(TAG, "Failed to load comments: ${result.message}")
                else -> {}
            }
            _commentsState.value = result
        }
    }
    
    private fun checkIfFavorite() {
        Log.d(TAG, "Checking if post $postId is favorite")
        viewModelScope.launch {
            _isFavorite.value = favoritesRepository.isFavorite(postId)
            Log.d(TAG, "Post $postId favorite status: ${_isFavorite.value}")
        }
    }
    
    fun toggleFavorite() {
        Log.d(TAG, "toggleFavorite called for post $postId")
        viewModelScope.launch {
            val currentPost = _post.value ?: return@launch
            
            if (_isFavorite.value) {
                Log.d(TAG, "Removing post $postId from favorites")
                favoritesRepository.removeFromFavorites(postId)
                _isFavorite.value = false
                _showSuccessMessage.emit("Removed from favorites")
            } else {
                Log.d(TAG, "Adding post $postId to favorites")
                val favoritePost = FavoritePostEntity(
                    id = currentPost.id,
                    title = currentPost.title,
                    body = currentPost.body,
                    isSynced = false
                )
                favoritesRepository.addToFavorites(favoritePost)
                _isFavorite.value = true
                _showSuccessMessage.emit("Added to favorites")
            }
        }
    }

    companion object {
        private const val TAG = "PostDetailViewModel"
    }
}
