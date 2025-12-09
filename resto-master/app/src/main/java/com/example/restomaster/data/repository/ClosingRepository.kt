package com.example.restomaster.data.repository

import com.example.restomaster.data.local.dao.ClosingDao
import com.example.restomaster.domain.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClosingRepository @Inject constructor(
    private val closingDao: ClosingDao
) {
    /**
     * Get comprehensive daily closing summary
     */
    suspend fun getDailyClosingSummary(from: Long, to: Long): DailyClosingSummary {
        val salesTotal = closingDao.getSalesTotal(from, to)
        val paymentsTotal = closingDao.getPaymentsTotal(from, to)
        val billCount = closingDao.getPaidBillCount(from, to)
        
        val paymentBreakdown = closingDao.getPaymentsByMethod(from, to).map {
            PaymentMethodTotal(method = it.provider, total = it.total)
        }
        
        val unpaidBills = closingDao.getUnpaidBills(from, to).map {
            UnpaidBill(
                billId = it.id,
                tableId = it.table_id,
                totalAmount = it.total_amount,
                closedAt = it.closed_at,
                paymentMethod = it.payment_method
            )
        }
        
        val abnormalPayments = closingDao.getAbnormalPayments(from, to).map {
            AbnormalPayment(
                paymentId = it.id,
                billId = it.bill_id,
                provider = it.provider,
                amount = it.amount,
                status = it.status,
                createdAt = it.created_at
            )
        }
        
        return DailyClosingSummary(
            salesTotal = salesTotal,
            paymentsTotal = paymentsTotal,
            diff = salesTotal - paymentsTotal,
            billCount = billCount,
            paymentBreakdown = paymentBreakdown,
            unpaidBills = unpaidBills,
            abnormalPayments = abnormalPayments
        )
    }
}
