package com.example.restomaster.domain.model

enum class TableStatus {
    ACTIVE,                // 通常
    CHECKOUT_REQUESTED,    // お会計要求（赤）
    PAYMENT_IN_PROGRESS,   // PayPay決済中
    PAID                   // 会計済（緑）
}
