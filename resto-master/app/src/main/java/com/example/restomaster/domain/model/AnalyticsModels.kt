package com.example.restomaster.domain.model

data class MenuItemSales(
    val menuItemId: Long,
    val name: String,
    val quantitySold: Int,
    val totalRevenue: Int,
    val revenuePercentage: Float,
    val abcClass: String  // "A", "B", or "C"
)

data class HourlySales(
    val hour: Int,
    val orderCount: Int,
    val totalSales: Int,
    val averageOrderValue: Int
)

data class DailySales(
    val dayOfWeek: Int,  // 1=Monday, 7=Sunday
    val dayName: String,
    val orderCount: Int,
    val totalSales: Int,
    val averageOrderValue: Int
)

data class MonthlySales(
    val yearMonth: String, // e.g., 2025-12
    val orderCount: Int,
    val totalSales: Int,
    val averageOrderValue: Int
)

data class SalesKPIs(
    val totalSales: Long,
    val averageCheckPerTable: Int,
    val averageSpendPerCustomer: Int,
    val totalOrders: Int,
    val totalCustomers: Int,
    val tableTurnoverRate: Float
)

data class WeeklySales(
    val weekLabel: String,
    val orderCount: Int,
    val totalSales: Int,
    val averageOrderValue: Int
)
