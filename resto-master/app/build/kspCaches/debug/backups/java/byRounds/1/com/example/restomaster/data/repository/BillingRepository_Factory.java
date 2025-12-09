package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.dao.BillDao;
import com.example.restomaster.data.local.dao.BillItemDao;
import com.example.restomaster.data.local.dao.CallRequestDao;
import com.example.restomaster.data.local.dao.MenuDao;
import com.example.restomaster.data.local.dao.OrderDao;
import com.example.restomaster.data.local.dao.PaymentDao;
import com.example.restomaster.data.local.dao.PaymentItemDao;
import com.example.restomaster.data.local.dao.StoreDao;
import com.example.restomaster.data.local.dao.TableDao;
import com.example.restomaster.data.receipt.ReceiptGenerator;
import com.example.restomaster.data.remote.PayPayApiService;
import com.example.restomaster.data.remote.StripeApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class BillingRepository_Factory implements Factory<BillingRepository> {
  private final Provider<OrderDao> orderDaoProvider;

  private final Provider<TableDao> tableDaoProvider;

  private final Provider<CallRequestDao> callRequestDaoProvider;

  private final Provider<BillDao> billDaoProvider;

  private final Provider<BillItemDao> billItemDaoProvider;

  private final Provider<PaymentDao> paymentDaoProvider;

  private final Provider<PaymentItemDao> paymentItemDaoProvider;

  private final Provider<MenuDao> menuDaoProvider;

  private final Provider<StoreDao> storeDaoProvider;

  private final Provider<StripeApiService> stripeApiServiceProvider;

  private final Provider<PayPayApiService> payPayApiServiceProvider;

  private final Provider<ReceiptGenerator> receiptGeneratorProvider;

  public BillingRepository_Factory(Provider<OrderDao> orderDaoProvider,
      Provider<TableDao> tableDaoProvider, Provider<CallRequestDao> callRequestDaoProvider,
      Provider<BillDao> billDaoProvider, Provider<BillItemDao> billItemDaoProvider,
      Provider<PaymentDao> paymentDaoProvider, Provider<PaymentItemDao> paymentItemDaoProvider,
      Provider<MenuDao> menuDaoProvider, Provider<StoreDao> storeDaoProvider,
      Provider<StripeApiService> stripeApiServiceProvider,
      Provider<PayPayApiService> payPayApiServiceProvider,
      Provider<ReceiptGenerator> receiptGeneratorProvider) {
    this.orderDaoProvider = orderDaoProvider;
    this.tableDaoProvider = tableDaoProvider;
    this.callRequestDaoProvider = callRequestDaoProvider;
    this.billDaoProvider = billDaoProvider;
    this.billItemDaoProvider = billItemDaoProvider;
    this.paymentDaoProvider = paymentDaoProvider;
    this.paymentItemDaoProvider = paymentItemDaoProvider;
    this.menuDaoProvider = menuDaoProvider;
    this.storeDaoProvider = storeDaoProvider;
    this.stripeApiServiceProvider = stripeApiServiceProvider;
    this.payPayApiServiceProvider = payPayApiServiceProvider;
    this.receiptGeneratorProvider = receiptGeneratorProvider;
  }

  @Override
  public BillingRepository get() {
    return newInstance(orderDaoProvider.get(), tableDaoProvider.get(), callRequestDaoProvider.get(), billDaoProvider.get(), billItemDaoProvider.get(), paymentDaoProvider.get(), paymentItemDaoProvider.get(), menuDaoProvider.get(), storeDaoProvider.get(), stripeApiServiceProvider.get(), payPayApiServiceProvider.get(), receiptGeneratorProvider.get());
  }

  public static BillingRepository_Factory create(Provider<OrderDao> orderDaoProvider,
      Provider<TableDao> tableDaoProvider, Provider<CallRequestDao> callRequestDaoProvider,
      Provider<BillDao> billDaoProvider, Provider<BillItemDao> billItemDaoProvider,
      Provider<PaymentDao> paymentDaoProvider, Provider<PaymentItemDao> paymentItemDaoProvider,
      Provider<MenuDao> menuDaoProvider, Provider<StoreDao> storeDaoProvider,
      Provider<StripeApiService> stripeApiServiceProvider,
      Provider<PayPayApiService> payPayApiServiceProvider,
      Provider<ReceiptGenerator> receiptGeneratorProvider) {
    return new BillingRepository_Factory(orderDaoProvider, tableDaoProvider, callRequestDaoProvider, billDaoProvider, billItemDaoProvider, paymentDaoProvider, paymentItemDaoProvider, menuDaoProvider, storeDaoProvider, stripeApiServiceProvider, payPayApiServiceProvider, receiptGeneratorProvider);
  }

  public static BillingRepository newInstance(OrderDao orderDao, TableDao tableDao,
      CallRequestDao callRequestDao, BillDao billDao, BillItemDao billItemDao,
      PaymentDao paymentDao, PaymentItemDao paymentItemDao, MenuDao menuDao, StoreDao storeDao,
      StripeApiService stripeApiService, PayPayApiService payPayApiService,
      ReceiptGenerator receiptGenerator) {
    return new BillingRepository(orderDao, tableDao, callRequestDao, billDao, billItemDao, paymentDao, paymentItemDao, menuDao, storeDao, stripeApiService, payPayApiService, receiptGenerator);
  }
}
