package com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author muhazerin
 *
 */

public class Reservation extends Entity implements Serializable{
	public static enum ResStatus {
		CONFIRMED, IN_WAITLIST, CHECKED_IN, EXPIRED, CHECKED_OUT, CANCELLED;
	}
	private static final long serialVersionUID = 1L;
	private Guest guest;
	private Room room;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private int noOfAdults;
	private int noOfChildren;
	private ResStatus resStatus;
	private ArrayList<RoomService> roomServiceList;
	
	public Reservation(Guest guest, Room room, LocalDate checkInDate, int noOfAdults, int noOfChildren, ResStatus resStatus) {
		roomServiceList = new ArrayList<RoomService>();
		this.guest = guest;
		this.room = room;
		this.checkInDate = checkInDate;
		this.checkOutDate = null;
		this.noOfAdults = noOfAdults;
		this.noOfChildren = noOfChildren;
		this.resStatus = resStatus;
	}
	public Guest getGuest() {
		return guest;
	}
	public void setGuest(Guest guest) {
		this.guest = guest;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public LocalDate getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}
	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public int getNoOfAdults() {
		return noOfAdults;
	}
	public void setNoOfAdults(int noOfAdults) {
		this.noOfAdults = noOfAdults;
	}
	public int getNoOfChildren() {
		return noOfChildren;
	}
	public void setNoOfChildren(int noOfChildren) {
		this.noOfChildren = noOfChildren;
	}
	public ResStatus getResStatus() {
		return resStatus;
	}
	public void setResStatus(ResStatus resStatus) {
		this.resStatus = resStatus;
	}
	public ArrayList<RoomService> getRoomServiceList() {
		return roomServiceList;
	}
	public void setRoomServiceList(ArrayList<RoomService> roomServiceList) {
		this.roomServiceList = roomServiceList;
	}
	public void addRoomService(RoomService roomService) {
		roomServiceList.add(roomService);
	}
	public double getRoomServicePrice() {
		double total = 0;
		for (RoomService roomService : roomServiceList) {
			for (MenuItem menuItem : roomService.getRoomService()) {
				total += menuItem.getPrice();
			}
		}
		return total;
	}
}
