/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

/**
 *
 *Editing Merchant Details.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.PostalCode;

public class EditMerchant extends Activity{

	private AutoCompleteTextView auto_city;
	final ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
    final int[] columnIds = new int[] {R.id.txtEditId,R.id.txtEditName,R.id.txtEditAddress,R.id.txtStatus};
    final String[] columnTags = new String[] {"Id", "Name", "Address","Status"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.edit_merchant);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText = (TextView)findViewById(R.id.myTitle);
        if ( myTitleText != null ) {
            myTitleText.setText("Edit Merchant");
            
        }
        DatabaseHandler dbh;
        dbh = new DatabaseHandler(getApplicationContext());
  		ArrayList<String> list = dbh.getCities();
  
  		for (int i = 0; i < list.size(); i++) {
  			String cityName = list.get(i).toString();
  			PostalCode postalCode = dbh.getPostalCodeDetailsByCityName(cityName);
  			float Pos_Longitude = Float.parseFloat ((postalCode.getLongitude() ));
  			float Pos_Latitude = Float.parseFloat ((postalCode.getLatitude() ));
  		}
  		
  	  ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditMerchant.this,android.R.layout.simple_dropdown_item_1line, list);
      auto_city = (AutoCompleteTextView)findViewById(R.id.editAutoCity);
      auto_city.setThreshold(1);
      auto_city.setAdapter(adapter);
  		
		
	}
	
	/**
	 * @param view
	 * 
	 * Retrieving the merchants list to be edited from the local database.
	 */
	public void onClickEditProceed(View view)
	{
		 auto_city = (AutoCompleteTextView)findViewById(R.id.editAutoCity);
		 String city=auto_city.getText().toString();
		 ListView lv=(ListView)findViewById(R.id.lstMerchantEdit);
		
	
		SQLiteDatabase db=openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		
		//Cursor OrderCursor = db.query("MERCHANT_MASTER", new String[] { "ADDRESS","MERCHANT_NAME","MERCHANT_ID","IS_ASSIGNED","IS_REGISTERED"}, "CITY ='" +city +"'" , null, null, null, "MERCHANT_ID");   //"CITY = " +city
   	 	//String sql="SELECT ADDRESS,MERCHANT_NAME,MERCHANT_ID,(case when is_assigned is null then '0' else is_assigned end)is_assigned, " +
   	 	//		"(case when is_registered is null then '0' else is_registered end)is_registered FROM MERCHANT_MASTER where city='"+city+"' order by MERCHANT_NAME and group by is_assigned, is_registered";
   	 	String sql = "SELECT ADDRESS,MERCHANT_NAME,MERCHANT_ID, (case when is_assigned is null then '0' else is_assigned end) as is_assigned, (case when is_registered is null then '0' else is_registered end) as is_registered " +
   	 			"FROM MERCHANT_MASTER where city = '"+city+"' order by (case when is_assigned is null then '0' else is_assigned end)||(case when is_registered is null then '0' else is_registered end) , MERCHANT_NAME";
		//String sql = "SELECT ADDRESS,MERCHANT_NAME,MERCHANT_ID,nvl(is_assigned,'0') as is_assigned, nvl(is_registered,'0') as is_registered FROM MERCHANT_MASTER where city = '"+city+"' " +
   	 	//		"order by nvl(is_assigned,'0')||nvl(is_registered,'0') , MERCHANT_NAME"; 
   	 	Cursor OrderCursor = db.rawQuery(sql, null);
   	 	listData.clear();
   	 	if(OrderCursor!= null) 
   	 	{
   	 	 if(OrderCursor.moveToFirst()){
                for (int i = 0; i < OrderCursor.getCount(); i++){
               	            	 
               	            	 HashMap<String,String> map = new HashMap<String, String>();
               	            	 map.put(columnTags[2],  OrderCursor.getString(OrderCursor.getColumnIndex("ADDRESS")));
               	            	 map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_NAME")));
               	            	 map.put(columnTags[0],OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
               	            	 
               	            	int isAssigned;
               	            	int isRegistered;
               	            	
               	            	isAssigned=Integer.valueOf(OrderCursor.getString(OrderCursor.getColumnIndex("is_assigned")));	
								isRegistered=Integer.valueOf(OrderCursor.getString(OrderCursor.getColumnIndex("is_registered")));	
							 
           	            	 if (isAssigned==0 && isRegistered==0){
           	            		map.put(columnTags[3],"C"); 
           	            	 }else if (isAssigned==1 && isRegistered==0){
            	            	map.put(columnTags[3],"A"); 
            	             }else if (isAssigned==1 && isRegistered==1){
                	            map.put(columnTags[3],"R"); 
               	             }
           	           listData.add(map);

               	 OrderCursor.moveToNext();
                  }//end of for
           }
   	 	 OrderCursor.close();
   	     db.close();
   	     
   	    DatabaseHandler dbh;
   	    dbh = new DatabaseHandler(getApplicationContext());

   	    SimpleAdapter adapter = new SimpleAdapter(EditMerchant.this, listData, R.layout.editmerchantlist,columnTags , columnIds);
        lv.setAdapter(adapter); 
    	lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	 	lv.setSelection(0);
	 	lv.smoothScrollToPosition(0);
   	 	}
	}
	
	/**
	 * @param view
	 * Sending the selected Merchant details to the next class.
	 */
	public void onClickEditMerchantfirst(View view)
	{
	LinearLayout vwParentRow = (LinearLayout)view.getParent();
		TextView fifId = (TextView)vwParentRow.getChildAt(3);
		String test5 = fifId.getText().toString();
		
		Intent intent = new Intent(EditMerchant.this,EditMerchantFinal.class);
	    intent.putExtra("merchantID", test5);
		startActivity(intent);
		finish();
	}

}
