package com.example.restomaster.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restomaster.ui.dashboard.DashboardScreen
import com.example.restomaster.ui.menu.MenuListScreen
import com.example.restomaster.ui.table.TableListScreen
import com.example.restomaster.ui.billing.BillingScreen

// Screen class moved to Screen.kt

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            com.example.restomaster.ui.dashboard.MasterDashboardScreen(
                onTableClick = { tableId -> navController.navigate("table_detail/$tableId") },
                onOrderClick = { orderId -> 
                    navController.navigate("order_detail/$orderId")
                },
                onNavigateToOrderList = { navController.navigate("order_list") },
                onNavigateTables = { navController.navigate(Screen.Tables.route) },
                onNavigateMenu = { navController.navigate(Screen.Menu.route) },
                onNavigateAnalytics = { navController.navigate(Screen.Analytics.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable("order_list") {
            com.example.restomaster.ui.dashboard.OrderListScreen(
                onBack = { navController.popBackStack() },
                onOrderClick = { orderId -> navController.navigate("order_detail/$orderId") }
            )
        }
        composable(
            route = Screen.TableDetail.route,
            arguments = listOf(androidx.navigation.navArgument("tableId") { type = androidx.navigation.NavType.LongType })
        ) { backStackEntry ->
            val tableId = backStackEntry.arguments?.getLong("tableId") ?: 0L
            com.example.restomaster.ui.table.detail.TableDetailScreen(
                tableId = tableId,
                onBack = { navController.popBackStack() },
                onNavigateReceipt = { billId -> navController.navigate("receipt/$billId") }
            )
        }
        composable(Screen.Tables.route) { 
            TableListScreen() 
        }
        composable(Screen.Menu.route) { 
            com.example.restomaster.ui.menu.MenuListScreen(
                onNavigateCamera = { navController.navigate(Screen.Camera.route) },
                onNavigateImport = { navController.navigate(Screen.MenuImport.route) },
                onNavigateDetail = { id -> navController.navigate("menu_detail/$id") }
            )
        }
        composable(Screen.Reservations.route) { 
            // Placeholder
            androidx.compose.material3.Text("Reservations Screen")
        }
        composable(Screen.Billing.route) {
            BillingScreen(
                onNavigateReceipt = { billId ->
                    navController.navigate("receipt/$billId")
                }
            )
        }
        composable(Screen.Settings.route) { 
            com.example.restomaster.ui.settings.SettingsScreen(
                onNavigateHandy = { navController.navigate(Screen.HandyTableList.route) }
            )
        }
        composable(Screen.Camera.route) {
            com.example.restomaster.ui.camera.CameraScreen(
                onImageCaptured = { uri ->
                    val encodedUri = java.net.URLEncoder.encode(uri.toString(), java.nio.charset.StandardCharsets.UTF_8.toString())
                    navController.navigate("ocr_result/$encodedUri")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.OcrResult.route,
            arguments = listOf(androidx.navigation.navArgument("imageUri") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val imageUriString = backStackEntry.arguments?.getString("imageUri")
            val imageUri = if (imageUriString != null) {
                android.net.Uri.parse(java.net.URLDecoder.decode(imageUriString, java.nio.charset.StandardCharsets.UTF_8.toString()))
            } else {
                null
            }
            
            com.example.restomaster.ui.ocr.OcrResultScreen(
                imageUri = imageUri,
                onSaved = {
                    navController.popBackStack(Screen.Menu.route, inclusive = false)
                }
            )
        }
        composable(Screen.MenuImport.route) {
            com.example.restomaster.ui.screen.menu_import.MenuImportScreen(
                onBack = { navController.popBackStack() },
                onImportFinished = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.MenuDetail.route,
            arguments = listOf(androidx.navigation.navArgument("menuItemId") { type = androidx.navigation.NavType.LongType })
        ) {
            com.example.restomaster.ui.menu.detail.MenuDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "order_detail/{orderId}",
            arguments = listOf(androidx.navigation.navArgument("orderId") { type = androidx.navigation.NavType.LongType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
            com.example.restomaster.ui.order.OrderDetailScreen(
                orderId = orderId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Analytics.route) {
            com.example.restomaster.ui.analytics.AnalyticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.HandyTableList.route) {
            com.example.restomaster.ui.handy.HandyTableListScreen(
                onTableClick = { tableId -> navController.navigate("handy_order_entry/$tableId") },
                onSwitchToHost = { 
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.HandyTableList.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.HandyOrderEntry.route,
            arguments = listOf(androidx.navigation.navArgument("tableId") { type = androidx.navigation.NavType.LongType })
        ) { backStackEntry ->
            val tableId = backStackEntry.arguments?.getLong("tableId") ?: 0L
            com.example.restomaster.ui.handy.HandyOrderEntryScreen(
                tableId = tableId,
                onBack = { navController.popBackStack() },
                onOrderComplete = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Receipt.route,
            arguments = listOf(androidx.navigation.navArgument("billId") { type = androidx.navigation.NavType.LongType })
        ) { backStackEntry ->
            val billId = backStackEntry.arguments?.getLong("billId") ?: 0L
            com.example.restomaster.ui.receipt.ReceiptPreviewScreen(
                billId = billId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
