package com.lk.lankabell.android.activity.tsr.tracking;


import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMSManager {

	private Context context;
	private ISMSCallbacks SMSCallbacks;

	public SMSManager(Context context, ISMSCallbacks SMSCallbacks) {
		super();
		this.context = context;
		this.SMSCallbacks = SMSCallbacks;
	}

	public void sendSMS(String phoneNumber, String message, final int msgId) {

		if (CONSTANTS.STOP_SENDING_SMS) {
			return;
		}
		
		
		
		if(phoneNumber.equals("") || message.equals("")){
			return;
		}
		
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				context.unregisterReceiver(this);
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					SMSCallbacks.onSmsSent("" + msgId);
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					SMSCallbacks.onSmsSendFail("" + msgId, SmsManager.RESULT_ERROR_GENERIC_FAILURE);
					Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					SMSCallbacks.onSmsSendFail("" + msgId, SmsManager.RESULT_ERROR_NO_SERVICE);
					Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					SMSCallbacks.onSmsSendFail("" + msgId, SmsManager.RESULT_ERROR_NULL_PDU);
					Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					SMSCallbacks.onSmsSendFail("" + msgId, SmsManager.RESULT_ERROR_RADIO_OFF);
					Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
					break;
				}
				
				
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				context.unregisterReceiver(this);
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					SMSCallbacks.onSmsDelivered("" + msgId);
					break;
				case Activity.RESULT_CANCELED:
					SMSCallbacks.onSmsDeliveryFail("" + msgId, Activity.RESULT_CANCELED);
					Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
					break;
				}
				
//				context.unregisterReceiver(this);
				
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}

}
