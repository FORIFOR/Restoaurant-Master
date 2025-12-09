package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.restomaster.data.local.entity.BillItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(billItem: BillItemEntity): Long

    @Query("SELECT * FROM bill_items WHERE bill_id = :billId")
    suspend fun findByBillId(billId: Long): List<BillItemEntity>

    @Query("SELECT * FROM bill_items WHERE id IN (:ids)")
    suspend fun findByIds(ids: List<Long>): List<BillItemEntity>

    @Query("DELETE FROM bill_items WHERE bill_id = :billId")
    suspend fun deleteByBillId(billId: Long)

    @Query("UPDATE bill_items SET is_paid = :isPaid WHERE id = :billItemId")
    suspend fun updateIsPaid(billItemId: Long, isPaid: Boolean)

    @Update
    suspend fun update(billItem: BillItemEntity)

    // ==================== Analytics Queries ====================

    @Query("""
        SELECT 
            bi.menu_item_id as menuItemId,
            bi.name,
            SUM(bi.quantity) as quantitySold,
            SUM(bi.line_total) as totalRevenue
        FROM bill_items bi
        JOIN bills b ON bi.bill_id = b.id
        WHERE b.status = 'PAID' AND b.closed_at BETWEEN :from AND :to
        GROUP BY bi.menu_item_id, bi.name
        ORDER BY totalRevenue DESC
    """)
    suspend fun getMenuItemSalesRaw(from: Long, to: Long): List<MenuItemSalesRaw>

    @Query("DELETE FROM bill_items")
    suspend fun deleteAll()
}

data class MenuItemSalesRaw(
    val menuItemId: Long,
    val name: String,
    val quantitySold: Int,
    val totalRevenue: Int
)
