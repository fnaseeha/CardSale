package com.lk.lankabell.android.activity.tsr.sync;

import java.util.ArrayList;

import com.lk.lankabell.android.activity.tsr.beans.TSRSystemprofile;
import com.lk.lankabell.android.activity.tsr.beans.cardSalesHeader;
import com.lk.lankabell.android.activity.tsr.sqlite.MerchantInventorySynch;
import com.lk.lankabell.android.activity.tsr.sqlite.MerchantNew;
import com.lk.lankabell.android.activity.tsr.sqlite.SerialDetails;

public class SynchBean {

	private String MobileNumber;
	private String UserName;	
	private String Password;
	private String Version;
	private ArrayList<TSRSystemprofile> systemprofiles;
	private ArrayList<MerchantNew> merchants;
	private ArrayList<cardSalesHeader> salesData;
	private ArrayList<CardAccept> cardAccepts;
	private ArrayList<MerchantInventorySynch> inventorySynchs;
	private ArrayList<SynchRemarks> synchRemarks;
	private ArrayList<CityRegistration> cityRegistrations;
	private ArrayList<SerialDetails> NextserialDetails;
	
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
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public ArrayList<TSRSystemprofile> getSystemprofiles() {
		return systemprofiles;
	}
	public void setSystemprofiles(ArrayList<TSRSystemprofile> systemprofiles) {
		this.systemprofiles = systemprofiles;
	}
	public ArrayList<MerchantNew> getMerchants() {
		return merchants;
	}
	public void setMerchants(ArrayList<MerchantNew> merchants) {
		this.merchants = merchants;
	}
	public ArrayList<cardSalesHeader> getSalesData() {
		return salesData;
	}
	public void setSalesData(ArrayList<cardSalesHeader> salesData) {
		this.salesData = salesData;
	}
	public ArrayList<CardAccept> getCardAccepts() {
		return cardAccepts;
	}
	public void setCardAccepts(ArrayList<CardAccept> cardAccepts) {
		this.cardAccepts = cardAccepts;
	}
	public ArrayList<MerchantInventorySynch> getInventorySynchs() {
		return inventorySynchs;
	}
	public void setInventorySynchs(ArrayList<MerchantInventorySynch> inventorySynchs) {
		this.inventorySynchs = inventorySynchs;
	}
	public ArrayList<SynchRemarks> getSynchRemarks() {
		return synchRemarks;
	}
	public void setSynchRemarks(ArrayList<SynchRemarks> synchRemarks) {
		this.synchRemarks = synchRemarks;
	}
	public ArrayList<CityRegistration> getCityRegistrations() {
		return cityRegistrations;
	}
	public void setCityRegistrations(ArrayList<CityRegistration> cityRegistrations) {
		this.cityRegistrations = cityRegistrations;
	}
	public ArrayList<SerialDetails> getNextserialDetails() {
		return NextserialDetails;
	}
	public void setNextserialDetails(ArrayList<SerialDetails> nextserialDetails) {
		NextserialDetails = nextserialDetails;
	}
	
	
}
