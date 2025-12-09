package com.example.restomaster.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val address: String?,
    val phone: String?,
    // Invoice-related fields
    @ColumnInfo(name = "invoice_registration_number") val invoiceRegistrationNumber: String? = null,
    @ColumnInfo(name = "postal_code") val postalCode: String? = null,
    @ColumnInfo(name = "address_line1") val addressLine1: String? = null,
    @ColumnInfo(name = "address_line2") val addressLine2: String? = null,
    @ColumnInfo(name = "tax_included_pricing") val taxIncludedPricing: Boolean = true,
    // Customer web app customization
    @ColumnInfo(name = "splash_image_path") val splashImagePath: String? = null,
    @ColumnInfo(name = "theme_color") val themeColor: String? = null
)
