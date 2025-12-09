package com.example.restomaster.data.model

import android.graphics.Rect

/**
 * Represents a single line of text recognized by OCR.
 */
data class OcrTextLine(
    val text: String,
    val boundingBox: Rect?
)
