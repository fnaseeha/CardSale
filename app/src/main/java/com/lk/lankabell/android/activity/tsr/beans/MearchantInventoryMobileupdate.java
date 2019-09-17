package com.lk.lankabell.android.activity.tsr.beans;

public class MearchantInventoryMobileupdate {

	private Integer Responsecode;
	private String MerchantID;
	private String CardType;
	private Integer denomination;
	
	public Integer getResponsecode() {
		return Responsecode;
	}
	public void setResponsecode(Integer responsecode) {
		Responsecode = responsecode;
	}
	public String getMerchantID() {
		return MerchantID;
	}
	public void setMerchantID(String merchantID) {
		MerchantID = merchantID;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	public Integer getDenomination() {
		return denomination;
	}
	public void setDenomination(Integer denomination) {
		this.denomination = denomination;
	}
	
}
