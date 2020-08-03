package com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity;

import java.io.Serializable;

/**
 * 
 * @author muhazerin
 *
 */

public class VipSuiteRoom extends Room implements Serializable{
	private static final long serialVersionUID = 1L;
	private double rate;
	private double defaultRate;
	private String roomType;
			
	// sets the rate of the room
	public VipSuiteRoom(BedType bedType, AvailabilityStatus availabilityStatus,
			boolean wifiEnabled, String facing, boolean smokingAllowed, int roomLevel, int roomNumber) {
		super(bedType, availabilityStatus, wifiEnabled, facing, smokingAllowed, roomLevel, roomNumber);
		defaultRate = rate = 100;
		if (bedType == Room.BedType.SINGLE) {
			rate += 10;
		}
		else if (bedType == Room.BedType.DOUBLE) {
			rate += 20;
		}
		else {
			rate += 30;
		}
		roomType = "VIP Suite";
	}
	
	public VipSuiteRoom(BedType bedType, AvailabilityStatus availabilityStatus, boolean wifiEnabled,
			String facing, boolean smokingAllowed, int roomLevel, int roomNumber, double rate) {
		super(bedType, availabilityStatus, wifiEnabled, facing, smokingAllowed, roomLevel, roomNumber);
		this.rate = rate;
		defaultRate = 100;
		roomType = "VIP Suite";
	}

	@Override
	public double getRate() {
		return rate;
	}

	@Override
	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String getRoomType() {
		return roomType;
	}

	@Override
	public double getDefaultRate() {
		return defaultRate;
	}

	@Override
	public void setDefaultRate(double defaultRate) {
		this.defaultRate = defaultRate;
	}
}
