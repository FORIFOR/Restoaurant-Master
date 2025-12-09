package com.example.restomaster.ui.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.dao.OrderDao
import com.example.restomaster.data.local.dao.MenuDao
import com.example.restomaster.ui.table.detail.OrderItemWithMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val orderDao: OrderDao,
    private val menuDao: MenuDao
) : ViewModel() {

    private val orderId: Long = checkNotNull(savedStateHandle["orderId"])

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    init {
        loadOrderItems()
    }

    private fun loadOrderItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            orderDao.getOrderItems(orderId).collect { items ->
                val itemsWithMenu = items.map { item ->
                    val menuItem = menuDao.getMenuItem(item.menuItemId)
                    OrderItemWithMenu(
                        orderItem = item,
                        menuName = menuItem?.name ?: "Unknown",
                        menuPrice = menuItem?.price?.toLong() ?: 0L
                    )
                }
                _uiState.value = _uiState.value.copy(
                    orderItems = itemsWithMenu,
                    isLoading = false
                )
            }
        }
    }

    fun onMarkItemServed(orderItemId: Long) {
        viewModelScope.launch {
            orderDao.updateOrderItemStatus(orderItemId, "served")
        }
    }
}

data class OrderDetailUiState(
    val orderItems: List<OrderItemWithMenu> = emptyList(),
    val isLoading: Boolean = false
)
