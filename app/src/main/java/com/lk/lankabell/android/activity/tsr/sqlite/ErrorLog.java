package com.lk.lankabell.android.activity.tsr.sqlite;

public class ErrorLog {
	
	private String processName;
	private String updateDate;
	
	public ErrorLog(String processName, String updateDate) {
		super();
		this.processName = processName;
		this.updateDate = updateDate;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	

}