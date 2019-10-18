package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.google.gson.Gson;
import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.beans.CardReturnsData;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Approved_card_returns extends Activity {
	ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
	int[] columnIds = new int[] {R.id.card_return_deno, R.id.card_return_bulkCode,R.id.card_return_startserial,R.id.card_return_endserial, R.id.card_return_numberofcards, R.id.card_return_type};
	String[] columnTags = new String[] {"denomination","bulkCode","Start_Serial","End_Serial","NO_OF_CARDS","Type"};
	private DatabaseHandler dbh;
	TextView error;
	EditText SecurityCode;
	private String URL = "";
	private static String NAMESPACE = "http://mainService";
	private static String METHOD_NAME = "ReturnCardsConfirm";
	private static String SOAP_ACTION = "http://mainService/ReturnCardsConfirm";

	SoapSerializationEnvelope envelope;
	SoapObject result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_approved_card_returns);
		
		URL = WsdlReader.getServiceUrl(true,this);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText=(TextView)findViewById(R.id.myTitle);
		if(myTitleText!=null) {
			myTitleText.setText("Cards Return Approved");
		}
		final TextView appversion = findViewById(R.id.appversion);

		dbh = new DatabaseHandler(getApplicationContext());
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}
		error = (TextView) findViewById(R.id.error_message);
		error.setText("");
		SecurityCode = (EditText) findViewById(R.id.securityCode);		

		SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		String sql = "select DENOMINATION as deno,BULK_NO as bulk,START_SERIAL as stse,NEXT_SERIAL as ns, END_SERIAL as ends,NO_OF_CARDS as nos,RETURN_TYPE as typ from TSR_CARD_RETURNS WHERE RETURN_TYPE = 'C' OR RETURN_TYPE = 'R' ";
		Cursor cursor = db.rawQuery(sql, null);
		listData.clear();
		if(cursor!= null){
	  	 	 if(cursor.moveToFirst()){
	  	 		for (int i = 0; i < cursor.getCount(); i++){
	  	 			HashMap<String,String> map = new HashMap<String, String>();
	  	 			
	            	map.put(columnTags[0], cursor.getString(cursor.getColumnIndex("deno")));
	            	map.put(columnTags[1], cursor.getString(cursor.getColumnIndex("bulk")));
	            	map.put(columnTags[2], cursor.getString(cursor.getColumnIndex("stse")));
	            	map.put(columnTags[3], cursor.getString(cursor.getColumnIndex("ends")));
	            	map.put(columnTags[4], cursor.getString(cursor.getColumnIndex("nos")));
	            	map.put(columnTags[5], cursor.getString(cursor.getColumnIndex("typ")));
	            	listData.add(map);
	  	 			cursor.moveToNext();
	  	 		}
	  	 	 }
		}
		cursor.close();
		db.close();
		SimpleAdapter adapter = new SimpleAdapter(Approved_card_returns.this, listData, R.layout.activity_approved_card_retuns__details, columnTags, columnIds);
		ListView lvData =(ListView)findViewById(R.id.listcardreturns);
		lvData.setAdapter(adapter);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_approved_card_returns, menu);
		return true;
	}
	
	public SimpleAdapter setAdapter(ArrayList<HashMap<String, String>> listData,int[] columnIds,String[] columnTags){
        SimpleAdapter adapter =new SimpleAdapter(this, listData, R.layout.activity_approved_card_retuns__details, columnTags , columnIds);        
        return adapter;
    }
	
	
	public void OnClickApprovedCard(View view) {
		try {
			LinearLayout vwParentRow = (LinearLayout) view.getParent();
			TextView Deno = (TextView) vwParentRow.findViewById(R.id.card_return_deno);
			TextView bulk = (TextView) vwParentRow.findViewById(R.id.card_return_bulkCode);
			TextView startserial = (TextView) vwParentRow.findViewById(R.id.card_return_startserial);
			TextView endserial = (TextView) vwParentRow.findViewById(R.id.card_return_endserial);
			TextView NumberOfCard = (TextView) vwParentRow.findViewById(R.id.card_return_numberofcards);
			TextView ReturnType = (TextView) vwParentRow.findViewById(R.id.card_return_type);

			String Denomination = Deno.getText().toString();
			String bulkcode = bulk.getText().toString();
			String cardstartserial = startserial.getText().toString();
			String cardendserila = endserial.getText().toString();
			String noc = NumberOfCard.getText().toString();
			String type = ReturnType.getText().toString();


			dbh.ReturnCards_reject(Denomination, bulkcode, cardstartserial, cardendserila, noc, type);
		}catch (Exception e){
			e.printStackTrace();
		}
	    Intent intent1 = new Intent(Approved_card_returns.this, Approved_card_returns.class);
	    startActivity(intent1);
	}
	
	public void OnClickConfirm(View view) {
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		error.setText("");
		String Securiry = SecurityCode.getText().toString().trim();
		if(Securiry.equalsIgnoreCase("")){
			error.setText("Please Enter Security Code..");
			return;
		}
		if (isOnline() == true) {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			User user = dbh.getUserDetails();
			final ArrayList<CardReturnsData> cardReturnsDatas = dbh.GetCaedReturnsDetails();
			Gson gson = new Gson();
			String cardreturn = gson.toJson(cardReturnsDatas);
			request.addProperty("strInputUserMobile", Utils.getSimSerialNumber(this));
			request.addProperty("strInputUserName", user.getUserName());
			request.addProperty("strInputUserPassword", user.getPassword());
			request.addProperty("CardReturns", cardreturn);
			request.addProperty("SecurityCode", Securiry);

			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			try {

				new GetResult(this).execute("");

				/*HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;*/

				Timer _timer  = new Timer();

				_timer.schedule(new TimerTask() {
					@Override
					public void run() {
						// use runOnUiThread(Runnable action)
						runOnUiThread(new Runnable() {
							@Override
							public void run() {

								try {
									CheckResult(dbh, cardReturnsDatas);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}, 1000);



			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void CheckResult(DatabaseHandler dbh, ArrayList<CardReturnsData> cardReturnsDatas) {
		if (result != null) {
			String Message = result.getProperty(0).toString().replace("\"", "");
			if (Message.equals("ErrorSecurityCode")) {
				error.setText("Please Enter the Valied Security Code.");
			}
			if (Message.equals("Error")) {
				error.setText("Please Try Again.");
			}
			if (Message.equals("Sucess")) {
				dbh.UpdateTheCardReturns(cardReturnsDatas);
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
				dlgAlert.setMessage("Successfully remove the Cards in mobile.");
				dlgAlert.setTitle("Confirmation Success.");
				dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Approved_card_returns.this, home.class);
						startActivity(intent);
					}
				});
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
			}
		}
	}

	private class GetResult extends AsyncTask<String, String, String> {


		private HttpTransportSE androidHttpTransport;
		private Context context;

		public GetResult(Context context) {
			super();
			this.context = context;
			androidHttpTransport = new HttpTransportSE(URL);
		}


		@Override
		protected String doInBackground(String... params) {
			try {

				androidHttpTransport.call(SOAP_ACTION, envelope);
				 result = (SoapObject) envelope.bodyIn;


				//   System.out.println("* result " + result!=null?result.toString():"");
//
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}


		@Override
		protected void onPostExecute(String result) {

			System.out.println(" * GetAync result "+result);

		}


		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(String... text) {


		}

	}
	public boolean isOnline() { // check network connection is available
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState() == NetworkInfo.State.CONNECTED
				|| connectivityManager.getNetworkInfo(
						ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			connected = true;
		} else {
			connected = false;
		}
		return connected;
	}
	
//	private String getMyPhoneNumber() {
////		TelephonyManager mTelephonyMgr;
////		mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
////		String getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
////		return getSimSerialNumber;
//	}
//	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), home.class);
		startActivity(intent);
		finish(); 
        return;
	}
	
}
