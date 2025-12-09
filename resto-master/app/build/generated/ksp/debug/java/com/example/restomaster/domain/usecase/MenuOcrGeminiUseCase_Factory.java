package com.example.restomaster.domain.usecase;

import com.example.restomaster.data.remote.GeminiDirectClient;
import com.example.restomaster.data.repository.AppSettingsRepository;
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
public final class MenuOcrGeminiUseCase_Factory implements Factory<MenuOcrGeminiUseCase> {
  private final Provider<GeminiDirectClient> clientProvider;

  private final Provider<AppSettingsRepository> appSettingsRepositoryProvider;

  public MenuOcrGeminiUseCase_Factory(Provider<GeminiDirectClient> clientProvider,
      Provider<AppSettingsRepository> appSettingsRepositoryProvider) {
    this.clientProvider = clientProvider;
    this.appSettingsRepositoryProvider = appSettingsRepositoryProvider;
  }

  @Override
  public MenuOcrGeminiUseCase get() {
    return newInstance(clientProvider.get(), appSettingsRepositoryProvider.get());
  }

  public static MenuOcrGeminiUseCase_Factory create(Provider<GeminiDirectClient> clientProvider,
      Provider<AppSettingsRepository> appSettingsRepositoryProvider) {
    return new MenuOcrGeminiUseCase_Factory(clientProvider, appSettingsRepositoryProvider);
  }

  public static MenuOcrGeminiUseCase newInstance(GeminiDirectClient client,
      AppSettingsRepository appSettingsRepository) {
    return new MenuOcrGeminiUseCase(client, appSettingsRepository);
  }
}
