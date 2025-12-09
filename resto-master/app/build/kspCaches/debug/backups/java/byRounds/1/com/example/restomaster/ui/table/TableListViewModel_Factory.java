package com.example.restomaster.ui.table;

import com.example.restomaster.domain.repository.OrderRepository;
import com.example.restomaster.domain.repository.TableRepository;
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
public final class TableListViewModel_Factory implements Factory<TableListViewModel> {
  private final Provider<TableRepository> tableRepositoryProvider;

  private final Provider<OrderRepository> orderRepositoryProvider;

  private final Provider<QrCodeGenerator> qrCodeGeneratorProvider;

  private final Provider<PdfGenerator> pdfGeneratorProvider;

  public TableListViewModel_Factory(Provider<TableRepository> tableRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<QrCodeGenerator> qrCodeGeneratorProvider,
      Provider<PdfGenerator> pdfGeneratorProvider) {
    this.tableRepositoryProvider = tableRepositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
    this.qrCodeGeneratorProvider = qrCodeGeneratorProvider;
    this.pdfGeneratorProvider = pdfGeneratorProvider;
  }

  @Override
  public TableListViewModel get() {
    return newInstance(tableRepositoryProvider.get(), orderRepositoryProvider.get(), qrCodeGeneratorProvider.get(), pdfGeneratorProvider.get());
  }

  public static TableListViewModel_Factory create(Provider<TableRepository> tableRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<QrCodeGenerator> qrCodeGeneratorProvider,
      Provider<PdfGenerator> pdfGeneratorProvider) {
    return new TableListViewModel_Factory(tableRepositoryProvider, orderRepositoryProvider, qrCodeGeneratorProvider, pdfGeneratorProvider);
  }

  public static TableListViewModel newInstance(TableRepository tableRepository,
      OrderRepository orderRepository, QrCodeGenerator qrCodeGenerator, PdfGenerator pdfGenerator) {
    return new TableListViewModel(tableRepository, orderRepository, qrCodeGenerator, pdfGenerator);
  }
}
