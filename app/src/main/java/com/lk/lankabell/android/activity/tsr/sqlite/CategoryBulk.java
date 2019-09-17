package com.lk.lankabell.android.activity.tsr.sqlite;



/**
 * @author Kolitha
 *
 *This class is used for retrieve Card Category Bulk details from database
 *
 */
public class CategoryBulk {
	
	private int categoryId;
	private int bulkId;
	
	public CategoryBulk(int categoryId,int bulkId)
	{
		super();
		this.categoryId = categoryId;
		this.bulkId = bulkId;
	}
	
	
	/**
	 * @return  categoryId
	 * 
	 * getting Category Id function
	 */
	public int getCategoryId() {
		return categoryId;
	}
	
	/**
	 * @return bulkId
	 * 
	 * getting Bulk Id function
	 * 
	 */
	public int getBulkId() {
		return bulkId;
	}
}
