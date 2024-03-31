package com.app.threadsapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.threadsapp.item_view.UserItem
import com.app.threadsapp.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavHostController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var search by remember { mutableStateOf("") }

        val searchViewModel: SearchViewModel = viewModel()
        val userList by searchViewModel.userList.observeAsState(null)


        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Search",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = search ,
                onValueChange = {
                    search = it
                },
                label = {
                    Text(
                        text = "Search User"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "search"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn {

                if (userList != null && userList!!.isNotEmpty()) {
                    val filterItems =
                        userList!!.filter { it.name!!.contains(search, ignoreCase = true) }
                    items(filterItems) { pairs ->

                        UserItem(
                            users = pairs,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true )
@Composable
fun SearchPreview() {
    SearchScreen(navController = rememberNavController())
}