package com.lk.lankabell.android.activity.tsr.sqlite;

/**
 * @author Administrator
 *
 * Retrieving Item details from database
 *
 */
public class Item {

	int itemId;
	String itemName;
	double unitPrice;
	double discountPrice;
	Boolean isActive;
	
	public Item(int itemId,String itemName,double unitPrice,Boolean isActive,double discountPrice){
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.unitPrice = unitPrice;
		this.isActive = isActive;
		this.discountPrice = discountPrice;
	}
	

	/**
	 * @param itemId
	 * 
	 * Setting the Item ID
	 */
	public void SetItemId(int itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * @return itemId
	 * 
	 * Getting the Item ID
	 * 
	 */
	public int GetItemId() {
		return itemId;
	}
	
	/**
	 * @param itemName
	 * 
	 * Setting Item name function.
	 */
	public void SetItemName(String itemName) {
		this.itemName = itemName;
	}
	
	/**
	 * @return itemName
	 * 
	 * Getting Item Name function.
	 */
	public String GetItemName() {
		return itemName;
	}
	
	/**
	 * @param unitPrice
	 * 
	 * Setting Unit Price function.
	 */
	public void SetUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	/**
	 * @return unitPrice
	 * 
	 * Getting unit Price function.
	 */
	public double GetUnitPrice() {
		return unitPrice;
	}
	
	/**
	 * @param isActive
	 * 
	 * Setting active or not function.
	 * 
	 */
	public void SetIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * @return isActive
	 * 
	 * Getting active or not function.

	 * 
	 */
	public Boolean getIsActive() {
		return isActive;
	}
	
	/**
	 * @return discountPrice
	 * 
	 * Getting discount price function
	 * 
	 */
	public double GetDiscountPrice() {
		return discountPrice;
	}
	
	/**
	 * @param discountPrice
	 * 
	 * Setting discount price function
	 */
	public void SetDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}
}
