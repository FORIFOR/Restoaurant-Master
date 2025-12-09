package com.example.restomaster.data.ocr;

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
public final class MlKitTextRecognizer_Factory implements Factory<MlKitTextRecognizer> {
  @Override
  public MlKitTextRecognizer get() {
    return newInstance();
  }

  public static MlKitTextRecognizer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MlKitTextRecognizer newInstance() {
    return new MlKitTextRecognizer();
  }

  private static final class InstanceHolder {
    private static final MlKitTextRecognizer_Factory INSTANCE = new MlKitTextRecognizer_Factory();
  }
}
