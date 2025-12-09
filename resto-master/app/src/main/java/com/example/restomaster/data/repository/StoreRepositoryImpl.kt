package com.example.restomaster.data.repository

import com.example.restomaster.data.local.dao.StoreDao
import com.example.restomaster.data.local.entity.StoreEntity
import com.example.restomaster.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeDao: StoreDao
) : StoreRepository {
    override fun getStore(): Flow<StoreEntity?> = storeDao.getStoreFlow()
    override suspend fun saveStore(store: StoreEntity) = storeDao.insertStore(store)
}
