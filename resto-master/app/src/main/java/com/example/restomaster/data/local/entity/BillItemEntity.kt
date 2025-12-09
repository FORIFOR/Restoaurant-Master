package com.example.restomaster.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bill_items",
    foreignKeys = [
        ForeignKey(
            entity = BillEntity::class,
            parentColumns = ["id"],
            childColumns = ["bill_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bill_id")]
)
data class BillItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "bill_id") val billId: Long,
    @ColumnInfo(name = "order_id") val orderId: Long,         // 元の注文（トレース用）
    @ColumnInfo(name = "order_item_id") val orderItemId: Long, // 元の注文行（トレース用）
    @ColumnInfo(name = "menu_item_id") val menuItemId: Long,   // 当時のメニューID
    val name: String,                                            // 会計時点の名称
    @ColumnInfo(name = "unit_price") val unitPrice: Int,        // 会計時点の単価
    val quantity: Int,
    @ColumnInfo(name = "line_total") val lineTotal: Int,        // unit_price * quantity
    @ColumnInfo(name = "tax_rate") val taxRate: Float,          // 0.1 or 0.08
    @ColumnInfo(name = "tax_amount") val taxAmount: Int = 0,    // この行の税額
    @ColumnInfo(name = "is_paid") val isPaid: Boolean = false   // 個別会計用
)
