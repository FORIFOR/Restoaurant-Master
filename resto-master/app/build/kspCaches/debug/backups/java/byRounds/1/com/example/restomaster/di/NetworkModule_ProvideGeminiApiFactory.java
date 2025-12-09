package com.example.restomaster.di;

import com.example.restomaster.data.remote.GeminiApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class NetworkModule_ProvideGeminiApiFactory implements Factory<GeminiApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideGeminiApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public GeminiApiService get() {
    return provideGeminiApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideGeminiApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideGeminiApiFactory(retrofitProvider);
  }

  public static GeminiApiService provideGeminiApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideGeminiApi(retrofit));
  }
}
