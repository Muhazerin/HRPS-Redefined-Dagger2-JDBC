package com.muhazerin.HRPS_Redefined_Dagger_JDBC.control;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import javax.inject.Inject;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.DeluxeRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Entity;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Room;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.SingleRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.StandardRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.VipSuiteRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.AddObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.ChecksOutRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.ModifyObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.PrintAllObjects;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.PrintSingleObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.SelectRoom;

/**
 * 
 * @author muhazerin
 * @author Asyraaf
 * @author https://github.com/masyraaf
 *
 */

public class RoomManager implements SelectRoom, AddObject, ModifyObject, PrintSingleObject, PrintAllObjects, ChecksOutRoom{
	private ArrayList<Room> roomList;
	private DataAccess dataAccess;
	private Scanner sc;
	
	@Inject
	public RoomManager(Scanner sc, DataAccess dataAccess) {
		this.dataAccess = dataAccess;
		this.sc = sc;
		
		roomList = new ArrayList<Room>();
		
		ArrayList<Entity> entityArray = dataAccess.retrieveAllObjects();
		if (entityArray.size() > 0) {
			for (Entity entity : entityArray) {
				if (entity instanceof Room) {
					Room room = (Room) entity;
					roomList.add(room);
				}
				else {
					System.out.println("Entity is not instance of Room");
					break;
				}
			}
		}
	}
	
