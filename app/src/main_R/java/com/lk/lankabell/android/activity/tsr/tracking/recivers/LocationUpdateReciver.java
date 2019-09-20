package com.lk.lankabell.android.activity.tsr.tracking.recivers;
 

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 

import com.lk.lankabell.android.activity.tsr.tracking.services.UpdateLocationService;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
public class LocationUpdateReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub 
		Utils.writeToLogFileWithTime("LocationUpdateReciver called");
		arg0.startService(new Intent(arg0,UpdateLocationService.class));
	}
	
	
	 
}
