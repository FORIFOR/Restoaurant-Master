package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.dao.CallRequestDao;
import com.example.restomaster.data.local.dao.OrderDao;
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
public final class MasterDashboardRepository_Factory implements Factory<MasterDashboardRepository> {
  private final Provider<TableDao> tableDaoProvider;

  private final Provider<OrderDao> orderDaoProvider;

  private final Provider<CallRequestDao> callRequestDaoProvider;

  public MasterDashboardRepository_Factory(Provider<TableDao> tableDaoProvider,
      Provider<OrderDao> orderDaoProvider, Provider<CallRequestDao> callRequestDaoProvider) {
    this.tableDaoProvider = tableDaoProvider;
    this.orderDaoProvider = orderDaoProvider;
    this.callRequestDaoProvider = callRequestDaoProvider;
  }

  @Override
  public MasterDashboardRepository get() {
    return newInstance(tableDaoProvider.get(), orderDaoProvider.get(), callRequestDaoProvider.get());
  }

  public static MasterDashboardRepository_Factory create(Provider<TableDao> tableDaoProvider,
      Provider<OrderDao> orderDaoProvider, Provider<CallRequestDao> callRequestDaoProvider) {
    return new MasterDashboardRepository_Factory(tableDaoProvider, orderDaoProvider, callRequestDaoProvider);
  }

  public static MasterDashboardRepository newInstance(TableDao tableDao, OrderDao orderDao,
      CallRequestDao callRequestDao) {
    return new MasterDashboardRepository(tableDao, orderDao, callRequestDao);
  }
}
