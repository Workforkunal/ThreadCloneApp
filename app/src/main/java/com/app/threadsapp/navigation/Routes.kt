package com.app.threadsapp.navigation

sealed class Routes(
    val routes: String
) {

    data object Splash: Routes("splash")
    data object AddThreadsScreen: Routes("add_threads_screen")
    data object HomeScreen: Routes("home_screen")
    data object NotificationsScreen: Routes("notifications_screen")
    data object ProfileScreen: Routes("profile_screen")
    data object SearchScreen: Routes("search_screen")
    data object BottomNavScreen: Routes("bottom_nav_screen")
    data object LoginScreen: Routes("login_screen")
    data object RegisterScreen: Routes("register_screen")
    data object OtherUsersScreen: Routes("other_users_screen/{data}")
}