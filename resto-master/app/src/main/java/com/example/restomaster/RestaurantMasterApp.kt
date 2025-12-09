package com.example.restomaster

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restomaster.ui.navigation.NavGraph
import com.example.restomaster.ui.navigation.Screen

@Composable
fun RestaurantMasterApp() {
    val navController = rememberNavController()
    val navItems = listOf(
        Screen.Home to Icons.Default.Home,
        Screen.Tables to Icons.Default.QrCode2,
        Screen.Menu to Icons.Default.MenuBook,
        Screen.Billing to Icons.Default.CreditCard,
        Screen.Settings to Icons.Default.Settings
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            NavigationBar {
                navItems.forEach { (screen, icon) ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { androidx.compose.material3.Icon(icon, contentDescription = screen.route) },
                        label = { androidx.compose.material3.Text(labelFor(screen)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private fun labelFor(screen: Screen): String = when (screen) {
    Screen.Home -> "ホーム"
    Screen.Tables -> "テーブル"
    Screen.Menu -> "メニュー"
    Screen.Billing -> "会計"
    Screen.Settings -> "設定"
    else -> screen.route
}
