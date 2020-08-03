package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao.RoomDAO;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RoomDaoModule {
	@Binds
	public abstract DataAccess bindsRoomDao(RoomDAO roomDao);
}
