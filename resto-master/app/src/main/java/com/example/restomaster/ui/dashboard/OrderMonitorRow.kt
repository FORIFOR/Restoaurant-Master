package com.example.restomaster.ui.dashboard

data class OrderMonitorRow(
    val orderId: Long,
    val tableNumber: Int,
    val itemsSummary: List<String>,
    val totalAmount: Int,
    val createdAt: Long,
    val status: String
)
