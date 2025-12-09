package com.example.restomaster;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.example.restomaster.data.local.AppDatabase;
import com.example.restomaster.data.local.dao.AllergenFeedbackDao;
import com.example.restomaster.data.local.dao.BillDao;
import com.example.restomaster.data.local.dao.BillItemDao;
import com.example.restomaster.data.local.dao.CallRequestDao;
import com.example.restomaster.data.local.dao.ClosingDao;
import com.example.restomaster.data.local.dao.MenuDao;
import com.example.restomaster.data.local.dao.MenuTranslationDao;
import com.example.restomaster.data.local.dao.OrderDao;
import com.example.restomaster.data.local.dao.PaymentDao;
import com.example.restomaster.data.local.dao.PaymentItemDao;
import com.example.restomaster.data.local.dao.ReceiptDao;
import com.example.restomaster.data.local.dao.StoreDao;
import com.example.restomaster.data.local.dao.TableDao;
import com.example.restomaster.data.ocr.MlKitTextRecognizer;
import com.example.restomaster.data.receipt.ReceiptGenerator;
import com.example.restomaster.data.remote.GeminiApiService;
import com.example.restomaster.data.remote.GeminiDirectClient;
import com.example.restomaster.data.remote.GeminiTranslationClient;
import com.example.restomaster.data.remote.ImageEnhancementApiService;
import com.example.restomaster.data.remote.OpenAIImageClient;
import com.example.restomaster.data.remote.PayPayApiService;
import com.example.restomaster.data.remote.StripeApiService;
import com.example.restomaster.data.repository.AnalyticsRepository;
import com.example.restomaster.data.repository.AppSettingsRepository;
import com.example.restomaster.data.repository.BillingRepository;
import com.example.restomaster.data.repository.ClosingRepository;
import com.example.restomaster.data.repository.MasterDashboardRepository;
import com.example.restomaster.data.repository.MenuRepositoryImpl;
import com.example.restomaster.data.repository.OrderRepositoryImpl;
import com.example.restomaster.data.repository.ReceiptRepository;
import com.example.restomaster.data.repository.TableRepositoryImpl;
import com.example.restomaster.data.repository.TranslationRepository;
import com.example.restomaster.data.server.LocalHttpServer;
import com.example.restomaster.di.DatabaseModule_ProvideAllergenFeedbackDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideAppDatabaseFactory;
import com.example.restomaster.di.DatabaseModule_ProvideBillDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideBillItemDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideCallRequestDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideClosingDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideMenuDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideMenuTranslationDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideOrderDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvidePaymentDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvidePaymentItemDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideReceiptDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideStoreDaoFactory;
import com.example.restomaster.di.DatabaseModule_ProvideTableDaoFactory;
import com.example.restomaster.di.DomainModule_ProvideAllergenSuggestionEngineFactory;
import com.example.restomaster.di.NetworkModule_ProvideGeminiApiFactory;
import com.example.restomaster.di.NetworkModule_ProvideGeminiRetrofitFactory;
import com.example.restomaster.di.NetworkModule_ProvideImageEnhancementApiServiceFactory;
import com.example.restomaster.di.NetworkModule_ProvideOkHttpClientFactory;
import com.example.restomaster.di.NetworkModule_ProvidePayPayApiServiceFactory;
import com.example.restomaster.di.NetworkModule_ProvideStripeApiServiceFactory;
import com.example.restomaster.di.NetworkModule_ProvideStripeRetrofitFactory;
import com.example.restomaster.domain.ai.AllergenSuggestionEngine;
import com.example.restomaster.domain.ocr.MenuOcrProcessor;
import com.example.restomaster.domain.repository.MenuRepository;
import com.example.restomaster.domain.repository.OrderRepository;
import com.example.restomaster.domain.repository.TableRepository;
import com.example.restomaster.domain.usecase.BatchTranslateMenusUseCase;
import com.example.restomaster.domain.usecase.GenerateMenuImageUseCase;
import com.example.restomaster.domain.usecase.GenerateReceiptUseCase;
import com.example.restomaster.domain.usecase.GenerateTranslationUseCase;
import com.example.restomaster.domain.usecase.MenuOcrGeminiUseCase;
import com.example.restomaster.domain.usecase.MenuOcrUseCase;
import com.example.restomaster.domain.util.PdfGenerator;
import com.example.restomaster.domain.util.QrCodeGenerator;
import com.example.restomaster.notification.OrderNotifier;
import com.example.restomaster.service.HttpServerService;
import com.example.restomaster.service.HttpServerService_MembersInjector;
import com.example.restomaster.ui.analytics.AnalyticsViewModel;
import com.example.restomaster.ui.analytics.AnalyticsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.billing.BillingViewModel;
import com.example.restomaster.ui.billing.BillingViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.closing.ClosingViewModel;
import com.example.restomaster.ui.closing.ClosingViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.dashboard.DashboardViewModel;
import com.example.restomaster.ui.dashboard.DashboardViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.dashboard.MasterDashboardViewModel;
import com.example.restomaster.ui.dashboard.MasterDashboardViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.dashboard.OrderListViewModel;
import com.example.restomaster.ui.dashboard.OrderListViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.handy.HandyOrderEntryViewModel;
import com.example.restomaster.ui.handy.HandyOrderEntryViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.handy.HandyTableListViewModel;
import com.example.restomaster.ui.handy.HandyTableListViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.menu.MenuListViewModel;
import com.example.restomaster.ui.menu.MenuListViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.menu.detail.MenuDetailViewModel;
import com.example.restomaster.ui.menu.detail.MenuDetailViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.ocr.OcrResultViewModel;
import com.example.restomaster.ui.ocr.OcrResultViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.order.OrderDetailViewModel;
import com.example.restomaster.ui.order.OrderDetailViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.receipt.ReceiptViewModel;
import com.example.restomaster.ui.receipt.ReceiptViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.sales.SalesHistoryViewModel;
import com.example.restomaster.ui.sales.SalesHistoryViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.screen.menu_import.MenuImportViewModel;
import com.example.restomaster.ui.screen.menu_import.MenuImportViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.settings.SettingsViewModel;
import com.example.restomaster.ui.settings.SettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.table.TableListViewModel;
import com.example.restomaster.ui.table.TableListViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.restomaster.ui.table.detail.TableDetailViewModel;
import com.example.restomaster.ui.table.detail.TableDetailViewModel_HiltModules_KeyModule_ProvideFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SetBuilder;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class DaggerRestoMasterApp_HiltComponents_SingletonC {
  private DaggerRestoMasterApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public RestoMasterApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements RestoMasterApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public RestoMasterApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements RestoMasterApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public RestoMasterApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements RestoMasterApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public RestoMasterApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements RestoMasterApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RestoMasterApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements RestoMasterApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RestoMasterApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements RestoMasterApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public RestoMasterApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements RestoMasterApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public RestoMasterApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends RestoMasterApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends RestoMasterApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends RestoMasterApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends RestoMasterApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return SetBuilder.<String>newSetBuilder(18).add(AnalyticsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(BillingViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(ClosingViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(DashboardViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(HandyOrderEntryViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(HandyTableListViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(MasterDashboardViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(MenuDetailViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(MenuImportViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(MenuListViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(OcrResultViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(OrderDetailViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(OrderListViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(ReceiptViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SalesHistoryViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(TableDetailViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(TableListViewModel_HiltModules_KeyModule_ProvideFactory.provide()).build();
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends RestoMasterApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AnalyticsViewModel> analyticsViewModelProvider;

    private Provider<BillingViewModel> billingViewModelProvider;

    private Provider<ClosingViewModel> closingViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<HandyOrderEntryViewModel> handyOrderEntryViewModelProvider;

    private Provider<HandyTableListViewModel> handyTableListViewModelProvider;

    private Provider<MasterDashboardViewModel> masterDashboardViewModelProvider;

    private Provider<MenuDetailViewModel> menuDetailViewModelProvider;

    private Provider<MenuImportViewModel> menuImportViewModelProvider;

    private Provider<MenuListViewModel> menuListViewModelProvider;

    private Provider<OcrResultViewModel> ocrResultViewModelProvider;

    private Provider<OrderDetailViewModel> orderDetailViewModelProvider;

    private Provider<OrderListViewModel> orderListViewModelProvider;

    private Provider<ReceiptViewModel> receiptViewModelProvider;

    private Provider<SalesHistoryViewModel> salesHistoryViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<TableDetailViewModel> tableDetailViewModelProvider;

    private Provider<TableListViewModel> tableListViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GeminiTranslationClient geminiTranslationClient() {
      return new GeminiTranslationClient(singletonCImpl.provideGeminiApiProvider.get());
    }

    private GenerateTranslationUseCase generateTranslationUseCase() {
      return new GenerateTranslationUseCase(singletonCImpl.menuDao(), singletonCImpl.translationRepositoryProvider.get(), geminiTranslationClient(), singletonCImpl.appSettingsRepositoryProvider.get());
    }

    private GenerateMenuImageUseCase generateMenuImageUseCase() {
      return new GenerateMenuImageUseCase(singletonCImpl.openAIImageClientProvider.get(), singletonCImpl.appSettingsRepositoryProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));
    }

    private BatchTranslateMenusUseCase batchTranslateMenusUseCase() {
      return new BatchTranslateMenusUseCase(singletonCImpl.bindMenuRepositoryProvider.get(), singletonCImpl.translationRepositoryProvider.get(), generateTranslationUseCase());
    }

    private GeminiDirectClient geminiDirectClient() {
      return new GeminiDirectClient(singletonCImpl.provideGeminiApiProvider.get());
    }

    private MenuOcrGeminiUseCase menuOcrGeminiUseCase() {
      return new MenuOcrGeminiUseCase(geminiDirectClient(), singletonCImpl.appSettingsRepositoryProvider.get());
    }

    private GenerateReceiptUseCase generateReceiptUseCase() {
      return new GenerateReceiptUseCase(singletonCImpl.receiptRepositoryProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.analyticsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.billingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.closingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.handyOrderEntryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.handyTableListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.masterDashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.menuDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.menuImportViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.menuListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.ocrResultViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.orderDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.orderListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
      this.receiptViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.salesHistoryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 15);
      this.tableDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 16);
      this.tableListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 17);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(18).put("com.example.restomaster.ui.analytics.AnalyticsViewModel", ((Provider) analyticsViewModelProvider)).put("com.example.restomaster.ui.billing.BillingViewModel", ((Provider) billingViewModelProvider)).put("com.example.restomaster.ui.closing.ClosingViewModel", ((Provider) closingViewModelProvider)).put("com.example.restomaster.ui.dashboard.DashboardViewModel", ((Provider) dashboardViewModelProvider)).put("com.example.restomaster.ui.handy.HandyOrderEntryViewModel", ((Provider) handyOrderEntryViewModelProvider)).put("com.example.restomaster.ui.handy.HandyTableListViewModel", ((Provider) handyTableListViewModelProvider)).put("com.example.restomaster.ui.dashboard.MasterDashboardViewModel", ((Provider) masterDashboardViewModelProvider)).put("com.example.restomaster.ui.menu.detail.MenuDetailViewModel", ((Provider) menuDetailViewModelProvider)).put("com.example.restomaster.ui.screen.menu_import.MenuImportViewModel", ((Provider) menuImportViewModelProvider)).put("com.example.restomaster.ui.menu.MenuListViewModel", ((Provider) menuListViewModelProvider)).put("com.example.restomaster.ui.ocr.OcrResultViewModel", ((Provider) ocrResultViewModelProvider)).put("com.example.restomaster.ui.order.OrderDetailViewModel", ((Provider) orderDetailViewModelProvider)).put("com.example.restomaster.ui.dashboard.OrderListViewModel", ((Provider) orderListViewModelProvider)).put("com.example.restomaster.ui.receipt.ReceiptViewModel", ((Provider) receiptViewModelProvider)).put("com.example.restomaster.ui.sales.SalesHistoryViewModel", ((Provider) salesHistoryViewModelProvider)).put("com.example.restomaster.ui.settings.SettingsViewModel", ((Provider) settingsViewModelProvider)).put("com.example.restomaster.ui.table.detail.TableDetailViewModel", ((Provider) tableDetailViewModelProvider)).put("com.example.restomaster.ui.table.TableListViewModel", ((Provider) tableListViewModelProvider)).build();
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return Collections.<String, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.restomaster.ui.analytics.AnalyticsViewModel 
          return (T) new AnalyticsViewModel(singletonCImpl.analyticsRepositoryProvider.get(), singletonCImpl.billingRepositoryProvider.get());

          case 1: // com.example.restomaster.ui.billing.BillingViewModel 
          return (T) new BillingViewModel(singletonCImpl.billingRepositoryProvider.get());

          case 2: // com.example.restomaster.ui.closing.ClosingViewModel 
          return (T) new ClosingViewModel(singletonCImpl.closingRepositoryProvider.get());

          case 3: // com.example.restomaster.ui.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.bindTableRepositoryProvider.get(), singletonCImpl.bindOrderRepositoryProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.example.restomaster.ui.handy.HandyOrderEntryViewModel 
          return (T) new HandyOrderEntryViewModel(singletonCImpl.bindMenuRepositoryProvider.get(), singletonCImpl.bindOrderRepositoryProvider.get());

          case 5: // com.example.restomaster.ui.handy.HandyTableListViewModel 
          return (T) new HandyTableListViewModel(singletonCImpl.bindTableRepositoryProvider.get(), singletonCImpl.bindOrderRepositoryProvider.get());

          case 6: // com.example.restomaster.ui.dashboard.MasterDashboardViewModel 
          return (T) new MasterDashboardViewModel(singletonCImpl.masterDashboardRepositoryProvider.get(), singletonCImpl.bindOrderRepositoryProvider.get(), singletonCImpl.orderNotifierProvider.get());

          case 7: // com.example.restomaster.ui.menu.detail.MenuDetailViewModel 
          return (T) new MenuDetailViewModel(singletonCImpl.bindMenuRepositoryProvider.get(), singletonCImpl.translationRepositoryProvider.get(), viewModelCImpl.generateTranslationUseCase(), viewModelCImpl.generateMenuImageUseCase(), singletonCImpl.provideAllergenSuggestionEngineProvider.get(), singletonCImpl.allergenFeedbackDao(), viewModelCImpl.savedStateHandle);

          case 8: // com.example.restomaster.ui.screen.menu_import.MenuImportViewModel 
          return (T) new MenuImportViewModel(new MlKitTextRecognizer(), new MenuOcrProcessor(), singletonCImpl.bindMenuRepositoryProvider.get());

          case 9: // com.example.restomaster.ui.menu.MenuListViewModel 
          return (T) new MenuListViewModel(singletonCImpl.bindMenuRepositoryProvider.get(), singletonCImpl.translationRepositoryProvider.get(), viewModelCImpl.batchTranslateMenusUseCase());

          case 10: // com.example.restomaster.ui.ocr.OcrResultViewModel 
          return (T) new OcrResultViewModel(new MenuOcrUseCase(), viewModelCImpl.menuOcrGeminiUseCase(), singletonCImpl.bindMenuRepositoryProvider.get());

          case 11: // com.example.restomaster.ui.order.OrderDetailViewModel 
          return (T) new OrderDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.orderDao(), singletonCImpl.menuDao());

          case 12: // com.example.restomaster.ui.dashboard.OrderListViewModel 
          return (T) new OrderListViewModel(singletonCImpl.bindOrderRepositoryProvider.get());

          case 13: // com.example.restomaster.ui.receipt.ReceiptViewModel 
          return (T) new ReceiptViewModel(viewModelCImpl.generateReceiptUseCase(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), viewModelCImpl.savedStateHandle);

          case 14: // com.example.restomaster.ui.sales.SalesHistoryViewModel 
          return (T) new SalesHistoryViewModel(singletonCImpl.bindOrderRepositoryProvider.get());

          case 15: // com.example.restomaster.ui.settings.SettingsViewModel 
          return (T) new SettingsViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.appSettingsRepositoryProvider.get(), singletonCImpl.billingRepositoryProvider.get(), singletonCImpl.storeDao());

          case 16: // com.example.restomaster.ui.table.detail.TableDetailViewModel 
          return (T) new TableDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.tableDao(), singletonCImpl.orderDao(), singletonCImpl.callRequestDao(), singletonCImpl.bindTableRepositoryProvider.get(), singletonCImpl.menuDao(), singletonCImpl.billingRepositoryProvider.get());

          case 17: // com.example.restomaster.ui.table.TableListViewModel 
          return (T) new TableListViewModel(singletonCImpl.bindTableRepositoryProvider.get(), singletonCImpl.bindOrderRepositoryProvider.get(), new QrCodeGenerator(), singletonCImpl.pdfGenerator());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends RestoMasterApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends RestoMasterApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectHttpServerService(HttpServerService httpServerService) {
      injectHttpServerService2(httpServerService);
    }

    private HttpServerService injectHttpServerService2(HttpServerService instance) {
      HttpServerService_MembersInjector.injectLocalHttpServer(instance, singletonCImpl.localHttpServerProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends RestoMasterApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideAppDatabaseProvider;

    private Provider<AnalyticsRepository> analyticsRepositoryProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<Retrofit> provideStripeRetrofitProvider;

    private Provider<StripeApiService> provideStripeApiServiceProvider;

    private Provider<PayPayApiService> providePayPayApiServiceProvider;

    private Provider<ReceiptGenerator> receiptGeneratorProvider;

    private Provider<BillingRepository> billingRepositoryProvider;

    private Provider<ClosingRepository> closingRepositoryProvider;

    private Provider<TableRepositoryImpl> tableRepositoryImplProvider;

    private Provider<TableRepository> bindTableRepositoryProvider;

    private Provider<OrderRepositoryImpl> orderRepositoryImplProvider;

    private Provider<OrderRepository> bindOrderRepositoryProvider;

    private Provider<ImageEnhancementApiService> provideImageEnhancementApiServiceProvider;

    private Provider<MenuRepositoryImpl> menuRepositoryImplProvider;

    private Provider<MenuRepository> bindMenuRepositoryProvider;

    private Provider<MasterDashboardRepository> masterDashboardRepositoryProvider;

    private Provider<OrderNotifier> orderNotifierProvider;

    private Provider<TranslationRepository> translationRepositoryProvider;

    private Provider<Retrofit> provideGeminiRetrofitProvider;

    private Provider<GeminiApiService> provideGeminiApiProvider;

    private Provider<AppSettingsRepository> appSettingsRepositoryProvider;

    private Provider<OpenAIImageClient> openAIImageClientProvider;

    private Provider<AllergenSuggestionEngine> provideAllergenSuggestionEngineProvider;

    private Provider<ReceiptRepository> receiptRepositoryProvider;

    private Provider<LocalHttpServer> localHttpServerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private BillDao billDao() {
      return DatabaseModule_ProvideBillDaoFactory.provideBillDao(provideAppDatabaseProvider.get());
    }

    private BillItemDao billItemDao() {
      return DatabaseModule_ProvideBillItemDaoFactory.provideBillItemDao(provideAppDatabaseProvider.get());
    }

    private TableDao tableDao() {
      return DatabaseModule_ProvideTableDaoFactory.provideTableDao(provideAppDatabaseProvider.get());
    }

    private OrderDao orderDao() {
      return DatabaseModule_ProvideOrderDaoFactory.provideOrderDao(provideAppDatabaseProvider.get());
    }

    private CallRequestDao callRequestDao() {
      return DatabaseModule_ProvideCallRequestDaoFactory.provideCallRequestDao(provideAppDatabaseProvider.get());
    }

    private PaymentDao paymentDao() {
      return DatabaseModule_ProvidePaymentDaoFactory.providePaymentDao(provideAppDatabaseProvider.get());
    }

    private PaymentItemDao paymentItemDao() {
      return DatabaseModule_ProvidePaymentItemDaoFactory.providePaymentItemDao(provideAppDatabaseProvider.get());
    }

    private MenuDao menuDao() {
      return DatabaseModule_ProvideMenuDaoFactory.provideMenuDao(provideAppDatabaseProvider.get());
    }

    private StoreDao storeDao() {
      return DatabaseModule_ProvideStoreDaoFactory.provideStoreDao(provideAppDatabaseProvider.get());
    }

    private ClosingDao closingDao() {
      return DatabaseModule_ProvideClosingDaoFactory.provideClosingDao(provideAppDatabaseProvider.get());
    }

    private PdfGenerator pdfGenerator() {
      return new PdfGenerator(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule), new QrCodeGenerator());
    }

    private MenuTranslationDao menuTranslationDao() {
      return DatabaseModule_ProvideMenuTranslationDaoFactory.provideMenuTranslationDao(provideAppDatabaseProvider.get());
    }

    private AllergenFeedbackDao allergenFeedbackDao() {
      return DatabaseModule_ProvideAllergenFeedbackDaoFactory.provideAllergenFeedbackDao(provideAppDatabaseProvider.get());
    }

    private ReceiptDao receiptDao() {
      return DatabaseModule_ProvideReceiptDaoFactory.provideReceiptDao(provideAppDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideAppDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 1));
      this.analyticsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AnalyticsRepository>(singletonCImpl, 0));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 5));
      this.provideStripeRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 4));
      this.provideStripeApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<StripeApiService>(singletonCImpl, 3));
      this.providePayPayApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<PayPayApiService>(singletonCImpl, 6));
      this.receiptGeneratorProvider = DoubleCheck.provider(new SwitchingProvider<ReceiptGenerator>(singletonCImpl, 7));
      this.billingRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BillingRepository>(singletonCImpl, 2));
      this.closingRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ClosingRepository>(singletonCImpl, 8));
      this.tableRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 9);
      this.bindTableRepositoryProvider = DoubleCheck.provider((Provider) tableRepositoryImplProvider);
      this.orderRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 10);
      this.bindOrderRepositoryProvider = DoubleCheck.provider((Provider) orderRepositoryImplProvider);
      this.provideImageEnhancementApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<ImageEnhancementApiService>(singletonCImpl, 12));
      this.menuRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 11);
      this.bindMenuRepositoryProvider = DoubleCheck.provider((Provider) menuRepositoryImplProvider);
      this.masterDashboardRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<MasterDashboardRepository>(singletonCImpl, 13));
      this.orderNotifierProvider = DoubleCheck.provider(new SwitchingProvider<OrderNotifier>(singletonCImpl, 14));
      this.translationRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TranslationRepository>(singletonCImpl, 15));
      this.provideGeminiRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 17));
      this.provideGeminiApiProvider = DoubleCheck.provider(new SwitchingProvider<GeminiApiService>(singletonCImpl, 16));
      this.appSettingsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AppSettingsRepository>(singletonCImpl, 18));
      this.openAIImageClientProvider = DoubleCheck.provider(new SwitchingProvider<OpenAIImageClient>(singletonCImpl, 19));
      this.provideAllergenSuggestionEngineProvider = DoubleCheck.provider(new SwitchingProvider<AllergenSuggestionEngine>(singletonCImpl, 20));
      this.receiptRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ReceiptRepository>(singletonCImpl, 21));
      this.localHttpServerProvider = DoubleCheck.provider(new SwitchingProvider<LocalHttpServer>(singletonCImpl, 22));
    }

    @Override
    public void injectRestoMasterApp(RestoMasterApp restoMasterApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.restomaster.data.repository.AnalyticsRepository 
          return (T) new AnalyticsRepository(singletonCImpl.billDao(), singletonCImpl.billItemDao(), singletonCImpl.tableDao());

          case 1: // com.example.restomaster.data.local.AppDatabase 
          return (T) DatabaseModule_ProvideAppDatabaseFactory.provideAppDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.example.restomaster.data.repository.BillingRepository 
          return (T) new BillingRepository(singletonCImpl.orderDao(), singletonCImpl.tableDao(), singletonCImpl.callRequestDao(), singletonCImpl.billDao(), singletonCImpl.billItemDao(), singletonCImpl.paymentDao(), singletonCImpl.paymentItemDao(), singletonCImpl.menuDao(), singletonCImpl.storeDao(), singletonCImpl.provideStripeApiServiceProvider.get(), singletonCImpl.providePayPayApiServiceProvider.get(), singletonCImpl.receiptGeneratorProvider.get());

          case 3: // com.example.restomaster.data.remote.StripeApiService 
          return (T) NetworkModule_ProvideStripeApiServiceFactory.provideStripeApiService(singletonCImpl.provideStripeRetrofitProvider.get());

          case 4: // @javax.inject.Named("stripe") retrofit2.Retrofit 
          return (T) NetworkModule_ProvideStripeRetrofitFactory.provideStripeRetrofit(singletonCImpl.provideOkHttpClientProvider.get());

          case 5: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          case 6: // com.example.restomaster.data.remote.PayPayApiService 
          return (T) NetworkModule_ProvidePayPayApiServiceFactory.providePayPayApiService(singletonCImpl.provideStripeRetrofitProvider.get());

          case 7: // com.example.restomaster.data.receipt.ReceiptGenerator 
          return (T) new ReceiptGenerator();

          case 8: // com.example.restomaster.data.repository.ClosingRepository 
          return (T) new ClosingRepository(singletonCImpl.closingDao());

          case 9: // com.example.restomaster.data.repository.TableRepositoryImpl 
          return (T) new TableRepositoryImpl(singletonCImpl.tableDao(), new QrCodeGenerator(), singletonCImpl.pdfGenerator());

          case 10: // com.example.restomaster.data.repository.OrderRepositoryImpl 
          return (T) new OrderRepositoryImpl(singletonCImpl.orderDao(), singletonCImpl.provideAppDatabaseProvider.get());

          case 11: // com.example.restomaster.data.repository.MenuRepositoryImpl 
          return (T) new MenuRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get(), singletonCImpl.provideImageEnhancementApiServiceProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 12: // com.example.restomaster.data.remote.ImageEnhancementApiService 
          return (T) NetworkModule_ProvideImageEnhancementApiServiceFactory.provideImageEnhancementApiService(singletonCImpl.provideStripeRetrofitProvider.get());

          case 13: // com.example.restomaster.data.repository.MasterDashboardRepository 
          return (T) new MasterDashboardRepository(singletonCImpl.tableDao(), singletonCImpl.orderDao(), singletonCImpl.callRequestDao());

          case 14: // com.example.restomaster.notification.OrderNotifier 
          return (T) new OrderNotifier(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 15: // com.example.restomaster.data.repository.TranslationRepository 
          return (T) new TranslationRepository(singletonCImpl.menuTranslationDao());

          case 16: // com.example.restomaster.data.remote.GeminiApiService 
          return (T) NetworkModule_ProvideGeminiApiFactory.provideGeminiApi(singletonCImpl.provideGeminiRetrofitProvider.get());

          case 17: // @javax.inject.Named("gemini") retrofit2.Retrofit 
          return (T) NetworkModule_ProvideGeminiRetrofitFactory.provideGeminiRetrofit(singletonCImpl.provideOkHttpClientProvider.get());

          case 18: // com.example.restomaster.data.repository.AppSettingsRepository 
          return (T) new AppSettingsRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 19: // com.example.restomaster.data.remote.OpenAIImageClient 
          return (T) new OpenAIImageClient();

          case 20: // com.example.restomaster.domain.ai.AllergenSuggestionEngine 
          return (T) DomainModule_ProvideAllergenSuggestionEngineFactory.provideAllergenSuggestionEngine();

          case 21: // com.example.restomaster.data.repository.ReceiptRepository 
          return (T) new ReceiptRepository(singletonCImpl.receiptDao(), singletonCImpl.billDao(), singletonCImpl.billItemDao(), singletonCImpl.storeDao());

          case 22: // com.example.restomaster.data.server.LocalHttpServer 
          return (T) new LocalHttpServer(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.bindOrderRepositoryProvider.get(), singletonCImpl.bindMenuRepositoryProvider.get(), singletonCImpl.tableDao(), singletonCImpl.storeDao(), singletonCImpl.translationRepositoryProvider.get(), singletonCImpl.callRequestDao(), singletonCImpl.billingRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
