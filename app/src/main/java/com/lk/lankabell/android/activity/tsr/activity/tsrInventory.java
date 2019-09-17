package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class tsrInventory extends Activity {
	
	ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
	 int[] columnIds = new int[] {R.id.lst_Row_CardType, R.id.lst_Row_Quantity,R.id.lst_Row_Denomination, R.id.lst_Row_Total};
	 
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tsrinventory);
		
		TextView lblUserName = (TextView)findViewById(R.id.lblUserName);
		
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		
		final ListView lv=(ListView)findViewById(R.id.lvTSRcardDetails);

	 	final ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
	 	final String[] columnTags = new String[] {"categoryName", "stocksInHand","ReorderLevel"}; 
	 	User user = dbh.getUserDetails();
	 	final int epfNo = user.getId();
	 	
      	SQLiteDatabase db=openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
      	Cursor InventoryDetailCursor = db.rawQuery("SELECT B.CATEGORY_NAME,A.STOCK_IN_HAND,A.REORDER_LEVEL FROM TSR_INVENTORY_DETAILS A,CARD_CATEGORY B WHERE B.CATEGORY_ID = A.CATEGORY_ID AND A.EPF_NO ='"+epfNo+"'", null);

  	 	if(InventoryDetailCursor!= null) 
  	 	{
  	 		
  	 		HashMap<String,String> map_Top = new HashMap<String, String>();
  	 		
  	 		map_Top.put(columnTags[1],"category Name"); 
  	 		map_Top.put(columnTags[2],"stocks In Hand");
  	 		map_Top.put(columnTags[3],"Reorder Level");
  	 		listData.add(map_Top);
  	 		
  	 		if(InventoryDetailCursor.moveToFirst()){
               for (int i = 0; i < InventoryDetailCursor.getCount(); i++){
            	   HashMap<String,String> map = new HashMap<String, String>();	 
              	            	 
              	            	 map.put(columnTags[0],  InventoryDetailCursor.getString(InventoryDetailCursor.getColumnIndex("CATEGORY_NAME")));
              	            	 map.put(columnTags[1],  InventoryDetailCursor.getString(InventoryDetailCursor.getColumnIndex("STOCK_IN_HAND")));
              	            	 map.put(columnTags[2], InventoryDetailCursor.getString(InventoryDetailCursor.getColumnIndex("REORDER_LEVEL")));
              	            	 listData.add(map);

              	            	InventoryDetailCursor.moveToNext();
                 }//end of for
  	 		}
  	 		InventoryDetailCursor.close();
  	 		db.close();
  	     
  	 		SimpleAdapter adapter = new SimpleAdapter(tsrInventory.this, listData, R.layout.merchantinventorylist,columnTags , columnIds);
  	 		lv.setAdapter(adapter);  
  	 	}
		
	}

}
