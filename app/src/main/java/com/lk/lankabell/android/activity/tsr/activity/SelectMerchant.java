package com.lk.lankabell.android.activity.tsr.activity;
/**
 * @author Kolitha
 *
 */


import java.util.ArrayList;
import java.util.HashMap;

import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;//
import com.lk.lankabell.android.activity.tsr.sqlite.PostalCode;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Jei
 *
 * This class is used for selecting merchants in the card sale activity.
 *
 */
public class SelectMerchant extends Activity implements OnClickListener {

	private DatabaseHandler dbh;
	TextView lblMerchant;
	private ProgressDialog progressDialog;
	private TextView tv;
	private LocationManager locManager;
	private LocationListener locListener = new MyLocationListener();
	private LocationManager MErchantlocManager;
	private LocationListener MErchantlocationListener = new MErchantLocationListener();
	//private boolean gps_enabled = false;
	//private boolean network_enabled = false;
	private Spinner spinner;
	private String longitudeValue;
	private String latitudeValue;
	private String Merchant_longitudeValue;
	private String Merchant_latitudeValue;
	private ListView lv;
	SimpleAdapter adapt = null;
	int selectedPosition;
	private AutoCompleteTextView textview;
	private float Merchant_Longitude;
	private float Merchant_Latitude;
	private Boolean Merchant_Area_Check;

	final ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
	// R.id.lstMerchantID};
	//final String[] columnTags = new String[] {"Id", "Name", ""};
	final int[] columnIds = new int[]{R.id.lstMerchantName, R.id.lstMerchantAddress, R.id.lstMerchantID};
	final String[] columnTags = new String[]{"Name", "Address", "ID"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_selectmerchant);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		myTitleText.setText("Select Merchant");

