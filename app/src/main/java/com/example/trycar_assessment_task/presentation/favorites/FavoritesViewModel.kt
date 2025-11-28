package com.example.trycar_assessment_task.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trycar_assessment_task.Network.INetworkConnectivityObserver
import com.example.trycar_assessment_task.data.local.entity.FavoritePostEntity
import com.example.trycar_assessment_task.domain.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Favorites screen
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val networkObserver: INetworkConnectivityObserver
) : ViewModel() {
    
    val favorites: StateFlow<List<FavoritePostEntity>> =
        favoritesRepository.getAllFavorites()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    
    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()
    
    init {
        observeNetworkConnectivity()
        syncFavoritesWhenOnline()
    }
    
    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkObserver.observeNetworkConnectivity()
                .collect { isAvailable ->
                    _isNetworkAvailable.value = isAvailable
                    if (isAvailable) {
                        syncFavorites()
                    }
                }
        }
    }
    
    private fun syncFavoritesWhenOnline() {
        viewModelScope.launch {
            if (networkObserver.isNetworkAvailable()) {
                syncFavorites()
            }
        }
    }
    
    private fun syncFavorites() {
        viewModelScope.launch {
            favoritesRepository.syncFavorites()
        }
    }
    
    fun removeFromFavorites(postId: Int) {
        viewModelScope.launch {
            favoritesRepository.removeFromFavorites(postId)
        }
    }
}
