package com.example.restomaster.di;

import com.example.restomaster.data.remote.MenuOcrApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideMenuOcrApiFactory implements Factory<MenuOcrApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideMenuOcrApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public MenuOcrApi get() {
    return provideMenuOcrApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideMenuOcrApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideMenuOcrApiFactory(retrofitProvider);
  }

  public static MenuOcrApi provideMenuOcrApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideMenuOcrApi(retrofit));
  }
}
