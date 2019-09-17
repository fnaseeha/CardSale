package com.lk.lankabell.android.activity.tsr.beans;

public class InitialData {

	private String loginAuthentication;
	private String loginData;
	private String cityData;
	private String cardTypeData;
	private String merchantData;
	private String cardDenominationData;
	private String merchantInventoryData;
	private String salesInvoiceID;
	private String nextSerialData;
	private String cardBulkSerialData;
	private String serverDate;

	public InitialData() {
	}

	public InitialData(String loginAuthentication, String loginData,
			String cityData, String cardTypeData, String merchantData,
			String cardDenominationData, String merchantInventoryData,
			String salesInvoiceID, String nextSerialData,
			String cardBulkSerialData) {
		super();
		this.loginAuthentication = loginAuthentication;
		this.loginData = loginData;
		this.cityData = cityData;
		this.cardTypeData = cardTypeData;
		this.merchantData = merchantData;
		this.cardDenominationData = cardDenominationData;
		this.merchantInventoryData = merchantInventoryData;
		this.salesInvoiceID = salesInvoiceID;
		this.nextSerialData = nextSerialData;
		this.cardBulkSerialData = cardBulkSerialData;
	}

	public String getLoginAuthentication() {
		return loginAuthentication;
	}

	public void setLoginAuthentication(String loginAuthentication) {
		this.loginAuthentication = loginAuthentication;
	}

	public String getLoginData() {
		return loginData;
	}

	public void setLoginData(String loginData) {
		this.loginData = loginData;
	}

	public String getCityData() {
		return cityData;
	}

	public void setCityData(String cityData) {
		this.cityData = cityData;
	}

	public String getCardTypeData() {
		return cardTypeData;
	}

	public void setCardTypeData(String cardTypeData) {
		this.cardTypeData = cardTypeData;
	}

	public String getMerchantData() {
		return merchantData;
	}

	public void setMerchantData(String merchantData) {
		this.merchantData = merchantData;
	}

	public String getCardDenominationData() {
		return cardDenominationData;
	}

	public void setCardDenominationData(String cardDenominationData) {
		this.cardDenominationData = cardDenominationData;
	}

	public String getMerchantInventoryData() {
		return merchantInventoryData;
	}

	public void setMerchantInventoryData(String merchantInventoryData) {
		this.merchantInventoryData = merchantInventoryData;
	}

	public String getSalesInvoiceID() {
		return salesInvoiceID;
	}

	public void setSalesInvoiceID(String salesInvoiceID) {
		this.salesInvoiceID = salesInvoiceID;
	}

	public String getNextSerialData() {
		return nextSerialData;
	}

	public void setNextSerialData(String nextSerialData) {
		this.nextSerialData = nextSerialData;
	}

	public String getCardBulkSerialData() {
		return cardBulkSerialData;
	}

	public void setCardBulkSerialData(String cardBulkSerialData) {
		this.cardBulkSerialData = cardBulkSerialData;
	}

	public String getServerDate() {
		return serverDate;
	}

	public void setServerDate(String serverDate) {
		this.serverDate = serverDate;
	}

	@Override
	public String toString() {
		return "InitialData{" +
				"loginAuthentication='" + loginAuthentication + '\'' +
				", loginData='" + loginData + '\'' +
				", cityData='" + cityData + '\'' +
				", cardTypeData='" + cardTypeData + '\'' +
				", merchantData='" + merchantData + '\'' +
				", cardDenominationData='" + cardDenominationData + '\'' +
				", merchantInventoryData='" + merchantInventoryData + '\'' +
				", salesInvoiceID='" + salesInvoiceID + '\'' +
				", nextSerialData='" + nextSerialData + '\'' +
				", cardBulkSerialData='" + cardBulkSerialData + '\'' +
				", serverDate='" + serverDate + '\'' +
				'}';
	}
}
