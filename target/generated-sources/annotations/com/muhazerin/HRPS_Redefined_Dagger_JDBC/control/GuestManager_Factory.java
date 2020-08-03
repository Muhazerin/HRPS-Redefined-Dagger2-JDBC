package com.muhazerin.HRPS_Redefined_Dagger_JDBC.control;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;
import dagger.internal.Factory;
import java.util.Scanner;
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
public final class GuestManager_Factory implements Factory<GuestManager> {
  private final Provider<Scanner> scProvider;

  private final Provider<DataAccess> dataAccessProvider;

  public GuestManager_Factory(Provider<Scanner> scProvider,
      Provider<DataAccess> dataAccessProvider) {
    this.scProvider = scProvider;
    this.dataAccessProvider = dataAccessProvider;
  }

  @Override
  public GuestManager get() {
    return newInstance(scProvider.get(), dataAccessProvider.get());
  }

  public static GuestManager_Factory create(Provider<Scanner> scProvider,
      Provider<DataAccess> dataAccessProvider) {
    return new GuestManager_Factory(scProvider, dataAccessProvider);
  }

  public static GuestManager newInstance(Scanner sc, DataAccess dataAccess) {
    return new GuestManager(sc, dataAccess);
  }
}
