package com.lk.lankabell.android.activity.tsr.sqlite;


public class ItemSalesDetails {
	
	Long itemSalesNo;//invoice_id
	String itemid;//CARD_TYPE
	int denomination;//DENOMINATION
	String bulkId;//BULK_NO
	int noOfItems;//NO_OF_CARDS
	double lineAmount;//LINE_AMOUNT
	String startSerial;//START_SERIAL
	int itemSaleId;//DETAILS_ID
	double lineDiscount;//LINE_DISCOUNT

	//SELECT DETAILS_ID,CARD_TYPE,DENOMINATION,BULK_NO,NO_OF_CARDS,START_SERIAL,END_SERIAL,LINE_AMOUNT,LINE_DISCOUNT,INVOICE_ID	
	//DETAILS_ID,CARD_TYPE,DENOMINATION, BULK_NO, NO_OF_CARDS,LINE_AMOUNT,START_SERIAL, DETAILS_ID,LINE_DISCOUNT
	public ItemSalesDetails(Long itemSalesNo,String itemId,int denomination,String bulkId,int noOfItems,double lineamount,String startSerial,int itemSaleId,double lineDiscount)
	{
		super();
		this.itemSalesNo = itemSalesNo;
		this.itemid = itemId;
		this.denomination = denomination;
		this.bulkId = bulkId;
		this.noOfItems = noOfItems;
		this.lineAmount = lineamount;
		this.startSerial = startSerial;
		this.itemSaleId = itemSaleId;
		this.lineDiscount = lineDiscount;

	}
	
	public void SetItemSalesNo(Long itemSalesNo){
		this.itemSalesNo = itemSalesNo;
	}
	
	public Long GetItemSalesNo(){
		return itemSalesNo;
	}
	
	public void SetItemNo(String itemNo){
		this.itemid = itemNo;
	}
	
	public String GetItemNo(){
		return itemid;
	}
	
	public void SetnoOfItems(int noOfItems){
		this.noOfItems = noOfItems;
	}
	
	public int GetnoOfItems(){
		return noOfItems;
	}
	
	public void SetlineAmount(Double lineAmount){
		this.lineAmount = lineAmount;
	}
	
	public Double GetLineAmount(){
		return lineAmount;
	}
	
	public void SetStartSerial(String startSerial){
		this.startSerial = startSerial;
	}
	
	public String GetStartSerial(){
		return startSerial;
	}
	
	public void SetItemSalesId(int itemSalesId){
		this.itemSaleId = itemSalesId;
	}
	
	public int GetItemSalesId(){
		return itemSaleId;
	}
	
	public void SetLineDiscount(int lineDiscount){
		this.lineDiscount = lineDiscount;
	}
	
	public double GetLineDiscount(){
		return lineDiscount;
	}
	public String GetBulkId(){
		return bulkId;
	}
	public int getDenomination(){
		return denomination;
	}

}
