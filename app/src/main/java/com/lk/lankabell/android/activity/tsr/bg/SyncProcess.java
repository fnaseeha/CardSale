/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.bg;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.ws.TSRServiceClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 
 *
 */
public class SyncProcess {
	
	private static final String TAG = SyncProcess.class.getSimpleName();
	private DatabaseHandler dbh;
	private Context context;
	private TSRServiceClient client;
	public SyncProcess(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dbh = new DatabaseHandler(context);
		client = new TSRServiceClient();
	}
	
	public void doProcess(){
		
		if(isNetworkConnected()){
			
			Log.i(TAG,"Timer task doing work");
			processSyncronizing();
			
		}
		
	}
	
	private boolean isNetworkConnected() {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			  
			  NetworkInfo ni = cm.getActiveNetworkInfo();
			  if (ni == null) {
			   // There are no active networks.
			   return false;
			  } else
			   return ni.isConnected();
		} catch (Exception e) {
			// TODO: handle exception
			Log.i(TAG, e.getLocalizedMessage());
		}
		
		 return false;
		  
		 }
	
	private void processSyncronizing(){
		
		/*boolean status = false;
		ArrayList<GeoInfo> list = dbh.getGeoInfo();
		
		for (Iterator<GeoInfo> iterator = list.iterator(); iterator.hasNext();) {
			GeoInfo geoInfo = (GeoInfo) iterator.next();
			status = client.saveGeoInfo(geoInfo.getEmpId(),geoInfo.getGeoId(),geoInfo.getLatitude(),geoInfo.getLongitude());
			
			Log.i(TAG,"Inside Status="+status);
			
			if(status){
				
				dbh.updateGeoInfo(geoInfo.getGeoId());
				status= false;
				
			}
		}*/
		
		
	}

}
