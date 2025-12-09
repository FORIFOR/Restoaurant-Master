package com.example.restomaster.domain.model

import java.time.LocalDateTime

/**
 * Data Transfer Object for Receipt Printing
 * This contains all necessary information to print a Qualified Invoice (適格請求書).
 */
data class ReceiptDto(
    val storeName: String,
    val storeAddress: String,
    val storePhone: String?,
    val invoiceRegistrationNumber: String?, // T + 13 digits
    val receiptId: String, // e.g., "No. 000123"
    val tableNumber: String?,
    val transactionDate: LocalDateTime,
    val items: List<ReceiptItemDto>,
    val subtotal: Int, // Total excluding tax (or including, depending on display preference, but usually total payment amount)
    val taxSummaries: List<TaxSummary>,
    val totalAmount: Int, // Grand total
    val paymentMethod: String, // Cash, Credit, etc.
    val receivedAmount: Int? = null, // For cash: Amount received
    val changeAmount: Int? = null    // For cash: Change
)

data class ReceiptItemDto(
    val name: String,
    val quantity: Int,
    val unitPrice: Int,
    val lineTotal: Int,
    val isReducedTax: Boolean // True for 8% (Takeout), False for 10%
)

data class TaxSummary(
    val rate: Int, // 8 or 10
    val taxableAmount: Int, // Amount subject to this tax rate
    val taxAmount: Int // Calculated tax amount
)

enum class TaxRateType(val rate: Int) {
    STANDARD_10(10),
    REDUCED_8(8)
}
