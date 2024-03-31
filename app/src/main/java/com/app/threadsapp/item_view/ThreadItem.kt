package com.app.threadsapp.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.app.threadsapp.model.ThreadModel
import com.app.threadsapp.model.UserModel

@Composable
fun ThreadItem(
    thread: ThreadModel,
    users: UserModel,
    navController: NavHostController,
    userId: String
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row {
                Image(
                    painter = rememberAsyncImagePainter(model = users.imageUrl),
                    contentDescription = "image",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = users.userName,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = users.bio,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Thin
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = thread.thread,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Justify
                )
            )


            Spacer(modifier = Modifier.height(20.dp))


            if (thread.image != "") {
                Image(
                    painter = rememberAsyncImagePainter(model = thread.image),
                    contentDescription = "image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Inside
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.DarkGray
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItemPreview() {
    ThreadItem(
        thread = ThreadModel(),
        users = UserModel(),
        navController = rememberNavController(),
        userId = ""
    )
}