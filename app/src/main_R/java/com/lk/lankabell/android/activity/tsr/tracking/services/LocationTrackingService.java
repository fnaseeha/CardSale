package com.lk.lankabell.android.activity.tsr.tracking.services;

import java.util.Date;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lk.lankabell.android.activity.tsr.activity.SelectorActivity;
import com.lk.lankabell.android.activity.tsr.tracking.IOnTrackerLocationChanged;
import com.lk.lankabell.android.activity.tsr.tracking.TrackerLocationManager;
import com.lk.lankabell.android.activity.tsr.tracking.db.DBTracker;
import com.lk.lankabell.android.activity.tsr.tracking.domain.UserLocation;
import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;
import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;

public class LocationTrackingService extends Service implements
        IOnTrackerLocationChanged, SensorEventListener {

	// private static final long MIN_TIME_BW_UPDATES = 5;
	// private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
	private DBTracker dbHelper;
	private TrackerLocationManager mTracker;
	private DatabaseHandler mDbTsr;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Utils.writeToLogFileWithTime("called gps tracking service");
		Log.v("LocationTrackingService", "onStartCommand");

		startForeground(CONSTANTS.NOTIFICATION_ID.FOREGROUND_SERVICE,
				getNotification());

		try {
			dbHelper = new DBTracker(getBaseContext());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			try {
				if (mDbTsr != null) {
					mDbTsr.close();
				}
			} catch (Exception e) {
			}

			mDbTsr = new DatabaseHandler(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		mTracker = new TrackerLocationManager(this, this);
		return START_STICKY;

	}

	public void updateLocationData(Location location) {

		// ////////////////////////////////////////////////////////
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		c.setAccuracy(Criteria.ACCURACY_COARSE);
		c.setAltitudeRequired(false);
		c.setBearingRequired(false);
		c.setCostAllowed(true);
		c.setPowerRequirement(Criteria.POWER_HIGH);
		String provider = locationManager.getBestProvider(c, true);
		Location locationn = locationManager.getLastKnownLocation(provider);
		// ////////////////////////////////////////////////////////

		UserLocation lastLocation = dbHelper.getLastLocation();
		// Location location1 = new Location("");
		// location1.setLatitude(lastLocation.getLat());
		// location1.setLongitude(lastLocation.getLon());
		//
		// Location location2 = new Location("");
		// location2.setLatitude(location.getLatitude());
		// location2.setLongitude(location.getLongitude());
		//
		// float distanceInMeters = location1.distanceTo(location2);

		if (lastLocation.getLat() != null) {
			if ((location.getTime() - lastLocation.getSystemDate().getTime()) < (1000 * 10)) {
				return;
			}
		}
		// removing milisconds
		long loc_time = System.currentTimeMillis();
				//location.getTime(); // location time
		Date d_loc_time = new Date(loc_time);// 29
		String s_loc_time = DateTimeFormatings.getDateTimeSec(d_loc_time);
		Date nomili_loc_date = DateTimeFormatings.getDateTimeWeb(s_loc_time);

		User user = mDbTsr.getUserDetails();
		boolean b = dbHelper.addLocation("" + user.getId(),
				"" + location.getLatitude(), "" + location.getLongitude(), ""
						+ location.getAccuracy(), nomili_loc_date.getTime(), ""
						+ location.getSpeed(), "" + System.currentTimeMillis());

		Utils.writeToLogFileWithTime("get location object :"
				+ location.getLatitude() + "," + location.getLongitude()
				+ " -- " + location.getAccuracy());
	}

	@Override
	public void onDestroy() {
		// Toast.makeText(this, "Location Service Destroying",
		// Toast.LENGTH_LONG).show();
		super.onDestroy();
		try {
			if (Utils.isTrackingOn(this)) {
				startService(new Intent(this, LocationTrackingService.class));
				// Toast.makeText(this, "Location Service restarted",
				// Toast.LENGTH_LONG).show();
			} else {
				mTracker.unregisterService();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void setOnTrackerLocationChanged(Location location) {
		// TODO Auto-generated method stub
		updateLocationData(location);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	private float[] mGravity;
	private float mAccel;
	private float mAccelCurrent;
	private float mAccelLast;

	private SensorManager sensorMan;
	private Sensor accelerometer;

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
		// mGravity = event.values.clone();
		// // Shake detection
		// float x = mGravity[0];
		// float y = mGravity[1];
		// float z = mGravity[2];
		// mAccelLast = mAccelCurrent;
		// mAccelCurrent = FloatMath.sqrt(x*x + y*y + z*z);
		// float delta = mAccelCurrent - mAccelLast;
		// mAccel = mAccel * 0.9f + delta;
		// // Make this higher or lower according to how much
		// // motion you want to detect
		//
		// Log.v("Accelerometer","" + mAccel);
		//
		// if(mAccel > 3){
		// // do something
		// Utils.writeToLogFile("accelerometer data : " + mAccel);
		// }
		// }
	}

	public Notification getNotification() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);
		builder.setSmallIcon(R.drawable.application_icon)
				.setContentTitle("TSR Automation").setContentText("Lanka Bell")
				.setOngoing(true).setWhen(System.currentTimeMillis());
		Intent intent = new Intent(getApplicationContext(),
				SelectorActivity.class);
		PendingIntent pintent = PendingIntent.getActivity(
				getApplicationContext(), 123123, intent, 0);
		builder.setContentIntent(pintent);
		Notification notification = builder.build();
		return notification;
	}

}
