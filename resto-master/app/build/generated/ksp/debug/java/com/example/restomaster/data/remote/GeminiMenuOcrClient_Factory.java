package com.example.restomaster.data.remote;

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
public final class GeminiMenuOcrClient_Factory implements Factory<GeminiMenuOcrClient> {
  private final Provider<MenuOcrApi> apiProvider;

  public GeminiMenuOcrClient_Factory(Provider<MenuOcrApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public GeminiMenuOcrClient get() {
    return newInstance(apiProvider.get());
  }

  public static GeminiMenuOcrClient_Factory create(Provider<MenuOcrApi> apiProvider) {
    return new GeminiMenuOcrClient_Factory(apiProvider);
  }

  public static GeminiMenuOcrClient newInstance(MenuOcrApi api) {
    return new GeminiMenuOcrClient(api);
  }
}
