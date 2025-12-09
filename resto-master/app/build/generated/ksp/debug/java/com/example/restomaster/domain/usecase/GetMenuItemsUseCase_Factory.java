package com.example.restomaster.domain.usecase;

import com.example.restomaster.domain.repository.MenuRepository;
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
public final class GetMenuItemsUseCase_Factory implements Factory<GetMenuItemsUseCase> {
  private final Provider<MenuRepository> repositoryProvider;

  public GetMenuItemsUseCase_Factory(Provider<MenuRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetMenuItemsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetMenuItemsUseCase_Factory create(Provider<MenuRepository> repositoryProvider) {
    return new GetMenuItemsUseCase_Factory(repositoryProvider);
  }

  public static GetMenuItemsUseCase newInstance(MenuRepository repository) {
    return new GetMenuItemsUseCase(repository);
  }
}
