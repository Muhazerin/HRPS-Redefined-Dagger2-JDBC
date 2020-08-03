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
public final class RoomDAO_Factory implements Factory<RoomDAO> {
  @Override
  public RoomDAO get() {
    return newInstance();
  }

  public static RoomDAO_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RoomDAO newInstance() {
    return new RoomDAO();
  }

  private static final class InstanceHolder {
    private static final RoomDAO_Factory INSTANCE = new RoomDAO_Factory();
  }
}
