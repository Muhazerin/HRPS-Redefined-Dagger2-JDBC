package com.muhazerin.HRPS_Redefined_Dagger_JDBC.control;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;
import dagger.internal.Factory;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ReservationManager_Factory implements Factory<ReservationManager> {
  private final Provider<Scanner> scProvider;

  private final Provider<DataAccess> dataAccessProvider;

  private final Provider<ScheduledExecutorService> scheduledExecutorServiceProvider;

  public ReservationManager_Factory(Provider<Scanner> scProvider,
      Provider<DataAccess> dataAccessProvider,
      Provider<ScheduledExecutorService> scheduledExecutorServiceProvider) {
    this.scProvider = scProvider;
    this.dataAccessProvider = dataAccessProvider;
    this.scheduledExecutorServiceProvider = scheduledExecutorServiceProvider;
  }

  @Override
  public ReservationManager get() {
    return newInstance(scProvider.get(), dataAccessProvider.get(), scheduledExecutorServiceProvider.get());
  }

  public static ReservationManager_Factory create(Provider<Scanner> scProvider,
      Provider<DataAccess> dataAccessProvider,
      Provider<ScheduledExecutorService> scheduledExecutorServiceProvider) {
    return new ReservationManager_Factory(scProvider, dataAccessProvider, scheduledExecutorServiceProvider);
  }

  public static ReservationManager newInstance(Scanner sc, DataAccess dataAccess,
      ScheduledExecutorService scheduledExecutorService) {
    return new ReservationManager(sc, dataAccess, scheduledExecutorService);
  }
}
