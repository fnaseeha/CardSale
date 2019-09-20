package com.lk.lankabell.android.activity.tsr.beans;

public class CardReturnsConfirmData {

	
	private String CARD_TYPE;
	private String DENOMINATION;
	private Integer BULK_NO;
	private Integer START_SERIAL;
	private Integer END_SERIAL;
	private Integer CARD_RETURNS;
	
	public CardReturnsConfirmData(String CARD_TYPE1,String DENOMINATION1,Integer BULK_NO1,Integer START_SERIAL1,Integer END_SERIAL1,Integer CARD_RETURNS1){
		this.CARD_TYPE = CARD_TYPE1;
		this.DENOMINATION = DENOMINATION1;
		this.BULK_NO = BULK_NO1;
		this.START_SERIAL = START_SERIAL1;
		this.END_SERIAL = END_SERIAL1;
		this.CARD_RETURNS = CARD_RETURNS1;
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

	public Integer getCARD_RETURNS() {
		return CARD_RETURNS;
	}

	public void setCARD_RETURNS(Integer cARD_RETURNS) {
		CARD_RETURNS = cARD_RETURNS;
	}
	
}
