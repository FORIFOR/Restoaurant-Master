package com.example.restomaster.domain.usecase

import com.example.restomaster.data.local.entity.TableEntity
import com.example.restomaster.domain.repository.TableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTablesUseCase @Inject constructor(
    private val repository: TableRepository
) {
    operator fun invoke(storeId: Long): Flow<List<TableEntity>> {
        return repository.getTables(storeId)
    }
}