		spinner = (Spinner) findViewById(R.id.spnCitySelect);
		if (CONSTANTS.NO_LOCATION_RESTRICT) {
			populateCities();
		} else {
			getGps();
		}
		//dataBindToSpinner();
		lv = (ListView) findViewById(R.id.lvRetailers);
		SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String city = spinner.getSelectedItem().toString();
				SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
				Cursor OrderCursor = db.query("MERCHANT_MASTER", new String[]{"ADDRESS", "MERCHANT_NAME", "MERCHANT_ID"}, "CITY ='" + city + "' AND IS_REGISTERED = '1' ", null, null, null, "MERCHANT_NAME");
				listData.clear();
				if (OrderCursor != null) {
					if (OrderCursor.moveToFirst()) {
						for (int i = 0; i < OrderCursor.getCount(); i++) {
							//Merchant_Longitude = Float.parseFloat (OrderCursor.getString(OrderCursor.getColumnIndex("LONGITUDE")));
							//Merchant_Latitude = Float.parseFloat (OrderCursor.getString(OrderCursor.getColumnIndex("LATITUDE")));
							//getMerchantGps();
							//if (Merchant_Area_Check) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_NAME")));
							map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("ADDRESS")));
							map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
							listData.add(map);
							//}
							OrderCursor.moveToNext();
						}//end of for
					}
					OrderCursor.close();
					db.close();
					SimpleAdapter adapter = new SimpleAdapter(SelectMerchant.this, listData, R.layout.listview, columnTags, columnIds);
					lv.setAdapter(adapter);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		SimpleAdapter adapter = new SimpleAdapter(this, listData, R.layout.listview, columnTags, columnIds);
		lv.setAdapter(adapter);
	}

	// ********************* Source code Modified by Jei ****************************

	public void onClickCardSales(View view) {

        LinearLayout vwParentRow = (LinearLayout) view.getParent();

        TextView merchantname = (TextView) vwParentRow.getChildAt(0).findViewById(R.id.lstMerchantName); //vwParentRow.getChildAt(0);
        TextView merchantid = (TextView) vwParentRow.getChildAt(0).findViewById(R.id.lstMerchantID); //vwParentRow.getChildAt(1);
        //TextView IdValue = (TextView) vwParentRow.getChildAt(1);//view.findViewById(R.id.lblMerchantId);

//		AbsoluteLayout vwParentRow = (AbsoluteLayout) view.getParent();
//		TextView merchantname = (TextView) vwParentRow.getChildAt(0);
//		TextView merchantid = (TextView) vwParentRow.getChildAt(1);

		String name = merchantname.getText().toString();
		String mid = merchantid.getText().toString();

		dbh = new DatabaseHandler(getApplicationContext());
		dbh.ClearTempSalesDetails();
		boolean check = dbh.ChekMerchantActivate(name, mid);
		if (check) {
			Toast toast = Toast.makeText(SelectMerchant.this, "sorry merchant is inative can't do the sales.", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
		} else {
			long id = Long.parseLong(mid);
			Double maxRecords = 0.0;
			maxRecords = Double.valueOf(dbh.GetMaxrecords());
			Double getRecordsCount = 0.0;
			Double percentage = 0.0;
			getRecordsCount = Double.valueOf(dbh.GetSalesDetailsCount());
			percentage = ((getRecordsCount * 100) / maxRecords);

			if (percentage > 70 && percentage <= 80) {
				Toast toast = Toast.makeText(SelectMerchant.this, "Limit reached to 80% ", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();

				Intent intent = new Intent(getApplicationContext(), CardSaleActivity.class);//CardSaleActivity.class
				intent.putExtra("merchantID", Long.toString(id));
				intent.putExtra("merchantName", name);
				intent.putExtra("city", spinner.getSelectedItem().toString());
				startActivity(intent);
				finish();

			} else if (percentage > 80 && percentage <= 95) {

				Toast toast = Toast.makeText(SelectMerchant.this, "Limit reached to 80% ", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();

				Intent intent = new Intent(getApplicationContext(), CardSaleActivity.class);//CardSaleActivity.class
				intent.putExtra("merchantID", Long.toString(id));
				intent.putExtra("merchantName", name);
				intent.putExtra("city", spinner.getSelectedItem().toString());
				startActivity(intent);
				finish();
			} else if (percentage > 95) {
				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
				dlgAlert.setMessage("Data limit reached to 95%");
				dlgAlert.setTitle("Infor");
				dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();

			} else {
				Intent intent = new Intent(getApplicationContext(), CardSaleActivity.class);//CardSaleActivity.class
				intent.putExtra("merchantID", Long.toString(id));
				intent.putExtra("merchantName", name);
				intent.putExtra("city", spinner.getSelectedItem().toString());
				startActivity(intent);
				finish();
			}
		}
	}


	public void onClickVisitMerchant(View view) {

        LinearLayout vwParentRow = (LinearLayout) view.getParent();

        TextView merchantname = (TextView) vwParentRow.getChildAt(0).findViewById(R.id.lstMerchantName); //vwParentRow.getChildAt(0);
        TextView merchantid = (TextView) vwParentRow.getChildAt(0).findViewById(R.id.lstMerchantID); //vwParentRow.getChildAt(1);

		String name = merchantname.getText().toString();
		String mid = merchantid.getText().toString();

		Toast toast = Toast.makeText(SelectMerchant.this, name + "  L   " + mid, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();

		Intent intent = new Intent(SelectMerchant.this, RemarksActivity.class);
		intent.putExtra("merchantID", mid);
		intent.putExtra("merchantName", name);
		startActivity(intent);
		finish();
	}


	//***************************** Source code Modified by Jei Ends *********************************************


	public void getGps() {
		progressDialog = ProgressDialog.show(SelectMerchant.this, "Please Wait..", "Gathering Geo informations");
		//progress.setVisibility(View.VISIBLE);
		boolean gps_enabled = false;
		boolean network_enabled = false;
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		try {
			gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// don't start listeners if no provider is enabled
		if (!gps_enabled || !network_enabled) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("Attention!");
			builder.setMessage(gps_enabled + "Sorry, location is not determined. Please enable location providers" + network_enabled);
			builder.setPositiveButton("OK", this);
			builder.setNeutralButton("Cancel", this);
			builder.create().show();
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

	public void getMerchantGps() {
		progressDialog = ProgressDialog.show(SelectMerchant.this, "Please Wait..", "Gathering Geo informations");
		boolean gps_enabled = false;
		boolean network_enabled = false;
		MErchantlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		try {
			gps_enabled = MErchantlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			network_enabled = MErchantlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (!gps_enabled || !network_enabled) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("Attention!");
			builder.setMessage(gps_enabled + "Sorry, location is not determined. Please enable location providers" + network_enabled);
			builder.setPositiveButton("OK", this);
			builder.setNeutralButton("Cancel", this);
			builder.create().show();
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
			MErchantlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MErchantlocationListener);
			
		}
		if (network_enabled) {
			MErchantlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, MErchantlocationListener);
		}
	}
	
	
	class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			
			if (location != null) {
				locManager.removeUpdates(locListener); 
				longitudeValue = Double.toString(location.getLongitude());
				latitudeValue = Double.toString(location.getLatitude());
				
				dbh = new DatabaseHandler(getApplicationContext());
				ArrayList<String> list = dbh.getRegisteredMerchantsCityList();
				
				ArrayList<String> SelectedList = new ArrayList<String>();
				Spinner itemSpinner = (Spinner)findViewById(R.id.spnCitySelect);
				for (int i = 0; i < list.size(); i++) {
					String cityName = list.get(i).toString();
					PostalCode postalCode = dbh.getPostalCodeDetailsByCityName(cityName);
					float Pos_Longitude = Float.parseFloat ((postalCode.getLongitude() ));
					float Pos_Latitude = Float.parseFloat ((postalCode.getLatitude() ));
					boolean isExist = IsPostalCodeInsideTheArea(Pos_Longitude,Pos_Latitude,Float.parseFloat(longitudeValue),Float.parseFloat(latitudeValue));
					if(isExist == true) {
						SelectedList.add(cityName);
					}
				}
			    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectMerchant.this,android.R.layout.simple_dropdown_item_1line, SelectedList);
			    itemSpinner.setAdapter(adapter);
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
	
	
	//***************************************************************************************************************************************
	
	class MErchantLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {			
			if (location != null) {
				locManager.removeUpdates(locListener); 
				Merchant_longitudeValue = Double.toString(location.getLongitude());
				Merchant_latitudeValue = Double.toString(location.getLatitude());
				boolean isExist = CHECK_MERCHANT_GEO(Merchant_Longitude,Merchant_Latitude,Float.parseFloat(longitudeValue),Float.parseFloat(latitudeValue));
				if(isExist == true) {
					Merchant_Area_Check = true;
				}
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
	
	
	//***************************************************************************************************************************************
	
	
	public void onClick(DialogInterface arg0, int which) {
		try {
			if(which == DialogInterface.BUTTON_NEUTRAL){
				tv.setText("Sorry, location is not determined. To fix this please enable location providers");
			}else if (which == DialogInterface.BUTTON_POSITIVE) {
				startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				finish();
				getGps();
			}
		} catch (Exception e) {
			Utils.writeToErrLogFileWithTime(e);
		}
	}
	
	
	/**
	 * @param posLong
	 * @param posLat
	 * @param Long
	 * @param Lat
	 * @return exists or not.
	 * 
	 * Checking the places which are inside the 2km radius function.
	 * 
	 */
	public boolean IsPostalCodeInsideTheArea(Float posLong,Float posLat,Float Long,Float Lat) {		
		boolean isExist =false;
		
		if(CONSTANTS.NO_LOCATION_RESTRICT){
			return true;
		}


		double distance = (float)Math.sqrt( ((posLat - Lat)*(posLat - Lat)*111*111) + ((posLong - Long)*(posLong - Long) *111*111) );
		if(distance < 5 ) {		// radius of the required circle
			isExist = true;
		} else {
			isExist = false;
		}		
		return isExist;
	}


//	public static Double distanceBetween(WeightedLatLng point1, LatLng point2) {
//		if (point1 == null || point2 == null) {
//			return null;
//		}
//
//		return SphericalUtil.computeDistanceBetween(point1, point2);
//	}

	private boolean CHECK_MERCHANT_GEO(Float posLong, Float posLat,Float Long,Float Lat) {	
		boolean isExist = false;
		if(CONSTANTS.NO_LOCATION_RESTRICT){
			return true;
		}
		
		double distance = (float)Math.sqrt( ((posLat - Lat)*(posLat - Lat)*111*111) + ((posLong - Long)*(posLong - Long) *111*111) );


		if(distance < 1 ) {
			isExist = true;
		} else {
			isExist = false;
		}		
		return isExist;
	}
	
	/**
	 * Binding the city data to the spinner function.
	 */
	private void  dataBindToSpinner() {
		SQLiteDatabase DB = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		spinner = (Spinner) findViewById(R.id.spnCitySelect);
		Cursor spnCursor = DB.rawQuery("SELECT distinct (A.CITY) FROM CITY_POSTAL_CODES A,MERCHANT_MASTER B WHERE A.CITY = B.CITY AND B.IS_REGISTERED ='1' ",null);
		
		String[] array_spinner = new String[spnCursor.getCount()];
		if(spnCursor.moveToFirst()) {
			for (int i = 0; i < spnCursor.getCount(); i++) {
				array_spinner[i] = spnCursor.getString(spnCursor.getColumnIndex("CITY") );
				spnCursor.moveToNext();
			}
		}
		spnCursor.close();
		DB.close();
		
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, array_spinner);
		spinner.setAdapter(adapter);
	}
	private void  populateCities() { 
		Spinner itemSpinner = (Spinner)findViewById(R.id.spnCitySelect); 
		dbh = new DatabaseHandler(getApplicationContext());
		ArrayList<String> list = dbh.getRegisteredMerchantsCityList();
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectMerchant.this,android.R.layout.simple_dropdown_item_1line, list);
	    itemSpinner.setAdapter(adapter);
	}
	
	
}

