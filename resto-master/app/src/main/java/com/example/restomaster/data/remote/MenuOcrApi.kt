package com.example.restomaster.data.remote

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MenuOcrApi {
    @Multipart
    @POST("/api/ocr/menu-gemini")
    suspend fun ocrMenu(
        @Part image: MultipartBody.Part
    ): OcrMenuResponse
}

data class OcrMenuResponse(
    val items: List<OcrMenuItemDto>
)

data class OcrMenuItemDto(
    val name: String,
    val price_yen: Int,
    val notes: String?
)
