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
public final class GuestDAO_Factory implements Factory<GuestDAO> {
  @Override
  public GuestDAO get() {
    return newInstance();
  }

  public static GuestDAO_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GuestDAO newInstance() {
    return new GuestDAO();
  }

  private static final class InstanceHolder {
    private static final GuestDAO_Factory INSTANCE = new GuestDAO_Factory();
  }
}
