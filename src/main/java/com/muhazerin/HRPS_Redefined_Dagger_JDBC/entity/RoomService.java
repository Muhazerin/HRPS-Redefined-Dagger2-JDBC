package com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity;

import java.util.ArrayList;
import java.io.Serializable;
import java.time.LocalDate;

public class RoomService extends Entity implements Serializable{
	public static enum RoomServiceStatus {
		ORDERED, PREPARING, DELIVERED;
	}
	private static final long serialVersionUID = 1L;
	private ArrayList<MenuItem> roomService;
	private RoomServiceStatus roomServiceStatus;
	private LocalDate orderDate;
	
	public RoomService(ArrayList<MenuItem> roomService) {
		this.roomService = roomService;
		roomServiceStatus = RoomServiceStatus.ORDERED;
		this.setOrderDate(LocalDate.now());
	}
	public RoomServiceStatus getRoomServiceStatus() {
		return roomServiceStatus;
	}
	public void setRoomServiceStatus(RoomServiceStatus roomServiceStatus) {
		this.roomServiceStatus = roomServiceStatus;
	}
	public ArrayList<MenuItem> getRoomService() {
		return roomService;
	}
	public void setRoomService(ArrayList<MenuItem> roomService) {
		this.roomService = roomService;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public void printOrder() {
		System.out.println("---------------Summary of order------------------");
		System.out.println("Ordered on: " + this.orderDate.toString());
		System.out.println("Order status: " + this.getRoomServiceStatus().toString());
		for (MenuItem mi : roomService) {
			System.out.println("Menu Item Ordered: " + mi.getName());
			System.out.println("Price: $" + mi.getPrice());
		}
		System.out.println("---------------------End------------------------");
	}
	
}
