package com.example.restomaster.ui.common

import java.text.NumberFormat
import java.util.Locale

private val jpCurrencyFormat: NumberFormat =
    NumberFormat.getCurrencyInstance(Locale.JAPAN).apply {
        maximumFractionDigits = 0 // Ensure no decimals for Yen
    }

fun formatYen(amount: Long): String = jpCurrencyFormat.format(amount)
fun formatYen(amount: Int): String = jpCurrencyFormat.format(amount)
fun formatYen(amount: Double): String = jpCurrencyFormat.format(amount)

fun formatDateRange(start: Long, end: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN)
    return "${sdf.format(java.util.Date(start))} 〜 ${sdf.format(java.util.Date(end))}"
}

fun translateDayName(dayName: String): String {
    return when (dayName.uppercase()) {
        "MONDAY", "MON" -> "月"
        "TUESDAY", "TUE" -> "火"
        "WEDNESDAY", "WED" -> "水"
        "THURSDAY", "THU" -> "木"
        "FRIDAY", "FRI" -> "金"
        "SATURDAY", "SAT" -> "土"
        "SUNDAY", "SUN" -> "日"
        else -> dayName
    }
}
