package com.example.restomaster.domain.model

import com.example.restomaster.data.local.entity.CallRequestEntity

data class TableStatusUi(
    val tableId: Long,
    val tableNumber: Int,
    val isOccupied: Boolean,
    val hasActiveOrder: Boolean,
    val hasUnservedItems: Boolean = false,
    val hasCallRequest: Boolean,
    val hasCheckoutRequest: Boolean,
    val activeOrderCount: Int,
    val unservedItemCount: Int = 0,
    val totalAmount: Int,
    val servedAmount: Int = 0,
    val guestCount: Int? = null,
    val stayMinutes: Int? = null,
    val lastOrderAt: Long? = null,
    val callRequests: List<CallRequestEntity> = emptyList()
)

data class DashboardSummary(
    val totalTables: Int = 0,
    val occupiedTables: Int = 0,
    val activeOrders: Int = 0,
    val newOrders: Int = 0,
    val callRequests: Int = 0,
    val checkoutRequests: Int = 0,
    val checkoutTotalAmount: Int = 0
)
