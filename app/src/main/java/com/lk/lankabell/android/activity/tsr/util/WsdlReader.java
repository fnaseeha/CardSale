package com.lk.lankabell.android.activity.tsr.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WsdlReader extends Activity {

	private DatabaseHandler dbHandler;
	private static String systeUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dbHandler = new DatabaseHandler(this);
		systeUrl = dbHandler.getServiceUrl();
		Log.v("WsdlReader systeUrl", systeUrl);
	}

	public static String getServiceUrl() {
		Properties prop = new Properties();
		String filePath = "";
		try {
			InputStream inputStream = WsdlReader.class.getClassLoader().getResourceAsStream("wsdl.properties");
			prop.load(inputStream);
			filePath = prop.getProperty("wsdlUrl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.wtf("wsdl", filePath);
		return filePath;
	}

	public static String getServiceUrl(boolean isalternative, Context context) {
		String filePath = "";
		if (CONSTANTS.IS_USING_ORGINAL_URL) {
			filePath = getServiceUrl();
		} else {

			try {
				if (isalternative && (context != null)) {
					DatabaseHandler dbHandler = new DatabaseHandler(context);
					systeUrl = dbHandler.getServiceUrl();
					if (!systeUrl.trim().equalsIgnoreCase("")) {
						return systeUrl +  WsdlReader.getWSDLServiceUrl();
					} else {
						filePath = getServiceUrl();
					}
				} else {
					filePath = getServiceUrl();
				}
			} catch (Exception e) {
				e.printStackTrace();
				filePath = getServiceUrl();
			}
			if(filePath.equals("")){
				(new SystemProfileAsyc(context)).execute("");
			}
			//filePath = getServiceUrl();
		}
		Log.wtf("wsdl", filePath);
		return filePath;
	}
	public static class SystemProfileAsyc extends AsyncTask<String, Void, String> {

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
					return http.makeRequest(CONSTANTS.GET_SERVICE_URL(epf));
				}
			} catch (Exception e) {
				Utils.writeToErrLogFileWithTime(e);
				e.printStackTrace();
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
							System.out.println("* updATED URL");
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				Utils.writeToErrLogFileWithTime(e);
			} finally {
				dbh.close();
			}
		}

	}
	public static String numberReader() {
		Properties prop = new Properties();
		String filePath = "";
		try {
			InputStream inputStream = WsdlReader.class.getClassLoader().getResourceAsStream("wsdl.properties");
			prop.load(inputStream);
			filePath = prop.getProperty("number");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.wtf("number", filePath);
		return filePath;
	}

	public static String getServerBaseUrl() {
		Properties prop = new Properties();
		String filePath = "";
		try {
			InputStream inputStream = WsdlReader.class.getClassLoader().getResourceAsStream("wsdl.properties");
			prop.load(inputStream);
			filePath = prop.getProperty("baseurl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.wtf("wsdl", filePath);
		return filePath;
	}

	public static String getWSDLServiceUrl() {
		Properties prop = new Properties();
		String filePath = "";
		try {
			InputStream inputStream = WsdlReader.class.getClassLoader().getResourceAsStream("wsdl.properties");
			prop.load(inputStream);
			String axis = prop.getProperty("axis");
			String axisservice = prop.getProperty("axisservice");
			filePath = axis + axisservice;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}
}