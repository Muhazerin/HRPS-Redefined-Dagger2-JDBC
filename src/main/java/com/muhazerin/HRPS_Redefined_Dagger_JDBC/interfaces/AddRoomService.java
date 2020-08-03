package com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Reservation;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.RoomService;

public interface AddRoomService {
	public void addRoomService(Reservation reservation, RoomService roomService);
}
