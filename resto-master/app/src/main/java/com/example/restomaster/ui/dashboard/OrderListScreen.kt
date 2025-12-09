package com.example.restomaster.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(
    onBack: () -> Unit,
    onOrderClick: (Long) -> Unit,
    viewModel: OrderListViewModel = hiltViewModel()
) {
    val orderRows by viewModel.orderRows.collectAsState()
    val spacing = LocalSpacing.current
    
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedOrderId by remember { mutableStateOf<Long?>(null) }

    if (showConfirmDialog && selectedOrderId != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("確認") },
            text = { Text("この注文を提供済みにしますか？\n提供済みにすると一覧から消えます。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.markOrderServed(selectedOrderId!!)
                        showConfirmDialog = false
                        selectedOrderId = null
                    }
                ) {
                    Text("提供済みにする")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("注文一覧") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
            contentPadding = PaddingValues(vertical = spacing.md)
        ) {
            if (orderRows.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(spacing.lg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "現在アクティブな注文はありません",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(orderRows, key = { it.orderId }) { row ->
                    OrderMonitorListItem(
                        row = row,
                        onClick = { onOrderClick(row.orderId) },
                        onMarkServed = {
                            selectedOrderId = row.orderId
                            showConfirmDialog = true
                        }
                    )
                }
            }
        }
    }
}
