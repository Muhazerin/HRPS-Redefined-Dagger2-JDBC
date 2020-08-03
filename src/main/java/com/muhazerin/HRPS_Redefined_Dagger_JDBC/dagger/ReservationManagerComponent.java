package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dagger;

import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.control.ReservationManager;

import dagger.BindsInstance;
import dagger.Component;

@Component (modules = ReservationDaoModule.class)
public interface ReservationManagerComponent {
	public ReservationManager getReservationManager();
	
	@Component.Factory
	public interface Factory {
		public ReservationManagerComponent create(
				@BindsInstance Scanner sc,
				@BindsInstance ScheduledExecutorService scheduledExecutorService);
	}
}
