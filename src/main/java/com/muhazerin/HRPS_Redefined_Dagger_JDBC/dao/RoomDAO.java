package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.inject.Inject;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.DeluxeRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Entity;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Room;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.SingleRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.StandardRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.VipSuiteRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;

public class RoomDAO implements DataAccess {

	@Inject
	public RoomDAO() {}
	
	@Override
	public ArrayList<Entity> retrieveAllObjects() {
		try {
			ArrayList<Entity> tempList = new ArrayList<Entity>();
			String query = "select * from room";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);

			Room room = null;
			while(rs.next()) {
				Room.BedType bedType = null;
				Room.AvailabilityStatus roomAvailabilityStatus = null;
				boolean wifiEnabled, smokingAllowed;
				wifiEnabled = smokingAllowed = false;
				
				wifiEnabled = assignWifiEnabled(wifiEnabled, rs);
				smokingAllowed = assignSmokingAllowed(smokingAllowed, rs);
				bedType = assignBedType(bedType, rs);
				roomAvailabilityStatus = assignAvailabilityStatus(roomAvailabilityStatus, rs);
				
				if (rs.getString("room_type").equalsIgnoreCase("Single")) {
					room = new SingleRoom(bedType, roomAvailabilityStatus, wifiEnabled, rs.getString("facing"), smokingAllowed, rs.getInt("room_level"), rs.getInt("room_number"), rs.getDouble("rate"));
				}
				else if (rs.getString("room_type").equalsIgnoreCase("Standard")) {
					room = new StandardRoom(bedType, roomAvailabilityStatus, wifiEnabled, rs.getString("facing"), smokingAllowed, rs.getInt("room_level"), rs.getInt("room_number"), rs.getDouble("rate"));
				}
				else if (rs.getString("room_type").equalsIgnoreCase("Deluxe")) {
					room = new DeluxeRoom(bedType, roomAvailabilityStatus, wifiEnabled, rs.getString("facing"), smokingAllowed, rs.getInt("room_level"), rs.getInt("room_number"), rs.getDouble("rate"));
				}
				else {
					room = new VipSuiteRoom(bedType, roomAvailabilityStatus, wifiEnabled, rs.getString("facing"), smokingAllowed, rs.getInt("room_level"), rs.getInt("room_number"), rs.getDouble("rate"));
				}
				
				tempList.add(room);
			}
			st.close();
			con.close();
			
			return tempList;
		}
		catch (Exception ex) {
			System.out.println("Inside RoomDAO.retrieveAllObjects: " + ex.getMessage());
		}
		return null;
	}

	@Override
	public void insertObject(Entity entity) {
		try {
			Room room;
			if (entity instanceof Room) {
				room = (Room)entity;
			}
			else {
				System.out.println("Object is not of type room");
				return;
			}
			String query = String.format("insert into room (room_level, room_number, bed_type, avail_status, wifi_enabled, smoking_allowed, facing, rate, room_type) values (%d, %d, '%s', '%s', %b, %b, '%s', %f, '%s')", 
					room.getRoomLevel(), room.getRoomNumber(), room.getBedType(), room.getAvailabilityStatus(), room.isWifiEnabled(), room.isSmokingAllowed(), room.getFacing(), room.getRate(), room.getRoomType());
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			int rowsAffected = st.executeUpdate(query);
			
			System.out.println(rowsAffected + " rows affected in the room table when inserting room");
			
			st.close();
			con.close();
		}
		catch (Exception ex) {
			System.out.println("Inside RoomDAO.insertObject: " + ex.getMessage());
		}
	}

	@Override
	public void updateObject(Entity oldEntity, Entity newEntity) {
		try {
			Room oldRoom, newRoom;
			if (oldEntity instanceof Room) {
				oldRoom = (Room) oldEntity;
			}
			else {
				System.out.println("Object is not of type room");
				return;
			}
			
			if (newEntity instanceof Room) {
				newRoom = (Room) newEntity;
			}
			else {
				System.out.println("Object is not of type room");
				return;
			}
			
			
			String query = String.format("update room set room_level = %d, room_number = %d, bed_type = '%s', avail_status = '%s', wifi_enabled = %b, smoking_allowed = %b, facing = '%s', rate = %f, room_type = '%s'"
					+ " where room_level = %d and room_number = %d and bed_type = '%s' and avail_status = '%s' and wifi_enabled = %b and smoking_allowed = %b and facing = '%s' and rate = %f and room_type = '%s'",
					newRoom.getRoomLevel(), newRoom.getRoomNumber(), newRoom.getBedType(), newRoom.getAvailabilityStatus(), newRoom.isWifiEnabled(), newRoom.isSmokingAllowed(), newRoom.getFacing(), newRoom.getRate(), newRoom.getRoomType(),
					oldRoom.getRoomLevel(), oldRoom.getRoomNumber(), oldRoom.getBedType(), oldRoom.getAvailabilityStatus(), oldRoom.isWifiEnabled(), oldRoom.isSmokingAllowed(), oldRoom.getFacing(), oldRoom.getRate(), oldRoom.getRoomType());
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			int rowsAffected = st.executeUpdate(query);
			
			System.out.println(rowsAffected + " rows affected in the room table when updating room");
			
			st.close();
			con.close();
		}
		catch (Exception ex) {
			System.out.println("Inside RoomDAO.updateObject: " + ex.getMessage());
		}
	}

