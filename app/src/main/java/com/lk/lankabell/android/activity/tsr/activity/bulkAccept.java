package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class bulkAccept extends Activity {
	
	ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
	int[] columnIds = new int[] {R.id.card_type, R.id.denom,R.id.bulk,R.id.startserial,R.id.endserial, R.id.btn_accept,R.id.noofcards};
	String[] columnTags = new String[] {"type","denomination","bulkId","Start_Serial","End_Serial","","NO_OF_CARDS"};
	    SharedPreferences prefs1 = null;
	 
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.cardacceptpage);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText = (TextView)findViewById(R.id.myTitle);
        if ( myTitleText != null ) {      	  
             myTitleText.setText("Card Acceptance");
        }
        
        SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		String sql = "select CARD_TYPE,DENOMINATION,BULK_NO,START_SERIAL,END_SERIAL,NO_OF_CARDS from TSR_RECEIVED_CARD_BUNDLES WHERE IS_ACCEPTED = '0' AND CARD_RETURNS = 0 ";
		Cursor OrderCursor = db.rawQuery(sql, null);
		//Cursor OrderCursor = db.query("TSR_RECEIVED_CARD_BUNDLES", new String[] { }, new String[] {" IS_ACCEPTED = '0' ", "CARD_RETURNS = '0' "} , null, null, null,null);
		
	  	 	listData.clear();
	  	 	if(OrderCursor!= null){
	  	 	 if(OrderCursor.moveToFirst()){
	               for (int i = 0; i < OrderCursor.getCount(); i++){
	              	            	 HashMap<String,String> map = new HashMap<String, String>();
	              	            	 map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("CARD_TYPE")));
	              	            	 map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")));
	              	            	 map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("BULK_NO")));
	              	            	 map.put(columnTags[3], OrderCursor.getString(OrderCursor.getColumnIndex("START_SERIAL")));
	              	            	 map.put(columnTags[4], OrderCursor.getString(OrderCursor.getColumnIndex("END_SERIAL")));
	              	            	 map.put(columnTags[6], OrderCursor.getString(OrderCursor.getColumnIndex("NO_OF_CARDS")));
	              	            	 listData.add(map);
	              	 OrderCursor.moveToNext();
	                 }
	          }
	  	 OrderCursor.close();
	  	 db.close();
	 	 SimpleAdapter adapter = new SimpleAdapter(bulkAccept.this, listData, R.layout.card_main,columnTags , columnIds);
	  	 ListView lvData =(ListView)findViewById(R.id.lvBulkData);
	  	 lvData.setAdapter(adapter);
	  	 }
	}
		
		
	    public SimpleAdapter setAdapter(ArrayList<HashMap<String, String>> listData,int[] columnIds,String[] columnTags){
	        SimpleAdapter adapter =new SimpleAdapter(this, listData, R.layout.card_main,columnTags , columnIds);        
	        return adapter;
	    }

		public void OnclickAccept(View view) {
			  AbsoluteLayout vwParentRow = (AbsoluteLayout )view.getParent();
		      TextView CardType = (TextView)vwParentRow.getChildAt(0);
		      TextView denomination = (TextView)vwParentRow.getChildAt(2);
		      TextView bulkId = (TextView)vwParentRow.getChildAt(1);
		      TextView startSerial = (TextView)vwParentRow.getChildAt(3);
		      TextView endSerialValue = (TextView)vwParentRow.getChildAt(4);
		      
		      DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
			  User user = dbh.getUserDetails();
			  dbh.updateAcceptedBulkIds(CardType.getText().toString(), Integer.parseInt(denomination.getText().toString()), bulkId.getText().toString(),Integer.parseInt(startSerial.getText().toString()),Integer.parseInt(endSerialValue.getText().toString()));
			  dbh.SaveEndSerial(user.getId(),CardType.getText().toString(), Integer.parseInt(denomination.getText().toString()), bulkId.getText().toString(),startSerial.getText().toString(),endSerialValue.getText().toString());

			  Intent intent = new Intent(getApplicationContext(),bulkAccept.class);
		      startActivity(intent);
		  	  finish();
		}
}
