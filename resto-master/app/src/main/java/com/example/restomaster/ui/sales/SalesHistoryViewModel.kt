package com.example.restomaster.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.dao.OrderWithItems
import com.example.restomaster.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SalesHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val storeId = 1L // Fixed for now

    val uiState: StateFlow<SalesHistoryUiState> = combine(
        orderRepository.getTotalSales(storeId),
        orderRepository.getClosedOrders(storeId)
    ) { totalSales, closedOrders ->
        SalesHistoryUiState(
            totalSales = totalSales ?: 0L,
            closedOrders = closedOrders,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SalesHistoryUiState(isLoading = true)
    )
}

data class SalesHistoryUiState(
    val totalSales: Long = 0,
    val closedOrders: List<OrderWithItems> = emptyList(),
    val isLoading: Boolean = false
)
