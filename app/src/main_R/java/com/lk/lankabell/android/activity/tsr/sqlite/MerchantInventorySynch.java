package com.lk.lankabell.android.activity.tsr.sqlite;

public class MerchantInventorySynch {
	
	private Long merchantId;
	private String cardType;
	private int denomination;
	private int stockInHand;
	private int reorderLevel;
	private int saleChanges;
	private int activationChanges;
	
	public int getActivationChanges() {
		return activationChanges;
	}

	public void setActivationChanges(int activationChanges) {
		this.activationChanges = activationChanges;
	}
	
	public MerchantInventorySynch(Long merchantId, String cardType,int denomination, int stockInHand, int reorderLevel,int saleChanges, int activationChanges) {
		super();
		this.merchantId = merchantId;
		this.cardType = cardType;
		this.denomination = denomination;
		this.stockInHand = stockInHand;
		this.reorderLevel = reorderLevel;
		this.saleChanges = saleChanges;
		this.activationChanges = activationChanges;
	}
	
	public MerchantInventorySynch(Long merchantId, String cardType,int denomination, int stockInHand, int reorderLevel, int saleChanges) {
		super();
		this.merchantId = merchantId;
		this.cardType = cardType;
		this.denomination = denomination;
		this.stockInHand = stockInHand;
		this.reorderLevel = reorderLevel;
		this.saleChanges = saleChanges;
	}

	public int getSaleChanges() {
		return saleChanges;
	}

	public void setSaleChanges(int saleChanges) {
		this.saleChanges = saleChanges;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getDenomination() {
		return denomination;
	}

	public void setDenomination(int denomination) {
		this.denomination = denomination;
	}

	public int getStockInHand() {
		return stockInHand;
	}

	public void setStockInHand(int stockInHand) {
		this.stockInHand = stockInHand;
	}

	public int getReorderLevel() {
		return reorderLevel;
	}

	public void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

}
