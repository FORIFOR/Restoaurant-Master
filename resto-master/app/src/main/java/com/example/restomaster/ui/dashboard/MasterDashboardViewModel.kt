package com.example.restomaster.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.repository.MasterDashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.restomaster.data.local.dao.OrderWithItems
import com.example.restomaster.notification.OrderNotifier
import android.util.Log

import com.example.restomaster.domain.repository.OrderRepository
import com.example.restomaster.domain.model.TableStatusUi
import com.example.restomaster.domain.model.DashboardSummary

@HiltViewModel
class MasterDashboardViewModel @Inject constructor(
    private val repository: MasterDashboardRepository,
    private val orderRepository: OrderRepository,
    private val orderNotifier: OrderNotifier
) : ViewModel() {

    private val _filter = MutableStateFlow(TableFilter.ALL)
    private var lastSeenOrderId: Long = 0L
    private val _orderRows = MutableStateFlow<List<OrderMonitorRow>>(emptyList())
    val orderRows: StateFlow<List<OrderMonitorRow>> = _orderRows
    private val notifiedCheckoutOrders = mutableSetOf<Long>()
    private val notifiedCallRequests = mutableSetOf<Long>()
    
    // Store ID is hardcoded to 1 for now
    private val _dashboardData = repository.getDashboardData(1L)

    val uiState: StateFlow<MasterDashboardUiState> = combine(
        _dashboardData,
        _filter
    ) { data, filter ->
        val tableStatuses = data.map { status ->
            val orders = status.orders
            val latestOrder = orders.maxByOrNull { it.order.createdAt }
            val allItems = orders.flatMap { it.items }
            val totalAmount = allItems.sumOf { it.item.unitPrice * it.item.quantity }
            val servedAmount = allItems.filter { it.item.status == "served" }
                .sumOf { it.item.unitPrice * it.item.quantity }
            val unservedItems = allItems.filter { it.item.status != "served" }
            val totalItemCount = allItems.sumOf { it.item.quantity }
            val callRequests = status.callRequests
            
            // Handle Order-based checkout requests
            orders.filter { it.order.status == "checkout_requested" }
                .forEach { orderWithItems ->
                    if (!notifiedCheckoutOrders.contains(orderWithItems.order.id)) {
                        val amount = orderWithItems.items.sumOf { it.item.unitPrice * it.item.quantity }
                        orderNotifier.notifyCheckout(status.table.tableNumber, amount)
                        notifiedCheckoutOrders.add(orderWithItems.order.id)
                        Log.i("MONITOR_VM", "Checkout request detected orderId=${orderWithItems.order.id}")
                    }
                }
            
            // Handle CallRequest-based requests (call_staff, checkout)
            callRequests.filter { it.status == "new" }.forEach { req ->
                if (!notifiedCallRequests.contains(req.id)) {
                    when (req.type) {
                        "call_staff" -> {
                            orderNotifier.notifyCall(status.table.tableNumber)
                            Log.i("MONITOR_VM", "Staff call detected table=${status.table.tableNumber}")
                        }
                        "checkout" -> {
                            // Only notify if not already notified via order status (though they might be separate)
                            // For safety, we notify here as well. OrderNotifier handles duplicate sounds reasonably well.
                            orderNotifier.notifyCheckout(status.table.tableNumber, totalAmount)
                            Log.i("MONITOR_VM", "Checkout call detected table=${status.table.tableNumber}")
                        }
                    }
                    notifiedCallRequests.add(req.id)
                }
            }
            
            // Calculate metrics
            // アクティブ注文＝未提供のアイテムがある注文
            val hasActiveOrder = orders.any { order -> order.items.any { it.item.status != "served" } }
            val hasCallRequest = callRequests.any { it.type == "call_staff" && it.status == "new" }
            val hasCheckoutRequest = callRequests.any { it.type == "checkout" && it.status == "new" } ||
                                     orders.any { it.order.status == "checkout_requested" }
            val stayMinutes = latestOrder?.let { 
                ((System.currentTimeMillis() - it.order.createdAt) / 60000).toInt() 
            }
            val lastOrderAt = orders.maxOfOrNull { it.order.createdAt }
            
            TableStatusUi(
                tableId = status.table.id,
                tableNumber = status.table.tableNumber,
                isOccupied = hasActiveOrder,
                hasActiveOrder = hasActiveOrder,
                hasUnservedItems = unservedItems.isNotEmpty(),
                hasCallRequest = hasCallRequest,
                hasCheckoutRequest = hasCheckoutRequest,
                activeOrderCount = totalItemCount,
                unservedItemCount = unservedItems.sumOf { it.item.quantity },
                totalAmount = totalAmount,
                servedAmount = servedAmount,
                guestCount = latestOrder?.order?.peopleCount,
                stayMinutes = stayMinutes,
                lastOrderAt = lastOrderAt,
                callRequests = callRequests
            )
        }

        val filteredTables = when (filter) {
            TableFilter.ALL -> tableStatuses
            TableFilter.ACTIVE_ORDERS -> tableStatuses.filter { it.hasActiveOrder }
            TableFilter.CHECKOUT -> tableStatuses.filter { it.hasCheckoutRequest }
            TableFilter.CALL -> tableStatuses.filter { it.hasCallRequest }
            TableFilter.EMPTY -> tableStatuses.filter { !it.isOccupied }
            TableFilter.OCCUPIED -> tableStatuses.filter { it.isOccupied }
        }.sortedWith(compareByDescending<TableStatusUi> { it.hasCallRequest }
            .thenByDescending { it.hasCheckoutRequest }
            .thenBy { it.tableNumber })

        val summary = DashboardSummary(
            totalTables = tableStatuses.size,
            occupiedTables = tableStatuses.count { it.isOccupied },
            activeOrders = tableStatuses.count { it.hasActiveOrder },
            newOrders = tableStatuses.count { it.hasActiveOrder && it.lastOrderAt != null },
            callRequests = tableStatuses.count { it.hasCallRequest },
            checkoutRequests = tableStatuses.count { it.hasCheckoutRequest },
            checkoutTotalAmount = tableStatuses
                .filter { it.hasCheckoutRequest }
                .sumOf { it.totalAmount }
        )

        // 新規注文検知（最大orderIdを比較）
        val maxId = data.flatMap { it.orders }.maxOfOrNull { it.order.id } ?: 0L
        if (maxId > lastSeenOrderId) {
            val newOrders = data.flatMap { it.orders }
                .filter { it.order.id > lastSeenOrderId }
                .map { it.toRow() }
            if (newOrders.isNotEmpty()) {
                orderNotifier.notifyNewOrder(newOrders.first())
                Log.i("MONITOR_VM", "New orders detected: ${newOrders.size}")
            }
            lastSeenOrderId = maxId
        }

        // 注文一覧（新しい順）をUIに保持
        _orderRows.value = data
            .flatMap { it.orders }
            .filter { order ->
                order.order.status != "closed" &&
                        order.items.any { it.item.status != "served" }
            }
            .sortedByDescending { it.order.createdAt }
            .map { it.toRow() }
        Log.i("MONITOR_VM", "rows updated size=${_orderRows.value.size}")

        MasterDashboardUiState(
            tables = filteredTables,
            filter = filter,
            summary = summary
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MasterDashboardUiState()
    )

    fun setFilter(filter: TableFilter) {
        _filter.value = filter
    }
    
    fun resolveCallRequest(requestId: Long) {
        viewModelScope.launch {
            val request = orderRepository.getCallRequestById(requestId)
            if (request != null) {
                orderRepository.updateCallRequest(request.copy(status = "handled", handledAt = System.currentTimeMillis()))
            }
        }
    }
    
    fun resolveCheckoutRequest(tableId: Long) {
        viewModelScope.launch {
            // Resolve CallRequest-based checkout
            val requests = orderRepository.getActiveCallRequests(1L).first()
            requests.filter { it.tableId == tableId && it.type == "checkout" }
                .forEach { req ->
                    orderRepository.updateCallRequest(req.copy(status = "handled", handledAt = System.currentTimeMillis()))
                }
            
            // Resolve Order-based checkout (optional)
        }
    }

    fun markOrderServed(orderId: Long) {
        viewModelScope.launch {
            runCatching { repository.markOrderServed(orderId) }
                .onFailure { Log.e("MONITOR_VM", "Failed to mark served orderId=$orderId", it) }
        }
    }
}

private fun OrderWithItems.toRow(): OrderMonitorRow {
    return OrderMonitorRow(
        orderId = this.order.id,
        tableNumber = this.table.tableNumber,
        itemsSummary = this.items.map { "${it.menuItem?.name ?: "Unknown"} x${it.item.quantity}" },
        totalAmount = this.items.sumOf { it.item.unitPrice * it.item.quantity },
        createdAt = this.order.createdAt,
        status = this.order.status
    )
}

data class MasterDashboardUiState(
    val tables: List<TableStatusUi> = emptyList(),
    val filter: TableFilter = TableFilter.ALL,
    val summary: DashboardSummary = DashboardSummary(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)



enum class TableFilter {
    ALL, ACTIVE_ORDERS, CHECKOUT, CALL, EMPTY, OCCUPIED
}
