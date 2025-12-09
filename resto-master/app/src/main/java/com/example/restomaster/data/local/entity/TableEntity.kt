package com.example.restomaster.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tables",
    indices = [Index(value = ["storeId", "tableNumber"], unique = true)]
)
data class TableEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storeId: Long,
    val tableNumber: Int,
    val token: String,

    val isActive: Boolean = true,
    val status: String = "ACTIVE" // Persisted as String (TableStatus.name)
)
