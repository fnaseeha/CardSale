/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lk.lankabell.android.activity.tsr.beans.NextSerialSync;
import com.lk.lankabell.android.activity.tsr.beans.SalesResponse;
import com.lk.lankabell.android.activity.tsr.beans.cardSalesDeatils;
import com.lk.lankabell.android.activity.tsr.beans.cardSalesHeader;
import com.lk.lankabell.android.activity.tsr.sqlite.CityRegistrationLimit;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
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
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;

/**
 */
public class BackgroundService extends Service {

	private Timer BackgroundSynchTimer;
	private Timer getgpsTimer;
	private Timer SynTimcher;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	Location location;
	double Latitude = 0;
	double Longitude = 0;
	protected LocationManager locationManager;
	private LocationListener locListener = new MyLocationListener();
	String currentchange = "";
	static Date savegps;
	DatabaseHandler databasehandler;
	static long lastGPSTime = 0;
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	//
	private static String NAMESPACE = "http://mainService";
	private String URL = "";
	//
	private static String SOAP_ACTION = "http://mainService/CardBulkSerialData";
	private static String SOAP_ACTION2 = "http://mainService/SaleRemarks";
	private static String SOAP_ACTION3 = "http://mainService/CardSale";
	private static String SOAP_ACTION4 = "http://mainService/MerchantInventoryDataLastUpdated";
	private static String SOAP_ACTION8 = "http://mainService/updateAcceptedBulkCardSerial";
	private static String SOAP_ACTION11 = "http://mainService/CityNewData";
	private static String SOAP_ACTION14 = "http://mainService/sendJsonDataList";
	private static String SOAP_ACTION20 = "http://mainService/sendVersion";
	private static String SOAP_ACTION21 = "http://mainService/updateCityRegistrationLimit";
	private static String SOAP_ACTION30 = "http://mainService/MerchantSynch";
	private static String SOAP_ACTION35 = "http://mainService/synchLocations";
	private static String SOAP_ACTION51 = "http://mainService/PullMerchantInventoryData";
	private static String SOAP_ACTION52 = "http://mainService/PushCardSales";

	private static String METHOD_NAME = "CardBulkSerialData";
	private static String METHOD_NAME2 = "SaleRemarks";
	private static String METHOD_NAME3 = "CardSale";
	private static String METHOD_NAME4 = "MerchantInventoryDataLastUpdated";
	private static String METHOD_NAME8 = "updateAcceptedBulkCardSerial";
	private static String METHOD_NAME11 = "CityNewData";
	private static String METHOD_NAME14 = "sendJsonDataList";
	private static String METHOD_NAME20 = "sendVersion";
	private static String METHOD_NAME21 = "updateCityRegistrationLimit";
	private static String METHOD_NAME30 = "MerchantSynch";
	private static String METHOD_NAME35 = "synchLocations";
	private static String METHOD_NAME52 = "PushCardSales";
	private static String METHOD_NAME51 = "PullMerchantInventoryData";
	private Long GPSSyncTIME;
	private Long GPSTIME;
	private Long AutoSync;

	public Long getGPSSyncTIME() {
		return GPSSyncTIME;
	}

	public void setGPSSyncTIME(Long gPSSyncTIME) {
		GPSSyncTIME = gPSSyncTIME;
	}

	public Long getGPSTIME() {
		return GPSTIME;
	}

	public void setGPSTIME(Long gPSTIME) {
		GPSTIME = gPSTIME;
	}

	public Long getAutoSync() {
		return AutoSync;
	}

