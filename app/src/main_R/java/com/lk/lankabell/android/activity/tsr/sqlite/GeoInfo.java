package com.lk.lankabell.android.activity.tsr.sqlite;

import java.util.Date;

/**
 * @author Administrator
 * 
 * Gathering Geo Informations.
 *
 */
public class GeoInfo {

	 private int geoId;     
	 
	 private String latitude;   
	 
	 private String longitude; 
	 
	 private Date grabTime;     
	 
	 private int empId;

	 
	public GeoInfo(int geoId, String latitude, String longitude, Date grabTime,
			int empId) {
		super();
		this.geoId = geoId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.grabTime = grabTime;
		this.empId = empId;
	}

	/**
	 * @return geoID
	 * 
	 * Getting Geo ID function.
	 */
	public int getGeoId() {
		return geoId;
	}

	public void setGeoId(int geoId) {
		this.geoId = geoId;
	}

	/**
	 * @return latitude
	 * 
	 * Getting latitude function.
	 * 
	 */
	public String getLatitude() {
		return latitude;
	}

	
	
	/**
	 * @param latitude
	 * 
	 * Setting latitude function.
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return longitude
	 * Getting longitude function.
	 * 
	 */
	public String getLongitude() {
		return longitude;
	}

	
	
	
	/**
	 * @param longitude
	 * Setting longitude function.
	 * 
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	
	
	/**
	 * @return grabTime
	 * 
	 * Getting Time function.
	 */
	public Date getGrabTime() {
		return grabTime;
	}

	/**
	 * @param grabTime
	 * 
	 * Setting Time function.
	 */
	public void setGrabTime(Date grabTime) {
		this.grabTime = grabTime;
	}

	/**
	 * @return empId
	 * 
	 * Get Emp ID function.
	 */
	public int getEmpId() {
		return empId;
	}

	/**
	 * @param empId
	 * 
	 * Set Emp ID function
	 */
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	 
	 
	 

}
