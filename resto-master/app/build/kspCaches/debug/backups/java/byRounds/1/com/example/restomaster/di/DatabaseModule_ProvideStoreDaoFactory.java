package com.example.restomaster.di;

import com.example.restomaster.data.local.AppDatabase;
import com.example.restomaster.data.local.dao.StoreDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideStoreDaoFactory implements Factory<StoreDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideStoreDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public StoreDao get() {
    return provideStoreDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideStoreDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideStoreDaoFactory(databaseProvider);
  }

  public static StoreDao provideStoreDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideStoreDao(database));
  }
}
