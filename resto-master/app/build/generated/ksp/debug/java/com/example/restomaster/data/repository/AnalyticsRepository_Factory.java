package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.dao.BillDao;
import com.example.restomaster.data.local.dao.BillItemDao;
import com.example.restomaster.data.local.dao.TableDao;
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
public final class AnalyticsRepository_Factory implements Factory<AnalyticsRepository> {
  private final Provider<BillDao> billDaoProvider;

  private final Provider<BillItemDao> billItemDaoProvider;

  private final Provider<TableDao> tableDaoProvider;

  public AnalyticsRepository_Factory(Provider<BillDao> billDaoProvider,
      Provider<BillItemDao> billItemDaoProvider, Provider<TableDao> tableDaoProvider) {
    this.billDaoProvider = billDaoProvider;
    this.billItemDaoProvider = billItemDaoProvider;
    this.tableDaoProvider = tableDaoProvider;
  }

  @Override
  public AnalyticsRepository get() {
    return newInstance(billDaoProvider.get(), billItemDaoProvider.get(), tableDaoProvider.get());
  }

  public static AnalyticsRepository_Factory create(Provider<BillDao> billDaoProvider,
      Provider<BillItemDao> billItemDaoProvider, Provider<TableDao> tableDaoProvider) {
    return new AnalyticsRepository_Factory(billDaoProvider, billItemDaoProvider, tableDaoProvider);
  }

  public static AnalyticsRepository newInstance(BillDao billDao, BillItemDao billItemDao,
      TableDao tableDao) {
    return new AnalyticsRepository(billDao, billItemDao, tableDao);
  }
}
