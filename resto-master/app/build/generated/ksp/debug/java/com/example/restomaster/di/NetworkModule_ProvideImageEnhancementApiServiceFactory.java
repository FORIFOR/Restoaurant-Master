package com.example.restomaster.di;

import com.example.restomaster.data.remote.ImageEnhancementApiService;
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
public final class NetworkModule_ProvideImageEnhancementApiServiceFactory implements Factory<ImageEnhancementApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideImageEnhancementApiServiceFactory(
      Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ImageEnhancementApiService get() {
    return provideImageEnhancementApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideImageEnhancementApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideImageEnhancementApiServiceFactory(retrofitProvider);
  }

  public static ImageEnhancementApiService provideImageEnhancementApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideImageEnhancementApiService(retrofit));
  }
}
