package com.example.restomaster.data.remote.model.refund

import kotlinx.serialization.Serializable

@Serializable
data class RefundListResponse(
    val order_id: String,
    val count: Int,
    val next_cursor: String? = null,
    val items: List<RefundItem> = emptyList()
)

@Serializable
data class RefundItem(
    val merchantRefundId: String,
    val amountJpy: Int? = 0,
    val status: String? = null,
    val reason: String? = null,
    val requestedAt: Long? = null,
    val acceptedAt: Long? = null,
    val paymentId: String? = null,
    val merchantPaymentId: String? = null
)

@Serializable
data class RefundOrderRequestV2(
    val order_id: String,
    val amount_jpy: Int,
    val reason: String? = "customer_request",
    val refund_seq: String? = null
)

@Serializable
data class RefundOrderResponseV2(
    val merchant_refund_id: String,
    val order_status: String? = null,
    val refund_summary: RefundSummary? = null,
    val refund: RefundItem? = null
)

@Serializable
data class RefundSummary(
    val totalRefundedJpy: Int? = null,
    val totalRefundPendingJpy: Int? = null,
    val lastRefundStatus: String? = null,
    val lastMerchantRefundId: String? = null,
    val refundableRemainingJpy: Int? = null
)
