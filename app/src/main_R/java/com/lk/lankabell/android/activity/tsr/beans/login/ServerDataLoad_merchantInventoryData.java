package com.lk.lankabell.android.activity.tsr.beans.login;

public class ServerDataLoad_merchantInventoryData {

	private Long MerchantID; 
	private String CardType;
	private Integer Denomination;
	private Integer StockInHand;
	private Integer ReorderLevel;
	private String StockCountedDate;
	private String activationCount;
	private String SalesCount;


	public Long getMerchantID() {
		return MerchantID;
	}

	public void setMerchantID(Long merchantID) {
		MerchantID = merchantID;
	}

	public String getCardType() {
		return CardType;
	}

	public void setCardType(String cardType) {
		CardType = cardType;
	}

	public Integer getDenomination() {
		return Denomination;
	}

	public void setDenomination(Integer denomination) {
		Denomination = denomination;
	}

	public Integer getStockInHand() {
		return StockInHand;
	}

	public void setStockInHand(Integer stockInHand) {
		StockInHand = stockInHand;
	}

	public Integer getReorderLevel() {
		return ReorderLevel;
	}

	public void setReorderLevel(Integer reorderLevel) {
		ReorderLevel = reorderLevel;
	}

	public String getStockCountedDate() {
		return StockCountedDate;
	}

	public void setStockCountedDate(String stockCountedDate) {
		StockCountedDate = stockCountedDate;
	}

	public String getActivationCount() {
		return activationCount;
	}

	public void setActivationCount(String activationCount) {
		this.activationCount = activationCount;
	}

	public String getSalesCount() {
		return SalesCount;
	}

	public void setSalesCount(String salesCount) {
		SalesCount = salesCount;
	}
	
	
}
