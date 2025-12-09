package com.example.restomaster.domain.usecase

import android.graphics.Bitmap
import com.example.restomaster.data.remote.GeminiDirectClient
import com.example.restomaster.data.repository.AppSettingsRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class MenuOcrGeminiUseCase @Inject constructor(
    private val client: GeminiDirectClient,
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend fun execute(bitmap: Bitmap): List<ParsedMenuItem> {
        return try {
            val apiKey = appSettingsRepository.geminiApiKeyFlow.firstOrNull().orEmpty()
            if (apiKey.isBlank()) return emptyList()

            val items = client.recognizeMenu(bitmap, apiKey)
            android.util.Log.i("OCR_FLOW", "Using Gemini result: items=${items.size}")
            items.map { dto ->
                ParsedMenuItem(
                    name = dto.name,
                    price = dto.price_yen
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("OCR_FLOW", "Gemini use case failed: ${e.message}", e)
            emptyList()
        }
    }
}
