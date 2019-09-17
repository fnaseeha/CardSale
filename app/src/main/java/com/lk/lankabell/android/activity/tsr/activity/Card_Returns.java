package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Card_Returns extends Activity {

	ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
	int[] columnIds = new int[] {R.id.deno, R.id.bulkCode,R.id.startserial,R.id.nextserial,R.id.endserial, R.id.numberofcards};
	String[] columnTags = new String[] {"denomination","bulkCode","Start_Serial","Next_Serial","End_Serial","NO_OF_CARDS"};
	SharedPreferences prefs1 = null;
	private DatabaseHandler dbh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_card__returns);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText=(TextView)findViewById(R.id.myTitle);
		if(myTitleText!=null) {
			myTitleText.setText("Cards Return");
		}
		
		SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		String sql = "select CS.DENOMINATION as DENO, CS.BULK_NO as BULKNO, CS.START_SERIAL as STARTSERIAL, NS.NEXT_SERIAL_VALUE as NEXTSERIAL, CS.END_SERIAL as ENDSERIAL, CS.NO_OF_CARDS as CARDS from TSR_RECEIVED_CARD_BUNDLES CS left join NEXT_SERIAL NS ON CS.BULK_NO = NS.BULK_NO AND CS.END_SERIAL = NS.END_SERIAL AND CS.DENOMINATION = NS.DENOMINATION AND CS.START_SERIAL = NS.START_SERIAL WHERE (NS.IS_ALL_SOLD = 0 or NS.IS_ALL_SOLD is null) and CS.CARD_RETURNS = '0' ";
		Cursor cursor = db.rawQuery(sql, null);
		listData.clear();
		if(cursor!= null){
	  	 	 if(cursor.moveToFirst()){
	  	 		for (int i = 0; i < cursor.getCount(); i++){
	  	 			HashMap<String,String> map = new HashMap<String, String>();
	  	 			String nextserail = cursor.getString(cursor.getColumnIndex("NEXTSERIAL"));
	  	 			int a = 0;
	  	 			if (nextserail == null || nextserail.isEmpty()) {
						a = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CARDS")));
					} else {
						a = (1 + (Integer.parseInt(cursor.getString(cursor.getColumnIndex("ENDSERIAL"))) - Integer.parseInt(cursor.getString(cursor.getColumnIndex("NEXTSERIAL")))));
					}
 	            	map.put(columnTags[0], cursor.getString(cursor.getColumnIndex("DENO")));
 	            	map.put(columnTags[1], cursor.getString(cursor.getColumnIndex("BULKNO")));
 	            	map.put(columnTags[2], cursor.getString(cursor.getColumnIndex("STARTSERIAL")));
 	            	map.put(columnTags[3], nextserail);
 	            	map.put(columnTags[4], cursor.getString(cursor.getColumnIndex("ENDSERIAL")));
 	            	map.put(columnTags[5], String.valueOf(a));
 	            	listData.add(map);
	  	 			cursor.moveToNext();
	  	 		}
	  	 	 }
		}		
		cursor.close();
		db.close();
		SimpleAdapter adapter = new SimpleAdapter(Card_Returns.this, listData, R.layout.activity_card__returns__details, columnTags, columnIds);
		ListView lvData =(ListView)findViewById(R.id.listcardreturns);
		lvData.setAdapter(adapter);
		
	}
	
	public SimpleAdapter setAdapter(ArrayList<HashMap<String, String>> listData,int[] columnIds,String[] columnTags){
        SimpleAdapter adapter =new SimpleAdapter(this, listData, R.layout.activity_card__returns__details, columnTags , columnIds);        
        return adapter;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_card__returns, menu);
		return true;
	}
	
	public void OnClickremovebutton(View view) {

		LinearLayout vwParentRow = (LinearLayout) view.getParent();
	    TextView Deno = (TextView)vwParentRow.getChildAt(0);
	    TextView bulk = (TextView)vwParentRow.getChildAt(1);
	    TextView startserial = (TextView)vwParentRow.getChildAt(2);
	    TextView nextserial = (TextView)vwParentRow.getChildAt(3);
	    TextView endserial = (TextView)vwParentRow.getChildAt(4);
	    TextView NumberOfCard = (TextView)vwParentRow.getChildAt(5);
	      
	    Spinner returnType = (Spinner) findViewById(R.id.Spn_cardreturn);
	    String cardreturn = "C";
	    if ((returnType.getSelectedItem().toString()).equals("Card Return")) {
	      cardreturn = "R";
		} else {
		  cardreturn = "C";
		}
	    dbh = new DatabaseHandler(getApplicationContext());
	    dbh.ReturnCards( Deno.getText().toString(), bulk.getText().toString(), startserial.getText().toString(), nextserial.getText().toString(), endserial.getText().toString(), NumberOfCard.getText().toString(), cardreturn );
	     
	    Intent intent1 = new Intent(Card_Returns.this,Card_Returns.class);
	    startActivity(intent1);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), home.class);
		startActivity(intent);
		finish(); 
        return;
	}

}
