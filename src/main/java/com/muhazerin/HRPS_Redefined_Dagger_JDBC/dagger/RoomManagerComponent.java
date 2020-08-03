package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import java.util.Scanner;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.control.RoomManager;

import dagger.BindsInstance;
import dagger.Component;

@Component (modules = RoomDaoModule.class)
public interface RoomManagerComponent {
	public RoomManager getRoomManager();
	
	@Component.Factory
	public interface Factory {
		public RoomManagerComponent create(@BindsInstance Scanner sc);
	}
}
