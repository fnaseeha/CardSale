package com.lk.lankabell.android.activity.tsr.tracking.domain;

import java.util.Date;

import android.database.Cursor;

public class UserLocation {

	long id ;
	String epf;
	Double lat;
	Double lon;
	private Double accuracy;
	Date systemDate;
	int isSynced;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEpf() {
		return epf;
	}
	public void setEpf(String epf) {
		this.epf = epf;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Date getSystemDate() {
		return systemDate;
	}
	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}
	
	
	public Double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}
	public int getIsSynced() {
		return isSynced;
	}
	public void setIsSynced(int isSynced) {
		this.isSynced = isSynced;
	}
	public static UserLocation getInstance(Cursor cursor) {
		int index = 0;
		
		UserLocation loc = new UserLocation();
		loc.setId(cursor.getLong(index++));
		loc.setEpf(cursor.getString(index++));
		loc.setLat(Double.parseDouble(cursor.getString(index++)));
		loc.setLon(Double.parseDouble(cursor.getString(index++)));
		loc.setAccuracy(Double.parseDouble(cursor.getString(index++)));
		loc.setSystemDate(new Date(cursor.getLong(index++)));
		loc.setIsSynced(cursor.getInt(index++));
		return loc;
		
		
	}
	
	
	
}
