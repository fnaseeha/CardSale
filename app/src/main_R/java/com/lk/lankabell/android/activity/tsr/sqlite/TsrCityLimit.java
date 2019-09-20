package com.lk.lankabell.android.activity.tsr.sqlite;

public class TsrCityLimit {
	private String postalCodes;
	private Integer maxLimit;
	
	public TsrCityLimit(String postalCodes, Integer maxLimit) {
		super();
		this.postalCodes = postalCodes;
		this.maxLimit = maxLimit;
	}

	public String getPostalCodes() {
		return postalCodes;
	}
	
	public void setPostalCodes(String postalCodes) {
		this.postalCodes = postalCodes;
	}
	
	public Integer getMaxLimit() {
		return maxLimit;
	}
	
	public void setMaxLimit(Integer maxLimit) {
		this.maxLimit = maxLimit;
	}
}
