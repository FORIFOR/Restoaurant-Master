package com.example.restomaster.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.repository.AnalyticsRepository
import com.example.restomaster.data.repository.BillingRepository
import com.example.restomaster.domain.model.*
import com.example.restomaster.util.DateRangeHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository,
    private val billingRepository: BillingRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _kpis = MutableStateFlow<SalesKPIs?>(null)
    val kpis: StateFlow<SalesKPIs?> = _kpis.asStateFlow()

    private val _abcAnalysis = MutableStateFlow<List<MenuItemSales>>(emptyList())
    val abcAnalysis: StateFlow<List<MenuItemSales>> = _abcAnalysis.asStateFlow()

    private val _hourlySales = MutableStateFlow<List<HourlySales>>(emptyList())
    val hourlySales: StateFlow<List<HourlySales>> = _hourlySales.asStateFlow()

    private val _dailySales = MutableStateFlow<List<DailySales>>(emptyList())
    val dailySales: StateFlow<List<DailySales>> = _dailySales.asStateFlow()

    private val _monthlySales = MutableStateFlow<List<MonthlySales>>(emptyList())
    val monthlySales: StateFlow<List<MonthlySales>> = _monthlySales.asStateFlow()

    private val _dateRange = MutableStateFlow(DateRangeHelper.getLast30Days())
    val dateRange: StateFlow<Pair<Long, Long>> = _dateRange.asStateFlow()

    private val _timeOfDayUiState = MutableStateFlow(TimeOfDayAnalyticsUiState())
    val timeOfDayUiState: StateFlow<TimeOfDayAnalyticsUiState> = _timeOfDayUiState.asStateFlow()

    init {
        // Initialize with default range (Today)
        onPeriodSelected(SalesPeriodPreset.TODAY)
    }

    fun setDateRange(from: Long, to: Long) {
        _dateRange.value = Pair(from, to)
        loadData()
    }

    fun onPeriodSelected(preset: SalesPeriodPreset) {
        val (start, end) = when (preset) {
            SalesPeriodPreset.TODAY -> DateRangeHelper.getToday()
            SalesPeriodPreset.YESTERDAY -> DateRangeHelper.getYesterday()
            SalesPeriodPreset.THIS_WEEK -> DateRangeHelper.getThisWeek()
            SalesPeriodPreset.THIS_MONTH -> DateRangeHelper.getThisMonth()
            SalesPeriodPreset.THIS_YEAR -> {
                // Assuming DateRangeHelper doesn't have getThisYear, implementing manually
                val calendar = java.util.Calendar.getInstance()
                val year = calendar.get(java.util.Calendar.YEAR)
                calendar.set(year, 0, 1, 0, 0, 0)
                val startOfYear = calendar.timeInMillis
                calendar.set(year, 11, 31, 23, 59, 59)
                val endOfYear = calendar.timeInMillis
                Pair(startOfYear, endOfYear)
            }
        }
        _dateRange.value = Pair(start, end)

        _timeOfDayUiState.value = _timeOfDayUiState.value.copy(
            periodPreset = preset,
            periodLabel = com.example.restomaster.ui.common.formatDateRange(start, end)
        )
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _timeOfDayUiState.value = _timeOfDayUiState.value.copy(isLoading = true)
            try {
                val (from, to) = _dateRange.value
                
                // Load all data in parallel
                val kpisDeferred = async { analyticsRepository.getKPIs(from, to) }
                val abcDeferred = async { analyticsRepository.getMenuItemABCAnalysis(from, to) }
                val hourlyDeferred = async { analyticsRepository.getSalesByHour(from, to) }
                val dailyDeferred = async { analyticsRepository.getSalesByDayOfWeek(from, to) }
                val weeklyDeferred = async { analyticsRepository.getSalesByWeek(from, to) }
                val monthlyDeferred = async { analyticsRepository.getSalesByMonth(from, to) }

                val kpisResult = kpisDeferred.await()
                val abcResult = abcDeferred.await()
                val hourlyResult = hourlyDeferred.await()
                val dailyResult = dailyDeferred.await()
                val weeklyResult = weeklyDeferred.await()
                val monthlyResult = monthlyDeferred.await()

                _kpis.value = kpisResult
                _abcAnalysis.value = abcResult
                _hourlySales.value = hourlyResult
                _dailySales.value = dailyResult
                _monthlySales.value = monthlyResult

                // Update TimeOfDayUiState
                val summary = SalesSummary(
                    totalAmount = kpisResult.totalSales,
                    orderCount = kpisResult.totalOrders,
                    avgPerGuest = kpisResult.averageSpendPerCustomer.toLong()
                )

                val timeBuckets = hourlyResult.map {
                    TimeBucketSales("${it.hour}時", it.totalSales.toLong(), it.orderCount)
                }

                val weekdayBuckets = dailyResult.map {
                    TimeBucketSales(com.example.restomaster.ui.common.translateDayName(it.dayName), it.totalSales.toLong(), it.orderCount)
                }

                val weeklyBuckets = weeklyResult.map {
                    TimeBucketSales(it.weekLabel, it.totalSales.toLong(), it.orderCount)
                }

                val monthlyBuckets = monthlyResult.map {
                    // Extract month from "YYYY-MM"
                    val month = it.yearMonth.substringAfter("-") + "月"
                    TimeBucketSales(month, it.totalSales.toLong(), it.orderCount)
                }

                // Determine Top Ranking based on period
                val topRanking = when (_timeOfDayUiState.value.periodPreset) {
                    SalesPeriodPreset.TODAY, SalesPeriodPreset.YESTERDAY -> {
                        hourlyResult.sortedByDescending { it.totalSales }
                            .take(5)
                            .map {
                                TimeSlot(
                                    "${it.hour}時",
                                    it.totalSales.toLong(),
                                    it.orderCount,
                                    it.averageOrderValue.toLong()
                                )
                            }
                    }
                    SalesPeriodPreset.THIS_WEEK -> {
                        dailyResult.sortedByDescending { it.totalSales }
                            .take(5)
                            .map {
                                TimeSlot(
                                    com.example.restomaster.ui.common.translateDayName(it.dayName),
                                    it.totalSales.toLong(),
                                    it.orderCount,
                                    it.averageOrderValue.toLong()
                                )
                            }
                    }
                    SalesPeriodPreset.THIS_MONTH -> {
                        weeklyResult.sortedByDescending { it.totalSales }
                            .take(5)
                            .map {
                                TimeSlot(
                                    it.weekLabel,
                                    it.totalSales.toLong(),
                                    it.orderCount,
                                    it.averageOrderValue.toLong()
                                )
                            }
                    }
                    SalesPeriodPreset.THIS_YEAR -> {
                        monthlyResult.sortedByDescending { it.totalSales }
                            .take(5)
                            .map {
                                TimeSlot(
                                    it.yearMonth,
                                    it.totalSales.toLong(),
                                    it.orderCount,
                                    it.averageOrderValue.toLong()
                                )
                            }
                    }
                }

                _timeOfDayUiState.value = _timeOfDayUiState.value.copy(
                    summary = summary,
                    timeBuckets = timeBuckets,
                    weekdayBuckets = weekdayBuckets,
                    weeklyBuckets = weeklyBuckets,
                    monthlyBuckets = monthlyBuckets,
                    topRanking = topRanking,
                    isLoading = false
                )

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
                _timeOfDayUiState.value = _timeOfDayUiState.value.copy(isLoading = false)
            }
        }
    }

    fun clearSalesDataAndReload() {
        viewModelScope.launch {
            runCatching { billingRepository.clearSalesData() }
            loadData()
        }
    }
}
