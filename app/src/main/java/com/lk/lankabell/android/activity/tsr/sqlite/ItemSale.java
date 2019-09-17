package com.lk.lankabell.android.activity.tsr.sqlite;

/**
 * @author Administrator
 * 
 * The ItemSale class
 *
 */
public class ItemSale {

	Long itemSaleId;//INVOICE_ID
	Long merchantId;//MERCHANT_ID
	String longitude;//LONGITUDE
	String latitude;//LATITUDE
	int totalItems;
	double totalAmount;//INVOICE_TOTAL
	boolean isActive;
	String salesdate;//ENTERED_DATE
	
	//,,,,,,ENTERED_BY,TOTAL_DISCOUNT 
	public ItemSale(Long itemSaleId,Long merchantId,String longitude,String latitude,int totalItems,double totalAmount,boolean isActive,String vardate){
		super();
		this.itemSaleId = itemSaleId;
		this.merchantId = merchantId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.totalItems = totalItems;
		this.totalAmount = totalAmount;
		this.isActive = isActive;
		this.salesdate = vardate;

	}
	
	/**
	 * @param itemSalesId
	 * 
	 * Setting ItemSales ID
	 */
	public void SetItemSalesId(Long itemSalesId) {
		this.itemSaleId = itemSalesId;
	}
	
	/**
	 * @return itemSaleId
	 * 
	 * Getting ItemSales ID
	 */
	public Long GetItemSalesId() {
		return itemSaleId;
	}
	
	
	/**
	 * @param merchantId
	 * 
	 * Setting Merchant ID
	 */
	public void SetMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	
	/**
	 * @return merchantId
	 * 
	 * Getting Merchant ID
	 */
	public Long GetMerchantId() {
		return merchantId;
	}
	
	
	/**
	 * @param longitude
	 */
	public void SetLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
	
	public String GetLongitude() {
		return longitude;
	}
	
	
	
	public String GetDate() {
		return salesdate;
	}
	
	
	
	public void SetLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	
	public String GetLatitude() {
		return latitude;
	}
	
	
	public void SetTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	
	public int GetTotalItems() {
		return totalItems;
	}
	
	
	public void SetTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	public double GetTotalAmount() {
		return totalAmount;
	}
	
	
	public void SetIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean GetIsActive() {
		return isActive;
	}
	

	

}
