package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class RemoveRoomService {
	public static void removeRoomService(int reservationId) {
		try {
			String selectRoomservice = "Select room_service_id from room_service where reservation_id = " + reservationId;
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
					   ResultSet.CONCUR_READ_ONLY);
			ResultSet roomServiceRS = st.executeQuery(selectRoomservice);
			
			// check if there's any room service
			if(roomServiceRS.next()) {
				roomServiceRS.previous();
				// Model of reservation -> room service
				// 1 reservation -> Many room service
				while (roomServiceRS.next()) {
					int roomServiceId = roomServiceRS.getInt("room_service_id");
					String removeRoomServiceMenuItem = "remove from room_service_menu_item where room_service_id = " + roomServiceId;
					String removeRoomService = "remove from room_service where room_service_id = " + roomServiceId;
					int roomServiceMenuItemRowsAffected = st.executeUpdate(removeRoomServiceMenuItem);
					int roomServiceRowsAffected = st.executeUpdate(removeRoomService);
					System.out.println(roomServiceMenuItemRowsAffected + " rows affected in the room_service_menu_item table when removing room_service_id = " + roomServiceId + "\n" +
									   roomServiceRowsAffected + " rows affected in the room_service table when removing room_service_id = " + roomServiceId);
				}
			}
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
