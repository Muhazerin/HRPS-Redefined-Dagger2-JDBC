package com.muhazerin.HRPS_Redefined_Dagger_JDBC.control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Entity;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Guest;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.MenuItem;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Reservation;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Room;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.RoomService;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.AddReservation;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.AddRoomService;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.AdjustObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.CheckInReservation;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.CheckOutReservation;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.ModifyObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.PrintAllObjects;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.PrintRoomServices;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.PrintSingleObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.SelectObject;

/**
 * 
 * @author muhazerin
 *
 */

public class ReservationManager implements AddReservation, ModifyObject, PrintSingleObject, PrintAllObjects, AdjustObject, SelectObject, AddRoomService, PrintRoomServices, CheckInReservation, CheckOutReservation{
	private ArrayList<Reservation> reservationList;
	private Scanner sc;
	private DataAccess dataAccess;
	private ScheduledExecutorService scheduledExecutorService;
	
	@Inject
	public ReservationManager(Scanner sc, DataAccess dataAccess, ScheduledExecutorService scheduledExecutorService) {
		this.dataAccess = dataAccess;
		this.scheduledExecutorService = scheduledExecutorService;
		this.sc = sc;
		
		reservationList = new ArrayList<Reservation>();
		LocalDate now = LocalDate.now();		
		ArrayList<Entity> entityArray = dataAccess.retrieveAllObjects();
		if (entityArray.size() > 0) {
			for (Entity entity : entityArray) {
				if (entity instanceof Reservation) {
					Reservation reservation = (Reservation) entity;
					reservationList.add(reservation);
					if (reservation.getResStatus() == Reservation.ResStatus.CONFIRMED) {
						// cancel reservation if now - checkInDate < 0
						if (now.until(reservation.getCheckInDate(), ChronoUnit.DAYS) < 0) {
							Reservation oldReservation = copyReservation(reservation);
							reservation.setResStatus(Reservation.ResStatus.CANCELLED);
							dataAccess.updateObject(oldReservation, reservation);
						}
						else {
							// schedule a task to auto expire the reservation after 1 day if the guest didnt check in
							LocalDateTime endTime = reservation.getCheckInDate().plusDays(1).atStartOfDay();
							LocalDateTime nowTime = LocalDateTime.now();
							long seconds = nowTime.until(endTime, ChronoUnit.SECONDS);
							Runnable task = () -> {
								if (reservation.getResStatus() == Reservation.ResStatus.CONFIRMED) {
									Reservation oldReservation = copyReservation(reservation);
									reservation.setResStatus(Reservation.ResStatus.CANCELLED);
									dataAccess.updateObject(oldReservation, reservation);
								}
							};
							scheduledExecutorService.schedule(task, seconds, TimeUnit.SECONDS);
						}
					}
				}
				else {
					System.out.println("Entity is not an instance of Reservation");
					break;
				}
			}
		}
	}
	
