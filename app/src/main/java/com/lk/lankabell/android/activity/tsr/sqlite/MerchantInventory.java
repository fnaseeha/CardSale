/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.sqlite;

/**
 * @author
 *
 */
public class MerchantInventory {
	
	 private String cardType;
	 private int Denomination;
	 private int quantity;
	 private int reorderLevel;
	
	 
	 public MerchantInventory(String cardType,int denomination,int quantity,int reorderLevel) {
		this.cardType = cardType;
		this.Denomination = denomination;
		this.quantity = quantity;
		this.reorderLevel = reorderLevel;
	}
	 
	 
	 


	public String GetCardType() {
		return cardType;
	}
	 
	 public int GetDenomination() {
			return Denomination;
		}
	 public int GetQuantity() {
			return quantity;
		}
	 public int GetReorderLevel() {
			return reorderLevel;
		}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getDenomination() {
		return Denomination;
	}

	public void setDenomination(int denomination) {
		Denomination = denomination;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getReorderLevel() {
		return reorderLevel;
	}

	public void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

}
