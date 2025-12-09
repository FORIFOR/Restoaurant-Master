package com.example.restomaster.data.model

/**
 * Represents a parsed menu item candidate from OCR results.
 */
data class MenuCandidate(
    val id: Long = 0L, // Temporary ID for UI handling
    val name: String,
    val price: Int?,
    val suggestedName: String? = null,
    val originalLines: List<OcrTextLine>
)
