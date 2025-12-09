package com.example.restomaster.data.repository;

import android.content.Context;
import com.example.restomaster.data.local.AppDatabase;
import com.example.restomaster.data.remote.ImageEnhancementApiService;
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
public final class MenuRepositoryImpl_Factory implements Factory<MenuRepositoryImpl> {
  private final Provider<AppDatabase> databaseProvider;

  private final Provider<ImageEnhancementApiService> imageEnhancementApiServiceProvider;

  private final Provider<Context> contextProvider;

  public MenuRepositoryImpl_Factory(Provider<AppDatabase> databaseProvider,
      Provider<ImageEnhancementApiService> imageEnhancementApiServiceProvider,
      Provider<Context> contextProvider) {
    this.databaseProvider = databaseProvider;
    this.imageEnhancementApiServiceProvider = imageEnhancementApiServiceProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public MenuRepositoryImpl get() {
    return newInstance(databaseProvider.get(), imageEnhancementApiServiceProvider.get(), contextProvider.get());
  }

  public static MenuRepositoryImpl_Factory create(Provider<AppDatabase> databaseProvider,
      Provider<ImageEnhancementApiService> imageEnhancementApiServiceProvider,
      Provider<Context> contextProvider) {
    return new MenuRepositoryImpl_Factory(databaseProvider, imageEnhancementApiServiceProvider, contextProvider);
  }

  public static MenuRepositoryImpl newInstance(AppDatabase database,
      ImageEnhancementApiService imageEnhancementApiService, Context context) {
    return new MenuRepositoryImpl(database, imageEnhancementApiService, context);
  }
}
