package com.example.restomaster.domain.model

/**
 * QR code size options for PDF generation
 */
enum class QrCodeSize(
    val displayName: String,
    val pixels: Int
) {
    SMALL("小", 300),
    MEDIUM("中", 500),
    LARGE("大", 800)
}
