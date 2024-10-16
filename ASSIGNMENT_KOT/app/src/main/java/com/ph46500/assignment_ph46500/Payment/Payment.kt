package com.ph46500.assignment_ph46500.Payment

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ph46500.assignment_ph46500.Component.PaymentOption
import com.ph46500.assignment_ph46500.Model.Cart
import com.ph46500.assignment_ph46500.Model.Order
import com.ph46500.assignment_ph46500.MyTopBar
import com.ph46500.assignment_ph46500.Payment.ui.theme.Assignment_ph46500Theme
import com.ph46500.assignment_ph46500.R
import com.ph46500.assignment_ph46500.ViewModel.ProductViewModel

class Payment : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {

                GreetingPreview()

            }
        }
    }
}

@Composable
fun PaymentScreen(viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current
    var selectedPayment by remember { mutableStateOf("Thanh toán trực tiếp") }
    var showDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var detailAddress by remember { mutableStateOf("") }

    val fullAddress = remember(province, district, detailAddress) {
        if (province.isNotEmpty() && district.isNotEmpty() && detailAddress.isNotEmpty()) {
            "$detailAddress, $district, $province"
        } else {
            ""
        }
    }

    val totalQuantity = viewModel.cartItems.value.sumOf { it.quantity }
    val totalAmount = viewModel.cartItems.value.sumOf { it.quantity * it.product.price }

    LaunchedEffect(Unit) {
        viewModel.fetchCart(context)
    }

    Scaffold(
        topBar = {
            MyTopBar(title = "Thanh Toán")
        },
        bottomBar = {
            BottomCart(totalQuantity, totalAmount, onPaymentClick = { showDialog = true })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //Thông tin cá nhân
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Thông tin người nhận",
                                    fontWeight = FontWeight.Bold
                                )

                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit infor",
                                    modifier = Modifier.clickable {
                                        showAddressDialog = true
                                    }
                                )

                            }
                            Spacer(modifier = Modifier.height(4.dp))
//                            Thông tin người nhận
                            Row {
                                Icon(
                                    imageVector = Icons.Default.AccountBox,
                                    contentDescription = "Icon Name",
                                )
                                Text(
                                    text = name.ifEmpty { "Trống" },
                                    fontSize = 16.sp,
                                )
                            }
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Icon phone",
                                )
                                Text(
                                    text = phoneNumber.ifEmpty { "Trống" },
                                    fontSize = 16.sp,
                                )
                            }
                            Row {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Icon Address",
                                    tint = Color.Red
                                )
                                Text(
                                    text = fullAddress.ifEmpty { "Trống" },
                                    fontSize = 16.sp,
                                )
                            }


                        }
                    }
                }

                //Thông tin thanh toán
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp, 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Chọn phương thức thanh toán",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            PaymentOption(
                                iconRes = R.drawable.img_cashpay,
                                optionName = "Thanh toán trực tiếp",
                                isSelected = selectedPayment == "Thanh toán trực tiếp",
                                onClick = { selectedPayment = "Thanh toán trực tiếp" }
                            )
                            PaymentOption(
                                iconRes = R.drawable.img_zalopay,
                                optionName = "Zalo Pay",
                                isSelected = selectedPayment == "Zalo Pay",
                                onClick = { selectedPayment = "Zalo Pay" }
                            )
                            PaymentOption(
                                iconRes = R.drawable.img_momo,
                                optionName = "Momo",
                                isSelected = selectedPayment == "Momo",
                                onClick = { selectedPayment = "Momo" }
                            )
                        }
                    }
                }
            }
            Card(
                Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(10.dp, 4.dp)
                    .background(Color.White),
                colors = CardDefaults.cardColors(Color.White),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    item {
                        Text(
                            text = "Món ăn của bạn",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(viewModel.cartItems.value) { cartItem ->
                        PayItem(cartItem)
                        Log.d("TAG", "PaymentScreen: listcart: ${viewModel.cartItems.value}")
                    }
                }
            }

            if (showDialog) {
                ConfirmationDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = {
//                        val currentTimestamp = java.time.OffsetDateTime.now().toString()
                        val order = Order(
                            items = viewModel.cartItems.value.map {
                                com.ph46500.assignment_ph46500.Model.OrderItem(
                                    product = it.product,
                                    quantity = it.quantity
                                )
                            },
                            totalAmount = totalAmount,
                            paymentMethod = if (selectedPayment.isEmpty()) "Thanh toán trực tiếp" else selectedPayment,
                            address = fullAddress, // You might want to make this dynamic
                            phoneNumber = phoneNumber, // You might want to make this dynamic
                            status = "Pending",
                            createdAt = ""
                        )
                        viewModel.checkout(context, order)
                        Log.d("TAG", "PaymentScreen: " + order)
                        showDialog = false
                        // Xử lý tiếp code
                    }
                )
            }

            if (showAddressDialog) {
                AddressDialog(
                    initName = name,
                    initPhoneNumber = phoneNumber,
                    initProvince = province,
                    initDistrict = district,
                    initDetailAddress = detailAddress,
                    onDismiss = { showAddressDialog = false },
                    onConfirm = { newName, newPhone, newProvince, newDistrict, newAddress ->
                        name = newName
                        phoneNumber = newPhone
                        province = newProvince
                        district = newDistrict
                        detailAddress = newAddress
                        showAddressDialog = false
                    }
                )
            }

        }
    }
}

