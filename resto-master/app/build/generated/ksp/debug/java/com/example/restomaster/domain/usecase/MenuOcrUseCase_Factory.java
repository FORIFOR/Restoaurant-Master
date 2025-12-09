package com.example.restomaster.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class MenuOcrUseCase_Factory implements Factory<MenuOcrUseCase> {
  @Override
  public MenuOcrUseCase get() {
    return newInstance();
  }

  public static MenuOcrUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MenuOcrUseCase newInstance() {
    return new MenuOcrUseCase();
  }

  private static final class InstanceHolder {
    private static final MenuOcrUseCase_Factory INSTANCE = new MenuOcrUseCase_Factory();
  }
}
