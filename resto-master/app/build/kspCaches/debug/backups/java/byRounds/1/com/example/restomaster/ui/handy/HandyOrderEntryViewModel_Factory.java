package com.example.restomaster.ui.handy;

import com.example.restomaster.domain.repository.MenuRepository;
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
public final class HandyOrderEntryViewModel_Factory implements Factory<HandyOrderEntryViewModel> {
  private final Provider<MenuRepository> menuRepositoryProvider;

  private final Provider<OrderRepository> orderRepositoryProvider;

  public HandyOrderEntryViewModel_Factory(Provider<MenuRepository> menuRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider) {
    this.menuRepositoryProvider = menuRepositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public HandyOrderEntryViewModel get() {
    return newInstance(menuRepositoryProvider.get(), orderRepositoryProvider.get());
  }

  public static HandyOrderEntryViewModel_Factory create(
      Provider<MenuRepository> menuRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider) {
    return new HandyOrderEntryViewModel_Factory(menuRepositoryProvider, orderRepositoryProvider);
  }

  public static HandyOrderEntryViewModel newInstance(MenuRepository menuRepository,
      OrderRepository orderRepository) {
    return new HandyOrderEntryViewModel(menuRepository, orderRepository);
  }
}
