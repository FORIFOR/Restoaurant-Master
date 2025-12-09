package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.dao.ClosingDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ClosingRepository_Factory implements Factory<ClosingRepository> {
  private final Provider<ClosingDao> closingDaoProvider;

  public ClosingRepository_Factory(Provider<ClosingDao> closingDaoProvider) {
    this.closingDaoProvider = closingDaoProvider;
  }

  @Override
  public ClosingRepository get() {
    return newInstance(closingDaoProvider.get());
  }

  public static ClosingRepository_Factory create(Provider<ClosingDao> closingDaoProvider) {
    return new ClosingRepository_Factory(closingDaoProvider);
  }

  public static ClosingRepository newInstance(ClosingDao closingDao) {
    return new ClosingRepository(closingDao);
  }
}
