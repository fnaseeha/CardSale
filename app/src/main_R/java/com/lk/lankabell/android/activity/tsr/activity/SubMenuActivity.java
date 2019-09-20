package com.lk.lankabell.android.activity.tsr.activity;


import com.lk.lankabell.android.activity.tsr.R;
//import com.lk.lankabell.android.tsr.activity.CardSaleActivity.MyLocationListener;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class SubMenuActivity extends Activity {

	private String city;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		
		// Set View to submenu.xml
		setContentView(R.layout.submenu);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		final TextView myTitleText=(TextView)findViewById(R.id.myTitle);
		myTitleText.setText("Select Your options");
		
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		
		TextView lblUserName = (TextView)findViewById(R.id.lblUserName);
		lblUserName.setText(user.getUserName());
		
		TextView lblMerchant = (TextView)findViewById(R.id.lblMerchant);
		lblMerchant.setText(getIntent().getExtras().getString("merchantName"));
		
		Button btnMainMenu = (Button)findViewById(R.id.btnMainMenu);
		
		TextView lblCity = (TextView)findViewById(R.id.lblCity);
		lblCity.setText(getIntent().getExtras().getString("city"));
		city = getIntent().getExtras().getString("city");

	    
	}

	
	
	public void subContinueClickHandler(View view) {

		String merchantId = getIntent().getExtras().getString("merchantID");
		
		//switch (view.getId()) {
		//case R.id.btnSubCon:
			RadioButton card = (RadioButton) findViewById(R.id.radioSubCardSale);
			String mer_name = getIntent().getExtras().getString("merchantName");

			if (card.isChecked()) {

				Intent intent = new Intent(getApplicationContext(),CardSaleActivity.class);
				intent.putExtra("merchantID", merchantId);
				intent.putExtra("merchantName",mer_name);
				intent.putExtra("city", city);
				startActivity(intent);
				finish();

			} else {
				Intent intent = new Intent(getApplicationContext(),RemarksActivity.class);
				intent.putExtra("merchantID", merchantId);
				intent.putExtra("merchantName",mer_name);
				intent.putExtra("city", city);
				startActivity(intent);
				finish();
			}
			//break;
		//}
	}
	
	public void OnClickBackToMainMenu(View view) {
		
		Intent intent = new Intent(getApplicationContext(),SelectorActivity.class);
		startActivity(intent);
		finish();
	}
	
	
}
