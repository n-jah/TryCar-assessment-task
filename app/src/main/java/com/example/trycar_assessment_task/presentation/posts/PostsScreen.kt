package com.example.trycar_assessment_task.presentation.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trycar_assessment_task.presentation.components.PostItem
import com.example.trycar_assessment_task.data.model.Post
import com.example.trycar_assessment_task.util.Resource

/**
 * Posts screen displaying list of posts from API
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    onPostClick: (Post) -> Unit,
    viewModel: PostsViewModel = hiltViewModel()
) {
    val postsState by viewModel.postsState.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    
    Scaffold(
        floatingActionButton = {
            if (postsState is Resource.Success || postsState is Resource.Error) {
                FloatingActionButton(
                    onClick = { viewModel.refresh() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Offline indicator
            if (!isNetworkAvailable) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Offline",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "No internet connection - Showing cached data",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            
            // Loading indicator
            if (isRefreshing) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            
            // Posts content
            when (postsState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                is Resource.Success -> {
                    val posts = (postsState as Resource.Success<List<Post>>).data ?: emptyList()
                    
                    if (posts.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No posts available",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(posts) { post ->
                                PostItem(
                                    post = post,
                                    onClick = { onPostClick(post) }
                                )
                            }
                        }
                    }
                }
                
                is Resource.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = (postsState as Resource.Error).message ?: "An error occurred",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadPosts(true) }) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

