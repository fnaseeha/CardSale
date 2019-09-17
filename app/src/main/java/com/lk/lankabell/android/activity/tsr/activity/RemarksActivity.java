package com.lk.lankabell.android.activity.tsr.activity;


import java.text.SimpleDateFormat;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.Remarks;
import com.lk.lankabell.android.activity.tsr.sqlite.User;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * @author Jei
 *
 * This class is used for sending remarks to the web service. 
 *
 */
public class RemarksActivity extends Activity implements android.content.DialogInterface.OnClickListener {

	private String remarks;
	private DatabaseHandler dbh = null;
	private ProgressDialog progressDialog;
	private TextView tv;
	private LocationManager locManager;
	private LocationListener locListener = new MyLocationListener();
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	TextView lblMerchanttext;
	TextView lblMerchantID;
	String londitudeValue;
	String latitudeValue;
	private EditText editRemarks;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.remarks);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		myTitleText.setText("Enter Remarks");

		editRemarks = (EditText) findViewById(R.id.txtRemark);

		String merchant = getIntent().getExtras().getString("merchantName");
		lblMerchanttext = (TextView) findViewById(R.id.lblMerchantName);
		lblMerchanttext.setText(merchant);

		String merchantID = getIntent().getExtras().getString("merchantID");
		lblMerchantID = (TextView) findViewById(R.id.merchantID);
		lblMerchantID.setText(merchantID);

		locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

	}

	public void saveClickHandlerRemarks(View view) {
		getGps();
	}


	/**
	 * @author Jei
	 *
	 * Gathering Geo informations and coodinates function.
	 *
	 */
	class MyLocationListener implements LocationListener {

		@SuppressWarnings("deprecation")
		public void onLocationChanged(Location location) {
			if (location != null) {
				locManager.removeUpdates(locListener);
				londitudeValue = Double.toString(location.getLongitude());
				latitudeValue = Double.toString(location.getLatitude());
				progressDialog.dismiss();
				final InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				remarks = editRemarks.getText().toString();
				if (remarks.equalsIgnoreCase("")) {
					AlertDialog alertDialog = new AlertDialog.Builder(RemarksActivity.this).create();
					alertDialog.setTitle("Message");
					alertDialog.setMessage("Remarks can not be empty!");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					alertDialog.setIcon(R.drawable.ic_alert);
					alertDialog.show();
				} else {
					mgr.hideSoftInputFromWindow(editRemarks.getWindowToken(), 0);
					dbh = new DatabaseHandler(getApplicationContext());
					Builder inputDialog = new AlertDialog.Builder(RemarksActivity.this);
					inputDialog.setTitle("Saving....");
					inputDialog.setMessage("Do you want to save?");
					inputDialog.setPositiveButton("Yes",
							new OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									saveRemarks();
								}
							});
					inputDialog.setNegativeButton("No",
							new OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							});
					inputDialog.show();
				}
			}
		}

		protected void saveRemarks() {
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			String dateValue = sdf1.format(System.currentTimeMillis());
			dbh = new DatabaseHandler(getApplicationContext());
			int remarksId = ((dbh.GetmaxRemarksId()) + 1);
			String merchantId = lblMerchantID.getText().toString();
			Long merchantID = Long.parseLong(merchantId);
			Remarks remarks = new Remarks(remarksId, merchantID, editRemarks.getText().toString(), true, londitudeValue, latitudeValue, dateValue);

			User user = dbh.getUserDetails();
			dbh.SaveRemaks(remarks, Integer.toString(user.getId()));
			dbh.UpdateRemarksTable(remarksId, 1);
			Toast toast = Toast.makeText(getApplicationContext(), "Successfully Saved!", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();

			//TextView lblMerchanttext = (TextView)findViewById(R.id.lblMerchant);
			//String merchantName = lblMerchanttext.getText().toString();

			Intent intent = new Intent(getApplicationContext(), home.class);
			//intent.putExtra("merchantID", merchantId);
			//intent.putExtra("merchantName", merchantName);
			//intent.putExtra("city", getIntent().getExtras().getString("city"));
			startActivity(intent);
			finish();
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
			finish();
			getGps();
		}
	}

	public void getGps() {
		progressDialog = ProgressDialog.show(RemarksActivity.this, "Please Wait..", "Gathering Geo informations");
		try {
			gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// don't start listeners if no provider is enabled
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
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
	}
	if (network_enabled) {
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
	}
}


public void OnClickBack(View view) {
	
	String merchantId = getIntent().getExtras().getString("merchantID");
	
	TextView lblMerchanttext = (TextView)findViewById(R.id.lblMerchant);
	String merchantName = lblMerchanttext.getText().toString();
	
	Intent intent = new Intent(getApplicationContext(), SelectMerchant.class);
	intent.putExtra("merchantID", merchantId);
	intent.putExtra("merchantName", merchantName);
	intent.putExtra("city", getIntent().getExtras().getString("city"));
	startActivity(intent);
	finish();
	
}
}
