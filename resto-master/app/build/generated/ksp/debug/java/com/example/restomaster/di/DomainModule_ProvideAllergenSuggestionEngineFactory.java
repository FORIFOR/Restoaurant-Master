package com.example.restomaster.di;

import com.example.restomaster.domain.ai.AllergenSuggestionEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class DomainModule_ProvideAllergenSuggestionEngineFactory implements Factory<AllergenSuggestionEngine> {
  @Override
  public AllergenSuggestionEngine get() {
    return provideAllergenSuggestionEngine();
  }

  public static DomainModule_ProvideAllergenSuggestionEngineFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AllergenSuggestionEngine provideAllergenSuggestionEngine() {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideAllergenSuggestionEngine());
  }

  private static final class InstanceHolder {
    private static final DomainModule_ProvideAllergenSuggestionEngineFactory INSTANCE = new DomainModule_ProvideAllergenSuggestionEngineFactory();
  }
}
