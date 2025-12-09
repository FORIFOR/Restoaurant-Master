package com.example.restomaster.data.receipt

import com.example.restomaster.domain.model.ReceiptDto
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptGenerator @Inject constructor() {

    companion object {
        private const val ESC: Byte = 0x1B
        private const val GS: Byte = 0x1D
        private const val LINE_WIDTH = 32 // Standard 58mm printer usually 32 chars
    }

    fun toEscPosBytes(receipt: ReceiptDto): ByteArray {
        val stream = ByteArrayOutputStream()
        val charset = Charset.forName("Shift_JIS") // Common for Japanese POS printers

        fun write(text: String) {
            stream.write(text.toByteArray(charset))
        }

        fun writeLine(text: String = "") {
            write(text + "\n")
        }

        fun writeCmd(vararg bytes: Byte) {
            stream.write(bytes)
        }

        // --- Initialization ---
        writeCmd(ESC, '@'.code.toByte()) // Initialize printer

        // --- Header ---
        writeCmd(ESC, 'a'.code.toByte(), 1) // Center align
        writeCmd(GS, '!'.code.toByte(), 0x11) // Double width & height
        writeLine(receipt.storeName)
        writeCmd(GS, '!'.code.toByte(), 0x00) // Normal size
        writeLine()
        
        writeLine(receipt.storeAddress)
        receipt.storePhone?.let { writeLine("TEL: $it") }
        receipt.invoiceRegistrationNumber?.let { 
            writeLine("登録番号: $it") 
        }
        writeLine()

        // --- Transaction Info ---
        writeCmd(ESC, 'a'.code.toByte(), 0) // Left align
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        writeLine("日時: ${receipt.transactionDate.format(dtf)}")
        writeLine("伝票No: ${receipt.receiptId}")
        receipt.tableNumber?.let { writeLine("テーブル: $it") }
        writeLine("-".repeat(LINE_WIDTH))

        // --- Items ---
        writeLine("領収証")
        writeLine()
        
        receipt.items.forEach { item ->
            val name = item.name + if (item.isReducedTax) " *" else ""
            val qtyPrice = "${item.quantity} x ¥${formatPrice(item.unitPrice)}"
            val lineTotal = "¥${formatPrice(item.lineTotal)}"
            
            writeLine(name)
            writeLine(formatSpaceBetween(qtyPrice, lineTotal))
        }
        writeLine("-".repeat(LINE_WIDTH))

        // --- Totals ---
        // Subtotals by tax rate
        receipt.taxSummaries.forEach { tax ->
            val rateLabel = "${tax.rate}%対象"
            val taxable = "¥${formatPrice(tax.taxableAmount)}"
            val taxAmount = "¥${formatPrice(tax.taxAmount)}"
            
            writeLine(formatSpaceBetween(rateLabel, taxable))
            writeLine(formatSpaceBetween("  (内消費税等", "$taxAmount)"))
        }
        writeLine("-".repeat(LINE_WIDTH))

        // Grand Total
        writeCmd(GS, '!'.code.toByte(), 0x01) // Double height? Or just bold
        writeCmd(ESC, 'E'.code.toByte(), 1) // Bold on
        writeLine(formatSpaceBetween("合計", "¥${formatPrice(receipt.totalAmount)}"))
        writeCmd(ESC, 'E'.code.toByte(), 0) // Bold off
        writeCmd(GS, '!'.code.toByte(), 0x00) // Normal size
        
        writeLine()
        writeLine("支払方法: ${receipt.paymentMethod}")
        if (receipt.receivedAmount != null) {
            writeLine(formatSpaceBetween("お預かり", "¥${formatPrice(receipt.receivedAmount)}"))
        }
        if (receipt.changeAmount != null) {
            writeLine(formatSpaceBetween("お釣り", "¥${formatPrice(receipt.changeAmount)}"))
        }

        // --- Footer ---
        writeLine()
        writeLine("*は軽減税率対象商品です")
        writeLine()
        writeCmd(ESC, 'a'.code.toByte(), 1) // Center align
        writeLine("ありがとうございました")
        writeLine()
        writeLine()
        writeLine()
        
        // Cut paper
        writeCmd(GS, 'V'.code.toByte(), 66, 0)

        return stream.toByteArray()
    }

    private fun formatPrice(price: Int): String {
        return String.format("%,d", price)
    }

    private fun formatSpaceBetween(left: String, right: String): String {
        val totalLen = getByteLength(left) + getByteLength(right)
        val spaces = LINE_WIDTH - totalLen
        return if (spaces > 0) {
            left + " ".repeat(spaces) + right
        } else {
            // If overflow, just separate by one space or wrap? 
            // For simplicity, just concat with space
            "$left $right"
        }
    }

    private fun getByteLength(text: String): Int {
        // Shift_JIS byte length approximation
        // ASCII = 1, Japanese = 2
        var len = 0
        text.forEach { c ->
            len += if (c.code < 128) 1 else 2
        }
        return len
    }
}
