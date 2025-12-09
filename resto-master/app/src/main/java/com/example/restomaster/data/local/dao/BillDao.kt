package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.restomaster.data.local.entity.BillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Query("SELECT * FROM bills WHERE table_id = :tableId AND status = 'OPEN' LIMIT 1")
    suspend fun getOpenBillForTable(tableId: Long): BillEntity?

    @Query("SELECT * FROM bills WHERE id = :billId")
    suspend fun getBill(billId: Long): BillEntity?

    @Query("SELECT * FROM bills WHERE id = :billId")
    fun getBillFlow(billId: Long): Flow<BillEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillEntity): Long

    @Update
    suspend fun updateBill(bill: BillEntity)

    @Query("SELECT * FROM bills WHERE closed_at BETWEEN :from AND :to AND status = 'PAID'")
    suspend fun getClosedBillsInRange(from: Long, to: Long): List<BillEntity>

    // ==================== Analytics Queries ====================

    @Query("""
        SELECT 
            COALESCE(SUM(total_amount), 0) as total,
            COALESCE(AVG(total_amount), 0) as avg_check,
            COALESCE(SUM(people_count), 0) as total_customers,
            COUNT(*) as total_bills
        FROM bills
        WHERE status = 'PAID' AND closed_at BETWEEN :from AND :to
    """)
    suspend fun getKPIsRaw(from: Long, to: Long): KPIsRaw

    @Query("""
        SELECT 
            CAST(strftime('%H', closed_at / 1000, 'unixepoch', 'localtime') AS INTEGER) as hour,
            COUNT(*) as order_count,
            SUM(total_amount) as total_sales
        FROM bills
        WHERE status = 'PAID' AND closed_at BETWEEN :from AND :to
        GROUP BY hour
        ORDER BY hour
    """)
    suspend fun getHourlySalesRaw(from: Long, to: Long): List<HourlySalesRaw>

    @Query("""
        SELECT 
            CAST(strftime('%w', closed_at / 1000, 'unixepoch', 'localtime') AS INTEGER) as day_of_week,
            COUNT(*) as order_count,
            SUM(total_amount) as total_sales
        FROM bills
        WHERE status = 'PAID' AND closed_at BETWEEN :from AND :to
        GROUP BY day_of_week
        ORDER BY day_of_week
    """)
    suspend fun getDailySalesRaw(from: Long, to: Long): List<DailySalesRaw>

    @Query("""
        SELECT 
            strftime('%Y-%m-%d', closed_at / 1000, 'unixepoch', 'localtime') as date,
            COUNT(*) as order_count,
            SUM(total_amount) as total_sales
        FROM bills
        WHERE status = 'PAID' AND closed_at BETWEEN :from AND :to
        GROUP BY date
        ORDER BY date
    """)
    suspend fun getSalesByDateRaw(from: Long, to: Long): List<DateSalesRaw>

    @Query("""
        SELECT 
            strftime('%Y-%m', closed_at / 1000, 'unixepoch', 'localtime') as year_month,
            COUNT(*) as order_count,
            SUM(total_amount) as total_sales
        FROM bills
        WHERE status = 'PAID' AND closed_at BETWEEN :from AND :to
        GROUP BY year_month
        ORDER BY year_month
    """)
    suspend fun getMonthlySalesRaw(from: Long, to: Long): List<MonthlySalesRaw>

    @Query("""
        SELECT COUNT(DISTINCT table_id) 
        FROM bills 
        WHERE status = 'PAID' 
          AND closed_at >= :startOfDay 
          AND closed_at < :endOfDay
    """)
    suspend fun getTableUsesForDay(startOfDay: Long, endOfDay: Long): Int

    @Query("""
        SELECT COUNT(*) 
        FROM bills 
        WHERE status = 'PAID' 
          AND closed_at BETWEEN :from AND :to
    """)
    suspend fun getPaidBillCount(from: Long, to: Long): Int

    @Query("DELETE FROM bills")
    suspend fun deleteAll()

    @Query("DELETE FROM bills WHERE status = 'PAID'")
    suspend fun deletePaidBills()
}

// Raw data classes for Room queries
data class KPIsRaw(
    val total: Long,
    val avg_check: Int,
    val total_customers: Int,
    val total_bills: Int
)

data class HourlySalesRaw(
    val hour: Int,
    val order_count: Int,
    val total_sales: Int
)

data class DailySalesRaw(
    val day_of_week: Int,
    val order_count: Int,
    val total_sales: Int
)

data class DateSalesRaw(
    val date: String,
    val order_count: Int,
    val total_sales: Int
)

data class MonthlySalesRaw(
    val year_month: String,
    val order_count: Int,
    val total_sales: Int
)
