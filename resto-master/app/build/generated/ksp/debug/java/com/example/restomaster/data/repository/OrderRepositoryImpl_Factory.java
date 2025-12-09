package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.AppDatabase;
import com.example.restomaster.data.local.dao.OrderDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class OrderRepositoryImpl_Factory implements Factory<OrderRepositoryImpl> {
  private final Provider<OrderDao> orderDaoProvider;

  private final Provider<AppDatabase> databaseProvider;

  public OrderRepositoryImpl_Factory(Provider<OrderDao> orderDaoProvider,
      Provider<AppDatabase> databaseProvider) {
    this.orderDaoProvider = orderDaoProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public OrderRepositoryImpl get() {
    return newInstance(orderDaoProvider.get(), databaseProvider.get());
  }

  public static OrderRepositoryImpl_Factory create(Provider<OrderDao> orderDaoProvider,
      Provider<AppDatabase> databaseProvider) {
    return new OrderRepositoryImpl_Factory(orderDaoProvider, databaseProvider);
  }

  public static OrderRepositoryImpl newInstance(OrderDao orderDao, AppDatabase database) {
    return new OrderRepositoryImpl(orderDao, database);
  }
}
