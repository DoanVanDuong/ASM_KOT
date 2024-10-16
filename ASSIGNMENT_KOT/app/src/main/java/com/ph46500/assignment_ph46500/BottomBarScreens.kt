package com.ph46500.assignment_ph46500

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreens(
    val screen: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomBarScreens("home", "Home", Icons.Default.Home)
    data object History : BottomBarScreens("history", "History", Icons.Filled.DateRange)
    data object Cart : BottomBarScreens("cart", "Cart", Icons.Default.ShoppingCart)
    data object Profile : BottomBarScreens("profile", "Profile", Icons.Default.Person)
}