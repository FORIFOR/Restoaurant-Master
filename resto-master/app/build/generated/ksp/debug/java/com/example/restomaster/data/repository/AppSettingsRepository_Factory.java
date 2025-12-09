package com.example.restomaster.data.repository;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppSettingsRepository_Factory implements Factory<AppSettingsRepository> {
  private final Provider<Context> contextProvider;

  public AppSettingsRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AppSettingsRepository get() {
    return newInstance(contextProvider.get());
  }

  public static AppSettingsRepository_Factory create(Provider<Context> contextProvider) {
    return new AppSettingsRepository_Factory(contextProvider);
  }

  public static AppSettingsRepository newInstance(Context context) {
    return new AppSettingsRepository(context);
  }
}
