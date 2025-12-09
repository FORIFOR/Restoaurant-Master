package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restomaster.data.local.entity.ReceiptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {
    @Query("SELECT * FROM receipts WHERE billId = :billId LIMIT 1")
    suspend fun getReceiptForBill(billId: Long): ReceiptEntity?

    @Query("SELECT * FROM receipts WHERE id = :id")
    suspend fun getReceiptById(id: Long): ReceiptEntity?

    @Query("SELECT * FROM receipts ORDER BY issuedAt DESC")
    fun getAllReceipts(): Flow<List<ReceiptEntity>>
    
    @Query("SELECT MAX(CAST(SUBSTR(receiptNumber, -3) AS INTEGER)) FROM receipts WHERE receiptNumber LIKE :datePrefix || '%'")
    suspend fun getMaxReceiptNumberForDate(datePrefix: String): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: ReceiptEntity): Long
}
