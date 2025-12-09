package com.example.restomaster.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
data class DualBarEntry(
    val label: String,
    val primaryValue: Float,
    val primaryLabel: String,
    val secondaryValue: Float,
    val secondaryLabel: String
)

@Composable
fun DualBarChart(
    data: List<DualBarEntry>,
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    secondaryColor: Color = MaterialTheme.colorScheme.secondary,
    primaryLabelTitle: String = "売上",
    secondaryLabelTitle: String = "注文件数",
    maxBarHeight: Dp = 180.dp
) {
    if (data.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
            Text("データがありません")
        }
        return
    }

    val maxPrimary = data.maxOf { it.primaryValue }.coerceAtLeast(1f)
    val maxSecondary = data.maxOf { it.secondaryValue }.coerceAtLeast(1f)

    Column(modifier = modifier.padding(16.dp)) {
        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendChip(color = primaryColor, label = primaryLabelTitle)
            LegendChip(color = secondaryColor, label = secondaryLabelTitle)
        }
        Spacer(Modifier.height(12.dp))

    val scrollState = rememberScrollState()
    val barWidth = 18.dp
    val gap = 16.dp
    val contentWidth = (barWidth * 2 + gap) * data.size + 32.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .height(maxBarHeight)
                .width(contentWidth),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(gap)
        ) {
            data.forEach { entry ->
                val h1 = (entry.primaryValue / maxPrimary * maxBarHeight.value).coerceAtLeast(4f)
                val h2 = (entry.secondaryValue / maxSecondary * maxBarHeight.value).coerceAtLeast(4f)
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(entry.primaryLabel, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(h1.dp)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(primaryColor.copy(alpha = 0.9f))
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(entry.secondaryLabel, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(h2.dp)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(secondaryColor.copy(alpha = 0.9f))
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "${entry.secondaryLabel} / ${entry.primaryLabel}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        // X軸ラベル（時間帯/曜日）をバーの下に横並びで明示
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.forEach { entry ->
                Text(
                    text = entry.label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LegendChip(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .width(14.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
