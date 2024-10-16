package com.ph46500.assignment_ph46500.Manager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ph46500.assignment_ph46500.Home_n_Product.MyDropdownMenu
import com.ph46500.assignment_ph46500.Manager.ui.theme.Assignment_ph46500Theme
import com.ph46500.assignment_ph46500.MyTopBar
import com.ph46500.assignment_ph46500.R

class AddProduct : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {
                PreviewAddProduct()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddProductScreen(navController: NavController) {
    Scaffold(
        topBar = {
            MyTopBar(
                title = "Thêm món ăn mới",
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    },
                        modifier = Modifier.size(100.dp)) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                    }
                },
//                actions = {
//                    IconButton(onClick = { /* Handle action 1 */ }) {
//                        Icon(Icons.Default.Search, contentDescription = "Search")
//                    }
//                    IconButton(onClick = { /* Handle action 2 */ }) {
//                        Icon(Icons.Default.Settings, contentDescription = "Settings")
//                    }
//                }
            )
        }
    ) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFFFFFFF)), // white background color
//        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            BodyAdd()
        }
    }
    }
}

@Composable
fun BodyAdd() {
    var tenMon by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.add_image),
            contentDescription = "Add image",
            modifier = Modifier
                .size(150.dp)
        )
        MyDropdownMenu()
        MyDropdownMenu()
        MyDropdownMenu()
        TextField(
            value = tenMon, onValueChange = { newText -> tenMon = newText },
            label = { Text(text = "Tên Món Ăn") },
            placeholder = { Text(text = "Nhập tên món ăn") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,  // Ẩn đường viền khi focus
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent, // Ẩn đường viền khi không focus
                disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent  // Ẩn đường viền khi disabled
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFC9CACC))
        ) {
            Text(
                text = "Thêm",
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun PreviewAddProduct() {
    Assignment_ph46500Theme {
            AddProductScreen(navController = rememberNavController())
     }
}