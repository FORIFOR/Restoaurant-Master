package com.example.restomaster.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.ui.theme.LocalSpacing
import com.example.restomaster.ui.theme.StatusAlert
import com.example.restomaster.ui.theme.StatusEmpty
import com.example.restomaster.ui.theme.StatusOccupied
import com.example.restomaster.data.local.entity.CallRequestEntity
import com.example.restomaster.domain.model.TableStatusUi
import com.example.restomaster.domain.model.DashboardSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterDashboardScreen(
    viewModel: MasterDashboardViewModel = hiltViewModel(),
    onTableClick: (Long) -> Unit,
    onOrderClick: (Long) -> Unit = {},
    onNavigateToOrderList: () -> Unit = {},
    onNavigateTables: () -> Unit,
    onNavigateMenu: () -> Unit,
    onNavigateAnalytics: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "レストランマスター",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "本日の営業状況",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.shadow(4.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = spacing.md)
        ) {
            Spacer(Modifier.height(spacing.md))
            
            // Summary Cards
            SummarySection(
                summary = uiState.summary,
                onActiveOrdersClick = onNavigateToOrderList,
                onCallRequestsClick = { viewModel.setFilter(TableFilter.CALL) },
                onCheckoutRequestsClick = { viewModel.setFilter(TableFilter.CHECKOUT) },
                onAnalyticsClick = onNavigateAnalytics
            )
            
            Spacer(Modifier.height(spacing.lg))
            
            // Filter Bar (Scrollable)
            FilterBar(
                currentFilter = uiState.filter,
                onFilterSelect = viewModel::setFilter
            )
            
            Spacer(Modifier.height(spacing.md))
            
            // Table Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                horizontalArrangement = Arrangement.spacedBy(spacing.md),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = spacing.lg)
            ) {
                items(uiState.tables, key = { it.tableId }) { table ->
                    TableStatusCard(
                        table = table,
                        onClick = { onTableClick(table.tableId) },
                        onResolveCall = { requestId -> viewModel.resolveCallRequest(requestId) },
                        onResolveCheckout = { viewModel.resolveCheckoutRequest(table.tableId) }
                    )
                }
            }
        }
    }
}

