package com.lk.lankabell.android.activity.tsr.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;

import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;
import com.lk.lankabell.android.activity.tsr.activity.LoginPage;

public class SharedPrefManager {
	
	
	public static void setSharePref(Context context, int shared, Object object) {
		Editor edit = context.getSharedPreferences(CONSTANTS.SHARED_PREF, Context.MODE_PRIVATE).edit();
		switch (shared) {
		
		case CONSTANTS.SHARED_SYSTEM_DATA_UPDATED_TIME:			
			edit.putLong(CONSTANTS.SHARED_SYSTEM_DATA_UPDATED_TIME_NAME,Long.parseLong(object.toString()));
			break;
		case CONSTANTS.SHARED_UPDATE_TIME_GPS:			
			edit.putInt(CONSTANTS.SHARED_UPDATE_TIME_GPS_NAME,Integer.parseInt(object.toString()));
			break;
 
		case CONSTANTS.SHARED_UPDATE_TIME_SYNC:			
			edit.putInt(CONSTANTS.SHARED_UPDATE_TIME_SYNC_NAME,Integer.parseInt(object.toString()));
			break;
		 

		default:
			break;
		}
		
		edit.commit();
	}
 

	public static void isDataSet(Context context, String value) {
		if(value.equals("0")){
			Intent intent = new Intent(context,LoginPage.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
	}

 
 
	
	public static int getGPSUpdateTime(Context context) {
		return context.getSharedPreferences(CONSTANTS.SHARED_PREF, Context.MODE_PRIVATE).getInt(CONSTANTS.SHARED_UPDATE_TIME_GPS_NAME, CONSTANTS.GPS_UPDATE_TIME);
	}
	public static int getGPSUpdateTimeSync(Context context) {
		return context.getSharedPreferences(CONSTANTS.SHARED_PREF, Context.MODE_PRIVATE).getInt(CONSTANTS.SHARED_UPDATE_TIME_SYNC_NAME,  CONSTANTS.SYNC_UPDATE_TIME);
	}
	 
	
	
	 
	
}
