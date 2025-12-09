package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.dao.TableDao;
import com.example.restomaster.domain.util.PdfGenerator;
import com.example.restomaster.domain.util.QrCodeGenerator;
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
public final class TableRepositoryImpl_Factory implements Factory<TableRepositoryImpl> {
  private final Provider<TableDao> tableDaoProvider;

  private final Provider<QrCodeGenerator> qrCodeGeneratorProvider;

  private final Provider<PdfGenerator> pdfGeneratorProvider;

  public TableRepositoryImpl_Factory(Provider<TableDao> tableDaoProvider,
      Provider<QrCodeGenerator> qrCodeGeneratorProvider,
      Provider<PdfGenerator> pdfGeneratorProvider) {
    this.tableDaoProvider = tableDaoProvider;
    this.qrCodeGeneratorProvider = qrCodeGeneratorProvider;
    this.pdfGeneratorProvider = pdfGeneratorProvider;
  }

  @Override
  public TableRepositoryImpl get() {
    return newInstance(tableDaoProvider.get(), qrCodeGeneratorProvider.get(), pdfGeneratorProvider.get());
  }

  public static TableRepositoryImpl_Factory create(Provider<TableDao> tableDaoProvider,
      Provider<QrCodeGenerator> qrCodeGeneratorProvider,
      Provider<PdfGenerator> pdfGeneratorProvider) {
    return new TableRepositoryImpl_Factory(tableDaoProvider, qrCodeGeneratorProvider, pdfGeneratorProvider);
  }

  public static TableRepositoryImpl newInstance(TableDao tableDao, QrCodeGenerator qrCodeGenerator,
      PdfGenerator pdfGenerator) {
    return new TableRepositoryImpl(tableDao, qrCodeGenerator, pdfGenerator);
  }
}
