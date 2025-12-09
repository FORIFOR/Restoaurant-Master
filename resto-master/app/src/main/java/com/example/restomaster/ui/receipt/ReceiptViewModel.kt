package com.example.restomaster.ui.receipt

import android.content.Context
import android.content.Intent
import android.app.Activity
import android.net.Uri
import android.print.PrintManager
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.domain.model.ReceiptData
import com.example.restomaster.domain.usecase.GenerateReceiptUseCase
import com.example.restomaster.util.ReceiptPdfGenerator
import com.example.restomaster.util.ReceiptPrintAdapter
import com.example.restomaster.util.EscPosPrinter
import com.example.restomaster.util.LoggingEscPosPrinter
import com.example.restomaster.util.BluetoothEscPosPrinter
import com.example.restomaster.util.TcpEscPosPrinter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ReceiptUiState(
    val receiptData: ReceiptData? = null,
    val pdfFile: File? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val generateReceiptUseCase: GenerateReceiptUseCase,
    @ApplicationContext private val appContext: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val billId: Long = savedStateHandle.get<Long>("billId") ?: 0L

    private val _uiState = MutableStateFlow(ReceiptUiState())
    val uiState: StateFlow<ReceiptUiState> = _uiState.asStateFlow()

    private val pdfGenerator = ReceiptPdfGenerator(appContext)
    // デフォルトはロギング。必要に応じて Bluetooth / TCP へ差し替え。
    private fun resolveEscPosPrinter(): EscPosPrinter {
        // 簡易設定: 環境変数 or BuildConfig で指定があればそちらを使う
        val btMac = System.getenv("ESC_POS_BT") ?: ""
        val tcpHost = System.getenv("ESC_POS_TCP_HOST") ?: ""
        val tcpPort = System.getenv("ESC_POS_TCP_PORT")?.toIntOrNull() ?: 9100
        return when {
            btMac.isNotBlank() -> BluetoothEscPosPrinter(btMac)
            tcpHost.isNotBlank() -> TcpEscPosPrinter(tcpHost, tcpPort)
            else -> LoggingEscPosPrinter()
        }
    }
    private val escPosPrinter: EscPosPrinter = resolveEscPosPrinter()

    init {
        loadReceipt()
    }

    private fun loadReceipt() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val receiptData = generateReceiptUseCase(billId)
                if (receiptData != null) {
                    val pdfFile = pdfGenerator.generatePdf(receiptData)
                    _uiState.value = ReceiptUiState(
                        receiptData = receiptData,
                        pdfFile = pdfFile,
                        isLoading = false
                    )
                } else {
                    _uiState.value = ReceiptUiState(
                        isLoading = false,
                        error = "レシートの生成に失敗しました"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ReceiptUiState(
                    isLoading = false,
                    error = e.message ?: "エラーが発生しました"
                )
            }
        }
    }

    /**
     * Get URI for sharing the PDF file
     */
    fun getPdfUri(): Uri? {
        val file = _uiState.value.pdfFile ?: return null
        return FileProvider.getUriForFile(
            appContext,
            "${appContext.packageName}.fileprovider",
            file
        )
    }

    /**
     * Create share intent for the PDF
     */
    fun createShareIntent(): Intent? {
        val uri = getPdfUri() ?: return null
        return Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    /**
     * Create print intent for the PDF
     */
    fun createPrintIntent(): Intent? {
        val uri = getPdfUri() ?: return null
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    fun printWithSystemService(context: Context) {
        val pdf = _uiState.value.pdfFile ?: return
        val act = context as? Activity
        if (act == null) {
            _uiState.value = _uiState.value.copy(error = "印刷はアクティビティコンテキストから実行してください")
            return
        }
        val printManager = act.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val adapter = ReceiptPrintAdapter(pdf)
        printManager.print("RestoMaster Receipt", adapter, null)
    }

    /**
        * ESC/POS送出（簡易ログ版）。将来ここでBluetooth/LAN送出に差し替え。
        */
    fun printWithEscPos() {
        val receipt = _uiState.value.receiptData ?: return
        val escposBytes = com.example.restomaster.data.receipt.ReceiptGenerator().toEscPosBytes(
            com.example.restomaster.domain.model.ReceiptDto(
                storeName = receipt.storeName,
                storeAddress = receipt.storeAddress,
                storePhone = receipt.storePhone,
                invoiceRegistrationNumber = receipt.invoiceRegistrationNumber,
                receiptId = receipt.receiptNumber,
                tableNumber = null,
                transactionDate = java.time.Instant.ofEpochMilli(receipt.issuedAt)
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                items = receipt.items.map {
                    com.example.restomaster.domain.model.ReceiptItemDto(
                        name = it.name,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                        lineTotal = it.lineTotal,
                        isReducedTax = it.isReducedTax
                    )
                },
                subtotal = receipt.subtotal,
                taxSummaries = listOf(
                    com.example.restomaster.domain.model.TaxSummary(10, receipt.taxBreakdown.subtotal10, receipt.taxBreakdown.tax10),
                    com.example.restomaster.domain.model.TaxSummary(8, receipt.taxBreakdown.subtotal8, receipt.taxBreakdown.tax8)
                ).filter { it.taxableAmount > 0 },
                totalAmount = receipt.totalAmount,
                paymentMethod = receipt.paymentMethod ?: "Cash",
                receivedAmount = receipt.totalAmount,
                changeAmount = 0
            )
        )
        escPosPrinter.print(escposBytes)
    }

    fun retry() {
        loadReceipt()
    }
}
