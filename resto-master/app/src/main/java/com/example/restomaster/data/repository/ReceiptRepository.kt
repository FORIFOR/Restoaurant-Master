package com.example.restomaster.data.repository

import com.example.restomaster.data.local.dao.BillDao
import com.example.restomaster.data.local.dao.BillItemDao
import com.example.restomaster.data.local.dao.ReceiptDao
import com.example.restomaster.data.local.dao.StoreDao
import com.example.restomaster.data.local.entity.ReceiptEntity
import com.example.restomaster.domain.model.ReceiptData
import com.example.restomaster.domain.model.ReceiptLineItem
import com.example.restomaster.domain.model.TaxBreakdown
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptRepository @Inject constructor(
    private val receiptDao: ReceiptDao,
    private val billDao: BillDao,
    private val billItemDao: BillItemDao,
    private val storeDao: StoreDao
) {
    companion object {
        private const val TAX_RATE_REDUCED = 0.08f
        private const val TAX_RATE_STANDARD = 0.10f
    }

    /**
     * Get or create a receipt for a bill
     */
    suspend fun getOrCreateReceiptForBill(billId: Long, storeId: Long = 1L): ReceiptData? {
        // Check if receipt already exists
        val existingReceipt = receiptDao.getReceiptForBill(billId)
        if (existingReceipt != null) {
            return buildReceiptData(existingReceipt, billId, storeId)
        }

        // Create new receipt
        val bill = billDao.getBill(billId) ?: return null
        val store = storeDao.getStoreById(storeId)

        // Generate receipt number (format: YYYYMMDD-NNN)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.JAPAN)
        val datePrefix = dateFormat.format(Date())
        val maxNumber = receiptDao.getMaxReceiptNumberForDate(datePrefix) ?: 0
        val receiptNumber = "$datePrefix-${String.format("%03d", maxNumber + 1)}"

        // Calculate tax breakdown using stored tax amounts
        val billItems = billItemDao.findByBillId(billId)
        val items8 = billItems.filter { it.taxRate == TAX_RATE_REDUCED }
        val items10 = billItems.filter { it.taxRate == TAX_RATE_STANDARD }

        val subtotal8 = items8.sumOf { it.lineTotal - it.taxAmount }
        val tax8 = items8.sumOf { it.taxAmount }
        val subtotal10 = items10.sumOf { it.lineTotal - it.taxAmount }
        val tax10 = items10.sumOf { it.taxAmount }

        val taxBreakdownJson = """{"subtotal8":$subtotal8,"tax8":$tax8,"subtotal10":$subtotal10,"tax10":$tax10}"""

        val receipt = ReceiptEntity(
            billId = billId,
            receiptNumber = receiptNumber,
            issuedAt = System.currentTimeMillis(),
            invoiceRegistrationNumber = store?.invoiceRegistrationNumber ?: "",
            taxIncluded = store?.taxIncludedPricing ?: true,
            subtotal = bill.subtotal,
            taxAmount = bill.tax,
            totalAmount = bill.totalAmount,
            taxBreakdownJson = taxBreakdownJson
        )

        val receiptId = receiptDao.insertReceipt(receipt)
        return buildReceiptData(receipt.copy(id = receiptId), billId, storeId)
    }

    /**
     * Get receipt by ID
     */
    suspend fun getReceiptById(receiptId: Long, storeId: Long = 1L): ReceiptData? {
        val receipt = receiptDao.getReceiptById(receiptId) ?: return null
        return buildReceiptData(receipt, receipt.billId, storeId)
    }

    /**
     * Get all receipts
     */
    fun getAllReceipts(): Flow<List<ReceiptEntity>> {
        return receiptDao.getAllReceipts()
    }

    /**
     * Build ReceiptData from entity
     */
    private suspend fun buildReceiptData(
        receipt: ReceiptEntity,
        billId: Long,
        storeId: Long
    ): ReceiptData {
        val store = storeDao.getStoreById(storeId)
        val bill = billDao.getBill(billId)
        val billItems = billItemDao.findByBillId(billId)
        val taxIncluded = store?.taxIncludedPricing ?: true

        // Build address string
        val addressParts = listOfNotNull(
            store?.postalCode?.let { "〒$it" },
            store?.addressLine1,
            store?.addressLine2
        )
        val fullAddress = if (addressParts.isNotEmpty()) {
            addressParts.joinToString(" ")
        } else {
            store?.address ?: ""
        }

        // Parse tax breakdown from JSON
        val taxBreakdown = parseTaxBreakdown(receipt.taxBreakdownJson)

        // Convert bill items to receipt line items
        val lineItems = billItems.map { item ->
            ReceiptLineItem(
                name = item.name,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
                lineTotal = item.lineTotal,
                taxRate = item.taxRate,
                isReducedTax = item.taxRate == TAX_RATE_REDUCED
            )
        }

        return ReceiptData(
            id = receipt.id,
            receiptNumber = receipt.receiptNumber,
            issuedAt = receipt.issuedAt,
            storeName = store?.name ?: "Restaurant",
            storeAddress = fullAddress,
            storePhone = store?.phone,
            invoiceRegistrationNumber = receipt.invoiceRegistrationNumber,
            items = lineItems,
            subtotal = receipt.subtotal,
            taxBreakdown = taxBreakdown,
            totalAmount = receipt.totalAmount,
            paymentMethod = bill?.paymentMethod,
            taxIncluded = taxIncluded
        )
    }

    private fun parseTaxBreakdown(json: String?): TaxBreakdown {
        if (json.isNullOrBlank()) {
            return TaxBreakdown(0, 0, 0, 0)
        }
        return try {
            // Simple JSON parsing without external library
            val subtotal8 = Regex(""""subtotal8":(\d+)""").find(json)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val tax8 = Regex(""""tax8":(\d+)""").find(json)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val subtotal10 = Regex(""""subtotal10":(\d+)""").find(json)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val tax10 = Regex(""""tax10":(\d+)""").find(json)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            TaxBreakdown(subtotal8, tax8, subtotal10, tax10)
        } catch (e: Exception) {
            TaxBreakdown(0, 0, 0, 0)
        }
    }
}
