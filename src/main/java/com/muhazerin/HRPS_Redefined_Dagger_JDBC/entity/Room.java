package com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity;

import java.io.Serializable;

/**
 * 
 * @author Asyraaf
 * @author https://github.com/masyraaf
 *
 */

public abstract class Room extends Entity implements Serializable{
	private static final long serialVersionUID = 1L;
		
	public static enum AvailabilityStatus{
		VACANT,
		OCCUPIED,
		RESERVED,
		MAINTENANCE
	}
	public static enum BedType{
		SINGLE,
		DOUBLE,
		MASTER
	}
	private int roomLevel, roomNumber;
	private BedType bedType;
	private AvailabilityStatus availabilityStatus;
	private boolean wifiEnabled, smokingAllowed;
	private String facing;
		
	public Room(BedType bedType, AvailabilityStatus availabilityStatus, boolean wifiEnabled, String facing, boolean smokingAllowed, int roomLevel, int roomNumber) {
		this.bedType = bedType;
		this.availabilityStatus = availabilityStatus;
		this.wifiEnabled = wifiEnabled;
		this.facing = facing;
		this.smokingAllowed = smokingAllowed;
		this.roomLevel = roomLevel;
		this.roomNumber = roomNumber;
	}
	public int getRoomLevel() {
		return roomLevel;
	}
	public void setRoomLevel(int roomLevel) {
		this.roomLevel = roomLevel;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	public abstract double getRate();
	public abstract void setRate(double rate);
	public abstract String getRoomType();
	public abstract double getDefaultRate();
	public abstract void setDefaultRate(double defaultRate);
	
	public BedType getBedType() {
		return bedType;
	}
	public void setBedType(BedType bedType) {
		this.bedType = bedType;
	}
	public AvailabilityStatus getAvailabilityStatus() {
		return availabilityStatus;
	}
	public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}
	public boolean isWifiEnabled() {
		return wifiEnabled;
	}
	public void setWifiEnabled(boolean wifiEnabled) {
		this.wifiEnabled = wifiEnabled;
	}
	public boolean isSmokingAllowed() {
		return smokingAllowed;
	}
	public void setSmokingAllowed(boolean smokingAllowed) {
		this.smokingAllowed = smokingAllowed;
	}
	public String getFacing() {
		return facing;
	}
	public void setFacing(String facing) {
		this.facing = facing;
	}	
}
