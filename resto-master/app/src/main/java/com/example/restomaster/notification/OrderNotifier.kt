package com.example.restomaster.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.restomaster.R
import com.example.restomaster.ui.dashboard.OrderMonitorRow
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderNotifier @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "order_channel"

    private val toneGenerator by lazy {
        try {
            android.media.ToneGenerator(android.media.AudioManager.STREAM_NOTIFICATION, 100)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    init {
        createChannelIfNeeded()
    }

    private fun createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "新規注文・呼び出し",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "モバイルオーダーの新規注文、呼び出し、会計依頼の通知"
                enableVibration(true)
                enableLights(true)
            }
            nm.createNotificationChannel(channel)
        }
    }

    fun notifyNewOrder(row: OrderMonitorRow) {
        val title = "${row.tableNumber}番卓から注文"
        val text = row.itemsSummary.take(2).joinToString(" / ")

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        nm.notify(row.orderId.toInt(), notification)
        playNotificationSound()
    }

    fun notifyCheckout(tableNumber: Int, totalAmount: Int) {
        val title = "${tableNumber}番卓から会計依頼"
        val text = "合計 ¥$totalAmount"
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
        nm.notify((tableNumber * 100000 + totalAmount).hashCode(), notification)
        playNotificationSound()
    }

    fun notifyCall(tableNumber: Int) {
        val title = "${tableNumber}番卓から呼び出し"
        val text = "店員を呼んでいます"
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
        nm.notify((tableNumber * 100000 + 9999).hashCode(), notification)
        playNotificationSound()
    }

    private fun playNotificationSound() {
        try {
            toneGenerator?.startTone(android.media.ToneGenerator.TONE_PROP_BEEP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
