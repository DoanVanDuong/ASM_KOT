package com.ph46500.assignment_ph46500.Home_n_Product

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ph46500.assignment_ph46500.Component.ProductCategoryDropdown
import com.ph46500.assignment_ph46500.Home_n_Product.ui.theme.Assignment_ph46500Theme
import com.ph46500.assignment_ph46500.Model.Product
import com.ph46500.assignment_ph46500.MyTopBar
import com.ph46500.assignment_ph46500.R
import com.ph46500.assignment_ph46500.ViewModel.ProductViewModel


class Home : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {

                PreviewHome()

            }
        }
    }
}

@SuppressLint("MissingColorAlphaChannel", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewModel: ProductViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val filteredProductList by viewModel.filteredProductList
    var searchQuery by remember { mutableStateOf("") }

    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//    val token = sharedPreferences.getString("token", "") ?: ""
    val token = viewModel.getTokenFromSharedPreferences(context) ?: ""
    Scaffold(
        topBar = {
            MyTopBar(
                title = "Cơm Tấm Đêm",
//                navigationIcon = {
//                    IconButton(onClick = { /* Handle navigation */ },
//                        modifier = Modifier.size(100.dp)) {
//                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
//                    }
//                },
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
    ) {paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues)
                .background(Color(0xFFFFFFFF)), // white background color
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Banner()
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState)
                            .padding(22.dp, 0.dp),
                        horizontalArrangement = Arrangement.spacedBy(34.dp)
                    ) {
                        listOf(
                            Tab("Món ăn", R.drawable.img_tab),
                            Tab("Đồ ăn thêm", R.drawable.img_tab),
                            Tab("Topping", R.drawable.img_tab),
                            Tab("Khác", R.drawable.img_tab)
                        )
                    }
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            viewModel.searchProducts(query)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Search Products") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                    )
                }
                item {
                    ProductCategoryDropdown { selectedCategory ->
                        // Handle category selection
                        viewModel.filterProductsByCategory(selectedCategory.name)
                        println("Selected category: ${selectedCategory.name}")
                    }
                }

                items(viewModel.filteredProductList.value) { product ->
                    ProductItem(product, navController, token = token)
                }

            }
        }
    }

}

@Composable
fun Banner() {
    Image(
        painter = painterResource(id = R.drawable.banner),
        contentDescription = "home banner",
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    )
}

@Composable
fun Tab(title: String, image: Int) {
    Column(
        modifier = Modifier
            .padding(0.dp, 4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

    }
}

@Composable
fun ProductItem(
    product: Product,
    navController: NavController,
    viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    token: String,
) {

    val context = LocalContext.current


    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("ProductDetailScreen/${product._id}")
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.image_url)
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
                    text = product.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = product.price.toString(),
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
                    text = product.quantity.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Image(imageVector = Icons.Filled.Add, contentDescription = "add more quantity")
                }
            }

            IconButton(
                onClick = {
                    if (token.isNotEmpty()) {
                        if (product._id != null) {
                            viewModel.addToCart(context, token, product._id, product.quantity ?: 1)
                            Log.d("TAG", "ProductItem: " + product._id)
                        }else {
                            Log.d("TAG", "Product ID is null")
                        }
                    } else {
                        println("Token is missing. Please log in first.")
                        Log.d("TAG", "ProductItem: Token is missing. Please log in first.")
                    }
//                    viewModel.addToCart(context, token, productId = product._id, product.quantity ?: 1)
                },
            ) {
                Box(
                    Modifier
                        .border(border = BorderStroke(1.dp, Color.LightGray), CircleShape)
                        .padding(10.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_add_shopping_cart),
                        contentDescription = "icon_add_shopping_cart",
                        tint = Color(0xFFFF6200)
                    )
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }

    // Danh sách các loại (categories) để hiển thị trong Dropdown
    val categories = listOf("Option 1", "Option 2", "Option 3")

    // Container cho Dropdown Menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        // TextField để hiển thị loại đã chọn
        TextField(
            value = selectedOptionText,
            onValueChange = { selectedOptionText = it },
            readOnly = true, // Để ngăn nhập tay vào TextField
            placeholder = { Text("Select Category") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(16.dp),
//                .height(40.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(fontSize = 12.sp)
        )
        // Hiển thị các mục trong Dropdown Menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        selectedOptionText = category
                        expanded = false
                    }
                )
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun PreviewHome() {
    Assignment_ph46500Theme {
        HomeScreen(navController = rememberNavController())
    }
}