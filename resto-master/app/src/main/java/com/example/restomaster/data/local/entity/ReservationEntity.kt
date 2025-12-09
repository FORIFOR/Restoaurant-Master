package com.example.restomaster.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storeId: Long,
    val date: String,              // "2025-11-26"
    val time: String,              // "18:30"
    val name: String,
    val phone: String?,
    val peopleCount: Int,
    val assignedTableId: Long?,
    val note: String?,
    val status: String             // "reserved", "seated", "canceled", "completed"
)
