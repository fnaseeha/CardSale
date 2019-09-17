package com.lk.lankabell.android.activity.tsr.beans.login;

public class ServerDataLoad_CardDenominationData {

	private Integer Denomination;
	private String CARDTYPE;
	
	public ServerDataLoad_CardDenominationData(int DENOMINATION, String CARDTYPE) {
		this.Denomination = DENOMINATION;
		this. CARDTYPE = CARDTYPE;
	}
	public Integer getDENOMINATION() {
		return Denomination;
	}
	public void setDENOMINATION(Integer dENOMINATION) {
		Denomination = dENOMINATION;
	}
	public String getCARDTYPE() {
		return CARDTYPE;
	}
	public void setCARDTYPE(String cARDTYPE) {
		CARDTYPE = cARDTYPE;
	}
	
}
