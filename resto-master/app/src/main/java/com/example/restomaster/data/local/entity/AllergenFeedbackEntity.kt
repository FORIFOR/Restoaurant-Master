package com.example.restomaster.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allergen_feedback")
data class AllergenFeedbackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val menuItemId: Long,
    val allergenCode: String,
    val aiSuggested: Boolean,
    val userAction: String,  // "kept", "removed", "added"
    val timestamp: Long
)
