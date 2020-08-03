package com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity;

import java.io.Serializable;

public class CreditCard implements Serializable{

	private static final long serialVersionUID = 1L;
	public static enum CardType {
		MASTER, VISA;
	}
	private long cardNo; 
	private int cvv;
	private CardType cardType;
	private String name, address, country, exp;
	
	public CreditCard() {};
	
	public CreditCard(String name, String address, String country, String exp, long cardNo, int cvv, CardType cardType) {
		setName(name);
		setAddress(address);
		setCountry(country);
		setExp(exp);
		setCardNo(cardNo);
		setCvv(cvv);
		setCardType(cardType);
	}
	public long getCardNo() {
		return cardNo;
	}
	public void setCardNo(long cardNo) {
		this.cardNo = cardNo;
	}
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}
	public CardType getCardType() {
		return cardType;
	}
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
}
