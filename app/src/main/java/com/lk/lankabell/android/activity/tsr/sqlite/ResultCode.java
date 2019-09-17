package com.lk.lankabell.android.activity.tsr.sqlite;

public class ResultCode {
	private int responseCode;
	private int records;

	public ResultCode(int responseCode, int records) {
		super();
		this.responseCode = responseCode;
		this.records = records;
	}

	public ResultCode() {
		super();
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

}
