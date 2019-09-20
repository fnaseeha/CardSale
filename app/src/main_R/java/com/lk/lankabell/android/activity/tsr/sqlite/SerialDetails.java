package com.lk.lankabell.android.activity.tsr.sqlite;

public class SerialDetails {
	
	private String epfId;
	private String cardType;
	private String denomination;
	private String bulkNo;
	private String startSerial;
	private String nextSerialValue;
	private String endSerial;
	private String isAllSold;
	
	public SerialDetails(String epfId, String cardType, String denomination,
			String bulkNo, String startSerial, String nextSerialValue,
			String endSerial, String isAllSold) {
		super();
		this.epfId = epfId;
		this.cardType = cardType;
		this.denomination = denomination;
		this.bulkNo = bulkNo;
		this.startSerial = startSerial;
		this.nextSerialValue = nextSerialValue;
		this.endSerial = endSerial;
		this.isAllSold = isAllSold;
	}
	
	
	
	public String getIsAllSold() {
		return isAllSold;
	}
	public void setIsAllSold(String isAllSold) {
		this.isAllSold = isAllSold;
	}
	public String getEpfId() {
		return epfId;
	}
	public void setEpfId(String epfId) {
		this.epfId = epfId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getDenomination() {
		return denomination;
	}
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}
	public String getBulkNo() {
		return bulkNo;
	}
	public void setBulkNo(String bulkNo) {
		this.bulkNo = bulkNo;
	}
	public String getStartSerial() {
		return startSerial;
	}
	public void setStartSerial(String startSerial) {
		this.startSerial = startSerial;
	}
	public String getNextSerialValue() {
		return nextSerialValue;
	}
	public void setNextSerialValue(String nextSerialValue) {
		this.nextSerialValue = nextSerialValue;
	}
	public String getEndSerial() {
		return endSerial;
	}
	public void setEndSerial(String endSerial) {
		this.endSerial = endSerial;
	}

	
	
}
