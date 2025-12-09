package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.dao.BillDao;
import com.example.restomaster.data.local.dao.BillItemDao;
import com.example.restomaster.data.local.dao.ReceiptDao;
import com.example.restomaster.data.local.dao.StoreDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ReceiptRepository_Factory implements Factory<ReceiptRepository> {
  private final Provider<ReceiptDao> receiptDaoProvider;

  private final Provider<BillDao> billDaoProvider;

  private final Provider<BillItemDao> billItemDaoProvider;

  private final Provider<StoreDao> storeDaoProvider;

  public ReceiptRepository_Factory(Provider<ReceiptDao> receiptDaoProvider,
      Provider<BillDao> billDaoProvider, Provider<BillItemDao> billItemDaoProvider,
      Provider<StoreDao> storeDaoProvider) {
    this.receiptDaoProvider = receiptDaoProvider;
    this.billDaoProvider = billDaoProvider;
    this.billItemDaoProvider = billItemDaoProvider;
    this.storeDaoProvider = storeDaoProvider;
  }

  @Override
  public ReceiptRepository get() {
    return newInstance(receiptDaoProvider.get(), billDaoProvider.get(), billItemDaoProvider.get(), storeDaoProvider.get());
  }

  public static ReceiptRepository_Factory create(Provider<ReceiptDao> receiptDaoProvider,
      Provider<BillDao> billDaoProvider, Provider<BillItemDao> billItemDaoProvider,
      Provider<StoreDao> storeDaoProvider) {
    return new ReceiptRepository_Factory(receiptDaoProvider, billDaoProvider, billItemDaoProvider, storeDaoProvider);
  }

  public static ReceiptRepository newInstance(ReceiptDao receiptDao, BillDao billDao,
      BillItemDao billItemDao, StoreDao storeDao) {
    return new ReceiptRepository(receiptDao, billDao, billItemDao, storeDao);
  }
}
