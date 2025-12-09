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
public final class SuggestAllergensUseCase_Factory implements Factory<SuggestAllergensUseCase> {
  @Override
  public SuggestAllergensUseCase get() {
    return newInstance();
  }

  public static SuggestAllergensUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SuggestAllergensUseCase newInstance() {
    return new SuggestAllergensUseCase();
  }

  private static final class InstanceHolder {
    private static final SuggestAllergensUseCase_Factory INSTANCE = new SuggestAllergensUseCase_Factory();
  }
}
