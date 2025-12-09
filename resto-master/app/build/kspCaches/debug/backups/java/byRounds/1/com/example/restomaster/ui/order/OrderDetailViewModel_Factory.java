package com.example.restomaster.ui.order;

import androidx.lifecycle.SavedStateHandle;
import com.example.restomaster.data.local.dao.MenuDao;
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
public final class OrderDetailViewModel_Factory implements Factory<OrderDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<OrderDao> orderDaoProvider;

  private final Provider<MenuDao> menuDaoProvider;

  public OrderDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<OrderDao> orderDaoProvider, Provider<MenuDao> menuDaoProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.orderDaoProvider = orderDaoProvider;
    this.menuDaoProvider = menuDaoProvider;
  }

  @Override
  public OrderDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), orderDaoProvider.get(), menuDaoProvider.get());
  }

  public static OrderDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider, Provider<OrderDao> orderDaoProvider,
      Provider<MenuDao> menuDaoProvider) {
    return new OrderDetailViewModel_Factory(savedStateHandleProvider, orderDaoProvider, menuDaoProvider);
  }

  public static OrderDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      OrderDao orderDao, MenuDao menuDao) {
    return new OrderDetailViewModel(savedStateHandle, orderDao, menuDao);
  }
}
