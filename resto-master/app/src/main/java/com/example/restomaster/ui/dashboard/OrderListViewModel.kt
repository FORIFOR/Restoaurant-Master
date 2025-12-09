package com.example.restomaster.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.dao.OrderWithItems
import com.example.restomaster.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val storeId = 1L

    val orderRows: StateFlow<List<OrderMonitorRow>> = orderRepository.getActiveOrdersWithItems(storeId)
        .map { orders ->
            orders
                .filter { order -> order.items.any { it.item.status != "served" } }
                .map { it.toRow() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun markOrderServed(orderId: Long) {
        viewModelScope.launch {
            // 注文明細を一括で「served」へ更新
            orderRepository.updateOrderItemsStatus(orderId, "served")
            // 注文ステータスも「served」へ更新（ダッシュボードのアクティブ注文から消すため）
            orderRepository.updateOrderStatus(orderId, "served")
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
}
