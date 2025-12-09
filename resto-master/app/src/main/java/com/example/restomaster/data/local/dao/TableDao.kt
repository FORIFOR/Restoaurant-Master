package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.restomaster.data.local.entity.TableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDao {

    @Query("SELECT * FROM tables WHERE storeId = :storeId ORDER BY tableNumber ASC")
    fun getTablesFlow(storeId: Long): Flow<List<TableEntity>>
    
    @Query("SELECT * FROM tables WHERE token = :token LIMIT 1")
    suspend fun findByToken(token: String): TableEntity?

    @Query("SELECT * FROM tables WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): TableEntity?

    @Query("SELECT * FROM tables WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<TableEntity?>

    @Query("SELECT * FROM tables WHERE id = :id LIMIT 1")
    fun getTableById(id: Long): Flow<TableEntity?>

    @Query("SELECT * FROM tables")
    suspend fun getAllTables(): List<TableEntity>


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(table: TableEntity): Long

    @Update
    suspend fun update(table: TableEntity)

    @Delete
    suspend fun delete(table: TableEntity)
}
