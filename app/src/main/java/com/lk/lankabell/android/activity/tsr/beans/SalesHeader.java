package com.lk.lankabell.android.activity.tsr.beans;

public class SalesHeader {

	private Integer EPF_ID;
	private String CARD_TYPE;
	private Integer DENOMINATION;
	private Integer IS_ALL_SOLD;
	private String BULK_NO;
	private String START_SERIAL;
	private String NEXT_SERIAL_VALUE;
	private String END_SERIAL;
	private Integer IS_SYNCH;
		
	public SalesHeader(Integer EPF_ID,String CARD_TYPE,Integer DENOMINATION,Integer IS_ALL_SOLD,String BULK_NO,String START_SERIAL,
			String NEXT_SERIAL_VALUE,String END_SERIAL,Integer IS_SYNCH){
		this.EPF_ID = EPF_ID;
		this.CARD_TYPE = CARD_TYPE;
		this.DENOMINATION = DENOMINATION;
		this.IS_ALL_SOLD = IS_ALL_SOLD;
		this.BULK_NO = BULK_NO;
		this.START_SERIAL = START_SERIAL;
		this.NEXT_SERIAL_VALUE = NEXT_SERIAL_VALUE;
		this.END_SERIAL = END_SERIAL;
		this.IS_SYNCH = IS_SYNCH;
	}

	public Integer getEPF_ID() {
		return EPF_ID;
	}

	public void setEPF_ID(Integer ePF_ID) {
		EPF_ID = ePF_ID;
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

	public Integer getIS_ALL_SOLD() {
		return IS_ALL_SOLD;
	}

	public void setIS_ALL_SOLD(Integer iS_ALL_SOLD) {
		IS_ALL_SOLD = iS_ALL_SOLD;
	}

	public String getBULK_NO() {
		return BULK_NO;
	}

	public void setBULK_NO(String bULK_NO) {
		BULK_NO = bULK_NO;
	}

	public String getSTART_SERIAL() {
		return START_SERIAL;
	}

	public void setSTART_SERIAL(String sTART_SERIAL) {
		START_SERIAL = sTART_SERIAL;
	}

	public String getNEXT_SERIAL_VALUE() {
		return NEXT_SERIAL_VALUE;
	}

	public void setNEXT_SERIAL_VALUE(String nEXT_SERIAL_VALUE) {
		NEXT_SERIAL_VALUE = nEXT_SERIAL_VALUE;
	}

	public String getEND_SERIAL() {
		return END_SERIAL;
	}

	public void setEND_SERIAL(String eND_SERIAL) {
		END_SERIAL = eND_SERIAL;
	}

	public Integer getIS_SYNCH() {
		return IS_SYNCH;
	}

	public void setIS_SYNCH(Integer iS_SYNCH) {
		IS_SYNCH = iS_SYNCH;
	}
	
	
	
}
