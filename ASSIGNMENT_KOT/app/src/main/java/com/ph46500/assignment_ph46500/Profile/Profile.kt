package com.ph46500.assignment_ph46500.Profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ph46500.assignment_ph46500.Account.Login
import com.ph46500.assignment_ph46500.Manager.ManageCategory
import com.ph46500.assignment_ph46500.Manager.ManageProduct
import com.ph46500.assignment_ph46500.MyTopBar
import com.ph46500.assignment_ph46500.Profile.ui.theme.Assignment_ph46500Theme
import com.ph46500.assignment_ph46500.R
import com.ph46500.assignment_ph46500.ViewModel.LoginViewModel
import com.ph46500.assignment_ph46500.ViewModel.ProductViewModel
import com.ph46500.assignment_ph46500.ViewModel.UserViewModel

class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {
                PreviewProfile()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {

    val context = LocalContext.current
    val userInfo by userViewModel.userInfoState.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getUserInfo(context)
    }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(0xFFFFFFFF)), // white background color
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                //Header
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEFF6F6)), verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier
                            .weight(1f)
                            .padding(10.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(userInfo?.avatar ?: R.drawable.img_empty)
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            Modifier
                                .size(80.dp)
                                .clip(shape = CircleShape),
                            contentScale = ContentScale.Crop,
                            )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            userInfo?.let { user ->
                                Text(
                                    text = user.name ?: "Chao Lao Lo", fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = user.email ?: "chaolaolo@gmail.com", color = Color.Gray
                                )
                            }
                        }
                    }
                    TextButton(onClick = {
                        viewModel.logout(context)
                        val intent = Intent(context, Login::class.java)
                        context.startActivity(intent)
                    }) {
                        Text(text = "Sign Out", color = Color(0xFF3A84DA))
                    }
                }

                //Body
                LazyColumn {
                    item {
                        ManageItem(title = "Sửa thông tin cá nhân", icon = Icons.Default.Edit) {
                            navController.navigate("EditProfileScreen")
//                            val intent = Intent(context, EditProfile::class.java)
//                            context.startActivity(intent)
                        }
                    }
                    item {
                        ManageItem(title = "Quản lý món ăn") {
                            val intent = Intent(context, ManageProduct::class.java)
                            context.startActivity(intent)
                        }
                    }
                    item {
                        ManageItem(title = "Quản lý loại món ăn") {
                            val intent = Intent(context, ManageCategory::class.java)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
}


@Composable
fun ManageItem(title: String, icon: ImageVector? = null, onclick: () -> Unit) {
    Card(
        onClick = onclick,
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp),
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontWeight = FontWeight.W500)
            icon?.let { Image(imageVector = it, contentDescription = null, Modifier.padding(10.dp, 0.dp)) }
        }
    }
}


@Composable
fun ProfileScreenUI(viewModel: LoginViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    var phoneNumber by remember { mutableStateOf(TextFieldValue("0342128462")) }
    var ward by remember { mutableStateOf(TextFieldValue("Trung Mỹ Tây")) }
    var street by remember { mutableStateOf(TextFieldValue("Đường Tô Ký")) }
    var houseNumber by remember { mutableStateOf(TextFieldValue("413")) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFF))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {
                var intent = Intent(context, EditProfile::class.java)
                context.startActivity(intent)
            }) {
                Text(text = "Edit", color = Color.Black)
            }
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your image resource
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            TextButton(onClick = {
                viewModel.logout(context)
                val intent = Intent(context, Login::class.java)
                context.startActivity(intent)
            }) {
                Text(text = "Signout", color = Color.Black)
            }
        }



        Text(
            text = "Phước",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        ProfileTextField(label = "Số điện thoại", value = phoneNumber) {
            phoneNumber = it
        }
        ProfileTextField(label = "Phường", value = ward) {
            ward = it
        }
        ProfileTextField(label = "Đường", value = street) {
            street = it
        }
        ProfileTextField(label = "Số nhà", value = houseNumber) {
            houseNumber = it
        }
    }
}


@Composable
fun ProfileTextField(label: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
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
            enabled = false,
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
fun PreviewProfile() {
    Assignment_ph46500Theme {
        ProfileScreen(rememberNavController())
    }
}
