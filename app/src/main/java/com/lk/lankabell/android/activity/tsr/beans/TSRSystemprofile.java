package com.lk.lankabell.android.activity.tsr.beans;

public class TSRSystemprofile {

	private Integer NEXT_MERCHANT_NO;
	private Integer NEXT_INVOICE_NO;
	private Integer MAX_RECORDS;
	private String Task;
	private String SYSTEM_URL;
	private Integer SYSTEM_AUTO_SYNCH;
	private Integer GPS_GETTING;
	private Integer GPS_AUTO_SYNCH;
	private String LastSysnchDoneDate;
	
	public TSRSystemprofile(Integer nEXT_MERCHANT_NO2, Integer nEXT_INVOICE_NO2, String task2, Integer MAX_RECORDS, String SYSTEM_URL2, Integer SYSTEM_AUTO_SYNCH2, Integer GPS_AUTO_SYNCH2, Integer GPS_GETTING2, String alastSysnchDoneDate) {
		setNEXT_MERCHANT_NO(nEXT_MERCHANT_NO2);
		setNEXT_INVOICE_NO(nEXT_INVOICE_NO2);
		setTask(task2);
		setMAX_RECORDS(MAX_RECORDS);
		setSYSTEM_URL(SYSTEM_URL2);
		setSYSTEM_AUTO_SYNCH(SYSTEM_AUTO_SYNCH2);
		setGPS_AUTO_SYNCH(GPS_AUTO_SYNCH2);
		setGPS_GETTING(GPS_GETTING2);
		setLastSysnchDoneDate(alastSysnchDoneDate);
	}
	
	public Integer getNEXT_MERCHANT_NO() {
		return NEXT_MERCHANT_NO;
	}
	public void setNEXT_MERCHANT_NO(Integer nEXT_MERCHANT_NO) {
		NEXT_MERCHANT_NO = nEXT_MERCHANT_NO;
	}
	public Integer getNEXT_INVOICE_NO() {
		return NEXT_INVOICE_NO;
	}
	public void setNEXT_INVOICE_NO(Integer nEXT_INVOICE_NO) {
		NEXT_INVOICE_NO = nEXT_INVOICE_NO;
	}
	public String getTask() {
		return Task;
	}
	public void setTask(String task) {
		Task = task;
	}
	public Integer getMAX_RECORDS() {
		return MAX_RECORDS;
	}
	public void setMAX_RECORDS(Integer mAX_RECORDS) {
		MAX_RECORDS = mAX_RECORDS;
	}

	public String getSYSTEM_URL() {
		return SYSTEM_URL;
	}

	public void setSYSTEM_URL(String sYSTEM_URL) {
		SYSTEM_URL = sYSTEM_URL;
	}

	public Integer getSYSTEM_AUTO_SYNCH() {
		return SYSTEM_AUTO_SYNCH;
	}

	public void setSYSTEM_AUTO_SYNCH(Integer sYSTEM_AUTO_SYNCH) {
		SYSTEM_AUTO_SYNCH = sYSTEM_AUTO_SYNCH;
	}

	public Integer getGPS_GETTING() {
		return GPS_GETTING;
	}

	public void setGPS_GETTING(Integer gPS_GETTING) {
		GPS_GETTING = gPS_GETTING;
	}

	public Integer getGPS_AUTO_SYNCH() {
		return GPS_AUTO_SYNCH;
	}

	public void setGPS_AUTO_SYNCH(Integer gPS_AUTO_SYNCH) {
		GPS_AUTO_SYNCH = gPS_AUTO_SYNCH;
	}

	public String getLastSysnchDoneDate() {
		return LastSysnchDoneDate;
	}

	public void setLastSysnchDoneDate(String lastSysnchDoneDate) {
		LastSysnchDoneDate = lastSysnchDoneDate;
	}
	
}
