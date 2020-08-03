package com.muhazerin.HRPS_Redefined_Dagger_JDBC.control;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import javax.inject.Inject;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Entity;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.MenuItem;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.AddObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.ModifyObject;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.PrintAllObjects;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.SelectMenuItems;

/**
 * 
 * @author muhazerin
 *
 */

public class MenuItemManager implements AddObject, SelectMenuItems, ModifyObject, PrintAllObjects{
	private ArrayList<MenuItem> menuItemList;
	private DataAccess dataAccess;
	private Scanner sc;
	
	@Inject
	public MenuItemManager(Scanner sc, DataAccess dataAccess) {
		this.dataAccess = dataAccess;
		this.sc = sc;
		
		menuItemList = new ArrayList<MenuItem>();
		ArrayList<Entity> entityArray = dataAccess.retrieveAllObjects();
		if (entityArray.size() > 0) {
			for (Entity entity : entityArray) {
				if (entity instanceof MenuItem) {
					MenuItem menuItem = (MenuItem) entity;
					menuItemList.add(menuItem);
				}
				else {
					System.out.println("Entity is not instance of MenuItem");
					break;
				}
			}
		}
	}

	@Override
	public void addObject() {
		String name = "", description = "";
		double price = 0;
		
		while (name.equals("")) {
			System.out.print("Enter menu item name: ");
			name = sc.nextLine();
			if (name.equals("")) {
				System.out.println("Menu item name cannot be empty");
			}
		}
		
		while (description.equals("")) {
			System.out.print("Enter menu item description: ");
			description = sc.nextLine();
			if (description.equals("")) {
				System.out.println("Menu item description cannot be empty");
			}
		}
		
		while (price < 1) {
			System.out.print("Enter menu item price: ");
			price = validatePrice(price, "Enter menu item price: $");
			if (price < 1) {
				System.out.println("Value must be more than 1");
			}
		}
		
		MenuItem menuItem = new MenuItem(name, description, price);
		menuItemList.add(menuItem);
		dataAccess.insertObject(menuItem);
		System.out.println("MenuItem has been added");
	}
	@Override
	public ArrayList<MenuItem> selectMenuItems() {
		ArrayList<MenuItem> tempList = new ArrayList<MenuItem>();
		
		printAll();
		String name = "";
		System.out.println("Enter 0 to exit");
		while (!name.equals("0")) {
			System.out.print("Enter menu item name: ");
			name = sc.nextLine();
			if (name.equals("0")) {
				break;
			}
			MenuItem mi = null;
			for (MenuItem menuItem : menuItemList) {
				if (menuItem.getName().equals(name)) {
					mi = menuItem;
					break;
				}
			}
			if (Objects.equals(mi, null)) {
				System.out.println("Menu item does not exist. Please enter the menu item full name");
			}
			else {
				tempList.add(mi);
				System.out.println("Menu item added");
			}
		}
		
		return tempList;
	}
	@Override
	public void modify() {
		ArrayList<MenuItem> tempList = searchMenuItem();
		if (tempList.size() == 0) {
			System.out.println("Menu item is not found in the menu item list");
			return;
		}
		if (tempList.size() > 1) {
			System.out.println("Multiple menu items found. Please refine your search query");
			for (MenuItem menuItem : tempList) {
				System.out.println("Name: " + menuItem.getName());
			}
			return;
		}
		MenuItem menuItem = tempList.get(0);
		MenuItem menuItem2 = copyMenuItem(menuItem);
		
		int option = -1;
		do {
			System.out.println("\n+------------------------------+");
			System.out.println("| What you you like to modify? |");
			System.out.println("+------------------------------+");
			System.out.println("| 0. Nothing                   |");
			System.out.println("| 1. Name                      |");
			System.out.println("| 2. Description               |");
			System.out.println("| 3. Price                     |");
			System.out.println("+------------------------------+");
			System.out.print("Enter choice: ");
			option = validateChoice(option, "Enter choice: ");
			
			switch(option) {
				case 0:
					System.out.println("Going back...");
					dataAccess.updateObject(menuItem2, menuItem);
					break;
				case 1:
					String name = "";
					while (name.equals("")) {
						System.out.print("Enter new name: ");
						name = sc.nextLine();
						if (name.equals("")) {
							System.out.println("Menu item name cannot be empty");
						}
					}
					menuItem.setName(name);
					System.out.println("Name changed");
					break;
				case 2:
					String description = "";
					while (description.equals("")) {
						System.out.print("Enter menu item description: ");
						description = sc.nextLine();
						if (description.equals("")) {
							System.out.println("Menu item description cannot be empty");
						}
					}
					menuItem.setDescription(description);
					System.out.println("Description changed");
					break;
				case 3:
					double price = 0;
					while (price < 1) {
						System.out.print("Enter new price: ");
						price = validatePrice(price, "Enter new price: ");
						if (price < 1) {
							System.out.println("Value must be more than 1");
						}
					}
					menuItem.setPrice(price);
					System.out.println("Price changed");
					break;
				default:
					System.out.println("Invalid choice");
					break;
			}
			
		} while (option != 0);
	}
//	@Override
//	public void removeObject() {
//		ArrayList<MenuItem> tempList = searchMenuItem();
//		if (tempList.size() == 0) {
//			System.out.println("Menu item is not found in the menu item list");
//			return;
//		}
//		if (tempList.size() > 1) {
//			System.out.println("Multiple menu items found. Please refine your search query");
//			for (MenuItem menuItem : tempList) {
//				System.out.println("Name: " + menuItem.getName());
//			}
//			return;
//		}
//		menuItemList.remove(tempList.get(0));
//		System.out.println("Menu item removed");
//		dataAccess.removeObject(tempList.get(0));
//	}
	@Override
	public void printAll() {
		if (menuItemList.size() == 0) {
			System.out.println("There's no menu item in the menu item list");
			return;
		}
		for (MenuItem menuItem: menuItemList) {
			print(menuItem);
		}
	}
	
	private double validatePrice(double price, String inputText) {
boolean valid = false;
		
		while (!valid) {
			if (!sc.hasNextDouble()) {
				System.out.println("Invalid Input. Please enter an floating point value");
				sc.nextLine();	// clear the input in the buffer
				System.out.print(inputText);
			}
			else {
				valid = true;
				price = sc.nextDouble();
				sc.nextLine();	// clear the "\n" in the buffer
			}
		}
		
		return price;
	}
	private void print(MenuItem menuItem) {
		System.out.printf("Name: %s\nDescription: %s\nPrice: $%.2f\n\n", menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
	}
	private ArrayList<MenuItem> searchMenuItem() {
		ArrayList<MenuItem> tempList = new ArrayList<MenuItem>();
		System.out.print("Enter menu item name: ");
		String name = sc.nextLine();
		
		if (menuItemList.size() == 0) {
			return tempList;
		}
		for (MenuItem menuItem : menuItemList) {
			if (menuItem.getName().contains(name)) {
				tempList.add(menuItem);
			}
		}
		return tempList;
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
	
	private MenuItem copyMenuItem(MenuItem menuItem)
	{
		MenuItem menuItem2 = new MenuItem(menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
		return menuItem2;
	}

}
