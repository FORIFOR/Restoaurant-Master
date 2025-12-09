package com.example.restomaster.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.restomaster.data.local.dao.MenuDao
import com.example.restomaster.data.local.dao.OrderDao
import com.example.restomaster.data.local.dao.ReservationDao
import com.example.restomaster.data.local.dao.StoreDao
import com.example.restomaster.data.local.dao.TableDao
import com.example.restomaster.data.local.dao.MasterAllergenDao
import com.example.restomaster.data.local.dao.MasterMenuItemAllergenDao
import com.example.restomaster.data.local.dao.AllergenFeedbackDao
import com.example.restomaster.data.local.entity.StoreEntity
import com.example.restomaster.data.local.entity.TableEntity
import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.data.local.entity.MasterAllergenEntity
import com.example.restomaster.data.local.entity.MasterMenuItemAllergen
import com.example.restomaster.data.local.entity.OrderEntity
import com.example.restomaster.data.local.entity.OrderItemEntity
import com.example.restomaster.data.local.entity.CallRequestEntity
import com.example.restomaster.data.local.entity.ReservationEntity
import com.example.restomaster.data.local.entity.AllergenFeedbackEntity
import com.example.restomaster.data.local.entity.BillEntity
import com.example.restomaster.data.local.entity.BillItemEntity
import com.example.restomaster.data.local.entity.PaymentItemEntity

@Database(
    entities = [
        StoreEntity::class,
        TableEntity::class,
        MenuItemEntity::class,
        MasterAllergenEntity::class,
        MasterMenuItemAllergen::class,
        OrderEntity::class,
        OrderItemEntity::class,
        CallRequestEntity::class,
        ReservationEntity::class,
        AllergenFeedbackEntity::class,
        com.example.restomaster.data.local.entity.MenuTranslationEntity::class,
        com.example.restomaster.data.local.entity.BillEntity::class,
        com.example.restomaster.data.local.entity.BillItemEntity::class,
        com.example.restomaster.data.local.entity.PaymentEntity::class,
        com.example.restomaster.data.local.entity.ReceiptEntity::class,
        com.example.restomaster.data.local.entity.PaymentItemEntity::class
    ],
    version = 14,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storeDao(): StoreDao
    abstract fun tableDao(): TableDao
    abstract fun menuDao(): MenuDao
    abstract fun orderDao(): OrderDao
    abstract fun reservationDao(): ReservationDao
    abstract fun masterAllergenDao(): MasterAllergenDao
    abstract fun masterMenuItemAllergenDao(): MasterMenuItemAllergenDao
    abstract fun allergenFeedbackDao(): AllergenFeedbackDao
    abstract fun callRequestDao(): com.example.restomaster.data.local.dao.CallRequestDao
    abstract fun menuTranslationDao(): com.example.restomaster.data.local.dao.MenuTranslationDao
    abstract fun billDao(): com.example.restomaster.data.local.dao.BillDao
    abstract fun billItemDao(): com.example.restomaster.data.local.dao.BillItemDao
    abstract fun paymentDao(): com.example.restomaster.data.local.dao.PaymentDao
    abstract fun receiptDao(): com.example.restomaster.data.local.dao.ReceiptDao
    abstract fun paymentItemDao(): com.example.restomaster.data.local.dao.PaymentItemDao
    abstract fun closingDao(): com.example.restomaster.data.local.dao.ClosingDao

    companion object {
        const val NAME = "resto_master.db"
    }
}
