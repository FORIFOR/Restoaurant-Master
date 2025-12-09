package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.restomaster.data.local.entity.CallRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallRequestDao {
    @Query("SELECT * FROM call_requests WHERE storeId = :storeId AND status = 'new' ORDER BY createdAt ASC")
    fun getActiveCallRequests(storeId: Long): Flow<List<CallRequestEntity>>

    @Query("SELECT * FROM call_requests WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getAllCallRequests(storeId: Long): Flow<List<CallRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(callRequest: CallRequestEntity): Long

    @Update
    suspend fun update(callRequest: CallRequestEntity)
    
    @Query("UPDATE call_requests SET status = 'handled', handledAt = :handledAt WHERE id = :id")
    suspend fun markAsHandled(id: Long, handledAt: Long)

    @Query("DELETE FROM call_requests")
    suspend fun deleteAll()
}
