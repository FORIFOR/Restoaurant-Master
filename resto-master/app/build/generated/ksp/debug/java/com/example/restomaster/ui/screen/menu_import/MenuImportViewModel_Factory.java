package com.example.restomaster.ui.screen.menu_import;

import com.example.restomaster.data.ocr.MlKitTextRecognizer;
import com.example.restomaster.domain.ocr.MenuOcrProcessor;
import com.example.restomaster.domain.repository.MenuRepository;
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
public final class MenuImportViewModel_Factory implements Factory<MenuImportViewModel> {
  private final Provider<MlKitTextRecognizer> mlKitTextRecognizerProvider;

  private final Provider<MenuOcrProcessor> menuOcrProcessorProvider;

  private final Provider<MenuRepository> menuRepositoryProvider;

  public MenuImportViewModel_Factory(Provider<MlKitTextRecognizer> mlKitTextRecognizerProvider,
      Provider<MenuOcrProcessor> menuOcrProcessorProvider,
      Provider<MenuRepository> menuRepositoryProvider) {
    this.mlKitTextRecognizerProvider = mlKitTextRecognizerProvider;
    this.menuOcrProcessorProvider = menuOcrProcessorProvider;
    this.menuRepositoryProvider = menuRepositoryProvider;
  }

  @Override
  public MenuImportViewModel get() {
    return newInstance(mlKitTextRecognizerProvider.get(), menuOcrProcessorProvider.get(), menuRepositoryProvider.get());
  }

  public static MenuImportViewModel_Factory create(
      Provider<MlKitTextRecognizer> mlKitTextRecognizerProvider,
      Provider<MenuOcrProcessor> menuOcrProcessorProvider,
      Provider<MenuRepository> menuRepositoryProvider) {
    return new MenuImportViewModel_Factory(mlKitTextRecognizerProvider, menuOcrProcessorProvider, menuRepositoryProvider);
  }

  public static MenuImportViewModel newInstance(MlKitTextRecognizer mlKitTextRecognizer,
      MenuOcrProcessor menuOcrProcessor, MenuRepository menuRepository) {
    return new MenuImportViewModel(mlKitTextRecognizer, menuOcrProcessor, menuRepository);
  }
}
