package com.example.restomaster.di;

import com.example.restomaster.data.remote.PayPayApiService;
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
public final class NetworkModule_ProvidePayPayApiServiceFactory implements Factory<PayPayApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvidePayPayApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public PayPayApiService get() {
    return providePayPayApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvidePayPayApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvidePayPayApiServiceFactory(retrofitProvider);
  }

  public static PayPayApiService providePayPayApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.providePayPayApiService(retrofit));
  }
}
