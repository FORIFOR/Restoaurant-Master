package com.example.restomaster.di;

import com.example.restomaster.data.local.AppDatabase;
import com.example.restomaster.data.local.dao.ReceiptDao;
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
public final class DatabaseModule_ProvideReceiptDaoFactory implements Factory<ReceiptDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideReceiptDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ReceiptDao get() {
    return provideReceiptDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideReceiptDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideReceiptDaoFactory(databaseProvider);
  }

  public static ReceiptDao provideReceiptDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideReceiptDao(database));
  }
}
