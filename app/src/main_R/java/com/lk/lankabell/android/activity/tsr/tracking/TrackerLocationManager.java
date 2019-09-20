package com.lk.lankabell.android.activity.tsr.tracking;

import com.lk.lankabell.android.activity.tsr.tracking.recivers.StatusChangeReceiver;
import com.lk.lankabell.android.activity.tsr.tracking.recivers.ScreenOnReciver;
import com.lk.lankabell.android.activity.tsr.tracking.services.LocationTrackingService;
import com.lk.lankabell.android.activity.tsr.tracking.services.UpdateLocationService;
import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class TrackerLocationManager {

	IOnTrackerLocationChanged onLocationChange;
	private Context context;
	private Location currentBestLocation;
	private static long MIN_TIME_BW_UPDATES = 30 * 1000;
	private static float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // maually changed
																// to 0

	public LocationManager locationManager;
	private TSRLocationListen locatoinlistner;

	public TrackerLocationManager(IOnTrackerLocationChanged onLocationChange, Context context) {
		super();
		this.onLocationChange = onLocationChange;
		this.context = context;
		trackLocation();
	}

	public void trackLocation() {
		try {
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			locatoinlistner =  new TSRLocationListen();
			Criteria locationCritera = new Criteria();
			locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
			locationCritera.setAltitudeRequired(false);
			locationCritera.setBearingRequired(false);
			locationCritera.setCostAllowed(true);
			locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);

			String providerName = locationManager.getBestProvider(locationCritera, true);
			Location location = locationManager.getLastKnownLocation(providerName);

			double latitude = 0;
			double longitude = 0;

			boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				if (isNetworkEnabled && CONSTANTS.USE_NETWORK_PROVIDER) {
					{
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new TSRLocationListen());
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								Log.v("accuracy", "" + location.getAccuracy());

							}
						}
					}
				}

				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					// if (location.getAccuracy() > 1000)
					{
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locatoinlistner);
						if (locationManager != null) {
							Location temp_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (temp_location != null && location != null) {
								if (location.getTime() < temp_location.getTime()) {
									location = temp_location;
								}
							}
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();

							}
						}
					}
				}

				// updateLocationData(location);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Utils.writeToErrLogFileWithTime(e);
		}

	}

	private class TSRLocationListen implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if (!DateTimeFormatings.isInWorkingHours()) {
				Utils.writeToLogFileWithTime("Unregistering LocationListener");
				Log.v("TrackerLocationManager", "unregistering services");
				unregisterService();
			}

			if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
				updateLocationData(location);
				// Utils.writeToLogFileWithTime("got location from gps provider");

			} else if (CONSTANTS.SAVE_NETWORK_PROVIDER_GPS_DATA && location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
				updateLocationData(location);
				// Utils.writeToLogFileWithTime("got location from network provider");
			}

			// updateLocationData(location);
			// saveLocaion(location.getLatitude(),location.getLongitude());
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

	public void updateLocationData(Location location) {
		// TODO Auto-generated method stub
		if (isBetterLocation(location, currentBestLocation)) {
			currentBestLocation = location;
			this.onLocationChange.setOnTrackerLocationChanged(location);

			try {
				SharedPreferences mShared = context.getSharedPreferences(CONSTANTS.SHARED_PREF_NAME, Context.MODE_PRIVATE);
				Editor editor = mShared.edit();
				editor.putLong(CONSTANTS.SHARED_PREF_LAST_GPS_TIME, ((location != null) ? location.getTime() : System.currentTimeMillis()));
				editor.commit();

				CONSTANTS.SAVE_NETWORK_PROVIDER_GPS_DATA = false;

			} catch (Exception e) {
				Utils.writeToErrLogFileWithTime(e);
			}

		} else {
//			 Utils.writeToLogFileWithTime("Given loaction is not best accuracy : "
//			 + location.getAccuracy() );
		}
	}

	public void unregisterService() {
		// TODO Auto-generated method stub
		try {
			context.stopService(new Intent(context, LocationTrackingService.class));
			context.stopService(new Intent(context, UpdateLocationService.class));
			if (locatoinlistner != null && locationManager != null) {
				locationManager.removeUpdates(locatoinlistner);
			}
			context.unregisterReceiver(new StatusChangeReceiver());
			context.unregisterReceiver(new ScreenOnReciver());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final int TWO_MINUTES = 1000 * 60 * 1;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		if (location.distanceTo(currentBestLocation) < MIN_DISTANCE_CHANGE_FOR_UPDATES) {
			return false;
		}

		if ((location.getTime() - currentBestLocation.getTime()) < MIN_TIME_BW_UPDATES) {
			return false;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

}
