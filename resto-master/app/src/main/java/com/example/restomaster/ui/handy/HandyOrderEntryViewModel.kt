package com.example.restomaster.ui.handy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.domain.repository.MenuRepository
import com.example.restomaster.domain.repository.OrderRepository
import com.example.restomaster.data.local.entity.MenuItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartItem(
    val menuItem: MenuItemEntity,
    val quantity: Int
)

@HiltViewModel
class HandyOrderEntryViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _menuItems = MutableStateFlow<List<MenuItemEntity>>(emptyList())
    val menuItems: StateFlow<List<MenuItemEntity>> = _menuItems.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMenu()
    }

    private fun loadMenu() {
        viewModelScope.launch {
            // Assuming storeId is 1
            _menuItems.value = menuRepository.getPublicMenuForStore(1L)
        }
    }

    fun addToCart(menuItem: MenuItemEntity) {
        val currentCart = _cart.value.toMutableList()
        val existingItemIndex = currentCart.indexOfFirst { it.menuItem.id == menuItem.id }

        if (existingItemIndex != -1) {
            val existingItem = currentCart[existingItemIndex]
            currentCart[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentCart.add(CartItem(menuItem, 1))
        }
        _cart.value = currentCart
    }

    fun removeFromCart(menuItem: MenuItemEntity) {
        val currentCart = _cart.value.toMutableList()
        val existingItemIndex = currentCart.indexOfFirst { it.menuItem.id == menuItem.id }

        if (existingItemIndex != -1) {
            val existingItem = currentCart[existingItemIndex]
            if (existingItem.quantity > 1) {
                currentCart[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity - 1)
            } else {
                currentCart.removeAt(existingItemIndex)
            }
            _cart.value = currentCart
        }
    }

    fun submitOrder(tableId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orderItems = _cart.value.map { cartItem ->
                    com.example.restomaster.data.server.CreateOrderItem(
                        menuItemId = cartItem.menuItem.id,
                        quantity = cartItem.quantity,
                        memo = null
                    )
                }
                
                // Assuming storeId is 1 for now
                orderRepository.createOrderFromPublic(
                    storeId = 1,
                    tableId = tableId,
                    peopleCount = null, // Could add UI for this later
                    items = orderItems
                )
                
                _cart.value = emptyList()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
