package com.example.restomaster.ui.table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.entity.TableEntity
import com.example.restomaster.domain.model.QrCodeSize
import com.example.restomaster.domain.repository.TableRepository
import com.example.restomaster.domain.util.PdfGenerator
import com.example.restomaster.domain.util.QrCodeGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

import com.example.restomaster.domain.repository.OrderRepository
import kotlinx.coroutines.flow.combine

@HiltViewModel
class TableListViewModel @Inject constructor(
    private val tableRepository: TableRepository,
    private val orderRepository: OrderRepository,
    private val qrCodeGenerator: QrCodeGenerator,
    private val pdfGenerator: PdfGenerator
) : ViewModel() {

    data class UiState(
        val isAddDialogVisible: Boolean = false,
        val newTableNumberInput: String = "",
        val isSaving: Boolean = false,
        val errorMessage: String? = null,
        val qrDialogTargetTable: TableEntity? = null,
        val qrSelectedSize: QrCodeSize = QrCodeSize.MEDIUM,
        val isQrGenerating: Boolean = false,
        val lastGeneratedQrUri: android.net.Uri? = null
    )

    private val storeId = 1L

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _qrPreviewState = MutableStateFlow<QrPreviewState?>(null)
    val qrPreviewState: StateFlow<QrPreviewState?> = _qrPreviewState

    // Combine tables and active orders to show status
    val tableListUiState: StateFlow<TableListUiState> = combine(
        tableRepository.getTables(storeId),
        orderRepository.getActiveOrders(storeId)
    ) { tables, orders ->
        val tableStatuses = tables.map { table ->
            val activeOrder = orders.find { it.tableId == table.id && it.status != "closed" }
            TableStatusItem(
                table = table,
                activeOrder = activeOrder,
                totalAmount = 0L // Placeholder until we fetch items
            )
        }
        TableListUiState(items = tableStatuses)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TableListUiState()
    )


    // Table Addition Dialog
    fun onClickAddTable() {
        _uiState.update {
            it.copy(
                isAddDialogVisible = true,
                newTableNumberInput = "",
                errorMessage = null
            )
        }
    }

    fun onDismissAddDialog() {
        _uiState.update { it.copy(isAddDialogVisible = false, errorMessage = null) }
    }

    fun onChangeTableNumberInput(value: String) {
        // Filter to digits only
        _uiState.update { it.copy(newTableNumberInput = value.filter { c -> c.isDigit() }) }
    }

    fun onConfirmAddTable() {
        val current = _uiState.value
        val number = current.newTableNumberInput.toIntOrNull()
        if (number == null || number <= 0) {
            _uiState.update { it.copy(errorMessage = "テーブル番号を正しく入力してください") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSaving = true, errorMessage = null) }

                // Generate random token for URL
                val token = UUID.randomUUID().toString()

                tableRepository.createTable(
                    storeId = storeId,
                    number = number,
                    token = token
                )

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isAddDialogVisible = false,
                        newTableNumberInput = "",
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "テーブルの作成に失敗しました: ${e.message}"
                    )
                }
            }
        }
    }

    // QR Generation (Legacy - keeping for backward compatibility)
    fun addTable(tableNumber: Int) {
        viewModelScope.launch {
            val token = UUID.randomUUID().toString()
            val table = TableEntity(
                storeId = storeId,
                tableNumber = tableNumber,
                token = token
            )
            tableRepository.addTable(table)
        }
    }

    fun generateQr(table: TableEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val ip = com.example.restomaster.util.NetworkUtils.getIpAddress() ?: "192.168.43.1"
            val port = 8080 // Default port
            val content = "http://$ip:$port/t/${table.token}"
            val bitmap = qrCodeGenerator.generate(content)
            val file = pdfGenerator.createQrPdf(table.tableNumber, bitmap)
            
            withContext(Dispatchers.Main) {
                if (file != null) {
                    _qrPreviewState.value = QrPreviewState(bitmap, file)
                }
            }
        }
    }

    fun clearQrPreview() {
        _qrPreviewState.value = null
    }
}

data class QrPreviewState(
    val bitmap: android.graphics.Bitmap,
    val pdfFile: java.io.File
)

data class TableListUiState(
    val items: List<TableStatusItem> = emptyList()
)

data class TableStatusItem(
    val table: TableEntity,
    val activeOrder: com.example.restomaster.data.local.entity.OrderEntity? = null,
    val totalAmount: Long = 0
)
