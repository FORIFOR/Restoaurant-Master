package com.example.restomaster.domain.usecase;

import com.example.restomaster.data.repository.ReceiptRepository;
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
public final class GenerateReceiptUseCase_Factory implements Factory<GenerateReceiptUseCase> {
  private final Provider<ReceiptRepository> receiptRepositoryProvider;

  public GenerateReceiptUseCase_Factory(Provider<ReceiptRepository> receiptRepositoryProvider) {
    this.receiptRepositoryProvider = receiptRepositoryProvider;
  }

  @Override
  public GenerateReceiptUseCase get() {
    return newInstance(receiptRepositoryProvider.get());
  }

  public static GenerateReceiptUseCase_Factory create(
      Provider<ReceiptRepository> receiptRepositoryProvider) {
    return new GenerateReceiptUseCase_Factory(receiptRepositoryProvider);
  }

  public static GenerateReceiptUseCase newInstance(ReceiptRepository receiptRepository) {
    return new GenerateReceiptUseCase(receiptRepository);
  }
}
