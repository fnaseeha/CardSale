package com.lk.lankabell.android.activity.tsr.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.beans.CashierCardDetails;
import com.lk.lankabell.android.activity.tsr.beans.Cashire_Card_Balance;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;

public class CashierModelActivity extends Activity {

	private ListView lvcashire;
	SimpleAdapter adapt=null;
	int selectedPosition;
	private String URL = "";
	private static String NAMESPACE = "http://mainService";
	private static String METHOD_NAME = "CashierModel";
	private static String METHOD_NAME1 = "CashierConfirm";	
	private static String SOAP_ACTION = "http://mainService/CashierModel";
	private static String SOAP_ACTION1 = "http://mainService/CashierConfirm";
	TextView Sales_Sum;
	TextView Inhand_Sum;
	TextView error;
	EditText AdjuestAmount;
	EditText Remarks;
	EditText SecurityCode;
	
	final ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();     
    final int[] columnIds = new int[] {R.id.lableDenomination, R.id.lablesalescountnumber,R.id.lablesalesamountnumber, R.id.lableInhandcountnumber, R.id.lableInhandamountnumber };
    final String[] columnTags = new String[] {"Denominations", "Sales Count", "Sales Amount", "Inhand Count", "Inhand Amount"};
	private DatabaseHandler dbh;

