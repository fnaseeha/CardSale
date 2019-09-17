package com.lk.lankabell.android.activity.tsr.sqlite;



public class PostalCode implements  Comparable<PostalCode>{

	 private String postalCode;
	 private String city;
	 private String latitude;
	 private String longitude;
	 private double distance;
	 private String region;
	 private String postOfficeName;
	 private String area;
	 
	 private static final String TAG = PostalCode.class.getSimpleName();
	 
	 
	public PostalCode(String postalCode, String city, String latitude,
			String longitude,double distance,String region,String postOfficeName,String area) {
		super();
		this.postalCode = postalCode;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
		this.region = region;
		this.postOfficeName =postOfficeName;
		this.area = area;

	}

	public String getPostalCode() {
		return postalCode;
	}
	
	public String getPostOfficeName() {
		return postOfficeName;
	}
	
	public String getArea() {
		return area;
	}
	
	public String getRegion() {
		return region;
	}
	
	public String getCity() {
		return city;
	}

	public String getLatitude() {
		if (latitude!=null){
			return latitude;
		}else{
			latitude="0.00";
			return latitude;
		}		
	}

	public String getLongitude() {
		if (longitude!=null){
			return longitude;
		}else{
			longitude="0.00";
			return longitude;
		}				
	}
	
	
	 
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public int compareTo(PostalCode comparePostalCode) {
		 
		double compareDistance = ((PostalCode) comparePostalCode).getDistance();
 
		//Log.i(TAG, String.valueOf(compareDistance));
		
		//ascending order
		//return this.distance - compareDistance;
		int ret = (int)(this.distance-compareDistance);
		
		//Log.i(TAG, this.distance+" " +String.valueOf(compareDistance)+" "+(compareDistance - this.distance)+" "+ret);
		//descending order
		return ret;
 
	}	
}
