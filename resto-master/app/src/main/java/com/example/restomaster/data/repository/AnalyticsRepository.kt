package com.example.restomaster.data.repository

import com.example.restomaster.data.local.dao.BillDao
import com.example.restomaster.data.local.dao.BillItemDao
import com.example.restomaster.data.local.dao.DateSalesRaw
import com.example.restomaster.data.local.dao.TableDao
import com.example.restomaster.domain.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepository @Inject constructor(
    private val billDao: BillDao,
    private val billItemDao: BillItemDao,
    private val tableDao: TableDao
) {

    /**
     * Get menu item ABC analysis
     * A-class: Top 70% of revenue
     * B-class: Next 20% of revenue
     * C-class: Bottom 10% of revenue
     */
    suspend fun getMenuItemABCAnalysis(from: Long, to: Long): List<MenuItemSales> {
        val rawData = billItemDao.getMenuItemSalesRaw(from, to)
        
        if (rawData.isEmpty()) return emptyList()
        
        // Calculate total revenue
        val totalRevenue = rawData.sumOf { it.totalRevenue }
        
        // Calculate cumulative revenue and classify
        var cumulativeRevenue = 0
        return rawData.map { item ->
            cumulativeRevenue += item.totalRevenue
            val revenuePercentage = (item.totalRevenue.toFloat() / totalRevenue) * 100
            val cumulativePercentage = (cumulativeRevenue.toFloat() / totalRevenue) * 100
            
            val abcClass = when {
                cumulativePercentage <= 70 -> "A"
                cumulativePercentage <= 90 -> "B"
                else -> "C"
            }
            
            MenuItemSales(
                menuItemId = item.menuItemId,
                name = item.name,
                quantitySold = item.quantitySold,
                totalRevenue = item.totalRevenue,
                revenuePercentage = revenuePercentage,
                abcClass = abcClass
            )
        }
    }

    /**
     * Get sales grouped by hour
     */
    suspend fun getSalesByHour(from: Long, to: Long): List<HourlySales> {
        val rawData = billDao.getHourlySalesRaw(from, to)
        
        return rawData.map { item ->
            HourlySales(
                hour = item.hour,
                orderCount = item.order_count,
                totalSales = item.total_sales,
                averageOrderValue = if (item.order_count > 0) {
                    item.total_sales / item.order_count
                } else 0
            )
        }
    }

    /**
     * Get sales grouped by day of week
     */
    suspend fun getSalesByDayOfWeek(from: Long, to: Long): List<DailySales> {
        val rawData = billDao.getDailySalesRaw(from, to)
        val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        // Fill all 7 days (0=Sun..6=Sat)
        val map = rawData.associateBy { it.day_of_week }
        return (0..6).map { dow ->
            val item = map[dow]
            val orders = item?.order_count ?: 0
            val sales = item?.total_sales ?: 0
            DailySales(
                dayOfWeek = dow,
                dayName = dayNames.getOrElse(dow) { "Unknown" },
                orderCount = orders,
                totalSales = sales,
                averageOrderValue = if (orders > 0) sales / orders else 0
            )
        }
    }



    /**
     * Get sales grouped by week (1st Week, 2nd Week...)
     */
    suspend fun getSalesByWeek(from: Long, to: Long): List<WeeklySales> {
        val rawData = billDao.getSalesByDateRaw(from, to)
        if (rawData.isEmpty()) return emptyList()

        // Group by week
        // Simple logic: Divide days into 7-day chunks starting from the 'from' date
        // Or better: Group by calendar week of the month
        
        val calendar = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

        val weeklyMap = mutableMapOf<Int, MutableList<DateSalesRaw>>()

        rawData.forEach { item ->
            val date = sdf.parse(item.date)
            if (date != null) {
                calendar.time = date
                val weekOfMonth = calendar.get(java.util.Calendar.WEEK_OF_MONTH)
                weeklyMap.getOrPut(weekOfMonth) { mutableListOf() }.add(item)
            }
        }

        return weeklyMap.toSortedMap().map { (week, items) ->
            val orderCount = items.sumOf { it.order_count }
            val totalSales = items.sumOf { it.total_sales }
            WeeklySales(
                weekLabel = "第${week}週",
                orderCount = orderCount,
                totalSales = totalSales,
                averageOrderValue = if (orderCount > 0) totalSales / orderCount else 0
            )
        }
    }

    /**
     * Get sales grouped by month (YYYY-MM) for the range
     */
    suspend fun getSalesByMonth(from: Long, to: Long): List<MonthlySales> {
        val rawData = billDao.getMonthlySalesRaw(from, to)
        return rawData.map { item ->
            val orders = item.order_count
            val sales = item.total_sales
            MonthlySales(
                yearMonth = item.year_month,
                orderCount = orders,
                totalSales = sales,
                averageOrderValue = if (orders > 0) sales / orders else 0
            )
        }
    }

    /**
     * Get all KPIs for a date range
     */
    suspend fun getKPIs(from: Long, to: Long): SalesKPIs {
        val kpisRaw = billDao.getKPIsRaw(from, to)
        
        val averageSpendPerCustomer = if (kpisRaw.total_customers > 0) {
            (kpisRaw.total / kpisRaw.total_customers).toInt()
        } else 0
        
        // Calculate table turnover for the period
        // 定義: 回転率 = 期間内の会計件数(PAID伝票数) / テーブル総数 / 日数
        val periodDays = (((to - from) / (24 * 60 * 60 * 1000)) + 1).toInt().coerceAtLeast(1)
        val paidBillCount = billDao.getPaidBillCount(from, to)
        val totalTables = tableDao.getAllTables().size.coerceAtLeast(1)
        val tableTurnoverRate = paidBillCount.toFloat() / totalTables / periodDays
        
        return SalesKPIs(
            totalSales = kpisRaw.total,
            averageCheckPerTable = kpisRaw.avg_check,
            averageSpendPerCustomer = averageSpendPerCustomer,
            totalOrders = kpisRaw.total_bills,
            totalCustomers = kpisRaw.total_customers,
            tableTurnoverRate = tableTurnoverRate
        )
    }

    /**
     * Get average check per table (客単価)
     */
    suspend fun getAverageCheckPerTable(from: Long, to: Long): Int {
        val kpisRaw = billDao.getKPIsRaw(from, to)
        return kpisRaw.avg_check
    }

    /**
     * Get average spend per customer (一人あたり平均)
     */
    suspend fun getAverageSpendPerCustomer(from: Long, to: Long): Int {
        val kpisRaw = billDao.getKPIsRaw(from, to)
        return if (kpisRaw.total_customers > 0) {
            (kpisRaw.total / kpisRaw.total_customers).toInt()
        } else 0
    }

    /**
     * Get total sales
     */
    suspend fun getTotalSales(from: Long, to: Long): Long {
        val kpisRaw = billDao.getKPIsRaw(from, to)
        return kpisRaw.total
    }

    /**
     * Get table turnover rate for a specific day
     */
    suspend fun getTableTurnoverRate(startOfDay: Long, endOfDay: Long): Float {
        // Get total number of tables
        val totalTables = tableDao.getAllTables().size
        if (totalTables == 0) return 0f
        
        // Get number of table uses
        val tableUses = billDao.getTableUsesForDay(startOfDay, endOfDay)
        
        return tableUses.toFloat() / totalTables
    }
}
