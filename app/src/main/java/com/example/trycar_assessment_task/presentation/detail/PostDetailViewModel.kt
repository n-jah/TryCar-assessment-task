package com.example.trycar_assessment_task.presentation.detail

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
    private val postTitle: String = savedStateHandle.get<String>("postTitle") ?: ""
    private val postBody: String = savedStateHandle.get<String>("postBody") ?: ""
    private val postUserId: Int = savedStateHandle.get<Int>("postUserId") ?: 0
    
    val post = Post(
        id = postId,
        userId = postUserId,
        title = postTitle,
        body = postBody
    )
    
    private val _commentsState = MutableStateFlow<Resource<List<Comment>>>(Resource.Loading())
    val commentsState: StateFlow<Resource<List<Comment>>> = _commentsState.asStateFlow()
    
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    
    private val _showSuccessMessage = MutableSharedFlow<String>()
    val showSuccessMessage: SharedFlow<String> = _showSuccessMessage.asSharedFlow()
    
    init {
        loadComments()
        checkIfFavorite()
    }
    
    private fun loadComments() {
        viewModelScope.launch {
            _commentsState.value = Resource.Loading()
            val result = postsRepository.getComments(postId)
            _commentsState.value = result
        }
    }
    
    private fun checkIfFavorite() {
        viewModelScope.launch {
            _isFavorite.value = favoritesRepository.isFavorite(postId)
        }
    }
    
    fun toggleFavorite() {
        viewModelScope.launch {
            if (_isFavorite.value) {
                favoritesRepository.removeFromFavorites(postId)
                _isFavorite.value = false
                _showSuccessMessage.emit("Removed from favorites")
            } else {
                val favoritePost = FavoritePostEntity(
                    id = post.id,
                    title = post.title,
                    body = post.body,
                    isSynced = false
                )
                favoritesRepository.addToFavorites(favoritePost)
                _isFavorite.value = true
                _showSuccessMessage.emit("Added to favorites")
            }
        }
    }
}
