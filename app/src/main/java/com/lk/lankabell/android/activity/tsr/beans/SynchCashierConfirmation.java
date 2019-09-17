package com.lk.lankabell.android.activity.tsr.beans;


public class SynchCashierConfirmation {

	
	private String CONFIRM_DATE;
	private Double SALE_AMOUNT;
	private Double IN_HAND_AMOUNT;
	private Double ADJUST_AMOUNT;	
	private String ADJUST_REMARK;
	private String CONFIRM_BY;
	
	public SynchCashierConfirmation(String SCC_CONFIRM_DATE, double SCC_SALE_AMOUNT, double SCC_IN_HAND_AMOUNT, double SCC_ADJUST_AMOUNT, String SCC_ADJUST_REMARK, String SCC_CONFIRM_BY) {
		this.CONFIRM_DATE = SCC_CONFIRM_DATE;
		this.SALE_AMOUNT = SCC_SALE_AMOUNT;
		this.IN_HAND_AMOUNT = SCC_IN_HAND_AMOUNT;
		this.ADJUST_AMOUNT = SCC_ADJUST_AMOUNT;
		this.ADJUST_REMARK = SCC_ADJUST_REMARK;
		this.CONFIRM_BY = SCC_CONFIRM_BY;
	}

	public String getCONFIRM_DATE() {
		return CONFIRM_DATE;
	}
	public void setCONFIRM_DATE(String cONFIRM_DATE) {
		CONFIRM_DATE = cONFIRM_DATE;
	}
	public Double getSALE_AMOUNT() {
		return SALE_AMOUNT;
	}
	public void setSALE_AMOUNT(Double sALE_AMOUNT) {
		SALE_AMOUNT = sALE_AMOUNT;
	}
	public Double getIN_HAND_AMOUNT() {
		return IN_HAND_AMOUNT;
	}
	public void setIN_HAND_AMOUNT(Double iN_HAND_AMOUNT) {
		IN_HAND_AMOUNT = iN_HAND_AMOUNT;
	}
	public Double getADJUST_AMOUNT() {
		return ADJUST_AMOUNT;
	}
	public void setADJUST_AMOUNT(Double aDJUST_AMOUNT) {
		ADJUST_AMOUNT = aDJUST_AMOUNT;
	}
	public String getADJUST_REMARK() {
		return ADJUST_REMARK;
	}
	public void setADJUST_REMARK(String aDJUST_REMARK) {
		ADJUST_REMARK = aDJUST_REMARK;
	}
	public String getCONFIRM_BY() {
		return CONFIRM_BY;
	}
	public void setCONFIRM_BY(String cONFIRM_BY) {
		CONFIRM_BY = cONFIRM_BY;
	}
	
	
	
}
