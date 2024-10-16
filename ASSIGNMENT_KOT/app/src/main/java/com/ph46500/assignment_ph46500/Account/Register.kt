package com.ph46500.assignment_ph46500.Account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ph46500.assignment_ph46500.Component.CustomTextField
import com.ph46500.assignment_ph46500.Model.User
import com.ph46500.assignment_ph46500.R
import com.ph46500.assignment_ph46500.ViewModel.RegisterViewModel
import com.ph46500.assignment_ph46500.ui.theme.Assignment_ph46500Theme
import org.w3c.dom.Text

class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {
                PreviewRegister()
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val registerViewModel = RegisterViewModel()

    var fullname by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var re_password by remember { mutableStateOf("") }

    var fullnameError by remember { mutableStateOf<String?>(null) }
    var userNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var rePasswordError by remember { mutableStateOf<String?>(null) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFFFFF)), // Dark background color
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
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
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hãy đăng ký tài khoản và tiếp tục..",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CustomTextField(
                value = fullname,
                label = "Full Name",
                onValueChange = {
                    fullname = it
                },
                isPassword = false,
                errorMessage = fullnameError
            )
            CustomTextField(
                value = userName,
                label = "User Name",
                onValueChange = {
                    userName = it
                },
                isPassword = false,
                errorMessage = userNameError
            )
            CustomTextField(
                value = email,
                label = "Email",
                onValueChange = {
                    email = it
                },
                isPassword = false,
                errorMessage = emailError
            )
            CustomTextField(
                value = password,
                label = "Password",
                onValueChange = {
                    password = it
                },
                isPassword = true,
                errorMessage = passwordError
            )
            CustomTextField(
                value = re_password,
                label = "Re-Password",
                onValueChange = {
                    re_password = it
                },
                isPassword = true,
                errorMessage = rePasswordError
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Confirm Button
            Button(
                onClick = {
                    var err = false
                    if (fullname.isEmpty()) {
                        fullnameError = "Vui lòng nhập Họ tên của bạn!"
                        err = true
                    } else {
                        fullnameError = null
                    }
                    if (userName.isEmpty()) {
                        userNameError = "Vui lòng nhập userName!!"
                        err = true
                    } else {
                        userNameError = null
                    }
                    if (email.isEmpty()) {
                        emailError = "Vui lòng nhập Email!"
                        err = true
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Sai định dạng Email!"
                        err = true
                    } else {
                        emailError = null
                    }
                    if (password.isEmpty()) {
                        passwordError = "Vui lòng nhập Mật Khẩu!"
                        err = true
                    } else if (password.length < 6) {
                        passwordError = "Mật khẩu ít nhất phải 6 kí tự!"
                        err = true
                    } else {
                        passwordError = null
                    }
                    if (re_password.isEmpty()) {
                        rePasswordError = "Vui xác nhận Mật Khẩu!"
                        err = true
                    } else if (re_password!=password) {
                        rePasswordError = "Mật khẩu không trùng khớp!"
                        err = true
                    } else {
                        rePasswordError = null
                    }
                    if (!err) {
                        val user = User(fullname, userName, email, password,"","","")
                        registerViewModel.registerUser(user, {
                            var intent = Intent(context, Login::class.java)
                            context.startActivity(intent)
                        }, { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF774D)), // Button color
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Đăng ký",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Nếu bạn đã có tài khoản - ",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                    ),
                )
                Text(
                    text = stringResource(id = R.string.login_text),
                    style = TextStyle(
                        color = Color.Blue,
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable {
                        var intent = Intent(context, Login::class.java)
                        context.startActivity(intent)
                    }
                )
            }


        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewRegister() {
    Assignment_ph46500Theme {
        RegisterScreen(navController = rememberNavController())
    }
}