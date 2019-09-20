package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.lk.lankabell.android.activity.tsr.R;

public class CardDetails extends Activity{

	ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
	int[] columnIds = new int[] {R.id.card_type2, R.id.denom2,R.id.bulk2,R.id.startserial2,R.id.endserial2,R.id.numberofcards};
	String[] columnTags = new String[] {"type","denomination","bulkId","Start_Serial","End_Serial","numberofcards"};
	SharedPreferences prefs1 = null;
	 
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.carddetailspage);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText = (TextView)findViewById(R.id.myTitle);
        if ( myTitleText != null ) {      	  
             myTitleText.setText("My Cards");
        }
        TextView txtnextserial = (TextView)findViewById(R.id.txtStartSerialNo2);
        txtnextserial.setText("Next Serial");
        //
        SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		String sql = "select CARD_TYPE,DENOMINATION,BULK_NO,NEXT_SERIAL_VALUE,END_SERIAL from next_serial WHERE IS_ALL_SOLD = 0 AND CARD_RETURNS = 0 ";
		Cursor OrderCursor = db.rawQuery(sql, null);
		
	  	 	listData.clear();
	  	 	if(OrderCursor!= null){
	  	 	 if(OrderCursor.moveToFirst()){
	               for (int i = 0; i < OrderCursor.getCount(); i++){
	              	            	 HashMap<String,String> map = new HashMap<String, String>();
	              	            	 map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("CARD_TYPE")));
	              	            	 map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")));
	              	            	 map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("BULK_NO")));
	              	            	 map.put(columnTags[3], OrderCursor.getString(OrderCursor.getColumnIndex("NEXT_SERIAL_VALUE")));
	              	            	 map.put(columnTags[4], OrderCursor.getString(OrderCursor.getColumnIndex("END_SERIAL")));
	              	            	 int endserial = OrderCursor.getInt(OrderCursor.getColumnIndex("END_SERIAL"));
	              	            	 int nextserial = OrderCursor.getInt(OrderCursor.getColumnIndex("NEXT_SERIAL_VALUE"));
	              	            	 int numberofcards = (endserial - nextserial) + 1;
	              	            	 Log.d("This is the number of cards : ", numberofcards+"  "+ endserial +"  "+ nextserial);
	              	            	 map.put(columnTags[5], "   "+numberofcards );
	              	            	 listData.add(map);
	              	 OrderCursor.moveToNext();
	                 }
	          }
	  	 OrderCursor.close();
	  	 db.close();
	 	 SimpleAdapter adapter = new SimpleAdapter(CardDetails.this, listData, R.layout.listcarddetails,columnTags , columnIds);
	  	 ListView lvData =(ListView)findViewById(R.id.lvBulkData2);
	  	 lvData.setAdapter(adapter);
	  	 }
	}
    public SimpleAdapter setAdapter(ArrayList<HashMap<String, String>> listData,int[] columnIds,String[] columnTags){
        SimpleAdapter adapter =new SimpleAdapter(this, listData, R.layout.listcarddetails,columnTags , columnIds);        
        return adapter;
    }

}
