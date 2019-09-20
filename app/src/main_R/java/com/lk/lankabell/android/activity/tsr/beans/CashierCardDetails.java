package com.lk.lankabell.android.activity.tsr.beans;

public class CashierCardDetails {
	
	private Integer Denomination;
	private Integer CardCount;
	private Double LineAmount;
	private Integer HandOnCount;
	private Integer DiscountRate;
	
	public CashierCardDetails(Integer CardsDenomination, Integer CardsCount, Double CardsLineAmount, Integer InHandCount, Integer Discount){
		this.Denomination = CardsDenomination;
		this.CardCount = CardsCount;
		this.LineAmount = CardsLineAmount;
		this.HandOnCount = InHandCount;
		this.DiscountRate = Discount;
	}
	
	public Integer getDenomination() {
		return Denomination;
	}
	public void setDenomination(Integer denomination) {
		Denomination = denomination;
	}
	public Integer getCardCount() {
		return CardCount;
	}
	public void setCardCount(Integer cardCount) {
		CardCount = cardCount;
	}
	public Double getLineAmount() {
		return LineAmount;
	}
	public void setLineAmount(Double lineAmount) {
		LineAmount = lineAmount;
	}

	public Integer getHandOnCount() {
		return HandOnCount;
	}

	public void setHandOnCount(Integer handOnCount) {
		HandOnCount = handOnCount;
	}

	public Integer getDiscountRate() {
		return DiscountRate;
	}

	public void setDiscountRate(Integer discountRate) {
		DiscountRate = discountRate;
	}
	
}
