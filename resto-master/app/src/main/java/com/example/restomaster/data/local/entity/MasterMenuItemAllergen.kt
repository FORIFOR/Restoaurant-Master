package com.example.restomaster.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.restomaster.data.local.entity.AllergenEntity
import com.example.restomaster.data.local.entity.MenuItemEntity

@Entity(
    tableName = "master_menu_item_allergens",
    primaryKeys = ["menuItemId", "allergenCode"],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = MenuItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["menuItemId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        ),
        androidx.room.ForeignKey(
            entity = MasterAllergenEntity::class,
            parentColumns = ["code"],
            childColumns = ["allergenCode"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)
data class MasterMenuItemAllergen(
    val menuItemId: Long,
    val allergenCode: String,
    val source: String = "manual"   // "manual" or "ai_suggested"
)
