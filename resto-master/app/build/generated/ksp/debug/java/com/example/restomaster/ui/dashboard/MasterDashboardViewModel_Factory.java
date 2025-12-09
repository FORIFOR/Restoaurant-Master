package com.example.restomaster.ui.dashboard;

import com.example.restomaster.data.repository.MasterDashboardRepository;
import com.example.restomaster.domain.repository.OrderRepository;
import com.example.restomaster.notification.OrderNotifier;
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
public final class MasterDashboardViewModel_Factory implements Factory<MasterDashboardViewModel> {
  private final Provider<MasterDashboardRepository> repositoryProvider;

  private final Provider<OrderRepository> orderRepositoryProvider;

  private final Provider<OrderNotifier> orderNotifierProvider;

  public MasterDashboardViewModel_Factory(Provider<MasterDashboardRepository> repositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<OrderNotifier> orderNotifierProvider) {
    this.repositoryProvider = repositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
    this.orderNotifierProvider = orderNotifierProvider;
  }

  @Override
  public MasterDashboardViewModel get() {
    return newInstance(repositoryProvider.get(), orderRepositoryProvider.get(), orderNotifierProvider.get());
  }

  public static MasterDashboardViewModel_Factory create(
      Provider<MasterDashboardRepository> repositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<OrderNotifier> orderNotifierProvider) {
    return new MasterDashboardViewModel_Factory(repositoryProvider, orderRepositoryProvider, orderNotifierProvider);
  }

  public static MasterDashboardViewModel newInstance(MasterDashboardRepository repository,
      OrderRepository orderRepository, OrderNotifier orderNotifier) {
    return new MasterDashboardViewModel(repository, orderRepository, orderNotifier);
  }
}
