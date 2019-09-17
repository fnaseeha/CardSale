/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.adapters.MainMenuAdapter;
import com.lk.lankabell.android.activity.tsr.beans.MainMenuItem;
import com.lk.lankabell.android.activity.tsr.bg.GeoListener;
import com.lk.lankabell.android.activity.tsr.models.SharedPrefManager;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;
import com.lk.lankabell.android.activity.tsr.util.HTTPServiceCalls;

/**
 * @author
 * 
 *         This class is used to display the Main menu.
 * 
 */
public class SelectorActivity extends Activity {

	private static ProgressDialog progressDialog;
	private static final String TAG = SelectorActivity.class.getSimpleName();

	double latitudeValue;
	double logitudeValue;

	ImageButton button;

	// =============================================================================================================================
	private Handler mTimer3 = new Handler();
//	private Runnable mTask3 = new Runnable() {
//		public void run() {
//			CountDownTimer timer = new CountDownTimer(60 * 60 * 1000, 1000) { // 1
//																				// hour
//																				// last
//																				// =
//																				// 15
//
//				public void onTick(long millisUntilFinished) {
//					Log.w("Seconds remaining: ", String.valueOf(millisUntilFinished / 1000));
//				}
//
//				public void onFinish() {
//					/*
//					 * Intent intent = new Intent(Intent.ACTION_MAIN);
//					 * intent.addCategory(Intent.CATEGORY_HOME);
//					 * intent.setFlags(
//					 * Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
//					 * startActivity(intent); finish(); System.exit(0);
//					 */
//
//					Intent intent = new Intent(getApplicationContext(), LoginPage.class);
//					startActivity(intent);
//
//				}
//			};
//			timer.start();
//			mTimer3.postDelayed(this, interval3 * 1000L);
//		}
//	};

	private int interval3 = 15 * 60; // 60 seconds
	private TSRApplication tsrApp;

	@Override
	protected void onDestroy() {
//		if (mTimer3 != null) {
//			mTimer3.removeCallbacks(mTask3); // cancel the timer
//		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {

		Builder inputDialog = new AlertDialog.Builder(this);
		inputDialog.setMessage("Can't back the System. Please exit the System.");
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

		setContentView(R.layout.activity_main_selector);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		tsrApp = (TSRApplication) getApplication();
		tsrApp.startBeginReceiver();
		tsrApp.startTSRServices();

//		mTimer3.postDelayed(mTask3, interval3 * 1000L); // start the timer for
														// the first time
		Log.d("=================", "=======================================");
		startService(new Intent(this, BackgroundService.class)); // start
																	// background
																	// webservices
		Log.d("================", "==1111111========================================");
		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setText("Home");
		}

