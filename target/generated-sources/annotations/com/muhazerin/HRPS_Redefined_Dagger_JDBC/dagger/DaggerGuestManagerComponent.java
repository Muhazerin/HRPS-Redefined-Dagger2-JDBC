package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.control.GuestManager;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao.GuestDAO;
import dagger.internal.Preconditions;
import java.util.Scanner;
import javax.annotation.processing.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DaggerGuestManagerComponent implements GuestManagerComponent {
  private final Scanner sc;

  private DaggerGuestManagerComponent(Scanner scParam) {
    this.sc = scParam;
  }

  public static GuestManagerComponent.Factory factory() {
    return new Factory();
  }

  @Override
  public GuestManager getGuestManager() {
    return new GuestManager(sc, new GuestDAO());}

  private static final class Factory implements GuestManagerComponent.Factory {
    @Override
    public GuestManagerComponent create(Scanner sc) {
      Preconditions.checkNotNull(sc);
      return new DaggerGuestManagerComponent(sc);
    }
  }
}
