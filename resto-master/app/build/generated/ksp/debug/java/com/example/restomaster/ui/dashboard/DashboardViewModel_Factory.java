package com.example.restomaster.ui.dashboard;

import android.content.Context;
import com.example.restomaster.domain.repository.OrderRepository;
import com.example.restomaster.domain.repository.TableRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<TableRepository> tableRepositoryProvider;

  private final Provider<OrderRepository> orderRepositoryProvider;

  private final Provider<Context> contextProvider;

  public DashboardViewModel_Factory(Provider<TableRepository> tableRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider, Provider<Context> contextProvider) {
    this.tableRepositoryProvider = tableRepositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(tableRepositoryProvider.get(), orderRepositoryProvider.get(), contextProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<TableRepository> tableRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider, Provider<Context> contextProvider) {
    return new DashboardViewModel_Factory(tableRepositoryProvider, orderRepositoryProvider, contextProvider);
  }

  public static DashboardViewModel newInstance(TableRepository tableRepository,
      OrderRepository orderRepository, Context context) {
    return new DashboardViewModel(tableRepository, orderRepository, context);
  }
}
