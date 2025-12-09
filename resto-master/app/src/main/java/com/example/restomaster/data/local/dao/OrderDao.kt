package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.example.restomaster.data.local.entity.CallRequestEntity
import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.data.local.entity.OrderEntity
import com.example.restomaster.data.local.entity.OrderItemEntity
import com.example.restomaster.data.local.entity.TableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE storeId = :storeId AND status != 'closed'")
    fun getActiveOrders(storeId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItems(orderId: Long): Flow<List<OrderItemEntity>>

    @Transaction
    @Query("SELECT * FROM orders WHERE storeId = :storeId AND status != 'closed' ORDER BY createdAt DESC")
    fun getActiveOrdersWithItems(storeId: Long): Flow<List<OrderWithItems>>
    
    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: Long): OrderEntity?

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItemsForOrder(orderId: Long): List<OrderItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItemEntity)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("UPDATE orders SET status = :newStatus WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, newStatus: String)

    @Query("UPDATE order_items SET status = :newStatus WHERE id = :orderItemId")
    suspend fun updateOrderItemStatus(orderItemId: Long, newStatus: String)

    @Query("UPDATE order_items SET status = :newStatus WHERE orderId = :orderId")
    suspend fun updateOrderItemsStatus(orderId: Long, newStatus: String)

    @Query("SELECT * FROM orders WHERE tableId = :tableId AND status != 'closed'")
    suspend fun getActiveOrdersForTable(tableId: Long): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE tableId = :tableId ORDER BY createdAt DESC")
    fun getOrdersForTable(tableId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE bill_id = :billId")
    suspend fun getOrdersByBillId(billId: Long): List<OrderEntity>

    // Sales History
    @Transaction
    @Query("SELECT * FROM orders WHERE storeId = :storeId AND status = 'closed' ORDER BY createdAt DESC")
    fun getClosedOrders(storeId: Long): Flow<List<OrderWithItems>>

    @Query("""
        SELECT SUM(oi.unitPrice * oi.quantity) 
        FROM orders o
        INNER JOIN order_items oi ON o.id = oi.orderId
        WHERE o.storeId = :storeId AND o.status = 'closed'
    """)
    fun getTotalSales(storeId: Long): Flow<Long?>

    // Call Requests
    @Query("SELECT * FROM call_requests WHERE storeId = :storeId AND status = 'new'")
    fun getActiveCallRequests(storeId: Long): Flow<List<CallRequestEntity>>

    @Query("SELECT * FROM call_requests WHERE id = :requestId")
    suspend fun getCallRequestById(requestId: Long): CallRequestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallRequest(request: CallRequestEntity)

    @Update
    suspend fun updateCallRequest(request: CallRequestEntity)

    @Query("DELETE FROM orders")
    suspend fun deleteAll()
}

/**
 * 注文1件に紐づく明細＋テーブルをまとめて扱うためのリレーション
 */
data class OrderItemWithMenu(
    @Embedded val item: OrderItemEntity,
    @Relation(
        parentColumn = "menuItemId",
        entityColumn = "id"
    )
    val menuItem: MenuItemEntity?
)

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(
        entity = OrderItemEntity::class,
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItemWithMenu>,
    @Relation(
        parentColumn = "tableId",
        entityColumn = "id"
    )
    val table: TableEntity
)
