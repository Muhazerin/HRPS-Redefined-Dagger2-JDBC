package com.muhazerin.HRPS_Redefined_Dagger_JDBC.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.inject.Inject;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Entity;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Guest;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces.DataAccess;
import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.CreditCard;

public class GuestDAO implements DataAccess{

	@Inject
	public GuestDAO() {}
	
	@Override
	public ArrayList<Entity> retrieveAllObjects() {
		try {
			ArrayList<Entity> tempList = new ArrayList<Entity>();
			Guest guest = null;
			String query = "select * from guest";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()) {
				if (rs.getString("card_type").equalsIgnoreCase("master")) {
					guest = new Guest(rs.getString("nric"), rs.getString("name"), rs.getString("gender"),
							rs.getString("nationality"), rs.getString("address"), rs.getString("country"),
							rs.getString("credit_card_name"), rs.getString("credit_card_address"),
							rs.getString("credit_card_country"), rs.getString("exp"), 
							rs.getLong("card_no"), rs.getInt("cvv"), CreditCard.CardType.MASTER);
				}
				else {
					guest = new Guest(rs.getString("nric"), rs.getString("name"), rs.getString("gender"),
							rs.getString("nationality"), rs.getString("address"), rs.getString("country"),
							rs.getString("credit_card_name"), rs.getString("credit_card_address"),
							rs.getString("credit_card_country"), rs.getString("exp"), 
							rs.getLong("card_no"), rs.getInt("cvv"), CreditCard.CardType.VISA);
				}
				
				tempList.add(guest);
			}
			st.close();
			con.close();
			
			return tempList;
		}
		catch (Exception ex) {
			System.out.println("Inside GuestDAO.retrieveAllObjects: " + ex.getMessage());
		}
		return null;
	}

	@Override
	public void insertObject(Entity entity) {
		try {
			Guest guest;
			if (entity instanceof Guest) {
				guest = (Guest)entity;
			}
			else {
				System.out.println("Object is not of type guest");
				return;
			}
			String query = String.format("insert into guest (nric, name, gender, nationality, address, country, card_type, card_no, cvv, credit_card_name, credit_card_address, credit_card_country, exp) values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', %d, %d, '%s', '%s', '%s', '%s')", 
					guest.getNRIC(), guest.getName(), guest.getGender(), guest.getNationality(), guest.getAddress(), guest.getCountry(), guest.getCreditCard().getCardType().toString(), guest.getCreditCard().getCardNo(), guest.getCreditCard().getCvv(), guest.getCreditCard().getName(), guest.getCreditCard().getAddress(), guest.getCreditCard().getCountry(), guest.getCreditCard().getExp());
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			int rowsAffected = st.executeUpdate(query);
			
			System.out.println(rowsAffected + " rows affected in guest table");
			
			st.close();
			con.close();
		}
		catch (Exception ex) {
			System.out.println("Inside GuestDAO.insertObject: " + ex.getMessage());
		}
	}

	@Override
	public void updateObject(Entity oldEntity, Entity newEntity) {
		try {
			Guest oldGuest, newGuest;
			if (oldEntity instanceof Guest) {
				oldGuest = (Guest) oldEntity;
			}
			else {
				System.out.println("Object is not of type guest");
				return;
			}
			
			if (newEntity instanceof Guest) {
				newGuest = (Guest) newEntity;
			}
			else {
				System.out.println("Object is not of type guest");
				return;
			}
			
			String query = String.format("update guest set nric = '%s', name = '%s', gender = '%s', nationality = '%s', address ='%s', country = '%s', card_type = '%s', card_no = %d, cvv = %d, credit_card_name = '%s', credit_card_address = '%s', credit_card_country = '%s', exp = '%s'" + 
			" where nric = '%s' and name = '%s' and gender = '%s' and nationality = '%s' and address = '%s' and country = '%s' and card_type = '%s' and card_no = %d and cvv = %d and credit_card_name = '%s' and credit_card_address = '%s' and credit_card_country = '%s' and exp = '%s'", 
					newGuest.getNRIC(), newGuest.getName(), newGuest.getGender(), newGuest.getNationality(), newGuest.getAddress(), newGuest.getCountry(), newGuest.getCreditCard().getCardType().toString(), newGuest.getCreditCard().getCardNo(), newGuest.getCreditCard().getCvv(), newGuest.getCreditCard().getName(), newGuest.getCreditCard().getAddress(), newGuest.getCreditCard().getCountry(), newGuest.getCreditCard().getExp(), 
					oldGuest.getNRIC(), oldGuest.getName(), oldGuest.getGender(), oldGuest.getNationality(), oldGuest.getAddress(), oldGuest.getCountry(), oldGuest.getCreditCard().getCardType().toString(), oldGuest.getCreditCard().getCardNo(), oldGuest.getCreditCard().getCvv(), oldGuest.getCreditCard().getName(), oldGuest.getCreditCard().getAddress(), oldGuest.getCreditCard().getCountry(), oldGuest.getCreditCard().getExp());
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps", "muhazerin", "CDW=$V6*BJ-M]gB");
			Statement st = con.createStatement();
			int rowsAffected = st.executeUpdate(query);
			
			System.out.println(rowsAffected + " rows affected in the guest table");
			
			st.close();
			con.close();
		}
		catch (Exception ex) {
			System.out.println("Inside GuestDAO.updateObject: " + ex.getMessage());
		}
	}

