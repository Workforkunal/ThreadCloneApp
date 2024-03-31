package com.app.threadsapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.app.threadsapp.item_view.ThreadItem
import com.app.threadsapp.model.UserModel
import com.app.threadsapp.navigation.Routes
import com.app.threadsapp.utils.SharedPref
import com.app.threadsapp.viewmodel.AuthViewModel
import com.app.threadsapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val context = LocalContext.current
        val authViewModel: AuthViewModel = viewModel()
        val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

        val userViewModel: UserViewModel = viewModel()
        val threads by userViewModel.threads.observeAsState(null)

        val followerList by userViewModel.followerList.observeAsState(null)
        val followingList by userViewModel.followingList.observeAsState(null)

        var currentUserId = ""
        if (FirebaseAuth.getInstance().currentUser != null) {
            currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        }

        if (currentUserId != "") {
            userViewModel.getFollowers(currentUserId)
            userViewModel.getFollowing(currentUserId)
        }

        val user = UserModel(
            name = SharedPref.getName(context),
            imageUrl = SharedPref.getImage(context),
            userName = SharedPref.getUserName(context),
            bio = SharedPref.getBio(context)
        )

        if (firebaseUser != null) {
            userViewModel.fetchThreads(firebaseUser!!.uid)
        }

        LaunchedEffect(firebaseUser) {
            if (firebaseUser == null) {
                navController.navigate(Routes.LoginScreen.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }

        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = SharedPref.getName(context),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textDecoration = TextDecoration.Underline
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Image(
                        painter = rememberAsyncImagePainter(model = SharedPref.getImage(context)),
                        contentDescription = "image",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = SharedPref.getUserName(context),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = SharedPref.getBio(context),
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${followerList!!.size} Followers",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "${followingList!!.size} Following",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ElevatedButton(
                        onClick = {
                            authViewModel.logout()
                            Toast.makeText(context, "Logout Successfully", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(Color.Blue)
                    ) {
                        Text(
                            text = "Logout",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.DarkGray
                )

            }
            items(threads ?: emptyList()) { pair ->
                ThreadItem(
                    thread = pair,
                    users = user,
                    navController = navController,
                    userId = SharedPref.getUserName(context)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePreview() {
    ProfileScreen(navController = rememberNavController())
}