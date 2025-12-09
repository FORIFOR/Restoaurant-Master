package com.example.restomaster.domain.repository

import com.example.restomaster.data.local.entity.TableEntity
import kotlinx.coroutines.flow.Flow

interface TableRepository {
    fun getTables(storeId: Long): Flow<List<TableEntity>>
    suspend fun getTableByToken(token: String): TableEntity?
    suspend fun getTableById(id: Long): TableEntity?
    suspend fun addTable(table: TableEntity)
    suspend fun updateTable(table: TableEntity)
    suspend fun createTable(storeId: Long, number: Int, token: String)
    suspend fun generateQrPdf(table: TableEntity, size: com.example.restomaster.domain.model.QrCodeSize): java.io.File
}
