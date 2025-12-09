package com.example.restomaster.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun KPICard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ABCBadge(
    abcClass: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor, label) = when (abcClass) {
        "A" -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Aランク") // 緑 - 売れ筋
        "B" -> Triple(Color(0xFFFFF8E1), Color(0xFFF57F17), "Bランク") // 黄 - 普通
        "C" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Cランク") // 赤 - 要改善
        else -> Triple(Color.Gray, Color.White, abcClass)
    }

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

fun formatCurrency(amount: Long): String {
    return NumberFormat.getCurrencyInstance(Locale.JAPAN).format(amount)
}

fun formatCurrency(amount: Int): String {
    return NumberFormat.getCurrencyInstance(Locale.JAPAN).format(amount)
}

@Composable
fun SummaryCardsRow(summary: SalesSummary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            title = "売上合計",
            value = com.example.restomaster.ui.common.formatYen(summary.totalAmount),
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "注文件数",
            value = "${summary.orderCount}件",
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "客単価",
            value = "${com.example.restomaster.ui.common.formatYen(summary.avgPerGuest)} / 人",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PeriodFilterChips(
    selected: SalesPeriodPreset,
    onSelected: (SalesPeriodPreset) -> Unit,
    currentRangeText: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selected == SalesPeriodPreset.TODAY,
                onClick = { onSelected(SalesPeriodPreset.TODAY) },
                label = { Text("今日") }
            )
            FilterChip(
                selected = selected == SalesPeriodPreset.YESTERDAY,
                onClick = { onSelected(SalesPeriodPreset.YESTERDAY) },
                label = { Text("昨日") }
            )
            FilterChip(
                selected = selected == SalesPeriodPreset.THIS_WEEK,
                onClick = { onSelected(SalesPeriodPreset.THIS_WEEK) },
                label = { Text("今週") }
            )
            FilterChip(
                selected = selected == SalesPeriodPreset.THIS_MONTH,
                onClick = { onSelected(SalesPeriodPreset.THIS_MONTH) },
                label = { Text("今月") }
            )
            FilterChip(
                selected = selected == SalesPeriodPreset.THIS_YEAR,
                onClick = { onSelected(SalesPeriodPreset.THIS_YEAR) },
                label = { Text("今年") }
            )
        }
        
        Text(
            text = currentRangeText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )
    }
}

@Composable
fun ChartCard(
    title: String,
    buckets: List<TimeBucketSales>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            
            if (buckets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("データがありません", color = Color.Gray)
                }
            } else {
                TimeOfDayBarLineChart(
                    buckets = buckets,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                
                Spacer(Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendDot(color = Color(0xFF005BAC))
                    Text("売上", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 4.dp, end = 16.dp))
                    
                    LegendDot(color = Color(0xFFFF9800))
                    Text("注文件数", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}

@Composable
fun TimeOfDayChartCard(buckets: List<TimeBucketSales>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("時間帯別売上", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("売上(円) と 注文件数", style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(12.dp))

            // Legend
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LegendDot(color = Color(0xFF005BAC))
                Text("売上(円)", style = MaterialTheme.typography.labelSmall)
                Spacer(Modifier.width(8.dp))
                LegendDot(color = Color(0xFFFF9800))
                Text("注文件数", style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(12.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
            ) {
                TimeOfDayBarLineChart(buckets = buckets, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun WeekdayChartCard(buckets: List<TimeBucketSales>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("曜日別売上", style = MaterialTheme.typography.titleMedium)
            
            Spacer(Modifier.height(12.dp))
            
             // Legend
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LegendDot(color = Color(0xFF005BAC))
                Text("売上(円)", style = MaterialTheme.typography.labelSmall)
                Spacer(Modifier.width(8.dp))
                LegendDot(color = Color(0xFFFF9800))
                Text("注文件数", style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(12.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
            ) {
                TimeOfDayBarLineChart(buckets = buckets, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun LegendDot(color: Color) {
    Box(modifier = Modifier.size(8.dp).background(color, androidx.compose.foundation.shape.CircleShape))
}

@Composable
fun TopTimeSlotsList(slots: List<TimeSlot>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("売上トップ5 (時間帯)", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            
            // Header
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text("時間帯", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Text("売上", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.End)
                Text("件数", modifier = Modifier.weight(0.5f), style = MaterialTheme.typography.labelMedium, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.End)
                Text("客単価", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.End)
            }
            HorizontalDivider()
            
            slots.forEach { slot ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text(slot.label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                    Text(com.example.restomaster.ui.common.formatYen(slot.sales), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, textAlign = androidx.compose.ui.text.style.TextAlign.End)
                    Text("${slot.orderCount}件", modifier = Modifier.weight(0.5f), style = MaterialTheme.typography.bodyMedium, textAlign = androidx.compose.ui.text.style.TextAlign.End)
                    Text(com.example.restomaster.ui.common.formatYen(slot.avgOrderValue), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, textAlign = androidx.compose.ui.text.style.TextAlign.End)
                }
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}
