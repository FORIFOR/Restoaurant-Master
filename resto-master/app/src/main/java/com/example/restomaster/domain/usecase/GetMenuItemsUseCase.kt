package com.example.restomaster.domain.usecase

import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMenuItemsUseCase @Inject constructor(
    private val repository: MenuRepository
) {
    operator fun invoke(storeId: Long): Flow<List<MenuItemEntity>> {
        return repository.getMenuItems(storeId)
    }
}
