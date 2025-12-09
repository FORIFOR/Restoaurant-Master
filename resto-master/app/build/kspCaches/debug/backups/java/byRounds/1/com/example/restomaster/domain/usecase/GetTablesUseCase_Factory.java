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
public final class GetTablesUseCase_Factory implements Factory<GetTablesUseCase> {
  private final Provider<TableRepository> repositoryProvider;

  public GetTablesUseCase_Factory(Provider<TableRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetTablesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetTablesUseCase_Factory create(Provider<TableRepository> repositoryProvider) {
    return new GetTablesUseCase_Factory(repositoryProvider);
  }

  public static GetTablesUseCase newInstance(TableRepository repository) {
    return new GetTablesUseCase(repository);
  }
}
