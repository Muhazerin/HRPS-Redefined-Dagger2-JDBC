package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.MenuItem;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.RoomService;

public abstract class RoomServiceDAO {
	public static ArrayList<RoomService> retrieveRoomService(int reservationId) {
		try {
			ArrayList<RoomService> roomServiceList = new ArrayList<RoomService>();
			String query = "SELECT * FROM room_service WHERE reservation_id = " + reservationId;
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			
			RoomService roomService = null;
			while (rs.next()) {
				ArrayList<MenuItem> menuItemList = retrieveMenuItem(rs.getInt("room_service_id"));
				roomService = new RoomService(menuItemList);
				
				RoomService.RoomServiceStatus roomServiceStatus = null;
				roomServiceStatus = assignRoomServiceStatus(roomServiceStatus, rs);
				roomService.setRoomServiceStatus(roomServiceStatus);
				
				Date tempOrderDate = rs.getDate("order_date");
				LocalDate orderDate = Instant.ofEpochMilli(tempOrderDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(); 
				roomService.setOrderDate(orderDate);
				
				roomServiceList.add(roomService);
			}
			
			st.close();
			con.close();
			
			return roomServiceList;
		}
		catch (Exception e) {
			System.out.println("Inside RoomServiceDAO.retrieveRoomService: " + e.getMessage());
		}
		
		return null;
	}
	
	public static void addRoomService(int reservationId, RoomService roomService) {
		try {
			String query = String.format("INSERT INTO room_service (room_service_status, order_date, reservation_id) VALUES ('%s', '%s', %d)", 
					roomService.getRoomServiceStatus().toString(), roomService.getOrderDate().toString(), reservationId);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			int rowsAffected = st.executeUpdate(query);
			
			System.out.println(rowsAffected + " rows affected in the room_service table");
			
			query = String.format("SELECT room_service_id FROM room_service where room_service_status = '%s' AND order_date = '%s' AND reservation_id = %d",
					roomService.getRoomServiceStatus().toString(), roomService.getOrderDate().toString(), reservationId);
			ResultSet rs = st.executeQuery(query);
			rs.next();
			int roomServiceId = rs.getInt("room_service_id");
			
			String menuItemQuery = "";
			for (MenuItem menuItem : roomService.getRoomService()) {
				menuItemQuery = String.format("SELECT menu_item_id FROM menu_item where name = '%s' AND description = '%s' AND price = %d",
						menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
				ResultSet menuItemRS = st.executeQuery(menuItemQuery);
				menuItemRS.next();
				int menuItemId = menuItemRS.getInt("menu_item_id");
				
				menuItemQuery = String.format("INSERT INTO room_service_menu_item (menu_item_id, room_service_id) VALUES (%d, %d)", 
						menuItemId, roomServiceId);
				rowsAffected = st.executeUpdate(menuItemQuery);
				
				System.out.println(rowsAffected + " rows affected in the room_service_menu_item table");
			}
			
			st.close();
			con.close();
		}
		catch (Exception e) {
			System.out.println("Inside RoomServiceDAO.addRoomService: " + e.getMessage());
		}
	}
	
	private static ArrayList<MenuItem> retrieveMenuItem(int roomServiceId) {
		try {
			ArrayList<MenuItem> menuItemList = new ArrayList<MenuItem>();
			String query = "SELECT menu_item.* FROM room_service_menu_item JOIN menu_item ON room_service_menu_item.menu_item_id = menu_item.menu_item_id WHERE room_service_id = " + roomServiceId;
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			MenuItem menuItem = null;
			while (rs.next()) {
				menuItem = new MenuItem(rs.getString("name"), rs.getString("description"), rs.getDouble("price"));
				menuItemList.add(menuItem);
			}
			
			return menuItemList;
		}
		catch (Exception e) {
			System.out.println("Inside RoomServiceDAO.retrieveMenuItem: " + e.getMessage());
		}
		return null;
	}
	
	private static RoomService.RoomServiceStatus assignRoomServiceStatus(RoomService.RoomServiceStatus roomServiceStatus, ResultSet rs) {
		try {
			if (rs.getString("room_service_status").equalsIgnoreCase("ORDERED")) {
				roomServiceStatus = RoomService.RoomServiceStatus.ORDERED;
			}
			else if (rs.getString("room_service_status").equalsIgnoreCase("PREPARING")) {
				roomServiceStatus = RoomService.RoomServiceStatus.PREPARING;
			}
			else {
				roomServiceStatus = RoomService.RoomServiceStatus.DELIVERED;
			}
		}
		catch (Exception e) {
			System.out.println("Inside RoomServiceDAO.assignRoomServiceStatus: " + e.getMessage());
		}
		return roomServiceStatus;
	}
}