@Composable
fun SummarySection(
    summary: DashboardSummary,
    onActiveOrdersClick: () -> Unit = {},
    onCallRequestsClick: () -> Unit = {},
    onCheckoutRequestsClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {}
) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
        Row(horizontalArrangement = Arrangement.spacedBy(spacing.md)) {
            SummaryCard(
                label = "稼働テーブル",
                mainValue = "${summary.occupiedTables} / ${summary.totalTables}",
                subValue = "空席 ${summary.totalTables - summary.occupiedTables}卓",
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                label = "アクティブ注文",
                mainValue = "${summary.activeOrders}件",
                subValue = "新規 ${summary.newOrders}件",
                modifier = Modifier.weight(1f),
                onClick = onActiveOrdersClick
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacing.md)) {
            SummaryCard(
                label = "店員呼び出し",
                mainValue = "${summary.callRequests}件",
                subValue = if (summary.callRequests > 0) "要対応" else "なし",
                isAlert = summary.callRequests > 0,
                modifier = Modifier.weight(1f),
                onClick = onCallRequestsClick
            )
            SummaryCard(
                label = "会計待ち",
                mainValue = "${summary.checkoutRequests}卓",
                subValue = "合計 ¥${summary.checkoutTotalAmount}", // Format currency
                isAlert = summary.checkoutRequests > 0,
                modifier = Modifier.weight(1f),
                onClick = onCheckoutRequestsClick
            )
        }
        // New Analytics Row
        Row(horizontalArrangement = Arrangement.spacedBy(spacing.md)) {
            SummaryCard(
                label = "売上分析",
                mainValue = "Analytics",
                subValue = "売上・分析を確認",
                modifier = Modifier.weight(1f),
                onClick = onAnalyticsClick
            )
            // Placeholder for balance
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun SummaryCard(
    label: String,
    mainValue: String,
    subValue: String,
    isAlert: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val spacing = LocalSpacing.current
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val borderColor = if (isAlert) StatusAlert else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    val valueColor = if (isAlert) StatusAlert else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(spacing.xs))
            Text(
                text = mainValue,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Spacer(Modifier.height(spacing.xs))
            Text(
                text = subValue,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FilterBar(
    currentFilter: TableFilter,
    onFilterSelect: (TableFilter) -> Unit
) {
    val spacing = LocalSpacing.current
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        contentPadding = PaddingValues(horizontal = 2.dp) // Avoid clipping shadows
    ) {
        item {
            FilterChip(
                label = "すべて",
                selected = currentFilter == TableFilter.ALL,
                onClick = { onFilterSelect(TableFilter.ALL) }
            )
        }
        item {
            FilterChip(
                label = "注文中",
                selected = currentFilter == TableFilter.ACTIVE_ORDERS,
                onClick = { onFilterSelect(TableFilter.ACTIVE_ORDERS) }
            )
        }
        item {
            FilterChip(
                label = "会計待ち",
                selected = currentFilter == TableFilter.CHECKOUT,
                onClick = { onFilterSelect(TableFilter.CHECKOUT) }
            )
        }
        item {
            FilterChip(
                label = "呼び出し",
                selected = currentFilter == TableFilter.CALL,
                onClick = { onFilterSelect(TableFilter.CALL) }
            )
        }
        item {
            FilterChip(
                label = "空席",
                selected = currentFilter == TableFilter.EMPTY,
                onClick = { onFilterSelect(TableFilter.EMPTY) }
            )
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = CircleShape,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun TableStatusCard(
    table: TableStatusUi,
    onClick: () -> Unit,
    onResolveCall: (Long) -> Unit,
    onResolveCheckout: () -> Unit
) {
    val spacing = LocalSpacing.current
    
    // Determine status color
    val statusColor = when {
        table.hasCallRequest -> StatusAlert
        table.hasCheckoutRequest -> StatusAlert
        table.hasActiveOrder -> StatusOccupied
        table.isOccupied -> StatusOccupied
        else -> StatusEmpty
    }

    val statusLabel = when {
        table.hasCallRequest -> "呼び出し"
        table.hasCheckoutRequest -> "会計"
        table.hasActiveOrder -> "注文中"
        table.isOccupied -> "着席"
        else -> "空席"
    }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.height(140.dp)) { // Increased height for buttons
            // Left colored strip
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxSize()
                    .background(statusColor)
            )
            
            Column(
                modifier = Modifier
                    .padding(spacing.md)
                    .fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${table.tableNumber}番卓",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Surface(
                        color = statusColor.copy(alpha = 0.1f),
                        contentColor = statusColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = statusLabel,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(Modifier.height(spacing.sm))
                
                // Content
                if (table.isOccupied || table.hasActiveOrder) {
                    Text(
                        text = "注文: ${table.activeOrderCount}品",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "¥${table.totalAmount}", // Format
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = "---",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (table.hasCallRequest) {
                        // Find the first new call request (simplified)
                        // Ideally we should pass the request ID from UI state
                        val requestId = table.callRequests.firstOrNull { it.type == "call_staff" && it.status == "new" }?.id
                        if (requestId != null) {
                            androidx.compose.material3.Button(
                                onClick = { onResolveCall(requestId) },
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = StatusAlert),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("対応", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    } else if (table.hasCheckoutRequest) {
                        androidx.compose.material3.Button(
                            onClick = onClick, // Navigate to table detail instead of resolving immediately
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = StatusAlert),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("会計対応", style = MaterialTheme.typography.labelSmall)
                        }
                    } else {
                        // Show icons if no action needed
                         if (table.isOccupied) {
                            Text(
                                text = "滞在: ${table.stayMinutes ?: 0}分",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun OrderMonitorListItem(
    row: com.example.restomaster.ui.dashboard.OrderMonitorRow,
    onClick: () -> Unit,
    onMarkServed: (() -> Unit)? = null
) {
    val time = remember(row.createdAt) {
        java.text.SimpleDateFormat("HH:mm", java.util.Locale.JAPAN).format(java.util.Date(row.createdAt))
    }
    val statusLabel = remember(row.status) {
        when (row.status) {
            "open" -> "受付済み"
            "preparing" -> "調理中"
            "checkout_requested" -> "会計待ち"
            else -> row.status
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${row.tableNumber}番卓",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "¥${row.totalAmount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "注文時刻: $time",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            row.itemsSummary.forEach {
                Text(
                    text = "・$it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ステータス: $statusLabel",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (onMarkServed != null) {
                    androidx.compose.material3.Button(
                        onClick = onMarkServed,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("提供済みにする", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
