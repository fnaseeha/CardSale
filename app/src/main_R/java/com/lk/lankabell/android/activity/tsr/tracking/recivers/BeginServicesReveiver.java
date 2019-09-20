package com.lk.lankabell.android.activity.tsr.tracking.recivers;

import com.lk.lankabell.android.activity.tsr.tracking.services.LocationTrackingService;
import com.lk.lankabell.android.activity.tsr.tracking.services.UpdateLocationService;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BeginServicesReveiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub 
		if (Utils.isTrackingOn(context)) { 
			
			context.startService(new Intent(context, LocationTrackingService.class));
			context.startService(new Intent(context, UpdateLocationService.class));
			Utils.writeToLogFileWithTime("started background services");
			
			
		}

	}

}
