package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao.GuestDAO;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class GuestDaoModule {
	@Binds
	public abstract DataAccess bindsGuestDao(GuestDAO guestDAO);
}
