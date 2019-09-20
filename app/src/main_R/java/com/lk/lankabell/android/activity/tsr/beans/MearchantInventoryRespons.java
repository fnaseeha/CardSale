package com.lk.lankabell.android.activity.tsr.beans;

import java.util.ArrayList;
import java.util.List;

import com.lk.lankabell.android.activity.tsr.sqlite.MerchantInventorySynch;

public class MearchantInventoryRespons {

	private ArrayList<MearchantInventoryMobileupdate> inventoryMobileupdate;
	private List<MerchantInventorySynch> inventorySynch;
	
	public ArrayList<MearchantInventoryMobileupdate> getInventoryMobileupdate() {
		return inventoryMobileupdate;
	}
	public void setInventoryMobileupdate(
			ArrayList<MearchantInventoryMobileupdate> inventoryMobileupdate) {
		this.inventoryMobileupdate = inventoryMobileupdate;
	}
	public List<MerchantInventorySynch> getInventorySynch() {
		return inventorySynch;
	}
	public void setInventorySynch(List<MerchantInventorySynch> inventorySynch) {
		this.inventorySynch = inventorySynch;
	}
	
}
