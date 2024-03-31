package com.app.threadsapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.threadsapp.R
import com.app.threadsapp.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun Splash(
    navController: NavHostController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo"
            )

        }
        LaunchedEffect(
            key1 = true
        ) {
            delay(2000)

            if (FirebaseAuth.getInstance().currentUser != null) {
                navController.navigate(Routes.BottomNavScreen.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            } else {
                navController.navigate(Routes.LoginScreen.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SplashPreview() {
    Splash(navController = rememberNavController())
}