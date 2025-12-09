package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.dao.StoreDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class StoreRepositoryImpl_Factory implements Factory<StoreRepositoryImpl> {
  private final Provider<StoreDao> storeDaoProvider;

  public StoreRepositoryImpl_Factory(Provider<StoreDao> storeDaoProvider) {
    this.storeDaoProvider = storeDaoProvider;
  }

  @Override
  public StoreRepositoryImpl get() {
    return newInstance(storeDaoProvider.get());
  }

  public static StoreRepositoryImpl_Factory create(Provider<StoreDao> storeDaoProvider) {
    return new StoreRepositoryImpl_Factory(storeDaoProvider);
  }

  public static StoreRepositoryImpl newInstance(StoreDao storeDao) {
    return new StoreRepositoryImpl(storeDao);
  }
}
