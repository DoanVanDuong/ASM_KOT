package com.ph46500.assignment_ph46500.Profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ph46500.assignment_ph46500.MainActivity
import com.ph46500.assignment_ph46500.Profile.ui.theme.Assignment_ph46500Theme
import com.ph46500.assignment_ph46500.R
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ph46500.assignment_ph46500.BottomBarScreens
import com.ph46500.assignment_ph46500.ViewModel.UserViewModel

class EditProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {
                    PreviewEditProfile()
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {

    val context = LocalContext.current
    val userInfo by userViewModel.userInfoState.collectAsState()

    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userViewModel.getUserInfo(context)
        name = userInfo?.name ?: ""
        dateOfBirth = userInfo?.dateOfBirth ?: ""
        gender = userInfo?.gender ?: ""
    }

    Scaffold { paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFF))
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.size(100.dp).clickable {  }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userInfo?.avatar ?: R.drawable.img_empty)
                    .crossfade(true)
                    .build(), contentDescription = "Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.Blue, shape = CircleShape)
                    .clickable { navController.navigate("EditAvatarScreen") },
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Edit avatar",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.BottomEnd)
                    .offset((-4).dp, (-4).dp)
                    .clip(shape = CircleShape)
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Họ và Tên") },
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text(text = "Ngày sinh") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text(text = "Giới tính") }
        )


        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = {
                userViewModel.UpdateUser(
                    context = context,
                    name = name,
                    dateOfBirth = dateOfBirth,
                    gender = gender
                )
                navController.navigateUp()
//                var intent = Intent(context, MainActivity::class.java)
//                context.startActivity(intent)
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF774D)), // Button color
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Lưu",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }


    }
    }
}


@Composable
fun EditTextField(label: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
            ),
//            label={ Text(text = label)},
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewEditProfile() {
    Assignment_ph46500Theme {
        EditProfileScreen(rememberNavController())
    }
}
