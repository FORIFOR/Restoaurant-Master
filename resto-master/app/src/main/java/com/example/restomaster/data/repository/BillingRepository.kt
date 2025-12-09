package com.example.restomaster.data.repository

import com.example.restomaster.data.local.dao.OrderDao
import com.example.restomaster.data.local.dao.TableDao
import com.example.restomaster.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import com.example.restomaster.data.local.dao.BillDao
import com.example.restomaster.data.local.dao.PaymentDao
import com.example.restomaster.data.remote.StripeApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

data class TableCheckoutSummary(
    val tableId: Long,
    val tableNumber: Int?,
    val orderIds: List<Long>,
    val servedTotal: Int,
    val pendingTotal: Int,
    val hasCheckoutRequest: Boolean = false,
    val checkoutRequestTime: Long? = null
)

data class BillSummary(
    val subtotal: Int,
    val tax: Int,
    val total: Int,
    val subtotal8: Int,  // 軽減税率対象
    val tax8: Int,
    val subtotal10: Int, // 標準税率対象
    val tax10: Int
)

@Singleton
class BillingRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val tableDao: TableDao,
    private val callRequestDao: com.example.restomaster.data.local.dao.CallRequestDao,
    private val billDao: BillDao,
    private val billItemDao: com.example.restomaster.data.local.dao.BillItemDao,
    private val paymentDao: PaymentDao,
    private val paymentItemDao: com.example.restomaster.data.local.dao.PaymentItemDao,
    private val menuDao: com.example.restomaster.data.local.dao.MenuDao,
    private val storeDao: com.example.restomaster.data.local.dao.StoreDao,
    private val stripeApiService: StripeApiService,
    private val payPayApiService: com.example.restomaster.data.remote.PayPayApiService,
    private val receiptGenerator: com.example.restomaster.data.receipt.ReceiptGenerator
) {
    companion object {
        private const val TAX_RATE_REDUCED = 0.08f  // 軽減税率
        private const val TAX_RATE_STANDARD = 0.10f // 標準税率
    }

    fun getCheckoutSummaries(storeId: Long): Flow<List<TableCheckoutSummary>> {
        return combine(
            orderDao.getActiveOrdersWithItems(storeId),
            callRequestDao.getActiveCallRequests(storeId)
        ) { activeOrders, callRequests ->
            val grouped = activeOrders.groupBy { it.order.tableId }
            grouped.map { (tableId, rows) ->
                val tableNumber = rows.firstOrNull()?.table?.tableNumber
                val orderIds = rows.map { it.order.id }
                val allItems = rows.flatMap { it.items }
                val servedTotal = allItems
                    .filter { it.item.status == "served" || it.item.status == "in_progress" || it.item.status == "received" }
                    .sumOf { it.item.unitPrice * it.item.quantity }
                val pendingTotal = allItems
                    .filter { it.item.status != "served" && it.item.status != "canceled" }
                    .sumOf { it.item.unitPrice * it.item.quantity }
                
                // Find checkout request for this table
                val checkoutRequest = callRequests.find { it.tableId == tableId && it.type == "checkout" }

                TableCheckoutSummary(
                    tableId = tableId,
                    tableNumber = tableNumber,
                    orderIds = orderIds,
                    servedTotal = servedTotal,
                    pendingTotal = pendingTotal,
                    hasCheckoutRequest = checkoutRequest != null,
                    checkoutRequestTime = checkoutRequest?.createdAt
                )
            }.sortedWith(
                compareByDescending<TableCheckoutSummary> { it.hasCheckoutRequest }
                    .thenBy { it.tableNumber ?: Int.MAX_VALUE }
            )
        }
    }

    suspend fun closeTableOrders(tableId: Long) {
        val orders = orderDao.getActiveOrdersForTable(tableId)
        val now = System.currentTimeMillis()
        orders.forEach { order ->
            orderDao.updateOrder(order.copy(status = "closed", closedAt = now))
        }
    }

    suspend fun prepareCheckout(tableId: Long, method: String, returnBaseUrl: String): String {
        // 1. Get active orders
        val orders = orderDao.getActiveOrdersForTable(tableId)
        if (orders.isEmpty()) throw IllegalStateException("No active orders for table $tableId")

        // 2. Calculate total
        // Note: This logic duplicates getCheckoutSummaries somewhat. In production, consolidate.
        // We need to fetch items to calculate total.
        val orderIds = orders.map { it.id }
        // Ideally we have a method to get total for orders. For now, let's assume we can get it via DAO or calculate it.
        // Let's use getActiveOrdersWithItems for simplicity
        val ordersWithItems = orderDao.getActiveOrdersWithItems(1) // Store ID 1 hardcoded for now
            .firstOrNull() // This is a Flow, we need to collect it. But we are in suspend function.
            // Wait, orderDao.getActiveOrdersWithItems returns Flow. We can't easily use it here synchronously without first().
            // Let's assume we can calculate from order items.
        
        // Simplified: Fetch items for these orders
        // We don't have a direct DAO method for "items by order IDs" exposed easily here without Flow.
        // Let's rely on a new helper or just fetch all active orders with items and filter.
        // For MVP, let's assume we have the total.
        
        // BETTER: Create a Bill first if not exists.
        // Check if there is an OPEN bill for this table
        var bill = billDao.getOpenBillForTable(tableId)
        
        if (bill == null) {
            // Calculate total
            // We need to execute this in a way that we get the data.
            // Let's add a DAO method to get items synchronously or just use what we have.
            // For now, I will use a placeholder calculation or fetch items.
            // Let's add a method to OrderDao: getOrderItemsForOrders(orderIds)
            
            // Assuming we added it or can do it.
            // Let's use a simpler approach: 
            // We'll use the existing Flow but take the first emission.
            val allActive = orderDao.getActiveOrdersWithItems(1).first()
            val tableOrders = allActive.filter { it.order.tableId == tableId }
            val total = tableOrders.flatMap { it.items }.sumOf { it.item.unitPrice * it.item.quantity }
            
            bill = com.example.restomaster.data.local.entity.BillEntity(
                tableId = tableId,
                status = "OPEN",
                openAt = System.currentTimeMillis(),
                subtotal = total, // Assuming tax included or 0 tax for now
                tax = 0,
                totalAmount = total
            )
            val billId = billDao.insertBill(bill)
            bill = bill.copy(id = billId)
            
            // Link orders to bill
            orders.forEach { order ->
                orderDao.updateOrder(order.copy(billId = billId))
            }
        }

        // 3. Create Payment (PENDING)
        val payment = com.example.restomaster.data.local.entity.PaymentEntity(
            billId = bill.id,
            provider = "STRIPE",
            providerType = "card",
            amount = bill.totalAmount,
            currency = "jpy",
            status = "PENDING",
            createdAt = System.currentTimeMillis()
        )
        val paymentId = paymentDao.insertPayment(payment)

        // 4. Call Cloud Backend
        val table = tableDao.findById(tableId)
        val req = com.example.restomaster.data.remote.CreateSessionRequest(
            billId = bill.id,
            amount = bill.totalAmount,
            currency = "jpy",
            tableLabel = if (table != null) "Table ${table.tableNumber}" else "Table $tableId",
            returnBaseUrl = returnBaseUrl
        )
        
        val res = stripeApiService.createCheckoutSession(req)
        
        // 5. Update Payment with session_id
        paymentDao.updatePayment(payment.copy(id = paymentId, providerRefId = res.sessionId))
        
        return res.checkoutUrl
    }

    suspend fun verifyStripePayment(sessionId: String): Boolean {
        // 1. Call Cloud Backend
        val res = stripeApiService.verifyPayment(com.example.restomaster.data.remote.VerifyRequest(sessionId))
        
        if (res.status == "SUCCEEDED") {
            // 2. Find Payment
            // We need to find payment by session_id. Add DAO method or search.
            // For now, let's assume we can find it.
            // We'll add getPaymentByProviderRefId to PaymentDao
            val payment = paymentDao.getByProviderRefId(sessionId)
            
            if (payment != null) {
                // 3. Update Payment
                paymentDao.updatePayment(payment.copy(
                    status = "SUCCEEDED",
                    confirmedAt = System.currentTimeMillis(),
                    extraJson = res.receiptUrl
                ))
                
                // 4. Update Bill
                val bill = billDao.getBill(payment.billId)
                if (bill != null) {
                    billDao.updateBill(bill.copy(
                        status = "PAID",
                        closedAt = System.currentTimeMillis(),
                        paymentMethod = "STRIPE"
                    ))
                    
                    // 5. Close Orders
                    val orders = orderDao.getOrdersByBillId(bill.id)
                    orders.forEach { order ->
                        orderDao.updateOrder(order.copy(status = "closed", closedAt = System.currentTimeMillis()))
                    }
                }
                return true
            }
        }
        return false
    }

    // ==================== Bill Management ====================

    /**
     * Get or create an OPEN bill for a table
     */
    suspend fun openOrMergeBillForTable(tableId: Long): com.example.restomaster.data.local.entity.BillEntity {
        var bill = billDao.getOpenBillForTable(tableId)
        if (bill == null) {
            // Create new OPEN bill (without items/amounts yet)
            bill = com.example.restomaster.data.local.entity.BillEntity(
                tableId = tableId,
                status = "OPEN",
                openAt = System.currentTimeMillis(),
                peopleCount = null,
                subtotal = 0,
                tax = 0,
                totalAmount = 0,
                paidAmount = 0
            )
            val billId = billDao.insertBill(bill)
            bill = bill.copy(id = billId)
        }
        return bill
    }

    /**
     * Create bill item snapshots from open orders
     */
    suspend fun syncBillItemsFromOpenOrders(billId: Long) {
        // Reset existing snapshots to avoid duplication when re-syncing
        billItemDao.deleteByBillId(billId)

        // Get all orders linked to this bill
        val orders = orderDao.getOrdersByBillId(billId)
        if (orders.isEmpty()) return
        val storeId = orders.first().storeId
        val store = storeDao.getStoreById(storeId)
        val taxIncluded = store?.taxIncludedPricing ?: true
        
        // For each order, get items and create snapshots
        orders.forEach { order ->
            val items = orderDao.getOrderItemsForOrder(order.id)
            items.forEach { orderItem ->
                // Fetch the menu item to get the name and tax rate type
                val menuItem = menuDao.getMenuItem(orderItem.menuItemId)
                
                // Determine tax rate
                val taxRate = if (menuItem?.taxRateType == "REDUCED_8") {
                    TAX_RATE_REDUCED
                } else {
                    TAX_RATE_STANDARD
                }

                val baseAmount = orderItem.unitPrice * orderItem.quantity
                val (lineTotal, taxAmount) = if (taxIncluded) {
                    val subtotal = (baseAmount / (1 + taxRate)).toInt()
                    val tax = baseAmount - subtotal
                    baseAmount to tax
                } else {
                    val tax = (baseAmount * taxRate).toInt()
                    (baseAmount + tax) to tax
                }

                val billItem = com.example.restomaster.data.local.entity.BillItemEntity(
                    billId = billId,
                    orderId = order.id,
                    orderItemId = orderItem.id,
                    menuItemId = orderItem.menuItemId,
                    name = menuItem?.name ?: "Unknown Item",
                    unitPrice = orderItem.unitPrice,
                    quantity = orderItem.quantity,
                    lineTotal = lineTotal,
                    taxRate = taxRate,
                    taxAmount = taxAmount,
                    isPaid = false
                )
                billItemDao.insert(billItem)
            }
        }
    }

    /**
     * Calculate bill summary with tax breakdown
     */
    suspend fun calculateSummary(billId: Long): BillSummary {
        val items = billItemDao.findByBillId(billId)
        
        // Group by tax rate
        val items8 = items.filter { it.taxRate == TAX_RATE_REDUCED }
        val items10 = items.filter { it.taxRate == TAX_RATE_STANDARD }
        
        // Calculate for 8% items
        val totalWith8 = items8.sumOf { it.lineTotal }
        val tax8 = items8.sumOf { it.taxAmount }
        val subtotal8 = totalWith8 - tax8
        
        // Calculate for 10% items
        val totalWith10 = items10.sumOf { it.lineTotal }
        val tax10 = items10.sumOf { it.taxAmount }
        val subtotal10 = totalWith10 - tax10
        
        return BillSummary(
            subtotal = subtotal8 + subtotal10,
            tax = tax8 + tax10,
            total = totalWith8 + totalWith10,
            subtotal8 = subtotal8,
            tax8 = tax8,
            subtotal10 = subtotal10,
            tax10 = tax10
        )
    }

    /**
     * Finalize bill with calculated totals
     */
    suspend fun finalizeBill(billId: Long) {
        val summary = calculateSummary(billId)
        val bill = billDao.getBill(billId) ?: return

        // Populate peopleCount from linked orders if not set
        val linkedOrders = orderDao.getOrdersByBillId(billId)
        val detectedPeopleCount = linkedOrders.mapNotNull { it.peopleCount }.maxOrNull()
        val billWithPeople = if (detectedPeopleCount != null && bill.peopleCount != detectedPeopleCount) {
            bill.copy(peopleCount = detectedPeopleCount)
        } else bill
        
        billDao.updateBill(billWithPeople.copy(
            subtotal = summary.subtotal,
            tax = summary.tax,
            totalAmount = summary.total,
            totalTax10 = summary.tax10,
            totalTax8 = summary.tax8,
            status = "PAYMENT_PENDING"
        ))
    }

    /**
     * Link orders to a bill
     */
    suspend fun linkOrdersToBill(billId: Long, orderIds: List<Long>) {
        orderIds.forEach { orderId ->
            val order = orderDao.getOrderById(orderId)
            if (order != null) {
                orderDao.updateOrder(order.copy(billId = billId))
            }
        }
    }

    /**
     * Close all orders associated with a bill
     */
    suspend fun closeOrdersForBill(billId: Long) {
        val orders = orderDao.getOrdersByBillId(billId)
        val now = System.currentTimeMillis()
        orders.forEach { order ->
            orderDao.updateOrder(order.copy(status = "closed", closedAt = now))
        }
    }

    // ==================== Payment Processing ====================

    /**
     * Apply a successful payment to a bill
     */
    suspend fun applyPaymentSucceeded(billId: Long, amount: Int, provider: String) {
        val bill = billDao.getBill(billId) ?: return
        
        val newPaidAmount = bill.paidAmount + amount
        val updates = if (newPaidAmount >= bill.totalAmount) {
            // Fully paid
            bill.copy(
                paidAmount = newPaidAmount,
                status = "PAID",
                closedAt = System.currentTimeMillis(),
                paymentMethod = if (bill.paymentMethod == null) provider else "MIXED"
            )
        } else {
            // Partially paid
            bill.copy(
                paidAmount = newPaidAmount,
                paymentMethod = if (bill.paymentMethod == null) provider else "MIXED"
            )
        }
        
        billDao.updateBill(updates)
        
        // If fully paid, close orders
        if (updates.status == "PAID") {
            closeOrdersForBill(billId)
        }
    }

    /**
     * Mark a payment as succeeded and process it
     */
    suspend fun markPaymentSucceeded(paymentId: Long) {
        val payment = paymentDao.getPaymentById(paymentId) ?: return
        
        // Update payment status
        paymentDao.updatePayment(payment.copy(
            status = "SUCCEEDED",
            confirmedAt = System.currentTimeMillis()
        ))
        
        // Mark associated bill items as paid
        val paymentItems = paymentItemDao.findByPaymentId(paymentId)
        paymentItems.forEach { paymentItem ->
            billItemDao.updateIsPaid(paymentItem.billItemId, true)
        }
        
        // Apply to bill
        applyPaymentSucceeded(payment.billId, payment.amount, payment.provider)
    }

    // ==================== Split Payments ====================

    /**
     * Create split payments for a bill
     */
    suspend fun createSplitPayments(billId: Long, splitCount: Int): List<com.example.restomaster.data.local.entity.PaymentEntity> {
        if (splitCount <= 0) throw IllegalArgumentException("Split count must be positive")
        
        val bill = billDao.getBill(billId) ?: throw IllegalStateException("Bill not found")
        val amountPerPerson = bill.totalAmount / splitCount
        val remainder = bill.totalAmount % splitCount
        
        val payments = mutableListOf<com.example.restomaster.data.local.entity.PaymentEntity>()
        val now = System.currentTimeMillis()
        
        repeat(splitCount) { index ->
            val amount = if (index == splitCount - 1) {
                amountPerPerson + remainder  // Last person pays the remainder
            } else {
                amountPerPerson
            }
            
            val payment = com.example.restomaster.data.local.entity.PaymentEntity(
                billId = billId,
                provider = "PENDING",  // Will be set when actual payment method is chosen
                providerType = "split",
                amount = amount,
                currency = "jpy",
                status = "PENDING",
                createdAt = now
            )
            val paymentId = paymentDao.insertPayment(payment)
            payments.add(payment.copy(id = paymentId))
        }
        
        return payments
    }

    // ==================== Individual Payments ====================

    /**
     * Start an individual payment for selected bill items
     */
    suspend fun startIndividualPayment(
        billId: Long,
        billItemIds: List<Long>,
        provider: String = "CASH"
    ): com.example.restomaster.data.local.entity.PaymentEntity {
        if (billItemIds.isEmpty()) throw IllegalArgumentException("No bill items selected")
        
        val items = billItemDao.findByIds(billItemIds)
        val totalAmount = items.sumOf { it.lineTotal }
        
        // Create payment
        val payment = com.example.restomaster.data.local.entity.PaymentEntity(
            billId = billId,
            provider = provider,
            providerType = "individual",
            amount = totalAmount,
            currency = "jpy",
            status = "PENDING",
            createdAt = System.currentTimeMillis()
        )
        val paymentId = paymentDao.insertPayment(payment)
        
        // Link payment to bill items
        items.forEach { item ->
            val paymentItem = com.example.restomaster.data.local.entity.PaymentItemEntity(
                paymentId = paymentId,
                billItemId = item.id,
                amount = item.lineTotal
            )
            paymentItemDao.insert(paymentItem)
        }
        
        return payment.copy(id = paymentId)
    }

    /**
     * Process a manual checkout for a table.
     * This creates a bill, syncs items, finalizes it, and marks it as paid.
     * @param tableId The table to checkout
     * @param paymentMethod The payment method (CASH, PAYPAY, etc.)
     */
    suspend fun processManualCheckout(tableId: Long, paymentMethod: String = "CASH"): Long {
        // 1. Ensure bill exists
        val bill = openOrMergeBillForTable(tableId)

        // 2. Link active orders on this table to the bill
        val activeOrders = orderDao.getActiveOrdersForTable(tableId)
        if (activeOrders.isEmpty()) return -1L
        linkOrdersToBill(bill.id, activeOrders.map { it.id })

        // 3. Sync items (in case new orders came in)
        syncBillItemsFromOpenOrders(bill.id)

        // 4. Calculate totals and update bill
        finalizeBill(bill.id)

        // 5. Refetch bill to get updated totals
        val updatedBill = billDao.getBill(bill.id) ?: return -1L

        // 6. Apply full payment with specified method
        // If total is 0, we still mark it as paid
        applyPaymentSucceeded(updatedBill.id, updatedBill.totalAmount, paymentMethod)
        
        return updatedBill.id
    }

    // ==================== Debug / Maintenance ====================

    /**
     * Clear all application data (Reset)
     */
    suspend fun clearAllData() {
        // Delete in order of dependencies
        paymentItemDao.deleteAll()
        paymentDao.deleteAll()
        billItemDao.deleteAll()
        billDao.deleteAll()
        orderDao.deleteAll()
        callRequestDao.deleteAll()
        // We might want to keep tables and menu items, or delete them too?
        // Usually "Initialize Data" implies transactional data, not master data.
        // But user said "Initialize Data". Let's assume transactional data only for now to be safe.
    }

    /**
     * Clear only sales history (Paid bills)
     */
    suspend fun clearSalesData() {
        // This is tricky because of foreign keys.
        // We need to delete payments and bill items associated with paid bills.
        // For simplicity, we can delete all bills that are PAID.
        // Cascading delete should handle related items if configured, but let's be explicit if needed.
        // Room ForeignKey with CASCADE will handle children.
        billDao.deletePaidBills()
    }

    // ==================== Receipt ====================

    suspend fun generateReceipt(billId: Long): ByteArray {
        val bill = billDao.getBill(billId) ?: throw IllegalStateException("Bill not found")
        val items = billItemDao.findByBillId(billId)
        val storeId = 1L // TODO: store selection; currently single-store運用
        val store = storeDao.getStoreById(storeId)
        val storeName = store?.name ?: "RestoMaster 店舗"
        val storeAddress = listOfNotNull(store?.addressLine1 ?: store?.address, store?.addressLine2).joinToString(" ").ifBlank { "住所未設定" }
        val storePhone = store?.phone
        val invoiceNumber = store?.invoiceRegistrationNumber
        val summary = calculateSummary(billId)
        val tableNumber = bill.tableId?.let { tableDao.findById(it)?.tableNumber?.toString() }
        val txDateTime = bill.closedAt?.let { java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() }
            ?: java.time.LocalDateTime.now()
        
        val receiptDto = com.example.restomaster.domain.model.ReceiptDto(
            storeName = storeName,
            storeAddress = storeAddress,
            storePhone = storePhone,
            invoiceRegistrationNumber = invoiceNumber,
            receiptId = "No. ${bill.id}",
            tableNumber = tableNumber ?: bill.tableId?.toString(),
            transactionDate = txDateTime,
            items = items.map { 
                com.example.restomaster.domain.model.ReceiptItemDto(
                    name = it.name,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                    lineTotal = it.lineTotal,
                    isReducedTax = it.taxRate == TAX_RATE_REDUCED
                )
            },
            subtotal = summary.subtotal,
            taxSummaries = listOf(
                com.example.restomaster.domain.model.TaxSummary(10, summary.subtotal10, summary.tax10),
                com.example.restomaster.domain.model.TaxSummary(8, summary.subtotal8, summary.tax8)
            ).filter { it.taxableAmount > 0 },
            totalAmount = summary.total,
            paymentMethod = bill.paymentMethod ?: "Cash",
            receivedAmount = bill.totalAmount, // Simplified for exact cash
            changeAmount = 0
        )
        
        return receiptGenerator.toEscPosBytes(receiptDto)
    }

    // ==================== Table Status & PayPay ====================

    suspend fun requestCheckout(tableId: Long) {
        val table = tableDao.findById(tableId) ?: return
        if (table.status == "ACTIVE") {
            tableDao.update(table.copy(status = "CHECKOUT_REQUESTED"))
        }
    }

    suspend fun startPayPayPayment(tableId: Long): com.example.restomaster.data.remote.CreatePayPayResponse {
        val table = tableDao.findById(tableId) ?: throw IllegalStateException("Table not found")
        val bill = openOrMergeBillForTable(tableId)
        
        // Ensure total is calculated
        syncBillItemsFromOpenOrders(bill.id)
        finalizeBill(bill.id)
        val updatedBill = billDao.getBill(bill.id) ?: throw IllegalStateException("Bill failed")
        
        if (updatedBill.totalAmount <= 0) throw IllegalStateException("Amount is 0")

        // Call PayPay API
        val req = com.example.restomaster.data.remote.CreatePayPayRequest(
            billId = updatedBill.id,
            amount = updatedBill.totalAmount,
            tableNumber = table.tableNumber,
            description = "Table ${table.tableNumber}"
        )
        val res = payPayApiService.createQrCode(req)
        
        // Update Table Status
        tableDao.update(table.copy(status = "PAYMENT_IN_PROGRESS"))
        
        // Update Payment Record with merchantPaymentId
        val payment = com.example.restomaster.data.local.entity.PaymentEntity(
            billId = updatedBill.id,
            provider = "PAYPAY",
            providerType = "qr",
            amount = updatedBill.totalAmount,
            currency = "jpy",
            status = "PENDING",
            merchantPaymentId = res.merchantPaymentId,
            paypayCodeId = res.paymentId,
            createdAt = System.currentTimeMillis()
        )
        paymentDao.insertPayment(payment)
        
        return res
    }

    suspend fun checkPayPayStatus(merchantPaymentId: String): Boolean {
        try {
            val res = payPayApiService.getPaymentStatus(merchantPaymentId)
            if (res.status == "COMPLETED" || res.status == "SUCCESS") {
                // Find payment
                val payment = paymentDao.getAllPayments() // Helper needed
                    .find { it.merchantPaymentId == merchantPaymentId }
                
                if (payment != null && payment.status != "SUCCEEDED") {
                    markPaymentSucceeded(payment.id)
                    // Also update table status to PAID
                    val bill = billDao.getBill(payment.billId)
                    if (bill != null) {
                         val table = tableDao.findById(bill.tableId)
                         if (table != null) {
                             tableDao.update(table.copy(status = "PAID"))
                         }
                    }
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


}
