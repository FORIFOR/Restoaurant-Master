package com.example.restomaster.ui.settings;

import android.content.Context;
import com.example.restomaster.data.local.dao.StoreDao;
import com.example.restomaster.data.repository.AppSettingsRepository;
import com.example.restomaster.data.repository.BillingRepository;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<AppSettingsRepository> appSettingsRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  private final Provider<StoreDao> storeDaoProvider;

  public SettingsViewModel_Factory(Provider<Context> contextProvider,
      Provider<AppSettingsRepository> appSettingsRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider, Provider<StoreDao> storeDaoProvider) {
    this.contextProvider = contextProvider;
    this.appSettingsRepositoryProvider = appSettingsRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
    this.storeDaoProvider = storeDaoProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(contextProvider.get(), appSettingsRepositoryProvider.get(), billingRepositoryProvider.get(), storeDaoProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<AppSettingsRepository> appSettingsRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider, Provider<StoreDao> storeDaoProvider) {
    return new SettingsViewModel_Factory(contextProvider, appSettingsRepositoryProvider, billingRepositoryProvider, storeDaoProvider);
  }

  public static SettingsViewModel newInstance(Context context,
      AppSettingsRepository appSettingsRepository, BillingRepository billingRepository,
      StoreDao storeDao) {
    return new SettingsViewModel(context, appSettingsRepository, billingRepository, storeDao);
  }
}
