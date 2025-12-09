package com.example.restomaster.ui.analytics

enum class SalesPeriodPreset {
    TODAY,
    YESTERDAY,
    THIS_WEEK,
    THIS_MONTH,
    THIS_YEAR
}

data class SalesSummary(
    val totalAmount: Long,
    val orderCount: Int,
    val avgPerGuest: Long
)

data class TimeBucketSales(
    val label: String, // "12時", "月", "第1週", "1月"
    val sales: Long,
    val orderCount: Int
)

data class TimeSlot(
    val label: String,
    val sales: Long,
    val orderCount: Int,
    val avgOrderValue: Long
)

data class TimeOfDayAnalyticsUiState(
    val isLoading: Boolean = false,
    val periodPreset: SalesPeriodPreset = SalesPeriodPreset.TODAY,
    val periodLabel: String = "",
    val summary: SalesSummary = SalesSummary(0, 0, 0),
    val timeBuckets: List<TimeBucketSales> = emptyList(), // For Today/Yesterday
    val weekdayBuckets: List<TimeBucketSales> = emptyList(), // For This Week
    val weeklyBuckets: List<TimeBucketSales> = emptyList(), // For This Month
    val monthlyBuckets: List<TimeBucketSales> = emptyList(), // For This Year
    val topRanking: List<TimeSlot> = emptyList() // Generic top list
)
