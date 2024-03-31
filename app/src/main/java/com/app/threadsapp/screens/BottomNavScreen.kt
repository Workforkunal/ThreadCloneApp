package com.app.threadsapp.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.threadsapp.model.BottomNavItem
import com.app.threadsapp.navigation.Routes

@Composable
fun BottomNavScreen(
    navController: NavHostController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navHostController = rememberNavController()

        Scaffold(
            bottomBar = { MyBottomBar(navHostController) }
        ) { innerPadding ->
            NavHost(
                navController = navHostController,
                startDestination = Routes.HomeScreen.routes,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Routes.HomeScreen.routes) {
                    HomeScreen(navController)
                }
                composable(route = Routes.AddThreadsScreen.routes) {
                    AddThreadsScreen(navHostController)
                }
                composable(route = Routes.SearchScreen.routes) {
                    SearchScreen(navController)
                }
                composable(route = Routes.NotificationsScreen.routes) {
                    NotificationsScreen()
                }
                composable(route = Routes.ProfileScreen.routes) {
                    ProfileScreen(navController)
                }
            }

        }
    }
}

@Composable
fun MyBottomBar(
    navHostController: NavHostController
) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        val backStackEntry = navHostController.currentBackStackEntryAsState()

        val list = listOf(
            BottomNavItem(
                title = "HomeScreen",
                route = Routes.HomeScreen.routes,
                icon = Icons.Filled.Home
            ),
            BottomNavItem(
                title = "SearchScreen",
                route = Routes.SearchScreen.routes,
                icon = Icons.Filled.Search
            ),
            BottomNavItem(
                title = "AddThreadsScreen",
                route = Routes.AddThreadsScreen.routes,
                icon = Icons.Filled.Add
            ),
            BottomNavItem(
                title = "NotificationsScreen",
                route = Routes.NotificationsScreen.routes,
                icon = Icons.Filled.Notifications
            ),
            BottomNavItem(
                title = "ProfileScreen",
                route = Routes.ProfileScreen.routes,
                icon = Icons.Filled.Person
            )
        )
        BottomAppBar(
            backgroundColor = Color.Blue,
            contentColor = Color.White
        ) {
            list.forEach {
                val selected = it.route == backStackEntry?.value?.destination?.route

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navHostController.navigate(it.route) {
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.title,
                        )
                    }
                )
            }
        }
    }
}