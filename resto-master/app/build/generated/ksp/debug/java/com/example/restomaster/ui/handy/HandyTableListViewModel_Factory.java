package com.example.restomaster.ui.handy;

import com.example.restomaster.domain.repository.OrderRepository;
import com.example.restomaster.domain.repository.TableRepository;
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
public final class HandyTableListViewModel_Factory implements Factory<HandyTableListViewModel> {
  private final Provider<TableRepository> tableRepositoryProvider;

  private final Provider<OrderRepository> orderRepositoryProvider;

  public HandyTableListViewModel_Factory(Provider<TableRepository> tableRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider) {
    this.tableRepositoryProvider = tableRepositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public HandyTableListViewModel get() {
    return newInstance(tableRepositoryProvider.get(), orderRepositoryProvider.get());
  }

  public static HandyTableListViewModel_Factory create(
      Provider<TableRepository> tableRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider) {
    return new HandyTableListViewModel_Factory(tableRepositoryProvider, orderRepositoryProvider);
  }

  public static HandyTableListViewModel newInstance(TableRepository tableRepository,
      OrderRepository orderRepository) {
    return new HandyTableListViewModel(tableRepository, orderRepository);
  }
}
