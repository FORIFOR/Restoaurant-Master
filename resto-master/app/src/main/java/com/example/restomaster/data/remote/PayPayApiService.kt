package com.example.restomaster.data.remote

import com.squareup.moshi.Json
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PayPayApiService {

    @POST("/payments/paypay/create")
    suspend fun createQrCode(@Body request: CreatePayPayRequest): CreatePayPayResponse

    @GET("/payments/paypay/status/{merchantPaymentId}")
    suspend fun getPaymentStatus(@Path("merchantPaymentId") merchantPaymentId: String): PaymentStatusResponse
}

data class CreatePayPayRequest(
    @Json(name = "bill_id") val billId: Long,
    val amount: Int,
    @Json(name = "table_number") val tableNumber: Int? = null,
    val description: String? = null
)

data class CreatePayPayResponse(
    @Json(name = "payment_id") val paymentId: String,
    @Json(name = "bill_id") val billId: Long,
    val amount: Int,
    @Json(name = "paypay_url") val paypayUrl: String,
    @Json(name = "merchant_payment_id") val merchantPaymentId: String
)

data class PaymentStatusResponse(
    @Json(name = "merchant_payment_id") val merchantPaymentId: String,
    val status: String, // PENDING, COMPLETED, FAILED
    val amount: Int?
)
