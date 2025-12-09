package com.example.restomaster.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "master_allergens")
data class MasterAllergenEntity(
    @PrimaryKey val code: String,
    val labelJa: String,
    val sortOrder: Int
)
