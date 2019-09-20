package com.lk.lankabell.android.activity.tsr.beans;

public class NextSerialSync {
	
	private String BULK_NO;
	private String START_SERIAL;
	private String END_SERIAL;
	private int Responce;
	
	public int getResponce() {
		return Responce;
	}
	public void setResponce(int responce) {
		Responce = responce;
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
	public String getEND_SERIAL() {
		return END_SERIAL;
	}
	public void setEND_SERIAL(String eND_SERIAL) {
		END_SERIAL = eND_SERIAL;
	}
	
}
