package com.example.restomaster.data.server

import com.example.restomaster.domain.repository.MenuRepository
import com.example.restomaster.domain.repository.OrderRepository
import com.example.restomaster.data.local.dao.TableDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.assertEquals

/**
 * Ktorルートの最小テスト。フェイク依存を使い、HTTPレイヤーが動くか確認。
 */
class PublicRoutesTest {

    private val fakeMenuRepo = FakeMenuRepository()
    private val fakeOrderRepo = FakeOrderRepository()
    private val fakeTableDao = FakeTableDao()

    @Test
    fun `GET menu returns items for valid token`() = testApplication {
        application {
            routing {
                publicRoutes(
                    menuRepository = fakeMenuRepo,
                    orderRepository = fakeOrderRepo,
                    tableDao = fakeTableDao
                )
            }
        }

        // Arrange
        runBlocking {
            fakeTableDao.add(token = "abc", storeId = 1L)
            fakeMenuRepo.setMenu(
                storeId = 1L,
                items = listOf(
                    PublicMenuItemDto(
                        id = 1,
                        name = "Test",
                        description = "desc",
                        price = 500,
                        category = "Food",
                        imageUrl = null,
                        allergens = emptyList()
                    )
                )
            )
        }

        // Act
        val response = client.get("/api/public/menu?table_token=abc")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assert(body.contains("\"name\":\"Test\""))
    }

    @Test
    fun `POST orders returns 404 for invalid token`() = testApplication {
        application {
            routing {
                publicRoutes(
                    menuRepository = fakeMenuRepo,
                    orderRepository = fakeOrderRepo,
                    tableDao = fakeTableDao
                )
            }
        }

        val response = client.post("/api/public/orders") {
            contentType(ContentType.Application.Json)
            setBody("""{"tableToken":"nope","peopleCount":null,"items":[]}""")
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}

// --- Simple fakes for test ---
private class FakeMenuRepository : MenuRepository {
    private val storeMenu = mutableMapOf<Long, List<com.example.restomaster.data.local.entity.MenuItemEntity>>()
    private val allergens = mutableMapOf<Long, List<String>>()

    fun setMenu(storeId: Long, items: List<PublicMenuItemDto>) {
        storeMenu[storeId] = items.map {
            com.example.restomaster.data.local.entity.MenuItemEntity(
                id = it.id,
                storeId = storeId,
                name = it.name,
                description = it.description,
                price = it.price,
                category = it.category,
                imagePath = null,
                isAvailable = true
            )
        }
    }

    override fun getMenuItems(storeId: Long) = throw NotImplementedError()
    override suspend fun getMenuItem(id: Long) = throw NotImplementedError()
    override suspend fun addMenuItem(item: com.example.restomaster.data.local.entity.MenuItemEntity) = Unit
    override suspend fun updateMenuItem(item: com.example.restomaster.data.local.entity.MenuItemEntity) = Unit
    override suspend fun deleteMenuItem(id: Long) = Unit
    override suspend fun getPublicMenuForStore(storeId: Long): List<com.example.restomaster.data.local.entity.MenuItemEntity> =
        storeMenu[storeId] ?: emptyList()
    override suspend fun getAllergensForItem(menuItemId: Long): List<String> =
        allergens[menuItemId] ?: emptyList()
    override suspend fun getAllergens() = emptyList<com.example.restomaster.data.local.entity.AllergenEntity>()
    override suspend fun getMenuItemAllergens(menuItemId: Long) = emptyList<com.example.restomaster.data.local.entity.MenuItemAllergenEntity>()
    override suspend fun updateMenuItemAllergens(menuItemId: Long, allergens: List<com.example.restomaster.data.local.entity.MenuItemAllergenEntity>) = Unit
    override suspend fun initializeAllergensIfNeeded() = Unit
}

private class FakeOrderRepository : OrderRepository {
    override fun getActiveOrders(storeId: Long) = throw NotImplementedError()
    override fun getOrderItems(orderId: Long) = throw NotImplementedError()
    override suspend fun getOrderById(orderId: Long) = null
    override suspend fun createOrderFromPublic(storeId: Long, tableId: Long, peopleCount: Int?, items: List<CreateOrderItem>): Long = 1L
    override suspend fun getCurrentOrderStatusForTable(tableId: Long) = throw NotImplementedError()
    override suspend fun addOrderItem(orderItem: com.example.restomaster.data.local.entity.OrderItemEntity) = Unit
    override suspend fun updateOrder(order: com.example.restomaster.data.local.entity.OrderEntity) = Unit
    override fun getActiveCallRequests(storeId: Long) = throw NotImplementedError()
    override suspend fun createCallRequest(request: com.example.restomaster.data.local.entity.CallRequestEntity) = Unit
    override suspend fun updateCallRequest(request: com.example.restomaster.data.local.entity.CallRequestEntity) = Unit
}

private class FakeTableDao : TableDao {
    private val map = mutableMapOf<String, com.example.restomaster.data.local.entity.TableEntity>()
    suspend fun add(token: String, storeId: Long) {
        map[token] = com.example.restomaster.data.local.entity.TableEntity(
            id = 1L,
            storeId = storeId,
            tableNumber = 1,
            token = token,
            isActive = true
        )
    }
    override fun getTablesFlow(storeId: Long) = throw NotImplementedError()
    override suspend fun findByToken(token: String) = map[token]
    override suspend fun findById(id: Long) = map.values.firstOrNull { it.id == id }
    override suspend fun insert(table: com.example.restomaster.data.local.entity.TableEntity) = 1L
    override suspend fun update(table: com.example.restomaster.data.local.entity.TableEntity) = Unit
    override suspend fun delete(table: com.example.restomaster.data.local.entity.TableEntity) = Unit
    override suspend fun getAllTables(): List<com.example.restomaster.data.local.entity.TableEntity> = map.values.toList()
    override fun observeById(id: Long) = throw NotImplementedError()
    override fun getTableById(id: Long) = throw NotImplementedError()
}
