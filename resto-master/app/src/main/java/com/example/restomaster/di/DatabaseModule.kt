package com.example.restomaster.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.restomaster.data.local.AppDatabase
import com.example.restomaster.data.local.AppDatabaseMigrations
import com.example.restomaster.data.local.dao.MenuDao
import com.example.restomaster.data.local.dao.OrderDao
import com.example.restomaster.data.local.dao.ReservationDao
import com.example.restomaster.data.local.dao.StoreDao
import com.example.restomaster.data.local.dao.TableDao
import com.example.restomaster.data.local.dao.MasterMenuItemAllergenDao
import com.example.restomaster.data.local.dao.AllergenFeedbackDao
import com.example.restomaster.data.local.entity.AllergenEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.NAME
        )
            .addMigrations(
                AppDatabaseMigrations.MIGRATION_1_2,
                AppDatabaseMigrations.MIGRATION_2_3,
                AppDatabaseMigrations.MIGRATION_3_4,
                AppDatabaseMigrations.MIGRATION_4_5,
                AppDatabaseMigrations.MIGRATION_5_6,
                AppDatabaseMigrations.MIGRATION_6_7,
                AppDatabaseMigrations.MIGRATION_7_8,
                AppDatabaseMigrations.MIGRATION_8_9,
                AppDatabaseMigrations.MIGRATION_9_10,
                AppDatabaseMigrations.MIGRATION_10_11,
                AppDatabaseMigrations.MIGRATION_11_12,
                AppDatabaseMigrations.MIGRATION_12_13,
                AppDatabaseMigrations.MIGRATION_13_14
            )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Pre-populate allergens
                    CoroutineScope(Dispatchers.IO).launch {
                        // This is a bit tricky since we can't easily get the DAO here without circular dependency or manual creation
                        // For simplicity, we might want to do this via a Worker or just let the Repository handle initial population
                    }
                }
            })
            .build()
    }

    @Provides
    fun provideStoreDao(database: AppDatabase): StoreDao = database.storeDao()

    @Provides
    fun provideTableDao(database: AppDatabase): TableDao = database.tableDao()

    @Provides
    fun provideMenuDao(database: AppDatabase): MenuDao = database.menuDao()

    @Provides
    fun provideOrderDao(database: AppDatabase): OrderDao = database.orderDao()

    @Provides
    fun provideMasterMenuItemAllergenDao(database: AppDatabase): MasterMenuItemAllergenDao {
        return database.masterMenuItemAllergenDao()
    }

    @Provides
    fun provideAllergenFeedbackDao(database: AppDatabase): AllergenFeedbackDao {
        return database.allergenFeedbackDao()
    }

    @Provides
    fun provideReservationDao(database: AppDatabase): ReservationDao = database.reservationDao()

    @Provides
    fun provideCallRequestDao(database: AppDatabase): com.example.restomaster.data.local.dao.CallRequestDao {
        return database.callRequestDao()
    }

    @Provides
    fun provideMenuTranslationDao(database: AppDatabase): com.example.restomaster.data.local.dao.MenuTranslationDao {
        return database.menuTranslationDao()
    }

    @Provides
    fun provideBillDao(database: AppDatabase): com.example.restomaster.data.local.dao.BillDao = database.billDao()

    @Provides
    fun provideBillItemDao(database: AppDatabase): com.example.restomaster.data.local.dao.BillItemDao = database.billItemDao()

    @Provides
    fun providePaymentDao(database: AppDatabase): com.example.restomaster.data.local.dao.PaymentDao = database.paymentDao()

    @Provides
    fun provideReceiptDao(database: AppDatabase): com.example.restomaster.data.local.dao.ReceiptDao = database.receiptDao()

    @Provides
    fun providePaymentItemDao(database: AppDatabase): com.example.restomaster.data.local.dao.PaymentItemDao = database.paymentItemDao()

    @Provides
    fun provideClosingDao(database: AppDatabase): com.example.restomaster.data.local.dao.ClosingDao = database.closingDao()
}
