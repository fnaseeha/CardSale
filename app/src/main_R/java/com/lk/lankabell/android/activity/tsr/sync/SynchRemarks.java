package com.lk.lankabell.android.activity.tsr.sync;

public class SynchRemarks {

	Integer remarksId;
	Long merchantId;
	String remarks;
	Boolean isActive;
	String longitude;
	String latitude;
	String date;
	
	public SynchRemarks(int remarksId,long merchantId,String remarks,boolean isActive,String longitude,String latitude,String date){
		super();
		this.remarksId = remarksId;
		this.merchantId = merchantId;
		this.remarks = remarks;
		this.isActive = isActive;
		this.longitude = longitude;
		this.latitude = latitude;
		this.date = date;
	}


	public void setRemarksId(Integer remarksId) {
		this.remarksId = remarksId;
	}
	
	public Integer getRemarksId() {
		return remarksId;
	}
	
	public String getVisitDate() {
		return date;
	}
	
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	
	public Long getMerchantId() {
		return merchantId;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getRemark() {
		return remarks;
	}
	
	public void setlongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getlongitude() {
		return longitude;
	}
	
	public void setlatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getlatitude() {
		return latitude;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}
	
}
