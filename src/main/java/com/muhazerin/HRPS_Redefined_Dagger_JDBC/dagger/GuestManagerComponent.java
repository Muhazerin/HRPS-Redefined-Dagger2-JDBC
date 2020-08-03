package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import java.util.Scanner;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.control.GuestManager;

import dagger.BindsInstance;
import dagger.Component;

@Component (modules = GuestDaoModule.class)
public interface GuestManagerComponent {
	public GuestManager getGuestManager();
	
	@Component.Factory
	public interface Factory {
		public GuestManagerComponent create(@BindsInstance Scanner sc);
	}
}
