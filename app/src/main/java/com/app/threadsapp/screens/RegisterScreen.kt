package com.app.threadsapp.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Person4
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.app.threadsapp.R
import com.app.threadsapp.navigation.Routes
import com.app.threadsapp.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController
) {
    var name by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember{ mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val error by authViewModel.error.observeAsState()

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else android.Manifest.permission.READ_EXTERNAL_STORAGE

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){ uri: Uri? ->
        imageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ){ isGranted: Boolean ->
        if (isGranted){
            Toast.makeText(context, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "PERMISSION IS REQUIRED", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(firebaseUser){
        if (firebaseUser != null){
            navController.navigate(Routes.BottomNavScreen.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    error?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Register Here",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            Image(
                painter = if (imageUri == null) painterResource(id = R.drawable.camera)
                else rememberAsyncImagePainter(model = imageUri),
                contentDescription = "camera",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context,
                            permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED
                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text(
                        text = "Name",
                        fontSize = 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Name"
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userName,
                onValueChange = {
                    userName = it
                },
                label = {
                    Text(
                        text = "Username",
                        fontSize = 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person2,
                        contentDescription = "Username"
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = bio,
                onValueChange = {
                    bio = it
                },
                label = {
                    Text(
                        text = "Bio",
                        fontSize = 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person4,
                        contentDescription = "Bio"
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(5.dp))


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
                    if (name.isEmpty() || userName.isEmpty() || bio.isEmpty()
                        || email.isEmpty() || password.isEmpty() || imageUri == null
                    ) {
                        Toast.makeText(context, "Please fill all the details !", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        authViewModel.register(
                            name,
                            userName,
                            bio,
                            email,
                            password,
                            imageUri!!,
                            context
                        )
                        Toast.makeText(context, "Register Successfully", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Blue)
            ) {
                Text(
                    text = "Register",
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
                    navController.navigate(Routes.LoginScreen.routes) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            ) {
                Text(
                    text = "Already register? Login Here !",
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterView() {
    RegisterScreen(navController = rememberNavController())
}

