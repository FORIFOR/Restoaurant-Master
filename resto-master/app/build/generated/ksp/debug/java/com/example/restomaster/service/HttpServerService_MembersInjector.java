package com.example.restomaster.service;

import com.example.restomaster.data.server.LocalHttpServer;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class HttpServerService_MembersInjector implements MembersInjector<HttpServerService> {
  private final Provider<LocalHttpServer> localHttpServerProvider;

  public HttpServerService_MembersInjector(Provider<LocalHttpServer> localHttpServerProvider) {
    this.localHttpServerProvider = localHttpServerProvider;
  }

  public static MembersInjector<HttpServerService> create(
      Provider<LocalHttpServer> localHttpServerProvider) {
    return new HttpServerService_MembersInjector(localHttpServerProvider);
  }

  @Override
  public void injectMembers(HttpServerService instance) {
    injectLocalHttpServer(instance, localHttpServerProvider.get());
  }

  @InjectedFieldSignature("com.example.restomaster.service.HttpServerService.localHttpServer")
  public static void injectLocalHttpServer(HttpServerService instance,
      LocalHttpServer localHttpServer) {
    instance.localHttpServer = localHttpServer;
  }
}
