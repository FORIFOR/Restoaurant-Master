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
public final class GeminiDirectClient_Factory implements Factory<GeminiDirectClient> {
  private final Provider<GeminiApiService> geminiApiProvider;

  public GeminiDirectClient_Factory(Provider<GeminiApiService> geminiApiProvider) {
    this.geminiApiProvider = geminiApiProvider;
  }

  @Override
  public GeminiDirectClient get() {
    return newInstance(geminiApiProvider.get());
  }

  public static GeminiDirectClient_Factory create(Provider<GeminiApiService> geminiApiProvider) {
    return new GeminiDirectClient_Factory(geminiApiProvider);
  }

  public static GeminiDirectClient newInstance(GeminiApiService geminiApi) {
    return new GeminiDirectClient(geminiApi);
  }
}
