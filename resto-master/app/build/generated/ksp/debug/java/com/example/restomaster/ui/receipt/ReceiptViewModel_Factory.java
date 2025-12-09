package com.example.restomaster.ui.receipt;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import com.example.restomaster.domain.usecase.GenerateReceiptUseCase;
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
public final class ReceiptViewModel_Factory implements Factory<ReceiptViewModel> {
  private final Provider<GenerateReceiptUseCase> generateReceiptUseCaseProvider;

  private final Provider<Context> appContextProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ReceiptViewModel_Factory(Provider<GenerateReceiptUseCase> generateReceiptUseCaseProvider,
      Provider<Context> appContextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    this.generateReceiptUseCaseProvider = generateReceiptUseCaseProvider;
    this.appContextProvider = appContextProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ReceiptViewModel get() {
    return newInstance(generateReceiptUseCaseProvider.get(), appContextProvider.get(), savedStateHandleProvider.get());
  }

  public static ReceiptViewModel_Factory create(
      Provider<GenerateReceiptUseCase> generateReceiptUseCaseProvider,
      Provider<Context> appContextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ReceiptViewModel_Factory(generateReceiptUseCaseProvider, appContextProvider, savedStateHandleProvider);
  }

  public static ReceiptViewModel newInstance(GenerateReceiptUseCase generateReceiptUseCase,
      Context appContext, SavedStateHandle savedStateHandle) {
    return new ReceiptViewModel(generateReceiptUseCase, appContext, savedStateHandle);
  }
}