@Composable
fun PayItem(
    cart: Cart,
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 2.dp),
        colors = CardDefaults.cardColors(Color(0xFFF1F5FD))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cart.product.image_url)
                    .crossfade(true)
                    .build(),
                contentDescription = "Product image",
                modifier = Modifier
                    .size(50.dp)
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

            Text(
                text = "x" + cart.quantity.toString(),
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(50.dp)
                    .align(Alignment.CenterVertically)
            )


        }
    }
}

@Composable
fun ConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Xác nhận thanh toán")
        },
        text = {
            Text(text = "Bạn có chắc chắn muốn thanh toán không?")
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = "Thanh Toán")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Không")
            }
        }
    )
}


@Composable
fun AddressDialog(
    initName: String,
    initPhoneNumber: String,
    initProvince: String,
    initDistrict: String,
    initDetailAddress: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, province: String, district: String, address: String) -> Unit,
) {
    var name by remember { mutableStateOf(initName) }
    var phoneNumber by remember { mutableStateOf(initPhoneNumber) }
    var province by remember { mutableStateOf(initProvince) }
    var district by remember { mutableStateOf(initDistrict) }
    var detailAddress by remember { mutableStateOf(initDetailAddress) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Thông tin cá nhân",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(text = "Họ và tên") })
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text(text = "Số điện thoại") })
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = province, onValueChange = { province = it }, label = { Text(text = "Tỉnh/Thành") })
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = district, onValueChange = { district = it }, label = { Text(text = "Quận/Huyện") })
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = detailAddress, onValueChange = { detailAddress = it }, label = { Text(text = "Địa chỉ cụ thể") })

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "Hủy bỏ")
                    }
                    TextButton(onClick = {
                        onConfirm(
                            name, phoneNumber, province, district, detailAddress
                        )
                    }) {
                        Text(text = "Lưu")
                    }
                }
            }
        }
    }
}

@Composable
fun BottomCart(totalQuantity: Int, totalAmount: Double, onPaymentClick: () -> Unit,viewModel: ProductViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    //Thanh toán
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Column(
                Modifier.weight(1f)
            ) {

                Row {
                    Text(
                        text = "Số lượng:",
                        fontSize = 16.sp,
                    )
                    Text(
                        text = totalQuantity.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
                Row {
                    Text(
                        text = "Tổng tiền:",
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "$",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(
                        text = totalAmount.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Button(
                onClick = onPaymentClick,
                Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(Color(0xFFFE724C)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "Thanh Toán")
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    Assignment_ph46500Theme {
        PaymentScreen()
    }
}