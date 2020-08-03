package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.control.RoomManager;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao.RoomDAO;
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
public final class DaggerRoomManagerComponent implements RoomManagerComponent {
  private final Scanner sc;

  private DaggerRoomManagerComponent(Scanner scParam) {
    this.sc = scParam;
  }

  public static RoomManagerComponent.Factory factory() {
    return new Factory();
  }

  @Override
  public RoomManager getRoomManager() {
    return new RoomManager(sc, new RoomDAO());}

  private static final class Factory implements RoomManagerComponent.Factory {
    @Override
    public RoomManagerComponent create(Scanner sc) {
      Preconditions.checkNotNull(sc);
      return new DaggerRoomManagerComponent(sc);
    }
  }
}