//	@Override
//	public void removeObject(Entity entity) {
//		try {
//			Guest guest;
//			if (entity instanceof Guest) {
//				guest = (Guest) entity;
//			}
//			else {
//				System.out.println("Object is not of type guest");
//				return;
//			}
//			
//			// need to remove from tables that uses guest as foreign key first
//			// Graph on removing guest;
//			// room_service_menu_item --> room_service --> reservation --> guest
//			String selectGuest = String.format("select guest_id from guest where nric = %s and name = %s and gender = %s and nationality = %s and address = %s and country = %s and card_type = %s and card_no = %d and cvv = %d and credit_card_name = %s and credit_card_address = %s and credit_card_country = %s and exp = %s", 
//					guest.getNRIC(), guest.getName(), guest.getGender(), guest.getNationality(), guest.getAddress(), guest.getCountry(), guest.getCreditCard().getCardType().toString(), guest.getCreditCard().getCardNo(), guest.getCreditCard().getCvv(), guest.getCreditCard().getName(), guest.getCreditCard().getAddress(), guest.getCreditCard().getCountry(), guest.getCreditCard().getExp());
//			
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hrps", "muhazerin", "CDW=$V6*BJ-M]gB");
//			// need the argument to move the rs forward and back
//			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
//					   ResultSet.CONCUR_READ_ONLY);
//			ResultSet guestRS = st.executeQuery(selectGuest);
//			
//			// if guest is found
//			if (guestRS.next()) { // this code will move the pointer forward and check if value is valid.
//				int guestId = guestRS.getInt("guest_id");
//				
//				RemoveReservation.removeReservation(guestId, "guest");
//				
//				String removeGuest = "remove from guest where guest_id = " + guestId;
//				int guestRowsAffected = st.executeUpdate(removeGuest);
//				System.out.println(guestRowsAffected + "rows affected in the guest table when removing guest_id = " + guestId);
//			}
//			else {
//				// guest is not found in database
//				System.out.println("Guest not found");
//			}
//			
//			st.close();
//			con.close();
//		}
//		catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
//	}

	public static Guest assignGuest(Guest guest, ResultSet rs) {
		try {
			if (rs.getString("card_type").equalsIgnoreCase("master")) {
				guest = new Guest(rs.getString("nric"), rs.getString("name"), rs.getString("gender"),
						rs.getString("nationality"), rs.getString("address"), rs.getString("country"),
						rs.getString("credit_card_name"), rs.getString("credit_card_address"),
						rs.getString("credit_card_country"), rs.getString("exp"), 
						rs.getLong("card_no"), rs.getInt("cvv"), CreditCard.CardType.MASTER);
			}
			else {
				guest = new Guest(rs.getString("nric"), rs.getString("name"), rs.getString("gender"),
						rs.getString("nationality"), rs.getString("address"), rs.getString("country"),
						rs.getString("credit_card_name"), rs.getString("credit_card_address"),
						rs.getString("credit_card_country"), rs.getString("exp"), 
						rs.getLong("card_no"), rs.getInt("cvv"), CreditCard.CardType.VISA);
			}
		}
		catch (Exception e) {
			System.out.println("Inside GuestDAO.assignGuest: " + e.getMessage());
		}
		return guest;
	}
	
}