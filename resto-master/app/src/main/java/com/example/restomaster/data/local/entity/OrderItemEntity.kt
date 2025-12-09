package com.example.restomaster.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val menuItemId: Long,
    val quantity: Int,
    val unitPrice: Int,
    val status: String,            // "received", "in_progress", "served", "canceled"
    val memo: String?
)
