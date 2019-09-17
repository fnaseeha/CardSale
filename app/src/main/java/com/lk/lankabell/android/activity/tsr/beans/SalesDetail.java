package com.lk.lankabell.android.activity.tsr.beans;

public class SalesDetail {

	private Integer DETAILS_ID;
	private Long INVOICE_ID;
	private String CARD_TYPE;
	private Integer DENOMINATION;
	private String BULK_NO;
	private Integer NO_OF_CARDS;
	private String START_SERIAL;
	private String END_SERIAL;
	private Double LINE_AMOUNT;
	private Double LINE_DISCOUNT;
	private Integer ISUPDATED;
	private Integer IS_SYNCH;
	
	public SalesDetail(Integer DETAILS_ID, Long INVOICE_ID, String CARD_TYPE, Integer DENOMINATION, String BULK_NO, Integer NO_OF_CARDS, String START_SERIAL, String END_SERIAL, Double LINE_AMOUNT, Double LINE_DISCOUNT, Integer ISUPDATED, Integer IS_SYNCH){
		this.DETAILS_ID = DETAILS_ID;
		this.INVOICE_ID = INVOICE_ID;
		this.CARD_TYPE = CARD_TYPE;
		this.DENOMINATION = DENOMINATION;
		this.BULK_NO = BULK_NO;
		this.NO_OF_CARDS = NO_OF_CARDS;
		this.START_SERIAL = START_SERIAL;
		this.END_SERIAL = END_SERIAL;
		this.LINE_AMOUNT = LINE_AMOUNT;
		this.LINE_DISCOUNT = LINE_DISCOUNT;
		this.ISUPDATED = ISUPDATED;
		this.IS_SYNCH = IS_SYNCH;
	}
	
	public Integer getDETAILS_ID() {
		return DETAILS_ID;
	}
	public void setDETAILS_ID(Integer dETAILS_ID) {
		DETAILS_ID = dETAILS_ID;
	}
	public Long getINVOICE_ID() {
		return INVOICE_ID;
	}
	public void setINVOICE_ID(Long iNVOICE_ID) {
		INVOICE_ID = iNVOICE_ID;
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
	public Integer getNO_OF_CARDS() {
		return NO_OF_CARDS;
	}
	public void setNO_OF_CARDS(Integer nO_OF_CARDS) {
		NO_OF_CARDS = nO_OF_CARDS;
	}
	public String getSTART_SERIAL() {
		return START_SERIAL;
	}
	public void setSTART_SERIAL(String sTART_SERIAL) {
		START_SERIAL = sTART_SERIAL;
	}
	public String getEND_SERIAL() {
		return END_SERIAL;
	}
	public void setEND_SERIAL(String eND_SERIAL) {
		END_SERIAL = eND_SERIAL;
	}
	public Double getLINE_AMOUNT() {
		return LINE_AMOUNT;
	}
	public void setLINE_AMOUNT(Double lINE_AMOUNT) {
		LINE_AMOUNT = lINE_AMOUNT;
	}
	public Double getLINE_DISCOUNT() {
		return LINE_DISCOUNT;
	}
	public void setLINE_DISCOUNT(Double lINE_DISCOUNT) {
		LINE_DISCOUNT = lINE_DISCOUNT;
	}
	public Integer getISUPDATED() {
		return ISUPDATED;
	}
	public void setISUPDATED(Integer iSUPDATED) {
		ISUPDATED = iSUPDATED;
	}
	public Integer getIS_SYNCH() {
		return IS_SYNCH;
	}
	public void setIS_SYNCH(Integer iS_SYNCH) {
		IS_SYNCH = iS_SYNCH;
	}
	
}
