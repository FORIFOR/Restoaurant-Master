package com.example.restomaster.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface StripeApiService {
    @POST("payments/stripe/create-checkout-session")
    suspend fun createCheckoutSession(@Body request: CreateSessionRequest): CreateSessionResponse

    @POST("payments/stripe/verify")
    suspend fun verifyPayment(@Body request: VerifyRequest): VerifyResponse
}
