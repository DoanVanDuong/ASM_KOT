package com.ph46500.assignment_ph46500

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ph46500.assignment_ph46500.Account.LoginScreen
import com.ph46500.assignment_ph46500.Account.RegisterScreen
import com.ph46500.assignment_ph46500.Cart.CartScreen
import com.ph46500.assignment_ph46500.History.HistoryScreen
import com.ph46500.assignment_ph46500.Home_n_Product.HomeScreen
import com.ph46500.assignment_ph46500.Home_n_Product.ProductDetailScreen
import com.ph46500.assignment_ph46500.Manager.ManagerCategoryScreen
import com.ph46500.assignment_ph46500.Manager.ManagerProductScreen
import com.ph46500.assignment_ph46500.Payment.PaymentScreen
import com.ph46500.assignment_ph46500.Profile.EditAvatarScreen
import com.ph46500.assignment_ph46500.Profile.EditProfileScreen
import com.ph46500.assignment_ph46500.Profile.ProfileScreen
import com.ph46500.assignment_ph46500.ui.theme.Assignment_ph46500Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            Assignment_ph46500Theme {
                MainApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainApp() {
    val navigationController = rememberNavController()
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val showBottomBar = when (currentDestination) {
        "ProductDetailScreen/{productId}" -> false
        "LoginScreen" -> false
        "RegisterScreen" -> false
        "PaymentScreen" -> false
        "EditProfileScreen" -> false
        null -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MyBottomAppBar(navigationController = navigationController)
            }
        }
    ) { paddingValues ->
        NavigationGraph(navigationController, Modifier.padding(paddingValues))
    }
}

@Composable
fun NavigationGraph(navigationController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navigationController,
        startDestination = BottomBarScreens.Home.screen,
        modifier = modifier
    ) {
        composable(BottomBarScreens.Home.screen) { HomeScreen(navigationController) }
        composable(BottomBarScreens.History.screen) { HistoryScreen() }
        composable(BottomBarScreens.Cart.screen) { CartScreen(navigationController) }
        composable(BottomBarScreens.Profile.screen) { ProfileScreen(navigationController) }
        composable("RegisterScreen") { RegisterScreen(navigationController) }
        composable("LoginScreen") { LoginScreen(navigationController) }
        composable("ManagerProductScreen") { ManagerProductScreen() }
        composable("ManagerCategoryScreen") { ManagerCategoryScreen() }
        composable("PaymentScreen") { PaymentScreen() }
        composable("EditProfileScreen") { EditProfileScreen(navigationController) }
        composable("EditAvatarScreen") { EditAvatarScreen() }
        composable(
            "ProductDetailScreen/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry . arguments ?. getString ("productId") ?: ""
            ProductDetailScreen(
            productId = productId ,
            navigationController = navigationController
            )
        }
    }
}

@Composable
fun MyBottomAppBar(navigationController: NavHostController) {
    var selected = remember { mutableStateOf(Icons.Default.Home) }

    val screens = listOf(
        BottomBarScreens.Home,
        BottomBarScreens.History,
        BottomBarScreens.Cart,
        BottomBarScreens.Profile,
    )

    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = Color(0xFFEAEBEA),
        modifier = Modifier.height(60.dp),

        ) {
        screens.forEach { screen ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any {
                    it.route == screen.screen
                } == true,
                modifier = Modifier.weight(1f),
                label = {
                    Text(
                        text = screen.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (selected.value == screen.icon) Color(0xFF478D8C) else Color.Black
                    )
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == screen.icon) Color(0xFF478D8C) else Color.Black
                    )
                },
                selectedContentColor = Color(0xFF478D8C),
                unselectedContentColor = Color.Black,
                onClick = {
                    selected.value = screen.icon
                    navigationController.navigate(screen.screen) {
                        popUpTo(navigationController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

@Composable
fun MyTopBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo header",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Text(text = title)
            }
        },
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = Color(0xFFEAEBEA),
        contentColor = Color.White,
    )
}


@Preview(showSystemUi = true)
@Composable
fun PreviewBottom() {
    Assignment_ph46500Theme {
        MyBottomAppBar(navigationController = rememberNavController())
    }
}
