package com.example.restomaster.ui.billing;

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
public final class BillingViewModel_Factory implements Factory<BillingViewModel> {
  private final Provider<BillingRepository> billingRepositoryProvider;

  public BillingViewModel_Factory(Provider<BillingRepository> billingRepositoryProvider) {
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public BillingViewModel get() {
    return newInstance(billingRepositoryProvider.get());
  }

  public static BillingViewModel_Factory create(
      Provider<BillingRepository> billingRepositoryProvider) {
    return new BillingViewModel_Factory(billingRepositoryProvider);
  }

  public static BillingViewModel newInstance(BillingRepository billingRepository) {
    return new BillingViewModel(billingRepository);
  }
}
