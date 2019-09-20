package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @author Jei
 * 
 * This class is used for Merchant Inventory City selection.
 * 
 *
 */
public class MerchantInventoryCitySelectFinal extends Activity{
	
	
	AutoCompleteTextView atc_city;
	public DatabaseHandler dbh;
	Button btnSelect,btnProceed;
	
	final ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
	final ArrayList<HashMap<String, String>> listData1=new ArrayList<HashMap<String, String>>();
    final int[] columnIds = new int[] {R.id.lblMerchantName, R.id.lblAddress,R.id.lblMerchantId,R.id.lblMeasure};
    final String[] columnTags = new String[] {"Name", "Address", "Id","measure"};

	ListView lv;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchantinventorylist);
		
		
		btnSelect =(Button)findViewById(R.id.btnSelect);
		
		lv = (ListView)findViewById(R.id.lvMerchantName);
		
		  dbh = new DatabaseHandler(getApplicationContext());
		
		
		String auto_city=getIntent().getExtras().getString("city");
		
		

		 SQLiteDatabase db=openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		 String sql = "SELECT B.MERCHANT_ID as MERCHANT_ID, M.[MERCHANT_NAME] as MERCHANT_NAME, M.[ADDRESS] as ADDRESS , MAX(B.MEASURE) as MEASURE, M.LATITUDE as LATITUDE, M.LONGITUDE as LONGITUDE FROM (SELECT mm.[MERCHANT_ID] as MERCHANT_ID, 0 as MEASURE FROM MERCHANT_INVENTORY mm where mm.[STOCK_IN_HAND] = 0 union SELECT mm.[MERCHANT_ID] as MERCHANT_ID, 2 as MEASURE FROM MERCHANT_INVENTORY mm where mm.[STOCK_IN_HAND] > mm.[REORDER_LEVEL] union SELECT mm.[MERCHANT_ID] as MERCHANT_ID, 1.25 as MEASURE FROM MERCHANT_INVENTORY mm where mm.[STOCK_IN_HAND] < mm.[REORDER_LEVEL] and  mm.[STOCK_IN_HAND] > 0) B INNER JOIN MERCHANT_MASTER M ON M.MERCHANT_ID = B.MERCHANT_ID where M.[IS_ACTIVE] = 1 and M.[CITY] = '"+auto_city+"' GROUP BY B.MERCHANT_ID ORDER BY B.MEASURE";
		//Cursor OrderCursor1 = db.rawQuery("SELECT DISTINCT(A.MERCHANT_ID), B.MERCHANT_NAME, B.ADDRESS, A.MEASURE,B.LATITUDE,B.LONGITUDE FROM (SELECT MERCHANT_ID, MIN( CAST(STOCK_IN_HAND AS REAL) /REORDER_LEVEL) AS MEASURE FROM MERCHANT_INVENTORY GROUP BY MERCHANT_ID) A INNER JOIN MERCHANT_MASTER B ON A.MERCHANT_ID=B.MERCHANT_ID WHERE B.CITY='"+auto_city+"' AND B.IS_REGISTERED='1' AND B.IS_ACTIVE ='1' ORDER BY A.MEASURE", null);
               	 Cursor OrderCursor1 = db.rawQuery(sql, null);
              	 	
              	 	listData.clear();
              	 	if(OrderCursor1!= null) 
              	 	{
              	 		
              	 	 if(OrderCursor1.moveToFirst()){
                           for (int j = 0; j < OrderCursor1.getCount(); j++){
            
            				
               	 
	              	 
               	 HashMap<String,String> map = new HashMap<String, String>();
 	            	 
 	            	 map.put(columnTags[0], OrderCursor1.getString(OrderCursor1.getColumnIndex("MERCHANT_NAME")));
 	            	 map.put(columnTags[1], OrderCursor1.getString(OrderCursor1.getColumnIndex("ADDRESS")));
 	            	 map.put(columnTags[2], OrderCursor1.getString(OrderCursor1.getColumnIndex("MERCHANT_ID")));
 	            	 map.put(columnTags[3], OrderCursor1.getString(OrderCursor1.getColumnIndex("MEASURE")));
 	            	 listData.add(map); 	
 	            	 
 	          	 OrderCursor1.moveToNext();
                           }  
              	 	 }

              		OrderCursor1.close();
		      	     db.close();
              	 	}
              	 	
              	   SpecialAdapter adapter = new SpecialAdapter(MerchantInventoryCitySelectFinal.this, listData, R.layout.merchantlistview, columnTags , columnIds);
              	   lv.setAdapter(adapter);   

	}

	/**
	 * @param view
	 * 
	 * Selecting the merchant from Merchant List
	 */
	public void OnclickSelect(View view) {

		LinearLayout vwParentRow = (LinearLayout)view.getParent();
		TextView nameValue = (TextView) vwParentRow.getChildAt(0).findViewById(R.id.lblMerchantName); //vwParentRow.getChildAt(0);
		TextView Address = (TextView) vwParentRow.getChildAt(0).findViewById(R.id.lblAddress); //vwParentRow.getChildAt(1);
		TextView IdValue = (TextView) vwParentRow.getChildAt(1);//view.findViewById(R.id.lblMerchantId);

//		LinearLayout vwParentRow = (LinearLayout)view.getParent();
//		TextView nameValue = (TextView) view.findViewById(R.id.lblMerchantName); //vwParentRow.getChildAt(0);
//		TextView Address = (TextView) view.findViewById(R.id.lblAddress); //vwParentRow.getChildAt(1);
//		TextView IdValue = (TextView) view.findViewById(R.id.lblMerchantId); //vwParentRow.getChildAt(2);
		
		Intent intent = new Intent(MerchantInventoryCitySelectFinal.this,MerchantInventoryDetails.class);
		intent.putExtra("merchantID", IdValue.getText().toString());
		intent.putExtra("merchantName", nameValue.getText().toString());
		intent.putExtra("Address", Address.getText().toString());
		startActivity(intent);
//		finish();
		
	}

	static class ViewHolder
	{
		TextView txtmerchantname,txtmerid,txtmeradd,txtmermeasure;
		Button btnselect;
	}

	/**
	 * @author Jei
	 *
	 *Interface related class
	 */
	public class SpecialAdapter extends SimpleAdapter {
			//    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
		
		private LayoutInflater mInflater;
		
			   public SpecialAdapter(Context ctx,List<HashMap<String, String>> listData, int resourceId, String[] columnTags, int[] columnIds) {
			        super(ctx, listData, resourceId, columnTags, columnIds);
				   
				 
			       
			        ctx=MerchantInventoryCitySelectFinal.this;
			        listData=listData;
			        resourceId=R.layout.merchantlistview;
			        columnTags=columnTags;
			        columnIds=columnIds;
			        
			        
			    }
			   
			   @Override
			public int getCount() {
			// TODO Auto-generated method stub
			return listData.size();
			}
			   
			   @Override
			public Object getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
			}
			    
		@Override
		public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
		}
			
			 
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			View view = super.getView(position, convertView, parent);	

			HashMap<String, String> map = listData.get(position);
			String measureValue = map.get("measure").toString();

	  	 	 
	  	 	double measure1=Double.parseDouble(measureValue);
  	   		
      	   	if(measure1 > 1.5)
      	    {
      	   		view.setBackgroundColor(getResources().getColor(R.color.CoolGreen));
      	    }
      	   	else if((1.5 >= measure1 ) && (measure1>1.0))
      	   	{
      	   		view.setBackgroundColor(getResources().getColor(R.color.CoolYellow));
      	   	}
 	       
 	       else if(1.0>=measure1) 
 	       {
 	    	   view.setBackgroundColor(Color.RED);
 	       }
      	   return view;
	  	 }		
	}	
	
	
		
		
	}


