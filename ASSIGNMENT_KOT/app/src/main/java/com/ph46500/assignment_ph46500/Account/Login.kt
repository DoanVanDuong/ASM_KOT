package com.ph46500.assignment_ph46500.Account

import android.app.Activity
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ph46500.assignment_ph46500.Account.ui.theme.Assignment_ph46500Theme
import com.ph46500.assignment_ph46500.BottomBarScreens
import com.ph46500.assignment_ph46500.Component.CustomTextField
import com.ph46500.assignment_ph46500.MainActivity
import com.ph46500.assignment_ph46500.R
import com.ph46500.assignment_ph46500.ViewModel.LoginViewModel

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {
                LoginScreen(navController = rememberNavController())
            }
        }
    }
}


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(),
) {
    val context = LocalContext.current
    viewModel.loadLoginInfo(context)

    if (viewModel.checkLogin(context)) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFFFFF)), // Dark background color
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .windowInsetsPadding((WindowInsets.ime))
                .verticalScroll(rememberScrollState())
        ) {
            // Image and Header Section
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your image resource
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .padding(20.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Hãy đăng nhập và trải nghiệm ẩm thực nào!",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = viewModel.email,
                label = "Email",
                onValueChange = { viewModel.email = it },
                isPassword = false,
                errorMessage = viewModel.errorEmail)
            CustomTextField(
                value = viewModel.password,
                label = "Password",
                onValueChange = { viewModel.password = it },
                isPassword = true,
                errorMessage = viewModel.errorPass
                )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(checked = viewModel.rememberMe, onCheckedChange = { viewModel.rememberMe = it })
//                    Text(
//                        text = "Ghi nhớ mật khẩu",
//                        style = TextStyle(fontSize = 16.sp, color = Color.Black),
//                        modifier = Modifier.padding(start = 8.dp)
//                    )
//                }
                Text(
                    text = stringResource(id = R.string.forgotpass_text),
                    style = TextStyle(
                        color = Color.Blue,
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.Underline
                    ),
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            // Confirm Button
            Button(
                onClick = {
                    viewModel.login(context) {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF774D)), // Button color
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Đăng nhập",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Nếu bạn chưa có tài khoản - ",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                    ),
                )
                Text(
                    text = stringResource(id = R.string.register_text),
                    style = TextStyle(
                        color = Color.Blue,
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable {
                        var intent = Intent(context, Register::class.java)
                        context.startActivity(intent)
                    }
                )
            }


        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    Assignment_ph46500Theme {
        LoginScreen(navController = rememberNavController())
    }
}