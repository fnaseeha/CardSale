package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.Serials;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.R;


/**
 * @author Jei
 *
 * This class is used to Perform Card Sales functionalities. 
 */
public class CardSaleActivity extends Activity   {		//implements android.content.DialogInterface.OnClickListener
		
	 ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
	 int[] columnIds = new int[] {R.id.lst_Row_CardType, R.id.lst_Row_Quantity,R.id.lst_Row_Denomination, R.id.lst_Row_Total,R.id.btnDelete};
	 String[] columnTags = new String[] {"Item", "noofcards","discount_Price","Dis_Amount","","","Real_price","startSerial","endSerial","RealAmount","Difference"};
       
	
	private DatabaseHandler dbh;
	private String city;
	Spinner spnItems;
	Spinner denominationSpinner;
	Spinner spnBulkNo;
	EditText txtqty;
	EditText txtstartSerial;
	EditText txtendSerial;
	EditText txtnoCards;
	String merchant;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.cardsale);	
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		TextView myTitleText=(TextView)findViewById(R.id.myTitle);
		myTitleText.setText("Card Sales");

		DataBindToItemTypeSpinner();
		 
		merchant =  getIntent().getExtras().getString("merchantName");		
		TextView lblMerchanttext = (TextView)findViewById(R.id.lblMerchantName);
		lblMerchanttext.setText(merchant);
		
		spnItems = (Spinner)findViewById(R.id.spnItems);
		
		
		
			spnItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {			
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
				
				dbh = new DatabaseHandler(getApplicationContext());
				final Spinner itemSpinner = (Spinner)findViewById(R.id.spnItems);
				DataBindToDenominationTypeSpinner(itemSpinner.getSelectedItem().toString());
		    } 
		    public void onNothingSelected(AdapterView<?> adapterView) {
		        return;
		    } 
		});	
		
		
		
		
		final Spinner denominationSpinner = (Spinner)findViewById(R.id.spnDenomination);
		denominationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {		
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
				
				dbh = new DatabaseHandler(getApplicationContext());
				
				DataBindToBulkNoeSpinner(spnItems.getSelectedItem().toString(),denominationSpinner.getSelectedItem().toString());
		    } 
		    public void onNothingSelected(AdapterView<?> adapterView) {
		        return;
		    } 
		});
				
	}
	
	public void exitOnClick(View view) {

		// Switching to sub menu screen
		finish();
		System.exit(0);
	}
	
	
	
	/**
	 * @param title
	 * @param msg
	 * @param resourceID
	 * 
	 * Alerting function.
	 */
	public void alert(String title,String msg,int resourceID)
	{
		
		 AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(msg);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      // here you can add functions
				   
			   }
			});
			alertDialog.setIcon(resourceID);
			alertDialog.show();
	}
	
	//  Card Type Spinner binding in Card Sales.
	
	/**
	 * Binding the item data to the spinner.
	 */
	public void DataBindToItemTypeSpinner() {
		SQLiteDatabase DB = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		spnItems = (Spinner)findViewById(R.id.spnItems);
		Cursor spnCursor = DB.rawQuery("SELECT TYPE_CODE FROM CARD_TYPES ORDER BY case TYPE_CODE when 'CDMA_PP' then 1 when 'VIZA_PP' then 2 else 3 end",null);
		
		String[] array_spinner = new String[spnCursor.getCount()];
		if(spnCursor.moveToFirst())
		{

			for (int i = 0; i < spnCursor.getCount(); i++) {
				array_spinner[i] = spnCursor.getString(spnCursor.getColumnIndex("TYPE_CODE") );
				spnCursor.moveToNext();
			}
		}
		spnCursor.close();
		DB.close();
		
		
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, array_spinner);
		spnItems.setAdapter(adapter);
	
	}
	
	
	
	/**
	 * @param cardType
	 * Binding the Denomination type to Spinner.
	 * 
	 */
	public void DataBindToDenominationTypeSpinner(String cardType) {
		SQLiteDatabase DB = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		denominationSpinner = (Spinner)findViewById(R.id.spnDenomination);
		Cursor spnCursor = DB.rawQuery("SELECT DENOMINATION FROM CARD_DENOMINATOIN WHERE CARD_TYPE = '"+cardType+"'",null);
		
		String[] array_spinner = new String[spnCursor.getCount()];
		if(spnCursor.moveToFirst())
		{
			for (int i = 0; i < spnCursor.getCount(); i++) {
				array_spinner[i] = spnCursor.getString(spnCursor.getColumnIndex("DENOMINATION") );
				spnCursor.moveToNext();
			}
		}
		spnCursor.close();
		DB.close();
		
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, array_spinner);
		denominationSpinner.setAdapter(adapter);
		txtstartSerial =(EditText)findViewById(R.id.txtStartSerial);
		txtendSerial =(EditText)findViewById(R.id.txtEndSerial);
		txtnoCards=(EditText)findViewById(R.id.txtNoofCards);
		txtstartSerial.setText("");
		txtendSerial.setText("");
		txtnoCards.setText("");
	}
	
	
	/**
	 * @param cardType
	 * @param denomination
	 * 
	 * Binding the Bulk No to Spinner.
	 */
	public void DataBindToBulkNoeSpinner(String cardType,String denomination) {
		
		
		
		SQLiteDatabase DB = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		spnBulkNo = (Spinner)findViewById(R.id.spnBulkId);
		int value = 1;
		String sql="SELECT DISTINCT(t.BULK_NO) FROM TSR_RECEIVED_CARD_BUNDLES t,next_serial n WHERE t.CARD_TYPE = '"+cardType+"' AND t.DENOMINATION ='"+denomination+"' AND t.IS_ACCEPTED= "+value+"  and n.START_SERIAL=t.START_SERIAL and n.END_SERIAL=t.END_SERIAL and n.BULK_NO=t.BULK_NO and n.IS_ALL_SOLD='0' ";
		Cursor spnCursor = DB.rawQuery(sql,null);
		
		String[] array_spinner = new String[spnCursor.getCount()];
		if(spnCursor.moveToFirst())
		{
			for (int i = 0; i < spnCursor.getCount(); i++) {
				array_spinner[i] = spnCursor.getString(spnCursor.getColumnIndex("BULK_NO") );
				spnCursor.moveToNext();
			}
		}
		spnCursor.close();
		DB.close();

		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, array_spinner);
		spnBulkNo.setAdapter(adapter);
		txtstartSerial =(EditText)findViewById(R.id.txtStartSerial);
		txtendSerial =(EditText)findViewById(R.id.txtEndSerial);
		txtnoCards=(EditText)findViewById(R.id.txtNoofCards);
		txtstartSerial.setText("");
		txtendSerial.setText("");
		txtnoCards.setText("");
		
		spnBulkNo.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			int i=spnBulkNo.getSelectedItemPosition();
			
		  public void onItemSelected(AdapterView<?> parent, View view,
				int pos, long id) {

			  
			  dbh = new DatabaseHandler(getApplicationContext());
			  User user = dbh.getUserDetails();
				

					if (dbh.CheckEndSerialAlreadyExistInSales(user.getId(), spnItems.getSelectedItem().toString(), Integer.parseInt(denominationSpinner.getSelectedItem().toString()) ,spnBulkNo.getSelectedItem().toString()) == true) {
						int StartSerialValue = dbh.GetEndSerialNonSoldFirst(user.getId(), spnItems.getSelectedItem().toString(), Integer.parseInt(denominationSpinner.getSelectedItem().toString()) ,spnBulkNo.getSelectedItem().toString());
						txtstartSerial.setText(Integer.toString(StartSerialValue));
					}
					

		}

		public void onNothingSelected(AdapterView<?> arg0) {

			
		}
		});
		
		
	}
	
	
	// Add
	
	   /**
	 * @param view
	 * Adding the card sale details to the database.
	 * 
	 */
	public void OnclicKAdd(View view) {
		TextView error = (TextView) findViewById(R.id.lblCarderror); 
		spnItems = (Spinner)findViewById(R.id.spnItems);
		spnBulkNo = (Spinner)findViewById(R.id.spnBulkId);
		denominationSpinner = (Spinner)findViewById(R.id.spnDenomination);
		txtqty =(EditText)findViewById(R.id.txtNoofCards);
		txtstartSerial =(EditText)findViewById(R.id.txtStartSerial);
		txtendSerial =(EditText)findViewById(R.id.txtEndSerial);
		txtnoCards=(EditText)findViewById(R.id.txtNoofCards);

		if (txtstartSerial.getText().toString().trim().equalsIgnoreCase("") || txtstartSerial.getText().toString().trim() == null ) {
			Erroralert("Message", " Please Enter Start Serial", R.drawable.icon);
			return;
		}
		else if (txtendSerial.getText().toString().trim().equalsIgnoreCase("")|| txtendSerial.getText().toString().trim() == null ) {
			Erroralert("Message", " Please Enter End Serial", R.drawable.icon);
			return;
		}
		else if (txtnoCards.getText().toString().trim().equalsIgnoreCase("") || txtnoCards.getText().toString().trim() == null) {
			Erroralert("Message", " Please Enter Quantity", R.drawable.icon);
			return;
		}	
		else if(spnItems.getSelectedItem().toString().trim().equalsIgnoreCase("") || spnItems.getSelectedItem().toString().trim() == null){
			Erroralert("Message", " Please Enter Card Type", R.drawable.icon);
			return;
		} 
		else if(dbh.GetInvoiceNumber() < 100){
			Erroralert("Message", "Invoice ID is Incorrect. Do the data reload and try again.", R.drawable.icon);
			return;
		}
		else{
			int enteredStarVale =Integer.parseInt(txtstartSerial.getText().toString());
			int enteredEndVale =Integer.parseInt(txtendSerial.getText().toString());
			int total=(Integer.valueOf(txtendSerial.getText().toString()) - Integer.valueOf(txtstartSerial.getText().toString()))+ 1;
			if(Integer.parseInt(txtnoCards.getText().toString()) != total){
				Erroralert("Message", "No of Cards mismatch", R.drawable.icon);
			} else{
				if(spnBulkNo.getSelectedItem().toString().equalsIgnoreCase(""))	{
					Erroralert("Message", " Please Enter Bulk No", R.drawable.icon);
				} else {
					Integer listStartSerial=0;
					Integer listEndSerial=0;
					Integer listNextSerial=0;
				
				User user = dbh.getUserDetails();
				ArrayList<Serials> list=dbh.getNonSoldStartNextAndEndSerials(user.getId(), spnItems.getSelectedItem().toString(), Integer.parseInt(denominationSpinner.getSelectedItem().toString()), spnBulkNo.getSelectedItem().toString(),String.valueOf(enteredStarVale));
				
				if (list.isEmpty()){
					Erroralert("Message", "Start Serial Invalid", R.drawable.icon);
				}
				
				for (Serials serials : list) {
					listStartSerial = serials.getStartSerial();
					listEndSerial = serials.getEndSerial();
					listNextSerial = serials.getNextSerial();
					//========================================================================================================
					if(listNextSerial!=enteredStarVale){
						Erroralert("Message", "Start Serial Invalid", R.drawable.icon);
					}
					else if(listStartSerial>enteredStarVale){
						Erroralert("Message", "Start Serial Invalid", R.drawable.icon);
					}
					else if(listEndSerial <enteredEndVale){
						Erroralert("Message", "End Serial Invalid", R.drawable.icon);
					}
					else{ 
						SQLiteDatabase db=openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
				  	 	Cursor OrderCursor = db.query("SALES_TEMP_DETAILS", new String[] { "CARD_TYPE","DENOMINATION","BULK_NO","NO_OF_CARDS","LINE_AMOUNT","START_SERIAL","END_SERIAL","LINE_DISCOUNT","listStartSerial"},null , null, null, null,null);  
				  	 	boolean currentcardisadd = true;
				  	 	Log.d("My current adding values are  checking : ", "Came to check this..........######### ");
				  	 	if(OrderCursor!= null) {
				  	 		if(OrderCursor.moveToFirst()){
				  	 			for (int i = 0; i < OrderCursor.getCount(); i++){
				  	 				String AddCardType = OrderCursor.getString(OrderCursor.getColumnIndex("CARD_TYPE"));
				  	 				Integer AddcardDenomination = Integer.parseInt(OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")).trim());
				  	 				Integer AddCardBulkNo = Integer.parseInt(OrderCursor.getString(OrderCursor.getColumnIndex("BULK_NO")).trim());
				  	 				Integer addCardStartSerial = Integer.parseInt(OrderCursor.getString(OrderCursor.getColumnIndex("START_SERIAL")).trim());
				  	 				Integer currentBulkID = Integer.parseInt(spnBulkNo.getSelectedItem().toString().trim());
				  	 				Integer currentdenomination = Integer.parseInt(denominationSpinner.getSelectedItem().toString().trim());
				  	 				String currentcardtype = spnItems.getSelectedItem().toString();
				  	 				Log.d("My current adding values are : ",  (currentBulkID.equals(AddCardBulkNo) +" === "+  currentBulkID +" = "+AddCardBulkNo +" **** "+ AddCardType.equals(currentcardtype)) +" 0011   |  "+ (AddcardDenomination == currentdenomination) +"  0022  |  "+ (AddCardBulkNo == currentBulkID) +"  0033  |  "+ (addCardStartSerial == enteredStarVale) );
				  	 				Log.d("My current adding values checking state ment is  : ", " ******* "+ ((AddCardType.equals(currentcardtype)) & (AddcardDenomination == currentdenomination) & ((AddCardBulkNo == currentBulkID) || currentBulkID.equals(AddCardBulkNo)) & (addCardStartSerial == enteredStarVale) ) );
				  	 				if ( (AddCardType.equals(currentcardtype)) & (AddcardDenomination == currentdenomination) & ((AddCardBulkNo == currentBulkID) || currentBulkID.equals(AddCardBulkNo)) & (addCardStartSerial == enteredStarVale) ) {
				  	 					Log.d("My current adding values are : ", "Came to the if statement in the check this.......... ");
				  	 					Erroralert("Message", "sorry this serial is already taken to card sales.", R.drawable.icon);
				  	 					currentcardisadd = false;
				  	 					return;
				  	 				}
				  	 				OrderCursor.moveToNext();
				  	 			}
				  	 		}
					  	 	OrderCursor.close();
					  	    db.close();
				  	 	}
				  	 	if (currentcardisadd) {
				  	 			dbh = new DatabaseHandler(getApplicationContext());
				  	 			int qty = Integer.parseInt(txtqty.getText().toString());
				  	 			double unitPrice =dbh.getValueByItemName(spnItems.getSelectedItem().toString(), Integer.parseInt(denominationSpinner.getSelectedItem().toString()));
				  	 			double discountRate = dbh.getDiscountPriceByItemName(spnBulkNo.getSelectedItem().toString(), Integer.parseInt(denominationSpinner.getSelectedItem().toString()), spnItems.getSelectedItem().toString());
				  	 			double discountPrice = (100 - discountRate) * unitPrice *0.01;
				  	 			double lineAmount = discountPrice * qty;
				  	 			double linediscount = (unitPrice - discountPrice) * qty;
				  	 		
				  	 			dbh.SaveSalesDetailsTemp(spnItems.getSelectedItem().toString(), Integer.parseInt(denominationSpinner.getSelectedItem().toString()), spnBulkNo.getSelectedItem().toString(), qty, txtstartSerial.getText().toString(), txtendSerial.getText().toString(), lineAmount, linediscount,discountRate,String.valueOf(listStartSerial));
				  	 			String merchantId = getIntent().getExtras().getString("merchantID");
				  	 		
				  	 			Intent intent = new Intent(getApplicationContext(), CardCategoryList.class);
				  	 			intent.putExtra("merchantName", merchant);
				  	 			intent.putExtra("merchantID", merchantId);
				  	 			intent.putExtra("city", city);
				  	 			intent.putExtra("startSerial", listStartSerial);
				  	 			startActivity(intent);
				  	 			finish();
				  	 		}
					   	}					
					//========================================================================================================
				}

				}
				}
			}
		}

	/**
	 * @param title
	 * @param msg
	 * @param resourceID
	 * 
	 * Error Alert function in case of any errors.
	 */
	@SuppressWarnings("deprecation")
	public void Erroralert(String title,String msg,int resourceID)
	{
		
		 AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(msg);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      // here you can add functions
				   
			   }
			});
			alertDialog.setIcon(resourceID);
			alertDialog.show();
	}
}

