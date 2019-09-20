package com.lk.lankabell.android.activity.tsr.tracking.recivers;


import com.lk.lankabell.android.activity.tsr.tracking.services.LocationTrackingService;
import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationStarterReciver extends BroadcastReceiver {

//	private Context context;
//	private TrackerLocationManager mTracker;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub

		if (DateTimeFormatings.isInWorkingHours()) {
			arg0.startService(new Intent(arg0, LocationTrackingService.class));
		} else {
			 
		}
//
//		this.context = arg0; 

	}

//	@Override
//	public void setOnTrackerLocationChanged(Location location) {
//		// TODO Auto-generated method stub
//		// DBhandler dbHelper = new DBhandler(context);
//		// boolean b = dbHelper.addLocation("","" + location.getLatitude(),"" +
//		// location.getLongitude(), "" +
//		// location.getAccuracy(),location.getTime());
//	}

}
