package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restomaster.data.local.entity.PaymentItemEntity

@Dao
interface PaymentItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(paymentItem: PaymentItemEntity): Long

    @Query("SELECT * FROM payment_items WHERE payment_id = :paymentId")
    suspend fun findByPaymentId(paymentId: Long): List<PaymentItemEntity>

    @Query("SELECT * FROM payment_items WHERE bill_item_id = :billItemId")
    suspend fun findByBillItemId(billItemId: Long): List<PaymentItemEntity>

    @Query("DELETE FROM payment_items")
    suspend fun deleteAll()
}
