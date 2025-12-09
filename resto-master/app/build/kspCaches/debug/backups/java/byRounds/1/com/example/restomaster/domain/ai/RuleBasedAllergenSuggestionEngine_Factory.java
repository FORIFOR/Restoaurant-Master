package com.example.restomaster.domain.ai;

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
public final class RuleBasedAllergenSuggestionEngine_Factory implements Factory<RuleBasedAllergenSuggestionEngine> {
  @Override
  public RuleBasedAllergenSuggestionEngine get() {
    return newInstance();
  }

  public static RuleBasedAllergenSuggestionEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RuleBasedAllergenSuggestionEngine newInstance() {
    return new RuleBasedAllergenSuggestionEngine();
  }

  private static final class InstanceHolder {
    private static final RuleBasedAllergenSuggestionEngine_Factory INSTANCE = new RuleBasedAllergenSuggestionEngine_Factory();
  }
}
