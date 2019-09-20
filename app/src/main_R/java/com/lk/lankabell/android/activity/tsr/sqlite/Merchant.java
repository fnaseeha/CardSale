package com.lk.lankabell.android.activity.tsr.sqlite;

public class Merchant implements  Comparable<Merchant>{

	private Long merchantId;
	private String name;
	private String address;
	private String phone;
	private String city;
	private int postalCode;
	private String latitude;
	private String longitude;
	private double distance;
	private int isRegistered;
	private String reloadNumber;


	

	public Merchant(Long merchantId,String name, String address, String phone,int postalCode, String city,
			 String latitude, String longitude, double distance,int isRegistred,String reloadNo) {
		super();
		this.merchantId = merchantId;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.city = city;
		this.postalCode = postalCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
		this.isRegistered = isRegistred;
		this.reloadNumber = reloadNo;

	}
	
	public Long getMerchantId() {
		return merchantId;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}
	
	public String getreloadNo() {
		return reloadNumber;
	}

	public int getIsRegistered() {
		return isRegistered;
	}

	public double getDistance() {
		return distance;
	}



	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public String getCity() {
		return city;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public int compareTo(Merchant compareMerchant) {
		 
		double compareDistance = ((Merchant) compareMerchant).getDistance();
 
		//Log.i(TAG, String.valueOf(compareDistance));
		
		//ascending order
		//return this.distance - compareDistance;
		int ret = (int)(this.distance-compareDistance);
		
		//Log.i(TAG, this.distance+" " +String.valueOf(compareDistance)+" "+(compareDistance - this.distance)+" "+ret);
		//descending order
		return ret;
 
	}	
}
