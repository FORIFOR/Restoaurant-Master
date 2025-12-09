package com.example.restomaster.data.local.entity

// Domain/data model (non-Room)
data class MenuItemAllergenEntity(
    val menuItemId: Long,
    val allergenCode: String,
    val source: String = "manual"   // "manual" or "ai"
)
