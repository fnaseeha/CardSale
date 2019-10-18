package com.lk.lankabell.android.activity.tsr.activity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lk.lankabell.android.activity.tsr.beans.CardReturnsConfirmData;
import com.lk.lankabell.android.activity.tsr.beans.CardReturnsData;
import com.lk.lankabell.android.activity.tsr.beans.NextSerialSync;
import com.lk.lankabell.android.activity.tsr.beans.SalesResponse;
import com.lk.lankabell.android.activity.tsr.beans.SynchCashierConfirmation;
import com.lk.lankabell.android.activity.tsr.beans.TSRSystemprofile;
import com.lk.lankabell.android.activity.tsr.beans.cardSalesDeatils;
import com.lk.lankabell.android.activity.tsr.beans.cardSalesHeader;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_CardDenominationData;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_CardNextSerial;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_CardType;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_CityData;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_Merchant_Master;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_cardBulkCode;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_merchantInventoryData;
import com.lk.lankabell.android.activity.tsr.sqlite.CityRegistrationLimit;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.ErrorLog;
import com.lk.lankabell.android.activity.tsr.sqlite.Locations;
import com.lk.lankabell.android.activity.tsr.sqlite.MerchantInventorySynch;
import com.lk.lankabell.android.activity.tsr.sqlite.MerchantNew;
import com.lk.lankabell.android.activity.tsr.sqlite.MerchantNewResponse;
import com.lk.lankabell.android.activity.tsr.sqlite.PostalCode;
import com.lk.lankabell.android.activity.tsr.sqlite.Remarks;
import com.lk.lankabell.android.activity.tsr.sqlite.ResultCode;
import com.lk.lankabell.android.activity.tsr.sqlite.SalesDetails;
import com.lk.lankabell.android.activity.tsr.sqlite.SalesHeaders;
import com.lk.lankabell.android.activity.tsr.sqlite.SerialDetails;
import com.lk.lankabell.android.activity.tsr.sqlite.TsrCityLimit;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.sqlite.bulkId;
import com.lk.lankabell.android.activity.tsr.sync.MearchActivation;
import com.lk.lankabell.android.activity.tsr.sync.postSync;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;
import com.lk.lankabell.android.activity.tsr.util.HTTPServiceCalls;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;
import com.lk.lankabell.android.activity.tsr.R;


public class Updates extends Activity {
	
	private static Context contexta;
	ProgressDialog ringProgressDialog_datareload;
	private User user;
	private static String SOAP_ACTION = "http://mainService/CardBulkSerialData";
	private static String SOAP_ACTION2 = "http://mainService/SaleRemarks";
	private static String SOAP_ACTION3 = "http://mainService/CardSale";
	private static String SOAP_ACTION8 = "http://mainService/updateAcceptedBulkCardSerial";
	private static String SOAP_ACTION11 = "http://mainService/CityNewData";
	private static String SOAP_ACTION14 = "http://mainService/sendJsonDataList";

	public static String NAMESPACE = "http://mainService";

	private static String METHOD_NAME = "CardBulkSerialData";
	private static String METHOD_NAME2 = "SaleRemarks";
	private static String METHOD_NAME3 = "CardSale";
	private static String METHOD_NAME8 = "updateAcceptedBulkCardSerial";
	private static String METHOD_NAME11 = "CityNewData";
	private static String METHOD_NAME14 = "sendJsonDataList";
	
	static ServerDataLoad serverDataLoad;
	static int selection = 0;

