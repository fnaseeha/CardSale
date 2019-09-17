package com.lk.lankabell.android.activity.tsr.sync;

public class CardAccept {

	private String CardType;
	private String Denomination;
	private String BulkCode;
	private String StartSerial;	
	private String EndSerial;
	
	public CardAccept(String Type,int denomination,String bulkNo,String startSerial,String endSerial){
		super();
		this.CardType = Type;
		this.Denomination = Integer.toString(denomination);
		this.BulkCode = bulkNo;
		this.StartSerial = startSerial;
		this.EndSerial = endSerial;
	}
	
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	public String getDenomination() {
		return Denomination;
	}
	public void setDenomination(String denomination) {
		Denomination = denomination;
	}
	public String getBulkCode() {
		return BulkCode;
	}
	public void setBulkCode(String bulkCode) {
		BulkCode = bulkCode;
	}
	public String getStartSerial() {
		return StartSerial;
	}
	public void setStartSerial(String startSerial) {
		StartSerial = startSerial;
	}
	public String getEndSerial() {
		return EndSerial;
	}
	public void setEndSerial(String endSerial) {
		EndSerial = endSerial;
	}
	
	
}
