package com.example.restomaster.data.receipt;

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
public final class ReceiptGenerator_Factory implements Factory<ReceiptGenerator> {
  @Override
  public ReceiptGenerator get() {
    return newInstance();
  }

  public static ReceiptGenerator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ReceiptGenerator newInstance() {
    return new ReceiptGenerator();
  }

  private static final class InstanceHolder {
    private static final ReceiptGenerator_Factory INSTANCE = new ReceiptGenerator_Factory();
  }
}