	private  String URL = WsdlReader.getServiceUrl(true,this);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.updates);
		URL = WsdlReader.getServiceUrl(true,this);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setText("Updates");
		}

		dbh = new DatabaseHandler(getApplicationContext());
		final TextView appversion = findViewById(R.id.appversion);
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}
		user = dbh.getUserDetails();
	}

	public static int getVersionCode(Context context) {
		contexta = context;
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException ex) {
		}
		return 0;
	}

	public void onClickUpdate(View view) 
	{
		Uri uriURL = Uri.parse("http://119.235.1.88:8080/tsr/app.apk");
		Intent LaunchBrowser = new Intent(Intent.ACTION_VIEW,uriURL);
		startActivity(LaunchBrowser);
	}

	public void onClickBackup(View view) {
		
		Gson gson = new Gson();
		String MerchantMaster = gson.toJson(dbh.getAllMerchantMaster());
		String MerchantMaster_upload = gson.toJson(dbh.GetAllRegisteredMerchants());
		String CardBundles = gson.toJson(dbh.getAllCardBundles());
		String NextSerial = gson.toJson(dbh.getAllCardNextSerials());
		String NextSerial_details = gson.toJson(dbh.getNextSerialDetails());
		String SalesHeader = gson.toJson(dbh.getAllSalesHeaders());
		String SalesDetail = gson.toJson(dbh.getAllSalesDetail());
		String MerchantInventory = gson.toJson(dbh.GetAllMerchantInventory());
		String locationsData = gson.toJson(dbh.getLocationsListBack());
		String salesHeaderJSON = gson.toJson(dbh.cardSalesHeader());
		 
		
		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/download");
		dir.mkdirs();
		final File file = new File(dir, "BackupData.txt");
		try {
			FileOutputStream f = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(f);
			pw.println("------------------------------------- All Merchants Details ---------------------------------------------- \n ");
			pw.println(MerchantMaster);
			pw.println("for json update");
			pw.println(MerchantMaster_upload);
			pw.println("\n \n \n \n \n");
			pw.println("------------------------------------- Bulk Card Serial Details ------------------------------------------- \n ");
			pw.println(CardBundles);
			pw.println("for json update");
			pw.println(NextSerial_details);
			pw.println("\n \n \n \n \n");
			pw.println("------------------------------------- Next Card Serial Details ------------------------------------------- \n ");
			pw.println(NextSerial);
			pw.println("\n \n \n \n \n");
			pw.println("------------------------------------- GPS locations Details ---------------------------------------------- \n ");
			pw.println(locationsData);
			pw.println("\n \n \n \n \n");
			pw.println("------------------------------------- Sales Headers ------------------------------------------------------ \n ");
			pw.println(SalesHeader);
			pw.println("\n \n \n \n \n");
			pw.println("------------------------------------- Sales Details ------------------------------------------------------ \n ");
			pw.println(SalesDetail);
			pw.println("\n \n \n \n \n");
			pw.println(salesHeaderJSON);
			pw.println("\n \n \n \n \n");
			pw.println("------------------------------------- Inventory Details -------------------------------------------------- \n ");
			pw.println(MerchantInventory);
			pw.println("\n \n \n \n \n");
			
			pw.flush();
			pw.close();
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dbh.clearAllSoldSerials();

		LayoutInflater factory = LayoutInflater.from(this);
		final View dialogBuilder = factory.inflate(R.layout.custom_okay_cancel_msg_dialog, null);
		final AlertDialog dialogView = new AlertDialog.Builder(this).create();
		dialogView.setView(dialogBuilder);
		TextView title = (TextView) dialogBuilder.findViewById(R.id.title);
		TextView message = (TextView) dialogBuilder.findViewById(R.id.message);
		title.setText("Backup Info");
		message.setText("Data Backup Done");

		dialogBuilder.findViewById(R.id.btn_send).setOnClickListener(new  View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendEnail(file);
			}
		});

		dialogBuilder.findViewById(R.id.btn_no).setOnClickListener(new  View.OnClickListener() {
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

	protected void sendEnail(File file) {
		try {
			
			Log.v("user send email", user.toString());
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"achchuthans@bellsolutions.lk"});
			i.putExtra(Intent.EXTRA_SUBJECT, dbh.GetEmpNoByPhoneNo(Utils.getSimSerialNumber(this)));
			i.putExtra(Intent.EXTRA_TEXT   ,dbh.GetEmpNoByPhoneNo(Utils.getSimSerialNumber(this)));
			//i.putExtra(Intent.EXTRA_STREAM ,Uri.fromFile(file));
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

			Uri myPhotoFileUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() +
					".provider", file);

			i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			i.putExtra(Intent.EXTRA_STREAM, myPhotoFileUri);

			try {
			    startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
			    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, "Email Send fails", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			Utils.writeToErrLogFileWithTime(e);
		}
	}

	public void onClickUpgradeSoftware(View view) {
		final ProgressDialog ringProgressDialog = ProgressDialog.show(Updates.this, "Please wait ...", "Manual Sync is process ...", true);
		ringProgressDialog.setCancelable(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Here you should write your time consuming task...
					Integer Systemprofile = 0 ,merchant = 0, Version = 0, CardBulkSerial = 0, SaleData = 0, CardAccept = 0, StockDataFromRemote = 0, g = 0, UpdatedCities = 0, NextSerialDetails = 0, updateMearchanInventory = 0, j = 0, updatecardsales = 0;
					DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
					dbh.clearStatusLog(); // clear the TSR_ERROR_LOG table details delete.
					dbh.clearErrorLog(); // clear the TSR_ERROR_LOG table details delete.
					dbh.clearSynchLog(); // clear the TSR_SYNC_LOG table details delete.

					postSync postSync = new postSync();
					// this method call before the sync. and insert TSR_SYNC_Log
					postSync.presynch(dbh);

					if (isOnline() == true) {
						Date date2 = new Date();
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		                dbh.SaveNetworkStatus("Connect",sdf.format(date2));
						Log.w("Method 0", "TSR System profile Data id Sysnc");
						User user = dbh.getUserDetails();
						String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
						System.out.println("* user "+user+" epfNo "+epfNo);
						// start the push methods ...
						Systemprofile = postSync.TSRSystemprofile(getApplicationContext(), getMyPhoneNumber(),
								user.getUserName(), user.getPassword() ,epfNo ,"Sysnc");
						Log.w("Method 2", "synchMerchants");
						
						merchant = synchMerchants();	//ok
						Log.w("Method 1", "sendVersion");
						Version = sendVersion();
						Log.w("Method 3", "getCardBulkSerialData");
						CardBulkSerial = getCardBulkSerialData();
						Log.w("Method 4", "SetSaleData");
						//SaleData = SetSaleData();	//ok
						SaleData = PushCardSales();
						Log.w("Method 5", "setUpdateCardAcceptTag");
						CardAccept = setUpdateCardAcceptTag(); // ok
						Log.w("Method 6", "updateStockDataFromRemote");
						StockDataFromRemote = updateStockDataFromRemote();	//ok
						Log.w("Method 7", "SetRemarksData");
						g = SetRemarksData();	//ok
						Log.w("Method 8", "getUpdatedCities");
						UpdatedCities = getUpdatedCities();
						Log.w("Method 9", "getNextSerialDetails");
						NextSerialDetails = getNextSerialDetails(); //ok
						Log.w("Method 10", "updateCityRegistrationLimit");
						j = updateCityRegistrationLimit();
						Log.w("Method 11", "synchLocation");
//						synchLocations();
						//synchLocationsError();
						//___________________________________________________________________________________
						Log.w("Method 12", "synchCardSales");
//						SaleData = PushCardSales();
//						updatecardsales = PushCardSales();
						// end push method ....
						// pull the mearchat Inventory data...
						Log.w("Method 51", "update mearchant Inventory Data.");
						updateMearchanInventory = PullMerchantInventoryData();
						Log.w("Method 52", "update mearchant Activations Data.");
						MearchantActivation();
						Log.w("Method 53", "Send the Status Data.");
						SendNetworkStatus();//#####
						Log.w("Method 54", "Send the Cashier Confirmation Data.");
//						synchCashierConfirmation();
						//Log.w("Method 61", "Send the Card Returns Data.");
						//synchCardReturns();
						
						
						try {
							((TSRApplication)getApplication()).updateGeoLocations();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					} else {
						Date date2 = new Date();
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		                dbh.SaveNetworkStatus("DisConnect",sdf.format(date2));
						 runOnUiThread(new Runnable() {
							 public void run() {
								 ringProgressDialog.dismiss();
								 diplayAlert("Sync Info","No 3G Signals");
                            	// new AlertDialog.Builder(Updates.this).setTitle("sync info").setMessage("No 3G Signals").setPositiveButton("Ok", null).show();
                             }
						 }); 						
					}

					// this method call for the after the sync. and update TSR_SYNC_Log
					postSync.postsynch(dbh, merchant, Version, CardBulkSerial, SaleData, CardAccept, StockDataFromRemote, g, UpdatedCities, NextSerialDetails);
					exportErrorLog();
					exportUpdateLog();
					
					List<ResultCode> finalResponse = dbh.getFinalResponse();
					
					for (ResultCode resultCode : finalResponse) {
						if (resultCode.getResponseCode() == 1) {
							runOnUiThread(new Runnable() {
								 public void run() {
									 ringProgressDialog.dismiss();
									 diplayAlert("Sync Info","Manual Sync Not Done");
//									 new AlertDialog.Builder(Updates.this).setTitle("sync info")
//											 .setMessage("Manual Sync Not Done")
//											 .setPositiveButton("Ok", null)
//											 .show();
	                             }
							 });
							return;
						} else if (resultCode.getResponseCode() == 2) {
							dbh.UpdateLastSysnhDate();
							runOnUiThread(new Runnable() {
								 public void run() {
									 ringProgressDialog.dismiss();
									 diplayAlert("Sync Info","Manual Sync Done.");
//									 new AlertDialog.Builder(Updates.this)
//											 .setTitle("sync info")
//											 .setMessage("Manual Sync Done")
//											 .setPositiveButton("Ok", null)
//											 .show();
	                             }
							 });
							return;
						} else if (resultCode.getResponseCode() == 3) {
							dbh.UpdateLastSysnhDate();
							runOnUiThread(new Runnable() {
								 public void run() {
									 ringProgressDialog.dismiss();
									 diplayAlert("Sync Info","Manual Sync is Already Done.");
//									 new AlertDialog.Builder(Updates.this)
//											 .setTitle("sync info")
//											 .setMessage("Manual Sync is Already Done.")
//											 .setPositiveButton("Ok", null)
//											 .show();
	                             }
							 });
							return;
						}
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

	private void diplayAlert(String b_title, String b_message) {

		LayoutInflater factory = LayoutInflater.from(this);
		final View dialogBuilder = factory.inflate(R.layout.custom_okay_msg_dialog, null);
		final AlertDialog dialogView = new AlertDialog.Builder(this).create();
		dialogView.setView(dialogBuilder);
		TextView title = (TextView) dialogBuilder.findViewById(R.id.title);
		TextView message = (TextView) dialogBuilder.findViewById(R.id.message);
		title.setText(b_title);
		message.setText(b_message);
		dialogBuilder.findViewById(R.id.okay).setOnClickListener(new  View.OnClickListener() {
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

	public void onClickCashire(View view) {
		boolean sysnch = CheckAllDataIsSysnchForSales();
		if (!isOnline()){
			diplayAlert("Sync Info","No 3G Signals");
		//	new AlertDialog.Builder(Updates.this).setTitle("sync info").setMessage("No 3G Signals").setPositiveButton("Ok", null).show();
			return;
		}
		if (!sysnch){
			diplayAlert("Sync Info","Please do a Manual Sync first");
		//	new AlertDialog.Builder(Updates.this).setTitle("sync info").setMessage("Please do a Manual Sync frist").setPositiveButton("Ok", null).show();
			return;
		}
		Intent intent = new Intent(Updates.this, CashierModelActivity.class);
		startActivity(intent);
		
	}
	@Deprecated
	public void onClickCashire_old(View view) {
		final ProgressDialog ringProgressDialog = ProgressDialog.show(Updates.this, "Please wait ...", "Data is Sync ... ", true);
		ringProgressDialog.setCancelable(false);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// Here you should write your time consuming task...
					Integer Systemprofile = 0 ,merchant = 0, Version = 0, CardBulkSerial = 0, SaleData = 0, CardAccept = 0, StockDataFromRemote = 0, g = 0, UpdatedCities = 0, NextSerialDetails = 0, updateMearchanInventory = 0, j = 0, updatecardsales = 0;
					DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
					dbh.clearStatusLog(); // clear the TSR_ERROR_LOG table details delete.
					dbh.clearErrorLog(); // clear the TSR_ERROR_LOG table details delete.
					dbh.clearSynchLog(); // clear the TSR_SYNC_LOG table details delete.

					postSync postSync = new postSync();
					// this method call before the sync. and insert TSR_SYNC_Log
					postSync.presynch(dbh);

					if (isOnline() == true) {
						Date date2 = new Date();
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		                dbh.SaveNetworkStatus("Connect",sdf.format(date2));
						Log.w("Method 0", "TSR System profile Data id Sysnc");
						User user = dbh.getUserDetails();
						String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
						// start the push methods ...
						Systemprofile = postSync.TSRSystemprofile(getApplicationContext(), getMyPhoneNumber(), user.getUserName(), user.getPassword() ,epfNo ,"Sysnc");
						Log.w("Method 2", "synchMerchants");
						
						merchant = synchMerchants();	//ok
						Log.w("Method 1", "sendVersion");
						Version = sendVersion();
						Log.w("Method 3", "getCardBulkSerialData");
						CardBulkSerial = getCardBulkSerialData();
						Log.w("Method 4", "SetSaleData");
						//SaleData = SetSaleData();	//ok
						SaleData = PushCardSales();
						Log.w("Method 5", "setUpdateCardAcceptTag");
						CardAccept = setUpdateCardAcceptTag(); // ok
						Log.w("Method 6", "updateStockDataFromRemote");
						StockDataFromRemote = updateStockDataFromRemote();	//ok
						Log.w("Method 7", "SetRemarksData");
						g = SetRemarksData();	//ok
						Log.w("Method 8", "getUpdatedCities");
						UpdatedCities = getUpdatedCities();
						Log.w("Method 9", "getNextSerialDetails");
						NextSerialDetails = getNextSerialDetails(); //ok
						Log.w("Method 10", "updateCityRegistrationLimit");
						j = updateCityRegistrationLimit();
						Log.w("Method 11", "synchLocation");
						synchLocations();
						//synchLocationsError();
						//___________________________________________________________________________________
						Log.w("Method 12", "synchCardSales");
						//SaleData = PushCardSales();
						//updatecardsales = PushCardSales();
						// end push method ....
						// pull the mearchat Inventory data...
						Log.w("Method 51", "update mearchant Inventory Data.");
						updateMearchanInventory = PullMerchantInventoryData();
						Log.w("Method 52", "update mearchant Activations Data.");
						MearchantActivation();
						Log.w("Method 53", "Send the Status Data.");
						SendNetworkStatus();
						
						// commected due to maltipal comfirmations --
						//Log.w("Method 54", "Send the Cashier Confirmation Data.");
//						synchCashierConfirmation();
						// -----------------------------------------
						//Log.w("Method 61", "Send the Card Returns Data.");
						//synchCardReturns();
					} else {
						Date date2 = new Date();
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		                dbh.SaveNetworkStatus("DisConnect",sdf.format(date2));
		                final List<ResultCode> finalResponse = dbh.getFinalResponse();
						 runOnUiThread(new Runnable() {
							 public void run() {
								 ringProgressDialog.dismiss();
								 diplayAlert("Sync Info","No 3G Signals");
//                            	 new AlertDialog.Builder(Updates.this).setTitle("sync info").setMessage("No 3G Signals")
//										 .setPositiveButton("Ok", null).show();

                            	 for (ResultCode resultCode : finalResponse) {
             						if (resultCode.getResponseCode() == 2) {
             							Intent intent = new Intent(Updates.this, CashierModelActivity.class);
             							startActivity(intent);
             						} else if (resultCode.getResponseCode() == 3) {
             							Intent intent = new Intent(Updates.this, CashierModelActivity.class);
             							startActivity(intent);
             						}
             					}
                             }
						 }); 						
					}

					// this method call for the after the sync. and update TSR_SYNC_Log
					postSync.postsynch(dbh, merchant, Version, CardBulkSerial, SaleData, CardAccept, StockDataFromRemote, g, UpdatedCities, NextSerialDetails);
					exportErrorLog();
					exportUpdateLog();
					
					List<ResultCode> finalResponse = dbh.getFinalResponse();
					
					for (ResultCode resultCode : finalResponse) {
						if (resultCode.getResponseCode() == 1) {
							runOnUiThread(new Runnable() {
								 public void run() {
									 ringProgressDialog.dismiss();
									 diplayAlert("Sync Info","Manual Sync Not Done");
//									 new AlertDialog.Builder(Updates.this).setTitle("sync info")
//											 .setMessage("Manual Sync Not Done")
//											 .setPositiveButton("Ok", null).show();
	                             }
							 });
							return;
						} else if (resultCode.getResponseCode() == 2) {
							dbh.UpdateLastSysnhDate();
							runOnUiThread(new Runnable() {
								 public void run() {
									 ringProgressDialog.dismiss();									 
									 Intent intent = new Intent(Updates.this, CashierModelActivity.class);
									 startActivity(intent);
	                             }
							 });
							return;
						} else if (resultCode.getResponseCode() == 3) {
							dbh.UpdateLastSysnhDate();
							runOnUiThread(new Runnable() {
								 public void run() {
									 ringProgressDialog.dismiss();
									 Intent intent = new Intent(Updates.this, CashierModelActivity.class);
									 startActivity(intent);
	                             }
							 });
							return;
						}
					}
					// Let the progress ring for 10 seconds...
					Thread.sleep(10000);
				} catch (Exception e) {
					
				}
				ringProgressDialog.dismiss();
			}
		}).start();
	}
	
	
	public void GONewInterface(View view) {
		Intent intent = new Intent(Updates.this,WebMobileProcessors.class);
		startActivity(intent);
	}

	private void exportUpdateLog() {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		// ===========================================================================================
		List<ErrorLog> list = new ArrayList<ErrorLog>();
		list = dbh.getAllStatusDetails();
		Gson gson = new Gson();
		String data = gson.toJson(list);

		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/download");
		dir.mkdirs();
		File file = new File(dir, "SyncLog.txt");
		try {
			FileOutputStream f = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(f);
			pw.println("-----------------SYNCH LOG------------------");
			pw.println(data);
			pw.println("-----------------------------------");
			pw.flush();
			pw.close();
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ===========================================================================================

	}

	private void exportErrorLog() {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		// ===========================================================================================
		List<ErrorLog> list = new ArrayList<ErrorLog>();
		list = dbh.getAllErrorDetails();
		Gson gson = new Gson();
		String data = gson.toJson(list);

		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/download");
		dir.mkdirs();
		File file = new File(dir, "ErrorLog.txt");
		try {
			FileOutputStream f = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(f);
			pw.println("-----------------ERROR LOG------------------");
			pw.println(data);
			pw.println("-----------------------------------");
			pw.flush();
			pw.close();
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ===========================================================================================

	}

	private static String SOAP_ACTION35 = "http://mainService/synchLocations";
	private static String METHOD_NAME35 = "synchLocations";

	private void synchLocations() {

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME35);
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		List<Locations> locations = dbh.getLocationsList();
		Gson gson = new Gson();
		String locationsList = gson.toJson(locations);
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("synchLocations", locationsList);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION35, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				Gson gsonResponse = new Gson();
				Log.w("success--------------", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+result+"------------------------------------------------");
				Type type = new TypeToken<ArrayList<String>>(){}.getType();
				ArrayList<String> list = gsonResponse.fromJson(result.getProperty(0).toString(),type);
				for (String string : list) {
					dbh.updateSynchLocations(string);
				}
			}
		} catch (Exception e) {
			Log.w("error", "locations not updated .");
		}
	}
	
	/*
	private static String SOAP_ACTION36 = "http://mainService/synchLocationsError";
	private static String METHOD_NAME36 = "synchLocationsError";

	private void synchLocationsError() {

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME36);
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		List<Locations> locations = dbh.getLocationsErrorList();
		Gson gson = new Gson();
		String locationsList = gson.toJson(locations);
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("synchLocations", locationsList);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION36, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				Gson gsonResponse = new Gson();
				Log.w("success--------------", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+result+"------------------------------------------------");
				Type type = new TypeToken<ArrayList<String>>(){}.getType();
				ArrayList<String> list = gsonResponse.fromJson(result.getProperty(0).toString(),type);
				for (String string : list) {
					dbh.updateSynchLocations(string);
				}
			}
		} catch (Exception e) {
			Log.w("error", "locations not updated .");
		}
	}
	*/
	

	private static String SOAP_ACTION20 = "http://mainService/sendVersion";
	private static String METHOD_NAME20 = "sendVersion";

	private Integer sendVersion() {
		Integer returnVal = 0;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME20);
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
        System.out.println("* user "+user);
		Double version = dbh.getVersion();
		System.out.println("* version "+version);
		if (version != 0.0) {
			String versionString = version.toString();
			request.addProperty("strInputUserMobile", getMyPhoneNumber());
			request.addProperty("strInputUserName", user.getUserName());
			request.addProperty("strInputUserPassword", user.getPassword());
			request.addProperty("version", versionString);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			try {
				StringBuilder xmlText= new StringBuilder();
                xmlText.append("");
				//xmlText.append(getMyPhoneNumber()).append(",").append(user.getUserName()).append(",").append(user.getPassword()).append(",").append(versionString);
				InputStream is = new ByteArrayInputStream(xmlText.toString().getBytes("UTF-8"));
				/*String ffsd = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + result.toString();
                XmlToJson xmlToJson = new XmlToJson.Builder(ffsd.trim()).build();
                JSONObject jsonObject = xmlToJson.toJson();*/
				XmlPullParser xp = new KXmlParser();
				xp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

				//InputStream is = httpEntity.getContent();

			//	InputStream is = IOUtils.toInputStream(str, "UTF-8");

				xp.setInput(is, "UTF-8");
				//new StringReader
			//	envelope.parse(xp);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION20, envelope);

				SoapObject result = (SoapObject) envelope.bodyIn;
				if (result != null) {
					String result_Value = result.getProperty(0).toString();
					String resultArray[] = result_Value.split(",");
					Log.w("server response", result_Value);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
					Date date = sdf.parse(resultArray[1]);

					dbh.SaveServerDate(date.toString());//
					if (resultArray[0].equals("success")) {
						dbh.updateVersionAsIsSynch();
						dbh.insertStatus("version successfully synced");
						returnVal = 2;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				returnVal = 1;
				dbh.insertError("Version not synced.Error : " + e.getMessage());
			}
		} else {
			dbh.insertStatus("version already synched");
			returnVal = 3;
		}
		return returnVal;
	}
	private void parseResponse(InputStream is, SoapEnvelope envelope)
			throws Exception {

		try {
			XmlPullParser xp = new KXmlParser();
			xp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
			xp.setInput(is, "UTF-8");
			envelope.parse(xp);
		} catch (Throwable e) {
			Log.e("LOG_TAG", "Error reading/parsing SOAP response", e);

		}

	}

	public void onClickDataUpdate(View view) throws IOException {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		dbh.clearAllSoldSerials();
		dbh.ClearAllLogsDetails();
		String salesId = "";
		DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
		User user = handler.getUserDetails();
		String strInputUserMobile = getMyPhoneNumber();
		String strInputUserName = user.getUserName();
		String strInputUserPassword = user.getPassword();
		Map<Long, String> salesIdList = handler.getPurgeSalesList(strInputUserMobile, strInputUserName, strInputUserPassword);
		for (Object key : salesIdList.keySet()) {
			salesId = key.toString();
			Long invoiceId = Long.valueOf(salesId);
			handler.DeleteSalesDetailsBySalesId(invoiceId);
			handler.DeleteSalesHeaderBySalesId(invoiceId);			
		}
		diplayAlert("Sync Info","Manual Purge Done");
//		new AlertDialog.Builder(Updates.this)
//				.setTitle("synch info")
//				.setMessage("Manual Purge Done").setPositiveButton("Ok", null).show();
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

	public void Erroralert(String title, String msg, int resourceID) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.setIcon(resourceID);
		alertDialog.show();

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

	private String getMyPhoneNumber() {
		 return Utils.getSimSerialNumber(this);
	}

	public Integer setUpdateCardAcceptTag() {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		List<bulkId> bulkIdList = dbh.getAcceptCardSerials();
		Integer returnVal = 0;
		if (bulkIdList != null && !bulkIdList.isEmpty()) {
			int error = 0;
			for (int i = 0; i < bulkIdList.size(); i++) {
				bulkId bulkIdData = bulkIdList.get(i);
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME8);

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
					androidHttpTransport.call(SOAP_ACTION8, envelope);
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
		return returnVal;
	}

	public Integer getCardBulkSerialData() {
		
		ArrayList<Map<String,String>> bulkcards = new ArrayList<Map<String,String>>();
		
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		String date = dbh.GetLastUpdatedDate();

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("strEntryDate", date);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		Integer returnVal = 0;
		Log.d("CardBulkSerialData send Json Array is :  ", request.toString());
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn; // return epf No
			if (result != null) {
				Log.d("Return responses in the json objet deatils :", result.toString());
				String dbQuery8 = result.getProperty(0).toString();
				String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
				String[] statusarray8 = dbQuery8String.split(",");
				String[] statusarrayData8 = dbQuery8String.split("<");
				int statusvalue8  = 0;
				try {
					statusvalue8 = Integer.parseInt(statusarray8[0].toString());
				} catch (Exception e) { 
					e.printStackTrace();
				}
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String dateValue = sdf1.format(System.currentTimeMillis());
				if (statusvalue8 == 1) {
					if (statusarray8[1].length() > 1) {
						for (int i = 1; i < statusarrayData8.length; i++) {
							String[] datavaluesStrings = statusarrayData8[i]
									.toString().split(">");
							String[] datavalues = datavaluesStrings[0]
									.split(",");
							if (dbh.CheckCardBulkRecordIsExists(datavalues[3].toString(),
                                    Integer.parseInt(datavalues[4].toString().replaceAll("\\]|\\>", "")),
                                    datavalues[0].toString(), datavalues[1].toString(), datavalues[2].toString()) == false) {
								dbh.SaveBulkIds(datavalues[3].toString(), Integer.parseInt(datavalues[4].toString().replaceAll("\\]|\\>", "")), datavalues[0].toString(), datavalues[1].toString(), datavalues[2].toString(), dateValue, Double.parseDouble(datavalues[6].toString()), Integer.parseInt(datavalues[7].toString()), Integer.parseInt(datavalues[8].toString()));
							}
							
							HashMap<String,String> cardvalues = new HashMap<String, String>();
							cardvalues.put("bulkid", datavalues[0].toString());
							cardvalues.put("start", datavalues[1].toString());
							cardvalues.put("tsr", "" + user.getId());
							bulkcards.add(cardvalues);
							
							
						}
						returnVal = 2;
						dbh.insertStatus("BulkSerials successfully synced");
					} else {
						returnVal = 3;
						dbh.insertStatus("BulkSerials already synched");
					}
				}
			}
		} catch (Exception e) {
			returnVal = 1;
			dbh.insertError("BulkSerials not synced.Error : " + e.getMessage());
			e.printStackTrace();
		}
		
		
		if (returnVal == 2) {
			updateSavedBuldCardSerials(bulkcards);
		}
		return returnVal;
	}

	
	String service_method_update_card_recive = "";
	
	private void updateSavedBuldCardSerials(ArrayList<Map<String, String>> bulkcards) {
		// TODO Auto-generated method stub
		
//		BulkCardUpdateAsync async = new BulkCardUpdateAsync(bulkcards);
//		async.execute(); 
		
		SoapObject request = new SoapObject(NAMESPACE, "UpdateBulkCardRecive");
		request.addProperty("mobile", getMyPhoneNumber());
		request.addProperty("username", user.getUserName());
		request.addProperty("password", user.getPassword()); 
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call("http://mainService/UpdateBulkCardRecive", envelope);
			SoapObject result = (SoapObject) envelope.bodyIn; // return epf No
			if (result != null) { 
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
//	private class BulkCardUpdateAsync extends AsyncTask<Void, Void, String>{
//
//		private ArrayList<Map<String, String>> bulkcards;
//
//		public BulkCardUpdateAsync(ArrayList<Map<String, String>> bulkcards) {
//			this.bulkcards = bulkcards;
//		}
//
//		@Override
//		protected String doInBackground(Void... params) {
//			
//			ArrayList<Map<String, String>> data = bulkcards;
//			
//			JSONArray array = new JSONArray(data);
//			
//			String json_string = array.toString();
//
//			HTTPServiceCalls calles = new HTTPServiceCalls();
//			try {
//				calles.makeRequest(CONSTANTS.URL_UPDATE_BULKCARD_SERIALS, json_string);
//			} catch (Exception e) {
//				Utils.writeToErrLogFileWithTime(e);
//			}
//			
//			return null;
//		}
//
//		 
//		
//	}
	
	

	private static String SOAP_ACTION4 = "http://mainService/MerchantInventoryDataLastUpdated";
	private static String METHOD_NAME4 = "MerchantInventoryDataLastUpdated";

	public Integer updateStockDataFromRemote() {// update oracle DB from SQLlite
												// DB
		Integer returnVal = 0;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		
		ArrayList<MerchantInventorySynch> inventories = dbh.GetNonSynchMerchantInventory();

		Gson gson = new Gson();
		String json = gson.toJson(inventories);
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("inventoryData", json);
		Log.w("Inventory Request", json);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;

		if (inventories != null && !inventories.isEmpty()) {
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION4, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				if (result != null) {
					Gson gsonResponse = new Gson();					
					MerchantInventorySynch[] inventorySynchsList = gsonResponse.fromJson(result.getProperty(0).toString(), MerchantInventorySynch[].class);
					Log.w("Inventory Response", inventorySynchsList.toString());					
					for (MerchantInventorySynch item : inventorySynchsList) {
						dbh.UpdateMerchantInventoryTableAfterActivation(item.getMerchantId(), item.getCardType(), item.getDenomination(), item.getStockInHand(), item.getReorderLevel(),item.getActivationChanges());
					}
					returnVal= 2;
					if (inventories.size() <= inventorySynchsList.length) {
						returnVal = 2;
					} else {
						returnVal = 1;
					}
					dbh.insertStatus("Inventory successfully synced");					
				}
			} catch (Exception e) {
				returnVal = 1;
				dbh.insertError("Inventoty not synced. Error : "
							+ e.getMessage());
				e.printStackTrace();
			}
		} else {
			returnVal = 3;
			dbh.insertStatus("Inventory already synched");
		}
		return returnVal;
	}

	public Integer SetRemarksData() {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		Integer returnVal = 0;
		ArrayList<Remarks> RemarksList = dbh.getUpdatedRemarksList();
		Gson gson = new Gson();
		String Remark = gson.toJson(RemarksList);
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());		
		request.addProperty("strSalesRemarks", Remark);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		if (RemarksList != null && !RemarksList.isEmpty()) {
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION2, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				if (result != null) {
					String result_Value = result.getProperty(0).toString();
					int resultNumeric = Integer.parseInt(result_Value);
					if (resultNumeric == 1) {
						dbh.UpdateRemarksTable(0);
						returnVal = 1;
						dbh.insertStatus("Remarks successfully synced");
					}
				}
			} catch (Exception e) {
				returnVal = 0;
				dbh.insertError("Remarks not synced.Error : "+ e.getMessage());
			}
		} else {
			dbh.insertStatus("Remarks already synched");
		}
		return returnVal;
	}

	public Integer SetSaleData() {
		Integer returnVal = 0;
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		ArrayList<SalesHeaders> salesHeaders = dbh.getSalesHeaders();
		ArrayList<SalesDetails> salesDetails = dbh.getUpdatedItemSalesDetailsList();

		Gson gson = new Gson();
		Gson gson2 = new Gson();
		String salesHeadersJson = gson.toJson(salesHeaders);
		String salesDetailsJson = gson2.toJson(salesDetails);
		User user = dbh.getUserDetails();
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("sendSalesHeadersList", salesHeadersJson);
		request.addProperty("sendSalesDetailsList", salesDetailsJson);

		Log.w("SalesHeader", salesHeadersJson);
		Log.w("SalesDetail", salesDetailsJson);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		if (salesHeaders != null && !salesHeaders.isEmpty()) {
			if (salesDetails != null && !salesDetails.isEmpty()) {
				try {
					HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION3, envelope);
					SoapObject result = (SoapObject) envelope.bodyIn;
					if (result != null) {
						Gson gsonResponse = new Gson();
						SalesResponse[] salesResponses = gsonResponse.fromJson(result.getProperty(0).toString(), SalesResponse[].class);
						Log.d("Return responses in the json objet deatils :", result.toString());
						for (SalesResponse salesResponse : salesResponses) {
							dbh.UpdateSalesHeaderTable(salesResponse);
						}
						if (salesResponses.length == salesHeaders.size()) {
							returnVal = 2;
						} else {
							returnVal = 1;
						}
						dbh.insertStatus("CardSales successfully synced");
					}
				} catch (Exception e) {
					returnVal = 1;
					dbh.insertError("Cards Seles is not synced.Error : "+ e.getMessage());
					e.printStackTrace();
				}
			}
		} else {
			returnVal = 3;
			dbh.insertStatus("CardSales already synched");
		}
		return returnVal;
	}

	private static String SOAP_ACTION30 = "http://mainService/MerchantSynch";
	private static String METHOD_NAME30 = "MerchantSynch";

	public Integer synchMerchants() {

		Integer returnVal = 0;
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		System.out.println("* user "+user);
		int rowcount = dbh.GetNonSynchRegisterMerchantsCount();
		
		if(rowcount < 1 ){
			return 2;
		}
		
		int excicutcount = rowcount/5;
		if ((rowcount%5) > 0) {
			excicutcount = excicutcount+1;
		}
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME30);
		Gson gson = new Gson();
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		for (int i = 0; i < excicutcount; i++) {
			ArrayList<MerchantNew> merchantNewList = dbh.GetNonSynchRegisterMerchants();
			String json = gson.toJson(merchantNewList);
			request.addProperty("merchantData", json);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			if (merchantNewList != null && !merchantNewList.isEmpty()) {
				try {
					HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION30, envelope);
					SoapObject result = (SoapObject) envelope.bodyIn;
					Log.w("=======================================================", result +"====================================");
					if (result != null) {
						Gson gsonResponse = new Gson();
						Log.d("Return responses in the json objet deatils :", result.toString());
						MerchantNewResponse[] merchantResponseList = gsonResponse.fromJson(result.getProperty(0).toString(),MerchantNewResponse[].class);
						Log.w("Merchants Sync Response sucess.", merchantResponseList.toString());
						for (MerchantNewResponse item : merchantResponseList) {
							dbh.UpdateMerchantsWithNewIds(item);
//							/dbh.updateMerchantsAsSynch();
						}
						dbh.insertStatus("Merchants successfully synced");
						if (merchantResponseList.length == merchantNewList.size()) {
							returnVal = 2;
						} else {
							returnVal = 1;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//Integer returnVal = 0;
		//DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		//SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME30);
		//User user = dbh.getUserDetails();
		//ArrayList<MerchantNew> merchantNewList = dbh.GetNonSynchMerchants();
		////
		//Gson gson = new Gson();
		//String json = gson.toJson(merchantNewList);
		//request.addProperty("strInputUserMobile", getMyPhoneNumber());
		//request.addProperty("strInputUserName", user.getUserName());
		//request.addProperty("strInputUserPassword", user.getPassword());
		//request.addProperty("merchantData", json);
        //
		//SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
		//		SoapEnvelope.VER11);
		//envelope.setOutputSoapObject(request);
		//envelope.dotNet = true;
		//if (merchantNewList != null && !merchantNewList.isEmpty()) {
		//	Log.w("Merchants send Json Array is :  ", json);
		//	try {				
		//		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		//		androidHttpTransport.call(SOAP_ACTION30, envelope);
		//		SoapObject result = (SoapObject) envelope.bodyIn;
		//		if (result != null) {
		//			Gson gsonResponse = new Gson();
		//			Log.d("Return responses in the json objet deatils :", result.toString());
		//			MerchantNewResponse[] merchantResponseList = gsonResponse
		//					.fromJson(result.getProperty(0).toString(),
		//							MerchantNewResponse[].class);
		//			Log.w("Merchants Sync Response sucess.", merchantResponseList.toString());
		//			for (MerchantNewResponse item : merchantResponseList) {
		//				dbh.UpdateMerchantsWithNewIds(item);
		//				// dbh.updateMerchantsAsSynch();
		//			}
		//			dbh.insertStatus("Merchants successfully synced");
		//			if (merchantResponseList.length == merchantNewList.size()) {
		//				returnVal = 2;
		//			} else {
		//				returnVal = 1;
		//			}
		//		}
		//	} catch (Exception e) {
		//		dbh.insertError("Merchants is not synced.Error : "+ e.getMessage());
		//		e.printStackTrace();
		//		returnVal = 1;
		//	}
		//} else {
		//	dbh.insertStatus("Merchants successfully synced");
		//	returnVal = 3;
		//}
		return returnVal;
	}

	public Integer getUpdatedCities() {
		DatabaseHandler dbhN = new DatabaseHandler(getApplicationContext());
		ArrayList<String> list = dbhN.getCities();
		Float Pos_Longitude = null;
		Float Pos_Latitude = null;
		for (int i = 0; i < list.size(); i++) {
			String cityName = list.get(i).toString();
			PostalCode postalCode = dbhN.getPostalCodeDetailsByCityName(cityName);
			if (Pos_Longitude != null) {
				Pos_Longitude = Float.parseFloat((postalCode.getLongitude()));
			} else {
				Pos_Longitude = (float) 0;
			}
			if (Pos_Latitude != null) {
				Pos_Latitude = Float.parseFloat((postalCode.getLatitude()));
			} else {
				Pos_Latitude = (float) 0;
			}
		}

		Integer returnVal = 0;
		User user = dbhN.getUserDetails();
		boolean network_enable = isOnline();
		if (network_enable == true) {
			returnVal = 3;
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME11);
			request.addProperty("strInputUserMobile", getMyPhoneNumber());
			request.addProperty("strInputUserName", user.getUserName());
			request.addProperty("strInputUserPassword", user.getPassword());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			try {
				Log.w("Merchants send Json Array is :  ", request.toString());
				/*	XmlPullParser xp = new KXmlParser();
			xp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
			xp.setInput(is, "UTF-8");
			envelope.parse(xp);*/
              //  XmlPullParser xp = new KXmlParser();
              //  xp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
              //  envelope.parse(xp);
               // xp.setInput(new InputStreamReader(request));
              //  xp.setInput(request, "UTF-8");

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

				androidHttpTransport.call(SOAP_ACTION11, envelope);
				SoapObject result5 = (SoapObject) envelope.bodyIn;
				if (result5 != null) {
					Log.d("Return responses in the json objet deatils :", result5.toString());
					String dbQuery5 = result5.getProperty(0).toString();
					String Value5 = dbQuery5.replaceAll("\\[|\\]", "");
					String[] statusarray5 = Value5.split(",");
					String[] statusarrayData5 = Value5.split("<");
					if (!statusarray5[0].toString().equals("")) {
						int statusvalue5 = Integer.parseInt(statusarray5[0].toString());
						if (statusvalue5 == 1) {
							for (int i = 1; i < statusarrayData5.length; i++) {
								String[] datavaluesStrings = statusarrayData5[i]
										.toString().split(">");
								String[] datavalues = datavaluesStrings[0]
										.split(",");
								if (datavalues[3].toString() == null) {
									datavalues[3] = "0.0";
								}
								if (datavalues[4].toString() == null) {
									datavalues[4] = "0.0";
								}
								if (!list.contains(datavalues[1].toString())) {
									dbhN.SaveCityData(datavalues[0].toString(),
											datavalues[1].toString(),
											datavalues[2].toString(),
											datavalues[3].toString(),
											datavalues[4].toString(),
											datavalues[5].toString(),
											datavalues[6].toString());
									returnVal = 2;
									dbhN.insertStatus("Cities successfully synced");
								} else {
									dbhN.insertStatus("Cities already synched");
									returnVal = 3;
								}
							}
						}
					}
				}

			} catch (Exception e) {
				returnVal = 1;
				dbhN.insertError("Cities not synced. Error : " + e.getMessage());
				e.printStackTrace();
			}
		}
		return returnVal;
	}

	private Integer getNextSerialDetails() {// SELECT IF EXISTS, UPDATE OR
											// INSERT INTO
											// SALES_CARD_NEXT_SERIAL
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		ArrayList<SerialDetails> serialDetails = dbh.getNextSerialDetails();
		Gson gson = new Gson();
		String json = gson.toJson(serialDetails);
		Integer returnVal = 1;
		User user = dbh.getUserDetails();
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME14);
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("sendJsonDataList", json);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		if (serialDetails != null && !serialDetails.isEmpty()) {
			Log.w("Next Serial Details send Json Array is :  ", json);
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION14, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				if (result != null) {
					Gson gsonResponse = new Gson();
					NextSerialSync[] nextSerialSyncs = gsonResponse.fromJson(result.getProperty(0).toString(),NextSerialSync[].class);
					Log.d("Result of the Next Serial sync : ", result.getProperty(0).toString());
					for (NextSerialSync nextSerialSync : nextSerialSyncs) {
						dbh.updateNextSerialsAsSynch(nextSerialSync);
					}
					if (nextSerialSyncs.length == serialDetails.size()) {
						returnVal = 2;
					} else {
						returnVal = 1;
					}
					dbh.insertStatus("Serials successfully synced");
				}
			} catch (Exception e) {
				returnVal = 1;
				dbh.insertError("Serials not synced.Error : " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			returnVal = 3;
			dbh.insertStatus("Serials already synched");
		}
		return returnVal;
	}

	private static String SOAP_ACTION21 = "http://mainService/updateCityRegistrationLimit";
	private static String METHOD_NAME21 = "updateCityRegistrationLimit";

	private Integer updateCityRegistrationLimit() {
		DatabaseHandler dbhN = new DatabaseHandler(getApplicationContext());
		User user = dbhN.getUserDetails();
		List<CityRegistrationLimit> registrationLimits = dbhN.getCityRegistrationLimits();
		Gson gson = new Gson();
		String limits = gson.toJson(registrationLimits);
		boolean network_enable = isOnline();
		Integer returnVal = 1;
		if (network_enable == true) {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME21);
			request.addProperty("strInputUserMobile", getMyPhoneNumber());
			request.addProperty("strInputUserName", user.getUserName());
			request.addProperty("strInputUserPassword", user.getPassword());
			request.addProperty("limits", limits);
			Log.w("city limit", limits);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION21, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				if (result != null) {
					String resultJson = result.getProperty(0).toString();
					Gson gsonResponse = new Gson();
					TsrCityLimit[] tsrCityLimitList = gsonResponse.fromJson(resultJson, TsrCityLimit[].class);
					Log.w("Inventory Response", tsrCityLimitList.toString());

					if (tsrCityLimitList.length >= 1) {
						for (TsrCityLimit item : tsrCityLimitList) {
							dbhN.UpdateTsrCityLimitList(item.getPostalCodes(),
									item.getMaxLimit());
						}
						returnVal = 2;
						dbhN.insertStatus("City Registration Limit successfully synced");
					} else {
						returnVal = 3;
						dbhN.insertStatus("City Registration Limit already synched");
					}
				}
			} catch (Exception e) {
				returnVal = 1;
				dbhN.insertError("City Registration Limit not synced.Error : "+ e.getMessage());
				e.printStackTrace();
			}
		}
		return returnVal;
	}
	
	
	private static String SOAP_ACTION51 = "http://mainService/PullMerchantInventoryData";
	private static String METHOD_NAME51 = "PullMerchantInventoryData";

	private Integer PullMerchantInventoryData() {
		Integer returnVal = 0;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME51);
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;

			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION51, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				if (result != null) {
					Gson gsonResponse = new Gson();					
					MerchantInventorySynch[] inventorySynchsList = gsonResponse.fromJson(result.getProperty(0).toString(), MerchantInventorySynch[].class);
					Log.w("Inventory Response", inventorySynchsList.toString());					
					for (MerchantInventorySynch item : inventorySynchsList) {
						Log.w("Inventory Response results : ", item.getMerchantId() +" |  "+ item.getCardType() +" |  "+ item.getDenomination() +" |  "+ item.getStockInHand() +" |  "+ item.getReorderLevel() +" |  "+ item.getActivationChanges());
						dbh.UpdateMerchantInventoryTableAfterActivation(item.getMerchantId(), item.getCardType(), item.getDenomination(), item.getStockInHand(), item.getReorderLevel(),item.getActivationChanges());
					}
					dbh.insertStatus("Inventory updateted successfully synced");					
				}
			} catch (Exception e) {
				returnVal = 1;
				dbh.insertError("Inventoty not synced. Error : "
							+ e.getMessage());
				e.printStackTrace();
			}
		return returnVal;
	}
	
	
	private static String SOAP_ACTION52 = "http://mainService/PushCardSales";
	private static String METHOD_NAME52 = "PushCardSales";
	
	private Integer PushCardSales() {
		Integer returnVal = 0;
		
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		ArrayList<cardSalesHeader> cardSalesHeaders = dbh.cardSalesHeader();
		Gson gson = new Gson();
		String salesHeaders = gson.toJson(cardSalesHeaders);
		for (cardSalesHeader cardSalesHeader : cardSalesHeaders) {
			Log.d("This is the bean class details : ", cardSalesHeader.getEnteredBy() +" | "+ cardSalesHeader.getCardSalesDeatils() +" | "+ cardSalesHeader.getMerchantId() );
			ArrayList<cardSalesDeatils> arrayList =  cardSalesHeader.getCardSalesDeatils();
			for (cardSalesDeatils cardSalesDeatils : arrayList) {
				Log.d("Bean class deatils ******************* : " , cardSalesDeatils.getCardType() +" | "+ cardSalesDeatils.getStartSerial() +" | "+ cardSalesDeatils.getBulkNo() +" | "+ cardSalesDeatils.getDenomination() +" | "+ cardSalesDeatils.getInvoiceId() +" | "+ cardSalesDeatils.getDetailsId() );
			}
		}
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME52);				
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("strInputSalesHeader", salesHeaders);
		Log.d("This is the sales header details _____________________________________ : ", salesHeaders );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION52, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn; // SoapFault
				if (result != null) {
					Gson gsonResponse = new Gson();
					SalesResponse[] salesResponses = gsonResponse.fromJson(result.getProperty(0).toString(), SalesResponse[].class);		
					Log.d("Return responses in the json objet deatils :", result.toString());
					for (SalesResponse salesResponse : salesResponses) {
						dbh.UpdateSalesHeaderTable(salesResponse);
					}
					if (salesResponses.length == cardSalesHeaders.size()) {
						returnVal = 2;
					} else {
						returnVal = 1;
					}
					dbh.insertStatus("CardSales successfully synced"); 
				}
			} catch (Exception e) {
				returnVal = 1;
				dbh.insertError("Inventoty not synced. Error : "+ e.getMessage());
				e.printStackTrace();
			}
			Log.d("This is the card sales sucess messages : " , "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +returnVal);
		return returnVal;
	}
	
	
	private static String SOAP_ACTION55 = "http://mainService/MearchantActivation";
	private static String METHOD_NAME55 = "MearchantActivation";
	
	private Integer MearchantActivation() {
		Integer returnVal = 0;
		Log.d(" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ " , "~~~~~~~~~~~~~~~ aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ~~~~~~~~~~~~~~~");
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME55);				
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION55, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn; // SoapFault
				if (result != null) {
					Gson gsonResponse = new Gson();
					MearchActivation[] activations = gsonResponse.fromJson(result.getProperty(0).toString(), MearchActivation[].class);
					for (MearchActivation mearchActivation : activations) {
						dbh.UpdateMerchantActivations(mearchActivation);
					}
				}
			} catch (Exception e) {
				returnVal = 1;
				dbh.insertError("Inventoty not synced. Error : "+ e.getMessage());
				e.printStackTrace();
			}
			Log.d("This is the card sales sucess messages : " , "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +returnVal);
		return returnVal;
	}
	
	
	private static String SOAP_ACTION56 = "http://mainService/SendNetworkStatus";
	private static String METHOD_NAME56 = "SendNetworkStatus";
	
	private void SendNetworkStatus() {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		List<String> networkStatus = dbh.GetNetworkStatus();
		Gson gson = new Gson();
		String networkstatusList = gson.toJson(networkStatus);	
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME56);				
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("synchNetworkStatus", networkstatusList);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION56, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn; // SoapFault
			if (result != null) {
				Gson gsonResponse = new Gson();
				Type type = new TypeToken<ArrayList<String>>(){}.getType();
				ArrayList<String> list = gsonResponse.fromJson(result.getProperty(0).toString(),type);
				for (String string : list) {
					dbh.updateNetworkStatus(string);
				}
			}
		} catch (Exception e) {
			dbh.insertError("SendNetworkStatus : "+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static String SOAP_ACTION57 = "http://mainService/synchCashierConfirmation";
	private static String METHOD_NAME57 = "synchCashierConfirmation";
	
	private void synchCashierConfirmation() {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		ArrayList<SynchCashierConfirmation> cashierConfirmations = dbh.GetCashierConfirmationDetails();
		if (cashierConfirmations.size() > 0) {
			Gson gson = new Gson();
			String cashierConfirm = gson.toJson(cashierConfirmations);	
			
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME57);				
			request.addProperty("strInputUserMobile", getMyPhoneNumber());
			request.addProperty("strInputUserName", user.getUserName());
			request.addProperty("strInputUserPassword", user.getPassword());
			request.addProperty("synchCashierConfirm", cashierConfirm);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION57, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn; // SoapFault
				if (result != null) {
					Gson gsonResponse = new Gson();
					Type type = new TypeToken<ArrayList<String>>(){}.getType();
					ArrayList<String> list = gsonResponse.fromJson(result.getProperty(0).toString(),type);
					for (String string : list) {
						dbh.updateNetworkStatus(string);
					}
				}
			} catch (Exception e) {
				dbh.insertError("Send CashierConfirmation : "+ e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private static String SOAP_ACTION61 = "http://mainService/synchCardReturns";
	private static String METHOD_NAME61 = "synchCardReturns";
	
	private void synchCardReturns() {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		ArrayList<CardReturnsData> cardReturnsDatas = dbh.GetCaedReturnsDetails();
		Gson gson = new Gson();
		String cardreturn = gson.toJson(cardReturnsDatas);	
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME61);				
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("CardReturns", cardreturn);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION61, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn; // SoapFault
			if (result != null) {				
				Gson gsonResponse = new Gson();
				CardReturnsConfirmData[] cardReturnsConfirmDatas = gsonResponse.fromJson(result.getProperty(0).toString(), CardReturnsConfirmData[].class);
				for (CardReturnsConfirmData returnsConfirmData : cardReturnsConfirmDatas) {
					dbh.UpdateCardReturns(returnsConfirmData);
				}
			}
		} catch (Exception e) {
			dbh.insertError("Send card returns : "+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	//______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________
	
	
	public void onClickResetDataInMobile(View view) {
		network_enable = isOnline();
		ringProgressDialog_datareload = ProgressDialog.show(Updates.this, "Please wait ...", "Data reload is process ...", true);
		ringProgressDialog_datareload.setCancelable(false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Here you should write your time consuming task...
					if (network_enable){
						boolean sysnch = CheckAllDataIsSysnch();
						if (sysnch) {
							boolean check_data = CheckTsrDataLOG();
							dbh = new DatabaseHandler(getApplicationContext());
							if (check_data) {
								dbh.DeleteDataReloadTable();
								LoadLostDataInServertToMobile("All");
							} else {
								dbh.DeleteDataReloadTable();
								String finalResponse = dbh.getFinalResetDataTabels();
								int i = dbh.getFinalResetData();
								
								if (i == 0 ) {
									selection = 0;
									runOnUiThread(new Runnable() {
										 public void run() {
											 //ringProgressDialog_datareload.dismiss();
											 AlertDialog.Builder alertDialog = new AlertDialog.Builder(Updates.this)
													 .setTitle("Confirm Data Reload...")
													 .setMessage("Are you sure you want Data Reload Again?")
													 .setIcon(R.drawable.ic_alert);

										        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
										            public void onClick(DialogInterface dialog,int which) {
														dialog.cancel();
														ringProgressDialog_datareload.show();
										            	LoadLostDataInServertToMobile("All");
														ringProgressDialog_datareload.dismiss();
										            	selection = 1;

										            }
										        });				 
										        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
										            public void onClick(DialogInterface dialog, int which) {
										            	dialog.cancel();
														ringProgressDialog_datareload.dismiss();
										            	selection = 0;
										            	return;
										            }
										        });
										        alertDialog.show();
			                             }
									 });
									
									
							        if (selection > 0) {
							        	LoadLostDataInServertToMobile("All");
									}				        
								}else {
									LoadLostDataInServertToMobile(finalResponse);
								}
							}
						} else {
							runOnUiThread(new Runnable() {
								 public void run() {
									 ringProgressDialog_datareload.dismiss();
									 diplayAlert("Sync Info","Please Synch before Data Reload.");
//									 new AlertDialog.Builder(Updates.this).setTitle("synch info")
//											 .setMessage("Please Synch before Data Reload.").setPositiveButton("Ok", null).show();
	                             }
							 });	
						}
					} else {
						runOnUiThread(new Runnable() {
							 public void run() {
								 ringProgressDialog_datareload.dismiss();
								 diplayAlert("Network Info","No 3G Signals / Check the Data Connection.");
//								 new AlertDialog.Builder(Updates.this).setTitle("Network info")
//										 .setMessage("No 3G Signals / Check the Data Connection.").setPositiveButton("Ok", null).show();
                            }
						});			
					}
					// Let the progress ring for 10 seconds...
					Thread.sleep(10000);
				} catch (Exception e) {
					
				}
				ringProgressDialog_datareload.dismiss();
			}
		}).start();
	}
	

	private void LoadAllData() {
		int finalResponse = dbh.getFinalResetDataINUpdate();
		if (finalResponse == 0) {
			runOnUiThread(new Runnable() {
				 public void run() {
					 ringProgressDialog_datareload.dismiss();dbh.UpdateLastSysnhDate();
					 diplayAlert("Data Reload info","Data Reload is Done");
//					 new AlertDialog.Builder(Updates.this).setTitle("Data Reload info")
//							 .setMessage("Data Reload is Done").setPositiveButton("Ok", null).show();
                }
			 });
		} else{
			runOnUiThread(new Runnable() {
				 public void run() {
					 ringProgressDialog_datareload.dismiss();
					 diplayAlert("Data Reload info","Data Reload is not Done");
//					 new AlertDialog.Builder(Updates.this).setTitle("Data Reload info").setMessage("Data Reload is not Done")
//							 .setPositiveButton("Ok", null).show();
                }
			 });
		}
	}

	private boolean CheckTsrDataLOG() {
		String CheckingQuery = "SELECT count(*) AS total_count FROM TSR_DATA_LOAD_LOG";
		SQLiteDatabase db1 = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Cursor cursor = db1.rawQuery(CheckingQuery,null);
		Integer checkDataLog = 0;
		while (cursor.moveToNext()) {
			if(cursor.getInt(0) > 0 ){
				checkDataLog = cursor.getInt(0);
			}
		}	
		if (checkDataLog == 0) {
			return true;
		} else {
			return false;
		}
	}

	private DatabaseHandler dbh;
	boolean network_enable;
	private static String SOAP_ACTION76 = "http://mainService/LoadLostDataInServertToMobile";
	private static String METHOD_NAME76 = "LoadLostDataInServertToMobile";

	private boolean CheckAllDataIsSysnch() {
		String CheckingQuery = "SELECT SUM(synch_count) as synch_count ,SUM(total_count) as total_count  FROM " +
				"(" +
				"Select  sum(is_synch) as synch_count, count(*) as total_count from sales_details " +
				"union Select sum(is_synch) as synch_count, count(*) as total_count from sales_header " +
				"union Select  sum(is_synch) as synch_count, count(*) as total_count from merchant_master " +
				"union select sum(is_synch) as synch_count, count(is_synch) as total_count from next_serial " +
				"union select  sum(is_synch) as synch_count, count(*) as total_count from TSR_RECEIVED_CARD_BUNDLES " +
				"union select  sum(is_synch) as synch_count, count(*) as total_count from CITY_POSTAL_CODES " +
				"union select sum(is_synch) as synch_count, count(*) as total_count from MERCHANT_INVENTORY" +
				" )";
		SQLiteDatabase db1 = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Cursor cursor = db1.rawQuery(CheckingQuery,null);
		Integer synch_count = -1;
		Integer total_count = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			synch_count = cursor.getInt(0);
			total_count = cursor.getInt(1);
			Log.d("Synch Count is --------------- ", ""+synch_count);
			Log.d("Totall Count is --------------- ", ""+total_count);
		}
		if (synch_count == total_count || synch_count.equals(total_count)) {
			return true;
		} else {
			return false;
		}		
	}
	private boolean CheckAllDataIsSysnchForSales() {
		String CheckingQuery = "SELECT SUM(synch_count) as synch_count ,SUM(total_count) as total_count  FROM " +
				"(" +
				"Select  sum(is_synch) as synch_count, count(*) as total_count from sales_details " +
				"union Select sum(is_synch) as synch_count, count(*) as total_count from sales_header " +
//				"union Select  sum(is_synch) as synch_count, count(*) as total_count from merchant_master " +
//				"union select sum(is_synch) as synch_count, count(is_synch) as total_count from next_serial " +
//				"union select  sum(is_synch) as synch_count, count(*) as total_count from TSR_RECEIVED_CARD_BUNDLES " +
//				"union select  sum(is_synch) as synch_count, count(*) as total_count from CITY_POSTAL_CODES " +
//				"union select sum(is_synch) as synch_count, count(*) as total_count from MERCHANT_INVENTORY" +
" )";
		SQLiteDatabase db1 = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Cursor cursor = db1.rawQuery(CheckingQuery,null);
		int synch_count = -1;
		int total_count = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			synch_count = cursor.getInt(0);
			total_count = cursor.getInt(1);
			Log.d("Synch Count is --------------- ", ""+synch_count);
			Log.d("Totall Count is --------------- ", ""+total_count);
		}
		
		
		if (synch_count != total_count ) {
			return false;
		} else {
			return true;
		}
