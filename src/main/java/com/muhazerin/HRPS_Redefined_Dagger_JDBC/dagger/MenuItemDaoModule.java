package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao.MenuItemDAO;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MenuItemDaoModule {
	@Binds
	public abstract DataAccess bindsMenuItemDao(MenuItemDAO menuItemDAO);
}
