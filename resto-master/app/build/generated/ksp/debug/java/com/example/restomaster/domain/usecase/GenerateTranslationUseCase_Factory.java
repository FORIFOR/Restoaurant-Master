package com.example.restomaster.domain.usecase;

import com.example.restomaster.data.local.dao.MenuDao;
import com.example.restomaster.data.remote.GeminiTranslationClient;
import com.example.restomaster.data.repository.AppSettingsRepository;
import com.example.restomaster.data.repository.TranslationRepository;
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
public final class GenerateTranslationUseCase_Factory implements Factory<GenerateTranslationUseCase> {
  private final Provider<MenuDao> menuDaoProvider;

  private final Provider<TranslationRepository> translationRepositoryProvider;

  private final Provider<GeminiTranslationClient> geminiClientProvider;

  private final Provider<AppSettingsRepository> appSettingsRepositoryProvider;

  public GenerateTranslationUseCase_Factory(Provider<MenuDao> menuDaoProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<GeminiTranslationClient> geminiClientProvider,
      Provider<AppSettingsRepository> appSettingsRepositoryProvider) {
    this.menuDaoProvider = menuDaoProvider;
    this.translationRepositoryProvider = translationRepositoryProvider;
    this.geminiClientProvider = geminiClientProvider;
    this.appSettingsRepositoryProvider = appSettingsRepositoryProvider;
  }

  @Override
  public GenerateTranslationUseCase get() {
    return newInstance(menuDaoProvider.get(), translationRepositoryProvider.get(), geminiClientProvider.get(), appSettingsRepositoryProvider.get());
  }

  public static GenerateTranslationUseCase_Factory create(Provider<MenuDao> menuDaoProvider,
      Provider<TranslationRepository> translationRepositoryProvider,
      Provider<GeminiTranslationClient> geminiClientProvider,
      Provider<AppSettingsRepository> appSettingsRepositoryProvider) {
    return new GenerateTranslationUseCase_Factory(menuDaoProvider, translationRepositoryProvider, geminiClientProvider, appSettingsRepositoryProvider);
  }

  public static GenerateTranslationUseCase newInstance(MenuDao menuDao,
      TranslationRepository translationRepository, GeminiTranslationClient geminiClient,
      AppSettingsRepository appSettingsRepository) {
    return new GenerateTranslationUseCase(menuDao, translationRepository, geminiClient, appSettingsRepository);
  }
}
