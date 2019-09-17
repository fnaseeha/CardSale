package com.lk.lankabell.android.activity.tsr.tracking;

public interface ISMSCallbacks {
	
	
	public void onSmsSent(String text);
	public void onSmsDelivered(String text);
	public void onSmsSendFail(String msgid,int failType);
	public void onSmsDeliveryFail(String msgid,int failType);

}
