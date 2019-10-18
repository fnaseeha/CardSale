package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.PostalCode;

public class AddCity extends Activity implements OnClickListener {

	public DatabaseHandler dbh;
	private ProgressDialog progressDialog;
	private LocationManager locManager;
	private LocationListener locListener = new MyLocationListener();
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private String longitudeValue;
	private String latitudeValue;
	EditText txtLat;
	EditText txtLong;
	AutoCompleteTextView dropdown_City;

	public void onClickUpdateCities(View view) {
	}

	public void onClickGetGEO(View view) {
		getGps();
	}

	int flag = 1;

	public void onClickAddCities(View view) {

		try {
			Button button = (Button) findViewById(R.id.Button01);
			if (flag == 1) {
				button.setEnabled(false);
				Log.d("Button", "Disabled the button.");
			}

			EditText txtLat = (EditText) findViewById(R.id.txtLat);
			String Lat = txtLat.getText().toString();

			EditText txtLong = (EditText) findViewById(R.id.txtLong);
			String Long = txtLong.getText().toString();

			AutoCompleteTextView dropdown_City = (AutoCompleteTextView) findViewById(R.id.dropdown_City);
			String cityselect = dropdown_City.getText().toString();

			PostalCode postalCode = dbh.getPostalCodeDetailsByCityName(cityselect);
			String postalCodeInt = postalCode.getPostalCode();

			Map<String, Integer> response = new HashMap<String, Integer>();
			response = dbh.UpdateCityLongLat(Float.valueOf(Long), Float.valueOf(Lat), postalCodeInt);

			Collection<String> result = response.keySet();
			String resultString = result.iterator().next();
			Collection<Integer> count = response.values();
			Integer countInt = count.iterator().next();

		if (resultString.equals("success")) {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
			dlgAlert.setMessage("New Geo Saved");
			dlgAlert.setTitle("Confirmation");
			dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getApplicationContext(), MerchantJobs.class);
					startActivity(intent);
					finish();
				}
			});
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
		} else {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
			dlgAlert.setMessage("Maximum registration limit reached (" + countInt + ")");
			dlgAlert.setTitle("Information");
			dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getApplicationContext(), MerchantJobs.class);
					startActivity(intent);
					finish();
				}
			});
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
		}

		}catch (Exception e){
			e.printStackTrace();
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
			dlgAlert.setMessage(e.getMessage());
			dlgAlert.setTitle("Error");
			dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getApplicationContext(), MerchantJobs.class);
					startActivity(intent);
					finish();
				}
			});
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.addcity);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		//final TextView myTextTitle=(TextView)findViewById(R.id.myTitle);

		//
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.addcity);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setText("Add City");
		}
		final TextView appversion = findViewById(R.id.appversion);
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}

		locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		txtLat = (EditText) findViewById(R.id.txtLat);
		txtLong = (EditText) findViewById(R.id.txtLong);

		txtLat.setKeyListener(null);
		txtLat.setEnabled(false);

		txtLong.setKeyListener(null);
		txtLong.setEnabled(false);


		dropdown_City = (AutoCompleteTextView) findViewById(R.id.dropdown_City);

		ArrayList<String> list = dbh.getCities();
		Float Pos_Longitude = null;
		Float Pos_Latitude = null;
		for (int i = 0; i < list.size(); i++) {
			String cityName = list.get(i).toString();
			PostalCode postalCode = dbh.getPostalCodeDetailsByCityName(cityName);
			if (Pos_Longitude != null) {
				Pos_Longitude = Float.parseFloat((postalCode.getLongitude()));
			} else {
				Pos_Longitude = (float) 0;
			}
			if (Pos_Latitude != null) {
				Pos_Latitude = Float.parseFloat((postalCode.getLatitude()));
			} else {
				Pos_Latitude = (float) 0;
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter(AddCity.this, android.R.layout.simple_dropdown_item_1line, list);
		AutoCompleteTextView dropdown_City = (AutoCompleteTextView) findViewById(R.id.dropdown_City);
		dropdown_City.setThreshold(1);
		dropdown_City.setAdapter(adapter);

		dropdown_City.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView,
									View selectedItemView, int position, long id) {
				String selection = (String) parentView.getItemAtPosition(position);
				System.out.println(selection);
				PostalCode postalCode = dbh.getPostalCodeDetailsByCityName(selection);
				//TOAST
				Context context = getApplicationContext();
				CharSequence text = "You seleted : " + selection;
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

				EditText txtLat = (EditText) findViewById(R.id.txtLat);
				txtLat.setText(postalCode.getLatitude());

				EditText txtLong = (EditText) findViewById(R.id.txtLong);
				txtLong.setText(postalCode.getLongitude());


			}
		});

	}

	public boolean IsPostalCodeInsideTheArea(Float posLong, Float posLat, Float Long, Float Lat) {
		boolean isExist = false;
		double distance = (float) Math.sqrt(((posLat - Lat) * (posLat - Lat) * 111 * 111) + ((posLong - Long) * (posLong - Long) * 111 * 111));
		if (distance <= 5) {
			isExist = true;
		} else {
			isExist = false;
		}
		return isExist;
	}

	class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			if (location != null) {
				locManager.removeUpdates(locListener);
				longitudeValue = Double.toString(location.getLongitude());
				latitudeValue = Double.toString(location.getLatitude());

				EditText txtLat = (EditText) findViewById(R.id.txtLat);
				txtLat.setText(latitudeValue);

				EditText txtLong = (EditText) findViewById(R.id.txtLong);
				txtLong.setText(longitudeValue);

				progressDialog.dismiss();

			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public void getGps() {
		progressDialog = ProgressDialog.show(AddCity.this, "Please Wait..", "Gathering Geo informations");
		try {
			gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gps_enabled && !network_enabled) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("Attention!");
			builder.setMessage("Sorry, location is not determined. Please enable location providers");
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
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
					0, locListener);
		}
		if (network_enabled) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					0, 0, locListener);
		}
		
	}

	public void onClick(DialogInterface arg0, int arg1) {
	}

}
