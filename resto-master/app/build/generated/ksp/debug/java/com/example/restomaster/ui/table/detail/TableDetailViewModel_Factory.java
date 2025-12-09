package com.example.restomaster.ui.table.detail;

import androidx.lifecycle.SavedStateHandle;
import com.example.restomaster.data.local.dao.CallRequestDao;
import com.example.restomaster.data.local.dao.MenuDao;
import com.example.restomaster.data.local.dao.OrderDao;
import com.example.restomaster.data.local.dao.TableDao;
import com.example.restomaster.data.repository.BillingRepository;
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
public final class TableDetailViewModel_Factory implements Factory<TableDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<TableDao> tableDaoProvider;

  private final Provider<OrderDao> orderDaoProvider;

  private final Provider<CallRequestDao> callRequestDaoProvider;

  private final Provider<TableRepository> tableRepositoryProvider;

  private final Provider<MenuDao> menuDaoProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public TableDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<TableDao> tableDaoProvider, Provider<OrderDao> orderDaoProvider,
      Provider<CallRequestDao> callRequestDaoProvider,
      Provider<TableRepository> tableRepositoryProvider, Provider<MenuDao> menuDaoProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.tableDaoProvider = tableDaoProvider;
    this.orderDaoProvider = orderDaoProvider;
    this.callRequestDaoProvider = callRequestDaoProvider;
    this.tableRepositoryProvider = tableRepositoryProvider;
    this.menuDaoProvider = menuDaoProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public TableDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), tableDaoProvider.get(), orderDaoProvider.get(), callRequestDaoProvider.get(), tableRepositoryProvider.get(), menuDaoProvider.get(), billingRepositoryProvider.get());
  }

  public static TableDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider, Provider<TableDao> tableDaoProvider,
      Provider<OrderDao> orderDaoProvider, Provider<CallRequestDao> callRequestDaoProvider,
      Provider<TableRepository> tableRepositoryProvider, Provider<MenuDao> menuDaoProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new TableDetailViewModel_Factory(savedStateHandleProvider, tableDaoProvider, orderDaoProvider, callRequestDaoProvider, tableRepositoryProvider, menuDaoProvider, billingRepositoryProvider);
  }

  public static TableDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      TableDao tableDao, OrderDao orderDao, CallRequestDao callRequestDao,
      TableRepository tableRepository, MenuDao menuDao, BillingRepository billingRepository) {
    return new TableDetailViewModel(savedStateHandle, tableDao, orderDao, callRequestDao, tableRepository, menuDao, billingRepository);
  }
}
