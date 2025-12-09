package com.example.restomaster.ui.menu;

import com.example.restomaster.data.repository.TranslationRepository;
import com.example.restomaster.domain.repository.MenuRepository;
import com.example.restomaster.domain.usecase.BatchTranslateMenusUseCase;
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
public final class MenuListViewModel_Factory implements Factory<MenuListViewModel> {
  private final Provider<MenuRepository> menuRepositoryProvider;

  private final Provider<TranslationRepository> translationRepositoryProvider;

  private final Provider<BatchTranslateMenusUseCase> batchTranslateMenusUseCaseProvider;

  public MenuListViewModel_Factory(Provider<MenuRepository> menuRepositoryProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<BatchTranslateMenusUseCase> batchTranslateMenusUseCaseProvider) {
    this.menuRepositoryProvider = menuRepositoryProvider;
    this.translationRepositoryProvider = translationRepositoryProvider;
    this.batchTranslateMenusUseCaseProvider = batchTranslateMenusUseCaseProvider;
  }

  @Override
  public MenuListViewModel get() {
    return newInstance(menuRepositoryProvider.get(), translationRepositoryProvider.get(), batchTranslateMenusUseCaseProvider.get());
  }

  public static MenuListViewModel_Factory create(Provider<MenuRepository> menuRepositoryProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<BatchTranslateMenusUseCase> batchTranslateMenusUseCaseProvider) {
    return new MenuListViewModel_Factory(menuRepositoryProvider, translationRepositoryProvider, batchTranslateMenusUseCaseProvider);
  }

  public static MenuListViewModel newInstance(MenuRepository menuRepository,
      TranslationRepository translationRepository,
      BatchTranslateMenusUseCase batchTranslateMenusUseCase) {
    return new MenuListViewModel(menuRepository, translationRepository, batchTranslateMenusUseCase);
  }
}
