package com.lk.lankabell.android.activity.tsr.activity;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.beans.Attendance;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.ISMSCallbacks;
import com.lk.lankabell.android.activity.tsr.tracking.SMSManager;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;
import com.lk.lankabell.android.activity.tsr.util.HTTPServiceCalls;

public class AddAttendanceActivity extends Activity implements ISMSCallbacks {

	private DatabaseHandler dbh;
	private ToggleButton swtInOut;
	private EditText txtTeamCode;
	private String strTeamCode;
	private Attendance attendace;
	private String msg;
	private SMSManager mSMS;
	private EditText txtBikeNo;
	private EditText txtBikeMeter;
	private String strBikeNo;
	private String strBikeMeter;
	private String msg_team;
	private String team_sms_id = "123456789";
	private LocationManager locationManager;

	boolean isFristLocationGot = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_attendance);

		mSMS = new SMSManager(this, this);
		dbh = new DatabaseHandler(getApplicationContext());
		swtInOut = (ToggleButton) findViewById(R.id.frg_atd_swtInOut);
		txtTeamCode = (EditText) findViewById(R.id.frg_atd_txtTeam);
		txtBikeNo = (EditText) findViewById(R.id.frg_atd_txtBike);
		txtBikeMeter = (EditText) findViewById(R.id.frg_atd_txtMeter);

		strTeamCode = getSharedPreferences("TSR_TEAM", MODE_PRIVATE).getString("team", "");
		txtTeamCode.setText(strTeamCode);
		strBikeNo = getSharedPreferences("TSR_TEAM", MODE_PRIVATE).getString("bike", "");
		txtBikeNo.setText(strBikeNo);
		strBikeMeter = getSharedPreferences("TSR_TEAM", MODE_PRIVATE).getString("meter", "");
		txtBikeMeter.setText(strBikeMeter);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 30);

		if (dbh.isAttendanceInMarked(null)) {
			swtInOut.setChecked(false);
			isFristLocationGot = true;
		} else {
			swtInOut.setChecked(true);
			// getNetWorkGPSLocation();
		}

		isFristLocationGot = true;

		chechBoth();

	}

	private void getNetWorkGPSLocation() {
		// locationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		// 0, 0, new TSRLocationListen());

	}

	private class TSRLocationListen implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			saveLcoation();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

	public void saveLcoation() {
		// TODO Auto-generated method stub

	}

	private void chechBoth() {
		if ((dbh.isAttendanceMarked("IN", new Date()) != null) && (dbh.isAttendanceMarked("OUT", new Date()) != null)) {
			Utils.ShowMsg(this, "You have marked both IN and OUT for today");
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			User user = dbh.getUserDetails();
			String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
			Attendance attendace = new Attendance(epfNo, new Date(), "", "", "0", 0, new Date());
			swtInOut.setChecked(!dbh.isAttendanceInMarked(attendace));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onClickSubmitAtd(View view) {

		try {
			String temp_strTeamCode = txtTeamCode.getText().toString().trim();
			String temp_strBikeNo = txtBikeNo.getText().toString().trim();
			String temp_strBikeMeter = txtBikeMeter.getText().toString().trim();

			if (temp_strTeamCode.trim().equals("") || temp_strTeamCode.trim().length() > 6) {
				Utils.ShowMsg(this, "Please enter valid team code");
				return;
			}
			if (temp_strBikeNo.trim().equals("") || temp_strTeamCode.trim().length() > 7) {
				Utils.ShowMsg(this, "Please enter valid bike number");
				return;
			}
			if (temp_strBikeMeter.trim().equals("")) {
				Utils.ShowMsg(this, "Please enter valid bike meter value");
				return;
			}

			if (strTeamCode.equals(temp_strTeamCode)) {

			} else {
				Editor editor = getSharedPreferences("TSR_TEAM", MODE_PRIVATE).edit();
				editor.putString("team", temp_strTeamCode);
				editor.commit();
				strTeamCode = temp_strTeamCode;
			}

			if (!strBikeNo.equals(temp_strBikeNo)) {
				Editor editor = getSharedPreferences("TSR_TEAM", MODE_PRIVATE).edit();
				editor.putString("bike", temp_strBikeNo);
				editor.commit();
				strBikeNo = temp_strBikeNo;
			}
			if (!strBikeMeter.equals(temp_strBikeMeter)) {
				Editor editor = getSharedPreferences("TSR_TEAM", MODE_PRIVATE).edit();
				editor.putString("meter", temp_strBikeMeter);
				editor.commit();
				strBikeMeter = temp_strBikeMeter;
			}

			if (!swtInOut.isChecked() && !dbh.isAttendanceInMarked(null)) {
				Utils.ShowMsg(this, "You should mark your IN first");
				return;
			}

			User user = dbh.getUserDetails();
			String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
			String strInOut = swtInOut.isChecked() ? "1" : "2";

			epfNo = epfNo.trim();

			msg = strTeamCode.trim() + " " + epfNo.trim() + " " + strInOut.trim();
			msg_team = strTeamCode + " " + strBikeNo + " " + strBikeMeter + " " + strInOut;
			String strType = swtInOut.isChecked() ? "IN" : "OUT";

			attendace = new Attendance(epfNo, new Date(), strType, "", "0", 0, new Date());

			attendace.setTeamCode(strTeamCode);
			attendace.setBikeNo(temp_strBikeNo);
			attendace.setOdometer(strBikeMeter);

			attendace.setSmsStatusCode(0);
			// implemented the code
			boolean isSuccess = false;
			boolean shouldConfirm = false;
			Attendance temp_Attendance = dbh.isAttendanceMarked(strType, attendace.getAttendaceDate());
			if (temp_Attendance == null) {
				isSuccess = dbh.saveAttendace(attendace);
			} else {
				attendace = temp_Attendance;
				shouldConfirm = true;
				confirmedToResend(temp_Attendance);
			}

			attendace.setBikeNo(temp_strBikeNo);
			attendace.setOdometer(strBikeMeter);

			Log.d("attemdace ", attendace.toString());
			 
			
			if (!shouldConfirm) {
				if (isSuccess) {
					sendMyAttendance();
				} else {
					Toast.makeText(this, "Attendace not submited. Please try again", Toast.LENGTH_LONG).show();
				}
			}
			if (swtInOut.isChecked()) {
				((TSRApplication) getApplication()).startTSRServices();
				((TSRApplication) getApplication()).startBeginReceiver();
			} else {
				((TSRApplication) getApplication()).stopServices();
			}

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "Error occured : " + e.getMessage(), Toast.LENGTH_LONG).show();
			Utils.writeToErrLogFileWithTime(e);
			e.printStackTrace();

		}

	}

	private void sendMyAttendance() {
		boolean isOnline = HTTPServiceCalls.isInternetAvailable(this);
		// isOnline = false ; //since the web service is not ready yet;
		if (isOnline) {
			try {
				Log.v("addtendace", attendace.toJsonRequest());
				AppAsyncTasks async = new AppAsyncTasks(this);
				// async.execute(new String[] { attendace.toJsonRequest() });
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] { attendace.toJsonRequest() });
				} else {
					async.execute(new String[] { attendace.toJsonRequest() });
				}
			} catch (JSONException e) {

				Utils.writeToErrLogFileWithTime(e);
				sendAttendanceSMS();
				sendTeamSMS();

			}
		} else {
			sendAttendanceSMS();
			sendTeamSMS();
		}

	}

	private void confirmedToResend(Attendance temp_Attendance) {
		// TODO Auto-generated method stub

		String status = "";
		if (temp_Attendance.getSmsStatusCode() == 1) {
			status = "pending";
		} else if (temp_Attendance.getSmsStatusCode() == 2) {
			status = "deliverded";
		} else {
			sendMyAttendance();
			return;
		}

		new AlertDialog.Builder(this).setTitle("Confirm").setMessage(String.format("Your attendance is in %s state, Are you sure you want to resend ?", status)).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendMyAttendance();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create().show();

	}

	private void sendTeamSMS() {
		// TODO Auto-generated method stub
		// send team attendance sms
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mSMS.sendSMS(CONSTANTS.ATTENDACE_MOBILE_NUMBER, msg_team, Integer.parseInt(team_sms_id));
	}

	@Override
	public void onSmsSent(String text) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Attendance SMS Sent", Toast.LENGTH_SHORT).show();
		if (!text.equals(team_sms_id)) {
			dbh.updateAttendaceSMSStatus(text, 1);
		}
	}

	@Override
	public void onSmsDelivered(String text) {
		// TODO Auto-generated method stub
		if (!text.equals(team_sms_id)) {
			dbh.updateAttendaceSMSStatus(text, 2);
		}
		Toast.makeText(this, "Attendance SMS Delivered", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSmsSendFail(String msgid, int failType) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Attendance SMS Send Failed", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSmsDeliveryFail(String msgid, int failType) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Attendance SMS Delivert Failed", Toast.LENGTH_LONG).show();
	}

	private class AppAsyncTasks extends AsyncTask<String, Void, String> {

		private Context context;
		private ProgressDialog pd;

		public AppAsyncTasks(Context context) {
			this.context = context;

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				return markAttendace(params[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Utils.writeToErrLogFileWithTime(e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String responce) {
			// TODO Auto-generated method stub
			super.onPostExecute(responce);
			pd.hide();

			if (responce == null) {
				sendAttendanceSMS();
				sendTeamSMS();
				return;
			}

			Log.v("Response", responce);

			try {
				JSONParser parser = new JSONParser();
				Object parsed = parser.parse(responce);
				JSONObject objJSonAtd = (JSONObject) parsed;

				JSONObject jResponse = (JSONObject) objJSonAtd.get("response");
				int status = ((Number) jResponse.get("status")).intValue();
				String msg = (String) jResponse.get("msg");

				if (status == 1) {
					updateAttendace();

					JSONObject jdata = (JSONObject) jResponse.get("data");
					int ind = ((Number) jdata.get("ind")).intValue();
					int team = ((Number) jdata.get("team")).intValue();

					if (ind == 0) {
						sendAttendanceSMS();
					}
					if (team == 0) {
						sendTeamSMS();
					}

					if (msg == null) {

						Toast.makeText(this.context, "Attendance marked.", Toast.LENGTH_LONG).show();

					} else {
						Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show();
						// Toast.makeText(this.context, "Attendance marked.",
						// Toast.LENGTH_LONG).show();
					}

				} else {
					Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show();
					sendAttendanceSMS();
					sendTeamSMS();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setTitle("Updating Attendance");
			pd.setMessage("Please wait..");
			// pd.setCancelable(false);
			pd.show();
		}

	}

	public void sendAttendanceSMS() {

		Toast.makeText(this, "Tying to send sms", Toast.LENGTH_LONG).show();
		// send individual attendance sms
		mSMS.sendSMS(CONSTANTS.ATTENDACE_MOBILE_NUMBER, msg, attendace.getId());

	}

	public void updateAttendace() {
		dbh.updateAttendaceSMSStatus("" + attendace.getId(), 2);
	}

	public String markAttendace(String responce) throws Exception {
		String result = "";
		Log.v("request", responce);
		HTTPServiceCalls calles = new HTTPServiceCalls();
		try {
			result = calles.makeRequest(CONSTANTS.getAttendanceUrl(this), responce);

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!isFristLocationGot) {
			Utils.ShowMsg(this, "Please wait till you location saved..");
			return;
		}
		super.onBackPressed();

	}

	public void onClickCheckAttedndance(View view) {
		User user = dbh.getUserDetails();
		String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
		(new AsyncCheckAttendance(this)).execute(new String[] { epfNo });
	}

	private class AsyncCheckAttendance extends AsyncTask<String, Void, String> {

		Context context;
		private ProgressDialog pg;

		public AsyncCheckAttendance(Context context) {
			super();
			this.context = context;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			HTTPServiceCalls calles = new HTTPServiceCalls();
			try {
				String url = CONSTANTS.getAttendanceCheckUrl(getBaseContext());
				result = calles.makeRequest(url, new String[] { "epf" }, params);

			} catch (Exception e) {
				e.printStackTrace();
				Utils.writeToErrLogFileWithTime(e);
				result = "{\"response\":{\"status\":0,\"data\":null,\"msg\":\"Error while requesting data\"}}";
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pg.cancel();

			viewCheckAttendanceData(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pg = ProgressDialog.show(context, "TSR Automation", "Checking attendace. Please wait");
			pg.setCancelable(true);

		}

	}

	public void viewCheckAttendanceData(String result) {
		// TODO Auto-generated method stub

		TextView tvIn = (TextView) findViewById(R.id.frg_atd_in_text);
		TextView tvOut = (TextView) findViewById(R.id.frg_atd_out_text);

		try {
			JSONParser parser = new JSONParser();
			Object parsed = parser.parse(result);
			JSONObject objJSonAtd = (JSONObject) parsed;

			JSONObject jResponse = (JSONObject) objJSonAtd.get("response");

			int status = ((Number) jResponse.get("status")).intValue();

			if (status == 1) {
				JSONArray data = (JSONArray) jResponse.get("data");
				if (data.size() == 1) {
					JSONObject obj1 = (JSONObject) data.get(0);
					String date = (String) obj1.get("date");
					if ("IN".equalsIgnoreCase((String) obj1.get("type"))) {
						tvIn.setText("IN marked at : " + date);
						dbh.updateServerTodayAttendance("IN", date);
					} else {
						tvOut.setText("Out marked at : " + date);
						// dbh.updateServerTodayAttendance("OUT",date);
					}
				} else if (data.size() == 2) {
					for (int a = 0; a < 2; a++) {
						JSONObject obj1 = (JSONObject) data.get(a);
						String date = (String) obj1.get("date");
						if ("IN".equalsIgnoreCase((String) obj1.get("type"))) {
							tvIn.setText("IN marked at : " + date);
							try {
								dbh.updateServerTodayAttendance("IN", date);
							} catch (Exception e) {
								// TODO: handle exception
							}
						} else {
							tvOut.setText("Out marked at : " + date);
							// dbh.updateServerTodayAttendance("OUT",date);
						}
					}
				}

			} else {
				Utils.ShowMsg(this, (String) jResponse.get("msg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
