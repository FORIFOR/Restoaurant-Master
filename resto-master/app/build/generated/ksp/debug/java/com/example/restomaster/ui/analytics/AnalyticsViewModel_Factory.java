package com.example.restomaster.ui.analytics;

import com.example.restomaster.data.repository.AnalyticsRepository;
import com.example.restomaster.data.repository.BillingRepository;
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
public final class AnalyticsViewModel_Factory implements Factory<AnalyticsViewModel> {
  private final Provider<AnalyticsRepository> analyticsRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public AnalyticsViewModel_Factory(Provider<AnalyticsRepository> analyticsRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.analyticsRepositoryProvider = analyticsRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public AnalyticsViewModel get() {
    return newInstance(analyticsRepositoryProvider.get(), billingRepositoryProvider.get());
  }

  public static AnalyticsViewModel_Factory create(
      Provider<AnalyticsRepository> analyticsRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new AnalyticsViewModel_Factory(analyticsRepositoryProvider, billingRepositoryProvider);
  }

  public static AnalyticsViewModel newInstance(AnalyticsRepository analyticsRepository,
      BillingRepository billingRepository) {
    return new AnalyticsViewModel(analyticsRepository, billingRepository);
  }
}