//		if (synch_count == total_count ) {
//			return true;
//		} else {
//			return false;
//		}
		 
	}
	
	private void LoadLostDataInServertToMobile(String LostTabels){
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME76);
		dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		dbh.DeleteDataReloadTable();
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("strReloadTables", LostTabels);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(WsdlReader.getServiceUrl(false,this));
				androidHttpTransport.call(SOAP_ACTION76, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				if (result != null) {
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					serverDataLoad = gson.fromJson(result.getProperty(0).toString(), ServerDataLoad.class);
					String AccountCode  = serverDataLoad.getAccountCode();
					String MobileNumber = serverDataLoad.getMobileNumber();
					String UserName  = serverDataLoad.getUserName();
					String Password  = serverDataLoad.getPassword();
					if (LostTabels.equals("All")) {
						boolean deletedb = getApplicationContext().deleteDatabase("TSRDBNEW");
						dbh = new DatabaseHandler(getApplicationContext());
						 if (deletedb) {
							 dbh.SaveLoginData(AccountCode, UserName, Password, MobileNumber,serverDataLoad.getServerDateTime());
							 AddCityDetails();
							 AddCardTypes();
							 AddMerchantData();
							 AddCardDenominations();
							 AddMerchantInventoryData();
							 AddCardNextSerialData();
							 AddCardBulkSerialData();
							 AddSystemProfile();
						 }
					} else {
						String Tabels[] = LostTabels.split("&");
						for (int i = 0; i < Tabels.length; i++) {
							String selectValue = Tabels[i].toString();
							if(selectValue.equals( "TSR_LOGIN" )){ 
								dbh.SaveLoginData(AccountCode, UserName, Password, MobileNumber,serverDataLoad.getServerDateTime());
							}
							if(selectValue.equals("CITY_POSTAL_CODES")){ 
								AddCityDetails();
							}
				            if(selectValue.equals("CARD_TYPES")){ 
				            	AddCardTypes();
				            }
				            if(selectValue.equals("MERCHANT_MASTER")){
				            	AddMerchantData();
				            }
				            if(selectValue.equals("CARD_DENOMINATOIN")){
				            	AddCardDenominations();
				            }
				            if(selectValue.equals("MERCHANT_INVENTORY")){
				            	AddMerchantInventoryData();
				            }
				            if(selectValue.equals("NEXT_SERIAL")){
				            	AddCardNextSerialData();
				            }
				            if(selectValue.equals("RECEIVED_CARD_BUNDLES")){
				            	AddCardBulkSerialData();
				            }
							if(selectValue.equals("SYSTEM_PROFILE")){
								AddSystemProfile();
							}
						}
					}
					LoadAllData();
				} else{
					runOnUiThread(new Runnable() {
						 public void run() {
							 ringProgressDialog_datareload.dismiss();
							 diplayAlert("Data Reload Info","Data Reload is not Done");
//							 new AlertDialog.Builder(Updates.this).setTitle("Data Reload info")
//									 .setMessage("Data Reload is not Done").setPositiveButton("Ok", null).show();
							 updateSystemSettings();
						 }
					 });
					
				}
			} catch (final Exception e) {
				runOnUiThread(new Runnable() {
					 public void run() {
						 ringProgressDialog_datareload.dismiss();
						 diplayAlert("Data Reload Info","Data Reload is not Done");
						// new AlertDialog.Builder(Updates.this).setTitle("Data Reload info").setMessage("Data Reload is not Done").setPositiveButton("Ok", null).show();
							dbh.insertError("reset and DataLoad not synced / load. Error : "+ e.getMessage());
							e.printStackTrace();
                    }
				 });
				
			}			
	}

	protected void updateSystemSettings() {
		(new SystemProfileAsyc(this)).execute(new String[] { "" });
	}

	private void AddSystemProfile() {
		TSRSystemprofile systemprofile = new TSRSystemprofile(serverDataLoad.getNext_Merchant_ID(), serverDataLoad.getNext_Invoice_ID(), "Login", serverDataLoad.getMAX_RECORDS(), serverDataLoad.getSYSTEM_URL(), serverDataLoad.getSYSTEM_AUTO_SYNCH(), serverDataLoad.getGPS_AUTO_SYNCH(), serverDataLoad.getGPS_GETTING(),"");
		Log.d("-----------------------------------", serverDataLoad.getMAX_RECORDS() +"  |  "+ serverDataLoad.getSYSTEM_URL() +"  |  "+ serverDataLoad.getSYSTEM_AUTO_SYNCH() +"  |  "+ serverDataLoad.getGPS_AUTO_SYNCH() +"  |  "+ serverDataLoad.getGPS_GETTING() +"  |  "+ systemprofile);
		dbh.UpdateMerchantsWithNewIds(systemprofile, this.getResources().getAssets());
		dbh.insertLoadData("SYSTEM_PROFILE", 1, 1, 1, 0 );
	}

	private void AddCardBulkSerialData() {
		int g = 0;
		List<ServerDataLoad_cardBulkCode> serverDataLoad_cardBulkCodes  = serverDataLoad.getServerDataLoad_cardBulkCodes();
		for (ServerDataLoad_cardBulkCode cardBulkCode : serverDataLoad_cardBulkCodes) {
			dbh.SaveBulkIds(cardBulkCode.getCardType(), cardBulkCode.getCardDenomination(), cardBulkCode.getCardBlkCode().toString(), cardBulkCode.getStartSerial().toString(), cardBulkCode.getEndSerial().toString(), cardBulkCode.getEntryDate(), cardBulkCode.getDiscountRate().doubleValue(), cardBulkCode.getIsCardAccept(), cardBulkCode.getNoOfCards());
			g++;
		}
		if ((g == serverDataLoad_cardBulkCodes.size()) && (g == serverDataLoad.getCount_BulkCard01())) {
			dbh.insertLoadData("RECEIVED_CARD_BUNDLES", serverDataLoad.getCount_BulkCard01(), serverDataLoad_cardBulkCodes.size(), g, 0 );
		} else {
			dbh.insertLoadData("RECEIVED_CARD_BUNDLES", serverDataLoad.getCount_BulkCard01(), serverDataLoad_cardBulkCodes.size(), g, 1 );
		}
	}

	private void AddCardNextSerialData() {
		List<ServerDataLoad_CardNextSerial> serverDataLoad_CardNextSerials  = serverDataLoad.getServerDataLoad_CardNextSerials();
		int f = 0;
		for (ServerDataLoad_CardNextSerial CardNextSerial : serverDataLoad_CardNextSerials) {
			dbh.SaveSerials(Integer.parseInt(CardNextSerial.getEpfID()), CardNextSerial.getCardType(), Integer.parseInt(CardNextSerial.getDenomination()) , Integer.parseInt(CardNextSerial.getBulkNo()), Integer.parseInt(CardNextSerial.getStartSerial()), Integer.parseInt(CardNextSerial.getNextSerial()), Integer.parseInt(CardNextSerial.getEndSerial()), Integer.parseInt(CardNextSerial.getIsAllSold()));
			f++;
		}
		if ((f == serverDataLoad_CardNextSerials.size()) && (f == serverDataLoad.getCount_NextSerial())) {
			dbh.insertLoadData("NEXT_SERIAL", serverDataLoad.getCount_NextSerial(), serverDataLoad_CardNextSerials.size(), f, 0 );
		} else {
			dbh.insertLoadData("NEXT_SERIAL", serverDataLoad.getCount_NextSerial(), serverDataLoad_CardNextSerials.size(), f, 1 );
		}
	}

	private void AddMerchantInventoryData() {
		int e = 0;
		List<ServerDataLoad_merchantInventoryData> serverDataLoad_merchantInventoryDatas  = serverDataLoad.getServerDataLoad_merchantInventoryDatas();
		for (ServerDataLoad_merchantInventoryData merchantInventoryData : serverDataLoad_merchantInventoryDatas) {
			dbh.SaveMerchantInventoryTableAtLogin(merchantInventoryData.getMerchantID(), merchantInventoryData.getCardType(), merchantInventoryData.getDenomination(), merchantInventoryData.getStockInHand(), merchantInventoryData.getStockCountedDate(), merchantInventoryData.getReorderLevel(), Integer.parseInt(merchantInventoryData.getActivationCount()), Integer.parseInt(merchantInventoryData.getSalesCount()));
			e++;
		}
		if ((e == serverDataLoad_merchantInventoryDatas.size()) && (e == serverDataLoad.getCount_Inventory())) {
			dbh.insertLoadData("MERCHANT_INVENTORY", serverDataLoad.getCount_Inventory(), serverDataLoad_merchantInventoryDatas.size(), e, 0 );
		} else {
			dbh.insertLoadData("MERCHANT_INVENTORY", serverDataLoad.getCount_Inventory(), serverDataLoad_merchantInventoryDatas.size(), e, 1  );
		}
	}

	private void AddCardDenominations() {
		List<ServerDataLoad_CardDenominationData> serverDataLoad_CardDenominationDatas  = serverDataLoad.getServerDataLoad_CardDenominationDatas();
		int d = 0;
		for (ServerDataLoad_CardDenominationData CardDenominationData : serverDataLoad_CardDenominationDatas) {
			dbh.SaveCardDenominations(CardDenominationData.getCARDTYPE(), CardDenominationData.getDENOMINATION().toString(), CardDenominationData.getDENOMINATION(), CardDenominationData.getDENOMINATION());
			d++;
		}
		if ((d == serverDataLoad_CardDenominationDatas.size()) && (d == serverDataLoad.getCount_Denomination())) {
			dbh.insertLoadData("CARD_DENOMINATOIN", serverDataLoad.getCount_Denomination(), serverDataLoad_CardDenominationDatas.size(), d, 0 );
		} else {
			dbh.insertLoadData("CARD_DENOMINATOIN", serverDataLoad.getCount_Denomination(), serverDataLoad_CardDenominationDatas.size(), d, 1 );
		}
	}

	private void AddMerchantData() {
		List<ServerDataLoad_Merchant_Master> serverDataLoad_Merchant_Masters  = serverDataLoad.getServerDataLoad_Merchant_Masters();
		Log.d("***************************************************", serverDataLoad_Merchant_Masters.toString());
		int c = 0;
		for (ServerDataLoad_Merchant_Master Merchant_Master : serverDataLoad_Merchant_Masters) {
			if ((Merchant_Master.getLATITUDE().isEmpty() || Merchant_Master.getLATITUDE() == null) && (Merchant_Master.getLONGITUDE().isEmpty() || Merchant_Master.getLONGITUDE() == null) ) {
				dbh.SaveMerchantDetails(Merchant_Master.getMERCHANTID(), Merchant_Master.getMERCHANTNAME(), Merchant_Master.getADDRESS(), "", "", Merchant_Master.getREGISTERDATE(), Merchant_Master.getTELEPHONE_NO(), Merchant_Master.getCITY(), Merchant_Master.getISACTIVE(), Merchant_Master.getREGISTEREDBYEPFNO().toString(), Merchant_Master.getISREGISTERED(), Merchant_Master.getISASSIGNED(), Merchant_Master.getISEDITED(),Merchant_Master.getRELOADNO());
				c++;
			} else {
				dbh.SaveMerchantDetails(Merchant_Master.getMERCHANTID(), Merchant_Master.getMERCHANTNAME(), Merchant_Master.getADDRESS(), Merchant_Master.getLATITUDE().toString(), Merchant_Master.getLONGITUDE().toString(), Merchant_Master.getREGISTERDATE(), Merchant_Master.getTELEPHONE_NO(), Merchant_Master.getCITY(), Merchant_Master.getISACTIVE(), Merchant_Master.getREGISTEREDBYEPFNO().toString(), Merchant_Master.getISREGISTERED(), Merchant_Master.getISASSIGNED(), Merchant_Master.getISEDITED(),Merchant_Master.getRELOADNO());
				c++;
			}
		}
		if ((c == serverDataLoad_Merchant_Masters.size()) && (c == serverDataLoad.getCount_Merchant())) {
			dbh.insertLoadData("MERCHANT_MASTER", serverDataLoad.getCount_Merchant(), serverDataLoad_Merchant_Masters.size(), c, 0 );
		} else {
			dbh.insertLoadData("MERCHANT_MASTER", serverDataLoad.getCount_Merchant(), serverDataLoad_Merchant_Masters.size(), c, 1 );
		}
	}

	private void AddCardTypes() {
		List<ServerDataLoad_CardType> ServerDataLoad_CardType  = serverDataLoad.getServerDataLoad_CardType();
		int b = 0;
		for (ServerDataLoad_CardType CardType2 : ServerDataLoad_CardType) {
			dbh.SaveCardTypes(CardType2.getTYPECODE(), CardType2.getTYPEDESCRIPTION(), CardType2.getCARDSTATUS(), CardType2.getACCOUNTCODE());
			b++;
		}
		if ((b == ServerDataLoad_CardType.size()) && (b == serverDataLoad.getCount_Card())) {
			dbh.insertLoadData("CARD_TYPES", serverDataLoad.getCount_Card(), ServerDataLoad_CardType.size() ,b, 0 );
		} else {
			dbh.insertLoadData("CARD_TYPES", serverDataLoad.getCount_Card(), ServerDataLoad_CardType.size() ,b, 1 );
		}
	}

	private void AddCityDetails() {
		List<ServerDataLoad_CityData> serverDataLoad_CityData  = serverDataLoad.getServerDataLoad_CityData();
		int a = 0;
		for (ServerDataLoad_CityData CityData2 : serverDataLoad_CityData) {
			dbh.SaveCityData(CityData2.getPOSTAL_CODE(), CityData2.getCITY(), CityData2.getREGION(), CityData2.getLATITUDE(), CityData2.getLONGITUDE(), CityData2.getPOST_OFFICE_NAME(), CityData2.getAREA(), CityData2.getCURRENT_CHANGES());
			a++;
		}
		if ((a == serverDataLoad_CityData.size()) && (a == serverDataLoad.getCount_City())) {
			dbh.insertLoadData("CITY_POSTAL_CODES", serverDataLoad.getCount_City() , serverDataLoad_CityData.size(), a, 0);
		} else {
			dbh.insertLoadData("CITY_POSTAL_CODES", serverDataLoad.getCount_City() , serverDataLoad_CityData.size(), a, 1);
		}
	}
	
	
	private class SystemProfileAsyc extends AsyncTask<String, Void, String> {

		private Context context;
		private HTTPServiceCalls http;
		private ProgressDialog pg;

		public SystemProfileAsyc(Context context) {
			super();
			this.context = context;
			http = new HTTPServiceCalls();
		}
		
		

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pg = ProgressDialog.show(this.context, "Please wait", "Updating Server url");
		}



		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			DatabaseHandler dbh = new DatabaseHandler(this.context);
			try {
				String epf = dbh.GetEmpNoByPhoneNo(Utils.getSimSerialNumber(this.context));
				if (!epf.equals("")) {
					String response = http.makeRequest(CONSTANTS.GET_SERVICE_URL(epf));
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
			pg.cancel();
		}



		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			DatabaseHandler dbh = new DatabaseHandler(this.context);
			pg.cancel();
			super.onPostExecute(result);
			try {
				JSONParser parser = new JSONParser();
				Object parsed = parser.parse(result);
				JSONObject objJSonAtd = (JSONObject) parsed;

				JSONObject jResponse = (JSONObject) objJSonAtd.get("response");

				int status = ((Number) jResponse.get("status")).intValue();

				if (status == 1) {
					String url = (String)jResponse.get("data");
					if (!url.trim().equals("")) {
						if(dbh.updateServiceUrl(url)){
							 Utils.ShowMsg(getBaseContext(), "Url updated");
						}
							
					}
				}

			} catch (Exception e) {
				Utils.writeToErrLogFileWithTime(e);
			} finally {
				dbh.close();
			}
		}

	}
	
	
	
}
