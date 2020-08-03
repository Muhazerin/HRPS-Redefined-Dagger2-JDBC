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
public final class MenuItemManager_Factory implements Factory<MenuItemManager> {
  private final Provider<Scanner> scProvider;

  private final Provider<DataAccess> dataAccessProvider;

  public MenuItemManager_Factory(Provider<Scanner> scProvider,
      Provider<DataAccess> dataAccessProvider) {
    this.scProvider = scProvider;
    this.dataAccessProvider = dataAccessProvider;
  }

  @Override
  public MenuItemManager get() {
    return newInstance(scProvider.get(), dataAccessProvider.get());
  }

  public static MenuItemManager_Factory create(Provider<Scanner> scProvider,
      Provider<DataAccess> dataAccessProvider) {
    return new MenuItemManager_Factory(scProvider, dataAccessProvider);
  }

  public static MenuItemManager newInstance(Scanner sc, DataAccess dataAccess) {
    return new MenuItemManager(sc, dataAccess);
  }
}
