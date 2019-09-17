package com.lk.lankabell.android.activity.tsr.sqlite;



/**
 * @author Administrator
 *
 *
 *This class is used for retrieving Merchant details from database;
 */
public class Common {

	private static int tsrId;
	private static int geoId;
	private static int locId;
	private static Merchant merchant;
	private static int bulkNo;
	private static CategoryBulk categoryBulk;

	/**
	 * @return merchant
	 * 
	 * Get Merchant ID function
	 */
	public static Merchant getMerchant() {
		return merchant;
	}

	/**
	 * @param merchant
	 * 
	 * Set Merchant ID function
	 */
	public static void setMerchant(Merchant merchant) {
		Common.merchant = merchant;
	}

	/**
	 * @return tsrID
	 * 
	 * Get TSR ID function
	 */
	public static int getTsrId() {
		return tsrId;
	}

	
	
	/**
	 * @param tsrId
	 * 
	 * 
	 * Set TSR ID function
	 */
	
	public static void setTsrId(int tsrId) {
		Common.tsrId = tsrId;
	}

	/**
	 * @return geoId
	 * 
	 * Get Geo ID function
	 */
	public static int getGeoId() {
		return geoId;
	}

	/**
	 * @param geoId
	 * 
	 * Set Geo ID function
	 */
	public static void setGeoId(int geoId) {
		Common.geoId = geoId;
	}

	/**
	 * @return locId
	 * 
	 * Get location ID function
	 */
	public static int getLocId() {
		return locId;
	}

	/**
	 * @param locId
	 * 
	 * Set Location ID function
	 */
	public static void setLocId(int locId) {
		Common.locId = locId;
	}
	
	/**
	 * @param categoryBulk
	 * 
	 * Set Bulk No function.
	 */
	public static void setBulkNo(CategoryBulk categoryBulk) {
		Common.categoryBulk = categoryBulk;
	}
	
}
