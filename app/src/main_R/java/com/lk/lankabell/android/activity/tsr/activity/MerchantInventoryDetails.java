/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.MerchantInventory;
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
 * @author Jei
 * 
 * This class is used to display the Merchant Inventory data.
 *
 */
public class MerchantInventoryDetails extends Activity {

	private DatabaseHandler dbh;
	private String cardType[];
	private String denomination[];
	private String quantity[];
	private String reorderLevel[];
	private String percentage[];
	
	TableLayout tl;
	TableRow tr;
	TextView cardTypeTextView,denominationTextView,quantityTextView,reorderLevelTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchantinventorydetails);
		
		String MerchantName = getIntent().getExtras().getString("merchantName");
		String Address = getIntent().getExtras().getString("Address");
		String id = getIntent().getExtras().getString("merchantID");
		
		TextView lblName =(TextView)findViewById(R.id.lblName);
		lblName.setText(MerchantName);
		TextView lblAddress =(TextView)findViewById(R.id.lblAddress);
		//TextView lblMerchantId =(TextView)findViewById(R.id.lblMerchantId);
		lblAddress.setText(Address);//merchantlistview
	//	lblMerchantId.setText(id);//merchantlistview

		SQLiteDatabase db=openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Cursor InventoryDetailCursor = db.rawQuery("SELECT 	CARD_TYPE,DENOMINATION,	STOCK_IN_HAND,REORDER_LEVEL FROM MERCHANT_INVENTORY WHERE MERCHANT_ID = "+Long.parseLong(id)+"", null);
		
		int count = InventoryDetailCursor.getCount();
		
		cardType =new String[count];
		denomination = new String[count];
		quantity = new String[count];
		reorderLevel = new String[count];
		percentage = new String[count];
		ArrayList<MerchantInventory> MerchantInventorylist = new ArrayList<MerchantInventory>();
		
		while (InventoryDetailCursor.moveToNext()) {
			MerchantInventorylist.add(new MerchantInventory(InventoryDetailCursor.getString(InventoryDetailCursor.getColumnIndex("CARD_TYPE")).toString(), Integer.parseInt(InventoryDetailCursor.getString(InventoryDetailCursor.getColumnIndex("DENOMINATION")).toString()), Integer.parseInt(InventoryDetailCursor.getString(InventoryDetailCursor.getColumnIndex("STOCK_IN_HAND")).toString()), Integer.parseInt(InventoryDetailCursor.getString(InventoryDetailCursor.getColumnIndex("REORDER_LEVEL")).toString())) );
		}
		
		for (int i = 0; i < MerchantInventorylist.size(); i++) {
			
			MerchantInventory merchantInventory = MerchantInventorylist.get(i); 
			String cardName = merchantInventory.GetCardType();

			if(cardName.equals("")){
				
			}
			else
			{
				cardType[i] = merchantInventory.GetCardType();
				denomination[i] = Integer.toString(merchantInventory.GetDenomination());
				quantity[i] = Integer.toString(merchantInventory.GetQuantity());
				reorderLevel[i] = Integer.toString(merchantInventory.GetReorderLevel());
				percentage[i] =Double.toString(Double.parseDouble(quantity[i].toString()) / Double.parseDouble(reorderLevel[i].toString()));
			}
		}
		
		tl = (TableLayout) findViewById(R.id.tbMerchantDetails);
	//	tl.setColumnShrinkable(1, true);
		addHeaders();
		addData();
		
	}
	
	/**
	 * Headers for Merchant Inventory table.
	 */
	public void addHeaders(){
		
		/** This function add the headers to the <span id="IL_AD1" class="IL_AD">table</span> **/
		/** Create a TableRow dynamically **/
		tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		
		/** Creating a TextView to add to the row **/
		TextView CardType = new TextView(this);
		CardType.setText("Card Type");
		CardType.setTextColor(Color.WHITE);
		CardType.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		CardType.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		//CardType.setWidth(1);
		CardType.setPadding(3, 3, 3, 3);
		tr.addView(CardType);  // Adding textView to tablerow.
		
		//************************* Source Code added by Jei***************************************
		
		TextView Denomination=new TextView(this);
		
		Denomination.setText("Denom");
		Denomination.setTextColor(Color.WHITE);
		Denomination.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		Denomination.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		//Denomination.setWidth(1);
		Denomination.setPadding(3, 3, 3, 3);
		tr.addView(Denomination);
		
		/** Creating another textview **/
		TextView Quantity = new TextView(this);
		Quantity.setText("Qty");
		Quantity.setTextColor(Color.WHITE);
		Quantity.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		Quantity.setPadding(3, 3, 3, 3);
		//Quantity.setWidth(1);
		Quantity.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(Quantity); // Adding textView to tablerow.
		
		
		TextView ReorderLevel = new TextView(this);
		ReorderLevel.setText("R.O.Level");
		ReorderLevel.setTextColor(Color.WHITE);
		ReorderLevel.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		ReorderLevel.setPadding(3, 3, 3, 3);
		//ReorderLevel.setWidth(1);
		ReorderLevel.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(ReorderLevel); // Adding textView to tablerow.
		
		
		// Add the TableRow to the TableLayout
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		
		// we are adding two textviews for the divider because we have two columns
		tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		
		
		/** Creating another textview **/
		TextView divider = new TextView(this);
		divider.setText("---------");
		//divider.setTextColor(Color.<span id="IL_AD8" class="IL_AD">GREEN</span>);
		divider.setTextColor(Color.GRAY);
		divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		divider.setPadding(3, 3, 3, 3);
		divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(divider); // Adding textView to tablerow.
		
		TextView divider2 = new TextView(this);
		divider2.setText("---------");
		divider2.setTextColor(Color.GRAY);
		divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		divider2.setPadding(3, 3, 3, 3);
		divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(divider2); // Adding textView to tablerow.
		
		TextView divider3 = new TextView(this);
		divider3.setText("------");
		//divider.setTextColor(Color.<span id="IL_AD8" class="IL_AD">GREEN</span>);
		divider3.setTextColor(Color.GRAY);
		divider3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		divider3.setPadding(3, 3, 3, 3);
		divider3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(divider3); // Adding textView to tablerow.
		
		TextView divider4 = new TextView(this);
		divider4.setText("---------");
		//divider.setTextColor(Color.<span id="IL_AD8" class="IL_AD">GREEN</span>);
		divider4.setTextColor(Color.GRAY);
		divider4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		divider4.setPadding(3, 3, 3, 3);
		divider4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(divider4); // Adding textView to tablerow.


		// Add the TableRow to the TableLayout
		tl.addView(tr, new TableLayout.LayoutParams(
		    LayoutParams.FILL_PARENT,
		    LayoutParams.WRAP_CONTENT));

	}
	
	
	/**
	 * Adding data to the Merchant Inventory
	 */
	public void addData()
	{
		if (cardType != null) {
			for (int i = 0; i < cardType.length; i++) {
				
				/** Create a TableRow dynamically **/
				tr = new TableRow(this);
				tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				
				/** Creating a TextView to add to the row **/
				cardTypeTextView = new TextView(this);
				cardTypeTextView.setText(cardType[i]);
				cardTypeTextView.setTextColor(Color.WHITE);
				cardTypeTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				cardTypeTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				cardTypeTextView.setPadding(3, 3, 3, 3);
				tr.addView(cardTypeTextView);  // Adding textView to tablerow.
				
				denominationTextView = new TextView(this);
				denominationTextView.setText(denomination[i]);
				denominationTextView.setTextColor(Color.WHITE);
				denominationTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				denominationTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				denominationTextView.setPadding(3, 3, 3, 3);
				tr.addView(denominationTextView);  // Adding textView to tablerow.
				
				quantityTextView = new TextView(this);
				quantityTextView.setText(quantity[i]);
				if (Double.parseDouble(percentage[i].toString())> 1.5) {
					quantityTextView.setTextColor(getResources().getColor(R.color.CoolGreen));
				}
				else if (Double.parseDouble(percentage[i].toString())> 1) {
					quantityTextView.setTextColor(getResources().getColor(R.color.CoolYellow));
				}
				else if(Double.parseDouble(percentage[i].toString())<= 1) {
					quantityTextView.setTextColor(Color.RED);
				}
				quantityTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				quantityTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				quantityTextView.setPadding(2, 2, 2, 2);
				tr.addView(quantityTextView);  // Adding textView to tablerow.
				
				reorderLevelTextView = new TextView(this);
				reorderLevelTextView.setText(reorderLevel[i]);
				reorderLevelTextView.setTextColor(Color.WHITE);
				reorderLevelTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				reorderLevelTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				reorderLevelTextView.setPadding(3, 3, 3, 3);
				tr.addView(reorderLevelTextView);  // Adding textView to tablerow.
				
				tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			}
		}
	}

}
