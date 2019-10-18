package com.lk.lankabell.android.activity.tsr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.Merchant;

public class MerchantViewerFinal extends Activity{

	DatabaseHandler dbh;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.merchantfinalviewer);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTextTitle=(TextView)findViewById(R.id.myTitle);
		myTextTitle.setText("Merchant Viewer");

		final TextView appversion = findViewById(R.id.appversion);
		dbh = new DatabaseHandler(getApplicationContext());
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}

		String merchantId =  getIntent().getExtras().getString("merchantID");

		Merchant merchant = dbh.GetMerchantByMerchantId(Long.valueOf(merchantId));
		EditText txtMerchantName =(EditText)findViewById(R.id.txtMerchantViewerName);
		EditText txtReload_number=(EditText)findViewById(R.id.txtMerchantViewerReloadNumber);
		EditText txtAddress =(EditText)findViewById(R.id.txtMerchantViewerAddress);
		EditText txtPhone =(EditText)findViewById(R.id.txtMerchantViewerContactNo);
		EditText txtCity =(EditText)findViewById(R.id.txtMerchantViewerCity);
		
		txtMerchantName.setText(merchant.getName().toString());
		txtMerchantName.setEnabled(false);
		txtAddress.setText(merchant.getAddress().toString());
		txtAddress.setEnabled(false);
		txtCity.setText(merchant.getCity().toString());
		txtCity.setEnabled(false);
		txtPhone.setText(merchant.getPhone().toString());
		txtPhone.setEnabled(false);
		txtReload_number.setText(merchant.getreloadNo().toString());
		txtReload_number.setEnabled(false);
		txtCity.setText(merchant.getCity().toString());
		txtCity.setEnabled(false);
	}
	
	public void btnBackMerchantViewer(View view) {//When press Edit button
		Intent intent = new Intent(MerchantViewerFinal.this, SelectorActivity.class);
		startActivity(intent);
		finish();
	}
	
}
