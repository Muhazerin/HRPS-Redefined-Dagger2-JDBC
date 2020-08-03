package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.control.MenuItemManager;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao.MenuItemDAO;
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
public final class DaggerMenuItemManagerComponent implements MenuItemManagerComponent {
  private final Scanner sc;

  private DaggerMenuItemManagerComponent(Scanner scParam) {
    this.sc = scParam;
  }

  public static MenuItemManagerComponent.Factory factory() {
    return new Factory();
  }

  @Override
  public MenuItemManager getMenuItemManager() {
    return new MenuItemManager(sc, new MenuItemDAO());}

  private static final class Factory implements MenuItemManagerComponent.Factory {
    @Override
    public MenuItemManagerComponent create(Scanner sc) {
      Preconditions.checkNotNull(sc);
      return new DaggerMenuItemManagerComponent(sc);
    }
  }
}
