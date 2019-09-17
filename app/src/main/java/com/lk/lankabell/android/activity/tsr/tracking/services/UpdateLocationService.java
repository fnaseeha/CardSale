package com.lk.lankabell.android.activity.tsr.tracking.services;

import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.lk.lankabell.android.activity.tsr.activity.TSRApplication;
import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;

public class UpdateLocationService extends Service {

	private static long last_location_upate_at = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			if (Utils.isTrackingOn(this)) {
				startService(new Intent(this, UpdateLocationService.class)); 
			}else{
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		Log.v("UpdateLocationService", "onStartCommand");

		try {

			long time_diff = System.currentTimeMillis() - last_location_upate_at;

			if (time_diff > (CONSTANTS.TWO_MINITUES)) {
				// try {
				// Thread.sleep(1000 * 60 *2);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				last_location_upate_at = System.currentTimeMillis();
				syncLocations();
			}

			SharedPreferences mShared = getSharedPreferences(CONSTANTS.SHARED_PREF_NAME, Context.MODE_PRIVATE);
			long last_gps_time = System.currentTimeMillis();
					//mShared.getLong(CONSTANTS.SHARED_PREF_LAST_GPS_TIME, 0);
			long now_gps_time = System.currentTimeMillis();

			Utils.writeToLogFileWithTime("last location time " + DateTimeFormatings.getDateTimeSec(new Date(last_gps_time)));
			Utils.writeToLogFileWithTime("now location time " + DateTimeFormatings.getDateTimeSec(new Date(now_gps_time)));

			if ((last_gps_time + CONSTANTS.TEN_MINUTES) > now_gps_time) {
				// CONSTANTS.SAVE_NETWORK_PROVIDER_GPS_DATA = false;
			} else {
				// CONSTANTS.SAVE_NETWORK_PROVIDER_GPS_DATA = true;
				((TSRApplication) getApplication()).saveCurrentGeoLocation();
			}

			if (last_gps_time == 0) {
				// CONSTANTS.SAVE_NETWORK_PROVIDER_GPS_DATA = true;
			}

		} catch (Exception e) {
			Utils.writeToLogFileWithTime(" on UpdateLocationService Error : " + e.getLocalizedMessage());
			Utils.writeToErrLogFileWithTime(e);
			e.printStackTrace();
		}
		return START_STICKY;
	}

	private void syncLocations() {
		// TODO Auto-generated method stub
		AsyncUpdate sUpdate = new AsyncUpdate();
		sUpdate.execute();
	}

	private class AsyncUpdate extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			while (true) {
				// TODO Auto-generated method stub
				boolean run = DateTimeFormatings.isInWorkingHours();
				if (!run) {
					break;
				}

				((TSRApplication) getApplication()).updateGeoLocations();
				try {
					Thread.sleep(CONSTANTS.LOCATION_UPDATE_SYNC_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return "";
		}

	}

}
