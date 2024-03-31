package com.app.threadsapp.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.app.threadsapp.item_view.ThreadItem
import com.app.threadsapp.navigation.Routes
import com.app.threadsapp.utils.SharedPref
import com.app.threadsapp.viewmodel.AuthViewModel
import com.app.threadsapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherUsersScreen(
    navController: NavHostController,
    uid: String
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
        val users by userViewModel.users.observeAsState(null)

        val followerList by userViewModel.followerList.observeAsState(null)
        val followingList by userViewModel.followingList.observeAsState(null)


        if (firebaseUser != null) {
            userViewModel.fetchThreads(uid)
            userViewModel.fetchUsers(uid)
            userViewModel.getFollowers(uid)
            userViewModel.getFollowing(uid)
        }

        LaunchedEffect(firebaseUser) {
            if (firebaseUser == null) {
                navController.navigate(Routes.LoginScreen.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }

        var currentUserId =""
        if (FirebaseAuth.getInstance().currentUser != null){
            currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        }

        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = users!!.name,
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textDecoration = TextDecoration.Underline
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Image(
                        painter = rememberAsyncImagePainter(model = users!!.imageUrl),
                        contentDescription = "image",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = users!!.userName,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = users!!.bio,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "${followerList?.size} Followers",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "${followingList?.size} Following",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ElevatedButton(
                        onClick = {
                            if (currentUserId != "")
                                userViewModel.followUsers(
                                    uid,
                                    currentUserId
                                )
                        },
                        colors = ButtonDefaults.buttonColors(Color.Blue)
                    ) {
                        Text(
                            text = if (followerList!= null &&
                                followerList!!.isNotEmpty() &&
                                followerList!!.contains(currentUserId)) "Following"
                            else "Follow",
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
            if (threads != null && users != null) {
                items(threads ?: emptyList()) { pair ->
                    ThreadItem(
                        thread = pair,
                        users = users!!,
                        navController = navController,
                        userId = SharedPref.getUserName(context)
                    )
                }
            }
        }
    }
}