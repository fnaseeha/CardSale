package com.lk.lankabell.android.activity.tsr.bg;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

/**
 * @author Administrator
 *
 *This class is used for Gathering geo informations
 */
public class GeoListener implements LocationListener {

	//private ProgressDialog progressDialog;
	private static final String TAG = GeoListener.class.getSimpleName();
	private static boolean isAcquired =false;
	private DatabaseHandler db;
	private LocationManager locManager;
	private Context context;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
    public double logitude;
    public double latitude;

	
	public GeoListener(Context context) {
		// TODO Auto-generated constructor stub
		locManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		this.context = context;
		this.db= new DatabaseHandler(context);
		
	}

	public void onLocationChanged(Location location) {
		if (true) {
			return;
		}
		Log.i(TAG, "Onlocation Changed");
		// TODO Auto-generated method stub
		if (location != null) {

			// This needs to stop getting the location data and save the battery
			// power.
			locManager.removeUpdates(this);
			String a = "";
			String londitude =String.valueOf(location.getLongitude());
			String latitude = String.valueOf(location.getLatitude());
			Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

			try {
				List<Address> addresses = geocoder.getFromLocation(
						location.getLatitude(), location.getLongitude(), 1);

				if (addresses != null) {
					Address returnedAddress = addresses.get(0);
					StringBuilder strReturnedAddress = new StringBuilder(
							"Address:\n");
					for (int i = 0; i < returnedAddress
							.getMaxAddressLineIndex(); i++) {
						strReturnedAddress.append(
								returnedAddress.getAddressLine(i)).append("\n");
					}
					a = strReturnedAddress.toString();
				} else {
					a = "No Address returned!";
				}
			} catch (IOException e) {
				// 
				e.printStackTrace();
				a = "Canont get Address!";
			}

			Log.i(TAG, "latitude<"+latitude +"> londitude<" +londitude+">");
			// tv.setText(londitude + "\n" + latitude + "\n" +a /*+ "\n" +
			// altitiude + "\n" + accuracy + "\n" + time*/);

			isAcquired = true;
			//SelectorActivity.dismissDialog();
			//progressDialog.dismiss();
			
			
		}

	}
	
	public static boolean isAcquireSuccess(){
		
		return isAcquired;
	}


	
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void requestUpdates() {

		if (true) {
			return;
		}
		//progressDialog = ProgressDialog.show(this.context , "Please Wait..", "Gathering Geo informations");
		Log.i(TAG, "request updates");
		isAcquired = false;
		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = locManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle("Attention!");
			builder.setMessage("Sorry, location is not determined. Please enable location providers");
			builder.setPositiveButton("OK", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					context.startActivity(new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				}
			});

			builder.setPositiveButton("Cancel", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					AlertDialog.Builder builder = new Builder(context);
					builder.setTitle("Attention!");
					builder.setMessage("Sorry, location is not determined. To fix this please enable location providers");
					builder.setPositiveButton("OK", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							System.exit(0);
						}
					});

				}
			});

			builder.create().show();
		}

		if (gps_enabled) {

			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		}
		if (network_enabled) {

			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		}

	}

}
