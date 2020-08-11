package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.inject.Inject;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Entity;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.MenuItem;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;

public class MenuItemDAO implements DataAccess {

	@Inject
	public MenuItemDAO() {}
	
	@Override
	public ArrayList<Entity> retrieveAllObjects() {
		try {
			ArrayList<Entity> tempList = new ArrayList<Entity>();
			
			MenuItem menuItem = null;
			String query = "select * from menu_item";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while (rs.next()) {
				menuItem = new MenuItem(rs.getString("name"),
						rs.getString("description"),
						rs.getDouble("price"));
				
				tempList.add(menuItem);
			}
			
			return tempList;
		}
		catch (Exception ex) {
			System.out.println("Inside MenuItemDAO.retrieveAllObjects: " + ex.getMessage());
		}
		return null;
	}

	@Override
	public void insertObject(Entity entity) {
		try {
			MenuItem menuItem;
			if (entity instanceof MenuItem) {
				menuItem = (MenuItem) entity;
			}
			else {
				System.out.println("Object is not of type menu item");
				return;
			}
			
			String query = String.format("insert into menu_item (name, description, price) values ('%s', '%s', %f)",
					menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
						
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			int rowsAffected = st.executeUpdate(query);
			
			System.out.println(rowsAffected + " rows affected in menu_item table when inserting menu item");
		
			st.close();
			con.close();
		}
		catch (Exception ex) {
			System.out.println("Inside MenuItemDAO.insertObject: " + ex.getMessage());
		}
	}

	@Override
	public void updateObject(Entity oldEntity, Entity newEntity) {
		try {
			MenuItem oldMenuItem, newMenuItem;
			if (oldEntity instanceof MenuItem) {
				oldMenuItem = (MenuItem) oldEntity;
			}
			else {
				System.out.println("Object is not of type menu item");
				return;
			}
			
			if (newEntity instanceof MenuItem) {
				newMenuItem = (MenuItem) newEntity;
			}
			else {
				System.out.println("Object is not of type menu item");
				return;
			}
			
			String query = String.format("update menu_item set name = '%s', description = '%s', price = %f" +
					" where name = '%s' and description = '%s' and price = %f", 
					newMenuItem.getName(), newMenuItem.getDescription(), newMenuItem.getPrice(),
					oldMenuItem.getName(), oldMenuItem.getDescription(), oldMenuItem.getPrice());
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			int rowsAffected = st.executeUpdate(query);
			
			System.out.println(rowsAffected + " rows affected in the menu_item table when updating menu item");
			
			st.close();
			con.close();
		}
		catch (Exception ex) {
			System.out.println("Inside MenuItemDAO.updateObject: " + ex.getMessage());
		}
	}

//	@Override
//	public void removeObject(Entity entity) {
//		try {
//			MenuItem menuItem;;
//			if (entity instanceof MenuItem) {
//				menuItem = (MenuItem) entity;
//			}
//			else {
//				System.out.println("Object is not of type menu item");
//				return;
//			}
//			
//			String selectMenuItem, removeMenuItem, removeRoomServiceMenuItem;
//			
//			selectMenuItem = String.format("select menu_item_id where name = %s and description = %s and price = %d", 
//					menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
//			
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps", "muhazerin", "CDW=$V6*BJ-M]gB");
//			// need the argument to move the rs forward and back
//			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
//					   ResultSet.CONCUR_READ_ONLY);
//			ResultSet menuItemRS = st.executeQuery(selectMenuItem);
//			
//			if (menuItemRS.next() ) {
//				int menuItemId = menuItemRS.getInt("menu_item_id");
//				
//				removeRoomServiceMenuItem = "remove from room_service_menu_item where menu_item_id =" + menuItemId;
//				int roomServiceMenuItemRowsAffected = st.executeUpdate(removeRoomServiceMenuItem);
//				System.out.println(roomServiceMenuItemRowsAffected + " rows affected in the room_service_menu_item table when removing menu_item_id = " + menuItemId);
//				
//				removeMenuItem = "remove from menu_item where menu_item_id = " + menuItemId;
//				int menuItemRowsAffected = st.executeUpdate(removeMenuItem);
//				System.out.println(menuItemRowsAffected + " rows affected in the menu_item table when removing menu_item_id = " + menuItemId);
//			}
//			st.close();
//			con.close();
//		}
//		catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
//	}

}
