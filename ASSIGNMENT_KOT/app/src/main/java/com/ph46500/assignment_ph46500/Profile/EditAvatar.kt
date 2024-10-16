package com.ph46500.assignment_ph46500.Profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ph46500.assignment_ph46500.MyTopBar
import com.ph46500.assignment_ph46500.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditAvatarScreen() {
    Scaffold(
        topBar = {
            MyTopBar(title = "Chỉnh sửa ảnh đại diện")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_cashpay),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(200.dp)
                    .clip(shape = CircleShape)
                    .border(2.dp, Color.Green, CircleShape)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(Color(0xD6E3F2FD))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cam),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "Chụp ảnh", color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFE3F2FD))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_album),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "Chọn ảnh từ album",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
            ) {
                Text(text = "Lưu")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewEditAvatarScreen() {
    EditAvatarScreen()
}