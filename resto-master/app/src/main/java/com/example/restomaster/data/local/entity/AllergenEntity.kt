package com.example.restomaster.data.local.entity

// This is now a domain/data model, not a Room entity
data class AllergenEntity(
    val code: String,
    val labelJa: String,
    val sortOrder: Int
)
