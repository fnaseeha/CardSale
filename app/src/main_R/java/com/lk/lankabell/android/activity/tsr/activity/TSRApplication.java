package com.lk.lankabell.android.activity.tsr.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lk.lankabell.android.activity.tsr.models.SharedPrefManager;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.db.DBTracker;
import com.lk.lankabell.android.activity.tsr.tracking.domain.Locations;
import com.lk.lankabell.android.activity.tsr.tracking.recivers.BeginServicesReveiver;
import com.lk.lankabell.android.activity.tsr.tracking.recivers.LocationStarterReciver;
import com.lk.lankabell.android.activity.tsr.tracking.recivers.LocationUpdateReciver;
import com.lk.lankabell.android.activity.tsr.tracking.recivers.ScreenOnReciver;
import com.lk.lankabell.android.activity.tsr.tracking.services.LocationTrackingService;
import com.lk.lankabell.android.activity.tsr.tracking.services.UpdateLocationService;
import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;

public class TSRApplication extends android.app.Application {

	private static String NAMESPACE = "http://mainService";
	// private static String METHOD_NAME35 = "synchLocations";
	private static String METHOD_NAME35 = "synchLocationsProc";
	private static String SOAP_ACTION35 = NAMESPACE + "/" + METHOD_NAME35;
	private String url = WsdlReader.getServiceUrl();
	private DBTracker dbTracker;
	private DatabaseHandler dbTSR;

