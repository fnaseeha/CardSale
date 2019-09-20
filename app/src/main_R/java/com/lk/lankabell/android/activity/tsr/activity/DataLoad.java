package com.lk.lankabell.android.activity.tsr.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.google.gson.Gson;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_CardDenominationData;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_CardNextSerial;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_CardType;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_CityData;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_Merchant_Master;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_cardBulkCode;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad_merchantInventoryData;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DataLoad extends Activity {

	private DatabaseHandler dbh;
	boolean network_enable;
	private static String SOAP_ACTION51 = "http://mainService/LoadDataInServertToMobile";
	private static String METHOD_NAME51 = "LoadDataInServertToMobile";
	private String URL = WsdlReader.getServiceUrl(true,this);
	private static String NAMESPACE = "http://mainService";

	private boolean isOnline() {
		boolean connected = false;
		final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			connected = true;
		} else {
			connected = false;
		} 
		return connected;
	}
	
	public void DataLoading() {
		network_enable = isOnline();
		if (network_enable){
			boolean sysnch = CheckAllDataIsSysnch();
			if (sysnch) {
				LoadDataInServertToMobile();
				int finalResponse = dbh.getFinalResetData();
				if (finalResponse == 1) {
					new AlertDialog.Builder(DataLoad.this).setTitle("Reset info").setMessage("Your mobile Data reset is Done").setPositiveButton("Ok", null).show();
				} else{
					new AlertDialog.Builder(DataLoad.this).setTitle("Reset info").setMessage("Your mobile Data reset is not Done").setPositiveButton("Ok", null).show();
				}
			} else {
				new AlertDialog.Builder(DataLoad.this).setTitle("synch info")
				.setMessage("Please Synch Your Mobile Data.").setPositiveButton("Ok", null)
				.show();	
			}
		} else {
			new AlertDialog.Builder(DataLoad.this).setTitle("Network info")
			.setMessage("No 3G Signals").setPositiveButton("Ok", null)
			.show();			
		}
	}

	private boolean CheckAllDataIsSysnch() {
		String CheckingQuery = "SELECT SUM(synch_count) as synch_count ,SUM(total_count) as total_count  FROM (Select  sum(is_synch) as synch_count, count(*) as total_count from merchant_master " +
				"union Select  sum(is_synch) as synch_count, count(*) as total_count from sales_details " +
				"union Select sum(is_synch) as synch_count, count(*) as total_count from sales_header " +
				"union select sum(is_synch) as synch_count, count(is_synch) as total_count from next_serial " +
				"union select  sum(is_synch) as synch_count, count(*) as total_count from TSR_RECEIVED_CARD_BUNDLES " +
				"union select  sum(is_synch) as synch_count, count(*) as total_count from CITY_POSTAL_CODES " +
				"union select sum(is_synch) as synch_count, count(*) as total_count from MERCHANT_INVENTORY )";
		SQLiteDatabase db1 = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Cursor cursor = db1.rawQuery(CheckingQuery,null);
		Integer synch_count = -1;
		Integer total_count = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			synch_count = cursor.getInt(0);
			total_count = cursor.getInt(1);
		}
		if (synch_count == total_count) {
			return false;
		} else {
			return true;
		}		
	}
	
	private String getMyPhoneNumber() {
		return Utils.getSimSerialNumber(this);
	}
	
	private void LoadDataInServertToMobile() {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME51);
		dbh = new DatabaseHandler(getApplicationContext());
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
					ServerDataLoad[] inventorySynchsList = gsonResponse.fromJson(result.getProperty(0).toString(), ServerDataLoad[].class);
					for (ServerDataLoad serverDataLoad : inventorySynchsList) {
						boolean deletedb = getApplicationContext().deleteDatabase("TSRDBNEW");
						if (deletedb) {
							Log.d("**********************************", ""+deletedb);
						}
						//
						Date ServerDateTime = new SimpleDateFormat("MM/dd/yyyy").parse(serverDataLoad.getServerDateTime());
						String AccountCode  = serverDataLoad.getAccountCode();
						String MobileNumber = serverDataLoad.getMobileNumber();
						String UserName  = serverDataLoad.getUserName();
						String Password  = serverDataLoad.getPassword();
						//
						/*
						SQLiteDatabase db = this.getReadableDatabase();
						String sql = "INSERT INTO TSR_SYSTEM_PROFILE (IS_SYNCH, NEXT_MERCHANT_NO, NEXT_INVOICE_NO) VALUES ('1','10001','100001') ";
						}
						Log.d("TThis is the system data query : ", sql);
						db.execSQL(sql);
						db.close();
						*/
						//
						dbh = new DatabaseHandler(getApplicationContext());
						dbh.SaveLoginData(AccountCode, UserName, Password, MobileNumber,serverDataLoad.getServerDateTime());
						//
						List<ServerDataLoad_CityData> serverDataLoad_CityData  = serverDataLoad.getServerDataLoad_CityData();
						int a = 0;
						for (ServerDataLoad_CityData CityData2 : serverDataLoad_CityData) {
							dbh.SaveCityData(CityData2.getPOSTAL_CODE(), CityData2.getCITY(), CityData2.getREGION(), CityData2.getLATITUDE(), CityData2.getLONGITUDE(), CityData2.getPOST_OFFICE_NAME(), CityData2.getAREA(), CityData2.getCURRENT_CHANGES());
							a++;
						}
						if ((a == serverDataLoad_CityData.size()) && (a == serverDataLoad.getCount_City())) {
							dbh.insertLoadData("CITY_POSTAL_CODES", serverDataLoad.getCount_City() , serverDataLoad_CityData.size(), a, 1);
						} else {
							dbh.insertLoadData("CITY_POSTAL_CODES", serverDataLoad.getCount_City() , serverDataLoad_CityData.size(), a, 0);
						}
						
						//
						List<ServerDataLoad_CardType> ServerDataLoad_CardType  = serverDataLoad.getServerDataLoad_CardType();
						int b = 0;
						for (ServerDataLoad_CardType CardType2 : ServerDataLoad_CardType) {
							dbh.SaveCardTypes(CardType2.getTYPECODE(), CardType2.getTYPEDESCRIPTION(), CardType2.getCARDSTATUS(), CardType2.getACCOUNTCODE());
							b++;
						}
						if ((b == ServerDataLoad_CardType.size()) && (b == serverDataLoad.getCount_Card())) {
							dbh.insertLoadData("CARD_TYPES", serverDataLoad.getCount_Card(), ServerDataLoad_CardType.size() ,b, 1 );
						} else {
							dbh.insertLoadData("CARD_TYPES", serverDataLoad.getCount_Card(), ServerDataLoad_CardType.size() ,b, 0 );
						}
						//
						List<ServerDataLoad_Merchant_Master> serverDataLoad_Merchant_Masters  = serverDataLoad.getServerDataLoad_Merchant_Masters();
						int c = 0;
						for (ServerDataLoad_Merchant_Master Merchant_Master : serverDataLoad_Merchant_Masters) {
							dbh.SaveMerchantDetails(Merchant_Master.getMERCHANTID(), Merchant_Master.getMERCHANTNAME(), Merchant_Master.getADDRESS(), Merchant_Master.getLATITUDE().toString(), Merchant_Master.getLONGITUDE().toString(), Merchant_Master.getREGISTERDATE(), Merchant_Master.getTELEPHONE_NO(), Merchant_Master.getCITY(), Merchant_Master.getISACTIVE(), Merchant_Master.getREGISTEREDBYEPFNO().toString(), Merchant_Master.getISREGISTERED(), Merchant_Master.getISASSIGNED(), Merchant_Master.getISEDITED(),Merchant_Master.getRELOADNO());
							c++;
						}
						if ((c == serverDataLoad_Merchant_Masters.size()) && (c == serverDataLoad.getCount_Merchant())) {
							dbh.insertLoadData("MERCHANT_MASTER", serverDataLoad.getCount_Merchant(), serverDataLoad_Merchant_Masters.size(), c, 1 );
						} else {
							dbh.insertLoadData("MERCHANT_MASTER", serverDataLoad.getCount_Merchant(), serverDataLoad_Merchant_Masters.size(), c, 0 );
						}
						//
						List<ServerDataLoad_CardDenominationData> serverDataLoad_CardDenominationDatas  = serverDataLoad.getServerDataLoad_CardDenominationDatas();
						int d = 0;
						for (ServerDataLoad_CardDenominationData CardDenominationData : serverDataLoad_CardDenominationDatas) {
							dbh.SaveCardDenominations(CardDenominationData.getCARDTYPE(), CardDenominationData.getDENOMINATION().toString(), CardDenominationData.getDENOMINATION(), CardDenominationData.getDENOMINATION());
							d++;
						}
						if ((d == serverDataLoad_CardDenominationDatas.size()) && (d == serverDataLoad.getCount_Denomination())) {
							dbh.insertLoadData("CARD_DENOMINATOIN", serverDataLoad.getCount_Denomination(), serverDataLoad_CardDenominationDatas.size(), d, 1 );
						} else {
							dbh.insertLoadData("CARD_DENOMINATOIN", serverDataLoad.getCount_Denomination(), serverDataLoad_CardDenominationDatas.size(), d, 0 );
						}						
						//
						int e = 0;
						List<ServerDataLoad_merchantInventoryData> serverDataLoad_merchantInventoryDatas  = serverDataLoad.getServerDataLoad_merchantInventoryDatas();
						for (ServerDataLoad_merchantInventoryData merchantInventoryData : serverDataLoad_merchantInventoryDatas) {
							dbh.SaveMerchantInventoryTableAtLogin(merchantInventoryData.getMerchantID(), merchantInventoryData.getCardType(), merchantInventoryData.getDenomination(), merchantInventoryData.getStockInHand(), merchantInventoryData.getStockCountedDate(), merchantInventoryData.getReorderLevel(), Integer.parseInt(merchantInventoryData.getActivationCount()), Integer.parseInt(merchantInventoryData.getSalesCount()));
							e++;
						}
						if ((e == serverDataLoad_merchantInventoryDatas.size()) && (e == serverDataLoad.getCount_Inventory())) {
							dbh.insertLoadData("MERCHANT_INVENTORY", serverDataLoad.getCount_Inventory(), serverDataLoad_merchantInventoryDatas.size(), e, 1 );
						} else {
							dbh.insertLoadData("MERCHANT_INVENTORY", serverDataLoad.getCount_Inventory(), serverDataLoad_merchantInventoryDatas.size(), e, 0  );
						}						
						//
						List<ServerDataLoad_CardNextSerial> serverDataLoad_CardNextSerials  = serverDataLoad.getServerDataLoad_CardNextSerials();
						int f = 0;
						for (ServerDataLoad_CardNextSerial CardNextSerial : serverDataLoad_CardNextSerials) {
							dbh.SaveSerials(Integer.parseInt(CardNextSerial.getEpfID()), CardNextSerial.getCardType(), Integer.parseInt(CardNextSerial.getDenomination()) , Integer.parseInt(CardNextSerial.getBulkNo()), Integer.parseInt(CardNextSerial.getStartSerial()), Integer.parseInt(CardNextSerial.getNextSerial()), Integer.parseInt(CardNextSerial.getEndSerial()), Integer.parseInt(CardNextSerial.getIsAllSold()));
							f++;
						}
						if ((f == serverDataLoad_CardNextSerials.size()) && (f == serverDataLoad.getCount_NextSerial())) {
							dbh.insertLoadData("next_serial_value", serverDataLoad.getCount_NextSerial(), serverDataLoad_CardNextSerials.size(), f, 1 );
						} else {
							dbh.insertLoadData("next_serial_value", serverDataLoad.getCount_NextSerial(), serverDataLoad_CardNextSerials.size(), f, 0 );
						}						
						//
						int g = 0;
						List<ServerDataLoad_cardBulkCode> serverDataLoad_cardBulkCodes  = serverDataLoad.getServerDataLoad_cardBulkCodes();
						for (ServerDataLoad_cardBulkCode cardBulkCode : serverDataLoad_cardBulkCodes) {
							dbh.SaveBulkIds(cardBulkCode.getCardType(), cardBulkCode.getCardDenomination(), cardBulkCode.getCardBlkCode().toString(), cardBulkCode.getStartSerial().toString(), cardBulkCode.getEndSerial().toString(), cardBulkCode.getEntryDate(), cardBulkCode.getDiscountRate().doubleValue(), cardBulkCode.getIsCardAccept(), cardBulkCode.getNoOfCards());
							g++;
						}
						if ((g == serverDataLoad_cardBulkCodes.size()) && (g == serverDataLoad.getCount_Denomination())) {
							dbh.insertLoadData("next_serial_value", serverDataLoad.getCount_BulkCard01(), serverDataLoad_cardBulkCodes.size(), g, 1 );
						} else {
							dbh.insertLoadData("next_serial_value", serverDataLoad.getCount_BulkCard01(), serverDataLoad_cardBulkCodes.size(), g, 0 );
						}
					}				
				}
			} catch (Exception e) {
				dbh.insertError("reset and DataLoad not synced / load. Error : "+ e.getMessage());
				e.printStackTrace();
			}
	}
	
}
