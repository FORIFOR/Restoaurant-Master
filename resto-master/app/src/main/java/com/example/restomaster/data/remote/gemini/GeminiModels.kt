package com.example.restomaster.data.remote.gemini

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    @Json(name = "generationConfig")
    val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val role: String = "user",
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null,
    @Json(name = "inline_data") val inlineData: GeminiInlineData? = null
)

@JsonClass(generateAdapter = true)
data class GeminiInlineData(
    @Json(name = "mime_type") val mimeType: String,
    val data: String
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContentParts?
)

@JsonClass(generateAdapter = true)
data class GeminiContentParts(
    val parts: List<GeminiPart>?
)

@JsonClass(generateAdapter = true)
data class GeminiPartText(
    val text: String?
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @Json(name = "response_mime_type")
    val responseMimeType: String? = null,
    @Json(name = "response_modalities")
    val responseModalities: List<String>? = null
)

// Imagen 3 API Models (using :predict endpoint)
@JsonClass(generateAdapter = true)
data class ImagenRequest(
    val instances: List<ImagenInstance>,
    val parameters: ImagenParameters
)

@JsonClass(generateAdapter = true)
data class ImagenInstance(
    val prompt: ImagenPrompt
)

@JsonClass(generateAdapter = true)
data class ImagenPrompt(
    val text: String
)

@JsonClass(generateAdapter = true)
data class ImagenParameters(
    val sampleCount: Int = 1,
    val aspectRatio: String = "ASPECT_RATIO_1_1"
)

@JsonClass(generateAdapter = true)
data class ImagenResponse(
    val predictions: List<ImagenPrediction>?
)

@JsonClass(generateAdapter = true)
data class ImagenPrediction(
    @Json(name = "bytesBase64Encoded") val bytesBase64Encoded: String  // Base64 encoded image
)