	SoapObject result;
	Double Sum_of_Sales = 0.0;
	Double Sum_of_Inhand = 0.0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_cashier_model);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText=(TextView)findViewById(R.id.myTitle);
		myTitleText.setText("Cashier Confirm");

		final TextView appversion = findViewById(R.id.appversion);
		dbh = new DatabaseHandler(getApplicationContext());
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}

		URL = WsdlReader.getServiceUrl(true,this);
		Sales_Sum = (TextView) findViewById(R.id.txtsalesAmount);
		Inhand_Sum = (TextView) findViewById(R.id.txthandamount);
		error = (TextView) findViewById(R.id.errormessage);
		error.setText("");
		SecurityCode = (EditText) findViewById(R.id.txtSecurityCode);
		AdjuestAmount = (EditText) findViewById(R.id.txtAdjustAmount);
		Remarks = (EditText) findViewById(R.id.txtRemarks);
		
		
		lvcashire = (ListView)findViewById(R.id.lvcashiremodel);

		
		setData();
		
		
	}

	private void setData() {
		
		if (isOnline()) {
//			loadDataFromServer();
			AysncCashireModel amodel = new AysncCashireModel(this);
			if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
				amodel.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);	
			} else {
				amodel.execute();
			} 
			
			
		} else {
			Log.w("==================", "===========================================================");
			ArrayList<Cashire_Card_Balance> salesDetails  = dbh.Get_CARD_CASH_BALANCE();			
			for (Cashire_Card_Balance cashire_Card_Balance : salesDetails) {
				HashMap<String,String> map = new HashMap<String, String>();
				new AlertDialog.Builder(CashierModelActivity.this).setTitle("Data details").setMessage(cashire_Card_Balance.getDenomination_Type() +" | "+cashire_Card_Balance.getCARD_IN_HAND() +" | "+cashire_Card_Balance.getCARD_IN_HAND_AMOUNT() +" | "+ cashire_Card_Balance.getCARD_SALES() +" | "+ cashire_Card_Balance.getCARD_SALES_AMOUNT()).setPositiveButton("Ok", null).show();
				map.put(columnTags[0],  cashire_Card_Balance.getDenomination_Type());
				map.put(columnTags[1],  cashire_Card_Balance.getCARD_IN_HAND()+"");
				map.put(columnTags[2],  new DecimalFormat("####.00").format(cashire_Card_Balance.getCARD_IN_HAND_AMOUNT()));
				map.put(columnTags[3],  cashire_Card_Balance.getCARD_SALES()+"");								
				map.put(columnTags[4],  new DecimalFormat("####.00").format(cashire_Card_Balance.getCARD_SALES_AMOUNT()));
				Sum_of_Sales = Sum_of_Sales + cashire_Card_Balance.getCARD_IN_HAND_AMOUNT();
				Sum_of_Inhand = Sum_of_Inhand + cashire_Card_Balance.getCARD_SALES_AMOUNT();
				listData.add(map);
			}
			
		}
		Sales_Sum.setText(new DecimalFormat("#,###.00").format(Sum_of_Sales));
		Inhand_Sum.setText(new DecimalFormat("#,###.00").format(Sum_of_Inhand));
		SimpleAdapter adapter = new SimpleAdapter(CashierModelActivity.this, listData, R.layout.cashier_model_list_view, columnTags, columnIds);
		lvcashire.setAdapter(adapter);
		
	}

	private SoapObject loadDataFromServer() {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		User user = dbh.getUserDetails();
		request.addProperty("strInputUserMobile", Utils.getSimSerialNumber(this));
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,60000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
//			viewData(result);
			return result;
		} catch (Exception e) {
			dbh.insertError("Cashier Model has error. Error : "+ e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

	private void viewData(SoapObject result) {
		if (result != null) {
			Gson gsonResponse = new Gson();
			CashierCardDetails[] cardDetails = gsonResponse.fromJson(result.getProperty(0).toString(), CashierCardDetails[].class);					
			Log.w("Inventory Response", cardDetails.toString());
			dbh.Clear_CARD_CASH_BALANCE();
			for (CashierCardDetails cashierCardDetails : cardDetails) {
				HashMap<String,String> map = new HashMap<String, String>();
				int cardcount = cashierCardDetails.getCardCount();
				double lineAmount = cashierCardDetails.getLineAmount();
				int HandCardCount = cashierCardDetails.getHandOnCount();					
				double discountPrice = (100 - cashierCardDetails.getDiscountRate()) * cashierCardDetails.getDenomination() *0.01;

				if (HandCardCount > 0) {
					Double handOnAmmount = discountPrice * HandCardCount;
					map.put(columnTags[0],  cashierCardDetails.getDenomination().toString() );
					map.put(columnTags[1],  cardcount +"" );
					map.put(columnTags[2],  new DecimalFormat("####.00").format(lineAmount));
					map.put(columnTags[3],  HandCardCount +"" );								
					map.put(columnTags[4],  new DecimalFormat("####.00").format(handOnAmmount));
					dbh.SaveCARD_CASH_BALANCE(cashierCardDetails.getDenomination().toString(),cardcount,lineAmount,HandCardCount,handOnAmmount);
					Sum_of_Sales = Sum_of_Sales + lineAmount;
					Sum_of_Inhand = Sum_of_Inhand + handOnAmmount;
				} else {
					map.put(columnTags[0],  cashierCardDetails.getDenomination().toString() );
					map.put(columnTags[1],  cardcount +"" );
					map.put(columnTags[2],  new DecimalFormat("####.00").format(lineAmount));
					map.put(columnTags[3],  HandCardCount +"" );								
					map.put(columnTags[4],  " 0.00 " );
					dbh.SaveCARD_CASH_BALANCE(cashierCardDetails.getDenomination().toString(),cardcount,lineAmount,HandCardCount,Double.parseDouble("0.00"));
					Sum_of_Sales = Sum_of_Sales + lineAmount;
					Sum_of_Inhand = Sum_of_Inhand + 0;
				}
				listData.add(map);
			}					
		}
		Sales_Sum.setText(new DecimalFormat("#,###.00").format(Sum_of_Sales));
		Inhand_Sum.setText(new DecimalFormat("#,###.00").format(Sum_of_Inhand));
		SimpleAdapter adapter = new SimpleAdapter(CashierModelActivity.this, listData, R.layout.cashier_model_list_view, columnTags, columnIds);
		lvcashire.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_cashier_model, menu);
		return true;
	}
	
	public void onClickConfirm(View view) {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		error.setText("");
		String TxtAdjuestAmount = AdjuestAmount.getText().toString().trim();
		String TxtRemarks = Remarks.getText().toString().trim();
		String Securiry = SecurityCode.getText().toString().trim();
		Double salesamount = Double.parseDouble(Sales_Sum.getText().toString().replaceAll(",", "").trim());
		
		if (!isOnline()) {
			Utils.ShowMsg(this, "Please turn on your data connection. can not confirm offline");
			return;
		}
		
		if(Securiry.equalsIgnoreCase("")){
			error.setText("Please Enter Security Code..");
			return;
		}
		if(TxtAdjuestAmount.equalsIgnoreCase("0") || TxtAdjuestAmount.equalsIgnoreCase("")){
			TxtAdjuestAmount = "0";
		} else{
			if(TxtRemarks.equalsIgnoreCase("")){
				error.setText("Enter Remark in Adjuested Amount.");
				return;
			}
		}
		if (salesamount < Double.parseDouble(TxtAdjuestAmount)) {
			error.setText("Can't Add Adjuest Amount greater than Sales Amount.");
			return;
		} else {
			if (isOnline() == true) {



				dbh.SaveConfirmation(salesamount.toString(),Inhand_Sum.getText().toString().trim(),TxtAdjuestAmount,TxtRemarks,Securiry);
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
				User user = dbh.getUserDetails();		
				request.addProperty("strInputUserMobile", Utils.getSimSerialNumber(this));
				request.addProperty("strInputUserName", user.getUserName());
				request.addProperty("strInputUserPassword", user.getPassword());
				request.addProperty("SalesAmount", salesamount.toString());
				request.addProperty("InHandAmount",Inhand_Sum.getText().toString().trim());
				request.addProperty("AdjuestAmount", TxtAdjuestAmount );
				request.addProperty("Remarks", TxtRemarks);
				request.addProperty("SecurityCode", Securiry);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
				envelope.dotNet = true;
				try {
					/*HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION1, envelope);*/


					new GetCashierData(this,envelope).execute("");


					Timer _timer  = new Timer();

					_timer.schedule(new TimerTask() {
						@Override
						public void run() {

							// use runOnUiThread(Runnable action)
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									try {
										getResponse();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}
					}, 2000);


				} catch (Exception e) {
					dbh.insertError("Cashier Model has error. Error : "+ e.getMessage());
					e.printStackTrace();
				}
			}else {
				dbh.SaveConfirmation(salesamount.toString(),Inhand_Sum.getText().toString().trim(),TxtAdjuestAmount,TxtRemarks,Securiry);
			}
		} 
	}

	public void getResponse() {
		if (result != null) {

			Gson gsonResponse = new Gson();
			Log.d(" ******************** ", " "+ gsonResponse );
			String Message = result.getProperty(0).toString().replace("\"", "");
			if (Message.equals("ErrorSecurityCode")) {
				error.setText("Please Enter the Valied Security Code.");
			}
			if (Message.equals("Error")) {
				error.setText("Please Try Again.");
			}
			if (Message.equals("Sucess")) {
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
				dlgAlert.setMessage("Cashier Card balance is success.");
				dlgAlert.setTitle("Confirm Sucess.");
				dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(CashierModelActivity.this, Updates.class);
						startActivity(intent);
					}
				});
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
				//new AlertDialog.Builder(CashierModelActivity.this).setTitle("").setMessage("").setPositiveButton("Ok", null).show();
			}
			Log.w("Inventory Response", Message.toString());
		}
	}

	/*private class BlockSync extends AsyncTask<String, String, String> {
		private Context context;
		private String simSerial;

		public BlockSync(Context context, String simSerial) {
			this.context = context;
			this.simSerial = simSerial;
		}

		@Override
		protected String doInBackground(String... strings) {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
			User user = dbh.getUserDetails();
			request.addProperty("strInputUserMobile", Utils.getSimSerialNumber(this));
			request.addProperty("strInputUserName", user.getUserName());
			request.addProperty("strInputUserPassword", user.getPassword());
			request.addProperty("SalesAmount", salesamount.toString());
			request.addProperty("InHandAmount", Inhand_Sum.getText().toString().trim());
			request.addProperty("AdjuestAmount", TxtAdjuestAmount);
			request.addProperty("Remarks", TxtRemarks);
			request.addProperty("SecurityCode", Securiry);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION1, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				if (result != null) {
					Gson gsonResponse = new Gson();
					Log.d(" ******************** ", " " + gsonResponse);
					String Message = result.getProperty(0).toString().replace("\"", "");
					if (Message.equals("ErrorSecurityCode")) {
						error.setText("Please Enter the Valied Security Code.");
					}
					if (Message.equals("Error")) {
						error.setText("Please Try Again.");
					}
					if (Message.equals("Sucess")) {
						AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
						dlgAlert.setMessage("Cashier Card balance is success.");
						dlgAlert.setTitle("Confirm Sucess.");
						dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(CashierModelActivity.this, Updates.class);
								startActivity(intent);
							}
						});
						dlgAlert.setCancelable(true);
						dlgAlert.create().show();
						//new AlertDialog.Builder(CashierModelActivity.this).setTitle("").setMessage("").setPositiveButton("Ok", null).show();
					}
					Log.w("Inventory Response", Message.toString());
				}
			} catch (Exception e) {
				dbh.insertError("Cashier Model has error. Error : " + e.getMessage());
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {

			System.out.println("* POST EXECUTED  " + result);

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(String... text) {


		}
	}*/

	private class GetCashierData extends AsyncTask<String, String, String> {


		private HttpTransportSE androidHttpTransport;
		private Context context;
		private SoapSerializationEnvelope envelope;
	//	private SoapObject result;

		public GetCashierData(Context context,
							  SoapSerializationEnvelope envelope) {
			super();
			this.context = context;
			androidHttpTransport = new HttpTransportSE(URL);
			this.envelope = envelope;
			//this.result = result;
		}


		@Override
		protected String doInBackground(String... params) {
			try {

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION1, envelope);
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
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(CashierModelActivity.this, Updates.class);
		startActivity(intent);
		finish();
        return;
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
//		TelephonyManager mTelephonyMgr;
//		mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		String getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
//		return getSimSerialNumber;
//	}

	private class AysncCashireModel extends AsyncTask<Void, Void, SoapObject>{


		private Context context;
		private ProgressDialog pg;

		public AysncCashireModel(Context context) {
			super();
			this.context = context;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected SoapObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return loadDataFromServer();
		}

		@Override
		protected void onPostExecute(SoapObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pg.dismiss();
			viewData(result);

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pg = ProgressDialog.show(this.context, "Loading", "Please wait. Loading data");
			pg.setCancelable(true);

		}



	}



}
