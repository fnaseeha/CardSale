package com.lk.lankabell.android.activity.tsr.tracking.domain;

import java.util.Date;

import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;

import android.database.Cursor;


public class Locations {
	
	private int epfNo;
	private Double longt;
	private Double latt;
	private Double Accuracy;
	private String datetime;
	private String Speed;
	private String UseTime;
	public Locations() {
		super();
	}

	public Locations(int id, Double longt, Double latt) {
		super();
		this.longt = longt;
		this.latt = latt;
	}

	public Locations(int epfNo, Double longt, Double latt, String gpsdatetime) {
		super();
		this.epfNo = epfNo;
		this.longt = longt;
		this.latt = latt;
		this.datetime = gpsdatetime;
	}


	public int getEpfNo() {
		return epfNo;
	}

	public void setEpfNo(int epfNo) {
		this.epfNo = epfNo;
	}

	public Double getLongt() {
		return longt;
	}

	public void setLongt(Double longt) {
		this.longt = longt;
	}

	public Double getLatt() {
		return latt;
	}

	public void setLatt(Double latt) {
		this.latt = latt;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	
	
	 

	public Double getAccuracy() {
		return Accuracy;
	}

	public void setAccuracy(Double accuracy) {
		Accuracy = accuracy;
	}

	public String getSpeed() {
		return Speed;
	}

	public void setSpeed(String speed) {
		Speed = speed;
	}

	public String getUseTime() {
		return UseTime;
	}

	public void setUseTime(String useTime) {
		UseTime = useTime;
	}

	public static Locations getInstance(Cursor cursor) {
		int index = 1;
		
		try {
			Locations loc = new Locations();
			
			
			loc.setEpfNo(Integer.parseInt(cursor.getString(index++)));
			//loc.setEpfNo(2254);
//		loc.setLatt(Double.parseDouble(cursor.getString(index++)));
//		loc.setLongt(Double.parseDouble(cursor.getString(index++)));
			loc.setLongt(Double.parseDouble(cursor.getString(index++)));
			loc.setLatt(Double.parseDouble(cursor.getString(index++)));
			loc.setAccuracy(Double.parseDouble(cursor.getString(index++)));
			loc.setDatetime(DateTimeFormatings.getDateTimeSec(new Date(cursor.getLong(index++))));
			loc.setSpeed("0");
			loc.setUseTime(loc.getDatetime());
			return loc;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	public String toString() {
		return "Locations [epfNo=" + epfNo + ", longt=" + longt + ", latt="
				+ latt + ", Accuracy=" + Accuracy + ", datetime=" + datetime
				+ ", Speed=" + Speed + ", UseTime=" + UseTime + "]";
	}
	
	
	
}

