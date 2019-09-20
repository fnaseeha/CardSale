package com.lk.lankabell.android.activity.tsr.beans;

public class ViewSyncReport {

	private String Date;
	private String component;
	private String synchCount;
	private String totalCount;
	
	public ViewSyncReport(String syncdate, String component, String synccount, String totalcount){
		setDate(syncdate);
		setComponent(component);
		setSynchCount(synccount);
		setTotalCount(totalcount);
	}
	
	public String getDate() {
		return Date;
	}
	
	public void setDate(String date) {
		Date = date;
	}
	
	public String getComponent() {
		return component;
	}
	
	public void setComponent(String component) {
		this.component = component;
	}
	
	public String getSynchCount() {
		return synchCount;
	}
	
	public void setSynchCount(String synchCount) {
		this.synchCount = synchCount;
	}
	
	public String getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	
}
