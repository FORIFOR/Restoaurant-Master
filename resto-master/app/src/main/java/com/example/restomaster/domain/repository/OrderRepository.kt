package com.example.restomaster.domain.repository

import com.example.restomaster.data.local.entity.CallRequestEntity
import com.example.restomaster.data.local.entity.OrderEntity
import com.example.restomaster.data.local.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getActiveOrders(storeId: Long): Flow<List<OrderEntity>>
    fun getActiveOrdersWithItems(storeId: Long): Flow<List<com.example.restomaster.data.local.dao.OrderWithItems>>
    fun getOrderItems(orderId: Long): Flow<List<OrderItemEntity>>
    suspend fun getOrderById(orderId: Long): OrderEntity?
    
    // Sales History
    fun getClosedOrders(storeId: Long): Flow<List<com.example.restomaster.data.local.dao.OrderWithItems>>
    fun getTotalSales(storeId: Long): Flow<Long?>
    
    // 公開API用
    suspend fun createOrderFromPublic(
        storeId: Long,
        tableId: Long,
        peopleCount: Int?,
        items: List<com.example.restomaster.data.server.CreateOrderItem>
    ): Long
    
    suspend fun getCurrentOrderStatusForTable(tableId: Long): com.example.restomaster.data.server.CurrentOrderStatusResponse
    suspend fun addOrderItem(orderItem: OrderItemEntity)
    suspend fun updateOrder(order: OrderEntity)
    suspend fun updateOrderItemsStatus(orderId: Long, newStatus: String)
    suspend fun updateOrderStatus(orderId: Long, newStatus: String)
    
    fun getActiveCallRequests(storeId: Long): Flow<List<CallRequestEntity>>
    suspend fun getCallRequestById(requestId: Long): CallRequestEntity?
    suspend fun createCallRequest(request: CallRequestEntity)
    suspend fun updateCallRequest(request: CallRequestEntity)
}
