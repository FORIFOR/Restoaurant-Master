package com.example.restomaster.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class OpenAIImageClient_Factory implements Factory<OpenAIImageClient> {
  @Override
  public OpenAIImageClient get() {
    return newInstance();
  }

  public static OpenAIImageClient_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OpenAIImageClient newInstance() {
    return new OpenAIImageClient();
  }

  private static final class InstanceHolder {
    private static final OpenAIImageClient_Factory INSTANCE = new OpenAIImageClient_Factory();
  }
}
