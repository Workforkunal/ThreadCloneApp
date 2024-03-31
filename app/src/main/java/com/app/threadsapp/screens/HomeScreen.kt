package com.app.threadsapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.threadsapp.R
import com.app.threadsapp.item_view.ThreadItem
import com.app.threadsapp.viewmodel.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val homeViewModel: HomeViewModel = viewModel()
        val threadAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)
        val isLoading by homeViewModel.isLoading.collectAsState()
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.threadlogo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(45.dp)
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        ){ paddingValues ->

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = homeViewModel::loadStuff,
                indicator = { state, refreshTrigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = refreshTrigger
                    )
                },
                modifier = Modifier.padding(paddingValues)
            ) {
                LazyColumn {
                    items(threadAndUsers ?: emptyList()) { pairs ->

                        ThreadItem(
                            thread = pairs.first,
                            users = pairs.second,
                            navController = navController,
                            userId = FirebaseAuth.getInstance().currentUser!!.uid
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(navController = rememberNavController())
}