package com.example.restomaster.ui.billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.repository.BillingRepository
import com.example.restomaster.data.repository.TableCheckoutSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BillingUiState(
    val summaries: List<TableCheckoutSummary> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val checkoutSuccessBillId: Long? = null
)

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val billingRepository: BillingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BillingUiState())
    val uiState: StateFlow<BillingUiState> = _uiState.asStateFlow()

    // Navigation event to open receipt preview
    private val _navigateToReceipt = MutableStateFlow<Long?>(null)
    val navigateToReceipt: StateFlow<Long?> = _navigateToReceipt.asStateFlow()

    init {
        observeSummaries()
    }

    private fun observeSummaries(storeId: Long = 1L) {
        viewModelScope.launch {
            billingRepository.getCheckoutSummaries(storeId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collectLatest { list ->
                    _uiState.value = _uiState.value.copy(
                        summaries = list,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }

    fun closeTable(tableId: Long, paymentMethod: String = "CASH") {
        viewModelScope.launch {
            try {
                val billId = billingRepository.processManualCheckout(tableId, paymentMethod)
                if (billId != -1L) {
                    _uiState.value = _uiState.value.copy(checkoutSuccessBillId = billId)
                    _navigateToReceipt.value = billId
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun printReceipt(billId: Long) {
        viewModelScope.launch {
            try {
                val bytes = billingRepository.generateReceipt(billId)
                // In a real app, we would send 'bytes' to a printer service via Bluetooth/USB.
                // For this MVP, we'll just log it or show a toast (simulated by clearing state).
                // Ideally, we might want to show a "Printing..." state.
                // For now, let's just dismiss the dialog after "printing".
                dismissCheckoutSuccessDialog()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Print failed: ${e.message}")
            }
        }
    }

    fun dismissCheckoutSuccessDialog() {
        _uiState.value = _uiState.value.copy(checkoutSuccessBillId = null)
    }

    fun onNavigatedReceipt() {
        _navigateToReceipt.value = null
    }
}