	public Object[] getList() {
		return roomList.toArray();
	}
	@Override
	public Room selectRoom(boolean walkIn) {
		listRoomsByOccupancyRate();
		boolean valid = false;
		Room room = null;
		
		while (!valid) {
			System.out.print("Enter room number: ");
			String roomNo = sc.nextLine();
			String[] parts = roomNo.split("-");
			if (parts.length == 2) {
				try {
					Room r = validateRoomNumber(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
					if (r.getAvailabilityStatus().equals(Room.AvailabilityStatus.VACANT)) {
						valid = true;
						room = r;
						Room oldRoom = copyRoom(room);
						if (walkIn) {
							r.setAvailabilityStatus(Room.AvailabilityStatus.OCCUPIED);
						}
						else {
							r.setAvailabilityStatus(Room.AvailabilityStatus.RESERVED);
						}
						dataAccess.updateObject(oldRoom, r);
					}
					else {
						System.out.println("Room is not vacant");
					}
				}
				catch (java.lang.NumberFormatException e) {
					System.out.println("Invalid String");
					System.out.println(e.getMessage());
				}
				catch (java.lang.NullPointerException e) {
					System.out.println("Invalid room");
					System.out.println(e.getMessage());
				}
			}
			else {
				System.out.println("Invalid String");
			}
		}
		return room;
	}
	@Override
	public void addObject() {
		Room.BedType bt = null;
		Room.AvailabilityStatus as = null;
		boolean wifiEnabled = false, smokingAllowed = false, valid = false;
		int rLevel = -1, rNumber = -1, rt = -1;
		Room r = null;

		rt = selectRoomType();
		bt = selectBedType();
		as = selectAvailStatus();
		wifiEnabled = selectWifiOption();
		smokingAllowed = selectSmokingOption();
		System.out.print("Facing: ");
		String facing = sc.nextLine();
		
		while(!valid) {
			System.out.print("Enter room level: ");
			rLevel = validateChoice(rLevel, "Enter room level: ");
			if (rLevel > 1 && rLevel < 6) {
				System.out.print("Enter room number: ");
				rNumber = validateChoice(rNumber, "Enter room number: ");
				if (rNumber < 1) {
					System.out.println("Invalid room number");
				}
				else {
					r = validateRoomNumber(rLevel, rNumber);
					if (Objects.equals(r, null)) {
						valid = true;
					}
					else {
						System.out.println("Invalid room number");
					}
				}
			}
			else {
				System.out.println("Invalid room level");
			}
		}
		Room room = null;
		if (rt == 1) {
			room = new SingleRoom(bt, as, wifiEnabled, facing, smokingAllowed, rLevel, rNumber);
			roomList.add(room);
		}
		else if (rt == 2) {
			room = new StandardRoom(bt, as, wifiEnabled, facing, smokingAllowed, rLevel, rNumber);
			roomList.add(room);
		}
		else if (rt == 3) {
			room = new VipSuiteRoom(bt, as, wifiEnabled, facing, smokingAllowed, rLevel, rNumber);
			roomList.add(room);
		}
		else {
			room = new DeluxeRoom(bt, as, wifiEnabled, facing, smokingAllowed, rLevel, rNumber);
			roomList.add(room);
		}
		this.dataAccess.insertObject(room);
		System.out.println("Room has been added");
	}
	@Override
	public void modify() {
		Room room = searchRoom();
		if (Objects.equals(room, null)) {
			System.out.println("Invalid Room");
			return;
		}
		
		// a variable to let rate change have priority above all rate changing algorithm
		boolean ratePriority = false;
		
		Room room2 = copyRoom(room);
		
		int option = -1;
		do {
			System.out.println("\n+------------------------------+");
			System.out.println("| What you you like to modify? |");
			System.out.println("+------------------------------+");
			System.out.println("| 0. Nothing                   |");
			System.out.println("| 1. Bed Type                  |");
			System.out.println("| 2. Availability Status       |");
			System.out.println("| 3. Wifi Enabled?             |");
			System.out.println("| 4. Smoking Allowed?          |");
			System.out.println("| 5. Facing                    |");
			System.out.println("| 6. Rate                      |");
			System.out.println("+------------------------------+");
			System.out.print("Enter choice: ");
			option = validateChoice(option, "Enter choice: ");
			
			switch(option) {
			case 0:
				System.out.println("Going back...");
				this.dataAccess.updateObject(room2, room);
				break;
			case 1:
				room.setBedType(selectBedType());
				
				// auto set the rate if not ratePriority
				if (!ratePriority) {
					double defaultRate = 0;
					if (room instanceof SingleRoom) {
						defaultRate = room.getDefaultRate();
					}
					if (room instanceof StandardRoom) {
						defaultRate = room.getDefaultRate();
					}
					if (room instanceof VipSuiteRoom) {
						defaultRate = room.getDefaultRate();
					}
					if (room instanceof DeluxeRoom) {
						defaultRate = room.getDefaultRate();
					}
					
					if (room.getBedType() == Room.BedType.SINGLE) {
						room.setRate(defaultRate + 10);
					}
					else if (room.getBedType() == Room.BedType.DOUBLE) {
						room.setRate(defaultRate + 20);
					}
					else {
						room.setRate(defaultRate + 30);
					}
				}
				
				System.out.println("Bed type changed");
				break;
			case 2:
				if (room.getAvailabilityStatus() == Room.AvailabilityStatus.OCCUPIED) {
					System.out.println("Unable to change room. Room is currently occupied");
				}
				else {
					room.setAvailabilityStatus(selectAvailStatus());
					System.out.println("Availability status changed");
				}
				break;
			case 3:
				room.setWifiEnabled(selectWifiOption());
				System.out.println("Wifi enabled changed");
				break;
			case 4:
				room.setSmokingAllowed(selectSmokingOption());
				System.out.println("Smoking allowed changed");
				break;
			case 5:
				System.out.print("Facing: ");
				room.setFacing(sc.nextLine());
				System.out.println("Facing changed");
				break;
			case 6:
				double rate = 0;
				while (rate < 1) {
					System.out.print("Enter rate: ");
					rate = validateRate(rate, "Enter rate: ");
				}
				room.setRate(rate);
				ratePriority = true;
				System.out.println("Rate changed");
				break;
			default:
				System.out.println("Invalid choice");
				break;
		}
		} while (option != 0);
		
	}
	@Override
	public void printSingle() {
		System.out.print("Enter room number: ");
		String roomNo = sc.nextLine();
		String[] parts = roomNo.split("-");
		if (parts.length == 2) {
			try {
				Room r = validateRoomNumber(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
				printRoomDetails(r);
			}
			catch (java.lang.NumberFormatException e) {
				System.out.println("Invalid String");
				System.out.println(e.getMessage());
			}
			catch (java.lang.NullPointerException e) {
				System.out.println("Invalid room");
				System.out.println(e.getMessage());
			}
		}
		else {
			System.out.println("Invalid String");
		}
	}
	@Override
	public void printAll() {
		int choice = -1;
		
		do {
			System.out.println("\n+--------------------------------+");
			System.out.println("| Select display type:           |");
			System.out.println("| 1. List Room by occupancy rate |");
			System.out.println("| 2. List Room by room status    |");
			System.out.println("+--------------------------------+");
			System.out.print("Enter choice: ");
			choice = validateChoice(choice, "Invalid Choice");
			switch (choice) {
				case 1:
					listRoomsByOccupancyRate();
					break;
				case 2:
					listRoomsByRoomStatus();
					break;
				default:
					System.out.println("Invalid Choice");
					break;
			}
		} while (choice != 1 && choice != 2);
	}
	@Override
	public void checksOutRoom(Room room) {
		Room oldRoom = copyRoom(room);
		room.setAvailabilityStatus(Room.AvailabilityStatus.VACANT);
		dataAccess.updateObject(oldRoom, room);
	}
	
	/*
	 * This method contains the menu for updating room type
	 */
	private void roomTypeMenu() {
		System.out.println("\n+-------------------+");
		System.out.println("| Select room type: |");
		System.out.println("| 1. Single         |");
		System.out.println("| 2. Standard       |");
		System.out.println("| 3. VIP Suite      |");
		System.out.println("| 4. Deluxe         |");
		System.out.println("+-------------------+");
		System.out.print("Enter choice: ");
	}
	/*
	 * This method returns the room type based on user's input
	 */
	private int selectRoomType() {
		int rtChoice = -1;
		do {
			roomTypeMenu();
			rtChoice = validateChoice(rtChoice, "Enter choice: ");
			if (!(rtChoice >= 1 && rtChoice <= 4))
				System.out.println("Invalid Choice");
			
		} while (rtChoice != 1 && rtChoice != 2 && rtChoice != 3 && rtChoice != 4);
		return rtChoice;
	}
	/*
	 * This method contains the menu for updating bed type
	 */
	private void bedTypeMenu() {
		System.out.println("\n+------------------+");
		System.out.println("| Select bed type: |");
		System.out.println("| 1. Single        |");
		System.out.println("| 2. Double        |");
		System.out.println("| 3. Master        |");
		System.out.println("+------------------+");
		System.out.print("Enter choice: ");
	}
	/*
	 * This method returns the bed type based on user's input
	 */
	private Room.BedType selectBedType() {
		int btChoice = -1;
		Room.BedType bt = null;
		do {
			bedTypeMenu();
			btChoice = validateChoice(btChoice, "Enter choice: ");
			switch(btChoice) {
				case 1:
					bt = Room.BedType.SINGLE;
					break;
				case 2:
					bt = Room.BedType.DOUBLE;
					break;
				case 3:
					bt = Room.BedType.MASTER;
					break;
				default:
					System.out.println("Invalid Choice");
					break;
			}
		} while (btChoice != 1 && btChoice != 2 && btChoice != 3);
		return bt;
	}
	/*
	 * This method contains the menu for updating the availability status
	 */
	private void availStatusMenu() {
		System.out.println("\n+-----------------------------+");
		System.out.println("| Select availability status: |");
		System.out.println("| 1. Vacant                   |");
		System.out.println("| 2. Occupied                 |");
		System.out.println("| 3. Reserved                 |");
		System.out.println("| 4. Maintenance              |");
		System.out.println("+-----------------------------+");
		System.out.print("Enter choice: ");
	}
	/*
	 * This method returns the availability status based on user's input
	 */
	private Room.AvailabilityStatus selectAvailStatus() {
		int asChoice = -1;
		Room.AvailabilityStatus as = null;
		
		do {
			availStatusMenu();
			asChoice = validateChoice(asChoice, "Enter choice: ");
			switch(asChoice) {
				case 1:
					as = Room.AvailabilityStatus.VACANT;
					break;
				case 2:
					as = Room.AvailabilityStatus.OCCUPIED;
					break;
				case 3:
					as = Room.AvailabilityStatus.RESERVED;
					break;
				case 4:
					as = Room.AvailabilityStatus.MAINTENANCE;
					break;
				default:
					System.out.println("Invalid Choice");
					break;
			}
			
		} while (asChoice != 1 && asChoice != 2 && asChoice != 3 && asChoice != 4);
		return as;
	}
	/*
	 * This method contains the menu for updating whether wifi is enabled
	 */
	private void wifiMenu() {
		System.out.println("\n+----------------+");
		System.out.println("| Is there wifi: |");
		System.out.println("| 1. Yes         |");
		System.out.println("| 2. No          |");
		System.out.println("+----------------+");
		System.out.print("Enter choice: ");
	}
	/*
	 * This method returns a boolean whether wifi is enabled based on user's input
	 * Returns true if wifi is enabled
	 * Returns false if wifi is not enabled
	 */
	private boolean selectWifiOption() {
		int wChoice = -1;
		boolean wifiEnabled = false;
		
		do {
			wifiMenu();
			wChoice = validateChoice(wChoice, "Enter choice: ");
			switch(wChoice) {
				case 1:
					wifiEnabled = true;
					break;
				case 2:
					wifiEnabled = false;
					break;
				default:
					System.out.println("Invalid Choice");
					break;
			}
		} while (wChoice != 1 && wChoice != 2);
		return wifiEnabled;
	}
	/*
	 * This method contains the menu for updating whether smoking is allowed
	 */
	private void smokingMenu() {
		System.out.println("\n+---------------------+");
		System.out.println("| Is smoking allowed: |");
		System.out.println("| 1. Yes              |");
		System.out.println("| 2. No               |");
		System.out.println("+---------------------+");
		System.out.print("Enter choice: ");
	}
	/*
	 * This method returns a boolean whether smoking is allowed based on user's input
	 * Returns true if smoking is allowed
	 * Returns false if smoking is not allowed
	 */
	private boolean selectSmokingOption() {
		int sChoice = -1;
		boolean smokingAllowed = false;
		
		do {
			smokingMenu();
			sChoice = validateChoice(sChoice, "Enter choice: ");
			switch(sChoice) {
				case 1:
					smokingAllowed = true;
					break;
				case 2:
					smokingAllowed = false;
					break;
				default:
					System.out.println("Invalid Choice");
					break;
			}
		} while (sChoice != 1 && sChoice != 2);
		return smokingAllowed;
	}
	/*
	 * return the room if room is found
	 * return null if room is not found
	 */
	private Room validateRoomNumber(int rL, int rN) {
		Room room = null;
		for (Room r : roomList) {
			if (r.getRoomLevel() ==  rL && r.getRoomNumber() == rN) {
				room = r;
				break;
			}
		}
		return room;
	}
	/*
	 * This method prints the room details
	 */
	private void printRoomDetails(Room r) {
		System.out.println("\n+----------Room Details------------+");
		System.out.println("Room number: " + String.format("%02d-%02d", r.getRoomLevel(), r.getRoomNumber()));
		System.out.println("Room type: " + r.getRoomType().toString());
		System.out.println("Bed Type: " + r.getBedType().toString());
		System.out.println("Room rate: $" + r.getRate());
		System.out.println("Availability Status: " + r.getAvailabilityStatus().toString());
		System.out.println("Wifi Enabled: " + boolToString(r.isWifiEnabled()));
		System.out.println("Smoking Allowed: " + boolToString(r.isSmokingAllowed()));
		System.out.println("Facing: " + r.getFacing());
	}
	/*
	 * This method returns a String based on the boolean
	 * Returns Yes if true
	 * Returns No if false
	 */
	private String boolToString(boolean bool) {
		if (bool) {
			return "Yes";
		}
		else {
			return "No";
		}
	}
	/*
	 * This method is used to ensure that user enters an integer
	 */
	private int validateChoice(int choice, String inputText) {
		boolean valid = false;
		
		while (!valid) {
			if (!sc.hasNextInt()) {
				System.out.println("Invalid Input. Please enter an integer");
				sc.nextLine();	// clear the input in the buffer
				System.out.print(inputText);
			}
			else {
				valid = true;
				choice = sc.nextInt();
				sc.nextLine();	// clear the "\n" in the buffer
			}
		}
		
		return choice;
	}
	
	private void listRoomsByOccupancyRate() {
		ArrayList<String> roomTypeArray = new ArrayList<String>();
		ArrayList<String> vacantRoom = new ArrayList<String>();
		int totalRooms = 0;
		
		// populate the room type in roomTypeArray
		for (Room r : roomList) {
			if (!roomTypeArray.contains(r.getRoomType())) {
				roomTypeArray.add(r.getRoomType());
			}
		}
		
		// go through the roomTypeArray and print the information
		for (String roomType : roomTypeArray) {
			totalRooms = 0;
			vacantRoom.clear();
			for (Room r : roomList) {
				if (r.getRoomType().equals(roomType)) {
					if (r.getAvailabilityStatus().equals(Room.AvailabilityStatus.VACANT)) {
						vacantRoom.add(String.format("%02d-%02d", r.getRoomLevel(), r.getRoomNumber()));
					}
					totalRooms++;
				}
			}
			System.out.print(roomType.toString() + ": ");
			System.out.printf("\tNumber: %d out of %d\n", vacantRoom.size(), totalRooms);
			System.out.print("\t\tRooms: ");
			for (String s : vacantRoom) {
				System.out.print(s + ", ");
			}
			System.out.println("");
		}
	}
	private void listRoomsByRoomStatus() {
		for (Room.AvailabilityStatus as : Room.AvailabilityStatus.values()) {
			System.out.println(as.toString() + " : ");
			System.out.print("\tRooms : ");
			for (Room r : roomList) {
				if (as.equals(r.getAvailabilityStatus())) {
					System.out.printf("%s, ", String.format("%02d-%02d", r.getRoomLevel(), r.getRoomNumber()));
				}
			}
			System.out.println("");
		}
	}

	private Room searchRoom() {
		Room r = null;
		System.out.print("Enter room number: ");
		String roomNo = sc.nextLine();
		String[] parts = roomNo.split("-");
		if (parts.length == 2) {
			try {
				r = validateRoomNumber(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
			}
			catch (java.lang.NumberFormatException e) {
				System.out.println("Invalid String");
				System.out.println(e.getMessage());
			}
			catch (java.lang.NullPointerException e) {
				System.out.println("Invalid room");
				System.out.println(e.getMessage());
			}
		}
		else {
			System.out.println("Invalid String");
		}
		return r;
	}
	private double validateRate(double choice, String inputText) {
		boolean valid = false;
		
		while (!valid) {
			if (!sc.hasNextDouble()) {
				System.out.println("Invalid Input. Please enter an floating point value");
				sc.nextLine();	// clear the input in the buffer
				System.out.print(inputText);
			}
			else {
				valid = true;
				choice = sc.nextDouble();
				sc.nextLine();	// clear the "\n" in the buffer
			}
		}
		
		return choice;
	}
	public int getTotalNumberOfRooms() {
		return roomList.size();
	}
	
	private Room copyRoom(Room room) {
		Room room2 = null;
		if (room.getRoomType().equalsIgnoreCase("Single")) {
			room2 = new SingleRoom(room.getBedType(), room.getAvailabilityStatus(), room.isWifiEnabled(),
					room.getFacing(), room.isSmokingAllowed(), room.getRoomLevel(), room.getRoomNumber(), room.getRate());
		}
		else if (room.getRoomType().equalsIgnoreCase("Standard")) {
			room2 = new StandardRoom(room.getBedType(), room.getAvailabilityStatus(), room.isWifiEnabled(),
					room.getFacing(), room.isSmokingAllowed(), room.getRoomLevel(), room.getRoomNumber(), room.getRate());
		}
		else if (room.getRoomType().equalsIgnoreCase("Deluxe")) {
			room2 = new DeluxeRoom(room.getBedType(), room.getAvailabilityStatus(), room.isWifiEnabled(),
					room.getFacing(), room.isSmokingAllowed(), room.getRoomLevel(), room.getRoomNumber(), room.getRate());
		}
		else {
			room2= new VipSuiteRoom(room.getBedType(), room.getAvailabilityStatus(), room.isWifiEnabled(),
					room.getFacing(), room.isSmokingAllowed(), room.getRoomLevel(), room.getRoomNumber(), room.getRate());
		}
			
		return room2;
	}
}
