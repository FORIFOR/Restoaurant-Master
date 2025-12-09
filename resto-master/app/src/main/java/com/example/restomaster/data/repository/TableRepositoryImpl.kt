package com.example.restomaster.data.repository

import com.example.restomaster.data.local.dao.TableDao
import com.example.restomaster.data.local.entity.TableEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

import com.example.restomaster.domain.repository.TableRepository as TableRepositoryInterface

import com.example.restomaster.domain.util.PdfGenerator
import com.example.restomaster.domain.util.QrCodeGenerator
import com.example.restomaster.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TableRepositoryImpl @Inject constructor(
    private val tableDao: TableDao,
    private val qrCodeGenerator: QrCodeGenerator,
    private val pdfGenerator: PdfGenerator
) : TableRepositoryInterface {
    override fun getTables(storeId: Long): Flow<List<TableEntity>> =
        tableDao.getTablesFlow(storeId)

    override suspend fun addTable(table: TableEntity) {
        tableDao.insert(table)
    }

    override suspend fun updateTable(table: TableEntity) = tableDao.update(table)
    
    override suspend fun getTableByToken(token: String): TableEntity? = tableDao.findByToken(token)

    override suspend fun getTableById(id: Long): TableEntity? = tableDao.findById(id)
    
    override suspend fun createTable(storeId: Long, number: Int, token: String) {
        val table = TableEntity(
            storeId = storeId,
            tableNumber = number,
            token = token
        )
        tableDao.insert(table)
    }

    override suspend fun generateQrPdf(table: TableEntity, size: com.example.restomaster.domain.model.QrCodeSize): java.io.File {
        return withContext(Dispatchers.IO) {
            val ip = NetworkUtils.getIpAddress() ?: "192.168.43.1"
            val port = 8080 // Default port
            val content = "http://$ip:$port/t/${table.token}"
            val bitmap = qrCodeGenerator.generate(content)
            // TODO: Pass size to PdfGenerator if supported, currently using default
            pdfGenerator.createQrPdf(table.tableNumber, bitmap) ?: throw IllegalStateException("Failed to generate PDF")
        }
    }
}
