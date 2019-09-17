package com.lk.lankabell.android.activity.tsr.sqlite;

import android.content.Context;

public class LocationFinder {
	
	private static final String TAG = LocationFinder.class.getSimpleName();
	private DatabaseHandler dbh = null;

	public LocationFinder(Context context) {
		dbh = new DatabaseHandler(context);
	}

	
	/*
	public ArrayList<PostalCode> findNearestPostalCode(double lat,double lon){
		
		Location locationA = new Location("point A");
		locationA.setLatitude(lat);
		locationA.setLongitude(lon);
		
	
		
		ArrayList<PostalCode> postalArray = dbh.getPostalAreas();
		ArrayList<PostalCode> newArray= new ArrayList<PostalCode>();
		double distance =0;
		
		for (Iterator<PostalCode> iterator = postalArray.iterator(); iterator.hasNext();) {
			PostalCode postalCode = (PostalCode) iterator.next();
			
			Location locationB = new Location("point B");
			locationB.setLatitude(postalCode.getLatitude());
			locationB.setLongitude(postalCode.getLongitude());
			
			//Log.i(TAG, postalCode.getCity()+" "+distance);
			distance = locationA.distanceTo(locationB);
			postalCode.setDistance(distance);
			
			newArray.add(postalCode);			
		}
		
		return newArray;
		
	}
	
public ArrayList<Merchant> findNearestMerchants(double lat,double lon){
		

	ArrayList<PostalCode> list = findNearestPostalCode(lat,lon);

	Collections.sort(list);
	
	Location locationA = new Location("point A");
	locationA.setLatitude(lat);
	locationA.setLongitude(lon);
		
	ArrayList<Merchant> mArray = dbh.getMerchantListWithPostalCode(list.get(0).getAreaCode());
	
	ArrayList<Merchant> newArray= new ArrayList<Merchant>();
	
	double distance =0;
		
		for (Iterator<Merchant> iterator = mArray.iterator(); iterator.hasNext();) {
			Merchant merchant = (Merchant) iterator.next();
			
			Location locationB = new Location("point B");
			locationB.setLatitude(merchant.getLatitude());
			locationB.setLongitude(merchant.getLongitude());
			
			
			distance = locationA.distanceTo(locationB);
			merchant.setDistance(distance);
			
			newArray.add(merchant); 			
		}
		
		
		return newArray;
		
	}
	*/
}
