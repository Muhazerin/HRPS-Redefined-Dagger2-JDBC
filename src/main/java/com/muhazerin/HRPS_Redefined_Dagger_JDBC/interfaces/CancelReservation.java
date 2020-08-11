package com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Guest;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Room;

public interface CancelReservation {
	public Room cancelReservation(Guest guest);
}
