package com.example.restomaster.domain.repository

import com.example.restomaster.data.local.entity.AllergenEntity
import com.example.restomaster.data.local.entity.MenuItemAllergenEntity
import com.example.restomaster.data.local.entity.MenuItemEntity
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getMenuItems(storeId: Long): Flow<List<MenuItemEntity>>
    suspend fun getMenuItem(id: Long): MenuItemEntity?
    suspend fun addMenuItem(item: MenuItemEntity)
    suspend fun updateMenuItem(item: MenuItemEntity)
    suspend fun deleteMenuItem(id: Long)
    
    // 公開API用
    suspend fun getPublicMenuForStore(storeId: Long): List<MenuItemEntity>
    suspend fun getAllergensForItem(menuItemId: Long): List<String>
    
    // Allergen methods
    suspend fun getAllergens(): List<AllergenEntity>
    suspend fun getMenuItemAllergens(menuItemId: Long): List<MenuItemAllergenEntity>
    suspend fun updateMenuItemAllergens(menuItemId: Long, allergens: List<MenuItemAllergenEntity>)
    suspend fun initializeAllergensIfNeeded()
    
    // AI Enhancement
    suspend fun enhanceMenuImage(file: java.io.File): java.io.File?
}
