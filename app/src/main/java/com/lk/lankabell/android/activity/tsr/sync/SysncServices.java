package com.lk.lankabell.android.activity.tsr.sync;


import java.util.ArrayList;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;
import com.lk.lankabell.android.activity.tsr.beans.TSRSystemprofile;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

public class SysncServices {
	

	public Context context;
	private static String NAMESPACE = "http://mainService";
	private static String METHOD_NAME_TSRSystemprofile = "tsrsystemprofile";
	private static String SOAP_ACTION_TSRSystemprofile = "http://mainService/tsrsystemprofile";
	private String URL = "";
	
	public Integer TSRSystemprofile(Context applicationContext,String myPhoneNumber, String userName, String password, String epfNo, String task) {
		this.context = applicationContext;
		URL = WsdlReader.getServiceUrl(true,applicationContext);
		Integer returnVal = 0;
		DatabaseHandler dbh = new DatabaseHandler(context);
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_TSRSystemprofile);
		ArrayList<TSRSystemprofile> tsrSystemprofiles = dbh.TSRSystemprofile(task);
		//
		
		Gson gson = new Gson();
		String json = gson.toJson(tsrSystemprofiles);
		 
		
		request.addProperty("strInputUserMobile", myPhoneNumber);
		request.addProperty("strInputUserName", userName);
		request.addProperty("strInputUserPassword", password);
		request.addProperty("strInputUserEpfNo", epfNo);
		request.addProperty("systemprofileData", json);
		 
		
		Log.d("Come to the my method send values : ", json );
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		if (tsrSystemprofiles != null && !tsrSystemprofiles.isEmpty()) {
			Log.w("TSR System profile send Json Array is :  ", json);
			try {				
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION_TSRSystemprofile, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				//
				if (result != null) {
					Gson gsonResponse = new Gson();
					Log.d("Return responses in the json objet deatils :", result.toString());

					TSRSystemprofile[] tsrSystemprofiles2 = gsonResponse.fromJson(result.getProperty(0).toString(), TSRSystemprofile[].class);

					Log.w("TSR System profile Sync Response sucess.", tsrSystemprofiles2.toString());

					for (TSRSystemprofile TSRSystemprofile : tsrSystemprofiles2) {
						if (TSRSystemprofile.getTask().equals("Login")) {
							dbh.UpdateMerchantsWithNewIds(TSRSystemprofile,context.getResources().getAssets());
						} else {
							dbh.UpdateMerchantsWithSysnc();
						}				
					}
					dbh.insertStatus("TSR System System profile successfully synced");
				}
			} catch (Exception e) {
				dbh.insertError("Merchants is not synced.Error : "+ e.getMessage());
				e.printStackTrace();
				returnVal = 1;
			}
		} else {
			dbh.insertStatus("Merchants successfully synced");
			returnVal = 3;
		}
		return returnVal;
	}
	
	
}
