package com.example.restomaster.data.remote

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.restomaster.data.remote.gemini.GeminiContent
import com.example.restomaster.data.remote.gemini.GeminiInlineData
import com.example.restomaster.data.remote.gemini.GeminiPart
import com.example.restomaster.data.remote.gemini.GeminiRequest
import com.example.restomaster.data.remote.gemini.GeminiGenerationConfig
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class GeminiDirectClient @Inject constructor(
    private val geminiApi: GeminiApiService
) {

    private val prompt = """
        これは日本語の飲食店メニューの写真です。
        画像から「料理名」と「税込価格（円）」をすべて抽出してください。
        出力は必ず次のJSON配列のみ: [{"name":"料理名","price_yen":1234,"notes":null}, ...]
        name は日本語そのまま、price_yen は整数（円）、読めない行はスキップしてください。
    """.trimIndent()

    suspend fun recognizeMenu(bitmap: Bitmap, apiKey: String): List<OcrMenuItemDto> {
        val base64 = bitmap.toJpegBase64()
        Log.i("OCR_GEMINI", "Calling Gemini directly: bytes=${base64.length}")

        return try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(
                            GeminiPart(text = prompt),
                            GeminiPart(
                                inlineData = GeminiInlineData(
                                    mimeType = "image/jpeg",
                                    data = base64
                                )
                            )
                        )
                    )
                ),
                generationConfig = GeminiGenerationConfig(
                    responseMimeType = "application/json"
                )
            )

            val response = geminiApi.generateContent(
                model = "gemini-2.0-flash",
                apiKey = apiKey,
                body = request
            )
            val text = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?: ""

            Log.i("OCR_GEMINI", "Gemini raw text (first 200): ${text.take(200)}")

            val items = parseMenuItems(text)
            Log.i("OCR_GEMINI", "Gemini OCR items=${items.size}")
            items.forEachIndexed { idx, item ->
                Log.i("OCR_GEMINI", "item#$idx name='${item.name}', price=${item.price_yen}, notes=${item.notes}")
            }
            items
        } catch (e: Exception) {
            Log.e("OCR_GEMINI", "Gemini OCR failed: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun testKey(key: String): Boolean {
        return try {
            val req = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = "Reply with 'ok' only."))
                    )
                )
            )
            val res = geminiApi.generateContent(
                model = "gemini-2.0-flash",
                apiKey = key,
                body = req
            )
            val text = res.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?.trim()
                ?.lowercase()
            text == "ok"
        } catch (e: Exception) {
            Log.e("OCR_GEMINI", "testKey failed: ${e.message}", e)
            false
        }
    }

    private fun Bitmap.toJpegBase64(): String {
        val baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val bytes = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun parseMenuItems(json: String): List<OcrMenuItemDto> {
        return try {
            val arr = JSONArray(stripMarkdownJsonFence(json))
            (0 until arr.length()).mapNotNull { i ->
                val obj = arr.optJSONObject(i) ?: return@mapNotNull null
                val name = obj.optString("name", "").takeIf { it.isNotBlank() } ?: return@mapNotNull null
                val price = if (obj.isNull("price_yen")) -1 else obj.optInt("price_yen", -1)
                if (price < 0) return@mapNotNull null
                val notesRaw = if (obj.isNull("notes")) null else obj.optString("notes", "")
                val notes = notesRaw?.takeIf { it.isNotBlank() }
                OcrMenuItemDto(name = name, price_yen = price, notes = notes)
            }
        } catch (e: Exception) {
            Log.e("OCR_GEMINI", "Failed to parse Gemini JSON: ${e.message}", e)
            emptyList()
        }
    }

    private fun stripMarkdownJsonFence(raw: String): String {
        var text = raw.trim()
        if (text.startsWith("```")) {
            text = text.removePrefix("```json").removePrefix("```").trim()
            val end = text.lastIndexOf("```")
            if (end != -1) {
                text = text.substring(0, end).trim()
            }
        }
        return text
    }
    suspend fun generateImage(menuName: String, description: String? = null, apiKey: String): Bitmap? {
        Log.i("IMAGE_GEN", "========== START IMAGE GENERATION (Gemini 2.0) ==========")
        Log.i("IMAGE_GEN", "Menu name: $menuName")
        
        return try {
            val prompt = """
                Generate a high-quality, photorealistic food image of the Japanese dish "$menuName".
                ${description?.let { "Description: $it" } ?: ""}
                Style: Professional restaurant menu photography, appetizing, well-lit, top-down or 45-degree angle, on a nice plate.
                Do not include any text in the image.
            """.trimIndent()

            Log.i("IMAGE_GEN", "Prompt: $prompt")

            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = prompt))
                    )
                ),
                generationConfig = GeminiGenerationConfig(
                    responseModalities = listOf("IMAGE")
                )
            )
            
            Log.i("IMAGE_GEN", "Calling Gemini API (gemini-2.0-flash-exp)...")
            
            val response = try {
                geminiApi.generateContent(
                    model = "gemini-2.0-flash-exp",
                    apiKey = apiKey,
                    body = request
                )
            } catch (httpError: retrofit2.HttpException) {
                Log.e("IMAGE_GEN", "HTTP Error: ${httpError.code()} ${httpError.message()}")
                Log.e("IMAGE_GEN", "Response body: ${httpError.response()?.errorBody()?.string()}")
                throw httpError
            }

            Log.i("IMAGE_GEN", "API call completed. Parsing response...")
            
            val candidates = response.candidates
            if (candidates.isNullOrEmpty()) {
                Log.e("IMAGE_GEN", "No candidates in response")
                return null
            }

            // Look for inline data in parts
            val parts = candidates.first().content?.parts
            if (parts.isNullOrEmpty()) {
                Log.e("IMAGE_GEN", "No parts in content")
                return null
            }

            val imagePart = parts.find { it.inlineData != null }
            val base64Image = imagePart?.inlineData?.data

            if (base64Image.isNullOrBlank()) {
                Log.e("IMAGE_GEN", "No image data found in response parts")
                Log.i("IMAGE_GEN", "Parts received: ${parts.map { "text=${it.text?.take(20)}..., inlineData=${it.inlineData != null}" }}")
                return null
            }

            Log.i("IMAGE_GEN", "Image data found! Length: ${base64Image.length}")
            
            val imageBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
            val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            
            if (bitmap != null) {
                Log.i("IMAGE_GEN", "Bitmap created successfully: ${bitmap.width}x${bitmap.height}")
                Log.i("IMAGE_GEN", "========== IMAGE GENERATION COMPLETE ==========")
            } else {
                Log.e("IMAGE_GEN", "Failed to decode bitmap")
            }
            
            bitmap

        } catch (e: Exception) {
            Log.e("IMAGE_GEN", "Image generation failed: ${e.message}", e)
            null
        }
    }
}