	public void setAutoSync(Long autoSync) {
		AutoSync = autoSync;
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		URL = WsdlReader.getServiceUrl(true, this);
		databasehandler = new DatabaseHandler(getApplicationContext());
		setAutoSync(databasehandler.getSystemAutoSynch()); //
		setGPSSyncTIME(databasehandler.getGPSAutoSynch());
		setGPSTIME(databasehandler.getGPSGetting());

		// int GPSSynch = databasehandler.getGPSAutoSynch();
		// int AutoSynch = databasehandler.getSystemAutoSynch();
		BackgroundSynchTimer = new Timer("BackgroundServiceSynch");
		// timer.scheduleAtFixe dRate(task, delay, period);
		BackgroundSynchTimer.schedule(syncTask, 1000L, 900000); // getAutoSync()
																// 180000 5Min
																// -- 240000l 5
																// * 60 * 1000
																// GPSSynch
		// getgpsTimer = new Timer("GetCueentGPSTimer");
		// getgpsTimer.schedule(getcurrentGPS, 1000L, 7 * 60 * 1000 ); // 10Min
		// -- 600000l
		SynTimcher = new Timer("GetSynchTimer");
		SynTimcher.schedule(getSynchData, 1000L, 14400000); // getGPSSyncTIME()
															// 6hour --
															// 25200000L
															// AutoSynch 14400000
															//Auto sync testing

		try {
			Thread thread = new Thread() {
				public void run() {
					try {
						long sleaptime = 300000; // getGPSTIME() 120000
													// databasehandler.getGPSGetting();
						while (true) {
							sleep(sleaptime);
							Calendar currentTime = Calendar.getInstance();
							currentTime.setTime(new Date());
							int hour = currentTime.get(Calendar.HOUR_OF_DAY);
							if (hour <= 17 && hour >= 8) {
								locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
								isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
								if (!isGPSEnabled) {
									Date date2 = new Date();
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
									databasehandler.SaveNetworkStatus("GPS is Disable", sdf.format(date2));

									Intent intent = new Intent();
									intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
									intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
									intent.putExtra("enabled", true);
									intent.setData(Uri.parse("3"));
									sendBroadcast(intent);
								}
								locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener, Looper.getMainLooper());
								location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

								Date date2 = new Date();
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

								if (location != null) {
									Latitude = location.getLatitude();
									;
									Longitude = location.getLongitude();
									;
									double Acu = location.getAccuracy();
									double spp = location.getSpeed();
									double tim = location.getTime();
									String loca = location.getProvider();

									if (Longitude >= 0 && Latitude >= 0) {
										Date date3 = new Date();
										databasehandler.SaveGPSData(String.valueOf(Acu), String.valueOf(spp), String.valueOf(tim), loca, String.valueOf(Longitude), String.valueOf(Latitude), sdf.format(date3));
									} else {
										databasehandler.SaveNetworkStatus("GPS is Zero", sdf.format(date2));
									}
								} else {
									databasehandler.SaveNetworkStatus("Location Object Null", sdf.format(date2));
								}
								location = null;
								locationManager = null;
							}
						}
					} catch (Exception e) {
						Date date2 = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						databasehandler.SaveNetworkStatus("GPS Throw Exception", sdf.format(date2));
						e.printStackTrace();
					}
				};
			};
			// thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BackgroundSynchTimer.cancel();
		BackgroundSynchTimer = null;
		getgpsTimer.cancel();
		getgpsTimer = null;
		SynTimcher.cancel();
		SynTimcher = null;
	}

	private TimerTask getcurrentGPS = new TimerTask() {
		public void run() {
			/*
			 * locationManager =
			 * (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			 * isGPSEnabled = locationManager.isProviderEnabled(
			 * LocationManager.GPS_PROVIDER); isNetworkEnabled =
			 * locationManager.isProviderEnabled(
			 * LocationManager.NETWORK_PROVIDER); if (!isGPSEnabled){ Intent
			 * intent = new Intent();
			 * intent.setClassName("com.android.settings",
			 * "com.android.settings.widget.SettingsAppWidgetProvider");
			 * intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
			 * intent.putExtra("enabled", true); intent.setData(Uri.parse("3"));
			 * sendBroadcast(intent); } else {
			 * locationManager.requestLocationUpdates(
			 * LocationManager.GPS_PROVIDER, 0, 0, locListener,
			 * Looper.getMainLooper()); location =
			 * locationManager.getLastKnownLocation
			 * (LocationManager.GPS_PROVIDER); if (location != null) { double d
			 * = location.getLatitude(); double d1 = location.getLongitude();
			 * 
			 * double Acu = location.getAccuracy(); double spp =
			 * location.getSpeed(); double tim = location.getTime(); String loca
			 * = location.getProvider();
			 * 
			 * DatabaseHandler databasehandler = new
			 * DatabaseHandler(getApplicationContext()); String s = (new
			 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
			 * 
			 * Calendar currentTime = Calendar.getInstance();
			 * currentTime.setTime(new Date()); int hour =
			 * currentTime.get(Calendar.HOUR_OF_DAY); if (hour <= 17 && hour >=
			 * 8){ //8-5
			 * databasehandler.SaveGPSData(String.valueOf(Acu),String.valueOf
			 * (spp),String.valueOf(tim),loca,String.valueOf(d),
			 * String.valueOf(d1), "");
			 * //databasehandler.SaveGPSData(String.valueOf(d),
			 * String.valueOf(d1), s); } location = null; locationManager =
			 * null; } }
			 */
		}
	};

