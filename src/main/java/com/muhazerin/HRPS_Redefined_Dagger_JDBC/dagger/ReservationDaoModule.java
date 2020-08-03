package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao.ReservationDAO;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ReservationDaoModule {
	@Binds
	public abstract DataAccess bindsReservationDao(ReservationDAO reservationDAO);
}
