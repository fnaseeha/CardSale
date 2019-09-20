package com.lk.lankabell.android.activity.tsr.sync;

public class CityRegistration {

	private String cityName;
	private int currentNo;
	private int maxLimit;
	private String postalCode;
	
	public CityRegistration(String CityName, int currentNo,int maxLimit, String PostalCode) {
		super();
		this.cityName = CityName;
		this.currentNo = currentNo;
		this.maxLimit = maxLimit;
		this.postalCode = PostalCode;
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
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
}
