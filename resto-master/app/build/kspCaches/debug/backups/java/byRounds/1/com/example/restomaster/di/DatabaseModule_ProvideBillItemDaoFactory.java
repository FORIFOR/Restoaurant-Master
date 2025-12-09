package com.example.restomaster.di;

import com.example.restomaster.data.local.AppDatabase;
import com.example.restomaster.data.local.dao.BillItemDao;
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
public final class DatabaseModule_ProvideBillItemDaoFactory implements Factory<BillItemDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideBillItemDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BillItemDao get() {
    return provideBillItemDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideBillItemDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideBillItemDaoFactory(databaseProvider);
  }

  public static BillItemDao provideBillItemDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBillItemDao(database));
  }
}
