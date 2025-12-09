package com.example.restomaster.ui.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.entity.OrderEntity
import com.example.restomaster.data.local.entity.TableEntity
import com.example.restomaster.domain.repository.OrderRepository
import com.example.restomaster.domain.repository.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tableRepository: TableRepository,
    private val orderRepository: OrderRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // Assuming single store for now
    private val currentStoreId = 1L

    data class UiState(
        val tables: List<TableEntity> = emptyList(),
        val activeOrders: List<OrderEntity> = emptyList(), // Simplified for now
        val isHostMode: Boolean = true
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                tableRepository.getTables(currentStoreId),
                orderRepository.getActiveOrders(currentStoreId)
            ) { tables, orders ->
                UiState(
                    tables = tables,
                    activeOrders = orders,
                    isHostMode = true // TODO: Fetch from Settings
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
