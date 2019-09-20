package com.lk.lankabell.android.activity.tsr.beans;

public class MerchantMaster {

	private Long MERCHANT_ID;
	private String MERCHANT_NAME;
	private String ADDRESS;
	private String RELOAD_NUMBER;
	private String LATITUDE;
	private String LONGITUDE;
	private String REGISTERED_ON;
	private String TELEPHONE_NO;
	private String CITY;
	private Integer IS_ACTIVE;
	private String REGISTERED_BY_EPF_NO;
	private Integer IS_REGISTERED;
	private Integer IS_EDITED;
	private Integer IS_UPDATED;
	private Integer IS_ASSIGNED;
	private Integer IS_SYNCH;
	
	public MerchantMaster(Long MERCHANT_ID, String MERCHANT_NAME, String ADDRESS, String RELOAD_NUMBER, String LATITUDE, String LONGITUDE, String REGISTERED_ON, String TELEPHONE_NO, String CITY, Integer IS_ACTIVE, String REGISTERED_BY_EPF_NO, Integer IS_REGISTERED, Integer IS_EDITED, Integer IS_UPDATED, Integer IS_ASSIGNED, Integer IS_SYNCH){
		this.MERCHANT_ID = MERCHANT_ID;
		this.MERCHANT_NAME = MERCHANT_NAME;
		this.ADDRESS = ADDRESS;
		this.RELOAD_NUMBER = RELOAD_NUMBER;
		this.LATITUDE = LATITUDE;
		this.LONGITUDE = LONGITUDE;
		this.REGISTERED_ON = REGISTERED_ON;
		this.TELEPHONE_NO = TELEPHONE_NO;
		this.CITY = CITY;
		this.IS_ACTIVE = IS_ACTIVE;
		this.REGISTERED_BY_EPF_NO = REGISTERED_BY_EPF_NO;
		this.IS_REGISTERED = IS_REGISTERED;
		this.IS_EDITED = IS_EDITED;
		this.IS_UPDATED = IS_UPDATED;
		this.IS_ASSIGNED = IS_ASSIGNED;
		this.IS_SYNCH = IS_SYNCH;
	} 
	
	public Long getMERCHANT_ID() {
		return MERCHANT_ID;
	}
	public void setMERCHANT_ID(Long mERCHANT_ID) {
		MERCHANT_ID = mERCHANT_ID;
	}
	public String getMERCHANT_NAME() {
		return MERCHANT_NAME;
	}
	public void setMERCHANT_NAME(String mERCHANT_NAME) {
		MERCHANT_NAME = mERCHANT_NAME;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getRELOAD_NUMBER() {
		return RELOAD_NUMBER;
	}
	public void setRELOAD_NUMBER(String rELOAD_NUMBER) {
		RELOAD_NUMBER = rELOAD_NUMBER;
	}
	public String getLATITUDE() {
		return LATITUDE;
	}
	public void setLATITUDE(String lATITUDE) {
		LATITUDE = lATITUDE;
	}
	public String getLONGITUDE() {
		return LONGITUDE;
	}
	public void setLONGITUDE(String lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}
	public String getREGISTERED_ON() {
		return REGISTERED_ON;
	}
	public void setREGISTERED_ON(String rEGISTERED_ON) {
		REGISTERED_ON = rEGISTERED_ON;
	}
	public String getTELEPHONE_NO() {
		return TELEPHONE_NO;
	}
	public void setTELEPHONE_NO(String tELEPHONE_NO) {
		TELEPHONE_NO = tELEPHONE_NO;
	}
	public String getCITY() {
		return CITY;
	}
	public void setCITY(String cITY) {
		CITY = cITY;
	}
	public Integer getIS_ACTIVE() {
		return IS_ACTIVE;
	}
	public void setIS_ACTIVE(Integer iS_ACTIVE) {
		IS_ACTIVE = iS_ACTIVE;
	}
	public String getREGISTERED_BY_EPF_NO() {
		return REGISTERED_BY_EPF_NO;
	}
	public void setREGISTERED_BY_EPF_NO(String rEGISTERED_BY_EPF_NO) {
		REGISTERED_BY_EPF_NO = rEGISTERED_BY_EPF_NO;
	}
	public Integer getIS_REGISTERED() {
		return IS_REGISTERED;
	}
	public void setIS_REGISTERED(Integer iS_REGISTERED) {
		IS_REGISTERED = iS_REGISTERED;
	}
	public Integer getIS_EDITED() {
		return IS_EDITED;
	}
	public void setIS_EDITED(Integer iS_EDITED) {
		IS_EDITED = iS_EDITED;
	}
	public Integer getIS_UPDATED() {
		return IS_UPDATED;
	}
	public void setIS_UPDATED(Integer iS_UPDATED) {
		IS_UPDATED = iS_UPDATED;
	}
	public Integer getIS_ASSIGNED() {
		return IS_ASSIGNED;
	}
	public void setIS_ASSIGNED(Integer iS_ASSIGNED) {
		IS_ASSIGNED = iS_ASSIGNED;
	}
	public Integer getIS_SYNCH() {
		return IS_SYNCH;
	}
	public void setIS_SYNCH(Integer iS_SYNCH) {
		IS_SYNCH = iS_SYNCH;
	}
	
	
}
