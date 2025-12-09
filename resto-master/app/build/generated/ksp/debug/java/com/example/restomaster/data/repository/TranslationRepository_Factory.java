package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.dao.MenuTranslationDao;
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
public final class TranslationRepository_Factory implements Factory<TranslationRepository> {
  private final Provider<MenuTranslationDao> translationDaoProvider;

  public TranslationRepository_Factory(Provider<MenuTranslationDao> translationDaoProvider) {
    this.translationDaoProvider = translationDaoProvider;
  }

  @Override
  public TranslationRepository get() {
    return newInstance(translationDaoProvider.get());
  }

  public static TranslationRepository_Factory create(
      Provider<MenuTranslationDao> translationDaoProvider) {
    return new TranslationRepository_Factory(translationDaoProvider);
  }

  public static TranslationRepository newInstance(MenuTranslationDao translationDao) {
    return new TranslationRepository(translationDao);
  }
}
