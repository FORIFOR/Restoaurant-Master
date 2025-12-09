package com.example.restomaster.data.repository;

import com.example.restomaster.data.local.dao.ReservationDao;
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
public final class ReservationRepositoryImpl_Factory implements Factory<ReservationRepositoryImpl> {
  private final Provider<ReservationDao> reservationDaoProvider;

  public ReservationRepositoryImpl_Factory(Provider<ReservationDao> reservationDaoProvider) {
    this.reservationDaoProvider = reservationDaoProvider;
  }

  @Override
  public ReservationRepositoryImpl get() {
    return newInstance(reservationDaoProvider.get());
  }

  public static ReservationRepositoryImpl_Factory create(
      Provider<ReservationDao> reservationDaoProvider) {
    return new ReservationRepositoryImpl_Factory(reservationDaoProvider);
  }

  public static ReservationRepositoryImpl newInstance(ReservationDao reservationDao) {
    return new ReservationRepositoryImpl(reservationDao);
  }
}
