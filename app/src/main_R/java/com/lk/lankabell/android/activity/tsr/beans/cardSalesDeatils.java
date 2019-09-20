package com.lk.lankabell.android.activity.tsr.beans;

public class cardSalesDeatils {
	
	private Integer detailsId;
	private String cardType;
	private Integer denomination;
	private Integer bulkNo;
	private Integer noOfCards;
	private String startSerial;
	private Double lineAmount;
	private Double lineDiscount;
	private Long invoiceId;
	
	public cardSalesDeatils(Integer detailsId, String cardType,Integer denomination, Integer bulkNo, Integer noOfCards,String startSerial, Double lineAmount, Double lineDiscount,Long invoiceId) {
		super();
		this.detailsId = detailsId;
		this.cardType = cardType;
		this.denomination = denomination;
		this.bulkNo = bulkNo;
		this.noOfCards = noOfCards;
		this.startSerial = startSerial;
		this.lineAmount = lineAmount;
		this.lineDiscount = lineDiscount;
		this.invoiceId=invoiceId;
	}
	
	public Integer getDetailsId() {
		return detailsId;
	}
	public void setDetailsId(Integer detailsId) {
		this.detailsId = detailsId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Integer getDenomination() {
		return denomination;
	}
	public void setDenomination(Integer denomination) {
		this.denomination = denomination;
	}
	public Integer getBulkNo() {
		return bulkNo;
	}
	public void setBulkNo(Integer bulkNo) {
		this.bulkNo = bulkNo;
	}
	public Integer getNoOfCards() {
		return noOfCards;
	}
	public void setNoOfCards(Integer noOfCards) {
		this.noOfCards = noOfCards;
	}
	public String getStartSerial() {
		return startSerial;
	}
	public void setStartSerial(String startSerial) {
		this.startSerial = startSerial;
	}
	public Double getLineAmount() {
		return lineAmount;
	}
	public void setLineAmount(Double lineAmount) {
		this.lineAmount = lineAmount;
	}
	public Double getLineDiscount() {
		return lineDiscount;
	}
	public void setLineDiscount(Double lineDiscount) {
		this.lineDiscount = lineDiscount;
	}
	public Long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}
	
}
