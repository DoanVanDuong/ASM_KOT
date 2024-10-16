package com.ph46500.assignment_ph46500.Home_n_Product

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ph46500.assignment_ph46500.Home_n_Product.ui.theme.Assignment_ph46500Theme
import com.ph46500.assignment_ph46500.R
import com.ph46500.assignment_ph46500.ViewModel.ProductViewModel


class ProductDetail : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {

                ProductDetailScreen(productId = "670f6845db6c59257dcff184", navigationController = rememberNavController())
//                PreviewHome()

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigationController: NavHostController,
) {
    LaunchedEffect(Unit) {
        viewModel.fetchProductDetail(productId)
    }
    val product = viewModel.selectedProduct.value
    var quantity by remember { mutableStateOf(product?.quantity ?: 1) }

    // Xử lý trường hợp không có sản phẩm
    if (product == null) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
            ){
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
                Text(text = "Loading product details...", modifier = Modifier, textAlign = TextAlign.Center)
            }
        }
        return
    }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(228, 228, 228, 0), // Màu nền của TopAppBar
            ),
            navigationIcon = {
                IconButton(
                    onClick = {
                        navigationController.navigateUp()
                    }, modifier = Modifier
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(Color.White, shape = CircleShape)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back",
                        modifier = Modifier
//                                .background(Color.Green)
                            .size(50.dp)
                            .padding(4.dp),
                    )
                }
            },
            title = { Text(text = "Chi Tiết Món Ăn", modifier = Modifier.padding(10.dp, 0.dp)) },
            actions = {
                IconButton(
                    onClick = { /*TODO*/ }, modifier = Modifier
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(Color.White, shape = CircleShape)
                ) {
                    Icon(
                        Icons.Default.Favorite, "Favorite", modifier = Modifier
                            .background(Color.White)
                            .size(50.dp)
                            .padding(4.dp), tint = Color.LightGray
                    )
                }
            },
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues)
                .background(Color(0xFFFFFFFF)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(20.dp, 0.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                AsyncImage(
                    model = product?.image_url ?: "",
                    contentDescription = "product",
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                        .clip(shape = RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.error_image)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .shadow(elevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(10.dp)
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = product?.category ?: "Category")
                        Row(
                            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = product?.name ?: "Name", fontSize = 22.sp, fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Filled.Star, contentDescription = "Rate icon", tint = Color(0xFFFBA427))
                                Text(text = "4.5")
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = product?.description
                                ?: "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Enim rem rerum consequuntur totam molestiae. Voluptas veniam " +
                                "molestiae eos" +
                                " " +
                                "numquam" +
                                " " + "ea voluptate delectus sequi laboriosam! Blanditiis soluta molestiae tenetur architecto. Sapiente.",
                            color = Color.Gray,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.weight(2f),
                                text = "Delivery Time",
                                fontSize = 16.sp,
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(3f)
                            ) {
                                Icon(painter = painterResource(id = R.drawable.clock_icon), contentDescription = "clock icon", tint = Color(0xFFFBA427))
                                Text(text = "25 mins", fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "Total Price", fontSize = 14.sp, fontWeight = FontWeight.W500
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "$", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Red)
                                Text(text = product?.price.toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.weight(1f)
                            ) {
                                IconButton(
                                    onClick = {
                                        if (quantity > 1) {
                                            quantity--
                                        } else if (quantity <= 1) {
                                            quantity == 1
                                        }
                                    }, modifier = Modifier.background(Color(0xFFFBA427))
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_minus), contentDescription = "minus one"
                                    )
                                }
                                Text(
                                    text = quantity.toString(), fontSize = 20.sp, color = Color.Red
                                )
                                IconButton(
                                    onClick = {
                                        quantity++
                                    },
                                    modifier = Modifier.background(Color(0xFFFBA427))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add, contentDescription = "Add more"
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(Color(0xFFEC071A))
                        ) {
                            Text(text = "Add To Cart")
                            Icon(
                                imageVector = Icons.Default.ShoppingCart, contentDescription = "minus one"
                            )
                        }

                    }
                }

            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewProductDetail() {
    ProductDetailScreen(productId = "66f2b2fb241ecbbc239dd506", navigationController = rememberNavController())
}