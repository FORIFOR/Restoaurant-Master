package com.example.restomaster.data.remote

import android.util.Log
import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.data.remote.gemini.GeminiContent
import com.example.restomaster.data.remote.gemini.GeminiGenerationConfig
import com.example.restomaster.data.remote.gemini.GeminiPart
import com.example.restomaster.data.remote.gemini.GeminiRequest
import org.json.JSONObject
import javax.inject.Inject

data class TranslationResultDto(
    val name: String,
    val description: String?
)

class GeminiTranslationClient @Inject constructor(
    private val geminiApi: GeminiApiService
) {

    suspend fun translateMenuItem(
        item: MenuItemEntity,
        targetLangs: List<String>,
        apiKey: String
    ): Map<String, TranslationResultDto> {
        if (targetLangs.isEmpty()) return emptyMap()

        val prompt = buildPrompt(item, targetLangs)
        Log.i("GEMINI_TRANS", "Prompt: $prompt")

        return try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = prompt))
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

            Log.i("GEMINI_TRANS", "Response: $text")
            parseResponse(text, targetLangs)

        } catch (e: Exception) {
            Log.e("GEMINI_TRANS", "Translation failed: ${e.message}", e)
            emptyMap()
        }
    }

    private fun buildPrompt(item: MenuItemEntity, targetLangs: List<String>): String {
        val langsJson = targetLangs.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }
        return """
            次の日本語の料理名と説明を、指定された複数の言語に翻訳してください。

            - 出力は必ず JSON のみ、コードブロックなし。
            - 各言語ごとに name と description を返してください。
            - 料理名は自然な現地語の言い方にしてくださいが、意訳しすぎないでください。
            - 説明文は簡潔に1〜2文にまとめてください。
            - descriptionが元の日本語でない場合は、空文字ではなく翻訳してください。

            入力:
            料理名: ${item.name}
            説明: ${item.description ?: "なし"}

            翻訳対象言語: $langsJson

            出力フォーマット:
            {
              "en": {"name": "...", "description": "..."},
              "zh-Hans": {"name": "...", "description": "..."},
              ...
            }
        """.trimIndent()
    }

    private fun parseResponse(json: String, targetLangs: List<String>): Map<String, TranslationResultDto> {
        val cleanJson = stripMarkdownJsonFence(json)
        val result = mutableMapOf<String, TranslationResultDto>()
        try {
            val root = JSONObject(cleanJson)
            for (lang in targetLangs) {
                val obj = root.optJSONObject(lang)
                if (obj != null) {
                    val name = obj.optString("name")
                    val desc = obj.optString("description")
                    if (name.isNotBlank()) {
                        result[lang] = TranslationResultDto(name, desc)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GEMINI_TRANS", "JSON Parse error: ${e.message}", e)
        }
        return result
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
}
