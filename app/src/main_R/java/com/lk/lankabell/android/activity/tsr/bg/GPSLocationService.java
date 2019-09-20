package com.lk.lankabell.android.activity.tsr.bg;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 */
public class GPSLocationService extends Service {

	public IBinder onBind(Intent intent) {
		return null;
	}
	private Timer timer;
	public void onCreate() {
		super.onCreate();
		timer = new Timer("GPSLocationService Timer");
		timer.schedule(syncTask, 1000L, 10 * 1000L);
	}
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		timer = null;
	}
	public void onLowMemory() {
		super.onLowMemory();
		stopService(new Intent(GPSLocationService.class.getName()));
	}
	private TimerTask syncTask = new TimerTask() {
		public void run() {
		
		}
	};	
	

}
