package com.lk.lankabell.android.activity.tsr.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HTTPServiceCalls {
	
	
	private static String SOAP_ACTION1 = "http://mainService/LoginAuthentication";
	private static String SOAP_ACTION20 = "http://mainService/loginBlock";
	
	private static String NAMESPACE = "http://mainService";

	private static String METHOD_NAME1 = "LoginAuthentication"; 
	private static String METHOD_NAME20 = "loginBlock";
	 

	public static boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) { 
			return false;
		} else{			
			return true;
		}
	}

	public static void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final Class conmanClass = Class.forName(conman.getClass().getName());
		final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
		connectivityManagerField.setAccessible(true);
		final Object connectivityManager = connectivityManagerField.get(conman);
		final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
		final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);

		setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
	}

//	public String makeRequest(String path, String data) throws Exception {
//
//		HttpParams httpParameters = new BasicHttpParams();
//		// Set the timeout in milliseconds until a connection is established.
//		// The default value is zero, that means the timeout is not used. 
//		int timeoutConnection = 8000;
//		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//		// Set the default socket timeout (SO_TIMEOUT) 
//		// in milliseconds which is the timeout for waiting for data.
//		int timeoutSocket = 8000;
//		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//		
//		
//		
//		HttpClient client = new DefaultHttpClient(httpParameters);
//		
//		HttpPost postUrl = new HttpPost(path);
//		if (!data.equals("")) {
//			List<NameValuePair> values = new ArrayList<NameValuePair>();
//			values.add(new BasicNameValuePair("data", data));
//			postUrl.setEntity(new UrlEncodedFormEntity(values));
//		}
//		HttpResponse response = client.execute(postUrl);
//		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//		String lines = "";
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			lines += line;
//		} 
//		return lines;
//
//	}
	public String makeRequest(String path, String[] names, String[] values) throws Exception {
		
		
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 8000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 8000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
		
		
		HttpClient client = new DefaultHttpClient(httpParameters);
		
		HttpPost postUrl = new HttpPost(path);
		if (names != null) {
			if(names.length != values.length){
				return "{\"response\":{\"status\":0,\"data\":null,\"msg\":\"Error while requesting data\"}}";
			} 
			
			if (names.length > 0) {
				List<NameValuePair> namepairevalues = new ArrayList<NameValuePair>();

				for (int index = 0; index < names.length; index++) {
					namepairevalues.add(new BasicNameValuePair(names[index], values[index]));
				}

				postUrl.setEntity(new UrlEncodedFormEntity(namepairevalues));
			}
		}
		HttpResponse response = client.execute(postUrl);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String lines = "";
		String line = "";
		while ((line = rd.readLine()) != null) {
			lines += line;
		} 
		return lines;
		
	}
	
	public String makeRequest(String path, String strData)  throws Exception{
		return makeRequest(path, new String[]{"data"}, new String[]{strData});
	}
	public String makeRequest(String path)  throws Exception{
		return makeRequest(path, null, null);
	}
	
	
	public String getTSRLogin(String mobile,String username,String password){
		
		
		String URL_TSR_SERVICE = WsdlReader.getServiceUrl();
		
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
			request.addProperty("strInputUserMobile", mobile);
			request.addProperty("strInputUserName", username);
			request.addProperty("strInputUserPassword", password);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_TSR_SERVICE);
			androidHttpTransport.call(SOAP_ACTION1, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			Log.d("Come the Body in ser" , result.toString());
			if (result != null) {
				String result_Value = result.getProperty(0).toString(); 
				Log.d("*******************", "*****************************************************"+result_Value);
  		        return result_Value;
			}  
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return "0"; 
		
	}

	

}
