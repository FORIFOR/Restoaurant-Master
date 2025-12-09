package com.example.restomaster.notification;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class OrderNotifier_Factory implements Factory<OrderNotifier> {
  private final Provider<Context> contextProvider;

  public OrderNotifier_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public OrderNotifier get() {
    return newInstance(contextProvider.get());
  }

  public static OrderNotifier_Factory create(Provider<Context> contextProvider) {
    return new OrderNotifier_Factory(contextProvider);
  }

  public static OrderNotifier newInstance(Context context) {
    return new OrderNotifier(context);
  }
}
