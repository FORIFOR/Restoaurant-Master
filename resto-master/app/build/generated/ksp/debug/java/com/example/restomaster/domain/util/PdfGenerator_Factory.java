package com.example.restomaster.domain.util;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class PdfGenerator_Factory implements Factory<PdfGenerator> {
  private final Provider<Context> contextProvider;

  private final Provider<QrCodeGenerator> qrCodeGeneratorProvider;

  public PdfGenerator_Factory(Provider<Context> contextProvider,
      Provider<QrCodeGenerator> qrCodeGeneratorProvider) {
    this.contextProvider = contextProvider;
    this.qrCodeGeneratorProvider = qrCodeGeneratorProvider;
  }

  @Override
  public PdfGenerator get() {
    return newInstance(contextProvider.get(), qrCodeGeneratorProvider.get());
  }

  public static PdfGenerator_Factory create(Provider<Context> contextProvider,
      Provider<QrCodeGenerator> qrCodeGeneratorProvider) {
    return new PdfGenerator_Factory(contextProvider, qrCodeGeneratorProvider);
  }

  public static PdfGenerator newInstance(Context context, QrCodeGenerator qrCodeGenerator) {
    return new PdfGenerator(context, qrCodeGenerator);
  }
}
