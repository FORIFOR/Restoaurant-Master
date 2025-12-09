package com.example.restomaster.data.ocr

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import com.example.restomaster.data.model.OcrTextLine
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MlKitTextRecognizer @Inject constructor() {

    private val recognizer = TextRecognition.getClient(
        JapaneseTextRecognizerOptions.Builder().build()
    )

    suspend fun recognize(
        bitmap: Bitmap,
        rotationDegrees: Int = 0
    ): List<OcrTextLine> =
        suspendCancellableCoroutine { cont ->
            val preprocessed = preprocess(bitmap, rotationDegrees)
            // rotationDegrees は InputImage にも渡し、ML Kit に実際の向きを伝える
            val image = InputImage.fromBitmap(preprocessed, rotationDegrees)
            recognizer.process(image)
                .addOnSuccessListener { text ->
                    val result = mutableListOf<OcrTextLine>()
                    text.textBlocks.forEach { block ->
                        block.lines.forEach { line ->
                            result += OcrTextLine(
                                text = line.text,
                                boundingBox = line.boundingBox
                            )
                        }
                    }
                    // Debug: raw OCR linesをログに出す
                    result.forEachIndexed { idx, line ->
                        android.util.Log.i(
                            "OCR_LINE_RAW",
                            "line#$idx text='${line.text}' bbox=${line.boundingBox} rot=$rotationDegrees"
                        )
                    }
                    cont.resume(result)
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }

    private fun preprocess(source: Bitmap, rotationDegrees: Int): Bitmap {
        val rotated = rotateIfNeeded(source, rotationDegrees)
        val scaled = scaleDown(rotated, 1600)
        val enhanced = enhanceContrastAndGrayscale(scaled, contrast = 1.2f)
        return applyAdaptiveBinarization(enhanced)
    }
    
    private fun applyAdaptiveBinarization(src: Bitmap): Bitmap {
        val width = src.width
        val height = src.height
        val pixels = IntArray(width * height)
        src.getPixels(pixels, 0, width, 0, 0, width, height)
        
        val threshold = 128 // Base threshold
        val windowSize = 15 // Adaptive window size
        
        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                val pixel = pixels[index]
                
                // Convert to grayscale (if not already)
                val gray = (android.graphics.Color.red(pixel) + 
                           android.graphics.Color.green(pixel) + 
                           android.graphics.Color.blue(pixel)) / 3
                
                // Calculate local mean
                var sum = 0
                var count = 0
                for (dy in -windowSize/2..windowSize/2) {
                    for (dx in -windowSize/2..windowSize/2) {
                        val nx = x + dx
                        val ny = y + dy
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            val nIndex = ny * width + nx
                            val nPixel = pixels[nIndex]
                            val nGray = (android.graphics.Color.red(nPixel) + 
                                        android.graphics.Color.green(nPixel) + 
                                        android.graphics.Color.blue(nPixel)) / 3
                            sum += nGray
                            count++
                        }
                    }
                }
                val localMean = if (count > 0) sum / count else threshold
                
                // Apply threshold
                val binaryValue = if (gray > localMean - 10) 255 else 0
                pixels[index] = android.graphics.Color.rgb(binaryValue, binaryValue, binaryValue)
            }
        }
        
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        result.setPixels(pixels, 0, width, 0, 0, width, height)
        return result
    }

    private fun rotateIfNeeded(src: Bitmap, degrees: Int): Bitmap {
        if (degrees % 360 == 0) return src
        val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    private fun scaleDown(src: Bitmap, maxDimension: Int): Bitmap {
        val w = src.width
        val h = src.height
        if (w <= maxDimension && h <= maxDimension) return src
        val ratio = if (w > h) maxDimension.toFloat() / w else maxDimension.toFloat() / h
        val newW = (w * ratio).toInt()
        val newH = (h * ratio).toInt()
        return Bitmap.createScaledBitmap(src, newW, newH, true)
    }

    private fun enhanceContrastAndGrayscale(src: Bitmap, contrast: Float): Bitmap {
        val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
        val translate = 128f * (1 - contrast)
        val contrastMatrix = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, translate,
                0f, contrast, 0f, 0f, translate,
                0f, 0f, contrast, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            )
        )
        colorMatrix.postConcat(contrastMatrix)

        val out = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
        canvas.drawBitmap(src, 0f, 0f, paint)
        return out
    }
}