	private static long last_location_upate_at = 0;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dbTracker = new DBTracker(this);
		dbTSR = new DatabaseHandler(this);
		url = WsdlReader.getServiceUrl(true, this);
	}

	public String updateGeoLocations() {

		// stop updating before 5 min from last

		if (!CONSTANTS.UPLOAD_GPS_DATA) {
			return "";
		}
		if (!DateTimeFormatings.isInWorkingHours()) {
			return "";
		}

		long time_diff = System.currentTimeMillis() - last_location_upate_at;

		if (time_diff > SharedPrefManager.getGPSUpdateTimeSync(this)) {

			last_location_upate_at = System.currentTimeMillis();

			List<Locations> locations = dbTracker.getLocationsUnSynced();
			int location_list_size = locations.size();
			Log.v("sync location list size", "" + location_list_size);
			Utils.writeToLogFileWithTime("updating locations sync location list size " + location_list_size);
			int portions_size = 30;
			if (location_list_size > 0) {
				if (location_list_size > portions_size) {
					String return_msg = "";
					int portions = (location_list_size / portions_size) + 1;
					int start_index = 0;
					int end_index = 0;
					for (int index = 0; index < portions; index++) {
						if (end_index > (location_list_size - 1)) {
							end_index = (location_list_size - 1);
						}

						List<Locations> sublist = locations.subList(start_index, end_index);
						return_msg = saveLocationList(sublist, true);
						start_index = end_index + 1;
						end_index += portions_size;

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					return return_msg;

				} else {
					return saveLocationList(locations, true);
				}
			}

		}
		return "";

	}

	private String saveLocationList(List<Locations> locations, boolean isOneTime) {

		for (Locations locations2 : locations) {
			// Utils.writeToLogFileWithTime(locations2.toString());
		}

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME35);
		Gson gson = new Gson();
		String locationsList = gson.toJson(locations);

		User user = dbTSR.getUserDetails();

		if (user == null) {
			Utils.writeToLogFile(String.format("Error while getting user at %s", DateTimeFormatings.getDateTime(new Date())));
			return "error";
		}

		request.addProperty("strInputUserMobile", Utils.getSimSerialNumber(this));
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("synchLocations", locationsList);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
			androidHttpTransport.call(SOAP_ACTION35, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				Gson gsonResponse = new Gson();
				Log.w("success--------------", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + result + "------------------------------------------------");
				Type type = new TypeToken<ArrayList<String>>() {
				}.getType();
				ArrayList<String> list = gsonResponse.fromJson(result.getProperty(0).toString(), type);
				for (String string : list) {
					// dbh.updateSynchLocations(string);
					Log.w("location id", string);
					dbTracker.updateLocation(string);
				}

				if (list.size() > 0) {
					// dbTracker.clearLocationData();
				}

				return "ok";

			} else {
				return "error";
			}

		} catch (Exception e) {
			Log.w("error", "locations not updated .");
			Utils.writeToLogFileWithTime("error while updating locations");
			if (e != null) {
				Utils.writeToLogFileWithTime(e.getLocalizedMessage());
			}
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
	}

	public void startTSRServices() {

		if (Utils.isTrackingOn(this)) {

			Log.v("Starting services", "-------------------------------------");
			startTrackingService();
			startUpdateService();
		}else{
			stopServices();
		}

		deleteOldLoactions();

	}

	private void deleteOldLoactions() {
		// TODO Auto-generated method stub
		Calendar syncedDateTime = Calendar.getInstance();
		syncedDateTime.add(Calendar.DATE, -1);
		SQLiteDatabase db = dbTracker.getWritableDatabase();//deleting old location history
		db.delete("locations", "sys_date < ?", new String[] { "" + syncedDateTime.getTimeInMillis() });
		db.close();
	}

	public void startTrackingService() {
		// TODO Auto-generated method stub
		startService(new Intent(this, LocationTrackingService.class));
		int minUpdates = SharedPrefManager.getGPSUpdateTime(this);
		if (minUpdates > 0) {
			Log.v("tracking service mins", "" + minUpdates);
			Intent intent = new Intent(this, LocationStarterReciver.class);
			intent.setAction(CONSTANTS.ACTION.STARTFOREGROUND_ACTION);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1325, intent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), minUpdates, pendingIntent);
			startScreeonOnService();
		}
	}

	public void startUpdateService() {
		// tracking location updating service to server
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTimeInMillis(System.currentTimeMillis());
		// calendar.set(Calendar.HOUR_OF_DAY, 8);
		// calendar.set(Calendar.MINUTE, 30);

		startService(new Intent(this, UpdateLocationService.class));
		int minUpdates = SharedPrefManager.getGPSUpdateTimeSync(this);
		Log.v("update service mins", "" + minUpdates);
		if (minUpdates > 0) {
			Intent intent = new Intent(this, LocationUpdateReciver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1234, intent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), minUpdates, pendingIntent);
		}
	}

	public void startScreeonOnService() {
		int minUpdates = 2;// SharedPrefManager.getUpdateTimeScreenOn(getBaseContext());
		Log.v("screen on service mins", "" + minUpdates);
		Intent intent = new Intent(this, ScreenOnReciver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 123, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		// alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
		// System.currentTimeMillis(), minUpdates * 60 * 1000, pendingIntent);
	}

	public void startBeginReceiver() {
		User user = dbTSR.getUserDetails();
		if (user != null) {
			Intent intent = new Intent(this, BeginServicesReveiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 5894, intent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.HOUR, 8);
			cal.set(Calendar.MINUTE, 0);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
			Utils.writeToLogFileWithTime("started from startBeginReceiver");

			startPeriodicReciver();
		}

	}

	private void startPeriodicReciver() {
		Intent intent = new Intent(this, BeginServicesReveiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 52894, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.HOUR, 14);
		cal.set(Calendar.MINUTE, 00);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

	}

	public void stopServices() {
		Utils.writeToLogFileWithTime("Stoping tracking services");

		if (CONSTANTS.SHOULD_STOP_SERVICES) {
			return;
		}

		try {
			stopService(new Intent(this, LocationTrackingService.class));
			stopService(new Intent(this, UpdateLocationService.class));

		} catch (Exception e) {
			// TODO: handle exception
		}

		PackageManager pm = this.getPackageManager();
		try {
			ComponentName componentName = new ComponentName(TSRApplication.this, LocationUpdateReciver.class);
			pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ComponentName componentName = new ComponentName(TSRApplication.this, LocationStarterReciver.class);
			pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ComponentName componentName = new ComponentName(TSRApplication.this, ScreenOnReciver.class);
			pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Utils.writeToLogFile(String.format("All services are stoped at : %s", DateTimeFormatings.getDateTime(new Date())));
		
		NotificationManager mNotifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifManager.cancel(CONSTANTS.NOTIFICATION_ID.FOREGROUND_SERVICE);

	}

	public void saveCurrentGeoLocation() {

		// try {
		// TrackerNetworkLocationManager networkTracker = new
		// TrackerNetworkLocationManager( this);
		// networkTracker.trackLocation();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// LocationManager locationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		//
		// Criteria locationCritera = new Criteria();
		// locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
		// locationCritera.setAltitudeRequired(false);
		// locationCritera.setBearingRequired(false);
		// locationCritera.setCostAllowed(true);
		// locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
		//
		// String providerName = LocationManager.NETWORK_PROVIDER; //
		// locationManager.getBestProvider(locationCritera, true);
		// Location location =
		// locationManager.getLastKnownLocation(providerName);
		//
		//
		// if(location == null){
		// Utils.writeToLocationLogFile("can not get the location");
		// }
		//
		//
		// // removing milisconds
		// long loc_time = location.getTime();
		// Date d_loc_time = new Date(loc_time);
		// String s_loc_time = DateTimeFormatings.getDateTimeSec(d_loc_time);
		// Date nomili_loc_date = DateTimeFormatings.getDateTimeWeb(s_loc_time);
		// User user = dbTSR.getUserDetails();
		// boolean b = dbTracker.addLocation("" + user.getId(),
		// "" + location.getLatitude(), "" + location.getLongitude(), ""
		// + location.getAccuracy(), nomili_loc_date.getTime(), ""
		// + location.getSpeed(), "" + location.getTime());
		//
		// if (b) {
		// try {
		// SharedPreferences mShared = getSharedPreferences(
		// CONSTANTS.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		// Editor editor = mShared.edit();
		// editor.putLong(
		// CONSTANTS.SHARED_PREF_LAST_GPS_TIME,
//		 ((location != null) ? location.getTime() : System
		// .currentTimeMillis()));
		// editor.commit();
		//
		// CONSTANTS.SAVE_NETWORK_PROVIDER_GPS_DATA = false;
		//
		// } catch (Exception e) {
		//
		// }
		// }

	}

}
