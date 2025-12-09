package com.example.restomaster.domain.usecase

import com.example.restomaster.data.repository.ReceiptRepository
import com.example.restomaster.domain.model.ReceiptData
import javax.inject.Inject

/**
 * Use case for generating a receipt from a bill.
 * Returns an existing receipt if one exists, or creates a new one.
 */
class GenerateReceiptUseCase @Inject constructor(
    private val receiptRepository: ReceiptRepository
) {
    /**
     * Generate or retrieve a receipt for the given bill
     * @param billId The bill ID to generate a receipt for
     * @param storeId The store ID (defaults to 1)
     * @return ReceiptData if successful, null if bill not found
     */
    suspend operator fun invoke(billId: Long, storeId: Long = 1L): ReceiptData? {
        return receiptRepository.getOrCreateReceiptForBill(billId, storeId)
    }
}
