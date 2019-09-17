package com.lk.lankabell.android.activity.tsr.sqlite;

public class MaterialEntry {
	
	String item;
	String repairType;
	String quantity;
	String remarks;
	
	public MaterialEntry(String ItemName,String RepairTypeName,String Quantiy,String Remarks)
	{
		super();
		this.item = ItemName;
		this.repairType = RepairTypeName;
		this.quantity = Quantiy;
		this.remarks = Remarks;
	}

	public String GetItem() {
		return item;
	}
	
	public String GetRepairType() {
		return repairType;
	}
	
	public String GetQuantity() {
		return quantity;
	}
	
	public String GetRemarks() {
		return remarks;
	}
}
