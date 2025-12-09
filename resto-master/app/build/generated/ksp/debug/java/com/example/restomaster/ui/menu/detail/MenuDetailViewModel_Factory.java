package com.example.restomaster.ui.menu.detail;

import androidx.lifecycle.SavedStateHandle;
import com.example.restomaster.data.local.dao.AllergenFeedbackDao;
import com.example.restomaster.data.repository.TranslationRepository;
import com.example.restomaster.domain.ai.AllergenSuggestionEngine;
import com.example.restomaster.domain.repository.MenuRepository;
import com.example.restomaster.domain.usecase.GenerateMenuImageUseCase;
import com.example.restomaster.domain.usecase.GenerateTranslationUseCase;
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
public final class MenuDetailViewModel_Factory implements Factory<MenuDetailViewModel> {
  private final Provider<MenuRepository> menuRepositoryProvider;

  private final Provider<TranslationRepository> translationRepositoryProvider;

  private final Provider<GenerateTranslationUseCase> generateTranslationUseCaseProvider;

  private final Provider<GenerateMenuImageUseCase> generateMenuImageUseCaseProvider;

  private final Provider<AllergenSuggestionEngine> allergenSuggestionEngineProvider;

  private final Provider<AllergenFeedbackDao> allergenFeedbackDaoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public MenuDetailViewModel_Factory(Provider<MenuRepository> menuRepositoryProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<GenerateTranslationUseCase> generateTranslationUseCaseProvider,
      Provider<GenerateMenuImageUseCase> generateMenuImageUseCaseProvider,
      Provider<AllergenSuggestionEngine> allergenSuggestionEngineProvider,
      Provider<AllergenFeedbackDao> allergenFeedbackDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.menuRepositoryProvider = menuRepositoryProvider;
    this.translationRepositoryProvider = translationRepositoryProvider;
    this.generateTranslationUseCaseProvider = generateTranslationUseCaseProvider;
    this.generateMenuImageUseCaseProvider = generateMenuImageUseCaseProvider;
    this.allergenSuggestionEngineProvider = allergenSuggestionEngineProvider;
    this.allergenFeedbackDaoProvider = allergenFeedbackDaoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public MenuDetailViewModel get() {
    return newInstance(menuRepositoryProvider.get(), translationRepositoryProvider.get(), generateTranslationUseCaseProvider.get(), generateMenuImageUseCaseProvider.get(), allergenSuggestionEngineProvider.get(), allergenFeedbackDaoProvider.get(), savedStateHandleProvider.get());
  }

  public static MenuDetailViewModel_Factory create(Provider<MenuRepository> menuRepositoryProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<GenerateTranslationUseCase> generateTranslationUseCaseProvider,
      Provider<GenerateMenuImageUseCase> generateMenuImageUseCaseProvider,
      Provider<AllergenSuggestionEngine> allergenSuggestionEngineProvider,
      Provider<AllergenFeedbackDao> allergenFeedbackDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new MenuDetailViewModel_Factory(menuRepositoryProvider, translationRepositoryProvider, generateTranslationUseCaseProvider, generateMenuImageUseCaseProvider, allergenSuggestionEngineProvider, allergenFeedbackDaoProvider, savedStateHandleProvider);
  }

  public static MenuDetailViewModel newInstance(MenuRepository menuRepository,
      TranslationRepository translationRepository,
      GenerateTranslationUseCase generateTranslationUseCase,
      GenerateMenuImageUseCase generateMenuImageUseCase,
      AllergenSuggestionEngine allergenSuggestionEngine, AllergenFeedbackDao allergenFeedbackDao,
      SavedStateHandle savedStateHandle) {
    return new MenuDetailViewModel(menuRepository, translationRepository, generateTranslationUseCase, generateMenuImageUseCase, allergenSuggestionEngine, allergenFeedbackDao, savedStateHandle);
  }
}
