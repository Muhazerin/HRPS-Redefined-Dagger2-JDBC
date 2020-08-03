package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.control.ReservationManager;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao.ReservationDAO;
import dagger.internal.Preconditions;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.processing.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DaggerReservationManagerComponent implements ReservationManagerComponent {
  private final Scanner sc;

  private final ScheduledExecutorService scheduledExecutorService;

  private DaggerReservationManagerComponent(Scanner scParam,
      ScheduledExecutorService scheduledExecutorServiceParam) {
    this.sc = scParam;
    this.scheduledExecutorService = scheduledExecutorServiceParam;
  }

  public static ReservationManagerComponent.Factory factory() {
    return new Factory();
  }

  @Override
  public ReservationManager getReservationManager() {
    return new ReservationManager(sc, new ReservationDAO(), scheduledExecutorService);}

  private static final class Factory implements ReservationManagerComponent.Factory {
    @Override
    public ReservationManagerComponent create(Scanner sc,
        ScheduledExecutorService scheduledExecutorService) {
      Preconditions.checkNotNull(sc);
      Preconditions.checkNotNull(scheduledExecutorService);
      return new DaggerReservationManagerComponent(sc, scheduledExecutorService);
    }
  }
}
