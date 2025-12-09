package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.restomaster.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments WHERE bill_id = :billId")
    suspend fun getPaymentsForBill(billId: Long): List<PaymentEntity>

    @Query("SELECT * FROM payments WHERE bill_id = :billId")
    fun getPaymentsForBillFlow(billId: Long): Flow<List<PaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Update
    suspend fun updatePayment(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE confirmed_at BETWEEN :from AND :to AND status = 'SUCCEEDED'")
    suspend fun getSuccessfulPaymentsInRange(from: Long, to: Long): List<PaymentEntity>

    @Query("SELECT * FROM payments WHERE provider_ref_id = :refId LIMIT 1")
    suspend fun getByProviderRefId(refId: String): PaymentEntity?

    @Query("SELECT * FROM payments WHERE id = :paymentId LIMIT 1")
    suspend fun getPaymentById(paymentId: Long): PaymentEntity?

    @Query("SELECT * FROM payments")
    suspend fun getAllPayments(): List<PaymentEntity>

    @Query("DELETE FROM payments")
    suspend fun deleteAll()
}
