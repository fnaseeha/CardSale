package com.lk.lankabell.android.activity.tsr.activity;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;


/**
 * @author Jei
 *
 *This class is used for Merchant Location registration 
 *
 */
public class MainRadioActivity extends Activity  implements android.content.DialogInterface.OnClickListener{

	//private ProgressBar progress;
	private ProgressDialog progressDialog;
	private TextView tv;
	private LocationManager locManager;
	private LocationListener locListener = new MyLocationListener();
	private Button button;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.menu);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		final TextView myTitleText=(TextView)findViewById(R.id.myTitle);
		if(myTitleText!=null)
		{
			myTitleText.setText("Main menu");
		}
		final TextView appversion = findViewById(R.id.appversion);

		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}

		button =(Button) findViewById(R.id.btnCon);
		button.setEnabled(false);
		tv = (TextView) findViewById(R.id.menuerrortv);

		locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		getGps();
	}

	public void myClickHandler(View view) {
		
		Log.d("Click","1");
		
		
		//switch (view.getId()) {
		//case R.id.btnCon:
			RadioButton card = (RadioButton) findViewById(R.id.radioCard);
			
			RadioButton visit = (RadioButton) findViewById(R.id.radioVisit);
			
			Log.d("Click","2");
	
			if (card.isChecked() || visit.isChecked()) {

				Intent intent = new Intent (MainRadioActivity.this, LocationActivity.class);
	            startActivity(intent);
	            finish();
	            Log.d("Click","3");
				/*Toast.makeText(MainRadioActivity.this,
						card.getText(), Toast.LENGTH_SHORT).show();*/
			} else {
				Intent intent = new Intent (MainRadioActivity.this, FaultActivity.class);
	            startActivity(intent);
	            finish();
	            Log.d("Click","4");
			}
			//break;
		//}
	}
	
	/**
	 * @author Jei
	 *
	 *This class is used for Gathering Geo information and co-ordinates
	 */
	class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {

			if (location != null) {
				// This needs to stop getting the location data and save the battery power.
				locManager.removeUpdates(locListener); 
				
				String londitude = "Londitude: " + location.getLongitude();
				String latitude = "Latitude: " + location.getLatitude();

				tv.setText(londitude + "\n" + latitude /*+ "\n" + altitiude + "\n" + accuracy + "\n" + time*/);

				progressDialog.dismiss();
				button.setEnabled(true);
				
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
		if(which == DialogInterface.BUTTON_NEUTRAL){
			tv.setText("Sorry, location is not determined. To fix this please enable location providers");
		}else if (which == DialogInterface.BUTTON_POSITIVE) {
			startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			finish();
			getGps();
		}
	}
	
	
	/**
	 * Gathering Geo informations function.
	 */
	public void getGps() {
		progressDialog = ProgressDialog.show(MainRadioActivity.this, "Please Wait..", "Gathering Geo informations");
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
			builder.create().show();
		//	progress.setVisibility(View.GONE);
			progressDialog.dismiss();
		}

		if (gps_enabled) {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
		}
		if (network_enabled) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
		}
	}
}