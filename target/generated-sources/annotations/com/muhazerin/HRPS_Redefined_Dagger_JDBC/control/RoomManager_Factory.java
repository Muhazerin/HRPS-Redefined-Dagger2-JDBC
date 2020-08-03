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
public final class RoomManager_Factory implements Factory<RoomManager> {
  private final Provider<Scanner> scProvider;

  private final Provider<DataAccess> dataAccessProvider;

  public RoomManager_Factory(Provider<Scanner> scProvider,
      Provider<DataAccess> dataAccessProvider) {
    this.scProvider = scProvider;
    this.dataAccessProvider = dataAccessProvider;
  }

  @Override
  public RoomManager get() {
    return newInstance(scProvider.get(), dataAccessProvider.get());
  }

  public static RoomManager_Factory create(Provider<Scanner> scProvider,
      Provider<DataAccess> dataAccessProvider) {
    return new RoomManager_Factory(scProvider, dataAccessProvider);
  }

  public static RoomManager newInstance(Scanner sc, DataAccess dataAccess) {
    return new RoomManager(sc, dataAccess);
  }
}
