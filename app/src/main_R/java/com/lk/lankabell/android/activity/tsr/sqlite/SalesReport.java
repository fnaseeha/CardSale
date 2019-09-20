package com.lk.lankabell.android.activity.tsr.sqlite;

public class SalesReport {
	private String date;
	private String city;
	private long merchantId = 0;
	private String merchantName = "";
	private String cardType;
	private Double denom;
	private Double amount;
	private Integer qty;
	

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Double getDenom() {
		return denom;
	}
	public void setDenom(Double denom) {
		this.denom = denom;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	
	
}
