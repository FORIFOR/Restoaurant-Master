package com.example.restomaster.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.restomaster.R
import com.example.restomaster.data.server.LocalHttpServer
import dagger.hilt.android.AndroidEntryPoint
import com.example.restomaster.util.AppLog
import javax.inject.Inject

/**
 * HTTPサーバをForeground Serviceとして常駐させる
 * テザリング時にお客さん用注文受付サーバを起動
 */
@AndroidEntryPoint
class HttpServerService : Service() {

    @Inject
    lateinit var localHttpServer: LocalHttpServer

    companion object {
        private const val TAG = "HttpServerService"
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "http_server_channel"
        private const val CHANNEL_NAME = "注文受付サーバ"
        const val ACTION_START = "START_SERVER"
        const val ACTION_STOP = "STOP_SERVER"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                AppLog.server("Starting HttpServerService")
                startForeground(NOTIFICATION_ID, createNotification())
                localHttpServer.start(port = 8080)
            }
            ACTION_STOP -> {
                AppLog.server("Stopping HttpServerService")
                localHttpServer.stop()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.d(TAG, "Service onDestroy")
        localHttpServer.stop()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "モバイルオーダー受付中"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("注文受付中")
            .setContentText("サーバが起動しています (Port: 8080)")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}