//	@Override
//	public void removeObject(Entity entity) {
//		try {
//			Room room;
//			if (entity instanceof Room) {
//				room = (Room) entity;
//			}
//			else {
//				System.out.println("Object is not of type room");
//				return;
//			}
//			
//			// need to remove from tables that uses room as foreign key first
//			// which is reservation			
//			String selectRoom =  String.format("select room_id from room where room_level = %d and room_number = %d and bed_type = %s and avail_status = %s and wifi_enabled = %b and smoking_allowed = %b and facing = %s and rate = %d and room_type = %s", 
//					room.getRoomLevel(), room.getRoomNumber(), room.getBedType(), room.getAvailabilityStatus(), room.isWifiEnabled(), room.isSmokingAllowed(), room.getFacing(), room.getRate(), room.getRoomType());
//			
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps", "muhazerin", "CDW=$V6*BJ-M]gB");
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(selectRoom);
//			
//			if (rs.next()) {
//				int roomId = rs.getInt("room_id");
//				
//				RemoveReservation.removeReservation(roomId, "room");
//				
//				String removeRoom = "remove from room where room_id = " + roomId;
//				int roomRowsAffected = st.executeUpdate(removeRoom);
//				System.out.println(roomRowsAffected + " rows affected in the room table when removing room_id = " + roomId);
//			}
//			else {
//				System.out.println("Room not found");
//			}
//			
//			st.close();
//			con.close();
//		}
//		catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
//	}

	public static boolean assignWifiEnabled(boolean wifiEnabled, ResultSet rs) {
		try {
			if (rs.getBoolean("wifi_enabled")) {
				wifiEnabled = true;
			}
			else {
				wifiEnabled = false;
			}
		} catch (Exception e) {
			System.out.println("Inside RoomDAO.assignWifiEnabled: " + e.getMessage());
		}
		return wifiEnabled;
	}
	
	public static boolean assignSmokingAllowed(boolean smokingAllowed, ResultSet rs) {
		try {
			if (rs.getBoolean("smoking_allowed")) {
				smokingAllowed = true;
			}
			else {
				smokingAllowed = false;
			}
		} catch (Exception e) {
			System.out.println("Inside RoomDAO.assignSmokingAllowed: " + e.getMessage());
		}
		return smokingAllowed;
	}
	
	public static Room.BedType assignBedType(Room.BedType bedType, ResultSet rs) {
		try {
			if (rs.getString("bed_type").equalsIgnoreCase("single")) {
				bedType = Room.BedType.SINGLE;
			}
			else if (rs.getString("bed_type").equalsIgnoreCase("double")) {
				bedType = Room.BedType.DOUBLE;
			}
			else {
				bedType = Room.BedType.MASTER;
			}
		}
		catch (Exception e) {
			System.out.println("Insisde RoomDAO.assignBedType: " + e.getMessage());
		}
		return bedType;
	}
	
	public static Room.AvailabilityStatus assignAvailabilityStatus(Room.AvailabilityStatus roomAvailabilityStatus, ResultSet rs) {
		try {
			if (rs.getString("avail_status").equalsIgnoreCase("vacant")) {
				roomAvailabilityStatus = Room.AvailabilityStatus.VACANT;
			}
			else if (rs.getString("avail_status").equalsIgnoreCase("occupied")) {
				roomAvailabilityStatus = Room.AvailabilityStatus.OCCUPIED;
			}
			else if (rs.getString("avail_status").equalsIgnoreCase("reserved")) {
				roomAvailabilityStatus = Room.AvailabilityStatus.RESERVED;
			}
			else {
				roomAvailabilityStatus = Room.AvailabilityStatus.MAINTENANCE;
			}
		}
		catch (Exception e) {
			System.out.println("Inside RoomDAO.assignAvailablityStatus: " + e.getMessage());
		}
		return roomAvailabilityStatus;
	}

}
