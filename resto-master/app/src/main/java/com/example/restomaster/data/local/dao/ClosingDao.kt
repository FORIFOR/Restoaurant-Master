package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ClosingDao {

    /**
     * Get total sales from PAID bills in the date range
     */
    @Query("""
        SELECT COALESCE(SUM(total_amount), 0)
        FROM bills
        WHERE status = 'PAID' AND closed_at BETWEEN :from AND :to
    """)
    suspend fun getSalesTotal(from: Long, to: Long): Long

    /**
     * Get payment totals grouped by provider (CASH/STRIPE/PAYPAY)
     */
    @Query("""
        SELECT provider, SUM(amount) AS total
        FROM payments
        WHERE status = 'SUCCEEDED' AND confirmed_at BETWEEN :from AND :to
        GROUP BY provider
    """)
    suspend fun getPaymentsByMethod(from: Long, to: Long): List<PaymentMethodSummary>

    /**
     * Get total successful payments in the date range
     */
    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM payments
        WHERE status = 'SUCCEEDED' AND confirmed_at BETWEEN :from AND :to
    """)
    suspend fun getPaymentsTotal(from: Long, to: Long): Long

    /**
     * Get PAID bills that have no SUCCEEDED payment (potential unpaid)
     */
    @Query("""
        SELECT b.id, b.table_id, b.total_amount, b.closed_at, b.payment_method
        FROM bills b
        WHERE b.status = 'PAID' 
          AND b.closed_at BETWEEN :from AND :to
          AND b.id NOT IN (
            SELECT DISTINCT bill_id FROM payments WHERE status = 'SUCCEEDED'
          )
    """)
    suspend fun getUnpaidBills(from: Long, to: Long): List<UnpaidBillSummary>

    /**
     * Get payments with abnormal status (FAILED, CANCELED, PENDING)
     */
    @Query("""
        SELECT id, bill_id, provider, amount, status, created_at
        FROM payments
        WHERE created_at BETWEEN :from AND :to
          AND status != 'SUCCEEDED'
    """)
    suspend fun getAbnormalPayments(from: Long, to: Long): List<AbnormalPaymentSummary>

    /**
     * Get count of PAID bills in the date range
     */
    @Query("""
        SELECT COUNT(*)
        FROM bills
        WHERE status = 'PAID' AND closed_at BETWEEN :from AND :to
    """)
    suspend fun getPaidBillCount(from: Long, to: Long): Int
}

// Raw data classes for Room queries
data class PaymentMethodSummary(
    val provider: String,
    val total: Long
)

data class UnpaidBillSummary(
    val id: Long,
    val table_id: Long,
    val total_amount: Int,
    val closed_at: Long?,
    val payment_method: String?
)

data class AbnormalPaymentSummary(
    val id: Long,
    val bill_id: Long,
    val provider: String,
    val amount: Int,
    val status: String,
    val created_at: Long
)
