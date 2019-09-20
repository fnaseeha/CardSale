package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.beans.SynchReport;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sync.SynchReportAdapter;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.R;

public class MySynchReports extends Activity {
	private Spinner selectWiseSpinner;
	private ListView listView;
	private TextView tvSyncTime;
	
	

	String epfNo="";
	String detOrsum="";
	Date fromDate = new Date();
	Date toDate = new Date();
	String SyncTimeVal = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mysynchreports);
		tvSyncTime = (TextView)findViewById(R.id.my_sync_report_time);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText=(TextView)findViewById(R.id.myTitle);
		if(myTitleText!=null){
			myTitleText.setText("My Sync Reports");
		}
		listView=(ListView) findViewById(R.id.synchreportlistView);
		addItemsOnSpinner1();
		setLastSyncTime();
	}

	
	private void setLastSyncTime() {
		// TODO Auto-generated method stub
		final DatabaseHandler dbh = new DatabaseHandler(this);
		try {
			String last_sync_at = dbh.getLastSyncTime();
			String f_date =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(last_sync_at));
			tvSyncTime.setText(String.format("Last updated at : %s", f_date));
		} catch (Exception e) {
			 Utils.ShowMsg(this, "Error : " + e.getLocalizedMessage());
		}
	}
	


	public void seeSynchView(View view){
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		selectWiseSpinner = (Spinner) findViewById(R.id.spinner1);
		//Date,component,percentage [synch count/total count]
		//1.Sales		-	Sales header,sales details
		//2.Merchants	-	Create Merchants/ Assigned Merchants/ Register Merchants
		//3.Cards		-	Card Acceptance,Card Serials
			ArrayList<SynchReport> Report = dbh.viewSynchReport(selectWiseSpinner.getSelectedItem().toString());
			SynchReportAdapter adapter = new SynchReportAdapter(MySynchReports.this, Report);
			listView.setAdapter(adapter);
		/*
		if (selectWiseSpinner.getSelectedItem().equals("Sales Synch")) {
			ArrayList<SalesSynchReport> salesReport =dbh.getSalesSynchReport();
			SalesSynchReportAdapter adapter = new SalesSynchReportAdapter(MySynchReports.this, salesReport);
			listView.setAdapter(adapter);
		} else if (selectWiseSpinner.getSelectedItem().equals("Merchants Synch")) {
			ArrayList<SynchReport> salesReport =dbh.getMerchantsSynchReport();
			SynchReportAdapter adapter = new SynchReportAdapter(MySynchReports.this, salesReport);
			listView.setAdapter(adapter);
		}else if (selectWiseSpinner.getSelectedItem().equals("Cards Synch")) {
			ArrayList<SynchReport> salesReport =dbh.getCardsSynchReport();
			SynchReportAdapter adapter = new SynchReportAdapter(MySynchReports.this, salesReport);
			listView.setAdapter(adapter);
		} */
	}

	private void addItemsOnSpinner1() {
		selectWiseSpinner = (Spinner) findViewById(R.id.spinner1);
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		List<String> list = new ArrayList<String>();
		list = dbh.loadSynchReportDropdown();
		
		//list.add("Merchants Synch");
		//list.add("Sales Synch");
		//list.add("Cards Synch");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectWiseSpinner.setAdapter(dataAdapter);
	}

}
