package com.ph46500.assignment_ph46500.History

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ph46500.assignment_ph46500.History.ui.theme.Assignment_ph46500Theme
import com.ph46500.assignment_ph46500.Model.Order
import com.ph46500.assignment_ph46500.MyTopBar
import com.ph46500.assignment_ph46500.Payment.PayItem
import com.ph46500.assignment_ph46500.R
import com.ph46500.assignment_ph46500.ViewModel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.Locale


data class Order(
    val status: String,
    val date: String,
    val time: String,
    val items: String,
    val price: String,
)


class History : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_ph46500Theme {
                PreviewHistory()
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HistoryScreen(viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var context = LocalContext.current
    val orders = viewModel.orderList.value

    LaunchedEffect(Unit) {
        viewModel.getOrders(context)
    }
    Scaffold(
        topBar = {
            MyTopBar(
                title = "Lịch sử",
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Order list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders) { order ->
                    OrderItem(order)
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order) {

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    val date = inputFormat.parse(order.createdAt)
    val formattedDate = date?.let { outputFormat.format(it) } ?: "Invalid date"

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xA8FBE9E7))
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = order.status,
                color = when (order.status) {
                    "Pending" -> Color(0xFFFF9800)
                    "Shipped" -> Color(0xFF2196F3)
                    "Delivered" -> Color(0xFF4CAF50)
                    else -> Color.Black
                }
//                if (order.status == "Pending") Color(0xFF4CAF50) else Color.Red,
                , fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
//                text = "20-10-2022  10:20",
                text = formattedDate,
                color = Color.Black,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = "Số lượng: x${order.items.size}",
                color = Color.Black,
                fontSize = 14.sp
            )
            Text(
                text = "₫${order.totalAmount}",
                color = Color.Black,
                fontSize = 14.sp
            )
        }
    }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHistory() {
    Assignment_ph46500Theme {
        HistoryScreen()
    }
}