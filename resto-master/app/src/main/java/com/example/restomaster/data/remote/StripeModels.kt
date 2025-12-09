package com.example.restomaster.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateSessionRequest(
    @Json(name = "bill_id") val billId: Long,
    val amount: Int,
    val currency: String,
    @Json(name = "table_label") val tableLabel: String,
    @Json(name = "return_base_url") val returnBaseUrl: String
)

@JsonClass(generateAdapter = true)
data class CreateSessionResponse(
    @Json(name = "checkout_url") val checkoutUrl: String,
    @Json(name = "session_id") val sessionId: String
)

@JsonClass(generateAdapter = true)
data class VerifyRequest(
    @Json(name = "session_id") val sessionId: String
)

@JsonClass(generateAdapter = true)
data class VerifyResponse(
    val status: String, // SUCCEEDED, PENDING, FAILED
    val amount: Int?,
    val currency: String?,
    @Json(name = "payment_intent_id") val paymentIntentId: String?,
    @Json(name = "receipt_url") val receiptUrl: String?,
    val metadata: Map<String, String>?
)
