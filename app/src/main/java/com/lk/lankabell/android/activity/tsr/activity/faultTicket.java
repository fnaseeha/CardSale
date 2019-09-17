package com.lk.lankabell.android.activity.tsr.activity;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.FaultTickets;
import com.lk.lankabell.android.activity.tsr.sqlite.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class faultTicket extends Activity {
	
	private DatabaseHandler dbh;
	private EditText txtteamCode = null;
	private EditText txtfaultId = null;
	private EditText txtactionTakenCode = null;
	private EditText txtsignalStrength = null;
	private EditText txtmeterReading = null;
	private EditText txtvehicleNo = null;
	
	int FaultTicketId;
	String TeamCode;
	String faultId;
	String actionTakenCode;
	String signalStrength;
	String meterReading;
	String vehicleNo;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.faultticket);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		TextView myTitleText=(TextView)findViewById(R.id.myTitle);
		myTitleText.setText("Fault Ticket");
		
		DatabaseHandler db  = new DatabaseHandler(getApplicationContext());
		User user = db.getUserDetails();
		
		TextView lblUserName = (TextView)findViewById(R.id.lblUserName);
		lblUserName.setText(user.getUserName());
	}

	public void onClickSave(View view) {
		
		txtteamCode = (EditText)findViewById(R.id.txtTeamCode);
		txtfaultId = (EditText)findViewById(R.id.txtFaultId);
		txtactionTakenCode = (EditText)findViewById(R.id.txtActionTakenCode);
		txtsignalStrength = (EditText)findViewById(R.id.txtSignalStrength);
		txtmeterReading = (EditText)findViewById(R.id.txtMeterReading);
		txtvehicleNo = (EditText)findViewById(R.id.txtVehicleNo);
		
		FaultTicketId = GenerateFaultId();
		TeamCode  = txtteamCode.getText().toString();
		faultId  = txtfaultId.getText().toString();
		actionTakenCode  = txtactionTakenCode.getText().toString();
		signalStrength  = txtsignalStrength.getText().toString();
		meterReading  = txtmeterReading.getText().toString();
		vehicleNo  = txtvehicleNo.getText().toString();
		
		if(TeamCode.equalsIgnoreCase("")|| faultId.equalsIgnoreCase("") || actionTakenCode.equalsIgnoreCase("") || signalStrength.equalsIgnoreCase("") || meterReading.equalsIgnoreCase("") || vehicleNo.equalsIgnoreCase(""))
		{

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Message");
			alertDialog.setMessage("All fields are mandatory");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int which) {
				      // here you can add functions

				   }
				});
				alertDialog.setIcon(R.drawable.ic_alert);
				alertDialog.show();

		}
		else
		{
		FaultTickets faultTickets = new FaultTickets(TeamCode, faultId, actionTakenCode, Integer.parseInt(signalStrength), Integer.parseInt(meterReading), vehicleNo);
		dbh =new DatabaseHandler(getApplicationContext());
		dbh.SavefaultTicket(faultTickets, FaultTicketId);
		
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Message");
		alertDialog.setMessage("Saved Successfully.");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      // here you can add functions
				   Intent intent = new Intent(faultTicket.this,
							SelectorActivity.class);
					startActivity(intent);
			   }
			});
			alertDialog.setIcon(R.drawable.ic_alert);
			alertDialog.show();
		}
		
	}
	
	public void onClickMaterialEntry(View view) {
		
		txtteamCode = (EditText)findViewById(R.id.txtTeamCode);
		txtfaultId = (EditText)findViewById(R.id.txtFaultId);
		txtactionTakenCode = (EditText)findViewById(R.id.txtActionTakenCode);
		txtsignalStrength = (EditText)findViewById(R.id.txtSignalStrength);
		txtmeterReading = (EditText)findViewById(R.id.txtMeterReading);
		txtvehicleNo = (EditText)findViewById(R.id.txtVehicleNo);
		
		FaultTicketId = GenerateFaultId();
		TeamCode  = txtteamCode.getText().toString();
		faultId  = txtfaultId.getText().toString();
		actionTakenCode  = txtactionTakenCode.getText().toString();
		signalStrength  = txtsignalStrength.getText().toString();
		meterReading  = txtmeterReading.getText().toString();
		vehicleNo  = txtvehicleNo.getText().toString();
		
		if(TeamCode.equalsIgnoreCase("")|| faultId.equalsIgnoreCase("") || actionTakenCode.equalsIgnoreCase("") || signalStrength.equalsIgnoreCase("") || meterReading.equalsIgnoreCase("") || vehicleNo.equalsIgnoreCase(""))
		{
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Message");
			alertDialog.setMessage("All fields are mandatory");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int which) {
				      // here you can add functions

				   }
				});
				alertDialog.setIcon(R.drawable.ic_alert);
				alertDialog.show();
		}
		else
		{
		FaultTickets faultTickets = new FaultTickets(TeamCode, faultId, actionTakenCode, Integer.parseInt(signalStrength), Integer.parseInt(meterReading), vehicleNo);
		dbh =new DatabaseHandler(getApplicationContext());
		dbh.SavefaultTicket(faultTickets, FaultTicketId);
		
		Intent intent = new Intent(faultTicket.this,faultMaterial.class);
		intent.putExtra("FaultTicketId", Integer.toString(FaultTicketId));
		startActivity(intent);
		
		}
	}
	
	public void OnclickBack(View view) {
		Intent intent = new Intent(faultTicket.this,
				SelectorActivity.class);
		startActivity(intent);
	}
	
	public int GenerateFaultId() {
		dbh = new DatabaseHandler(getApplicationContext());
		int id;
		int maxId = dbh.GetMaxFaultTicketId();
		id = maxId +1;
		return id;
	}
}
