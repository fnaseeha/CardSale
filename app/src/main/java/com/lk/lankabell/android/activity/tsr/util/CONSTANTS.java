package com.lk.lankabell.android.activity.tsr.util;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

import android.content.Context;

public class CONSTANTS {

	public static final String ATTENDACE_MOBILE_NUMBER = "0717069248";//"+94115760927";
	public static final String NAMESPACE = "http://mainService";

	public static String URL_SERVER_BASE() {

		String wsdl_base_host = WsdlReader.getServerBaseUrl();

		// local host server
		// // QA server
		// URL_SERVER_BASE = "http://10.2.5.21:8090/Webservice/api/v1/";
		// URL_SERVER_BASE = "http://119.235.6.76:8090/Webservice/api/v1/";

		String URL_SERVER_BASE = wsdl_base_host + "Webservice_1/api/v1/";
//		 String URL_SERVER_BASE = wsdl_base_host + "Webservice/api/v1/";

		return URL_SERVER_BASE;
	}

	public static String URL_SERVER_BASE(Context context) {

		String wsdl_base_host = WsdlReader.getServerBaseUrl();
		String filePath = "";
		if (CONSTANTS.IS_USING_ORGINAL_URL) {

		} else {

			try {
				if ((context != null)) {
					DatabaseHandler dbHandler = new DatabaseHandler(context);
					filePath = dbHandler.getServiceUrl();
					if (!filePath.trim().equalsIgnoreCase("")) {
						wsdl_base_host = filePath;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// local host server
		// // QA server
		// URL_SERVER_BASE = "http://10.2.5.21:8090/Webservice/api/v1/";
		// URL_SERVER_BASE = "http://119.235.6.76:8090/Webservice/api/v1/";

		String URL_SERVER_BASE = wsdl_base_host + "Webservice_1/api/v1/";
//		 String URL_SERVER_BASE = wsdl_base_host + "Webservice/api/v1/";

		return URL_SERVER_BASE;
	}

//	public static final String URL_ATTENDANCE = URL_SERVER_BASE() + "attendance/mark.json";

	public static final String getAttendanceUrl(Context context) {
		return URL_SERVER_BASE(context) + "attendance/mark.json";
	}

	public static final String getAttendanceCheckUrl(Context context){
		return URL_SERVER_BASE(context) + "attendances/check.json";
	}
	public static final String URL_UPDATE_BULKCARD_SERIALS = URL_SERVER_BASE() + "attendance/mark.json";;

	public static final boolean SHOULD_STOP_SERVICES = false;

	public static final long LOCATION_UPDATE_SYNC_TIME = 30 * 1000;

	public static final String SHARED_PREF_NAME = "TRACKING_DATA";

	public static final String SHARED_PREF_LAST_GPS_TIME = "LAST_GPS_AT";

	public static final long TEN_MINUTES = 1000 * 60 * 10;
	public static final long TWO_MINITUES = 1000 * 60 * 2;
	public static final String SHARED_PREF = "tsr_shared_pref";

	public static boolean USE_NETWORK_PROVIDER = false;
	public static boolean SAVE_NETWORK_PROVIDER_GPS_DATA = false;
	public static boolean UPLOAD_GPS_DATA = true;
	public static boolean NO_LOCATION_RESTRICT = true;
	public static boolean STOP_SENDING_SMS = true;
	public static boolean IS_USING_ORGINAL_URL = false;

	public interface ACTION {
		public static String MAIN_ACTION = "com.lankabell.foregroundservice.action.main";
		public static String PREV_ACTION = "com.lankabell.foregroundservice.action.prev";
		public static String PLAY_ACTION = "com.lankabell.foregroundservice.action.play";
		public static String NEXT_ACTION = "com.lankabell.foregroundservice.action.next";
		public static String STARTFOREGROUND_ACTION = "com.lankabell.foregroundservice.action.startforeground";
		public static String STOPFOREGROUND_ACTION = "com.lankabell.foregroundservice.action.stopforeground";
	}

	public interface NOTIFICATION_ID {
		public static int FOREGROUND_SERVICE = 1010;
	}

	public static String GET_SERVICE_URL(String epf) {
		return URL_SERVER_BASE() + "server_url/" + epf.trim() + ".json";
	}

	public static String GET_SERVICE_TIMES = URL_SERVER_BASE() + "set_times.json";

	public static final int SHARED_UPDATE_TIME_GPS = 10;
	public static final String SHARED_UPDATE_TIME_GPS_NAME = "gps_time";

	public static final int SHARED_UPDATE_TIME_SCREENON = 11;
	public static final String SHARED_UPDATE_TIME_SCREENON_NAME = "screen_on";

	public static final int SHARED_UPDATE_TIME_SYNC = 12;
	public static final String SHARED_UPDATE_TIME_SYNC_NAME = "sync_time";
	public static  String SHARED_LOGOUT = "0";

	public static final int SHARED_SYSTEM_DATA_UPDATED_TIME = 9;
	public static final String SHARED_SYSTEM_DATA_UPDATED_TIME_NAME = "system_data_updated_at";

	public static final int GPS_UPDATE_TIME = 5;
	public static final int SYNC_UPDATE_TIME = 5;
	public static final int SCREENON_UPDATE_TIME = 15;
	public static final int MAX_LENGTH_ARRAY_LIST = 50;
}
