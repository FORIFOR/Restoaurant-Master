package com.example.restomaster.ui.dashboard;

import com.example.restomaster.domain.repository.OrderRepository;
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
public final class OrderListViewModel_Factory implements Factory<OrderListViewModel> {
  private final Provider<OrderRepository> orderRepositoryProvider;

  public OrderListViewModel_Factory(Provider<OrderRepository> orderRepositoryProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public OrderListViewModel get() {
    return newInstance(orderRepositoryProvider.get());
  }

  public static OrderListViewModel_Factory create(
      Provider<OrderRepository> orderRepositoryProvider) {
    return new OrderListViewModel_Factory(orderRepositoryProvider);
  }

  public static OrderListViewModel newInstance(OrderRepository orderRepository) {
    return new OrderListViewModel(orderRepository);
  }
}
