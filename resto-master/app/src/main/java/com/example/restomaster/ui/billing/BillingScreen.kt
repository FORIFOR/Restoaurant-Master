package com.example.restomaster.ui.billing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.data.repository.TableCheckoutSummary
import com.example.restomaster.ui.components.DaCard
import com.example.restomaster.ui.components.DaPrimaryButton
import com.example.restomaster.ui.theme.LocalSpacing
import java.text.NumberFormat
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BillingScreen(
    viewModel: BillingViewModel = hiltViewModel(),
    onNavigateReceipt: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val navReceipt by viewModel.navigateToReceipt.collectAsState()
    val spacing = LocalSpacing.current
    val confirmTarget = remember { mutableStateOf<TableCheckoutSummary?>(null) }

    LaunchedEffect(navReceipt) {
        if (navReceipt != null) {
            onNavigateReceipt(navReceipt!!)
            viewModel.onNavigatedReceipt()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("会計") }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text("読み込み中…")
                }
            }

            uiState.summaries.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("会計対象の注文はありません")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = spacing.md),
                    verticalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    items(uiState.summaries, key = { it.tableId }) { summary ->
                        BillingCard(
                            summary = summary,
                            onClose = { confirmTarget.value = summary }
                        )
                    }
                }
            }
        }
    }

    val orderToClose = confirmTarget.value
    if (orderToClose != null) {
        PaymentMethodDialog(
            summary = orderToClose,
            onDismiss = { confirmTarget.value = null },
            onConfirm = { paymentMethod ->
                viewModel.closeTable(orderToClose.tableId, paymentMethod)
                confirmTarget.value = null
            }
        )
    }

    // Checkout Success Dialog
    if (uiState.checkoutSuccessBillId != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCheckoutSuccessDialog() },
            title = { Text("会計完了") },
            text = { Text("会計が正常に完了しました。") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.printReceipt(uiState.checkoutSuccessBillId!!)
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Print, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("レシート印刷")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissCheckoutSuccessDialog() }) { Text("閉じる") }
            }
        )
    }
}

@Composable
private fun BillingCard(
    summary: TableCheckoutSummary,
    onClose: () -> Unit
) {
    val spacing = LocalSpacing.current
    val numberFormat = rememberNumberFormat()

    DaCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(spacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${summary.tableNumber ?: summary.tableId}番卓",
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (summary.hasCheckoutRequest) {
                            Spacer(Modifier.width(8.dp))
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Payment,
                                        contentDescription = "Checkout Request",
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(Modifier.width(2.dp))
                                    Text("会計依頼", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                    if (summary.orderIds.isNotEmpty()) {
                        Text(
                            text = "注文 ${summary.orderIds.size}件 (ID: ${summary.orderIds.joinToString()})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (summary.hasCheckoutRequest && summary.checkoutRequestTime != null) {
                        val elapsed = (System.currentTimeMillis() - summary.checkoutRequestTime) / 1000 / 60
                        Text(
                            text = "${elapsed}分前に会計依頼",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Check, contentDescription = "会計確定")
                }
            }

            Spacer(Modifier.height(spacing.sm))

            Text("提供済み合計: ¥${numberFormat.format(summary.servedTotal)}")
            Text("未提供を含む: ¥${numberFormat.format(summary.pendingTotal)}")

            Spacer(Modifier.height(spacing.sm))

            DaPrimaryButton(
                text = "会計を確定する",
                onClick = onClose,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun rememberNumberFormat(): NumberFormat {
    return remember { NumberFormat.getInstance(Locale.JAPAN) }
}

@Composable
private fun PaymentMethodDialog(
    summary: TableCheckoutSummary,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val numberFormat = rememberNumberFormat()
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text("支払い方法を選択") 
        },
        text = {
            Column {
                Text(
                    "${summary.tableNumber ?: summary.tableId}番卓",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "注文${summary.orderIds.size}件 / 合計: ¥${numberFormat.format(summary.servedTotal)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(Modifier.height(16.dp))
                
                // Cash Button
                DaPrimaryButton(
                    text = "💵 現金",
                    onClick = { onConfirm("CASH") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(Modifier.height(8.dp))
                
                // PayPay Button
                DaPrimaryButton(
                    text = "📱 PayPay",
                    onClick = { onConfirm("PAYPAY") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(Modifier.height(8.dp))
                
                // Credit Card (via Stripe)
                DaPrimaryButton(
                    text = "💳 クレジットカード",
                    onClick = { onConfirm("STRIPE") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("キャンセル") }
        }
    )
}
