package com.example.restomaster.domain.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.restomaster.domain.model.QrCodeSize
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class PdfGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val qrCodeGenerator: QrCodeGenerator
) {

    suspend fun exportSingleTableQr(
        storeName: String,
        tableNumber: Int,
        tableToken: String,
        qrSize: QrCodeSize
    ): Uri {
        // QR content URL (local host IP for now)
        val qrContent = "http://192.168.43.1:8080/t/$tableToken"

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(
            595, 842, 1 // A4 size in points (72 DPI)
        ).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 24f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 18f
            isAntiAlias = true
        }

        // Draw title
        val margin = 48f
        canvas.drawText(storeName, margin, 80f, titlePaint)
        canvas.drawText("テーブル $tableNumber", margin, 120f, textPaint)

        // Generate and draw QR code
        val qrBitmap = qrCodeGenerator.generate(
            content = qrContent,
            sizePx = qrSize.pixels
        )

        val centerX = pageInfo.pageWidth / 2f
        val qrDisplaySize = 300 // Display size on PDF (points)
        val qrLeft = centerX - qrDisplaySize / 2f
        val qrTop = 180f

        val srcRect = Rect(0, 0, qrBitmap.width, qrBitmap.height)
        val dstRect = Rect(
            qrLeft.toInt(),
            qrTop.toInt(),
            (qrLeft + qrDisplaySize).toInt(),
            (qrTop + qrDisplaySize).toInt()
        )
        canvas.drawBitmap(qrBitmap, srcRect, dstRect, null)

        // Draw footer
        val footerPaint = Paint().apply {
            color = Color.GRAY
            textSize = 14f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(
            "スマートフォンでQRコードをスキャンして注文してください",
            centerX,
            qrTop + qrDisplaySize + 40f,
            footerPaint
        )

        pdfDocument.finishPage(page)

        // Save to app-specific external storage
        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "restaurant_qr"
        ).apply { mkdirs() }

        val file = File(
            dir,
            "table_${tableNumber}_${qrSize.name.lowercase()}.pdf"
        )

        FileOutputStream(file).use { fos ->
            pdfDocument.writeTo(fos)
        }
        pdfDocument.close()

        // Return URI via FileProvider
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    // Legacy method for backward compatibility
    fun createQrPdf(
        tableNumber: Int,
        qrBitmap: Bitmap,
        fileName: String = "Table_$tableNumber.pdf"
    ): File? {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points (approx)
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // Draw Title
        // Draw Title (Table Number)
        paint.color = Color.BLACK
        paint.textSize = 36f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("テーブル $tableNumber", pageInfo.pageWidth / 2f, 80f, paint)

        // Draw Instructions (Step 1-3)
        paint.textSize = 20f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textAlign = Paint.Align.LEFT
        val instructLeft = 80f
        val instructTop = 140f
        val lineHeight = 35f

        canvas.drawText("STEP 1. スマートフォンのカメラを起動", instructLeft, instructTop, paint)
        canvas.drawText("STEP 2. QRコードをスキャン", instructLeft, instructTop + lineHeight, paint)
        canvas.drawText("STEP 3. メニューから注文", instructLeft, instructTop + lineHeight * 2, paint)

        // Draw QR Code
        val qrSize = 350
        val left = (pageInfo.pageWidth - qrSize) / 2
        val top = 280
        val srcRect = Rect(0, 0, qrBitmap.width, qrBitmap.height)
        val dstRect = Rect(left, top, left + qrSize, top + qrSize)
        canvas.drawBitmap(qrBitmap, srcRect, dstRect, null)

        // Draw Footer
        paint.textSize = 16f
        paint.textAlign = Paint.Align.CENTER
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("ご来店ありがとうございます", pageInfo.pageWidth / 2f, top + qrSize + 60f, paint)

        document.finishPage(page)

        // Save to App-specific Documents directory (does not require permission on Android 10+)
        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "restaurant_qr"
        )
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, fileName)

        return try {
            document.writeTo(FileOutputStream(file))
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            document.close()
        }
    }
}
