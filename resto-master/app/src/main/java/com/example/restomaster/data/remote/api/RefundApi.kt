package com.example.restomaster.data.remote.api

import com.example.restomaster.data.remote.model.refund.RefundListResponse
import com.example.restomaster.data.remote.model.refund.RefundOrderRequestV2
import com.example.restomaster.data.remote.model.refund.RefundOrderResponseV2
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RefundApi(private val baseUrl: String) {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true 
            })
        }
    }

    suspend fun fetchRefunds(
        orderId: String,
        limit: Int = 20,
        sortBy: String = "requestedAt",
        sortDir: String = "desc",
        cursor: String? = null
    ): RefundListResponse {
        return client.get("$baseUrl/orders/$orderId/refunds") {
            parameter("limit", limit)
            parameter("sort_by", sortBy)
            parameter("sort_dir", sortDir)
            if (cursor != null) parameter("cursor", cursor)
        }.body()
    }

    suspend fun refundOrderV2(req: RefundOrderRequestV2): RefundOrderResponseV2 {
        return client.post("$baseUrl/paypay/refund/order/v2") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }.body()
    }
}
