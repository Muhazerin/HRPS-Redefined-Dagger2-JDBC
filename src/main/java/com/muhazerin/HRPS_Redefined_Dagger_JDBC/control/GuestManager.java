package com.muhazerin.HRPS_Redefined_Dagger_JDBC.control;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import javax.inject.Inject;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.CreditCard;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Entity;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Guest;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.ModifyObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.PrintAllObjects;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.PrintSingleObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.SelectObject;

/**
 * 
 * @author muhazerin
 *
 */

public class GuestManager implements SelectObject, ModifyObject, PrintSingleObject, PrintAllObjects {//, RemoveObject{	
	private ArrayList<Guest> guestList;
	private DataAccess dataAccess; 
	private Scanner sc;
	
	@Inject
	public GuestManager(Scanner sc, DataAccess dataAccess) {
		this.dataAccess = dataAccess;
		this.sc = sc;
		
		guestList = new ArrayList<Guest>();
		ArrayList<Entity> entityArray = dataAccess.retrieveAllObjects();
		if (entityArray.size() > 0) {
			for (Entity entity : entityArray) {
				if (entity instanceof Guest) {
					Guest guest = (Guest) entity;
					guestList.add(guest);
				}
				else {
					System.out.println("Entity is not instance of Guest");
					break;
				}
			}
		}
	}
	
