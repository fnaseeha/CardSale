package com.lk.lankabell.android.activity.tsr.sqlite;

public class CityRegistrationLimit {
	private String epfNo;
	private String cityName;
	private int currentNo;
	private int maxLimit;
	private String LATITUDE;
	private String LONGITUDE;
	private String postalCode;


	public CityRegistrationLimit(String postalCode, int currentNo,int maxLimit,String latitude,String longitude) {
		super();
		this.postalCode = postalCode;
		this.currentNo = currentNo;
		this.maxLimit = maxLimit;
		this.LATITUDE = latitude;
		this.LONGITUDE = longitude;
	}
	
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getEpfNo() {
		return epfNo;
	}

	public void setEpfNo(String epfNo) {
		this.epfNo = epfNo;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getCurrentNo() {
		return currentNo;
	}

	public void setCurrentNo(int currentNo) {
		this.currentNo = currentNo;
	}

	public int getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(int maxLimit) {
		this.maxLimit = maxLimit;
	}

	public String getLATITUDE() {
		return LATITUDE;
	}

	public void setLATITUDE(String lATITUDE) {
		LATITUDE = lATITUDE;
	}

	public String getLONGITUDE() {
		return LONGITUDE;
	}

	public void setLONGITUDE(String lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}


}
