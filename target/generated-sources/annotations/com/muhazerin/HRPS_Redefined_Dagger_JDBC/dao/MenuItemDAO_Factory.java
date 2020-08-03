package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao;

import dagger.internal.Factory;
import javax.annotation.processing.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MenuItemDAO_Factory implements Factory<MenuItemDAO> {
  @Override
  public MenuItemDAO get() {
    return newInstance();
  }

  public static MenuItemDAO_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MenuItemDAO newInstance() {
    return new MenuItemDAO();
  }

  private static final class InstanceHolder {
    private static final MenuItemDAO_Factory INSTANCE = new MenuItemDAO_Factory();
  }
}
