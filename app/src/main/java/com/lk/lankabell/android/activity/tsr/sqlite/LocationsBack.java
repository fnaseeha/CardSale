package com.lk.lankabell.android.activity.tsr.sqlite;

public class LocationsBack {
	
	private int epfNo;
	private String Accuracy;
	private String Speed;
	private String Time;
	private String Provider;
	private String longt;
	private String latt;
	private String datetime;
	
	public LocationsBack() {
		super();
	}

/*	public LocationsBack1(int epfNo,String Accuracy, String Speed, String UseTime,String Provider, String latt, String longt, String currentdate) {
		super();
		this.epfNo = epfNo;
		this.Accuracy = Accuracy;
		this.Speed = Speed;
		this.Time = UseTime;
		this.Provider = Provider;
		this.longt = latt;
		this.latt = longt;
		this.datetime = currentdate;
	}
*/
	public LocationsBack(int epfNo,String Accuracy, String Speed, String UseTime,String Provider, Double latt, Double longt, String currentdate) {
		super();
		this.epfNo = epfNo;
		this.Accuracy = Accuracy;
		this.Speed = Speed;
		this.Time = UseTime;
		this.Provider = Provider;
		this.longt = ""+latt;
		this.latt = ""+longt;
		this.datetime = currentdate;
	}
	
	public int getEpfNo() {
		return epfNo;
	}
	public void setEpfNo(int epfNo) {
		this.epfNo = epfNo;
	}
	public String getAccuracy() {
		return Accuracy;
	}
	public void setAccuracy(String accuracy) {
		Accuracy = accuracy;
	}
	public String getSpeed() {
		return Speed;
	}
	public void setSpeed(String speed) {
		Speed = speed;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getProvider() {
		return Provider;
	}
	public void setProvider(String provider) {
		Provider = provider;
	}
	public String getLongt() {
		return longt;
	}
	public void setLongt(String longt) {
		this.longt = longt;
	}
	public String getLatt() {
		return latt;
	}
	public void setLatt(String latt) {
		this.latt = latt;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	
	
}
