package com.example.restomaster.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restomaster.ui.common.formatYen
import android.graphics.Paint

@Composable
fun TimeOfDayBarLineChart(
    buckets: List<TimeBucketSales>,
    modifier: Modifier = Modifier
) {
    if (buckets.isEmpty()) return

    val maxSales = buckets.maxOf { it.sales }.coerceAtLeast(1L).toFloat()
    val maxOrders = buckets.maxOf { it.orderCount }.coerceAtLeast(1).toFloat()

    val barColor = Color(0xFF005BAC) // Digital Agency Blue
    val lineColor = Color(0xFFFF9800) // Orange
    val labelColor = Color.Gray

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val bottomPadding = 24.dp.toPx()
        val chartHeight = height - bottomPadding
        
        val stepX = width / buckets.size
        val barWidth = stepX * 0.6f

        // Draw Bars (Sales)
        buckets.forEachIndexed { index, bucket ->
            val barHeight = (bucket.sales / maxSales) * chartHeight
            val x = index * stepX + (stepX - barWidth) / 2
            
            if (bucket.sales > 0) {
                drawRect(
                    color = barColor,
                    topLeft = Offset(x, chartHeight - barHeight),
                    size = Size(barWidth, barHeight)
                )
                
                // Draw Sales Label on top of bar if space allows and not 0
                // Simplified: only if it's a peak or significant? 
                // User said: "¥0 は出さない, それ以外は ¥4,260 のように3桁区切り"
                // Drawing text on canvas requires nativeCanvas
                /*
                drawContext.canvas.nativeCanvas.drawText(
                    formatYen(bucket.sales),
                    x + barWidth / 2,
                    chartHeight - barHeight - 5f,
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 10.sp.toPx()
                        textAlign = Paint.Align.CENTER
                    }
                )
                */
            }
        }

        // Draw Line (Order Count)
        val path = Path()
        buckets.forEachIndexed { index, bucket ->
            val x = index * stepX + stepX / 2
            val y = chartHeight - (bucket.orderCount / maxOrders) * chartHeight
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw Points and Labels for Order Count
        buckets.forEachIndexed { index, bucket ->
            val x = index * stepX + stepX / 2
            val y = chartHeight - (bucket.orderCount / maxOrders) * chartHeight
            
            drawCircle(
                color = lineColor,
                center = Offset(x, y),
                radius = 3.dp.toPx()
            )
        }

        // Draw X Axis Labels
        val textPaint = Paint().apply {
            color = android.graphics.Color.GRAY
            textSize = 10.sp.toPx()
            textAlign = Paint.Align.CENTER
        }

        buckets.forEachIndexed { index, bucket ->
            // Thinning: if it looks like hours (0-23), show every 3 hours
            val label = bucket.label.replace("時", "")
            val isHour = label.toIntOrNull() != null
            val shouldDraw = if (isHour) {
                label.toInt() % 3 == 0
            } else {
                true // Draw all for days of week
            }

            if (shouldDraw) {
                val x = index * stepX + stepX / 2
                drawContext.canvas.nativeCanvas.drawText(
                    bucket.label,
                    x,
                    height - 5f, // Bottom padding
                    textPaint
                )
            }
        }
    }
}
