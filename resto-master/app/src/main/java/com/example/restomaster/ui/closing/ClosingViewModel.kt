package com.example.restomaster.ui.closing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.repository.ClosingRepository
import com.example.restomaster.domain.model.*
import com.example.restomaster.util.DateRangeHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ClosingPeriod(val label: String) {
    TODAY("今日"),
    YESTERDAY("昨日")
}

data class DailyClosingUiState(
    val isLoading: Boolean = false,
    val period: ClosingPeriod = ClosingPeriod.TODAY,
    val periodLabel: String = "",
    val summary: DailyClosingSummary? = null,
    val error: String? = null
)

@HiltViewModel
class ClosingViewModel @Inject constructor(
    private val closingRepository: ClosingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyClosingUiState())
    val uiState: StateFlow<DailyClosingUiState> = _uiState.asStateFlow()

    private var currentRange: Pair<Long, Long> = DateRangeHelper.getToday()

    init {
        onPeriodSelected(ClosingPeriod.TODAY)
    }

    fun onPeriodSelected(period: ClosingPeriod) {
        currentRange = when (period) {
            ClosingPeriod.TODAY -> DateRangeHelper.getToday()
            ClosingPeriod.YESTERDAY -> DateRangeHelper.getYesterday()
        }
        
        _uiState.value = _uiState.value.copy(
            period = period,
            periodLabel = com.example.restomaster.ui.common.formatDateRange(currentRange.first, currentRange.second)
        )
        
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val (from, to) = currentRange
                val summary = closingRepository.getDailyClosingSummary(from, to)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    summary = summary
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "データの読み込みに失敗しました"
                )
            }
        }
    }

    fun refresh() {
        loadData()
    }
}
