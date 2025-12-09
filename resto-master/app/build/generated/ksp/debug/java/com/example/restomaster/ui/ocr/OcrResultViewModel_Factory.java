package com.example.restomaster.ui.ocr;

import com.example.restomaster.domain.repository.MenuRepository;
import com.example.restomaster.domain.usecase.MenuOcrGeminiUseCase;
import com.example.restomaster.domain.usecase.MenuOcrUseCase;
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
public final class OcrResultViewModel_Factory implements Factory<OcrResultViewModel> {
  private final Provider<MenuOcrUseCase> menuOcrUseCaseProvider;

  private final Provider<MenuOcrGeminiUseCase> menuOcrGeminiUseCaseProvider;

  private final Provider<MenuRepository> menuRepositoryProvider;

  public OcrResultViewModel_Factory(Provider<MenuOcrUseCase> menuOcrUseCaseProvider,
      Provider<MenuOcrGeminiUseCase> menuOcrGeminiUseCaseProvider,
      Provider<MenuRepository> menuRepositoryProvider) {
    this.menuOcrUseCaseProvider = menuOcrUseCaseProvider;
    this.menuOcrGeminiUseCaseProvider = menuOcrGeminiUseCaseProvider;
    this.menuRepositoryProvider = menuRepositoryProvider;
  }

  @Override
  public OcrResultViewModel get() {
    return newInstance(menuOcrUseCaseProvider.get(), menuOcrGeminiUseCaseProvider.get(), menuRepositoryProvider.get());
  }

  public static OcrResultViewModel_Factory create(Provider<MenuOcrUseCase> menuOcrUseCaseProvider,
      Provider<MenuOcrGeminiUseCase> menuOcrGeminiUseCaseProvider,
      Provider<MenuRepository> menuRepositoryProvider) {
    return new OcrResultViewModel_Factory(menuOcrUseCaseProvider, menuOcrGeminiUseCaseProvider, menuRepositoryProvider);
  }

  public static OcrResultViewModel newInstance(MenuOcrUseCase menuOcrUseCase,
      MenuOcrGeminiUseCase menuOcrGeminiUseCase, MenuRepository menuRepository) {
    return new OcrResultViewModel(menuOcrUseCase, menuOcrGeminiUseCase, menuRepository);
  }
}
