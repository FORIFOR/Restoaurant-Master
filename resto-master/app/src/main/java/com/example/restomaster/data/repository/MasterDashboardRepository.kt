package com.example.restomaster.data.repository

import com.example.restomaster.data.local.dao.CallRequestDao
import com.example.restomaster.data.local.dao.OrderDao
import com.example.restomaster.data.local.dao.OrderWithItems
import com.example.restomaster.data.local.dao.TableDao
import com.example.restomaster.data.local.entity.CallRequestEntity
import com.example.restomaster.data.local.entity.TableEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MasterDashboardRepository @Inject constructor(
    private val tableDao: TableDao,
    private val orderDao: OrderDao,
    private val callRequestDao: CallRequestDao
) {
    data class TableStatus(
        val table: TableEntity,
        val orders: List<OrderWithItems>,
        val callRequests: List<CallRequestEntity>
    )

    fun getDashboardData(storeId: Long): Flow<List<TableStatus>> {
        val tablesFlow = tableDao.getTablesFlow(storeId)
        val ordersFlow = orderDao.getActiveOrdersWithItems(storeId)
        val callsFlow = callRequestDao.getActiveCallRequests(storeId)

        return combine(tablesFlow, ordersFlow, callsFlow) { tables, orders, calls ->
            tables.map { table ->
                TableStatus(
                    table = table,
                    orders = orders.filter { it.order.tableId == table.id },
                    callRequests = calls.filter { it.tableId == table.id }
                )
            }
        }
    }

    suspend fun markOrderServed(orderId: Long) {
        orderDao.updateOrderItemsStatus(orderId, "served")
    }
}
