package com.lk.lankabell.android.activity.tsr.activity;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Jei
 * <p>
 * This class is used to select the  Merchant Inventory Types.
 */
public class MerchantInventorySelectType extends Activity {


    private LocationListener locListener = new MyLocationListener();
    private ProgressDialog progressDialog;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private String londitudeValue;
    private String latitudeValue;
    private LocationManager locManager;
    Spinner spnType;
    int LOCATION_PERMISSSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.merchant_inventory_select_type);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
        if (myTitleText != null) {
            myTitleText.setText("Merchant Inventory");

        }
        final TextView appversion = findViewById(R.id.appversion);
        DatabaseHandler dbh=  new DatabaseHandler(getApplicationContext());
        if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}
        AskLocationPermission();
        DataBindToSpinner();

        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //getGps();
    }

    private void AskLocationPermission() {
        //WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(MerchantInventorySelectType.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(MerchantInventorySelectType.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                AskLocationPermissionAgain();
                //new ToastManager(LoginPage.this).error("SMS Won't send to Coordinator!");
            } else {
                ActivityCompat.requestPermissions(MerchantInventorySelectType.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSSION_REQUEST_CODE);

            }
        }

    }

    private void AskLocationPermissionAgain() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Nowx
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSSION_REQUEST_CODE);

        } else {
            // permission has been granted, continue as usual
        }
    }


    public void getGps() {
        progressDialog = ProgressDialog.show(MerchantInventorySelectType.this, "Please Wait..",
                "Gathering Geo informations");
        // TODO Auto-generated method stub
        //progress.setVisibility(View.VISIBLE);
        // exceptions will be thrown if provider is not permitted.
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
            builder.create().show();
            progressDialog.dismiss();
        }

        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locListener);
        }
        if (network_enabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locListener);
        }
    }

    /**
     * @author Jei
     * <p>
     * This class is used for Gathering Geo information and co-ordinates
     */
    class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

            if (location != null) {
                // This needs to stop getting the location data and save the battery power.
                locManager.removeUpdates(locListener);

                londitudeValue = Double.toString(location.getLongitude());
                latitudeValue = Double.toString(location.getLatitude());

                progressDialog.dismiss();

            }else{
                System.out.println("* location is null");
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


    /**
     * @param view Displaying Spinner selection function
     */
    public void OnclickShow(View view) {




        spnType = (Spinner) findViewById(R.id.spnType);

        final String type = spnType.getSelectedItem().toString();
        if(type.equals("This Area")){
            getGps();
            final String latitudeVal = latitudeValue;
            final String LonditudeVal = londitudeValue;

            Timer _timer  = new Timer();

            _timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // use runOnUiThread(Runnable action)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                if(latitudeVal.equals("0")&&LonditudeVal.equals("0")){
                                    Toast.makeText(MerchantInventorySelectType.this,"Error while gethering Geo Location",Toast.LENGTH_LONG).show();
                                }else{
                                    Intent intent = new Intent(MerchantInventorySelectType.this, MerchantInventoryMerchantList.class);
                                    intent.putExtra("latitudeValue", latitudeVal);
                                    intent.putExtra("londitudeValue", LonditudeVal);
                                    intent.putExtra("type", type);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(MerchantInventorySelectType.this,"Error while gethering Geo Location",Toast.LENGTH_LONG).show();

                                //remove f
                                // make simple f
                                // check
                            }
                        }
                    });
                }
            }, 2000);


        }else{
            Intent intent = new Intent(MerchantInventorySelectType.this, MerchantInventoryMerchantList.class);
            intent.putExtra("latitudeValue", latitudeValue);
            intent.putExtra("londitudeValue", londitudeValue);
            intent.putExtra("type", type);
            startActivity(intent);
        }


    }

    /**
     * Binding the merchant inventory types to spinner function.
     */
    public void DataBindToSpinner() {

        Spinner spnType = (Spinner) findViewById(R.id.spnType);

        String[] array_spinner = new String[3];
        array_spinner[0] = "All";
        array_spinner[1] = "This Area";
        array_spinner[2] = "By City";

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array_spinner);
        spnType.setAdapter(adapter1);

    }


}
