package com.example.restomaster.domain.usecase;

import com.example.restomaster.data.repository.TranslationRepository;
import com.example.restomaster.domain.repository.MenuRepository;
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
public final class BatchTranslateMenusUseCase_Factory implements Factory<BatchTranslateMenusUseCase> {
  private final Provider<MenuRepository> menuRepositoryProvider;

  private final Provider<TranslationRepository> translationRepositoryProvider;

  private final Provider<GenerateTranslationUseCase> generateTranslationUseCaseProvider;

  public BatchTranslateMenusUseCase_Factory(Provider<MenuRepository> menuRepositoryProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<GenerateTranslationUseCase> generateTranslationUseCaseProvider) {
    this.menuRepositoryProvider = menuRepositoryProvider;
    this.translationRepositoryProvider = translationRepositoryProvider;
    this.generateTranslationUseCaseProvider = generateTranslationUseCaseProvider;
  }

  @Override
  public BatchTranslateMenusUseCase get() {
    return newInstance(menuRepositoryProvider.get(), translationRepositoryProvider.get(), generateTranslationUseCaseProvider.get());
  }

  public static BatchTranslateMenusUseCase_Factory create(
      Provider<MenuRepository> menuRepositoryProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<GenerateTranslationUseCase> generateTranslationUseCaseProvider) {
    return new BatchTranslateMenusUseCase_Factory(menuRepositoryProvider, translationRepositoryProvider, generateTranslationUseCaseProvider);
  }

  public static BatchTranslateMenusUseCase newInstance(MenuRepository menuRepository,
      TranslationRepository translationRepository,
      GenerateTranslationUseCase generateTranslationUseCase) {
    return new BatchTranslateMenusUseCase(menuRepository, translationRepository, generateTranslationUseCase);
  }
}
