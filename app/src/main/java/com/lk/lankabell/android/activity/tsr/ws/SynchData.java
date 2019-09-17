package com.lk.lankabell.android.activity.tsr.ws;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lk.lankabell.android.activity.tsr.beans.SalesResponse;
import com.lk.lankabell.android.activity.tsr.beans.login.ServerDataLoad;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.sync.SynchBean;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;

public class SynchData {

	private static String SOAP_ACTION4 = "http://mainService/Backgroundsynch";
	private static String METHOD_NAME4 = "Backgroundsynch";
	private static String NAMESPACE = "http://mainService";
	static ServerDataLoad serverDataLoad;
	private String URL = WsdlReader.getServiceUrl();
	public Context context;

	public Integer updateStockDataFromRemote() {
		Integer returnVal = 0;
		DatabaseHandler dbh = new DatabaseHandler(context);
		User user = dbh.getUserDetails();
		SynchBean synchbeen = new SynchBean(); 
		Double version = dbh.getVersion();
		if (version != 0.0) {
			synchbeen.setVersion(version.toString());
		}
		//synchbeen.setMobileNumber(getMyPhoneNumber());
		synchbeen.setUserName(user.getUserName());
		synchbeen.setPassword(user.getPassword());
		// ............................................................................
		Log.w("Method 1", "synchProfilre");
		synchbeen.setSystemprofiles(dbh.TSRSystemprofile("Sysnc"));
		Log.w("Method 2", "synchMerchants");
		synchbeen.setMerchants(dbh.GetNonSynchMerchants());
		Log.w("Method 3", "synchCardSales");
		synchbeen.setSalesData(dbh.cardSalesHeader());
		Log.w("Method 4", "synchCardAcceptTag");
		synchbeen.setCardAccepts(dbh.GetCardAcceptData());
		Log.w("Method 5", "updateStockDataFromRemote");
		synchbeen.setInventorySynchs(dbh.GetNonSynchMerchantInventory());
		Log.w("Method 6", "SetRemarksData");
		synchbeen.setSynchRemarks(dbh.SyncRemarkData());
		Log.w("Method 7", "synchNextSerialDetails");
		synchbeen.setNextserialDetails(dbh.getNextSerialDetails());
		Log.w("Method 8", "synchCityRegistration");
		synchbeen.setCityRegistrations(dbh.CityRegistrationDetails());
		// >>>>>>>>>>>>>>>>>............Log.w("Method 3", "getCardBulkSerialData");
		//Log.w("Method 7", "getUpdatedCities");
		//................................................................................
		Gson gson = new Gson();
		String SynchBeandata = gson.toJson(synchbeen);
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);
		//request.addProperty("strInputUserMobile", getMyPhoneNumber());
		request.addProperty("strInputUserName", user.getUserName());
		request.addProperty("strInputUserPassword", user.getPassword());
		request.addProperty("strsynchdata", SynchBeandata);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION4, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				Gson gsonResponse = new Gson();
				SalesResponse[] salesResponses = gsonResponse.fromJson(result.getProperty(0).toString(), SalesResponse[].class);		
				Log.d("Return responses in the json objet deatils :", result.toString());
				for (SalesResponse salesResponse : salesResponses) {
					dbh.UpdateSalesHeaderTable(salesResponse);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnVal;
	}

	
}
