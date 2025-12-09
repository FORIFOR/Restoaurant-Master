package com.example.restomaster.ui.table.detail

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.dao.CallRequestDao
import com.example.restomaster.data.local.dao.OrderDao
import com.example.restomaster.data.local.dao.TableDao
import com.example.restomaster.data.local.entity.CallRequestEntity
import com.example.restomaster.data.local.entity.OrderEntity
import com.example.restomaster.data.local.entity.TableEntity
import com.example.restomaster.domain.repository.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TableDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tableDao: TableDao,
    private val orderDao: OrderDao,
    private val callRequestDao: CallRequestDao,
    private val tableRepository: TableRepository,
    private val menuDao: com.example.restomaster.data.local.dao.MenuDao,
    private val billingRepository: com.example.restomaster.data.repository.BillingRepository
) : ViewModel() {

    private val tableId: Long = checkNotNull(savedStateHandle["tableId"])

    private val _qrPreviewState = MutableStateFlow<QrPreviewState?>(null)
    val qrPreviewState: StateFlow<QrPreviewState?> = _qrPreviewState

    private val orderItemsFlow = MutableStateFlow<List<OrderItemWithMenu>>(emptyList())
    private val orderHistoryFlow = MutableStateFlow<List<OrderEntity>>(emptyList())

    // Navigation event for receipt preview
    private val _navigateToReceipt = MutableStateFlow<Long?>(null)
    val navigateToReceipt: StateFlow<Long?> = _navigateToReceipt.asStateFlow()

    val uiState: StateFlow<TableDetailUiState> = combine(
        tableDao.getTableById(tableId),
        orderDao.getActiveOrders(1L), // TODO: Store ID
        callRequestDao.getActiveCallRequests(1L),
        orderItemsFlow,
        orderHistoryFlow
    ) { table, orders, calls, orderItems, orderHistory ->
        if (table == null) {
            TableDetailUiState(isLoading = false, errorMessage = "Table not found")
        } else {
            val activeOrder = orders.find { it.tableId == table.id }
            val callRequests = calls.filter { it.tableId == table.id }
            
            // Calculate stay time
            val stayMinutes = if (activeOrder != null) {
                ((System.currentTimeMillis() - activeOrder.createdAt) / 60000).toInt()
            } else null
            
            TableDetailUiState(
                table = table,
                activeOrder = activeOrder,
                orderItems = orderItems,
                orderHistory = orderHistory,
                callRequests = callRequests,
                stayMinutes = stayMinutes,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TableDetailUiState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            uiState.collect { state ->
                // Fetch order items when active order changes
                state.activeOrder?.let { order ->
                    fetchOrderItems(order.id)
                }
                // Fetch order history for this table
                state.table?.let { table ->
                    fetchOrderHistory(table.id)
                }
            }
        }
    }

    private suspend fun fetchOrderItems(orderId: Long) {
        orderDao.getOrderItems(orderId).collect { items ->
            val itemsWithMenu = items.map { item ->
                val menuItem = menuDao.getMenuItem(item.menuItemId)
                OrderItemWithMenu(
                    orderItem = item,
                    menuName = menuItem?.name ?: "Unknown",
                    menuPrice = menuItem?.price?.toLong() ?: 0L
                )
            }
            orderItemsFlow.value = itemsWithMenu
        }
    }

    private suspend fun fetchOrderHistory(tableId: Long) {
        // Get all orders for this table
        orderDao.getOrdersForTable(tableId).collect { orders ->
            orderHistoryFlow.value = orders
        }
    }

    fun onGenerateQr() {
        val table = uiState.value.table ?: return
        viewModelScope.launch {
            try {
                // Reuse the logic from TableListViewModel via Repository or duplicate for now
                // Since logic is in ViewModel in TableList, we should ideally move it to a UseCase or Repository
                // For speed, we'll use the Repository method if available, or duplicate the PDF generation call
                
                val pdfFile = tableRepository.generateQrPdf(table, com.example.restomaster.domain.model.QrCodeSize.MEDIUM)
                // We need to render the first page of PDF to Bitmap for preview, 
                // OR just show the PDF directly. 
                // For consistency with TableListScreen, let's assume we can get a bitmap.
                // Actually TableListViewModel does PDF generation AND Bitmap generation.
                // Let's just generate the PDF and let the UI share it.
                
                // For preview, we might need a Bitmap. 
                // Let's simplify and just allow sharing the PDF.
                
                _qrPreviewState.value = QrPreviewState(pdfFile)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun onDismissQrPreview() {
        _qrPreviewState.value = null
    }

    fun onResolveCall(requestId: Long) {
        viewModelScope.launch {
            callRequestDao.markAsHandled(requestId, System.currentTimeMillis())
        }
    }
    
    fun onMarkItemServed(orderItemId: Long) {
        viewModelScope.launch {
            orderDao.updateOrderItemStatus(orderItemId, "served")
        }
    }

    fun onCheckoutDone() {
        val order = uiState.value.activeOrder ?: return
        viewModelScope.launch {
            // 会計処理: 伝票作成→明細スナップショット→支払い完了→注文を閉じる
            val billId = runCatching { billingRepository.processManualCheckout(order.tableId) }.getOrDefault(-1L)
            if (billId != -1L) {
                _navigateToReceipt.value = billId
            }

            // Mark checkout request as handled if it exists
            val checkoutRequest = uiState.value.callRequests.find { it.type == "checkout" }
            if (checkoutRequest != null) {
                callRequestDao.markAsHandled(checkoutRequest.id, System.currentTimeMillis())
            }
        }
    }

    fun onNavigatedReceipt() {
        _navigateToReceipt.value = null
    }
}

data class TableDetailUiState(
    val table: TableEntity? = null,
    val activeOrder: OrderEntity? = null,
    val orderItems: List<OrderItemWithMenu> = emptyList(),
    val orderHistory: List<OrderEntity> = emptyList(),
    val callRequests: List<CallRequestEntity> = emptyList(),
    val stayMinutes: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class OrderItemWithMenu(
    val orderItem: com.example.restomaster.data.local.entity.OrderItemEntity,
    val menuName: String,
    val menuPrice: Long
)

data class QrPreviewState(
    val pdfFile: File
)
