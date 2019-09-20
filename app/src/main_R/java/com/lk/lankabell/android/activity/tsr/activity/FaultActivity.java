package com.lk.lankabell.android.activity.tsr.activity;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FaultActivity extends Activity {

	EditText editMeter = null;
	EditText editActionCode = null;
	InputMethodManager mgr = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.faultticket);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);

		TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		myTitleText.setText("Fault Ticket");

	}

	public void faultOnClickBack(View view) {

		Intent i = new Intent(this.getApplicationContext(),
				MainRadioActivity.class);
		startActivity(i);
		finish();
	}

	public void saveButtonClickHandler(View view) {

		String actionCode = editActionCode.getText().toString();

		String meter = editMeter.getText().toString();

		Toast.makeText(FaultActivity.this, "All fields are mandatory",Toast.LENGTH_SHORT).show();

		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

		Builder inputDialog = new AlertDialog.Builder(this);
		inputDialog.setTitle("Saving....");
		inputDialog.setMessage("Do you want to save?");
		inputDialog.setPositiveButton("Yes", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				/*
				 * Intent intent = new Intent (getApplicationContext(),
				 * MeterialEntryActivity.class); startActivity(intent);
				 * finish();
				 */

				// addMaterialInfo();
			}
		});
		inputDialog.setNegativeButton("No", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				/*
				 * Intent intent = new Intent (getApplicationContext(),
				 * MainRadioActivity.class); startActivity(intent); finish();
				 */

			}
		});
		inputDialog.show();
	}

	// }

	public void onClickMaterialEntry(View view) {

		Builder inputDialog = new AlertDialog.Builder(this);
		inputDialog
				.setMessage("Do you want to enter the material information?");
		inputDialog.setPositiveButton("Yes", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				/*
				 * Intent intent = new Intent(getApplicationContext(),
				 * MeterialEntryActivity.class); startActivity(intent);
				 * finish();
				 */
			}
		});
		inputDialog.setNegativeButton("No", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				Intent intent = new Intent(getApplicationContext(),
						FaultActivity.class);
				startActivity(intent);
				finish();

			}
		});
		inputDialog.show();
	}

	public void onClickSave(View view) {

		EditText txtteamCode = (EditText) findViewById(R.id.txtTeamCode);
		String teamCode = txtteamCode.getText().toString();

		EditText txtfaultCode1 = (EditText) findViewById(R.id.txtFaultId);
		EditText txtactionTakenCode = (EditText) findViewById(R.id.txtActionTakenCode);
		EditText txtsignalStrength = (EditText) findViewById(R.id.txtSignalStrength);
		// EditText txtmeterReading =
		// (EditText)findViewById(R.id.txtmeterReading);
		EditText txtVehicleNo = (EditText) findViewById(R.id.txtVehicleNo);

		String faultCode = txtfaultCode1.getText().toString();
		String actionTakenCode = txtactionTakenCode.getText().toString();
		String signalStrength = txtsignalStrength.getText().toString();
		// int meterReading =
		// Integer.parseInt(txtmeterReading.getText().toString());
		String vehicleNo = txtVehicleNo.getText().toString();

		/*
		 * if(teamCode.equalsIgnoreCase("") || faultCode.equalsIgnoreCase("") ||
		 * actionTakenCode
		 * .equalsIgnoreCase("")||signalStrength.equalsIgnoreCase("")||
		 * vehicleNo.equalsIgnoreCase("") ||
		 * Integer.toString(meterReading).equalsIgnoreCase(""))
		 */
		{
			// Toast.makeText(FaultActivity.this,
			// "All fields are mandatory", Toast.LENGTH_SHORT)
			// .show();
		}

	}
}
