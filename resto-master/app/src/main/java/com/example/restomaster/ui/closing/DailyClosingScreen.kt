package com.example.restomaster.ui.closing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.domain.model.*
import com.example.restomaster.ui.common.formatYen
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DailyClosingScreen(
    viewModel: ClosingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Period Selector
        PeriodSelector(
            selected = uiState.period,
            onSelected = viewModel::onPeriodSelected,
            periodLabel = uiState.periodLabel
        )

        Spacer(Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
        } else {
            uiState.summary?.let { summary ->
                // Summary Card
                SummaryCard(summary)

                Spacer(Modifier.height(16.dp))

                // Payment Breakdown
                PaymentBreakdownCard(summary.paymentBreakdown)

                // Unpaid Bills (if any)
                if (summary.unpaidBills.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    UnpaidBillsCard(summary.unpaidBills)
                }

                // Abnormal Payments (if any)
                if (summary.abnormalPayments.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    AbnormalPaymentsCard(summary.abnormalPayments)
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun PeriodSelector(
    selected: ClosingPeriod,
    onSelected: (ClosingPeriod) -> Unit,
    periodLabel: String
) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ClosingPeriod.entries.forEach { period ->
                FilterChip(
                    selected = selected == period,
                    onClick = { onSelected(period) },
                    label = { Text(period.label) }
                )
            }
        }
        Text(
            text = periodLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun SummaryCard(summary: DailyClosingSummary) {
    val isBalanced = summary.diff == 0L
    val statusColor = if (isBalanced) Color(0xFF4CAF50) else Color(0xFFFF9800)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("日次サマリー", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        if (isBalanced) "✓ 整合" else "△ 差額あり",
                        color = statusColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Sales Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("売上合計", style = MaterialTheme.typography.bodyLarge)
                Text(formatYen(summary.salesTotal), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(8.dp))

            // Payments Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("決済合計", style = MaterialTheme.typography.bodyLarge)
                Text(formatYen(summary.paymentsTotal), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Difference
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("差額", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(
                    formatYen(summary.diff),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isBalanced) Color(0xFF4CAF50) else Color(0xFFFF9800)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "会計件数: ${summary.billCount}件",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PaymentBreakdownCard(breakdown: List<PaymentMethodTotal>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("決済内訳", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(12.dp))

            if (breakdown.isEmpty()) {
                Text("決済データがありません", color = Color.Gray)
            } else {
                breakdown.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.displayName, style = MaterialTheme.typography.bodyMedium)
                        Text(formatYen(item.total), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
private fun UnpaidBillsCard(unpaidBills: List<UnpaidBill>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "⚠ 未収金のある会計 (${unpaidBills.size}件)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100)
            )

            Spacer(Modifier.height(8.dp))

            unpaidBills.forEach { bill ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "会計#${bill.billId} (テーブル${bill.tableId})",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        formatYen(bill.totalAmount.toLong()),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AbnormalPaymentsCard(abnormalPayments: List<AbnormalPayment>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "⚠ 異常ステータスの決済 (${abnormalPayments.size}件)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828)
            )

            Spacer(Modifier.height(8.dp))

            abnormalPayments.forEach { payment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "${payment.provider} - ${payment.statusDisplayName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "会計#${payment.billId}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Text(
                        formatYen(payment.amount.toLong()),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
