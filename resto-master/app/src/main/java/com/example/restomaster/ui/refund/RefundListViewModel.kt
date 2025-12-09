package com.example.restomaster.ui.refund

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.remote.api.RefundApi
import com.example.restomaster.data.remote.model.refund.RefundItem
import com.example.restomaster.data.remote.model.refund.RefundOrderRequestV2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RefundUiState(
    val items: List<RefundItem> = emptyList(),
    val nextCursor: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,

    val isRefunding: Boolean = false,
    val refundError: String? = null,
    val refundSuccess: String? = null
)

class RefundListViewModel(
    private val api: RefundApi,
    private val orderId: String
) : ViewModel() {

    private val _state = MutableStateFlow(RefundUiState())
    val state: StateFlow<RefundUiState> = _state

    fun loadInitial() {
        _state.value = RefundUiState(isLoading = true)
        viewModelScope.launch {
            runCatching { api.fetchRefunds(orderId) }
                .onSuccess { res ->
                    _state.value = RefundUiState(
                        items = res.items,
                        nextCursor = res.next_cursor,
                        isLoading = false
                    )
                }
                .onFailure { e ->
                    _state.value = RefundUiState(
                        isLoading = false,
                        error = e.message
                    )
                }
        }
    }

    fun loadMore() {
        val cur = _state.value.nextCursor ?: return
        if (_state.value.isLoading) return

        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            runCatching { api.fetchRefunds(orderId, cursor = cur) }
                .onSuccess { res ->
                    _state.value = _state.value.copy(
                        items = _state.value.items + res.items,
                        nextCursor = res.next_cursor,
                        isLoading = false
                    )
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
        }
    }

    fun refundPartial(amount: Int, reason: String? = "customer_request") {
        if (_state.value.isRefunding) return

        _state.value = _state.value.copy(
            isRefunding = true,
            refundError = null,
            refundSuccess = null
        )

        viewModelScope.launch {
            runCatching {
                api.refundOrderV2(
                    RefundOrderRequestV2(
                        order_id = orderId,
                        amount_jpy = amount,
                        reason = reason,
                        refund_seq = null
                    )
                )
            }.onSuccess {
                _state.value = _state.value.copy(
                    isRefunding = false,
                    refundSuccess = "Refund requested"
                )
                // ✅ 即リフレッシュ
                loadInitial()
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    isRefunding = false,
                    refundError = e.message
                )
            }
        }
    }
}
