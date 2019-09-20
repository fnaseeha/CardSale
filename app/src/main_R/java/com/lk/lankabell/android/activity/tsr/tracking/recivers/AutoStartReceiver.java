package com.lk.lankabell.android.activity.tsr.tracking.recivers;

import com.lk.lankabell.android.activity.tsr.activity.BackgroundService;
import com.lk.lankabell.android.activity.tsr.tracking.services.LocationTrackingService;
import com.lk.lankabell.android.activity.tsr.tracking.services.UpdateLocationService;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStartReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		if (Utils.isTrackingOn(context)) {
			try {
				  
				context.startService(new Intent(context, LocationTrackingService.class));
				context.startService(new Intent(context, UpdateLocationService.class));
				Utils.writeToLogFileWithTime("started background services on boot complete");

				Intent intent = new Intent(context, BackgroundService.class);
				context.startService(intent);
				Log.i("Autostart", "started...............................");

				// context.startActivity(new Intent(context, LoginPage.class));

			} catch (Exception e) {
				Utils.writeToErrLogFileWithTime(e);
			}

		}
	}

}
