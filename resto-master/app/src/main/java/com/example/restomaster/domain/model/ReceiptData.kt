package com.example.restomaster.domain.model

/**
 * Data class representing receipt line item for PDF generation
 */
data class ReceiptLineItem(
    val name: String,
    val quantity: Int,
    val unitPrice: Int,
    val lineTotal: Int,
    val taxRate: Float,
    val isReducedTax: Boolean  // 軽減税率対象「※」マーク用
)

/**
 * Tax breakdown for 8% and 10% rates
 */
data class TaxBreakdown(
    val subtotal8: Int,
    val tax8: Int,
    val subtotal10: Int,
    val tax10: Int
) {
    val totalTax: Int get() = tax8 + tax10
}

/**
 * Complete receipt data for PDF generation
 */
data class ReceiptData(
    val id: Long,
    val receiptNumber: String,
    val issuedAt: Long,
    val storeName: String,
    val storeAddress: String,
    val storePhone: String?,
    val invoiceRegistrationNumber: String?,
    val items: List<ReceiptLineItem>,
    val subtotal: Int,
    val taxBreakdown: TaxBreakdown,
    val totalAmount: Int,
    val paymentMethod: String?,
    val taxIncluded: Boolean = true
)
