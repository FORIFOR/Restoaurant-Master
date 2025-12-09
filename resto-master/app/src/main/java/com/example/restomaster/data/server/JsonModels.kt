package com.example.restomaster.data.server

import kotlinx.serialization.Serializable

/**
 * 公開API用DTOクラス
 * お客さん向けメニュー画面とJSON APIで使用
 */

/**
 * メニュー項目（お客さん向け）
 */
@Serializable
data class PublicMenuItemDto(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Int,
    val category: String,
    val imageUrl: String? = null,
    val isAiGenerated: Boolean = false,
    val isAvailable: Boolean = true,
    val allergens: List<String> = emptyList()  // ["egg", "milk"] など
)

/**
 * 注文作成リクエスト
 */
@Serializable
data class CreateOrderRequest(
    val tableToken: String,
    val peopleCount: Int? = null,
    val items: List<CreateOrderItem>
)

/**
 * 注文アイテム
 */
@Serializable
data class CreateOrderItem(
    val menuItemId: Long,
    val quantity: Int,
    val memo: String? = null
)

/**
 * 注文作成レスポンス
 */
@Serializable
data class CreateOrderResponse(
    val orderId: Long,
    val status: String
)

/**
 * 現在の注文状況レスポンス
 */
@Serializable
data class CurrentOrderStatusResponse(
    val tableNumber: Int,
    val orders: List<OrderSummary>,
    val totalAmount: Int,
    val perPersonAmount: Int? = null
)

/**
 * 注文サマリー
 */
@Serializable
data class OrderSummary(
    val orderId: Long,
    val status: String,
    val items: List<OrderItemSummary>
)

/**
 * 注文アイテムサマリー
 */
@Serializable
data class OrderItemSummary(
    val name: String,
    val quantity: Int,
    val unitPrice: Int,
    val status: String   // received / in_progress / served
)
