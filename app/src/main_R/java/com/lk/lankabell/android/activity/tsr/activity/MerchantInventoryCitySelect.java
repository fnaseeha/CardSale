/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

public class MerchantInventoryCitySelect extends Activity{
	AutoCompleteTextView atc_city;
	public DatabaseHandler dbh;
	Button btnSelect,btnProceed;
	ListView lv;
	
	final ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
	final ArrayList<HashMap<String, String>> listData1=new ArrayList<HashMap<String, String>>();
    final int[] columnIds = new int[] {R.id.lblMerchantName, R.id.lblAddress,R.id.lblMerchantId,R.id.lblMeasure};
    final String[] columnTags = new String[] {"Name", "Address", "Id","measure"};
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.merchantcityselect);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText = (TextView)findViewById(R.id.myTitle);
        if ( myTitleText != null ) {
            myTitleText.setText("Merchant Inventory By City");
        }
		btnSelect =(Button)findViewById(R.id.btnSelect);
		lv=(ListView)findViewById(R.id.lvMerSelect);
		dbh = new DatabaseHandler(getApplicationContext());

		ArrayList<String> list = dbh.getCities();
  		ArrayAdapter<String> adapter = new ArrayAdapter(MerchantInventoryCitySelect.this,android.R.layout.simple_dropdown_item_1line, list);
        atc_city = (AutoCompleteTextView)findViewById(R.id.atc_city_select);
        atc_city.setThreshold(1);
        atc_city.setAdapter(adapter);
        
        btnProceed=(Button)findViewById(R.id.btnMerSelProceed);
        btnProceed.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			atc_city = (AutoCompleteTextView)findViewById(R.id.atc_city_select);
			dbh = new DatabaseHandler(getApplicationContext());
		  	ArrayList<String> list = dbh.getCities();
			String auto_city=atc_city.getText().toString();
			if(!list.contains(auto_city)){
					AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MerchantInventoryCitySelect.this);
		    		dlgAlert.setMessage("City doesn't exists in the system");
		    		dlgAlert.setTitle("Alert");
		    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent intent1 = new Intent(MerchantInventoryCitySelect.this,MerchantInventoryCitySelect.class);
							startActivity(intent1);
		           		}
					});
		    		dlgAlert.setCancelable(true);
		    		dlgAlert.create().show();
				}
			  else{
				Intent intent = new Intent( MerchantInventoryCitySelect.this,MerchantInventoryCitySelectFinal.class);
				intent.putExtra("city", auto_city);
				startActivity(intent);
			  }
		}
		});
       }
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), home.class);
		startActivity(intent);
		finish(); 
        return;
	}
	
}   