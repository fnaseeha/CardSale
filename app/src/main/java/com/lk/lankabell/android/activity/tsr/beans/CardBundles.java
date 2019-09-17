package com.lk.lankabell.android.activity.tsr.beans;

public class CardBundles {

	private String CARD_TYPE;
	private Integer DENOMINATION;
	private String BULK_NO;
	private Integer START_SERIAL;
	private Integer END_SERIAL;
	private Integer IS_ACCEPTED;
	private Integer IS_ACCEPT_UPDATE;
	private Integer IS_ALL_SOLD;
	private String ENTRY_DATE;
	private String DISCOUNT_RATE;
	private Integer CARD_RESPONSE;
	private Integer IS_SYNCH;
	private Integer NO_OF_CARDS;
	
	public CardBundles(String CARD_TYPE,Integer DENOMINATION,String BULK_NO,Integer START_SERIAL,Integer END_SERIAL,Integer IS_ACCEPTED,Integer IS_ACCEPT_UPDATE,Integer IS_ALL_SOLD,String ENTRY_DATE,String DISCOUNT_RATE,Integer CARD_RESPONSE,Integer IS_SYNCH,Integer NO_OF_CARDS){
		this.CARD_TYPE = CARD_TYPE;
		this.DENOMINATION = DENOMINATION;
		this.BULK_NO = BULK_NO;
		this.START_SERIAL = START_SERIAL;
		this.END_SERIAL = END_SERIAL;
		this.IS_ACCEPTED = IS_ACCEPTED;
		this.IS_ACCEPT_UPDATE = IS_ACCEPT_UPDATE;
		this.IS_ALL_SOLD = IS_ALL_SOLD;
		this.ENTRY_DATE = ENTRY_DATE;
		this.DISCOUNT_RATE = DISCOUNT_RATE;
		this.CARD_RESPONSE = CARD_RESPONSE;
		this.IS_SYNCH = IS_SYNCH;
		this.NO_OF_CARDS = NO_OF_CARDS;
	}
	
	public String getCARD_TYPE() {
		return CARD_TYPE;
	}
	public void setCARD_TYPE(String cARD_TYPE) {
		CARD_TYPE = cARD_TYPE;
	}
	public Integer getDENOMINATION() {
		return DENOMINATION;
	}
	public void setDENOMINATION(Integer dENOMINATION) {
		DENOMINATION = dENOMINATION;
	}
	public String getBULK_NO() {
		return BULK_NO;
	}
	public void setBULK_NO(String bULK_NO) {
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
	public Integer getIS_ACCEPTED() {
		return IS_ACCEPTED;
	}
	public void setIS_ACCEPTED(Integer iS_ACCEPTED) {
		IS_ACCEPTED = iS_ACCEPTED;
	}
	public Integer getIS_ACCEPT_UPDATE() {
		return IS_ACCEPT_UPDATE;
	}
	public void setIS_ACCEPT_UPDATE(Integer iS_ACCEPT_UPDATE) {
		IS_ACCEPT_UPDATE = iS_ACCEPT_UPDATE;
	}
	public Integer getIS_ALL_SOLD() {
		return IS_ALL_SOLD;
	}
	public void setIS_ALL_SOLD(Integer iS_ALL_SOLD) {
		IS_ALL_SOLD = iS_ALL_SOLD;
	}
	public String getENTRY_DATE() {
		return ENTRY_DATE;
	}
	public void setENTRY_DATE(String eNTRY_DATE) {
		ENTRY_DATE = eNTRY_DATE;
	}
	public String getDISCOUNT_RATE() {
		return DISCOUNT_RATE;
	}
	public void setDISCOUNT_RATE(String dISCOUNT_RATE) {
		DISCOUNT_RATE = dISCOUNT_RATE;
	}
	public Integer getCARD_RESPONSE() {
		return CARD_RESPONSE;
	}
	public void setCARD_RESPONSE(Integer cARD_RESPONSE) {
		CARD_RESPONSE = cARD_RESPONSE;
	}
	public Integer getIS_SYNCH() {
		return IS_SYNCH;
	}
	public void setIS_SYNCH(Integer iS_SYNCH) {
		IS_SYNCH = iS_SYNCH;
	}
	public Integer getNO_OF_CARDS() {
		return NO_OF_CARDS;
	}
	public void setNO_OF_CARDS(Integer nO_OF_CARDS) {
		NO_OF_CARDS = nO_OF_CARDS;
	}
	
}
