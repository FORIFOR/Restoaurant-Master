package com.example.restomaster.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "menu_translations",
    foreignKeys = [
        ForeignKey(
            entity = MenuItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["menu_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("menu_id"), Index("lang")]
)
data class MenuTranslationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "menu_id") val menuId: Long,
    val lang: String,              // "en", "zh-Hans", "fr", "it"
    val name: String,
    val description: String?,
    val status: String,            // "draft" or "approved"
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