	public Object[] getList() {
		return guestList.toArray();
	}
	@Override
	public Object selectObject() {
		Guest guest = null;
		if (guestList.size() == 0) {
			// guestList is empty
			add();
			guest = guestList.get(guestList.size() - 1);
		}
		else {
			// if guestlist isn't empty,
			// need to go through that list and check whether guest details is stored in the database.
			System.out.print("Enter nric: ");
			String nric = sc.nextLine();
			guest = searchGuestByNRIC(nric);	// nric is the unique key
			
			if (Objects.equals(guest, null)) { // if guest is not found in the guestlist
				add(nric);
				guest = guestList.get(guestList.size() - 1);
			}
			else {	// if guest is found in the guestlist
				System.out.println("Guest found in the guest list");
			}
		}
		return guest;
	}
	@Override
	public void modify() {
		ArrayList<Guest> tempList = searchGuest();
		if (tempList.size() == 0) {
			System.out.println("Name is not found in the guest list");
			return;
		}
		if (tempList.size() > 1) {
			System.out.println("Multiple guest found. Please refine your search query");
			for (Guest g : tempList) {
				System.out.println("Name: " + g.getName());
			}
			return;
		}
		Guest g =  tempList.get(0);
		Guest oldGuest = copyGuest(g);
		
		int option = -1;
		String answer = "";
		do {
			System.out.println("\n+------------------------------+");
			System.out.println("| What you you like to modify? |");
			System.out.println("+------------------------------+");
			System.out.println("| 0. Nothing                   |");
			System.out.println("| 1. NRIC                      |");
			System.out.println("| 2. Name                      |");
			System.out.println("| 3. Gender                    |");
			System.out.println("| 4. Nationality               |");
			System.out.println("| 5. Country                   |");
			System.out.println("| 6. Address                   |");
			System.out.println("| 7. Credit Card               |");
			System.out.println("+------------------------------+");
			System.out.print("Enter choice: ");
			option = validateChoice(option, "Enter choice: ");
			
			switch(option) {
				case 0:
					System.out.println("Going back...");
					dataAccess.updateObject(oldGuest, g);
					break;
				case 1:
					System.out.print("Enter new NRIC: ");
					g.setNRIC(sc.nextLine());
					System.out.println("NRIC changed");
					break;
				case 2:
					System.out.print("Enter new name: ");
					String name = sc.nextLine();
					g.setName(name);
					System.out.println("Name changed");
					answer = "";
					while (answer == "") {
						System.out.print("Change guest's credit card name with new name (Y/n): ");
						answer = sc.nextLine();
						if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
							if (answer.equalsIgnoreCase("y")) {
								g.getCreditCard().setName(name);
								System.out.println("Credit Card Name changed");
							}
						}
						else {
							answer = "";
							System.out.println("Invalid input. Please enter Y/n");
						}
					}
					break;
				case 3:
					System.out.print("Enter new gender: ");
					g.setGender(sc.nextLine());
					System.out.println("Gender changed");
					break;
				case 4:
					System.out.print("Enter new nationality: ");
					g.setNationality(sc.nextLine());
					System.out.println("Nationality changed");
					break;
				case 5:
					System.out.print("Enter new country: ");
					String country = sc.nextLine();
					g.setCountry(country);
					System.out.println("Country changed");
					answer = "";
					while (answer == "") {
						System.out.print("Change guest's credit card country with new name (Y/n): ");
						answer = sc.nextLine();
						if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
							if (answer.equalsIgnoreCase("y")) {
								g.getCreditCard().setName(country);
								System.out.println("Credit Card Country changed");
							}
						}
						else {
							answer = "";
							System.out.println("Invalid input. Please enter Y/n");
						}
					}
					break;
				case 6:
					System.out.print("Enter new address: ");
					String address = sc.nextLine();
					g.setAddress(address);
					System.out.println("Address changed");
					answer = "";
					while (answer == "") {
						System.out.print("Change guest's credit card address with new name (Y/n): ");
						answer = sc.nextLine();
						if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
							if (answer.equalsIgnoreCase("y")) {
								g.getCreditCard().setName(address);
								System.out.println("Credit Card Address changed");
							}
						}
						else {
							answer = "";
							System.out.println("Invalid input. Please enter Y/n");
						}
					}
					break;
				case 7:
					modifyCreditCard(g);
					break;
				default:
					System.out.println("Invalid choice");
					break;
			}
			
		} while (option != 0);
		
	}
	@Override
	public void printSingle() {
		ArrayList<Guest> tempList = searchGuest();
		if (tempList.size() == 0) {
			System.out.println("Name is not found in the guest list");
			return;
		}
		if (tempList.size() > 1) {
			System.out.println("Multiple guest found. Please refine your search query");
			for (Guest g : tempList) {
				System.out.println("Name: " + g.getName());
			}
			return;
		}
		print(tempList.get(0));
	}
	@Override
	public void printAll() {
		if (guestList.size() > 0) {
			for (Guest g : guestList) {
				print(g);
			}
		}
		else {
			System.out.println("There's no guest in the guest list");
		}
	}
	
	private void add() {
		String nric;
		
		System.out.print("Enter nric: ");
		nric = sc.nextLine();
		
		add(nric);
	}
	private void add(String nric) {
		String name, gender, nationality, address, country;
		String cName, cAddress, cCountry, cExp, option;
		long cCardNo = 0;
		int cCvv = 0;
		CreditCard.CardType cCardType = null;
		
		System.out.print("Enter name: ");
		name = sc.nextLine();
		System.out.print("Enter gender: ");
		gender = sc.nextLine();
		System.out.print("Enter country: ");
		country = sc.nextLine();
		System.out.print("Enter nationality: ");
		nationality = sc.nextLine();
		System.out.print("Enter address: ");
		address = sc.nextLine();
		
		// Add Credit Card Details
		cName = cAddress = cCountry = cExp = option = "";
		while (option == "") {
			System.out.print("Use the same name, country, and address for Credit Card? (Y/n): ");
			option = sc.nextLine();
			if (option.equalsIgnoreCase("y") || option.equalsIgnoreCase("n")) {
				if (option.equalsIgnoreCase("y")) {
					cName = name;
					cAddress = address;
					cCountry = country;
				}
				else {
					System.out.print("Enter name: ");
					cName = sc.nextLine();
					System.out.print("Enter country: ");
					cCountry = sc.nextLine();
					System.out.print("Enter address: ");
					cAddress = sc.nextLine();
				}
				cCardType = getCardType();
				while (cCardNo == 0) {
					try {
						System.out.print("Enter card number: ");
						cCardNo = sc.nextLong();
						sc.nextLine();	// clear the "\n" in the buffer
					}
					catch (InputMismatchException e) {
						System.out.println("Invalid input. Please enter an integer");
						cCardNo = 0;
						sc.nextLine();	// clear the input in the buffer
					}
					catch (Exception e) {
						System.out.println("Error!! Error message: " + e.getMessage());
						cCardNo = 0;
						sc.nextLine();	// clear the input in the buffer
					}
				}
				
				System.out.print("Enter exp (mm/yy): ");
				cExp = validateExp(cExp, "Enter exp (mm/yy): ");
				
				while (cCvv == 0) {
					try {
						System.out.print("Enter cvv: ");
						cCvv = sc.nextInt();
						sc.nextLine();	// clear the "\n" in the buffer
					}
					catch (InputMismatchException e) {
						System.out.println("Invalid input. Please enter an integer");
						cCvv = 0;
						sc.nextLine();	// clear the input in the buffer
					}
					catch (Exception e) {
						System.out.println("Error!! Error message: " + e.getMessage());
						cCvv = 0;
						sc.nextLine();	// clear the input in the buffer
					}
				}
				
			}
			else {
				option = "";
				System.out.println("Invalid input. Please enter Y/n");
			}
		}
		
		Guest g = new Guest(nric, name, gender, nationality, address, country, cName, cAddress, cCountry, cExp, cCardNo, cCvv, cCardType);
		guestList.add(g);
		dataAccess.insertObject(g);
		System.out.println("Guest added");
	}
	private void print(Guest g) {
		System.out.printf("NRIC: %s, Name: %s, Gender: %s, Country: %s, Nationality: %s, Address: %s\n", g.getNRIC(), g.getName(), g.getGender(), g.getCountry(), g.getNationality(), g.getAddress());
		System.out.println("Credit Card Details: ");
		System.out.printf("Card Type: %s, Name: %s, Country: %s, Address: %s, Card No: %d, Exp: %s, CVV: %d\n", g.getCreditCard().getCardType().toString(), g.getCreditCard().getName(), g.getCreditCard().getCountry(), g.getCreditCard().getAddress(), g.getCreditCard().getCardNo(), g.getCreditCard().getExp(), g.getCreditCard().getCvv());
	}
	private ArrayList<Guest> searchGuest() {
		ArrayList<Guest> tempGuest = new ArrayList<Guest>();
		System.out.print("Enter guest name: ");
		String name = sc.nextLine();
		
		if (guestList.size() == 0) {
			return tempGuest;
		}
		for (Guest g : guestList) {
			if (g.getName().contains(name)) {
				tempGuest.add(g);
			}
		}
		return tempGuest;
	}
	private Guest searchGuestByNRIC(String nric) {
		Guest guest = null;
		
		for (Guest g : guestList) {
			if (g.getNRIC().equalsIgnoreCase(nric)) {
				guest = g;
				break;
			}
		}
		
		return guest;
	}
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
	private CreditCard.CardType getCardType() {
		int choice = -1;
		CreditCard.CardType cardType = null;
		
		do {
			System.out.println("\n+-------------------+");
			System.out.println("| Select card type: |");
			System.out.println("| 1. Mastercard     |");
			System.out.println("| 2. Visa           |");
			System.out.println("+-------------------+");
			System.out.print("Enter choice: ");
			choice = validateChoice(choice, "Enter choice: ");
			switch (choice) {
				case 1:
					cardType = CreditCard.CardType.MASTER;
					break;
				case 2:
					cardType = CreditCard.CardType.VISA;
					break;
				default:
					System.out.println("Invalid Choice");
					break;
			}
		} while (choice != 1 && choice != 2);
		return cardType;
	}
	private String validateExp(String exp, String inputText) {
		boolean valid = false, isNum1 = false, isNum2 = false;
		
		while (!valid) {
			exp = sc.nextLine();
			if (!exp.contains("/")) {
				System.out.println("Invalid expiry date. Please enter an expiry date");
				System.out.print(inputText);
			}
			else {
				String[] parts = exp.split("/");
				if (parts.length == 2) {
					isNum1 = isInteger(parts[0]);
					isNum2 = isInteger(parts[1]);
				}
				if (!isNum1 || !isNum2) {
					System.out.println("Invalid expiry date. Please enter an expiry date");
					System.out.print(inputText);
				}
				else {
					valid = true;
				}
			}
		}
		
		return exp;
	}
	private boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	private void modifyCreditCard(Guest guest) {
		int option = -1;
		String answer = "";
		
		do {
			System.out.println("\n+------------------------------+");
			System.out.println("| What you you like to modify? |");
			System.out.println("+------------------------------+");
			System.out.println("| 0. Nothing                   |");
			System.out.println("| 1. Name                      |");
			System.out.println("| 2. Address                   |");
			System.out.println("| 3. Country                   |");
			System.out.println("| 4. Expiry Date               |");
			System.out.println("| 5. Card Number               |");
			System.out.println("| 6. CVV                       |");
			System.out.println("| 7. Card Type                 |");
			System.out.println("+------------------------------+");
			System.out.print("Enter choice: ");
			option = validateChoice(option, "Enter choice: ");
			
			switch (option) {
				case 1:
					System.out.print("Enter new name: ");
					String name = sc.nextLine();
					guest.getCreditCard().setName(name);
					System.out.println("Name changed");
					answer = "";
					while (answer == "") {
						System.out.print("Change guest's name with new name (Y/n): ");
						answer = sc.nextLine();
						if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
							if (answer.equalsIgnoreCase("y")) {
								guest.setName(name);
								System.out.println("Guest's name changed");
							}
						}
						else {
							answer = "";
							System.out.println("Invalid input. Please enter Y/n");
						}
					}
					break;
				case 2:
					System.out.print("Enter new address: ");
					String address = sc.nextLine();
					guest.getCreditCard().setAddress(address);
					System.out.print("Address changed");
					answer = "";
					while (answer == "") {
						System.out.print("Change guest's address with new name (Y/n): ");
						answer = sc.nextLine();
						if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
							if (answer.equalsIgnoreCase("y")) {
								guest.setName(address);
								System.out.println("Guest's address changed");
							}
						}
						else {
							answer = "";
							System.out.println("Invalid input. Please enter Y/n");
						}
					}
					break;
				case 3:
					System.out.print("Enter new country: ");
					String country = sc.nextLine();
					guest.getCreditCard().setCountry(country);
					System.out.println("Country changed");
					answer = "";
					while (answer == "") {
						System.out.print("Change guest's country with new name (Y/n): ");
						answer = sc.nextLine();
						if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
							if (answer.equalsIgnoreCase("y")) {
								guest.setName(country);
								System.out.println("Guest's country changed");
							}
						}
						else {
							answer = "";
							System.out.println("Invalid input. Please enter Y/n");
						}
					}
					break;
				case 4:
					String exp = "";
					System.out.print("Enter exp (mm/yy): ");
					exp = validateExp(exp, "Enter exp (mm/yy): ");
					guest.getCreditCard().setExp(exp);
					System.out.println("Expiry date changed");
					break;
				case 5:
					long cardNo = 0;
					while (cardNo == 0) {
						try {
							System.out.print("Enter card number: ");
							cardNo = sc.nextLong();
							sc.nextLine();	// clear the "\n" in the buffer
						}
						catch (InputMismatchException e) {
							System.out.println("Invalid input. Please enter an integer");
							cardNo = 0;
							sc.nextLine();	// clear the input in the buffer
						}
						catch (Exception e) {
							System.out.println("Error!! Error message: " + e.getMessage());
							cardNo = 0;
							sc.nextLine();	// clear the input in the buffer
						}
					}
					guest.getCreditCard().setCardNo(cardNo);
					System.out.println("Card number changed");
					break;
				case 6:
					int cvv = 0;
					while (cvv == 0) {
						try {
							System.out.print("Enter cvv: ");
							cvv = sc.nextInt();
							sc.nextLine();	// clear the "\n" in the buffer
						}
						catch (InputMismatchException e) {
							System.out.println("Invalid input. Please enter an integer");
							cvv = 0;
							sc.nextLine();	// clear the input in the buffer
						}
						catch (Exception e) {
							System.out.println("Error!! Error message: " + e.getMessage());
							cvv = 0;
							sc.nextLine();	// clear the input in the buffer
						}
					}
					guest.getCreditCard().setCvv(cvv);
					System.out.println("CVV changed");
					break;
				case 7:
					CreditCard.CardType cardType = null;
					cardType = getCardType();
					guest.getCreditCard().setCardType(cardType);
					System.out.println("Card Type changed");
					break;
				default:
					System.out.println("Invalid choice");
					break;
			}
		} while (option != 0);
	}
	
	private Guest copyGuest(Guest guest) {
		Guest guest2 = new Guest(
				guest.getNRIC(), guest.getName(), guest.getGender(), guest.getNationality(), guest.getAddress(), guest.getCountry(),
				guest.getCreditCard().getName(), guest.getCreditCard().getAddress(), guest.getCreditCard().getCountry(),
				guest.getCreditCard().getExp(), guest.getCreditCard().getCardNo(), guest.getCreditCard().getCvv(),
				guest.getCreditCard().getCardType());
		
		return guest2;
	}
}
