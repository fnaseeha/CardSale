/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.PostalCode;
import com.lk.lankabell.android.activity.tsr.sqlite.User;

public class AddMerchant extends Activity{
	private EditText txtmerchant_name,txtmerchant_add,txtmerchant_contact,txtReload_number;
	private AutoCompleteTextView auto_city;
	public DatabaseHandler dbh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.addmerchant);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		  final TextView myTitleText = (TextView)findViewById(R.id.myTitle);
          if ( myTitleText != null ) {myTitleText.setText("Add Merchant");}
          dbh = new DatabaseHandler(getApplicationContext());
  		ArrayList<String> list = dbh.getCities();
  		for (int i = 0; i < list.size(); i++) {
  			String cityName = list.get(i).toString();
  			PostalCode postalCode = dbh.getPostalCodeDetailsByCityName(cityName);
  			float Pos_Longitude = Float.parseFloat ((postalCode.getLongitude() ));
  			float Pos_Latitude = Float.parseFloat ((postalCode.getLatitude() ));
  		}
  	    ArrayAdapter<String> adapter = new ArrayAdapter(AddMerchant.this,android.R.layout.simple_dropdown_item_1line, list);
        auto_city = (AutoCompleteTextView)findViewById(R.id.atc_AutoCompleteCity);
        auto_city.setThreshold(1);
        auto_city.setAdapter(adapter);
    }
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(), MerchantJobs.class);
		startActivity(intent);
		finish(); 

        return;
	}
	
	public void onClickMerchantInsert(View view){	
		Button button=(Button) findViewById(R.id.btnAddMerchant);		
		ArrayList<String> list = dbh.getCities();
		//ArrayList<String> list = dbh.getCities();
		TextView error=(TextView) findViewById(R.id.txtErrorMessage);
		txtmerchant_name=(EditText)findViewById(R.id.txtAdd_merchantName);
		final String merchant_name=txtmerchant_name.getText().toString();
		txtmerchant_add=(EditText)findViewById(R.id.txtAdd_merchantAddress);
		final String merchant_address=txtmerchant_add.getText().toString();
		txtmerchant_contact=(EditText)findViewById(R.id.txtAddMerchantContact);
		String merchant_contact=txtmerchant_contact.getText().toString();
		auto_city=(AutoCompleteTextView)findViewById(R.id.atc_AutoCompleteCity);
		String cityselect=auto_city.getText().toString();
		txtReload_number =(EditText)findViewById(R.id.txtReloadNumber);
		String Reload_number=txtReload_number.getText().toString();
		
		int merchant_id = dbh.GetmaxMerchantId();
		
		merchant_id = dbh.getValiedMerchantId(merchant_id);
		
		
		if(merchant_name.equalsIgnoreCase("")){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Please Enter Name");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtmerchant_name.setText(txtmerchant_name.getText());
					txtmerchant_add.setText(txtmerchant_add.getText());
					txtmerchant_contact.setText(txtmerchant_contact.getText());
					auto_city.setText(auto_city.getText());
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
		}
		else if(merchant_address.equalsIgnoreCase("")){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Please Enter Address");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtmerchant_name.setText(txtmerchant_name.getText());
					auto_city.setText(auto_city.getText());
					txtmerchant_contact.setText(txtmerchant_contact.getText());
					txtmerchant_add.setText(txtmerchant_add.getText());
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
		}
		else if(Reload_number.equalsIgnoreCase("")){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Please Enter Reload number");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtmerchant_name.setText(txtmerchant_name.getText());
					auto_city.setText(auto_city.getText());
					txtmerchant_contact.setText(txtmerchant_contact.getText());
					txtmerchant_add.setText(txtmerchant_add.getText());
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
		}
		else if(merchant_contact.equalsIgnoreCase("")){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Enter Phone Number");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtmerchant_name.setText(txtmerchant_name.getText());
					txtmerchant_add.setText(txtmerchant_add.getText());
					auto_city.setText(auto_city.getText());
					txtmerchant_contact.setText("");
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
		}
		else if(cityselect.equalsIgnoreCase("")){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Please Enter City ");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtmerchant_name.setText(txtmerchant_name.getText());
					txtmerchant_add.setText(txtmerchant_add.getText());
					auto_city.setText(auto_city.getText());
					txtmerchant_contact.setText(txtmerchant_contact.getText());
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
		}
		else if(merchant_contact.length()<10|| merchant_contact.length()>10){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

    		dlgAlert.setMessage("Entered Phone number is incorrect");
    		dlgAlert.setTitle("Alert");
			dlgAlert.setIcon(R.drawable.ic_alert);
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtmerchant_name.setText(txtmerchant_name.getText());
					txtmerchant_add.setText(txtmerchant_add.getText());
					txtmerchant_contact.setText("");
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
		}
		else if(Reload_number.length()<10|| Reload_number.length()>10){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Entered Reload number is incorrect");
    		dlgAlert.setTitle("Alert");
			dlgAlert.setIcon(R.drawable.ic_alert);
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtmerchant_name.setText(txtmerchant_name.getText());
					txtmerchant_add.setText(txtmerchant_add.getText());
					txtmerchant_contact.setText("");
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
		}
		else if(!list.contains(cityselect)){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("City doesn't exists in the system");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtmerchant_name.setText(txtmerchant_name.getText());
					txtmerchant_add.setText(txtmerchant_add.getText());
					txtmerchant_contact.setText(txtmerchant_contact.getText());
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
		}
		else if(merchant_id < 100 ){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Merchant ID is Incorrect. Do the data reload and try again.");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtmerchant_name.setText(txtmerchant_name.getText());
					txtmerchant_add.setText(txtmerchant_add.getText());
					txtmerchant_contact.setText(txtmerchant_contact.getText());
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
		}
		else {
			String userAllreadyAdd = dbh.CheckMerchantList(merchant_name, merchant_address, cityselect);
			Log.d("-----", "user is the ====================== "+userAllreadyAdd);
			if(userAllreadyAdd.isEmpty() || userAllreadyAdd.equalsIgnoreCase("") || userAllreadyAdd == "" ) {
				User user = dbh.getUserDetails();
				String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
				
				dbh.UpdateLastMerchantNo(merchant_id+1,epfNo);
				Log.d("New Mearchant ID: ", merchant_id +" ********* "+ epfNo );
				String FormatEpfNo = StringUtils.leftPad(epfNo, 7, "0");  
				String idMerchant="";				
				StringBuilder builder=new StringBuilder(String.valueOf(merchant_id));
				builder.append(FormatEpfNo);
				idMerchant=builder.toString();

				long longMearchantID = Long.valueOf(idMerchant); 
			
				dbh.SaveUpdatedMerchants(longMearchantID, merchant_name, merchant_address, merchant_contact, cityselect,Reload_number,epfNo);
				dbh.updateUpdatedMerchantTempTable(merchant_id, 1);

				diplayAlert("Success","Successfully Added");
//				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//				dlgAlert.setMessage("Successfully Added");
//				dlgAlert.setTitle("Confirmation");
//				dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						Intent intent = new Intent(getApplicationContext(),MerchantJobs.class);
//						startActivity(intent);
//						finish();
//					}
//				});
//				dlgAlert.setCancelable(true);
//				dlgAlert.create().show();

			} else {
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
	    		dlgAlert.setMessage("This merchant is already create. Merchant reference is "+ userAllreadyAdd);
	    		dlgAlert.setTitle("Alert");
	    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						txtmerchant_name.setText(txtmerchant_name.getText());
						txtmerchant_add.setText(txtmerchant_add.getText());
						txtmerchant_contact.setText(txtmerchant_contact.getText());
						txtReload_number.setText(txtReload_number.getText());
					}});
	    		dlgAlert.setCancelable(true);
	    		dlgAlert.create().show();
	    		return;
			}
		}
	}

	private void diplayAlert(String b_title, String b_message) {

		LayoutInflater factory = LayoutInflater.from(this);
		final View dialogBuilder = factory.inflate(R.layout.custom_okay_msg_dialog, null);
		final AlertDialog dialogView = new AlertDialog.Builder(this).create();
		dialogView.setView(dialogBuilder);
		TextView title = (TextView) dialogBuilder.findViewById(R.id.title);
		TextView message = (TextView) dialogBuilder.findViewById(R.id.message);
		title.setText(b_title);
		message.setText(b_message);
		dialogView.setCancelable(true);
		dialogBuilder.findViewById(R.id.okay).setOnClickListener(new  View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),MerchantJobs.class);
				startActivity(intent);
				finish();
			}
		});

		dialogView.show();
		Rect displayRectangle = new Rect();
		Window window = this.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		dialogView.getWindow().setLayout((int) (displayRectangle.width() *
				0.8f), dialogView.getWindow().getAttributes().height);
	}
/*
	public boolean isOnline() {			// check network connection is available
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
		        connected = true;
		    }else{
		        connected = false;
		    }
		    return connected;
	}
	*/
}