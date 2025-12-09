package com.example.restomaster.domain.usecase


import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MenuOcrUseCase @Inject constructor() {

    private val recognizer = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())

    suspend fun execute(bitmap: Bitmap): List<ParsedMenuItem> {
        val image = InputImage.fromBitmap(bitmap, 0)
        return try {
            val result = recognizer.process(image).await()
            parseTextToMenuItems(result.text)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun parseTextToMenuItems(text: String): List<ParsedMenuItem> {
        val lines = text.split("\n")
        val parsedItems = mutableListOf<ParsedMenuItem>()

        // Simple heuristic: Look for lines that have text followed by a number (price)
        // This is a very basic parser and can be improved with Regex or AI later.
        val priceRegex = Regex("""(\d{3,})""") // Matches numbers with 3 or more digits (e.g., 100, 1000)

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            val priceMatch = priceRegex.find(trimmed)
            if (priceMatch != null) {
                // Assume text before the price is the name
                val priceStr = priceMatch.value
                val name = trimmed.replace(priceStr, "").replace("円", "").replace("¥", "").trim()
                
                // Filter out noise (too short names)
                if (name.length > 1) {
                    val price = priceStr.toIntOrNull() ?: 0
                    parsedItems.add(ParsedMenuItem(name, price))
                }
            }
        }
        return parsedItems
    }
}

data class ParsedMenuItem(
    val name: String,
    val price: Int
)
