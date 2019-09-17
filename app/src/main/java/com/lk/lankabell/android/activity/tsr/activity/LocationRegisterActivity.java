/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import java.text.SimpleDateFormat;
import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.Common;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.Merchant;
import com.lk.lankabell.android.activity.tsr.sqlite.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import java.util.Date;

public class LocationRegisterActivity extends Activity {

	private Long id;
	private DatabaseHandler dbh = null;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.merchant_location);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		final TextView myTextTitle=(TextView)findViewById(R.id.myTitle);
		myTextTitle.setText("Register Location");
		
		TextView name = (TextView) findViewById(R.id.txtName); 
		name.setText(Common.getMerchant().getName());
		
		TextView address = (TextView) findViewById(R.id.txtAddress); 
		address.setText(Common.getMerchant().getAddress());

		
		TextView phone = (TextView) findViewById(R.id.txtMobile); 
		phone.setText(Common.getMerchant().getPhone());
		
		TextView city = (TextView) findViewById(R.id.txtCity); 
		city.setText(Common.getMerchant().getCity());
		
		TextView reloadNo = (TextView) findViewById(R.id.txtReloadNo); 
		reloadNo.setText(Common.getMerchant().getreloadNo());
		
		dbh = new DatabaseHandler(getApplicationContext());
		id = dbh.getMerchantIdByNameANDPhone(name.getText().toString(), phone.getText().toString());

		
	}
	
	/**
	 * Registration Success function.
	 */
	public void registrationSuccess(){
		// when click yes button on the pop up menu
		
		
		Builder inputDialog = new AlertDialog.Builder(this);
		String longitude = (getIntent().getExtras().getString("Long_Value"));
		String latitude = (getIntent().getExtras().getString("Lati_Val"));
		Merchant merchant = new Merchant(id, dbh.getMerchantNameById(id), "", "", 0, "", latitude, longitude, 0, 1,"");
		Date d = new Date(); 
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");  
		//sdf.format(d).toString()
		DatabaseHandler db  = new DatabaseHandler(getApplicationContext());
		User user = db.getUserDetails();
		dbh.UpdateMerchant(merchant.getLatitude(), merchant.getLongitude(), sdf.format(d).toString(), Integer.toString(user.getId()), id);	
				
		inputDialog.setMessage("Successfully registered..");
		inputDialog.setPositiveButton("Ok", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				Intent intent = new Intent(getApplicationContext(),MerchantJobs.class);
				startActivity(intent);
				finish();
			}
		});
		
		inputDialog.show();
	}
	
	/**
	 * @param view
	 * 
	 * Merchant Registration function.
	 * 
	 */
	public void registerMerchant(View view){
		// press register button
		
		Builder inputDialog = new AlertDialog.Builder(this);
		inputDialog.setMessage("Do you want to Continue?");
		inputDialog.setPositiveButton("Yes", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				registrationSuccess();	
			}
		});
		inputDialog.setNegativeButton("No", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		inputDialog.show();
	}
	
}
