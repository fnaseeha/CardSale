package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.Common;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.Merchant;
import com.lk.lankabell.android.activity.tsr.sqlite.PostalCode;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MerchantActivity extends Activity implements OnClickListener {

	private DatabaseHandler dbh;
	private AutoCompleteTextView textView;
	private Dialog dialog;
	TextView lblMerchant;
	private ProgressDialog progressDialog;
	private TextView tv;
	private LocationManager locManager;
	private LocationListener locListener = new MyLocationListener();
	private boolean gps_enabled = false;
	private boolean network_enabled = false;

	private String longitudeValue;
	private String latitudeValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.pick_merchant);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		myTitleText.setText("Register Location");
		dbh = new DatabaseHandler(getApplicationContext());
		final TextView appversion = findViewById(R.id.appversion);
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}
		locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		getGps();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(), MerchantJobs.class);
		startActivity(intent);
		finish();

		return;
	}

	public void onClickProceed(View view) {
		dbh = new DatabaseHandler(getApplicationContext());
		final Spinner spnCityName = (Spinner) findViewById(R.id.spnCity);
		String cityName = spnCityName.getSelectedItem().toString();
		ArrayList<Merchant> list = dbh.getMerchantList(cityName);
		ArrayList<String> arrList = new ArrayList<String>();
		int i = 0;
		Log.d("This is the ch re ", list.size() + " ________________ ");
		for (Iterator<Merchant> iterator = list.iterator(); iterator.hasNext(); ) {
			Merchant merchant = (Merchant) iterator.next();
			arrList.add(merchant.getName());
			Log.d(i + " This is search result in Merchants : ", merchant.getName() + " = " + merchant.getMerchantId());
			i++;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Merchant");
		final ListView modeList = new ListView(this);
		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arrList);
		modeList.setAdapter(modeAdapter);
		modeList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				String selectedFromList = (String) (modeList.getItemAtPosition(myItemInt));
				Merchant m = dbh.getMerchant(selectedFromList, spnCityName.getSelectedItem().toString());
				if (m != null) {
					Common.setMerchant(m);
					Intent i = new Intent(getApplicationContext(), LocationRegisterActivity.class);
					i.putExtra("Long_Value", longitudeValue);
					i.putExtra("Lati_Val", latitudeValue);
					startActivity(i);
					finish();
				}
			}
		});

		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();


	}

	public void onClickBack(View view) {

		Intent intent = new Intent(getApplicationContext(),
				SelectorActivity.class);
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//	dialog.dismiss();
		finish();
	}

	public boolean IsPostalCodeInsideTheArea(Float posLong, Float posLat, Float Long, Float Lat) {

		boolean isExist = false;
		double distance = (float) Math.sqrt(((posLat - Lat) * (posLat - Lat) * 111 * 111) + ((posLong - Long) * (posLong - Long) * 111 * 111));

		if (CONSTANTS.NO_LOCATION_RESTRICT) {
			return true;
		}


		if (distance < 5)            // radius of the required circle
		{
			isExist = true;
		} else {
			isExist = false;
		}

		return isExist;
	}


	class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if (location != null) {
				// This needs to stop getting the location data and save the battery power.
				locManager.removeUpdates(locListener);

				longitudeValue = Double.toString(location.getLongitude());
				latitudeValue = Double.toString(location.getLatitude());

				DataBindToItemTypeSpinner();

				progressDialog.dismiss();

			}
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
	}

	public void onClick(DialogInterface arg0, int which) {
		// TODO Auto-generated method stub
		if (which == DialogInterface.BUTTON_NEUTRAL) {
			tv.setText("Sorry, location is not determined. To fix this please enable location providers");
		} else if (which == DialogInterface.BUTTON_POSITIVE) {
			startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			//		finish();
			getGps();
		}
	}

	public void getGps() {
		progressDialog = ProgressDialog.show(MerchantActivity.this, "Please Wait..", "Gathering Geo informations");
		// TODO Auto-generated method stub
		//progress.setVisibility(View.VISIBLE);
		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("Attention!");
			builder.setMessage("Sorry, location is not determined. Please enable location providers");
			builder.setPositiveButton("OK", this);
			builder.setNeutralButton("Cancel", this);
			builder.setPositiveButton("", this);
			builder.create().show();
			//	progress.setVisibility(View.GONE);
			progressDialog.dismiss();
		}

		if (gps_enabled) {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
		}
		if (network_enabled) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
		}
	}
	
	public void Erroralert(String title,String msg,int resourceID)
	{
		
		 AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(msg);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      // here you can add functions
				   
			   }
			});
			alertDialog.setIcon(resourceID);
			alertDialog.show();
	}
	
	
	public void DataBindToItemTypeSpinner() {
		ArrayList<String> list = dbh.getCities();
		
		ArrayList<String> SelectedList = new ArrayList<String>();
		
		
		for (int i = 0; i < list.size(); i++) {
			String cityName = list.get(i).toString();
			PostalCode postalCode = dbh.getPostalCodeDetailsByCityName(cityName);
			float Pos_Longitude = Float.parseFloat ((postalCode.getLongitude() ));
			float Pos_Latitude = Float.parseFloat ((postalCode.getLatitude() ));
			boolean isExist = IsPostalCodeInsideTheArea(Pos_Longitude,Pos_Latitude,Float.parseFloat(longitudeValue),Float.parseFloat(latitudeValue));
			
			if(isExist == true)
			{
				SelectedList.add(cityName);
			}
		}
		
		String[] array_spinner = new String[SelectedList.size()];
		for (int i = 0; i < SelectedList.size(); i++){
			array_spinner[i] = SelectedList.get(i).toString();
		}
		
		
		Spinner spnCity = (Spinner)findViewById(R.id.spnCity);
		ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array_spinner);
		spnCity.setAdapter(adapter1);
	}
}
