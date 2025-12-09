package com.example.restomaster.ui.sales;

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
public final class SalesHistoryViewModel_Factory implements Factory<SalesHistoryViewModel> {
  private final Provider<OrderRepository> orderRepositoryProvider;

  public SalesHistoryViewModel_Factory(Provider<OrderRepository> orderRepositoryProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public SalesHistoryViewModel get() {
    return newInstance(orderRepositoryProvider.get());
  }

  public static SalesHistoryViewModel_Factory create(
      Provider<OrderRepository> orderRepositoryProvider) {
    return new SalesHistoryViewModel_Factory(orderRepositoryProvider);
  }

  public static SalesHistoryViewModel newInstance(OrderRepository orderRepository) {
    return new SalesHistoryViewModel(orderRepository);
  }
}
