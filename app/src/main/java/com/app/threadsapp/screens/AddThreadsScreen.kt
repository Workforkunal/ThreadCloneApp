package com.app.threadsapp.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.app.threadsapp.navigation.Routes
import com.app.threadsapp.utils.SharedPref
import com.app.threadsapp.viewmodel.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddThreadsScreen(
    navController: NavHostController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var thread by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }

        val context = LocalContext.current
        val threadViewModel: AddThreadViewModel = viewModel()
        val isPosted by threadViewModel.isPosted.observeAsState(false)

        val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else android.Manifest.permission.READ_EXTERNAL_STORAGE

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(context, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "PERMISSION IS REQUIRED", Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(isPosted) {
            if (isPosted!!) {
                thread = ""
                imageUri = null

                Toast.makeText(context, "Thread Added.", Toast.LENGTH_SHORT).show()

                navController.navigate(Routes.HomeScreen.routes) {
                    popUpTo(Routes.AddThreadsScreen.routes) {
                        inclusive = true
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "close",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Routes.HomeScreen.routes) {
                                popUpTo(Routes.AddThreadsScreen.routes) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                Text(
                    text = "Add thread",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Image(
                    painter = rememberAsyncImagePainter(model = SharedPref.getImage(context)),
                    contentDescription = "image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = SharedPref.getUserName(context),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(10.dp)
                )
            }

            BasicTextFieldWithHint(
                value = thread,
                onValueChange = {
                    thread = it
                },
                hint = "Please write a thread...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 8.dp
                    )
            )

            if (imageUri == null) {
                Icon(
                    imageVector = Icons.Filled.Attachment,
                    contentDescription = "close",
                    modifier = Modifier.clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context,
                            permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED
                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .height(250.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUri),
                        contentDescription = "image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentScale = ContentScale.Inside
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close",
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clickable {
                                imageUri = null
                            }
                    )
                }
            }
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (replyText, button) = createRefs()
                Text(
                    text = "Anyone can reply...",
                    style = TextStyle(
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.constrainAs(replyText) {
                        start.linkTo(parent.start, margin = 12.dp)
                        bottom.linkTo(parent.bottom, margin = 12.dp)
                    }
                )

                ElevatedButton(
                    onClick = {
                        if (imageUri == null) {
                            threadViewModel.saveData(
                                thread,
                                FirebaseAuth.getInstance().currentUser!!.uid,
                                ""
                            )
                        } else {
                            threadViewModel.saveImage(
                                thread,
                                FirebaseAuth.getInstance().currentUser!!.uid,
                                imageUri!!
                            )
                        }
                    },
                    modifier = Modifier.constrainAs(button) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                    colors = ButtonDefaults.buttonColors(Color.Blue)
                ) {
                    Text(
                        text = "Post",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (threadViewModel.postInProgress.value) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun BasicTextFieldWithHint(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                fontSize = 18.sp
            )
        }

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Justify
            )
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AddScreenPreview() {
    AddThreadsScreen(navController = rememberNavController())
}