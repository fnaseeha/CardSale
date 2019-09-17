package com.lk.lankabell.android.activity.tsr.sqlite;



/**
 * @author Jei
 * 
 * This class is used for getting Card Bulk ID from database.
 *
 */
public class bulkId {
	
	String type;
	int denomination;
	String bulkno;
	String StartSerial;
	String endSerial;
	
	public bulkId(String Type,int denomination,String bulkNo,String startSerial,String endSerial){
		super();
		this.type = Type;
		this.denomination = denomination;
		this.bulkno = bulkNo;
		this.StartSerial = startSerial;
		this.endSerial = endSerial;
	}
	
	
	/**
	 * @return type
	 * 
	 * Getting bulk type function
	 */
	public String Gettype() {
		return type;
	}
	
	
	/**
	 * @return denomination
	 * 
	 * Getting Card denomination function
	 */
	public int GetDenomination() {
		return denomination;
	}
	
	
	/**
	 * @return Bulk No
	 * 
	 * Getting Bulk ID function
	 */
	public String GetBulkId() {
		return bulkno;
	}
	
	
	
	/**
	 * @return start serial
	 * 
	 * Getting start serial function
	 */
	public String GetStartSerial() {
		return StartSerial;
	}
	
	
	/**
	 * @return end serial
	 * 
	 * Getting end serial function
	 */
	public String GetEndSerial() {
		return endSerial;
	}
	
	
	/**
	 * @param type
	 * 
	 * Setting Card type function.
	 */
	public void Settype(String type) {
		this.type = type;
	}
	
	
	/**
	 * @param denomination
	 * 
	 * 
	 * Setting card active or not function.
	 */
	
	public void SetIsActive(int denomination) {
		this.denomination = denomination;
	}
	
	
	/**
	 * @param bulkNo
	 * 
	 * Setting Bulk No function
	 * 
	 */
	public void SetbulkNo(String bulkNo) {
		this.bulkno = bulkNo;
	}
	
	
	/**
	 * @param startSerial
	 * 
	 * Setting StartSerial function
	 */
	public void SetStartSerial(String startSerial) {
		this.StartSerial = startSerial;
	}
	
	
	/**
	 * @param endSerial
	 * 
	 * Setting EndSerial function
	 * 
	 */
	public void SetEndSerial(String endSerial) {
		this.endSerial = endSerial;
	}

}
