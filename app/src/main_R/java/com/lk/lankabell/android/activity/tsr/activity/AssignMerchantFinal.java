package com.lk.lankabell.android.activity.tsr.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.R;

public class AssignMerchantFinal extends Activity{
	
	
	private DatabaseHandler dbh = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.assignmerchantmain);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		final TextView myTextTitle=(TextView)findViewById(R.id.myTitle);
		myTextTitle.setText("Assign Merchant");
		
		TextView name = (TextView) findViewById(R.id.txtMerchantName); 
		
		Intent i=getIntent();
		Bundle bundle=i.getExtras();
		
		name.setText(bundle.getString("merchantName"));

		TextView address = (TextView) findViewById(R.id.txtMerchantAddress); 
		
		Intent s=getIntent();
		Bundle bundle1=s.getExtras();		
		address.setText(bundle1.getString("Address"));
		
		TextView phone = (TextView) findViewById(R.id.txtMerchantContact); 
		Intent s1=getIntent();
		Bundle bundle2=s1.getExtras();
		phone.setText(bundle2.getString("contact"));
		
		TextView merchantid = (TextView) findViewById(R.id.txtMerchantID); 
		Intent s2=getIntent();
		Bundle bundle3=s2.getExtras();
		
		merchantid.setText(bundle2.getString("merchantid"));
		
		dbh = new DatabaseHandler(getApplicationContext());
	}

	public boolean isOnline() {			// check network connection is available
		boolean connected = false;
		
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		
		    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || 
		            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
		        //we are connected to a network
		        connected = true;
		    }
		    else
		    {
		        connected = false;
		    }
		    return connected;
	}
	
}
