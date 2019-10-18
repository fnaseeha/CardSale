/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.bg.GeoListener;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

/**
 * @author
 * 
 *         This class is used to display the Main menu.
 * 
 */
public class SelectorActivity_copy extends Activity {

	private static ProgressDialog progressDialog;
	private static final String TAG = SelectorActivity_copy.class.getSimpleName();

	double latitudeValue;
	double logitudeValue;

	ImageButton button;

	// =============================================================================================================================
	private Handler mTimer3 = new Handler();
	private Runnable mTask3 = new Runnable() {
		public void run() {
			CountDownTimer timer = new CountDownTimer(60 * 60 * 1000, 1000) { // 1
																				// hour
																				// last
																				// =
																				// 15

				public void onTick(long millisUntilFinished) {
					Log.w("Seconds remaining: ",
							String.valueOf(millisUntilFinished / 1000));
				}

				public void onFinish() {
					/*
					 * Intent intent = new Intent(Intent.ACTION_MAIN);
					 * intent.addCategory(Intent.CATEGORY_HOME);
					 * intent.setFlags(
					 * Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
					 * startActivity(intent); finish(); System.exit(0);
					 */

					Intent intent = new Intent(getApplicationContext(),
							LoginPage.class);
					startActivity(intent);

				}
			};
			timer.start();
			mTimer3.postDelayed(this, interval3 * 1000L);
		}
	};

	private int interval3 = 15 * 60; // 60 seconds

	@Override
	protected void onDestroy() {
		if (mTimer3 != null) {
			mTimer3.removeCallbacks(mTask3); // cancel the timer
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {

		Builder inputDialog = new AlertDialog.Builder(this);
		inputDialog
				.setMessage("Can't back the System. Please exit the System.");
		inputDialog.setNegativeButton("Ok", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		inputDialog.show();
		return;
	}

	// =============================================================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.location_new);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);
		
		TSRApplication tsrApp = (TSRApplication)getApplication();
		tsrApp.startTSRServices();
		
		mTimer3.postDelayed(mTask3, interval3 * 1000L); // start the timer for
														// the first time
		Log.d("==================================================",
				"=======================================================");
		startService(new Intent(this, BackgroundService.class)); // start
																	// background
																	// webservices
		Log.d("==================================================",
				"==1111111=====================================================");
		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setText("Home");
		}
		final TextView appversion = findViewById(R.id.appversion);
		DatabaseHandler dbh=  new DatabaseHandler(getApplicationContext());
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}
		
		 
		
	}

	/**
	 * Geeting GPS coordinates function.
	 */
	public void getGps() {

		GeoListener geo = new GeoListener(getApplicationContext());
		Log.i(TAG, "1>" + GeoListener.isAcquireSuccess());
		geo.requestUpdates();

		progressDialog = ProgressDialog.show(SelectorActivity_copy.this, "",
				"Please wait..acquiring location informations");
	}

	/**
	 * @param view
	 * 
	 *            Logout function.
	 */
	public void onClickExit(View view) {


		LayoutInflater factory = LayoutInflater.from(this);
		final View dialogBuilder = factory.inflate(R.layout.custom_msg_dialog, null);
		final AlertDialog dialogView = new AlertDialog.Builder(this).create();
		dialogView.setView(dialogBuilder);
		TextView title = (TextView) dialogBuilder.findViewById(R.id.title);
		title.setText("Do you want to exit?");
		dialogBuilder.findViewById(R.id.btn_yes).setOnClickListener(new  View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				System.exit(0);
//				Intent intent = new Intent();
//				intent.setAction(Intent.ACTION_MAIN);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				startActivity(intent);
			}
		});
		dialogBuilder.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogView.dismiss();
			}
		});
		dialogView.show();
		Rect displayRectangle = new Rect();
		Window window = this.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		dialogView.getWindow().setLayout((int) (displayRectangle.width() *
				0.8f), dialogView.getWindow().getAttributes().height);

	}

	public void onClickVisit(View view) {
		int finalResponse = CheckTsrDataLOG();
		if (finalResponse == 0) {

			AbsoluteLayout vwParentRow = (AbsoluteLayout) view.getParent();
			TextView merchantname = (TextView) vwParentRow.getChildAt(0);
			TextView merchantid = (TextView) vwParentRow.getChildAt(1);

			String name = merchantname.getText().toString();
			String mid = merchantid.getText().toString();

			Intent intent = new Intent(getApplicationContext(),
					SubMenuActivity.class);
			intent.putExtra("merchantID", mid);
			intent.putExtra("merchantName", name);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(SelectorActivity_copy.this)
					.setTitle("Reset info")
					.setMessage(
							"Your mobile Data reset is not Done. Please reset again.")
					.setPositiveButton("Ok", null).show();
		}
	}

	/**
	 * @param view
	 * 
	 *            Navigate to the Card Sale Main menu page function.
	 */
	public void onclickCards(View view) {
		int finalResponse = CheckTsrDataLOG();
		if (finalResponse == 0) {
			Intent intent = new Intent(getApplicationContext(), home.class);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(SelectorActivity_copy.this)
					.setTitle("Reset info")
					.setMessage(
							"Your mobile Data reset is not Done. Please reset again.")
					.setPositiveButton("Ok", null).show();
		}
	}

	/**
	 * @param view
	 * 
	 *            Navigate to Merchant main menu function.
	 */
	public void onClickMerchantNew(View view) { // select mysales button
		int finalResponse = CheckTsrDataLOG();
		if (finalResponse == 0) {
			Intent intent = new Intent(getApplicationContext(),
					MerchantJobs.class);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(SelectorActivity_copy.this)
					.setTitle("Reset info")
					.setMessage(
							"Your mobile Data reset is not Done. Please reset again.")
					.setPositiveButton("Ok", null).show();
		}
	}

	/**
	 * @param view
	 * 
	 *            Navigate to Updates page function.
	 */
	public void onClickUpdates(View view) {
		Intent intent = new Intent(getApplicationContext(), Updates.class);
		startActivity(intent);
	}

	public void onClickMySynch(View view) {
		Intent intent = new Intent(getApplicationContext(),
				MySynchReports.class);
		startActivity(intent);
	}

	public void onClickFaultTicket(View view) {
		int finalResponse = CheckTsrDataLOG();
		if (finalResponse == 0) {
			Intent intent = new Intent(getApplicationContext(),
					MerchantViewer.class);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(SelectorActivity_copy.this)
					.setTitle("Reset info")
					.setMessage(
							"Your mobile Data reset is not Done. Please reset again.")
					.setPositiveButton("Ok", null).show();
		}
	}

	public void onClickRegister(View view) {
		int finalResponse = CheckTsrDataLOG();
		if (finalResponse == 0) {
			Intent intent = new Intent(getApplicationContext(),
					CardDetails.class);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(SelectorActivity_copy.this)
					.setTitle("Reset info")
					.setMessage(
							"Your mobile Data reset is not Done. Please reset again.")
					.setPositiveButton("Ok", null).show();
		}
	}

	private int CheckTsrDataLOG() {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		int finalResponse = dbh.getFinalResetData();
		return finalResponse;
	}

}