	@Override
	public void addReservation(Guest guest, Room room, boolean walkIn) {
		LocalDate checkInDate = null;
		Reservation.ResStatus resStatus = null;
		if (walkIn) {
			checkInDate = LocalDate.now();
			resStatus = Reservation.ResStatus.CHECKED_IN;
		}
		else {
			boolean valid = false;
			DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
			while (!valid) {
				try {
					System.out.print("Enter check in date (yyyy-mm-dd): ");
					String date = sc.nextLine();
					checkInDate = LocalDate.parse(date, dateFormatter);
					valid = true;
				}
				catch (DateTimeParseException e) {
					System.out.println("Invalid date format");
				}
			}
			resStatus = Reservation.ResStatus.CONFIRMED;
		}
		System.out.print("Enter the number of adults checking in: ");
		int noOfAdults = 0;
		while (noOfAdults < 1) {
			noOfAdults = validateChoice(noOfAdults, "Enter the number of adults checking in: ");
			if (noOfAdults < 1)
				System.out.println("Value should not be less than 1");
		}
		
		System.out.print("Enter the number of childrens checking in: ");
		int noOfChildren = -1;
		while (noOfChildren < 0) {
			noOfChildren = validateChoice(noOfChildren, "Enter the number of childrens checking in: ");
			if (noOfChildren < 0)
				System.out.println("Value should not be less than 0");
		}
		
		Reservation r = new Reservation(guest, room, checkInDate, noOfAdults, noOfChildren, resStatus);
		reservationList.add(r);
		dataAccess.insertObject(r);
		
		// adding a schedule to cancel resStatus is still CONFIRMED by the end the day after the checkInDate.
		if (!walkIn) {
			LocalDateTime endTime = checkInDate.plusDays(1).atStartOfDay();
			LocalDateTime now = LocalDateTime.now();
			long seconds = now.until(endTime, ChronoUnit.SECONDS);
			Runnable task = () -> {
				if (r.getResStatus() == Reservation.ResStatus.CONFIRMED) {
					Reservation oldReservation = copyReservation(r);
					r.setResStatus(Reservation.ResStatus.CANCELLED);
					dataAccess.updateObject(oldReservation, r);
				}
			};
			scheduledExecutorService.schedule(task, seconds, TimeUnit.SECONDS);
		}
		
		System.out.println("Reservation has been added");
	}
	@Override
	public void modify() {
		ArrayList<Reservation> tempList = searchReservation();
		if (tempList.size() == 0) {
			// reservationList is empty or name not found
			System.out.println("Name is not found in the reservation list");
			return;
		}
		if (tempList.size() > 1) {
			// multiple guest name found
			System.out.println("Multiple reservation found. Please refine your search query");
			for (Reservation r : tempList) {
				print(r);
			}
			return;
		}
		Reservation r = tempList.get(0);
		Reservation oldReservation = copyReservation(r);
		
		int option = -1;
		do {
			System.out.println("\n+------------------------------+");
			System.out.println("| What you you like to modify? |");
			System.out.println("+------------------------------+");
			System.out.println("| 0. Nothing                   |");
			System.out.println("| 1. Number of children        |");
			System.out.println("| 2. Number of adults          |");
			System.out.println("+------------------------------+");
			System.out.print("Enter choice: ");
			option = validateChoice(option, "Enter choice: ");
			
			switch(option) {
				case 0:
					System.out.println("Going back...");
					dataAccess.updateObject(oldReservation, r);
					break;
				case 1:
					int noOfChildren = -1;
					while (noOfChildren < 0) {
						System.out.print("Enter the number of children: ");
						noOfChildren = validateChoice(noOfChildren, "Enter the number of children: ");
						if (noOfChildren < 0)
							System.out.println("Value should not be less than 0");
					}
					r.setNoOfChildren(noOfChildren);
					break;
				case 2:
					int noOfAdults = 0;
					while (noOfAdults < 1) {
						System.out.print("Enter the number of adults: ");
						noOfAdults = validateChoice(noOfAdults, "Enter the number of adults: ");
						if (noOfAdults < 1)
							System.out.println("Value should not be less than 1");
					}
					r.setNoOfChildren(noOfAdults);
					break;
				default:
					System.out.println("Invalid choice");
					break;
			}
		} while (option != 0);
	}
	@Override
	public void printSingle() {
		ArrayList<Reservation> tempList = searchReservation();
		if (tempList.size() == 0) {
			System.out.println("Name is not found in the reservation list");
			return;
		}
		if (tempList.size() > 1) {
			System.out.println("Multiple reservation found. Please refine your search query");
			for (Reservation reservation : tempList) {
				System.out.println("Name: " + reservation.getGuest().getName());
			}
			return;
		}
		print(tempList.get(0));
	}
	@Override
	public void printAll() {
		if (reservationList.size() == 0) {
			System.out.println("There are no reservation in the reservation list");
			return;
		}
		for (Reservation r : reservationList) {
			print(r);
		}
	}
	@Override
	public void adjustObject(Object[] objArray) {
		// do something if there are objects in the array
		if (objArray.length > 0) {
			// adjust reservation Guest if object is of Guest type
			if (objArray[0] instanceof Guest) {
				for (Object obj : objArray) {
					Guest guest = (Guest) obj;
					for (Reservation reservation : reservationList) {
						if (reservation.getGuest().getNRIC().equals(guest.getNRIC())) {
							reservation.setGuest(guest);
							break;
						}
					}
				}
				return;
			}
			// adjust reservation room if object is of Room type
			if (objArray[0] instanceof Room) {
				for (Object obj : objArray) {
					Room room = (Room) obj;
					for (Reservation reservation : reservationList) {
						if (reservation.getRoom().getRoomLevel() == room.getRoomLevel() && reservation.getRoom().getRoomNumber() == room.getRoomNumber()) {
							reservation.setRoom(room);
							break;
						}
					}
				}
				return;
			}
		}
	}
	@Override
	public Object selectObject() {
		ArrayList<Reservation> tempList = searchReservation();
		if (tempList.size() == 0) {
			// reservationList is empty or name not found
			System.out.println("Name is not found in the reservation list");
			return null;
		}
		if (tempList.size() > 1) {
			// multiple guest name found
			System.out.println("Multiple reservation found. Please refine your search query");
			for (Reservation r : tempList) {
				print(r);
			}
			return null;
		}
		Reservation r = tempList.get(0);
		return r;
	}
	@Override
	public void addRoomService(Reservation reservation, RoomService roomService) {
		Reservation reservation2 = copyReservation(reservation);
		reservation.addRoomService(roomService);
		dataAccess.updateObject(reservation2, reservation);
	}
	@Override
	public void printRoomServices() {
		ArrayList<Reservation> tempList = searchReservation();
		if (tempList.size() == 0) {
			// reservationList is empty or name not found
			System.out.println("Name is not found in the reservation list");
			return;
		}
		if (tempList.size() > 1) {
			// multiple guest name found
			System.out.println("Multiple reservation found. Please refine your search query");
			for (Reservation r : tempList) {
				print(r);
			}
			return;
		}
		Reservation reservation = tempList.get(0);
		
		if (reservation.getRoomServiceList().size() == 0) {
			System.out.println("There is no room service ordered yet");
			return;
		}
		int i = 1;
		for (RoomService roomService : reservation.getRoomServiceList()) {
			System.out.println("\nRoom Service Order #" + i);
			i++;
			
			System.out.println("Room Service Order Status: " + roomService.getRoomServiceStatus().toString());
			System.out.println("Ordered Items");
			System.out.println("-------------");
			for (MenuItem menuItem : roomService.getRoomService()) {
				System.out.printf("Name: %s, Description: %s, Price: $%.2f\n", menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
			}
		}
 	}
	@Override
	public void checkInReservation() {
		ArrayList<Reservation> tempList = searchReservation();
		if (tempList.size() == 0) {
			System.out.println("Reservation does not exist");
			return;
		}
		
		// filter the tempList
		for (Reservation reservation : tempList) {
			if (reservation.getResStatus() != Reservation.ResStatus.CONFIRMED) {
				tempList.remove(reservation);
			}
		}
		
		// checks the list again
		if (tempList.size() > 1) {
			System.out.println("Multiple reservation found. Please refine your search query");
			for (Reservation r : tempList) {
				print(r);
			}
			return;
		}
		if (tempList.size() == 0) {
			System.out.println("Reservation does not exist");
			return;
		}
		
		// if the code reaches here, there's one item in the reservation. Prompt user whether it is the correct reservation
		System.out.println("Reservation found in the reservationList");
		print(tempList.get(0));
		String choice = "";
		do {
			System.out.print("Is the right reservation (Y/n): ");
			choice = sc.nextLine();
			if (choice.equalsIgnoreCase("y")) {
				// checks in the guests
				Reservation reservation = copyReservation(tempList.get(0));
				tempList.get(0).setResStatus(Reservation.ResStatus.CHECKED_IN);
				dataAccess.updateObject(reservation, tempList.get(0));
				System.out.println("Reservation status has been set to Checked_In");
			}
			else if (choice.equalsIgnoreCase("n")){
				// exit
				System.out.println("No other reservation found");
				return;
			}
			else {
				System.out.println("Invalid Choice");
			}
		} while (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"));
	}
	@Override
	public Reservation checkOutReservation() {
		ArrayList<Reservation> tempList = searchReservation();
		Reservation reservation = null;
		
		if (tempList.size() == 0) {
			System.out.println("Reservation does not exist");
			return reservation;
		}
		if (tempList.size() > 1){
			System.out.println("Multiple reservation found. Please refine your query");
			for (Reservation r : tempList) {
				print(r);
			}
			return reservation;
		}
		reservation = tempList.get(0);
		Reservation oldReservation = copyReservation(reservation);
		reservation.setResStatus(Reservation.ResStatus.CHECKED_OUT);
		reservation.setCheckOutDate(LocalDate.now());
		dataAccess.updateObject(oldReservation, reservation);
		return reservation;
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
	private ArrayList<Reservation> searchReservation() {
		ArrayList<Reservation> tempList = new ArrayList<Reservation>();
		
		System.out.print("Enter guest name: ");
		String name = sc.nextLine();
		
		if (reservationList.size() == 0) {
			return tempList;
		}
		for (Reservation r : reservationList) {
			if (r.getGuest().getName().contains(name) || r.getGuest().getName().equalsIgnoreCase(name)) {
				tempList.add(r);
			}
		}
		
		return tempList;
	}
	private void print(Reservation r) {
		if (r.getResStatus().equals(Reservation.ResStatus.CHECKED_OUT))
			System.out.printf("Name: %s, Room Number: %d-%d, Check In Date: %s, Check Out Date: %s, No of Adults: %d, No of Children: %d, Reservation Status: %s\n", r.getGuest().getName(), r.getRoom().getRoomLevel(), r.getRoom().getRoomNumber(), r.getCheckInDate().toString(), r.getCheckOutDate().toString(), r.getNoOfAdults(), r.getNoOfChildren(), r.getResStatus().toString());
		else
			System.out.printf("Name: %s, Room Number: %d-%d, Check In Date: %s, Check Out Date: %s, No of Adults: %d, No of Children: %d, Reservation Status: %s\n", r.getGuest().getName(), r.getRoom().getRoomLevel(), r.getRoom().getRoomNumber(), r.getCheckInDate().toString(), "NIL", r.getNoOfAdults(), r.getNoOfChildren(), r.getResStatus().toString());
	}
	
	private Reservation copyReservation(Reservation reservation) {
		Reservation reservation2 = new Reservation(reservation.getGuest(), reservation.getRoom(),
				reservation.getCheckInDate(), reservation.getNoOfAdults(), reservation.getNoOfChildren(),
				reservation.getResStatus());
		reservation2.setCheckOutDate(reservation.getCheckOutDate());
		reservation2.setRoomServiceList(reservation.getRoomServiceList());
		
		return reservation2;
	}
}
