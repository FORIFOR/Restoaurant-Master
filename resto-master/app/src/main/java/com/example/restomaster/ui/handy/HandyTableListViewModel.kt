package com.example.restomaster.ui.handy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.domain.repository.TableRepository
import com.example.restomaster.domain.model.TableStatusUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HandyTableListViewModel @Inject constructor(
    tableRepository: TableRepository,
    orderRepository: com.example.restomaster.domain.repository.OrderRepository
) : ViewModel() {

    val tables: StateFlow<List<TableStatusUi>> = kotlinx.coroutines.flow.combine(
        tableRepository.getTables(1L),
        orderRepository.getActiveOrders(1L)
    ) { tables, orders ->
        tables.map { table ->
            val hasActiveOrder = orders.any { it.tableId == table.id }
            TableStatusUi(
                tableId = table.id,
                tableNumber = table.tableNumber,
                isOccupied = hasActiveOrder,
                hasActiveOrder = hasActiveOrder,
                hasCallRequest = false,
                hasCheckoutRequest = false,
                totalAmount = 0,
                activeOrderCount = 0,
                callRequests = emptyList()
            )
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
