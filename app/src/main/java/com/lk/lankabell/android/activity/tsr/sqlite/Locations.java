package com.lk.lankabell.android.activity.tsr.sqlite;



public class Locations {
	
	private int epfNo;
	private String longt;
	private String latt;
	private String datetime;
	private String Accuracy;
	private String Speed;
	private String UseTime;

	public Locations(int epfNo, String longt, String latt, String datetime, String accuracy, String speed, String useTime) {
		super();
		this.epfNo = epfNo;
		this.longt = longt;
		this.latt = latt;
		this.datetime = datetime;
		this.Accuracy = accuracy;
		this.Speed = speed;
		this.UseTime = useTime;
	}


	public int getEpfNo() {
		return epfNo;
	}

	public void setEpfNo(int epfNo) {
		this.epfNo = epfNo;
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

	public String getUseTime() {
		return UseTime;
	}

	public void setUseTime(String useTime) {
		UseTime = useTime;
	}

}