		setMainMenu();

		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));

		(new SystemTimesAsyc(this)).execute(new String[]{""});

	}

	BroadcastReceiver mybroadcast = new BroadcastReceiver() {

		// When Event is published, onReceive method is called
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("[BroadcastReceiver]", "MyReceiver");

			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				tsrApp.startBeginReceiver();
				tsrApp.startTSRServices();
				Utils.writeToLogFileWithTime("[BroadcastReceiver Screen ON");
				Log.i("[BroadcastReceiver]", "Screen ON");
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			}

		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		tsrApp.startBeginReceiver();
		tsrApp.startTSRServices();

		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));

	}

	private void setMainMenu() {
		// TODO Auto-generated method stub
		ArrayList<MainMenuItem> menuitems = new ArrayList<MainMenuItem>();
		MainMenuItem itemMerchents = new MainMenuItem(R.drawable.merchant_icon, getResources().getString(R.string.merchants));
		itemMerchents.setOnclick(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickMerchantNew(arg0);
			}
		});

		menuitems.add(itemMerchents);

		MainMenuItem itemCards = new MainMenuItem(R.drawable.card_money_icon, getResources().getString(R.string.cards));
		itemCards.setOnclick(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onclickCards(arg0);
			}
		});

		menuitems.add(itemCards);

		MainMenuItem itemMyMerchants = new MainMenuItem(R.drawable.fault_ticketicon, getResources().getString(R.string.my_merchants));
		itemMyMerchants.setOnclick(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickFaultTicket(arg0);
			}
		});

		menuitems.add(itemMyMerchants);

		MainMenuItem itemMyCards = new MainMenuItem(R.drawable.phone_device_icon, getResources().getString(R.string.my_cards));
		itemMyCards.setOnclick(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickRegister(arg0);
			}
		});

		menuitems.add(itemMyCards);

		MainMenuItem itemUpdates = new MainMenuItem(R.drawable.settings, getResources().getString(R.string.updates));
		itemUpdates.setOnclick(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickUpdates(arg0);
			}
		});

		menuitems.add(itemUpdates);

		MainMenuItem itemMySync = new MainMenuItem(R.drawable.synch, getResources().getString(R.string.my_synch));
		itemMySync.setOnclick(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickMySynch(arg0);
			}
		});

		menuitems.add(itemMySync);

		MainMenuItem itemAttendance = new MainMenuItem(R.drawable.attendance, getResources().getString(R.string.attendance));
		itemAttendance.setOnclick(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickOpenAttendance(arg0);
			}
		});

		menuitems.add(itemAttendance);

		MainMenuItem itemLogout = new MainMenuItem(R.drawable.logout_icon_final, getResources().getString(R.string.logout));
		itemLogout.setOnclick(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickExit(arg0);
			}
		});

		menuitems.add(itemLogout);

		MainMenuAdapter adpMainMenu = new MainMenuAdapter(getApplicationContext(), menuitems);
		GridView grid = (GridView) findViewById(R.id.gridview);
		grid.setAdapter(adpMainMenu);

	}

	protected void onClickOpenAttendance(View arg0) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(), AddAttendanceActivity.class);
		startActivity(i);
	}

	/**
	 * Geeting GPS coordinates function.
	 */
	public void getGps() {

		GeoListener geo = new GeoListener(getApplicationContext());
		Log.i(TAG, "1>" + GeoListener.isAcquireSuccess());
		geo.requestUpdates();

		progressDialog = ProgressDialog.show(SelectorActivity.this, "", "Please wait..acquiring location informations");
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

				CONSTANTS.SHARED_LOGOUT ="1";

				SharedPrefManager.setLocalSharedPref(SelectorActivity.this,CONSTANTS.SHARED_LOGOUT,"1");

				//overridePendingTransition(R.anim.left_in, R.anim.slide_to_right);
				finish();
				System.exit(0);


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


		//
//
//		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//
//// ... code for customizing the buttons and title
//		LayoutInflater inflater = this.getLayoutInflater();
//		final View dialogView = inflater.inflate(R.layout.custom_msg_dialog, null);
//		dialogBuilder.setView(dialogView);
//		TextView title = (TextView) dialogView.findViewById(R.id.title);
//		Button okay = (Button) dialogView.findViewById(R.id.btn_yes);
//		Button no = (Button) dialogView.findViewById(R.id.btn_no);
//
//		title.setText("Do you want to exit?");
//		okay.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//				System.exit(0);
//				Intent intent = new Intent();
//				intent.setAction(Intent.ACTION_MAIN);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				startActivity(intent);
//			}
//		});
//
//		no.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialogView.dismiss();
//			}
//		});
//		AlertDialog alertDialog = dialogBuilder.create();
//		alertDialog.show();


//		Builder inputDialog = new AlertDialog.Builder(this);
//		inputDialog.setMessage("Do you want to exit?");
//		inputDialog.setPositiveButton("Yes", new OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//
//				finish();
//				System.exit(0);
//				Intent intent = new Intent();
//				intent.setAction(Intent.ACTION_MAIN);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				startActivity(intent);
//
//
//			}
//		});
//		inputDialog.setNegativeButton("No", new OnClickListener() {
//
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
//		inputDialog.show();
	}

	public void onClickVisit(View view) {
		int finalResponse = CheckTsrDataLOG();
		if (finalResponse == 0) {

			AbsoluteLayout vwParentRow = (AbsoluteLayout) view.getParent();
			TextView merchantname = (TextView) vwParentRow.getChildAt(0);
			TextView merchantid = (TextView) vwParentRow.getChildAt(1);

			String name = merchantname.getText().toString();
			String mid = merchantid.getText().toString();

			Intent intent = new Intent(getApplicationContext(), SubMenuActivity.class);
			intent.putExtra("merchantID", mid);
			intent.putExtra("merchantName", name);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(SelectorActivity.this).setTitle("Reset info").setMessage("Your mobile Data reset is not Done. Please reset again.").setPositiveButton("Ok", null).show();
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
			new AlertDialog.Builder(SelectorActivity.this).setTitle("Reset info").setMessage("Your mobile Data reset is not Done. Please reset again.").setPositiveButton("Ok", null).show();
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
			Intent intent = new Intent(getApplicationContext(), MerchantJobs.class);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(SelectorActivity.this).setTitle("Reset info").setMessage("Your mobile Data reset is not Done. Please reset again.").setPositiveButton("Ok", null).show();
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
		Intent intent = new Intent(getApplicationContext(), MySynchReports.class);
		startActivity(intent);
	}

	public void onClickFaultTicket(View view) {
		int finalResponse = CheckTsrDataLOG();
		if (finalResponse == 0) {
			Intent intent = new Intent(getApplicationContext(), MerchantViewer.class);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(SelectorActivity.this).setTitle("Reset info").setMessage("Your mobile Data reset is not Done. Please reset again.").setPositiveButton("Ok", null).show();
		}
	}

	public void onClickRegister(View view) {
		int finalResponse = CheckTsrDataLOG();
		if (finalResponse == 0) {
			Intent intent = new Intent(getApplicationContext(), CardDetails.class);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(SelectorActivity.this).setTitle("Reset info").setMessage("Your mobile Data reset is not Done. Please reset again.").setPositiveButton("Ok", null).show();
		}
	}

	private int CheckTsrDataLOG() {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		int finalResponse = dbh.getFinalResetData();
		return finalResponse;
	}

	// this will only update the server times for gps get and update 
	private class SystemTimesAsyc extends AsyncTask<String, Void, String> {

		private Context context;
		private HTTPServiceCalls http;
//		private ProgressDialog pg;

		public SystemTimesAsyc(Context context) {
			super();
			this.context = context;
			http = new HTTPServiceCalls();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			pg = ProgressDialog.show(this.context, "Please wait", "Updating Server url");
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			DatabaseHandler dbh = new DatabaseHandler(this.context);
			try {
				String epf = dbh.GetEmpNoByPhoneNo(Utils.getSimSerialNumber(this.context));
				System.out.println("* epf "+epf);
				if (!epf.equals("")) {
					String response = http.makeRequest(CONSTANTS.GET_SERVICE_TIMES, new String[]{"epf"}, new String[]{epf} );
					return response;
				}
			} catch (Exception e) {
				Utils.writeToErrLogFileWithTime(e);
			} finally {
				dbh.close();
			}
			return "{\"response\":{\"status\":0,\"data\":null,\"msg\":\"Error while requesting data\"}}";
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
//			pg.cancel();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			DatabaseHandler dbh = new DatabaseHandler(this.context);
//			pg.cancel();
			super.onPostExecute(result);
			try {
				JSONParser parser = new JSONParser();
				Object parsed = parser.parse(result);
				JSONObject objJSonAtd = (JSONObject) parsed;

				JSONObject jResponse = (JSONObject) objJSonAtd.get("response");

				int status = ((Number) jResponse.get("status")).intValue();

				if (status == 1) {
					JSONObject data = (JSONObject)jResponse.get("data");
					 setTimes(data);
				}

			} catch (Exception e) {
				Utils.writeToErrLogFileWithTime(e);
			} finally {
				dbh.close();
			}
		}

	}
	public void setTimes(JSONObject data) {
		int gps_time = Integer.parseInt((String) data.get("gpsGet"));
		int gps_update_time = Integer.parseInt((String) data.get("gpsUpdate"));
		 
		SharedPrefManager.setSharePref(this, CONSTANTS.SHARED_UPDATE_TIME_SYNC, gps_update_time );
		SharedPrefManager.setSharePref(this, CONSTANTS.SHARED_UPDATE_TIME_GPS,gps_time );
		
		((TSRApplication)getApplication()).startTSRServices();
	}

}
