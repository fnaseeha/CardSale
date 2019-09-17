package com.lk.lankabell.android.activity.tsr.beans;

public class CardReturnsSearchData {

	private String CARD_TYPE;
	private String DENOMINATION;
	private Integer BULK_NO;
	private Integer START_SERIAL;
	private Integer END_SERIAL;
	//private Integer CARD_RETURNS;
	private Integer Number_of_cards;
	private String Reject_Type;
	
	
	
	//public CardReturnsSearchData(String cARD_TYPE, String dENOMINATION, Integer bULK_NO, Integer sTART_SERIAL, Integer eND_SERIAL, Integer number_of_cards, String reject_Type) {
	//	CARD_TYPE = cARD_TYPE;
	//	DENOMINATION = dENOMINATION;
	//	BULK_NO = bULK_NO;
	//	START_SERIAL = sTART_SERIAL;
	//	END_SERIAL = eND_SERIAL;
	//	//CARD_RETURNS = cARD_RETURNS;
	//	Number_of_cards = number_of_cards;
	//	Reject_Type = reject_Type;
	//}
	
	public CardReturnsSearchData(String cARD_TYPE, String dENOMINATION, Integer bULK_NO, Integer sTART_SERIAL, Integer eND_SERIAL, Integer cARD_RETURNS, Integer number_of_cards, String reject_Type) {
		CARD_TYPE = cARD_TYPE;
		DENOMINATION = dENOMINATION;
		BULK_NO = bULK_NO;
		START_SERIAL = sTART_SERIAL;
		END_SERIAL = eND_SERIAL;
		//CARD_RETURNS = cARD_RETURNS;
		Number_of_cards = number_of_cards;
		Reject_Type = reject_Type;
	}
	
	
	public String getCARD_TYPE() {
		return CARD_TYPE;
	}
	public void setCARD_TYPE(String cARD_TYPE) {
		CARD_TYPE = cARD_TYPE;
	}
	public String getDENOMINATION() {
		return DENOMINATION;
	}
	public void setDENOMINATION(String dENOMINATION) {
		DENOMINATION = dENOMINATION;
	}
	public Integer getBULK_NO() {
		return BULK_NO;
	}
	public void setBULK_NO(Integer bULK_NO) {
		BULK_NO = bULK_NO;
	}
	public Integer getSTART_SERIAL() {
		return START_SERIAL;
	}
	public void setSTART_SERIAL(Integer sTART_SERIAL) {
		START_SERIAL = sTART_SERIAL;
	}
	public Integer getEND_SERIAL() {
		return END_SERIAL;
	}
	public void setEND_SERIAL(Integer eND_SERIAL) {
		END_SERIAL = eND_SERIAL;
	}
	//public Integer getCARD_RETURNS() {
	//	return CARD_RETURNS;
	//}
	//public void setCARD_RETURNS(Integer cARD_RETURNS) {
	//	CARD_RETURNS = cARD_RETURNS;
	//}
	public Integer getNumber_of_cards() {
		return Number_of_cards;
	}
	public void setNumber_of_cards(Integer number_of_cards) {
		Number_of_cards = number_of_cards;
	}
	public String getReject_Type() {
		return Reject_Type;
	}
	public void setReject_Type(String reject_Type) {
		Reject_Type = reject_Type;
	}
	
	
	
}
