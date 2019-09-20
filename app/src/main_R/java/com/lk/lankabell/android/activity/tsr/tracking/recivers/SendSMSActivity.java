package com.lk.lankabell.android.activity.tsr.tracking.recivers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lk.lankabell.android.activity.tsr.tracking.ISMSCallbacks;
import com.lk.lankabell.android.activity.tsr.tracking.SMSManager;
import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;
import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SendSMSActivity extends Activity implements
		ISMSCallbacks {

	String smsNumber = "0727856493";
	private DatabaseHandler dbh;
	private User user;
	private int net_status;
	private int gps_status;
	private TextView txt;
	private SimpleDateFormat sdf;
	private Date date2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_change_sms);

		txt = (TextView) findViewById(R.id.act_notification_msg);
		txt.setText("Your coordinator has been notified that you haved turned off GPS or Internet connection");

		dbh = new DatabaseHandler(this);
		user = dbh.getUserDetails();

		sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date2 = new Date();

		net_status = getIntent().getIntExtra("3g", 0);
		gps_status = getIntent().getIntExtra("gps", 0);

		 
		
		boolean isAttendaceMarked = dbh.isAttendanceInMarked(null);
		
		if (gps_status == 1) {
			finish();
		} 
		if(isAttendaceMarked){
			if (shouldSendSMS()) {
				notifySMS();
			} else {
				finish();
			}
		}else{			
			finish();
		}
		


	}

	private boolean shouldSendSMS() {
		boolean send = true;
		if (user != null) {
			int epf = user.getId();

			if (epf == 9111596 || epf == 9125866) {
			 send = false;
			 return send;
			}
		}
		

		if (gps_status == 1) {
			send = false;
		}
		 
		
		if (!DateTimeFormatings.isInOfficeHours()) {
			send = false;
		} 

		return send;

	}

	private void notifySMS() {
		try {

			String gps = "Off";
			String net = "Off";

			if (gps_status == 1) {
				gps = "On";
			} else {
			}
			if (net_status == 1) {
				net = "On";
			} else {
			}
			// Utils.ShowMsg(this, String.format("GPS State %s", gps));
			// Utils.ShowMsg(this, String.format("3G State %s", net));

			String smsText = String.format("GPS %s ,  3G : %s ,  EPF : %s ",
					gps, net, ("" + user.getId()));
			SMSManager mSMS = new SMSManager(this, this);

			Toast.makeText(this, smsText, Toast.LENGTH_LONG).show();

			mSMS.sendSMS(smsNumber, smsText, 123);
		} catch (Exception e) {
			if (e != null) {
				Utils.writeToLogFile(e.getLocalizedMessage());
			}
		}
	}

	@Override
	public void onSmsSent(String text) {
		// TODO Auto-generated method stub
		try {
			// Thread.sleep(2000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();

	}

	@Override
	public void onSmsDelivered(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSmsSendFail(String msgid, int failType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSmsDeliveryFail(String msgid, int failType) {
		// TODO Auto-generated method stub

	}

}
