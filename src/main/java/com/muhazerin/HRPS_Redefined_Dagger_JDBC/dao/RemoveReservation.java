package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class RemoveReservation {
	public static void removeReservation(int id, String arg) {
		try {
			String selectReservation, removeReservation;
			
			if (arg.equalsIgnoreCase("guest")) {
				selectReservation = "Select reservation_id from reservation where guest_id = " + id;
				removeReservation = "remove from reservation where guest_id = " + id;
			}
			else if (arg.equalsIgnoreCase("room")) {
				selectReservation = "Select reservation_id from reservation where room_id = " + id;
				removeReservation = "remove from reservation where room_id = " + id;

			}
			else {
				System.out.println("Invalid argument: " + arg);
				return;
			}
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
					   ResultSet.CONCUR_READ_ONLY);
			ResultSet reservationRS = st.executeQuery(selectReservation);
			
			// checks if there is reservation
			if (reservationRS.next()) {
				reservationRS.previous();
				while (reservationRS.next()) {
					int reservationId = reservationRS.getInt("reservation_id");
					RemoveRoomService.removeRoomService(reservationId);
				}
				int reservationRowsAffected = st.executeUpdate(removeReservation);
				System.out.println(reservationRowsAffected + " rows affected in the reservation table when removing "+ arg +"_id = " + id);
			}
			
		}
		catch (Exception ex) {
			System.out.println("Inside RemoveReservation.removeReservation: " + ex.getMessage());
		}
		
	}
}
