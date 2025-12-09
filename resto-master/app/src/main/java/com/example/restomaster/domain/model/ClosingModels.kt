package com.example.restomaster.domain.model

/**
 * Summary of daily closing data
 */
data class DailyClosingSummary(
    val salesTotal: Long,           // Sum of PAID bills
    val paymentsTotal: Long,        // Sum of SUCCEEDED payments
    val diff: Long,                 // salesTotal - paymentsTotal
    val billCount: Int,             // Number of PAID bills
    val paymentBreakdown: List<PaymentMethodTotal>,
    val unpaidBills: List<UnpaidBill>,
    val abnormalPayments: List<AbnormalPayment>
) {
    val isBalanced: Boolean get() = diff == 0L && unpaidBills.isEmpty() && abnormalPayments.isEmpty()
}

data class PaymentMethodTotal(
    val method: String,  // CASH, STRIPE, PAYPAY
    val total: Long
) {
    val displayName: String get() = when (method) {
        "CASH" -> "現金"
        "STRIPE" -> "クレジットカード"
        "PAYPAY" -> "PayPay"
        "LINEPAY" -> "LINE Pay"
        else -> method
    }
}

data class UnpaidBill(
    val billId: Long,
    val tableId: Long,
    val totalAmount: Int,
    val closedAt: Long?,
    val paymentMethod: String?
)

data class AbnormalPayment(
    val paymentId: Long,
    val billId: Long,
    val provider: String,
    val amount: Int,
    val status: String,
    val createdAt: Long
) {
    val statusDisplayName: String get() = when (status) {
        "PENDING" -> "処理中"
        "FAILED" -> "失敗"
        "CANCELED" -> "キャンセル"
        else -> status
    }
}
