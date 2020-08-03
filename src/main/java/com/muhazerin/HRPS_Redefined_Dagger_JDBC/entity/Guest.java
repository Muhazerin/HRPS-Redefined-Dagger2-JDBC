package com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity;

import java.io.Serializable;

/**
 * 
 * @author Asyraaf
 * @author https://github.com/masyraaf
 *
 */

public class Guest extends Entity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String nric;
	private String name;
	private String gender;
	private String nationality;
	private String address;
	private String country;
	private CreditCard creditCard;
	
	public Guest(String nric, String name, String gender, String nationality, String address, String country, String cName, String cAddress, String cCountry, String cExp, long cCardNo, int cCvv, CreditCard.CardType cCardType) {
		this.nric = nric;
		this.name = name;
		this.gender = gender;
		this.nationality = nationality;
		this.address = address;
		this.country = country;
		this.creditCard = new CreditCard(cName, cAddress, cCountry, cExp, cCardNo, cCvv, cCardType);
	}
	public String getNRIC() {
		return nric;
	}
	public void setNRIC(String nric) {
		this.nric = nric;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
}
