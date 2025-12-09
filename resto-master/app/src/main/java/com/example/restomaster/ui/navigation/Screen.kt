package com.example.restomaster.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Tables : Screen("tables")
    object Menu : Screen("menu")
    object Reservations : Screen("reservations")
    object Settings : Screen("settings")
    object Camera : Screen("camera")
    object OcrResult : Screen("ocr_result/{imageUri}")
    object MenuImport : Screen("menu_import")
    object MenuDetail : Screen("menu_detail/{menuItemId}")
    object TableDetail : Screen("table_detail/{tableId}")
    object Billing : Screen("billing")
    object Analytics : Screen("analytics")
    object HandyTableList : Screen("handy_table_list")
    object HandyOrderEntry : Screen("handy_order_entry/{tableId}")
    object Receipt : Screen("receipt/{billId}") {
        val base = "receipt"
    }
}
