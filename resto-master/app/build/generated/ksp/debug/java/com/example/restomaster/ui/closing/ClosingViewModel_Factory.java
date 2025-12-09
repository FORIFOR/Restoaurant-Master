package com.example.restomaster.ui.closing;

import com.example.restomaster.data.repository.ClosingRepository;
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
public final class ClosingViewModel_Factory implements Factory<ClosingViewModel> {
  private final Provider<ClosingRepository> closingRepositoryProvider;

  public ClosingViewModel_Factory(Provider<ClosingRepository> closingRepositoryProvider) {
    this.closingRepositoryProvider = closingRepositoryProvider;
  }

  @Override
  public ClosingViewModel get() {
    return newInstance(closingRepositoryProvider.get());
  }

  public static ClosingViewModel_Factory create(
      Provider<ClosingRepository> closingRepositoryProvider) {
    return new ClosingViewModel_Factory(closingRepositoryProvider);
  }

  public static ClosingViewModel newInstance(ClosingRepository closingRepository) {
    return new ClosingViewModel(closingRepository);
  }
}
