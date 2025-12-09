package com.example.restomaster.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.restomaster.data.remote.GeminiDirectClient
import com.example.restomaster.data.repository.AppSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class GenerateMenuImageUseCase @Inject constructor(
    private val client: com.example.restomaster.data.remote.OpenAIImageClient,
    private val appSettingsRepository: AppSettingsRepository,
    @ApplicationContext private val context: Context
) {
    suspend fun execute(menuName: String, description: String? = null): String? {
        Log.i("GENERATE_IMAGE_UC", "========== UseCase START (OpenAI) ==========")
        Log.i("GENERATE_IMAGE_UC", "Menu name: $menuName, Description: $description")
        
        val apiKey = appSettingsRepository.openAiApiKeyFlow.firstOrNull().orEmpty()
        if (apiKey.isBlank()) {
            Log.e("GENERATE_IMAGE_UC", "OpenAI API key is blank!")
            return null
        }
        
        Log.i("GENERATE_IMAGE_UC", "API key obtained, length: ${apiKey.length}")
        Log.i("GENERATE_IMAGE_UC", "Calling OpenAIImageClient.generateImage()...")

        val prompt = buildString {
            append("Professional food photo of Japanese dish \"")
            append(menuName)
            append("\". Restaurant menu style, appetizing, 1:1 aspect ratio, no text in image.")
            if (!description.isNullOrBlank()) {
                append(" Description: ")
                append(description)
            }
        }

        val bitmap = client.generateImage(prompt, apiKey)
        
        if (bitmap == null) {
            Log.e("GENERATE_IMAGE_UC", "generateImage() returned null - image generation failed")
            return null
        }
        
        Log.i("GENERATE_IMAGE_UC", "Bitmap received: ${bitmap.width}x${bitmap.height}")
        
        return withContext(Dispatchers.IO) {
            try {
                val fileName = "ai_menu_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)
                
                Log.i("GENERATE_IMAGE_UC", "Saving image to: ${file.absolutePath}")
                
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                
                Log.i("GENERATE_IMAGE_UC", "Image saved successfully!")
                Log.i("GENERATE_IMAGE_UC", "========== UseCase COMPLETE ==========")
                file.absolutePath
            } catch (e: Exception) {
                Log.e("GENERATE_IMAGE_UC", "Failed to save image: ${e.message}", e)
                e.printStackTrace()
                null
            }
        }
    }
}