	private TimerTask syncTask = new TimerTask() {
		public void run() {
			DatabaseHandler databasehandler = new DatabaseHandler(getApplicationContext());
			if (isOnline() == true) {
				Date date2 = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				// databasehandler.SaveNetworkStatus("Connect",sdf.format(date2));
				synchLocations();
				SendNetworkStatus();
			} else {
				Date date2 = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				// databasehandler.SaveNetworkStatus("DisConnect",sdf.format(date2));
			}
		}
	};

	private TimerTask getSynchData = new TimerTask() {
		public void run() {
			DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
			if (isOnline() == true) {
				Date date2 = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				// dbh.SaveNetworkStatus("Connect",sdf.format(date2));
				Calendar currentTime = Calendar.getInstance();
				currentTime.setTime(new Date());
				int hour = currentTime.get(Calendar.HOUR_OF_DAY);

				if (hour <= 17 && hour >= 8) {
					Integer Systemprofile = 0, merchant = 0, Version = 0, CardBulkSerial = 0, SaleData = 0, CardAccept = 0, StockDataFromRemote = 0, g = 0, UpdatedCities = 0, NextSerialDetails = 0, updateMearchanInventory = 0, j = 0, updatecardsales = 0;
					dbh.clearStatusLog();
					dbh.clearErrorLog();
					dbh.clearSynchLog();
					postSync postSync = new postSync();
					postSync.presynch(dbh);
					//
					Log.w("Method 0", "TSR System profile Data id Sysnc");
					User user = dbh.getUserDetails();
					try {
						String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
						Systemprofile = postSync.TSRSystemprofile(getApplicationContext(), getMyPhoneNumber(), user.getUserName(), user.getPassword(), epfNo, "Sysnc");
					} catch (Exception e) {

					}
					Log.w("Method 2", "synchMerchants");
					merchant = synchMerchants(); // ok
					Log.w("Method 1", "sendVersion");
					Version = sendVersion();
					Log.w("Method 3", "getCardBulkSerialData");
					CardBulkSerial = getCardBulkSerialData();
					Log.w("Method 4", "SetSaleData");
					SaleData = PushCardSales();
					Log.w("Method 5", "setUpdateCardAcceptTag");
					CardAccept = setUpdateCardAcceptTag(); // ok
					Log.w("Method 6", "updateStockDataFromRemote");
					StockDataFromRemote = updateStockDataFromRemote(); // ok
					Log.w("Method 7", "SetRemarksData");
					g = SetRemarksData(); // ok
					Log.w("Method 8", "getUpdatedCities");
					UpdatedCities = getUpdatedCities();
					Log.w("Method 9", "getNextSerialDetails");
					NextSerialDetails = getNextSerialDetails(); // ok
					Log.w("Method 10", "updateCityRegistrationLimit");
					j = updateCityRegistrationLimit();
					Log.w("Method 11", "synchLocation");
					// ___________________________________________________________________________________
					Log.w("Method 12", "synchCardSales");
					Log.w("Method 51", "update mearchant Inventory Data.");
					updateMearchanInventory = PullMerchantInventoryData();
					Log.w("Method 52", "update mearchant Activations Data.");
					MearchantActivation();
					Log.w("Method 53", "Send the Status Data.");
					SendNetworkStatus();//######
					postSync.postsynch(dbh, merchant, Version, CardBulkSerial, SaleData, CardAccept, StockDataFromRemote, g, UpdatedCities, NextSerialDetails);
					List<ResultCode> finalResponse = dbh.getFinalResponse();
					for (ResultCode resultCode : finalResponse) {
						if (resultCode.getResponseCode() == 2) {
							dbh.UpdateLastSysnhDate();
						}
					}
				}
			} else {
				Date date2 = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				// dbh.SaveNetworkStatus("DisConnect",sdf.format(date2));
			}
		}
	};

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

