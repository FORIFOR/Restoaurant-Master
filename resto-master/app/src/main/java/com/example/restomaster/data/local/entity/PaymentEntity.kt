package com.example.restomaster.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payments",
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
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "bill_id") val billId: Long,
    val provider: String,       // STRIPE / CASH / PAYPAY / LINEPAY
    @ColumnInfo(name = "provider_type") val providerType: String, // card / wallet / cash
    val amount: Int,
    val currency: String,       // "jpy"
    val status: String,         // PENDING / SUCCEEDED / FAILED / CANCELED
    @ColumnInfo(name = "provider_ref_id") val providerRefId: String? = null, // Stripe: checkout_session_id
    @ColumnInfo(name = "merchant_payment_id") val merchantPaymentId: String? = null, // PayPay: merchant-generated ID
    @ColumnInfo(name = "paypay_code_id") val paypayCodeId: String? = null, // PayPay: QR code ID
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "confirmed_at") val confirmedAt: Long? = null,
    @ColumnInfo(name = "extra_json") val extraJson: String? = null
)
