package com.example.restomaster.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenAIImageClient @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateImage(prompt: String, apiKey: String): Bitmap? = withContext(Dispatchers.IO) {
        Log.i("OPENAI_CLIENT", "Generating image with prompt: $prompt")
        
        val jsonBody = JSONObject().apply {
            put("model", "dall-e-3")
            put("prompt", prompt)
            put("n", 1)
            put("size", "1024x1024")
            put("response_format", "b64_json")
        }

        val request = Request.Builder()
            .url("https://api.openai.com/v1/images/generations")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful) {
                Log.e("OPENAI_CLIENT", "API Error: ${response.code} $responseBody")
                return@withContext null
            }

            if (responseBody.isNullOrBlank()) {
                Log.e("OPENAI_CLIENT", "Empty response body")
                return@withContext null
            }

            val jsonResponse = JSONObject(responseBody)
            val dataArray = jsonResponse.optJSONArray("data")
            if (dataArray == null || dataArray.length() == 0) {
                Log.e("OPENAI_CLIENT", "No data in response")
                return@withContext null
            }

            val b64Json = dataArray.getJSONObject(0).getString("b64_json")
            val imageBytes = Base64.decode(b64Json, Base64.DEFAULT)
            
            Log.i("OPENAI_CLIENT", "Image generated successfully. Size: ${imageBytes.size} bytes")
            
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        } catch (e: IOException) {
            Log.e("OPENAI_CLIENT", "Network error: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e("OPENAI_CLIENT", "Unexpected error: ${e.message}", e)
            null
        }
    }
}
