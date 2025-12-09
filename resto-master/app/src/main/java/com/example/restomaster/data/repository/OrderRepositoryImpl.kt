package com.example.restomaster.data.repository

import androidx.room.withTransaction
import com.example.restomaster.data.local.AppDatabase
import com.example.restomaster.data.local.dao.OrderDao
import com.example.restomaster.data.local.entity.CallRequestEntity
import com.example.restomaster.data.local.entity.OrderEntity
import com.example.restomaster.data.local.entity.OrderItemEntity
import com.example.restomaster.data.server.*
import com.example.restomaster.domain.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.restomaster.util.AppLog

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val database: AppDatabase
) : OrderRepository {
    override fun getActiveOrders(storeId: Long): Flow<List<OrderEntity>> = orderDao.getActiveOrders(storeId)
    override fun getActiveOrdersWithItems(storeId: Long): Flow<List<com.example.restomaster.data.local.dao.OrderWithItems>> = orderDao.getActiveOrdersWithItems(storeId)
    override fun getOrderItems(orderId: Long): Flow<List<OrderItemEntity>> = orderDao.getOrderItems(orderId)
    override suspend fun getOrderById(orderId: Long): OrderEntity? = orderDao.getOrderById(orderId)
    
    override fun getClosedOrders(storeId: Long): Flow<List<com.example.restomaster.data.local.dao.OrderWithItems>> = orderDao.getClosedOrders(storeId)
    override fun getTotalSales(storeId: Long): Flow<Long?> = orderDao.getTotalSales(storeId)
    override suspend fun addOrderItem(orderItem: OrderItemEntity) = orderDao.insertOrderItem(orderItem)
    override suspend fun updateOrder(order: OrderEntity) = orderDao.updateOrder(order)
    override suspend fun updateOrderItemsStatus(orderId: Long, newStatus: String) = orderDao.updateOrderItemsStatus(orderId, newStatus)
    override suspend fun updateOrderStatus(orderId: Long, newStatus: String) = orderDao.updateOrderStatus(orderId, newStatus)
    
    override fun getActiveCallRequests(storeId: Long): Flow<List<CallRequestEntity>> = orderDao.getActiveCallRequests(storeId)
    override suspend fun getCallRequestById(requestId: Long): CallRequestEntity? = orderDao.getCallRequestById(requestId)
    override suspend fun createCallRequest(request: CallRequestEntity) = orderDao.insertCallRequest(request)
    override suspend fun updateCallRequest(request: CallRequestEntity) = orderDao.updateCallRequest(request)
    
    /**
     * 公開API用: お客さんからの注文を作成
     */
    override suspend fun createOrderFromPublic(
        storeId: Long,
        tableId: Long,
        peopleCount: Int?,
        items: List<CreateOrderItem>
    ): Long = withContext(Dispatchers.IO) {
        database.withTransaction {
            AppLog.db("createOrderFromPublic tableId=$tableId items=${items.size}")
            // Order作成
            val orderId = orderDao.insertOrder(
                OrderEntity(
                    storeId = storeId,
                    tableId = tableId,
                    status = "open",
                    peopleCount = peopleCount,
                    createdAt = System.currentTimeMillis(),
                    closedAt = null
                )
            )
            
            // OrderItemsを追加
            val menuDao = database.menuDao()
            items.forEach { req ->
                val menuItem = menuDao.getMenuItem(req.menuItemId)
                if (menuItem != null) {
                    orderDao.insertOrderItem(
                        OrderItemEntity(
                            orderId = orderId,
                            menuItemId = req.menuItemId,
                            quantity = req.quantity,
                            unitPrice = menuItem.price,
                            status = "received",
                            memo = req.memo
                        )
                    )
                }
            }
            
            val total = items.sumOf { req ->
                val menuItem = menuDao.getMenuItem(req.menuItemId)
                (menuItem?.price ?: 0) * req.quantity
            }
            AppLog.db("orderCreated orderId=$orderId tableId=$tableId total=$total")

            orderId
        }
    }
    
    /**
     * 公開API用: テーブルの現在の注文状況を取得
     */
    override suspend fun getCurrentOrderStatusForTable(tableId: Long): CurrentOrderStatusResponse = withContext(Dispatchers.IO) {
        val table = database.tableDao().findById(tableId) ?: throw IllegalArgumentException("Table not found")
        val orders = orderDao.getActiveOrders(table.storeId).first().filter { it.tableId == tableId }

        val orderSummaries = orders.map { order ->
            val items = orderDao.getOrderItems(order.id).first()
            val itemSummaries = items.map { item ->
                val menuItem = database.menuDao().getMenuItem(item.menuItemId)
                OrderItemSummary(
                    name = menuItem?.name ?: "Unknown",
                    quantity = item.quantity,
                    unitPrice = item.unitPrice,
                    status = item.status
                )
            }

            OrderSummary(
                orderId = order.id,
                status = order.status,
                items = itemSummaries
            )
        }

        val totalAmount = orderSummaries.flatMap { it.items }
            .sumOf { it.quantity * it.unitPrice }

        val peopleCount = orders.firstOrNull { it.peopleCount != null }?.peopleCount
        val perPersonAmount = if (peopleCount != null && peopleCount > 0) {
            totalAmount / peopleCount
        } else null

        CurrentOrderStatusResponse(
            tableNumber = table.tableNumber,
            orders = orderSummaries,
            totalAmount = totalAmount,
            perPersonAmount = perPersonAmount
        )
    }
}
