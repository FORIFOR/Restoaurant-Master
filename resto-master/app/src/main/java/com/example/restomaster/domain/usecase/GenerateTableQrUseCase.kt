package com.example.restomaster.domain.usecase

import android.graphics.Bitmap
import android.graphics.Color
import com.example.restomaster.domain.repository.TableRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import javax.inject.Inject

class GenerateTableQrUseCase @Inject constructor(
    private val tableRepository: TableRepository
) {
    suspend operator fun invoke(tableId: Long, sizePx: Int = 500): Bitmap? {
        // In a real app we might fetch the table to get the token, 
        // but here we might just pass the content directly or fetch it.
        // Let's assume we fetch the token from repo if we only have ID, 
        // or we can just generate for a given string.
        // For now, let's make it generic to generate QR for a string.
        return null // Placeholder, actual logic below
    }

    fun generate(content: String, sizePx: Int): Bitmap {
        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.MARGIN to 1
        )
        val bitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            sizePx,
            sizePx,
            hints
        )
        val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        for (x in 0 until sizePx) {
            for (y in 0 until sizePx) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }
}