	private String getMyPhoneNumber() {
		return Utils.getSimSerialNumber(this);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	private Integer sendVersion() {
		Integer returnVal = 0;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME20);
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		Double version = dbh.getVersion();
		if (version != 0.0) {
			String versionString = version.toString();
			request.addProperty("strInputUserMobile", getMyPhoneNumber());
			request.addProperty("strInputUserName", user.getUserName());
			request.addProperty("strInputUserPassword", user.getPassword());
			request.addProperty("version", versionString);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			try {
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
				returnVal = 1;
				dbh.insertError("Version not synced.Error : " + e.getMessage());
			}
		} else {
			dbh.insertStatus("version already synched");
			returnVal = 3;
		}
		return returnVal;
	}

	private void synchLocations() {

		if (true)
			return;

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
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION35, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				Gson gsonResponse = new Gson();
				Type type = new TypeToken<ArrayList<String>>() {
				}.getType();
				ArrayList<String> list = gsonResponse.fromJson(result.getProperty(0).toString(), type);
				for (String string : list) {
					dbh.updateSynchLocations(string);
					Log.w("success", "locations : " + locationsList.toString() + " updated in server.");
				}
			}
		} catch (Exception e) {
			Log.w("error", "locations not updated .");
		}
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
				request.addProperty("ACCOUNT_CODE", Integer.toString(user.getId()));
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
							dbh.UpdateCardAcceptResponse(bulkIdData.Gettype(), bulkIdData.GetDenomination(), bulkIdData.GetBulkId(), Integer.parseInt(bulkIdData.GetStartSerial()), Integer.parseInt(bulkIdData.GetEndSerial()));
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
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		User user = dbh.getUserDetails();
		String date = dbh.GetLastUpdatedDate();

		Integer returnVal = 0;
		Log.d("CardBulkSerialData send Json Array is :  ", request.toString());
		try {

			request.addProperty("strInputUserMobile", getMyPhoneNumber());
			request.addProperty("strInputUserName", user.getUserName());
			request.addProperty("strInputUserPassword", user.getPassword());
			request.addProperty("strEntryDate", date);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn; // return epf No
			if (result != null) {
				Log.d("Return responses in the json objet deatils :", result.toString());
				String dbQuery8 = result.getProperty(0).toString();
				String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
				String[] statusarray8 = dbQuery8String.split(",");
				String[] statusarrayData8 = dbQuery8String.split("<");
				int statusvalue8 = 0;
				try {
					statusvalue8 = Integer.parseInt(statusarray8[0].toString());
				} catch (Exception e) {
				}
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String dateValue = sdf1.format(System.currentTimeMillis());
				if (statusvalue8 == 1) {
					if (statusarray8[1].length() > 1) {
						for (int i = 1; i < statusarrayData8.length; i++) {
							String[] datavaluesStrings = statusarrayData8[i].toString().split(">");
							String[] datavalues = datavaluesStrings[0].split(",");
							if (dbh.CheckCardBulkRecordIsExists(datavalues[3].toString(), Integer.parseInt(datavalues[4].toString().replaceAll("\\]|\\>", "")), datavalues[0].toString(), datavalues[1].toString(), datavalues[2].toString()) == false) {
								dbh.SaveBulkIds(datavalues[3].toString(), Integer.parseInt(datavalues[4].toString().replaceAll("\\]|\\>", "")), datavalues[0].toString(), datavalues[1].toString(), datavalues[2].toString(), dateValue, Double.parseDouble(datavalues[6].toString()), Integer.parseInt(datavalues[7].toString()), Integer.parseInt(datavalues[8].toString()));
							}
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
		return returnVal;
	}

	public Integer updateStockDataFromRemote() {
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
						dbh.UpdateMerchantInventoryTableAfterActivation(item.getMerchantId(), item.getCardType(), item.getDenomination(), item.getStockInHand(), item.getReorderLevel(), item.getActivationChanges());
					}
					returnVal = 2;
					if (inventories.size() <= inventorySynchsList.length) {
						returnVal = 2;
					} else {
						returnVal = 1;
					}
					dbh.insertStatus("Inventory successfully synced");
				}
			} catch (Exception e) {
				returnVal = 1;
				dbh.insertError("Inventoty not synced. Error : " + e.getMessage());
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
				dbh.insertError("Remarks not synced.Error : " + e.getMessage());
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

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
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
					dbh.insertError("Cards Seles is not synced.Error : " + e.getMessage());
					e.printStackTrace();
				}
			}
		} else {
			returnVal = 3;
			dbh.insertStatus("CardSales already synched");
		}
		return returnVal;
	}

	public Integer synchMerchants() {
		Integer returnVal = 0;
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME30);
		User user = dbh.getUserDetails();

		if (user == null) {
			return 3;
		}

		ArrayList<MerchantNew> merchantNewList = dbh.GetNonSynchMerchants();
		//
		Gson gson = new Gson();
		String json = gson.toJson(merchantNewList);
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("merchantData", json);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		if (merchantNewList != null && !merchantNewList.isEmpty()) {
			Log.w("Merchants send Json Array is :  ", json);
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION30, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				if (result != null) {
					Gson gsonResponse = new Gson();
					Log.d("Return responses in the json objet deatils :", result.toString());
					MerchantNewResponse[] merchantResponseList = gsonResponse.fromJson(result.getProperty(0).toString(), MerchantNewResponse[].class);
					Log.w("Merchants Sync Response sucess.", merchantResponseList.toString());
					for (MerchantNewResponse item : merchantResponseList) {
						dbh.UpdateMerchantsWithNewIds(item);
						// dbh.updateMerchantsAsSynch();
					}
					dbh.insertStatus("Merchants successfully synced");
					if (merchantResponseList.length == merchantNewList.size()) {
						returnVal = 2;
					} else {
						returnVal = 1;
					}
				}
			} catch (Exception e) {
				dbh.insertError("Merchants is not synced.Error : " + e.getMessage());
				e.printStackTrace();
				returnVal = 1;
			}
		} else {
			dbh.insertStatus("Merchants successfully synced");
			returnVal = 3;
		}
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
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			try {
				Log.w("Merchants send Json Array is :  ", request.toString());
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
								String[] datavaluesStrings = statusarrayData5[i].toString().split(">");
								String[] datavalues = datavaluesStrings[0].split(",");
								if (datavalues[3].toString() == null) {
									datavalues[3] = "0.0";
								}
								if (datavalues[4].toString() == null) {
									datavalues[4] = "0.0";
								}
								if (!list.contains(datavalues[1].toString())) {
									dbhN.SaveCityData(datavalues[0].toString(), datavalues[1].toString(), datavalues[2].toString(), datavalues[3].toString(), datavalues[4].toString(), datavalues[5].toString(), datavalues[6].toString());
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

	private Integer getNextSerialDetails() {
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
					NextSerialSync[] nextSerialSyncs = gsonResponse.fromJson(result.getProperty(0).toString(), NextSerialSync[].class);
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
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
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
							dbhN.UpdateTsrCityLimitList(item.getPostalCodes(), item.getMaxLimit());
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
				dbhN.insertError("City Registration Limit not synced.Error : " + e.getMessage());
				e.printStackTrace();
			}
		}
		return returnVal;
	}

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
					dbh.UpdateMerchantInventoryTableAfterActivation(item.getMerchantId(), item.getCardType(), item.getDenomination(), item.getStockInHand(), item.getReorderLevel(), item.getActivationChanges());
				}
				dbh.insertStatus("Inventory updateted successfully synced");
			}
		} catch (Exception e) {
			returnVal = 1;
			dbh.insertError("Inventoty not synced. Error : " + e.getMessage());
			e.printStackTrace();
		}
		return returnVal;
	}

	private Integer PushCardSales() {
		Integer returnVal = 0;
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		User user = dbh.getUserDetails();
		ArrayList<cardSalesHeader> cardSalesHeaders = dbh.cardSalesHeader();
		Gson gson = new Gson();
		String salesHeaders = gson.toJson(cardSalesHeaders);
		for (cardSalesHeader cardSalesHeader : cardSalesHeaders) {
			Log.d("This is the bean class details : ", cardSalesHeader.getEnteredBy() + " | " + cardSalesHeader.getCardSalesDeatils() + " | " + cardSalesHeader.getMerchantId());
			ArrayList<cardSalesDeatils> arrayList = cardSalesHeader.getCardSalesDeatils();
			for (cardSalesDeatils cardSalesDeatils : arrayList) {
				Log.d("Bean class deatils ******************* : ", cardSalesDeatils.getCardType() + " | " + cardSalesDeatils.getStartSerial() + " | " + cardSalesDeatils.getBulkNo() + " | " + cardSalesDeatils.getDenomination() + " | " + cardSalesDeatils.getInvoiceId() + " | " + cardSalesDeatils.getDetailsId());
			}
		}

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME52);
		request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("strInputSalesHeader", salesHeaders);
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
			dbh.insertError("Inventoty not synced. Error : " + e.getMessage());
			e.printStackTrace();
		}
		return returnVal;
	}

	private static String SOAP_ACTION55 = "http://mainService/MearchantActivation";
	private static String METHOD_NAME55 = "MearchantActivation";

	private Integer MearchantActivation() {
		Integer returnVal = 0;
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
			dbh.insertError("Inventoty not synced. Error : " + e.getMessage());
			e.printStackTrace();
		}
		Log.d("This is the card sales sucess messages : ", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + returnVal);
		return returnVal;
	}

	private static String SOAP_ACTION56 = "http://mainService/SendNetworkStatus";
	private static String METHOD_NAME56 = "SendNetworkStatus";

	private void SendNetworkStatus() {
		try {
			DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
			User user = dbh.getUserDetails();
			List<String> networkStatus = dbh.GetNetworkStatus();
			if (networkStatus.size() < 1) {
				return;
			}
			Gson gson = new Gson();

			if (networkStatus.size() > CONSTANTS.MAX_LENGTH_ARRAY_LIST) {
				networkStatus = networkStatus.subList(0, CONSTANTS.MAX_LENGTH_ARRAY_LIST);
			}

			String networkstatusList = gson.toJson(networkStatus);

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME56);

			try {
				request.addProperty("strInputUserMobile", getMyPhoneNumber());
				request.addProperty("strInputUserName", user.getUserName());
				request.addProperty("strInputUserPassword", user.getPassword());
				request.addProperty("synchNetworkStatus", networkstatusList);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				Utils.writeToErrLogFileWithTime(e1);
				return;
			}

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION56, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn; // SoapFault
				if (result != null) {
					Gson gsonResponse = new Gson();
					Type type = new TypeToken<ArrayList<String>>() {
					}.getType();
					ArrayList<String> list = gsonResponse.fromJson(result.getProperty(0).toString(), type);
					for (String string : list) {
						dbh.updateNetworkStatus(string);
					}
				}
			} catch (Exception e) {
				Utils.writeToErrLogFileWithTime(e);
			}
		} catch (Exception e) {
			Utils.writeToErrLogFileWithTime(e);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// ==================================================================GPS====================================================================
	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			// locationManager.removeUpdates(locListener);
			loc.getLatitude();
			loc.getLongitude();

			Latitude = loc.getLatitude();
			Longitude = loc.getLongitude();

			LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new MyLocationListener(), Looper.getMainLooper());
			location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
			}
		}

		public void onProviderDisabled(String provider) {
			Intent intent = new Intent();
			intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
			intent.setData(Uri.parse("3"));
			sendBroadcast(intent);

			// Intent intent1 = new
			// Intent("android.location.GPS_ENABLED_CHANGE");
			// intent1.putExtra("enabled", true);
			// sendBroadcast(intent1);
		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	}

}
