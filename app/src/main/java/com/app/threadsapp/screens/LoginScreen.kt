package com.app.threadsapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.threadsapp.navigation.Routes
import com.app.threadsapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }

        val focusManager = LocalFocusManager.current
        val authViewModel: AuthViewModel = viewModel()
        val firebaseUser by authViewModel.firebaseUser.observeAsState()
        val error by authViewModel.error.observeAsState()

        val context = LocalContext.current

        LaunchedEffect(firebaseUser) {
            if (firebaseUser != null) {
                navController.navigate(Routes.BottomNavScreen.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }

        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login Here",
                style = TextStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text(
                        text = "Email",
                        fontSize = 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email"
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text(
                        text = "Password",
                        fontSize = 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Password"
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password"
                    else "Show password"

                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector = image,
                            contentDescription = description
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            ElevatedButton(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please provide all fields !", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        authViewModel.login(email, password, context)
                        Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Blue)
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            TextButton(
                onClick = {
                    navController.navigate(Routes.RegisterScreen.routes) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            ) {
                Text(
                    text = "New User? Register Here !",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (authViewModel.signUpInProgress.value) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    LoginScreen(navController = rememberNavController())
}