package com.example.restomaster.di;

import com.example.restomaster.data.local.AppDatabase;
import com.example.restomaster.data.local.dao.MenuDao;
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
public final class DatabaseModule_ProvideMenuDaoFactory implements Factory<MenuDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideMenuDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MenuDao get() {
    return provideMenuDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMenuDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMenuDaoFactory(databaseProvider);
  }

  public static MenuDao provideMenuDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMenuDao(database));
  }
}
