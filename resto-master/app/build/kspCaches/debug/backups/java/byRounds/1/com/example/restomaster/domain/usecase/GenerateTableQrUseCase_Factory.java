package com.example.restomaster.domain.usecase;

import com.example.restomaster.domain.repository.TableRepository;
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
public final class GenerateTableQrUseCase_Factory implements Factory<GenerateTableQrUseCase> {
  private final Provider<TableRepository> tableRepositoryProvider;

  public GenerateTableQrUseCase_Factory(Provider<TableRepository> tableRepositoryProvider) {
    this.tableRepositoryProvider = tableRepositoryProvider;
  }

  @Override
  public GenerateTableQrUseCase get() {
    return newInstance(tableRepositoryProvider.get());
  }

  public static GenerateTableQrUseCase_Factory create(
      Provider<TableRepository> tableRepositoryProvider) {
    return new GenerateTableQrUseCase_Factory(tableRepositoryProvider);
  }

  public static GenerateTableQrUseCase newInstance(TableRepository tableRepository) {
    return new GenerateTableQrUseCase(tableRepository);
  }
}
