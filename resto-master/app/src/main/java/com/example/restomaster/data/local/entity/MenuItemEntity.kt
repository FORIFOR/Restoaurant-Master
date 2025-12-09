package com.example.restomaster.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storeId: Long,
    val name: String,
    @ColumnInfo(name = "description") val description: String? = null,
    val price: Int,
    val category: String,           // "food", "drink" etc.
    @ColumnInfo(name = "image_path") val imagePath: String? = null,
    val isAvailable: Boolean = true,
    @ColumnInfo(name = "tax_rate_type") val taxRateType: String = "STANDARD_10", // STANDARD_10 or REDUCED_8
    @ColumnInfo(name = "is_ai_generated") val isAiGenerated: Boolean = false
)
