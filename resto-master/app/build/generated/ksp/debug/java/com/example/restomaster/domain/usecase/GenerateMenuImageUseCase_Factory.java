package com.example.restomaster.domain.usecase;

import android.content.Context;
import com.example.restomaster.data.remote.OpenAIImageClient;
import com.example.restomaster.data.repository.AppSettingsRepository;
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
public final class GenerateMenuImageUseCase_Factory implements Factory<GenerateMenuImageUseCase> {
  private final Provider<OpenAIImageClient> clientProvider;

  private final Provider<AppSettingsRepository> appSettingsRepositoryProvider;

  private final Provider<Context> contextProvider;

  public GenerateMenuImageUseCase_Factory(Provider<OpenAIImageClient> clientProvider,
      Provider<AppSettingsRepository> appSettingsRepositoryProvider,
      Provider<Context> contextProvider) {
    this.clientProvider = clientProvider;
    this.appSettingsRepositoryProvider = appSettingsRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public GenerateMenuImageUseCase get() {
    return newInstance(clientProvider.get(), appSettingsRepositoryProvider.get(), contextProvider.get());
  }

  public static GenerateMenuImageUseCase_Factory create(Provider<OpenAIImageClient> clientProvider,
      Provider<AppSettingsRepository> appSettingsRepositoryProvider,
      Provider<Context> contextProvider) {
    return new GenerateMenuImageUseCase_Factory(clientProvider, appSettingsRepositoryProvider, contextProvider);
  }

  public static GenerateMenuImageUseCase newInstance(OpenAIImageClient client,
      AppSettingsRepository appSettingsRepository, Context context) {
    return new GenerateMenuImageUseCase(client, appSettingsRepository, context);
  }
}
