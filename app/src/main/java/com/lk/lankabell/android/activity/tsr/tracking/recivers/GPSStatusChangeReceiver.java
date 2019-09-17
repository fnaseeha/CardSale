package com.lk.lankabell.android.activity.tsr.tracking.recivers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lk.lankabell.android.activity.tsr.tracking.ISMSCallbacks;
import com.lk.lankabell.android.activity.tsr.activity.BackgroundService;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.util.HTTPServiceCalls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class GPSStatusChangeReceiver extends BroadcastReceiver implements
        ISMSCallbacks {

	private SimpleDateFormat sdf;
	private Date date2;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		int gps_status = 0;

		final LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			gps_status = 1;
		}

		int connection = 0;
		if (HTTPServiceCalls.isInternetAvailable(context)) {
			connection = 1;
		}
		
		DatabaseHandler dbh = new DatabaseHandler(context);
		sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date2 = new Date();
		
		if (gps_status == 1) {
			dbh.SaveNetworkStatus("GPS is Enabled", sdf.format(date2));
		} else {
			dbh.SaveNetworkStatus("GPS is Disabled", sdf.format(date2));
		}
//		if (connection == 1) {
//			dbh.SaveNetworkStatus("Connected", sdf.format(date2));
//		} else {
//			dbh.SaveNetworkStatus("Disconnected", sdf.format(date2));
//		}
		
		

		if (connection == 0 || gps_status == 0) {
			Intent intentSMS = new Intent(context, SendSMSActivity.class);
			intentSMS.putExtra("gps", gps_status);
			intentSMS.putExtra("3g", connection);
			intentSMS.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentSMS);
		}else{
			context.startService(new Intent(context, BackgroundService.class));
		}

	}

	@Override
	public void onSmsSent(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSmsDelivered(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSmsSendFail(String msgid, int failType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSmsDeliveryFail(String msgid, int failType) {
		// TODO Auto-generated method stub

	}

}
