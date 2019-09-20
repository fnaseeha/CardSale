package com.lk.lankabell.android.activity.tsr.ws;

import com.lk.lankabell.android.activity.tsr.activity.BackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Autostart extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Intent intent = new Intent(arg0, BackgroundService.class);
		arg0.startService(intent);
		Log.i("Autostart", "started...............................");
	}

}
