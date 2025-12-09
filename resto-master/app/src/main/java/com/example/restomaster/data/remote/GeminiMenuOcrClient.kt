package com.example.restomaster.data.remote

import android.graphics.Bitmap
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class GeminiMenuOcrClient @Inject constructor(
    private val api: MenuOcrApi
) {

    suspend fun recognize(bitmap: Bitmap): List<OcrMenuItemDto> {
        val bytes = bitmap.toJpegBytes()
        val requestBody = bytes.toRequestBody("image/jpeg".toMediaType())
        val part = MultipartBody.Part.createFormData(
            name = "file",
            filename = "menu.jpg",
            body = requestBody
        )
        val response = api.ocrMenu(part)
        return response.items
    }

    private fun Bitmap.toJpegBytes(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        return stream.toByteArray()
    }
}
