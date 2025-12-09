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
public final class GeminiTranslationClient_Factory implements Factory<GeminiTranslationClient> {
  private final Provider<GeminiApiService> geminiApiProvider;

  public GeminiTranslationClient_Factory(Provider<GeminiApiService> geminiApiProvider) {
    this.geminiApiProvider = geminiApiProvider;
  }

  @Override
  public GeminiTranslationClient get() {
    return newInstance(geminiApiProvider.get());
  }

  public static GeminiTranslationClient_Factory create(
      Provider<GeminiApiService> geminiApiProvider) {
    return new GeminiTranslationClient_Factory(geminiApiProvider);
  }

  public static GeminiTranslationClient newInstance(GeminiApiService geminiApi) {
    return new GeminiTranslationClient(geminiApi);
  }
}
