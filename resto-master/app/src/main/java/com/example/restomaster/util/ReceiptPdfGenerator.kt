package com.example.restomaster.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.restomaster.domain.model.ReceiptData
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility class for generating invoice-compliant PDF receipts
 * following Japanese インボイス制度 requirements
 */
class ReceiptPdfGenerator(private val context: Context) {

    companion object {
        // Page dimensions (thermal receipt-like: narrow and tall)
        private const val PAGE_WIDTH = 280
        private const val PAGE_HEIGHT = 800
        
        // Margins
        private const val MARGIN_LEFT = 16f
        private const val MARGIN_RIGHT = 16f
        private const val CONTENT_WIDTH = PAGE_WIDTH - 32
        
        // Font sizes
        private const val FONT_SIZE_TITLE = 14f
        private const val FONT_SIZE_HEADER = 11f
        private const val FONT_SIZE_NORMAL = 10f
        private const val FONT_SIZE_SMALL = 8f
        
        // Line heights
        private const val LINE_HEIGHT_TITLE = 18f
        private const val LINE_HEIGHT_NORMAL = 14f
        private const val LINE_HEIGHT_SMALL = 12f
    }

    private val numberFormat = NumberFormat.getInstance(Locale.JAPAN)
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN)

    // Paints
    private val titlePaint = Paint().apply {
        color = Color.BLACK
        textSize = FONT_SIZE_TITLE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }

    private val headerPaint = Paint().apply {
        color = Color.BLACK
        textSize = FONT_SIZE_HEADER
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val normalPaint = Paint().apply {
        color = Color.BLACK
        textSize = FONT_SIZE_NORMAL
        isAntiAlias = true
    }

    private val normalRightPaint = Paint().apply {
        color = Color.BLACK
        textSize = FONT_SIZE_NORMAL
        textAlign = Paint.Align.RIGHT
        isAntiAlias = true
    }

    private val smallPaint = Paint().apply {
        color = Color.BLACK
        textSize = FONT_SIZE_SMALL
        isAntiAlias = true
    }

    private val linePaint = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 0.5f
    }

    private val boldPaint = Paint().apply {
        color = Color.BLACK
        textSize = FONT_SIZE_NORMAL
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }

    private val boldRightPaint = Paint().apply {
        color = Color.BLACK
        textSize = FONT_SIZE_NORMAL
        textAlign = Paint.Align.RIGHT
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }

    /**
     * Generate PDF file from receipt data
     * @return File path of generated PDF
     */
    fun generatePdf(receiptData: ReceiptData): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        var y = 24f
        val centerX = PAGE_WIDTH / 2f

        // Store name (centered)
        canvas.drawText(receiptData.storeName, centerX, y, titlePaint)
        y += LINE_HEIGHT_TITLE

        // Store address (centered)
        if (receiptData.storeAddress.isNotBlank()) {
            // Split address if too long
            val addressLines = wrapText(receiptData.storeAddress, headerPaint, CONTENT_WIDTH.toFloat())
            addressLines.forEach { line ->
                canvas.drawText(line, centerX, y, headerPaint)
                y += LINE_HEIGHT_NORMAL
            }
        }

        // Phone
        if (!receiptData.storePhone.isNullOrBlank()) {
            canvas.drawText("TEL: ${receiptData.storePhone}", centerX, y, headerPaint)
            y += LINE_HEIGHT_NORMAL
        }

        // Invoice registration number
        if (!receiptData.invoiceRegistrationNumber.isNullOrBlank()) {
            canvas.drawText("登録番号: ${receiptData.invoiceRegistrationNumber}", centerX, y, smallPaint.apply { textAlign = Paint.Align.CENTER })
            y += LINE_HEIGHT_SMALL
        }

        y += 4f
        
        // Separator line
        canvas.drawLine(MARGIN_LEFT, y, PAGE_WIDTH - MARGIN_RIGHT, y, linePaint)
        y += LINE_HEIGHT_NORMAL

        // Title: 領収書
        canvas.drawText("領　収　書", centerX, y, titlePaint)
        y += LINE_HEIGHT_TITLE

        // Separator line
        canvas.drawLine(MARGIN_LEFT, y, PAGE_WIDTH - MARGIN_RIGHT, y, linePaint)
        y += LINE_HEIGHT_NORMAL

        // Date and receipt number
        canvas.drawText(dateFormat.format(Date(receiptData.issuedAt)), MARGIN_LEFT, y, normalPaint)
        y += LINE_HEIGHT_NORMAL
        canvas.drawText("No. ${receiptData.receiptNumber}", MARGIN_LEFT, y, normalPaint)
        y += LINE_HEIGHT_NORMAL + 8f

        // Items header
        canvas.drawText("商品名", MARGIN_LEFT, y, smallPaint)
        canvas.drawText("数量", MARGIN_LEFT + 120, y, smallPaint)
        canvas.drawText("単価", MARGIN_LEFT + 160, y, smallPaint)
        canvas.drawText("金額", PAGE_WIDTH - MARGIN_RIGHT, y, smallPaint.apply { textAlign = Paint.Align.RIGHT })
        smallPaint.textAlign = Paint.Align.LEFT // Reset
        y += LINE_HEIGHT_SMALL

        // Separator line (dotted effect with shorter line)
        canvas.drawLine(MARGIN_LEFT, y, PAGE_WIDTH - MARGIN_RIGHT, y, linePaint)
        y += 4f

        // Line items
        for (item in receiptData.items) {
            val itemName = if (item.isReducedTax) "${item.name}※" else item.name
            
            // Item name (may need wrapping)
            val displayName = if (itemName.length > 12) itemName.take(11) + "…" else itemName
            canvas.drawText(displayName, MARGIN_LEFT, y, normalPaint)
            canvas.drawText("${item.quantity}", MARGIN_LEFT + 130, y, normalPaint)
            canvas.drawText("¥${numberFormat.format(item.unitPrice)}", MARGIN_LEFT + 160, y, normalPaint)
            canvas.drawText("¥${numberFormat.format(item.lineTotal)}", PAGE_WIDTH - MARGIN_RIGHT, y, normalRightPaint)
            y += LINE_HEIGHT_NORMAL
        }

        y += 4f
        // Separator line
        canvas.drawLine(MARGIN_LEFT, y, PAGE_WIDTH - MARGIN_RIGHT, y, linePaint)
        y += LINE_HEIGHT_NORMAL

        // Tax breakdown
        val tb = receiptData.taxBreakdown

        // 10% items
        if (tb.subtotal10 > 0) {
            canvas.drawText("10%対象", MARGIN_LEFT, y, normalPaint)
            canvas.drawText("¥${numberFormat.format(tb.subtotal10 + tb.tax10)}", MARGIN_LEFT + 80, y, normalPaint)
            canvas.drawText("消費税 ¥${numberFormat.format(tb.tax10)}", PAGE_WIDTH - MARGIN_RIGHT, y, normalRightPaint)
            y += LINE_HEIGHT_NORMAL
        }

        // 8% items (reduced tax)
        if (tb.subtotal8 > 0) {
            canvas.drawText("8%対象※", MARGIN_LEFT, y, normalPaint)
            canvas.drawText("¥${numberFormat.format(tb.subtotal8 + tb.tax8)}", MARGIN_LEFT + 80, y, normalPaint)
            canvas.drawText("消費税 ¥${numberFormat.format(tb.tax8)}", PAGE_WIDTH - MARGIN_RIGHT, y, normalRightPaint)
            y += LINE_HEIGHT_NORMAL
        }

        y += 4f
        // Separator line
        canvas.drawLine(MARGIN_LEFT, y, PAGE_WIDTH - MARGIN_RIGHT, y, linePaint)
        y += LINE_HEIGHT_NORMAL

        // Total
        canvas.drawText("合計", MARGIN_LEFT, y, boldPaint)
        canvas.drawText("¥${numberFormat.format(receiptData.totalAmount)}", PAGE_WIDTH - MARGIN_RIGHT, y, boldRightPaint)
        y += LINE_HEIGHT_NORMAL

        // Tax included
        canvas.drawText("（内消費税", MARGIN_LEFT, y, smallPaint)
        canvas.drawText("¥${numberFormat.format(tb.totalTax)}）", PAGE_WIDTH - MARGIN_RIGHT, y, smallPaint.apply { textAlign = Paint.Align.RIGHT })
        smallPaint.textAlign = Paint.Align.LEFT
        y += LINE_HEIGHT_NORMAL

        // Payment method
        if (!receiptData.paymentMethod.isNullOrBlank()) {
            y += 4f
            val paymentLabel = when (receiptData.paymentMethod) {
                "CASH" -> "現金"
                "STRIPE" -> "クレジットカード"
                "PAYPAY" -> "PayPay"
                "LINEPAY" -> "LINE Pay"
                "MIXED" -> "複合払い"
                else -> receiptData.paymentMethod
            }
            canvas.drawText("お支払い: $paymentLabel", MARGIN_LEFT, y, normalPaint)
            y += LINE_HEIGHT_NORMAL
        }

        y += 8f
        // Separator line
        canvas.drawLine(MARGIN_LEFT, y, PAGE_WIDTH - MARGIN_RIGHT, y, linePaint)
        y += LINE_HEIGHT_NORMAL

        // Reduced tax note
        if (receiptData.items.any { it.isReducedTax }) {
            canvas.drawText("※は軽減税率対象商品です", MARGIN_LEFT, y, smallPaint)
            y += LINE_HEIGHT_SMALL
        }

        // Thank you message
        y += 8f
        canvas.drawText("ありがとうございました", centerX, y, headerPaint)

        document.finishPage(page)

        // Save to file
        val receiptsDir = File(context.filesDir, "receipts")
        if (!receiptsDir.exists()) {
            receiptsDir.mkdirs()
        }
        
        val fileName = "receipt_${receiptData.receiptNumber.replace("-", "_")}.pdf"
        val file = File(receiptsDir, fileName)
        
        FileOutputStream(file).use { outputStream ->
            document.writeTo(outputStream)
        }
        
        document.close()
        
        return file
    }

    /**
     * Wrap text to fit within specified width
     */
    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val words = text.toCharArray()
        val lines = mutableListOf<String>()
        var currentLine = StringBuilder()
        
        for (char in words) {
            val testLine = currentLine.toString() + char
            if (paint.measureText(testLine) <= maxWidth) {
                currentLine.append(char)
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine.toString())
                }
                currentLine = StringBuilder(char.toString())
            }
        }
        
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
        }
        
        return lines
    }
}
