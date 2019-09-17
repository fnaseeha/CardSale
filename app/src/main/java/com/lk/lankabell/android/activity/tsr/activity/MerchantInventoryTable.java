package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.Merchant;
import com.lk.lankabell.android.activity.tsr.R;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

/**
 * @author 
 * 
 * This class is used for Displaying Merchant Inventory.
 *
 */
public class MerchantInventoryTable extends Activity {
	
	private DatabaseHandler dbh;
	private String Merchant[];
	private String Address[];
	private String ButtonVal[];
	
	
	TableLayout tl;
	TableRow tr;
	TextView NameTextView,AddressTextView,Buttontv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchentinventorytable);
		
		dbh = new DatabaseHandler(getApplicationContext());
		SQLiteDatabase db=openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Cursor InventoryDetailCursor = db.rawQuery("SELECT A.MERCHANT_ID,A.MERCHANT_NAME,A.ADDRESS FROM MERCHANT_MASTER A WHERE A.IS_REGISTERED ='1' AND 	IS_ACTIVE ='1'", null);
		
		int count = InventoryDetailCursor.getCount();
		Merchant = new String[count];
		Address = new String[count];
		ButtonVal = new String[count];
		
		ArrayList<com.lk.lankabell.android.activity.tsr.sqlite.Merchant> Merchantlist = new ArrayList<Merchant>();
		while (InventoryDetailCursor.moveToNext()) {
			Merchantlist.add(new Merchant(InventoryDetailCursor.getLong(InventoryDetailCursor.getColumnIndex("MERCHANT_ID")), InventoryDetailCursor.getString(InventoryDetailCursor.getColumnIndex("MERCHANT_NAME")).toString(), InventoryDetailCursor.getString(InventoryDetailCursor.getColumnIndex("ADDRESS")).toString(), "", 0, "", "", "", 0, 1,""));
		}
		
		for (int i = 0; i < Merchantlist.size(); i++) {
			
			Merchant merchant = Merchantlist.get(i); 
			String merchantName = merchant.getName();

			if(merchantName.equals(""))
			{
				
			}
			else
			{
				Merchant[i] = merchant.getName();
				Address[i] = merchant.getAddress();
				ButtonVal[i] ="jj";
			}
		}
		
		tl = (TableLayout) findViewById(R.id.tbMerchantList);
		addData();
	}
	
	/**
	 * Adding data to the Merchant inventory table.
	 */
	public void addData()
	{
		if (Merchant != null) {
			for (int i = 0; i < Merchant.length; i++) {
				
				/** Create a TableRow dynamically **/
				tr = new TableRow(this);
				tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				
				/** Creating a TextView to add to the row **/
				NameTextView = new TextView(this);
				NameTextView.setText(Merchant[i]);
				NameTextView.setTextColor(Color.WHITE);
				NameTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				NameTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				NameTextView.setPadding(1, 1, 1, 1);
				tr.addView(NameTextView);  // Adding textView to tablerow.
				
				AddressTextView = new TextView(this);
				AddressTextView.setText(Address[i]);
				AddressTextView.setTextColor(Color.WHITE);
				AddressTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				AddressTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				AddressTextView.setPadding(1, 1, 1, 1);
				tr.addView(AddressTextView);  // Adding textView to tablerow.
				
				Buttontv = new TextView(this);;
				Buttontv.setText(ButtonVal[i]);
				Buttontv.setTextColor(Color.WHITE);
				Buttontv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				Buttontv.setPadding(1, 1, 1, 1);
				tr.addView(Buttontv);  // Adding textView to tablerow.
				
				tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			}
		}
	}

}
