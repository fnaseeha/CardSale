package com.lk.lankabell.android.activity.tsr.beans;

import java.util.ArrayList;

public class cardSalesHeader {

	private Long invoiceId;
	private Long merchantId;
	private String latitude;
	private String longitude;
	private Double invoiceTotal;
	private String enteredDate;
	private Integer enteredBy;
	private Double discount;
	private ArrayList<cardSalesDeatils> cardSalesDeatils;
	
	public cardSalesHeader(Long invoiceId, Long merchantId, String latitude,String longitude, Double invoiceTotal, String enteredDate,Integer enteredBy, Double discount) {
		super();
		this.invoiceId = invoiceId;
		this.merchantId = merchantId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.invoiceTotal = invoiceTotal;
		this.enteredDate = enteredDate;
		this.enteredBy = enteredBy;
		this.discount = discount;
	}
	
	public Long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public Double getInvoiceTotal() {
		return invoiceTotal;
	}
	public void setInvoiceTotal(Double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}
	public String getEnteredDate() {
		return enteredDate;
	}
	public void setEnteredDate(String enteredDate) {
		this.enteredDate = enteredDate;
	}
	public Integer getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(Integer enteredBy) {
		this.enteredBy = enteredBy;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public ArrayList<cardSalesDeatils> getCardSalesDeatils() {
		return cardSalesDeatils;
	}
	public void setCardSalesDeatils(ArrayList<cardSalesDeatils> cardSalesDeatils) {
		this.cardSalesDeatils = cardSalesDeatils;
	}

}
