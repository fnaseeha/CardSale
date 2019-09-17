package com.lk.lankabell.android.activity.tsr.beans.login;

import java.util.List;

public class ServerDataLoad {
	
	private String ServerDateTime;
	private String AccountCode;
	private String MobileNumber;
	private String UserName;
	private String Password;
	private Integer Count_City;
	private Integer Count_Card;
	private Integer Count_Merchant;
	private Integer Count_Denomination;
	private Integer Count_Inventory;
	private Integer Count_NextSerial;
	private Integer Count_BulkCard01;
	private Integer Next_Merchant_ID;
	private Integer Next_Invoice_ID;
	private Integer MAX_RECORDS;
	private String SYSTEM_URL;
	private Integer SYSTEM_AUTO_SYNCH;
	private Integer GPS_GETTING;
	private Integer GPS_AUTO_SYNCH;
	private List<ServerDataLoad_CityData> serverDataLoad_CityData;
	private List<ServerDataLoad_CardType> ServerDataLoad_CardType;
	private List<ServerDataLoad_Merchant_Master> serverDataLoad_Merchant_Masters;
	private List<ServerDataLoad_CardDenominationData> serverDataLoad_CardDenominationDatas;
	private List<ServerDataLoad_merchantInventoryData> serverDataLoad_merchantInventoryDatas;
	private List<ServerDataLoad_CardNextSerial> serverDataLoad_CardNextSerials;
	private List<ServerDataLoad_cardBulkCode> serverDataLoad_cardBulkCodes;


	public String getServerDateTime() {
		return ServerDateTime;
	}

	public void setServerDateTime(String serverDateTime) {
		ServerDateTime = serverDateTime;
	}
	
	public String getAccountCode() {
		return AccountCode;
	}

	public void setAccountCode(String accountCode) {
		AccountCode = accountCode;
	}

	public String getMobileNumber() {
		return MobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
	
	public String getSYSTEM_URL() {
		return SYSTEM_URL;
	}

	public void setSYSTEM_URL(String sYSTEM_URL) {
		SYSTEM_URL = sYSTEM_URL;
	}

	public Integer getSYSTEM_AUTO_SYNCH() {
		return SYSTEM_AUTO_SYNCH;
	}

	public void setSYSTEM_AUTO_SYNCH(Integer sYSTEM_AUTO_SYNCH) {
		SYSTEM_AUTO_SYNCH = sYSTEM_AUTO_SYNCH;
	}

	public Integer getGPS_GETTING() {
		return GPS_GETTING;
	}

	public void setGPS_GETTING(Integer gPS_GETTING) {
		GPS_GETTING = gPS_GETTING;
	}

	public Integer getGPS_AUTO_SYNCH() {
		return GPS_AUTO_SYNCH;
	}

	public void setGPS_AUTO_SYNCH(Integer gPS_AUTO_SYNCH) {
		GPS_AUTO_SYNCH = gPS_AUTO_SYNCH;
	}
	
	public List<ServerDataLoad_CityData> getServerDataLoad_CityData() {
		return serverDataLoad_CityData;
	}

	public void setServerDataLoad_CityData(
			List<ServerDataLoad_CityData> serverDataLoad_CityData) {
		this.serverDataLoad_CityData = serverDataLoad_CityData;
	}

	public List<ServerDataLoad_CardType> getServerDataLoad_CardType() {
		return ServerDataLoad_CardType;
	}

	public void setServerDataLoad_CardType(
			List<ServerDataLoad_CardType> serverDataLoad_CardType) {
		ServerDataLoad_CardType = serverDataLoad_CardType;
	}

	public List<ServerDataLoad_Merchant_Master> getServerDataLoad_Merchant_Masters() {
		return serverDataLoad_Merchant_Masters;
	}

	public void setServerDataLoad_Merchant_Masters(
			List<ServerDataLoad_Merchant_Master> serverDataLoad_Merchant_Masters) {
		this.serverDataLoad_Merchant_Masters = serverDataLoad_Merchant_Masters;
	}

	public List<ServerDataLoad_CardDenominationData> getServerDataLoad_CardDenominationDatas() {
		return serverDataLoad_CardDenominationDatas;
	}

	public void setServerDataLoad_CardDenominationDatas(
			List<ServerDataLoad_CardDenominationData> serverDataLoad_CardDenominationDatas) {
		this.serverDataLoad_CardDenominationDatas = serverDataLoad_CardDenominationDatas;
	}

	public List<ServerDataLoad_merchantInventoryData> getServerDataLoad_merchantInventoryDatas() {
		return serverDataLoad_merchantInventoryDatas;
	}

	public void setServerDataLoad_merchantInventoryDatas(
			List<ServerDataLoad_merchantInventoryData> serverDataLoad_merchantInventoryDatas) {
		this.serverDataLoad_merchantInventoryDatas = serverDataLoad_merchantInventoryDatas;
	}

	public List<ServerDataLoad_CardNextSerial> getServerDataLoad_CardNextSerials() {
		return serverDataLoad_CardNextSerials;
	}

	public void setServerDataLoad_CardNextSerials(
			List<ServerDataLoad_CardNextSerial> serverDataLoad_CardNextSerials) {
		this.serverDataLoad_CardNextSerials = serverDataLoad_CardNextSerials;
	}

	public List<ServerDataLoad_cardBulkCode> getServerDataLoad_cardBulkCodes() {
		return serverDataLoad_cardBulkCodes;
	}

	public void setServerDataLoad_cardBulkCodes(
			List<ServerDataLoad_cardBulkCode> serverDataLoad_cardBulkCodes) {
		this.serverDataLoad_cardBulkCodes = serverDataLoad_cardBulkCodes;
	}

	public Integer getCount_City() {
		return Count_City;
	}

	public void setCount_City(Integer count_City) {
		Count_City = count_City;
	}

	public Integer getCount_Card() {
		return Count_Card;
	}

	public void setCount_Card(Integer count_Card) {
		Count_Card = count_Card;
	}

	public Integer getCount_Merchant() {
		return Count_Merchant;
	}

	public void setCount_Merchant(Integer count_Merchant) {
		Count_Merchant = count_Merchant;
	}

	public Integer getCount_Denomination() {
		return Count_Denomination;
	}

	public void setCount_Denomination(Integer count_Denomination) {
		Count_Denomination = count_Denomination;
	}

	public Integer getCount_Inventory() {
		return Count_Inventory;
	}

	public void setCount_Inventory(Integer count_Inventory) {
		Count_Inventory = count_Inventory;
	}

	public Integer getCount_NextSerial() {
		return Count_NextSerial;
	}

	public void setCount_NextSerial(Integer count_NextSerial) {
		Count_NextSerial = count_NextSerial;
	}

	public Integer getCount_BulkCard01() {
		return Count_BulkCard01;
	}

	public void setCount_BulkCard01(Integer count_BulkCard01) {
		Count_BulkCard01 = count_BulkCard01;
	}

	public Integer getNext_Merchant_ID() {
		return Next_Merchant_ID;
	}

	public void setNext_Merchant_ID(Integer next_Merchant_ID) {
		Next_Merchant_ID = next_Merchant_ID;
	}

	public Integer getNext_Invoice_ID() {
		return Next_Invoice_ID;
	}

	public void setNext_Invoice_ID(Integer next_Invoice_ID) {
		Next_Invoice_ID = next_Invoice_ID;
	}

	public Integer getMAX_RECORDS() {
		return MAX_RECORDS;
	}

	public void setMAX_RECORDS(Integer mAX_RECORDS) {
		MAX_RECORDS = mAX_RECORDS;
	}
	
}
