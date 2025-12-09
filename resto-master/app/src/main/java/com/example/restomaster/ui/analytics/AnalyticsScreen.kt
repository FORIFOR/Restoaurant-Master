package com.example.restomaster.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.domain.model.DailySales
import com.example.restomaster.domain.model.HourlySales
import com.example.restomaster.domain.model.MenuItemSales
import com.example.restomaster.domain.model.SalesKPIs
import com.example.restomaster.ui.analytics.formatCurrency
import com.example.restomaster.ui.closing.DailyClosingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val kpis by viewModel.kpis.collectAsState()
    val abcAnalysis by viewModel.abcAnalysis.collectAsState()
    val hourlySales by viewModel.hourlySales.collectAsState()
    val dailySales by viewModel.dailySales.collectAsState()
    val monthlySales by viewModel.monthlySales.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val dateRange by viewModel.dateRange.collectAsState()

    val timeOfDayUiState by viewModel.timeOfDayUiState.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("概要", "ABC分析", "時間帯別", "日次締め")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("売上分析") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "再読込")
                    }
                    IconButton(onClick = { viewModel.clearSalesDataAndReload() }) {
                        Icon(Icons.Default.DeleteForever, contentDescription = "データ初期化")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            if (isLoading && selectedTabIndex != 2) { // TimeOfDayTab handles its own loading
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                when (selectedTabIndex) {
                    0 -> OverviewTab(kpis)
                    1 -> ABCAnalysisTab(abcAnalysis)
                    2 -> TimeOfDayTab(timeOfDayUiState, viewModel::onPeriodSelected)
                    3 -> DailyClosingScreen()
                }
            }
        }
    }
}

@Composable
fun OverviewTab(kpis: SalesKPIs?) {
    if (kpis == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("データがありません")
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            KPICard(
                title = "売上合計",
                value = formatCurrency(kpis.totalSales),
                icon = Icons.Default.AttachMoney,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        item {
            KPICard(
                title = "テーブル平均単価",
                value = formatCurrency(kpis.averageCheckPerTable),
                icon = Icons.Default.TableRestaurant
            )
        }
        item {
            KPICard(
                title = "客単価",
                value = formatCurrency(kpis.averageSpendPerCustomer),
                icon = Icons.Default.Person
            )
        }
        item {
            KPICard(
                title = "注文件数",
                value = kpis.totalOrders.toString(),
                icon = Icons.Default.Receipt
            )
        }
        item {
            KPICard(
                title = "来客数",
                value = kpis.totalCustomers.toString(),
                icon = Icons.Default.Groups
            )
        }
        item {
            KPICard(
                title = "回転率",
                value = String.format("%.1f回", kpis.tableTurnoverRate),
                icon = Icons.Default.Autorenew
            )
        }
    }
}

@Composable
fun ABCAnalysisTab(items: List<MenuItemSales>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("商品名", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("売上", fontWeight = FontWeight.Bold, modifier = Modifier.width(80.dp))
                Text("ランク", fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
            }
            HorizontalDivider()
        }

        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.name, fontWeight = FontWeight.Medium)
                    Text(
                        "${item.quantitySold}個販売 • 売上構成比 ${String.format("%.1f", item.revenuePercentage)}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Text(
                    formatCurrency(item.totalRevenue),
                    modifier = Modifier.width(80.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Box(modifier = Modifier.width(60.dp)) {
                    ABCBadge(item.abcClass)
                }
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun TimeOfDayTab(
    uiState: TimeOfDayAnalyticsUiState,
    onPeriodSelected: (SalesPeriodPreset) -> Unit
) {
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PeriodFilterChips(
            selected = uiState.periodPreset,
            onSelected = onPeriodSelected,
            currentRangeText = uiState.periodLabel
        )

        Spacer(Modifier.height(12.dp))

        SummaryCardsRow(uiState.summary)

        Spacer(Modifier.height(16.dp))

        // Dynamic Chart Selection
        when (uiState.periodPreset) {
            SalesPeriodPreset.TODAY, SalesPeriodPreset.YESTERDAY -> {
                TimeOfDayChartCard(buckets = uiState.timeBuckets)
            }
            SalesPeriodPreset.THIS_WEEK -> {
                WeekdayChartCard(buckets = uiState.weekdayBuckets)
            }
            SalesPeriodPreset.THIS_MONTH -> {
                // Reuse WeekdayChartCard or create a generic one for Weekly
                // For now, reusing WeekdayChartCard as it takes TimeBucketSales
                ChartCard(title = "週別売上", buckets = uiState.weeklyBuckets)
            }
            SalesPeriodPreset.THIS_YEAR -> {
                ChartCard(title = "月別売上", buckets = uiState.monthlyBuckets)
            }
        }

        Spacer(Modifier.height(16.dp))

        TopTimeSlotsList(uiState.topRanking)
        
        Spacer(Modifier.height(24.dp))
    }
}


