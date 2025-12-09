package com.example.restomaster.domain.repository

import com.example.restomaster.data.local.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    fun getStore(): Flow<StoreEntity?>
    suspend fun saveStore(store: StoreEntity)
}
