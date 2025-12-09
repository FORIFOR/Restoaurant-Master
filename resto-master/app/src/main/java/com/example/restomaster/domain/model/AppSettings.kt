package com.example.restomaster.domain.model

/**
 * Application settings data class
 */
data class AppSettings(
    val mode: AppMode = AppMode.HOST,
    val hostIp: String = "",
    val hostPort: Int = 8080
)
