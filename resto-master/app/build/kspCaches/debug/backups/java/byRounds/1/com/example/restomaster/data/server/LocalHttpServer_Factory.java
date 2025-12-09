package com.example.restomaster.data.server;

import android.content.Context;
import com.example.restomaster.data.local.dao.CallRequestDao;
import com.example.restomaster.data.local.dao.StoreDao;
import com.example.restomaster.data.local.dao.TableDao;
import com.example.restomaster.data.repository.BillingRepository;
import com.example.restomaster.data.repository.TranslationRepository;
import com.example.restomaster.domain.repository.MenuRepository;
import com.example.restomaster.domain.repository.OrderRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class LocalHttpServer_Factory implements Factory<LocalHttpServer> {
  private final Provider<Context> contextProvider;

  private final Provider<OrderRepository> orderRepositoryProvider;

  private final Provider<MenuRepository> menuRepositoryProvider;

  private final Provider<TableDao> tableDaoProvider;

  private final Provider<StoreDao> storeDaoProvider;

  private final Provider<TranslationRepository> translationRepositoryProvider;

  private final Provider<CallRequestDao> callRequestDaoProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public LocalHttpServer_Factory(Provider<Context> contextProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<MenuRepository> menuRepositoryProvider, Provider<TableDao> tableDaoProvider,
      Provider<StoreDao> storeDaoProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<CallRequestDao> callRequestDaoProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
    this.menuRepositoryProvider = menuRepositoryProvider;
    this.tableDaoProvider = tableDaoProvider;
    this.storeDaoProvider = storeDaoProvider;
    this.translationRepositoryProvider = translationRepositoryProvider;
    this.callRequestDaoProvider = callRequestDaoProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public LocalHttpServer get() {
    return newInstance(contextProvider.get(), orderRepositoryProvider.get(), menuRepositoryProvider.get(), tableDaoProvider.get(), storeDaoProvider.get(), translationRepositoryProvider.get(), callRequestDaoProvider.get(), billingRepositoryProvider.get());
  }

  public static LocalHttpServer_Factory create(Provider<Context> contextProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<MenuRepository> menuRepositoryProvider, Provider<TableDao> tableDaoProvider,
      Provider<StoreDao> storeDaoProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<CallRequestDao> callRequestDaoProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new LocalHttpServer_Factory(contextProvider, orderRepositoryProvider, menuRepositoryProvider, tableDaoProvider, storeDaoProvider, translationRepositoryProvider, callRequestDaoProvider, billingRepositoryProvider);
  }

  public static LocalHttpServer newInstance(Context context, OrderRepository orderRepository,
      MenuRepository menuRepository, TableDao tableDao, StoreDao storeDao,
      TranslationRepository translationRepository, CallRequestDao callRequestDao,
      BillingRepository billingRepository) {
    return new LocalHttpServer(context, orderRepository, menuRepository, tableDao, storeDao, translationRepository, callRequestDao, billingRepository);
  }
}
