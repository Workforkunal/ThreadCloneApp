package com.app.threadsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.threadsapp.screens.AddThreadsScreen
import com.app.threadsapp.screens.BottomNavScreen
import com.app.threadsapp.screens.HomeScreen
import com.app.threadsapp.screens.LoginScreen
import com.app.threadsapp.screens.NotificationsScreen
import com.app.threadsapp.screens.OtherUsersScreen
import com.app.threadsapp.screens.ProfileScreen
import com.app.threadsapp.screens.RegisterScreen
import com.app.threadsapp.screens.SearchScreen
import com.app.threadsapp.screens.Splash

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.routes,
    ) {

        composable(Routes.Splash.routes) {
            Splash(navController)
        }
        composable(Routes.AddThreadsScreen.routes) {
            AddThreadsScreen(navController)
        }
        composable(Routes.HomeScreen.routes) {
            HomeScreen(navController)
        }
        composable(Routes.SearchScreen.routes) {
            SearchScreen(navController)
        }
        composable(Routes.NotificationsScreen.routes) {
            NotificationsScreen()
        }
        composable(Routes.ProfileScreen.routes) {
            ProfileScreen(navController)
        }
        composable(Routes.BottomNavScreen.routes) {
            BottomNavScreen(navController)
        }
        composable(Routes.LoginScreen.routes) {
            LoginScreen(navController)
        }
        composable(Routes.RegisterScreen.routes) {
            RegisterScreen(navController)
        }
        composable(Routes.OtherUsersScreen.routes) {
            val data = it.arguments!!.getString("data")
            OtherUsersScreen(navController, data!!)
        }
    }
}