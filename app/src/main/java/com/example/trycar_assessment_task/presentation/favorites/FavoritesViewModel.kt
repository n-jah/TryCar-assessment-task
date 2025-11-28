package com.example.trycar_assessment_task.presentation.favorites

import android.util.Log
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
        Log.d(TAG, "FavoritesViewModel initialized")
        observeNetworkConnectivity()
        syncFavoritesWhenOnline()
    }
    
    private fun observeNetworkConnectivity() {
        Log.d(TAG, "Starting network connectivity observation")
        viewModelScope.launch {
            networkObserver.observeNetworkConnectivity()
                .collect { isAvailable ->
                    Log.d(TAG, "Network connectivity changed: $isAvailable")
                    _isNetworkAvailable.value = isAvailable
                    if (isAvailable) {
                        Log.d(TAG, "Network available, triggering sync")
                        syncFavorites()
                    }
                }
        }
    }
    
    private fun syncFavoritesWhenOnline() {
        Log.d(TAG, "Checking if initial sync is needed")
        viewModelScope.launch {
            if (networkObserver.isNetworkAvailable()) {
                Log.d(TAG, "Network available, performing initial sync")
                syncFavorites()
            } else {
                Log.d(TAG, "Network not available, skipping initial sync")
            }
        }
    }
    
    private fun syncFavorites() {
        Log.d(TAG, "Syncing favorites...")
        viewModelScope.launch {
            favoritesRepository.syncFavorites()
            Log.d(TAG, "Favorites sync completed")
        }
    }
    
    fun removeFromFavorites(postId: Int) {
        Log.d(TAG, "Removing post $postId from favorites")
        viewModelScope.launch {
            favoritesRepository.removeFromFavorites(postId)
            Log.d(TAG, "Post $postId removed")
        }
    }

    companion object {
        private const val TAG = "FavoritesViewModel"
    }
}
