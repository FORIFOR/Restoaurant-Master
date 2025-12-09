package com.example.restomaster.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bill_splits",
    foreignKeys = [
        ForeignKey(
            entity = BillEntity::class,
            parentColumns = ["id"],
            childColumns = ["bill_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PaymentEntity::class,
            parentColumns = ["id"],
            childColumns = ["payment_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("bill_id"), Index("payment_id")]
)
data class BillSplitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "bill_id") val billId: Long,
    val label: String? = null,                 // "Aさん", "Bさん" など
    val amount: Int,     // この人の支払額
    @ColumnInfo(name = "payment_id") val paymentId: Long? = null,              // 紐づけられた payment.id
    val status: String        // PENDING / PAID / CANCELLED
)
