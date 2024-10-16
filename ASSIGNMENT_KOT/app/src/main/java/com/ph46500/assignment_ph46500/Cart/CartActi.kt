package com.ph46500.assignment_ph46500.Cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ph46500.assignment_ph46500.Cart.ui.theme.Assignment_ph46500Theme
import com.ph46500.assignment_ph46500.Model.Cart
import com.ph46500.assignment_ph46500.Model.Product
import com.ph46500.assignment_ph46500.MyTopBar
import com.ph46500.assignment_ph46500.R
import com.ph46500.assignment_ph46500.ViewModel.ProductViewModel

class CartActi : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {
                Scaffold(
                    topBar = {
                        MyTopBar(title = "Giỏ Hàng")
                    }
                ) {
                    PreviewCart()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartScreen(navController: NavController, viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current

    var showConfirmReset by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchCart(context)
    }

    val totalQuantity = viewModel.cartItems.value.sumOf { it.quantity }
    val totalPrice = viewModel.cartItems.value.sumOf { it.product.price * it.quantity }

    Scaffold(
        topBar = {
            MyTopBar(
                title = "Giỏ Hàng",
            )
        },
        bottomBar = {
            BottomAppBar(
                windowInsets = WindowInsets(0.dp),
                backgroundColor = Color(0xFFE0F2F1)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Số lượng: x$totalQuantity")
                        Text(text = "Tổng Tiền: ₫$totalPrice")
                    }


                    Row {
                        if (totalQuantity >= 1) {
                            Button(
                                onClick = {
                                    showConfirmReset = true

                                },
                                colors = ButtonDefaults.buttonColors(Color.Red)
                            ) {
                                Text(text = "Reset")
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(onClick = {
                            navController.navigate("PaymentScreen")
                        }, colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))) {
                            Text(text = "Pay")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(0xFFFFFFFF)), // white background color
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                if (viewModel.cartItems.value.isEmpty()) {
                    EmptyCart()
                } else {
//                    Display list of cart items
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(viewModel.cartItems.value) { cartItem ->
                            // Here you can call a composable function to render each cart item
                            CartItem(cartItem, navController)
                        }
                    }
                }
//                CartItem(Cart(Product("a", "a", "a", 2.2, "a", 123, "a"), 2))
            }
        }
        if (showConfirmReset) {
            ConfirmResetDialog(
                onConfirm = {
                    viewModel.ClearCart(context)
                    showConfirmReset = false
                },
                onDismiss = {
                    showConfirmReset = false
                }
            )
        }
    }
}


@Composable
fun EmptyCart() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_cart),
            contentDescription = "img cart",
            modifier = Modifier
                .size(100.dp),
            colorFilter = ColorFilter.tint(Color.Black)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Không có đơn hàng!",
            color = Color.Red,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
//                .background(Color(0xFFC9CACC))
            ,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFC9CACC))
        ) {
            Text(
                text = "Đặt hàng",
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}


@Composable
fun CartItem(
    cart: Cart,
    navController: NavController? = null,
//    viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {

    val context = LocalContext.current


    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController?.navigate("ProductDetailScreen/${cart.product._id}")
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cart.product.image_url)
                    .crossfade(true)
                    .build(),
                contentDescription = "Product image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,

                )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cart.product.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = cart.product.price.toString(),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Image(painter = painterResource(id = R.drawable.icon_minus), contentDescription = "minus quantity")
                }
                Text(
                    text = cart.quantity.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Image(imageVector = Icons.Filled.Add, contentDescription = "add more quantity")
                }
            }


        }
    }
}

@Composable
fun ConfirmResetDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(text = "Xác Nhận Xóa", fontWeight = FontWeight.Bold)
        },
        text = { Text(text = "Bạn có muốn xóa hết món ăn khỏi giỏ hàng?") },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }) {
                Text(text = "Có")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = "Không")
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewCart() {
    Assignment_ph46500Theme {
        CartScreen(rememberNavController())
    }
}