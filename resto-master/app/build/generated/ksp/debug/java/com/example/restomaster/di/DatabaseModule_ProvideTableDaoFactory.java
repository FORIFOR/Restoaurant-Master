package com.example.restomaster.di;

import com.example.restomaster.data.local.AppDatabase;
import com.example.restomaster.data.local.dao.TableDao;
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
public final class DatabaseModule_ProvideTableDaoFactory implements Factory<TableDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideTableDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TableDao get() {
    return provideTableDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTableDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTableDaoFactory(databaseProvider);
  }

  public static TableDao provideTableDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTableDao(database));
  }
}
