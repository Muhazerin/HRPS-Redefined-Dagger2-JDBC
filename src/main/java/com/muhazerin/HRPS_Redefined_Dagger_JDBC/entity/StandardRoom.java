package com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity;

import java.io.Serializable;

/**
 * 
 * @author muhazerin
 *
 */

public class StandardRoom extends Room implements Serializable{
	private static final long serialVersionUID = 1L;
	private double rate;
	private double defaultRate;
	private String roomType;
			
	// sets the rate of the room
	public StandardRoom(BedType bedType, AvailabilityStatus availabilityStatus,
			boolean wifiEnabled, String facing, boolean smokingAllowed, int roomLevel, int roomNumber) {
		super(bedType, availabilityStatus, wifiEnabled, facing, smokingAllowed, roomLevel, roomNumber);
		defaultRate = rate = 75;
		if (bedType == Room.BedType.SINGLE) {
			rate += 10;
		}
		else if (bedType == Room.BedType.DOUBLE) {
			rate += 20;
		}
		else {
			rate += 30;
		}
		roomType = "Standard";
	}
	
	public StandardRoom(BedType bedType, AvailabilityStatus availabilityStatus, boolean wifiEnabled,
			String facing, boolean smokingAllowed, int roomLevel, int roomNumber, double rate) {
		super(bedType, availabilityStatus, wifiEnabled, facing, smokingAllowed, roomLevel, roomNumber);
		this.rate = rate;
		defaultRate = 75;
		roomType = "Standard";
	}
	
	@Override
	public double getDefaultRate() {
		return defaultRate;
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
	public void setDefaultRate(double defaultRate) {
		this.defaultRate = defaultRate;
	}
	
}
