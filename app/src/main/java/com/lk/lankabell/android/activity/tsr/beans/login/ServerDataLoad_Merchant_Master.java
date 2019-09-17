package com.lk.lankabell.android.activity.tsr.beans.login;

public class ServerDataLoad_Merchant_Master {

	private Long MERCHANTID;
	private String MERCHANTNAME;
	private String ADDRESS;
	private Integer ISREGISTERED;
	private String LATITUDE;
	private String LONGITUDE;
	private Integer REGISTEREDBYEPFNO;
	private String REGISTERDATE;
	private String TELEPHONE_NO;
	private Integer ISACTIVE;
	private String CITY;
	private Integer ISASSIGNED;
	private Integer ISEDITED;
	private String RELOADNO;
	
	public Long getMERCHANTID() {
		return MERCHANTID;
	}


	public void setMERCHANTID(Long mERCHANTID) {
		MERCHANTID = mERCHANTID;
	}


	public String getMERCHANTNAME() {
		return MERCHANTNAME;
	}


	public void setMERCHANTNAME(String mERCHANTNAME) {
		MERCHANTNAME = mERCHANTNAME;
	}


	public String getADDRESS() {
		return ADDRESS;
	}


	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}


	public Integer getISREGISTERED() {
		return ISREGISTERED;
	}


	public void setISREGISTERED(Integer iSREGISTERED) {
		ISREGISTERED = iSREGISTERED;
	}


	public String getLATITUDE() {
		if (LATITUDE == null) {
			return "";
		} else {
			return LATITUDE;
		}
	}


	public void setLATITUDE(String lATITUDE) {
		LATITUDE = lATITUDE;
	}


	public String getLONGITUDE() {
		if (LONGITUDE == null) {
			return "";
		} else {
			return LONGITUDE;
		}
	}


	public void setLONGITUDE(String lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}


	public Integer getREGISTEREDBYEPFNO() {
		return REGISTEREDBYEPFNO;
	}


	public void setREGISTEREDBYEPFNO(Integer rEGISTEREDBYEPFNO) {
		REGISTEREDBYEPFNO = rEGISTEREDBYEPFNO;
	}


	public String getREGISTERDATE() {
		return REGISTERDATE;
	}


	public void setREGISTERDATE(String rEGISTERDATE) {
		REGISTERDATE = rEGISTERDATE;
	}


	public String getTELEPHONE_NO() {
		return TELEPHONE_NO;
	}


	public void setTELEPHONE_NO(String tELEPHONE_NO) {
		TELEPHONE_NO = tELEPHONE_NO;
	}


	public Integer getISACTIVE() {
		return ISACTIVE;
	}


	public void setISACTIVE(Integer iSACTIVE) {
		ISACTIVE = iSACTIVE;
	}


	public String getCITY() {
		return CITY;
	}


	public void setCITY(String cITY) {
		CITY = cITY;
	}


	public Integer getISASSIGNED() {
		return ISASSIGNED;
	}


	public void setISASSIGNED(Integer iSASSIGNED) {
		ISASSIGNED = iSASSIGNED;
	}


	public Integer getISEDITED() {
		return ISEDITED;
	}


	public void setISEDITED(Integer iSEDITED) {
		ISEDITED = iSEDITED;
	}


	public String getRELOADNO() {
		return RELOADNO;
	}


	public void setRELOADNO(String rELOADNO) {
		RELOADNO = rELOADNO;
	}

	 
	
}
