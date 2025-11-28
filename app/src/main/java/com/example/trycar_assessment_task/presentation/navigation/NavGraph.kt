package com.example.trycar_assessment_task.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trycar_assessment_task.presentation.detail.PostDetailScreen
import com.example.trycar_assessment_task.presentation.favorites.FavoritesScreen
import com.example.trycar_assessment_task.presentation.posts.PostsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            // Only show bottom bar on main screens (not on detail screen)
            if (currentDestination?.route in listOf(Screen.Posts.route, Screen.Favorites.route)) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Posts.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Posts.route) {
                PostsScreen(
                    onPostClick = { post ->
                        navController.navigate(Screen.PostDetail.createRoute(post.id))
                    }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onPostClick = { postId ->
                        navController.navigate(Screen.PostDetail.createRoute(postId))
                    }
                )
            }

            composable(
                route = Screen.PostDetail.route,
                arguments = listOf(
                    navArgument("postId") { type = NavType.IntType }
                )
            ) {
                PostDetailScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}

// Bottom navigation items
private val bottomNavItems = listOf(
    Screen.Posts,
    Screen.Favorites
)

// Screen definitions
sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Posts : Screen("posts", "Posts", Icons.Default.List)
    data object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    data object PostDetail : Screen(
        "post_detail/{postId}",
        "Post Detail",
        Icons.Default.List
    ) {
        fun createRoute(postId: Int): String {
            return "post_detail/$postId"
        }
    }
}

