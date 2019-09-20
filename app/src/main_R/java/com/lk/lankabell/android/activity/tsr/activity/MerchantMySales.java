/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class MerchantMySales extends Activity {
	
	private String startDate = null;
	private String endDate = null;
	
	private String startdatevalue;
	private String enddatevalue;
	
	DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
	TextView lblDateAndTime;
	Calendar myCalendar1 = Calendar.getInstance();
	Calendar myCalendar2 = Calendar.getInstance();
	TextView lblStartDate ;
	TextView lblendDate;
	SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("yyyy/MM/dd");
	
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
	public void onDateSet(DatePicker view, int year, int month,
			int day) {
	myCalendar1.set(Calendar.YEAR, year);
	myCalendar1.set(Calendar.MONTH, month);
	myCalendar1.set(Calendar.DAY_OF_MONTH, day);
	updateLabel1();
	}
	};
	
	private void updateLabel1() {
		startDate = myCalendar1.getTime().toString();
		lblStartDate = (TextView)findViewById(R.id.lblMerchantwiseStartDate);
		lblStartDate.setText((dateformatMMDDYYYY.format(myCalendar1.getTime())).toString());
	}
	
	DatePickerDialog.OnDateSetListener e = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int month,int day) {
		myCalendar2.set(Calendar.YEAR, year);
		myCalendar2.set(Calendar.MONTH, month);
		myCalendar2.set(Calendar.DAY_OF_MONTH, day);
		updateLabel2();
		}
		};
		
		private void updateLabel2() {
			endDate = myCalendar2.getTime().toString();
			lblendDate = (TextView)findViewById(R.id.lblMerchantwiseEndDate);
			lblendDate.setText((dateformatMMDDYYYY.format(myCalendar2.getTime())).toString());
		}
		
		public void onClick(DialogInterface arg0, int which) {
			// TODO Auto-generated method stub
		}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysales_merchantwise);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		DataBindToItemTypeSpinner();
		Button btnstartdate = (Button) findViewById(R.id.btnMerchantwiseStartdate);
		btnstartdate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(MerchantMySales.this, d, myCalendar1
						.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
						myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		
		Button btnenddate=(Button)findViewById(R.id.btnMerchantwiseEnddate);
		btnenddate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new DatePickerDialog(MerchantMySales.this, e, myCalendar2
						.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
						myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
      	   	
      	
			Button btnViewSales = (Button)findViewById(R.id.btnMerchantwiseViewMySales);	
			final ListView lv=(ListView)findViewById(R.id.lvMerchantwiseitems);
			final ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
			final int[] columnIds = new int[] {R.id.lstDateSalesColumn1, R.id.lstCardSalesColumn2, R.id.lstDenomSalesColumn3,R.id.lstQtySalesColumn4};
			final String[] columnTags = new String[] {"Merchant","Denomination","Quantity","Date"}; 

	      	SQLiteDatabase db=openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
	      	final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

	      	btnViewSales.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			// TODO Auto-generated method stub
				
				lblStartDate = (TextView)findViewById(R.id.lblMerchantwiseStartDate);
				lblendDate = (TextView)findViewById(R.id.lblMerchantwiseEndDate);
				
				startdatevalue = lblStartDate.getText().toString();
				enddatevalue = lblendDate.getText().toString();
				
				Spinner spnCardType = (Spinner)findViewById(R.id.spnSalesCardType);
				String cardType =  spnCardType.getSelectedItem().toString();
					
				if(startdatevalue !="" && enddatevalue !="" )
				{

					SQLiteDatabase db=openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		      	 	Cursor OrderCursor = db.rawQuery("SELECT A.MERCHANT_ID,B.DENOMINATION,B.NO_OF_CARDS,A.ENTRY_DATE FROM SALES_HEADER A,SALES_DETAILS B WHERE  A.INVOICE_ID = B.INVOICE_ID AND CARD_TYPE ='"+cardType+"' ",null);	
		      	 	 	
		      	 String fromdate = lblStartDate.getText().toString();
                 String mindate=dbh.GetminDate();
                 String mindate1 =null;
                 
				if(mindate != "")
				{
					 mindate1 = dateformatMMDDYYYY.format(Date.parse(mindate));
				
				
		      	 {		
		      	 	
		      	 	int a = OrderCursor.getCount();
		      	 	
		      	 	listData.clear();
		      	 	if(OrderCursor!= null) 
		      	 	{

		      	 	 if(OrderCursor.moveToFirst()){
		                   for (int i = 0; i < OrderCursor.getCount(); i++){
		                  	 
		                  	 				
		                  	            	 HashMap<String,String> map = new HashMap<String, String>();
		                  	            	 
		                  	            	 String enteredDate_S = OrderCursor.getString(OrderCursor.getColumnIndex("ENTRY_DATE"));
		                  	            	 String startDate_S =  lblStartDate.getText().toString();
		                  	            	 String endDate_S = lblendDate.getText().toString();
		                  	            	 
		                  	            	Date enteredDate = null;
		                  	            	Date StartDate =null;
		                  	            	Date endDate = null;
		                  	            	 
		                  	            	try {
		                  	            		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		                  	            		enteredDate= (Date) sdf1.parse(enteredDate_S);
											} catch (ParseException e) {
												// 
												e.printStackTrace();
											}
		                  	            	
		                  	            	try {
												StartDate= (Date) dateformatMMDDYYYY.parse(startDate_S);
											} catch (ParseException e) {
												// 
												e.printStackTrace();
											}
		                  	            	
		                  	            	try {
												endDate= (Date) dateformatMMDDYYYY.parse(endDate_S);
											} catch (ParseException e) {
												// 
												e.printStackTrace();
											}

		                  	            	 if(endDate.after(enteredDate) == true || endDate == enteredDate )        	            	
		                  	            	 {
		                  	            		if(enteredDate.after(StartDate) == true || StartDate == enteredDate )
		                  	            		{
		                  	            			
		                  	            			if (listData.size() > 0) {
														
		                  	            		    int count = listData.size();
		                  	            			for (int j = 0; j < count ; j++) {

		                  	            				HashMap<String,String> map1 = new HashMap<String, String>();
		                  	            				map1 = listData.get(j);		
		                  	            				
		                  	            				
														if(map1.get("Merchant").toString().equals((OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")))) &&
																(map1.get("Denomination")).toString().equals(OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")))  &&
																(map1.get("Date")).toString().equals(OrderCursor.getString(OrderCursor.getColumnIndex("ENTRY_DATE"))))
															
														{
															int totalCards = Integer.parseInt(OrderCursor.getString(OrderCursor.getColumnIndex("NO_OF_CARDS"))) +
																							Integer.parseInt(map1.get(columnTags[3]).toString());
															
															listData.remove(map1);
															
															map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
				                  	            			map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")));
				                  	            			map.put(columnTags[3], OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")));
				                  	            			map.put(columnTags[2], Integer.toString(totalCards) );
				                  	            			listData.add(map);
				                  	            			break;
														}
														else
														{
															map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
				                  	            			map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("CARD_TYPE")));
				                  	            			map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")));
				                  	            			map.put(columnTags[3],  OrderCursor.getString(OrderCursor.getColumnIndex("NO_OF_CARDS")));
				                  	            			listData.add(map);
				                  	            			break;
														}
													}
		                  	            			
		                  	            			
		                  	            				}
		                  	            				else
		                  	            				{
		                  	            					map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
		                  	            					map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("CARD_TYPE")));
		                  	            					map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")));
		                  	            					map.put(columnTags[3],  OrderCursor.getString(OrderCursor.getColumnIndex("NO_OF_CARDS")));
		                  	            					listData.add(map);
		                  	            				}
		                  	          				
		                  	            			
		                  	            		}
		                  	            	 }

		                  	 OrderCursor.moveToNext();
		                     }//end of for
		              }
		      	 	OrderCursor.close();
		      	     db.close();
		      	     
		      	   SimpleAdapter adapter = new SimpleAdapter(MerchantMySales.this, listData, R.layout.mysaleslist,columnTags , columnIds);
		      	   lv.setAdapter(adapter);   
		      	 }
		     }	
		}
		else {
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MerchantMySales.this);

    		dlgAlert.setMessage("Data is Insufficient. Do you want to load it from online ?");
    		dlgAlert.setTitle("Alert");
    		
    		dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			})
    		
    		.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(),MerchantMySales.class);
            		startActivity(intent);
				}
			});
		}
			
	}
	else
	{
		Erroralert("Message", "Please select start & end dates", R.drawable.icon);
	}
}
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});	
		   SimpleAdapter adapter = new SimpleAdapter(this, listData, R.id.lvitems,columnTags , columnIds);
	  	   lv.setAdapter(adapter);
		
	}
	
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
	
	public void DataBindToItemTypeSpinner() {
		SQLiteDatabase DB = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Spinner spnItems = (Spinner)findViewById(R.id.spnSalesCardType);
		Cursor spnCursor = DB.rawQuery("SELECT DISTINCT(CARD_TYPE) FROM CARD_DENOMINATOIN",null);
		
		String[] array_spinner = new String[spnCursor.getCount()];
		if(spnCursor.moveToFirst())
		{

			for (int i = 0; i < spnCursor.getCount(); i++) {
				array_spinner[i] = spnCursor.getString(spnCursor.getColumnIndex("CARD_TYPE") );
				spnCursor.moveToNext();
			}
		}
		spnCursor.close();
		DB.close();
		
		
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, array_spinner);
		spnItems.setAdapter(adapter);
	
	}

}
