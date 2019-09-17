package com.lk.lankabell.android.activity.tsr.beans;

public class Cashire_Card_Balance {

	private String Denomination_Type;
	private Integer CARD_IN_HAND;
	private Double CARD_IN_HAND_AMOUNT;
	private Integer CARD_SALES;
	private Double CARD_SALES_AMOUNT;
	
	public Cashire_Card_Balance(String Denomination,Integer IN_HAND, Double IN_HAND_AMOUNT,Integer SALES,Double SALES_AMOUNT) {
		this.Denomination_Type = Denomination;
		this.CARD_IN_HAND = IN_HAND;
		this.CARD_IN_HAND_AMOUNT = IN_HAND_AMOUNT;
		this.CARD_SALES = SALES;
		this.CARD_SALES_AMOUNT = SALES_AMOUNT;
	}
	
	public String getDenomination_Type() {
		return Denomination_Type;
	}
	public void setDenomination_Type(String denomination_Type) {
		Denomination_Type = denomination_Type;
	}
	public Integer getCARD_IN_HAND() {
		return CARD_IN_HAND;
	}
	public void setCARD_IN_HAND(Integer cARD_IN_HAND) {
		CARD_IN_HAND = cARD_IN_HAND;
	}
	public Double getCARD_IN_HAND_AMOUNT() {
		return CARD_IN_HAND_AMOUNT;
	}
	public void setCARD_IN_HAND_AMOUNT(Double cARD_IN_HAND_AMOUNT) {
		CARD_IN_HAND_AMOUNT = cARD_IN_HAND_AMOUNT;
	}
	public Integer getCARD_SALES() {
		return CARD_SALES;
	}
	public void setCARD_SALES(Integer cARD_SALES) {
		CARD_SALES = cARD_SALES;
	}
	public Double getCARD_SALES_AMOUNT() {
		return CARD_SALES_AMOUNT;
	}
	public void setCARD_SALES_AMOUNT(Double cARD_SALES_AMOUNT) {
		CARD_SALES_AMOUNT = cARD_SALES_AMOUNT;
	}
	
}
