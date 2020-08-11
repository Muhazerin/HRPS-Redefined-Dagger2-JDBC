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

import javax.inject.Inject;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.DeluxeRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Entity;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Guest;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Reservation;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Room;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.RoomService;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.SingleRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.StandardRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.VipSuiteRoom;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;

public class ReservationDAO implements DataAccess {

	@Inject
	public ReservationDAO() {}
	
	@Override
	public ArrayList<Entity> retrieveAllObjects() {
		try {
			ArrayList<Entity> tempList = new ArrayList<Entity>();
			String query = "SELECT guest.*, room.*, reservation.* FROM reservation JOIN guest ON guest.guest_id = reservation.guest_id JOIN room ON room.room_id = reservation.room_id";
		
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			Guest guest = null;
			Room room = null;
			Reservation reservation = null;
			
			while (rs.next()) {
				Room.BedType bedType = null;
				Room.AvailabilityStatus roomAvailabilityStatus = null;
				boolean wifiEnabled, smokingAllowed;
				wifiEnabled = smokingAllowed = false;
				
				wifiEnabled = RoomDAO.assignWifiEnabled(wifiEnabled, rs);
				smokingAllowed = RoomDAO.assignSmokingAllowed(smokingAllowed, rs);
				bedType = RoomDAO.assignBedType(bedType, rs);
				roomAvailabilityStatus = RoomDAO.assignAvailabilityStatus(roomAvailabilityStatus, rs);
				
				if (rs.getString("room_type").equalsIgnoreCase("Single")) {
					room = new SingleRoom(bedType, roomAvailabilityStatus, wifiEnabled, rs.getString("facing"), smokingAllowed, rs.getInt("room_level"), rs.getInt("room_number"));
				}
				else if (rs.getString("rooom_type").equalsIgnoreCase("Standard")) {
					room = new StandardRoom(bedType, roomAvailabilityStatus, wifiEnabled, rs.getString("facing"), smokingAllowed, rs.getInt("room_level"), rs.getInt("room_number"));
				}
				else if (rs.getString("room_type").equalsIgnoreCase("Deluxe")) {
					room = new DeluxeRoom(bedType, roomAvailabilityStatus, wifiEnabled, rs.getString("facing"), smokingAllowed, rs.getInt("room_level"), rs.getInt("room_number"));
				}
				else {
					room = new VipSuiteRoom(bedType, roomAvailabilityStatus, wifiEnabled, rs.getString("facing"), smokingAllowed, rs.getInt("room_level"), rs.getInt("room_number"));
				}
				
				guest = GuestDAO.assignGuest(guest, rs);
				
				
				LocalDate checkInDate, checkOutDate;
				Date tempCheckInDate = rs.getDate("check_in_date");
				checkInDate = Instant.ofEpochMilli(tempCheckInDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(); 
				
				Date tempCheckOutDate = rs.getDate("check_out_date");
				if (tempCheckOutDate == null) {
					checkOutDate = null;
				}
				else {
					checkOutDate = Instant.ofEpochMilli(tempCheckOutDate.getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate(); 
				}
				
				Reservation.ResStatus resStatus = null;
				if (rs.getString("res_status").equalsIgnoreCase("CONFIRMED")) {
					resStatus = Reservation.ResStatus.CONFIRMED;
				}
				else if (rs.getString("res_status").equalsIgnoreCase("IN_WAITLIST")) {
					resStatus = Reservation.ResStatus.IN_WAITLIST;
				}
				else if (rs.getString("res_status").equalsIgnoreCase("CHECKED_IN")) {
					resStatus = Reservation.ResStatus.CHECKED_IN;
				}
				else if (rs.getString("res_status").equalsIgnoreCase("EXPIRED")) {
					resStatus = Reservation.ResStatus.EXPIRED;
				}
				else if (rs.getString("res_status").equalsIgnoreCase("CHECKED_OUT")) {
					resStatus = Reservation.ResStatus.CHECKED_OUT;
				}
				else {
					resStatus = Reservation.ResStatus.CANCELLED;
				}
				
				reservation = new Reservation(guest, room, checkInDate, rs.getInt("no_of_adults"), rs.getInt("no_of_children"), resStatus);
				if (checkOutDate != null) {
					reservation.setCheckOutDate(checkOutDate);
				}
				
				ArrayList<RoomService> roomServiceList = RoomServiceDAO.retrieveRoomService(rs.getInt("reservation_id"));
				if(roomServiceList.size() > 0) {
					reservation.setRoomServiceList(roomServiceList);
				}
				
				tempList.add(reservation);
			}
			
			st.close();
			con.close();
			
			return tempList;
		}
		catch (Exception ex) {
			System.out.println("Inside ReservationDAO.retrieveAllObjects: " + ex.getMessage());
		}
		return null;
	}

	@Override
	public void insertObject(Entity entity) {
		// Inserting Reservation = Creating new Reservation = No Room Service
		try {
			Reservation reservation;
			if (entity instanceof Reservation) {
				reservation = (Reservation) entity;
			}
			else {
				System.out.println("Object is not of type Reservation");
				return;
			}
			int guest_id = retrieveGuestId(reservation.getGuest());
			int room_id = retrieveRoomId(reservation.getRoom());
			String query = String.format("INSERT INTO reservation (guest_id, room_id, check_in_date, no_of_adults, no_of_children, res_status) values (%d, %d, '%s', %d, %d, '%s')", 
					guest_id, room_id, reservation.getCheckInDate().toString(), reservation.getNoOfAdults(), reservation.getNoOfChildren(), reservation.getResStatus().toString());
		
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			int rowsAffected = st.executeUpdate(query);
			
			System.out.println(rowsAffected + " rows affected in the reservation table");
			
			st.close();
			con.close();
		}
		catch (Exception e) {
			System.out.println("Inside ReservationDAO.insertObject: " + e.getMessage());
		}
	}

	@Override
	public void updateObject(Entity oldEntity, Entity newEntity) {
		try {
			Reservation oldReservation, newReservation;
			if (oldEntity instanceof Reservation) {
				oldReservation = (Reservation) oldEntity;
			}
			else {
				System.out.println("oldEntity is not of type Reservaiton");
				return;
			}
			if (newEntity instanceof Reservation) {
				newReservation = (Reservation) newEntity;
			}
			else {
				System.out.println("newEntity is not of type Reservaiton");
				return;
			}

			// the only changes to the room service is when guest adds more room service
			boolean addRoomService = false;
			if (oldReservation.getRoomServiceList().size() != newReservation.getRoomServiceList().size()) {
				addRoomService = true;
			}
			
			// possible reservation related changes: 
			// 1. number of children
			// 2. number of adults
			// 3. user checks in
			// 4. user checks out
			// 5. reservation cancelled
			
			int reservationId = retrieveReservationId(oldReservation);
			String updateQuery = "";
			
			// reservation cancelled
			if (newReservation.getResStatus().toString().equalsIgnoreCase("CANCELLED") && !oldReservation.getResStatus().toString().equalsIgnoreCase("CANCELLED")) {
				updateQuery = "UPDATE reservation SET res_status = '" + newReservation.getResStatus().toString() + "' WHERE reservation_id = " + reservationId;
			}
			// checkout reservation 
			if (newReservation.getResStatus().toString().equalsIgnoreCase("CHECKED_OUT") && !oldReservation.getResStatus().toString().equalsIgnoreCase("CHECKED_OUT")) {
				updateQuery = "UPDATE reservation SET res_status = '" + newReservation.getResStatus().toString() + "', check_out_date = '" + newReservation.getCheckOutDate().toString() + "' WHERE reservation_id = " + reservationId;
			}
			// no of children/adults changed
			if ((newReservation.getNoOfAdults() != oldReservation.getNoOfAdults()) || (newReservation.getNoOfChildren() != oldReservation.getNoOfChildren())) {
				updateQuery = "UPDATE reservation SET no_of_adults = '" + newReservation.getNoOfAdults() + "', no_of_children = " + newReservation.getNoOfChildren() + " WHERE reservation_id = " + reservationId;
			}
			// user checks in
			if (oldReservation.getResStatus().toString().equalsIgnoreCase("CONFIRMED") && newReservation.getResStatus().toString().equalsIgnoreCase("CHECKED_IN")) {
				updateQuery = "UPDATE reservation SET res_status = '" + newReservation.getResStatus().toString() + "' WHERE reservation_id = " + reservationId;
			}
			
			System.out.println("\ntest\n");
			System.out.println(updateQuery);
			
			// update reservation only if updateQuery is not empty
			if (!updateQuery.equals("")) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
				Statement st = con.createStatement();
				int rowsAffected = st.executeUpdate(updateQuery);
				
				System.out.println(rowsAffected + " rows affected in the reservation table");
				
				st.close();
				con.close();
			}
			
			// if addRoomService is true, add the latest room service to database
			if (addRoomService) {
				RoomServiceDAO.addRoomService(reservationId, newReservation.getRoomServiceList().get(newReservation.getRoomServiceList().size() - 1));
			}
		}
		catch (Exception e) {
			System.out.println("Inside ReservationDAO.updateObject: " + e.getMessage());
		}
	}

//	@Override
//	public void removeObject(Entity entity) {
//		// TODO Auto-generated method stub
//		
//	}

	private int retrieveGuestId(Guest guest) {
		try {
			int id = 0;
			
			String query = "SELECT guest_id FROM guest WHERE nric = '" + guest.getNRIC() + "'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			rs.next();
			id = rs.getInt("guest_id");
			st.close();
			con.close();
			
			return id;
		}
		catch (Exception e) {
			System.out.println("Inside ReservationDAO.retrieveGuestId: " + e.getMessage());
		}
		
		return 0;
	}
	
	private int retrieveRoomId(Room room) {
		try {
			int id = 0;
			
			String query = "SELECT room_id FROM room WHERE room_level = " + room.getRoomLevel() + " and room_number = " + room.getRoomNumber();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			rs.next();
			id = rs.getInt("room_id");
			st.close();
			con.close();
			
			return id;
		}
		catch (Exception e) {
			System.out.println("Inside ReservationDAO.retrieveRoomId: " + e.getMessage());
		}
		return 0;
	}

	private int retrieveReservationId(Reservation reservation) {
		try {
			int guestId = retrieveGuestId(reservation.getGuest());
			int roomId = retrieveRoomId(reservation.getRoom());
			String query = String.format("SELECT reservation_id FROM reservation WHERE guest_id = %d AND room_id = %d AND check_in_date = '%s' AND no_of_adults = %d AND no_of_children = %d AND res_status = '%s'",
					guestId, roomId, reservation.getCheckInDate().toString(), reservation.getNoOfAdults(), reservation.getNoOfChildren(), reservation.getResStatus().toString());
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps?useTimeZone=true&serverTimezone=UTC#&dummyparam=", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			rs.next();
			int id = rs.getInt("reservation_id");
			
			st.close();
			con.close();
			return id;
		}
		catch (Exception e) {
			System.out.println("Inside ReservationDAO.retrieveReservationId: " + e.getMessage());
		}
		return 0;
	}
}
