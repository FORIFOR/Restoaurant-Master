package com.example.restomaster.domain.ocr;

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
public final class MenuOcrProcessor_Factory implements Factory<MenuOcrProcessor> {
  @Override
  public MenuOcrProcessor get() {
    return newInstance();
  }

  public static MenuOcrProcessor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MenuOcrProcessor newInstance() {
    return new MenuOcrProcessor();
  }

  private static final class InstanceHolder {
    private static final MenuOcrProcessor_Factory INSTANCE = new MenuOcrProcessor_Factory();
  }
}
