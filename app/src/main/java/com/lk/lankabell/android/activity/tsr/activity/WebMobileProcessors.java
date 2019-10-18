package com.lk.lankabell.android.activity.tsr.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.google.gson.Gson;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;
import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.MerchantInventorySynch;
import com.lk.lankabell.android.activity.tsr.sqlite.MerchantNewResponse;
import com.lk.lankabell.android.activity.tsr.sqlite.User;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class WebMobileProcessors extends Activity {

	private static String NAMESPACE = "http://mainService";
	private String URL = WsdlReader.getServiceUrl(true,this);
	DatabaseHandler dbh = null;
	private static String SOAP_ACTION10 = "http://mainService/CardBulkSerialData";
	private static String METHOD_NAME10 = "CardBulkSerialData";
	private static String SOAP_ACTION20 = "http://mainService/sendVersion";
	private static String METHOD_NAME20 = "sendVersion";
	private static String SOAP_ACTION30 = "http://mainService/MerchantSynch";
	private static String METHOD_NAME30 = "MerchantSynch";
	private static String SOAP_ACTION40 = "http://mainService/MerchantInventoryDataLastUpdated";
	private static String METHOD_NAME40 = "MerchantInventoryDataLastUpdated";
	private static String SOAP_ACTION50 = "http://mainService/PushCardSales";
	private static String METHOD_NAME50 = "PushCardSales";
	private static String METHOD_NAME60 = "updateAcceptedBulkCardSerial";
	private static String SOAP_ACTION60 = "http://mainService/updateAcceptedBulkCardSerial";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_web_mobile_processors);		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setText("Updates");
		}
		final TextView appversion = findViewById(R.id.appversion);
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_web_mobile_processors, menu);
		return true;
	}
	
	public static int getVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	
	public boolean isOnline() { // check network connection is available
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			connected = true;
		} else {
			connected = false;
		}
		return connected;
	}
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
	
	private String getMyPhoneNumber() {
		return Utils.getSimSerialNumber(this);
	}
	
	public void onClickUpgradeSoftware(View view) {
		final ProgressDialog ringProgressDialog = ProgressDialog.show(WebMobileProcessors.this, "Please wait ...", "Manual Synch is process ...", true);
		ringProgressDialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					dbh = new DatabaseHandler(getApplicationContext());
					if (isOnline() == true) {
						Date date2 = new Date();
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		                dbh.SaveNetworkStatus("Connect",sdf.format(date2));
		                //User user = dbh.getUserDetails();
		                //String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
		                Log.w("Method 1", "synchMerchants");
		                int merchant = SynchRegisterMerchant();
		                Log.w("Method 1", "sendVersion");
						int Version = sendVersion();
						Log.w("Method 3", "getCardBulkSerialData");
						int CardBulkSerial = getCardBulkSerialData();
						Log.w("Method 4", "PushCardSales");
						int SaleData = PushCardSales();
						Log.w("Methods States", "merchant : "+ merchant +"  Version :"+Version +"  CardBulkSerial :"+CardBulkSerial +"  PushCardSales :"+SaleData);
					} else {
						Date date2 = new Date();
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		                dbh.SaveNetworkStatus("DisConnect",sdf.format(date2));
						 runOnUiThread(new Runnable() {
							 public void run() {
								 ringProgressDialog.dismiss();
                            	 new AlertDialog.Builder(WebMobileProcessors.this).setTitle("synch info").setMessage("No 3G Signals").setPositiveButton("Ok", null).show();
                             }
						 }); 
					}
					// Let the progress ring for 10 seconds...
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ringProgressDialog.dismiss();
			}
		}).start();
	}
	
	
	public int SynchRegisterMerchant() {
		if (dbh == null) {
			dbh = new DatabaseHandler(getApplicationContext());
		}		
		int rowcount = dbh.GetNonSynchRegisterMerchantsCount();
		if (rowcount > 0) {
			int excicutcount = rowcount/5;
			if ((rowcount%5) > 0) {
				excicutcount = excicutcount+1;
			}
			for (int i = 0; i < excicutcount; i++) {
				String json = new Gson().toJson(dbh.GetNonSynchRegisterMerchants());
				String result = SynchData(SOAP_ACTION30,METHOD_NAME30,"merchantData",json);
				if (result.isEmpty() || result == null || result.equals("")) {
					
				} else {
					Gson gsonResponse = new Gson();
					MerchantNewResponse[] merchantResponseList = gsonResponse.fromJson(result,MerchantNewResponse[].class);
					for (MerchantNewResponse item : merchantResponseList) {
						if (item.getRespons() > 0) {
							dbh.UpdateMerchantsWithNewIds(item);
						}
					}
				}
			}
		}
		return 1;
	}
	
	private Integer sendVersion() throws ParseException {
		if (dbh == null) {
			dbh = new DatabaseHandler(getApplicationContext());
		}
		String result = SynchData(SOAP_ACTION20,METHOD_NAME20,"version",dbh.getVersion().toString());
		if (result.isEmpty() || result == null || result.equals("")) {
					
		} else {
			String resultArray[] = result.split(",");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
			Date date = sdf.parse(resultArray[1]);
			dbh.SaveServerDate(date.toString());
			if (resultArray[0].equals("success")) {
				dbh.updateVersionAsIsSynch();
				dbh.insertStatus("version successfully synced");
			}
		}
		return 1;
	}
	
	public Integer getCardBulkSerialData() {
		if (dbh == null) {
			dbh = new DatabaseHandler(getApplicationContext());
		}
		String date = dbh.GetLastUpdatedDate();
		String result = SynchData(SOAP_ACTION10,METHOD_NAME10,"strEntryDate", date);
		if (result.isEmpty() || result == null || result.equals("")) {
					
		} else {
			
		}
		return 1;
	}
	
	public Integer updateStockDataFromRemote() {
		if (dbh == null) {
			dbh = new DatabaseHandler(getApplicationContext());
		}
		int rowcount = dbh.GetNonSynchStockDataFromRemoteCount();
		if (rowcount > 0) {
			int excicutcount = rowcount/5;
			if ((rowcount%5) > 0) {
				excicutcount = excicutcount+1;
			}
			for (int i = 0; i < excicutcount; i++) {
				String result = SynchData(SOAP_ACTION40,METHOD_NAME40,"inventoryData", new Gson().toJson(dbh.GetNonSynchMerchantInventoryByLimit()));
				if (result.isEmpty() || result == null || result.equals("")) {
							
				} else {
					Gson gsonResponse = new Gson();					
					MerchantInventorySynch[] inventorySynchsList = gsonResponse.fromJson(result, MerchantInventorySynch[].class);			
					for (MerchantInventorySynch item : inventorySynchsList) {
						dbh.UpdateMerchantInventoryTableAfterActivation(item.getMerchantId(), item.getCardType(), item.getDenomination(), item.getStockInHand(), item.getReorderLevel(),item.getActivationChanges());
					}
				}
			}
		}
		return 1;
	}
	
	private Integer PushCardSales() {
		if (dbh == null) {
			dbh = new DatabaseHandler(getApplicationContext());
		}
		int rowcount = dbh.GetNonSynchCardSalesCount();
		if (rowcount > 0) {
			int excicutcount = rowcount/5;
			if ((rowcount%5) > 0) {
				excicutcount = excicutcount+1;
			}
			for (int i = 0; i < excicutcount; i++) {
				String result = SynchData(SOAP_ACTION50,METHOD_NAME50,"strInputSalesHeader", new Gson().toJson(dbh.GetNonSynchcardSalesHeaderByLimit()));
				if (result.isEmpty() || result == null || result.equals("")) {
							
				} else {
					Gson gsonResponse = new Gson();					
					MerchantInventorySynch[] inventorySynchsList = gsonResponse.fromJson(result, MerchantInventorySynch[].class);			
					for (MerchantInventorySynch item : inventorySynchsList) {
						dbh.UpdateMerchantInventoryTableAfterActivation(item.getMerchantId(), item.getCardType(), item.getDenomination(), item.getStockInHand(), item.getReorderLevel(),item.getActivationChanges());
					}
				}
			}
		}
		return 1;
	}
	
	

	public Integer setUpdateCardAcceptTag() {
		if (dbh == null) {
			dbh = new DatabaseHandler(getApplicationContext());
		}
		String date = dbh.GetLastUpdatedDate();
		String result = SynchData(SOAP_ACTION60,METHOD_NAME60,"strEntryDate", date);
		if (result.isEmpty() || result == null || result.equals("")) {
					
		} else {
			
		}
		return 1;
		
		/*
		
		List<bulkId> bulkIdList = dbh.getAcceptCardSerials();
		Integer returnVal = 0;
		if (bulkIdList != null && !bulkIdList.isEmpty()) {
			int error = 0;
			for (int i = 0; i < bulkIdList.size(); i++) {
				bulkId bulkIdData = bulkIdList.get(i);
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME60);

				request.addProperty("strInputUserMobile", getMyPhoneNumber());
				request.addProperty("strInputUserName", user.getUserName());
				request.addProperty("strInputUserPassword", user.getPassword());
				request.addProperty("ACCOUNT_CODE",Integer.toString(user.getId()));
				request.addProperty("BULK_CODE", bulkIdData.GetBulkId());
				request.addProperty("START_SERIAL", bulkIdData.GetStartSerial());
				request.addProperty("CARD_TYPE", bulkIdData.Gettype());
				request.addProperty("DENOMINATION", Integer.toString(bulkIdData.GetDenomination()));
				request.addProperty("END_SERIAL", bulkIdData.GetEndSerial());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
				envelope.dotNet = true;
				Log.w("Card Accept send Json Array is :  ", request.toString());
				try {
					HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION60, envelope);
					SoapObject result = (SoapObject) envelope.bodyIn;
					if (result != null) {
						Log.d("Return responses in the json objet deatils :", result.getProperty(0).toString());
						String result_Value = result.getProperty(0).toString();
						int resultNumeric = Integer.parseInt(result_Value);
						if (resultNumeric == 1) {
							dbh.UpdateCardAcceptResponse(bulkIdData.Gettype(),bulkIdData.GetDenomination(), bulkIdData.GetBulkId(), Integer.parseInt(bulkIdData.GetStartSerial()),Integer.parseInt(bulkIdData.GetEndSerial()));
							returnVal = 2;
							dbh.insertStatus("Card Acceptance  successfully synced");
						} else {
							error = error + 1;
						}
					}
				} catch (Exception e) {
					error = error + 1;
					dbh.insertError("Card Acceptance not synced.Error : " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (error > 0) {
				returnVal = 1;
			} else {
				returnVal = 2;
			}
		} else {
			returnVal = 3;
			dbh.insertStatus("CardAcceptance already synched");
		} 
		return returnVal;*/
	}
	
	
	
	
	private String SynchData(String SOAP_ACTION, String METHOD_NAME, String PropertyName, String json) {
		if (dbh == null) {
			dbh = new DatabaseHandler(getApplicationContext());
		}
		String Result = "";
		User user = dbh.getUserDetails();
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty(PropertyName, json);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				Result = result.getProperty(0).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result;
	}
	
}
