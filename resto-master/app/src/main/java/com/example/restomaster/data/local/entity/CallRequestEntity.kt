package com.example.restomaster.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_requests")
data class CallRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storeId: Long,
    val tableId: Long,
    val type: String,              // "call_staff", "checkout"
    val status: String,            // "new", "handled"
    val createdAt: Long,
    val handledAt: Long?
)
