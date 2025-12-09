package com.example.restomaster.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payment_items",
    foreignKeys = [
        ForeignKey(
            entity = PaymentEntity::class,
            parentColumns = ["id"],
            childColumns = ["payment_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BillItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["bill_item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("payment_id"), Index("bill_item_id")]
)
data class PaymentItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "payment_id") val paymentId: Long,
    @ColumnInfo(name = "bill_item_id") val billItemId: Long,
    val amount: Int  // 部分支払いにも対応
)
