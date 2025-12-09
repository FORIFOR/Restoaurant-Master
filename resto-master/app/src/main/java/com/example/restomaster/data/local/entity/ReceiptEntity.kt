package com.example.restomaster.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val billId: Long,
    val receiptNumber: String,           // 連番 or 日付＋通番
    val issuedAt: Long,
    val invoiceRegistrationNumber: String,   // 店舗のT登録番号
    val taxIncluded: Boolean,
    val subtotal: Int,
    val taxAmount: Int,
    val totalAmount: Int,
    val taxBreakdownJson: String? = null     // 10%/8%などの内訳
)
