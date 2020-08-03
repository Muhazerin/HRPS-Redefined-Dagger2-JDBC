package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import java.util.Scanner;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.control.MenuItemManager;

import dagger.BindsInstance;
import dagger.Component;

@Component (modules = MenuItemDaoModule.class)
public interface MenuItemManagerComponent {
	public MenuItemManager getMenuItemManager();
	
	@Component.Factory
	public interface Factory {
		public MenuItemManagerComponent create(@BindsInstance Scanner sc);
	}
}
