package com.example.restomaster.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "table_id") val tableId: Long,
    val status: String,         // OPEN / PAYMENT_PENDING / PAID / CANCELLED
    @ColumnInfo(name = "open_at") val openAt: Long,      // epoch millis
    @ColumnInfo(name = "closed_at") val closedAt: Long? = null,
    @ColumnInfo(name = "people_count") val peopleCount: Int? = null,  // 何名様か（客単価用）
    val subtotal: Int,
    val tax: Int,
    @ColumnInfo(name = "total_amount") val totalAmount: Int,
    @ColumnInfo(name = "total_tax_10") val totalTax10: Int = 0,
    @ColumnInfo(name = "total_tax_8") val totalTax8: Int = 0,
    @ColumnInfo(name = "paid_amount") val paidAmount: Int = 0,  // 既に支払済み合計（割り勘対応）
    @ColumnInfo(name = "payment_method") val paymentMethod: String? = null, // CASH / STRIPE / PAYPAY / LINEPAY / MIXED
    val note: String? = null
)
