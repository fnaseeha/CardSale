package com.lk.lankabell.android.activity.tsr.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;
import com.lk.lankabell.android.activity.tsr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MysalesDateWise extends Activity {
	
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
	
	// Lankabell Test server IP
	private String URL = WsdlReader.getServiceUrl(true,this);
    
    private static String SOAP_ACTION="http://mainService/MySalesDateWise";
	private static String NAMESPACE = "http://mainService";  
	private static String METHOD_NAME = "MySalesDateWise";

	    
	    DatePickerDialog.OnDateSetListener d =  new DatePickerDialog.OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int month,
					int day) {
				// TODO Auto-generated method stub
				myCalendar1.set(Calendar.YEAR, year);
				myCalendar1.set(Calendar.MONTH, month);
				myCalendar1.set(Calendar.DAY_OF_MONTH, day);
				updateLabel1();
			}
		};
		
		private void updateLabel1() {
			startDate = myCalendar1.getTime().toString();
			lblStartDate = (TextView)findViewById(R.id.lblSalesStartDate);
			lblStartDate.setText((dateformatMMDDYYYY.format(myCalendar1.getTime())).toString());
		}
		
		DatePickerDialog.OnDateSetListener e = new DatePickerDialog.OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar2.set(Calendar.YEAR, year);
				myCalendar2.set(Calendar.MONTH, monthOfYear);
				myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel2();
			}
		};
		
		private void updateLabel2() {
			endDate = myCalendar2.getTime().toString();
			lblendDate = (TextView)findViewById(R.id.lblSalesEndDate);
			lblendDate.setText((dateformatMMDDYYYY.format(myCalendar2.getTime())).toString());
		}
		
		
		public void onClick(DialogInterface arg0, int which) {
			// TODO Auto-generated method stub
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysales_datewise);
		
		
		Button btnstartdate = (Button) findViewById(R.id.btnSalesStartdate);
		btnstartdate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(MysalesDateWise.this, d, myCalendar1
						.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
						myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		Button btnenddate=(Button)findViewById(R.id.btnSalesEnddate);
		btnenddate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new DatePickerDialog(MysalesDateWise.this, e, myCalendar2
						.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
						myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		
		Button btnviewData = (Button)findViewById(R.id.btnViewSalesData);
		
		final ListView lv=(ListView)findViewById(R.id.lvSalesitems);
		final ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
		final int[] columnIds = new int[] {R.id.lstDateSalesDateColumn1, R.id.lstCardSalesDateColumn2, R.id.lstDenomSalesDateColumn3,R.id.lstQtySaleDatesColumn4,R.id.lstQtySaleDatesColumn5};
		final String[] columnTags = new String[] {"date","cardtype", "denom","qty" ,"merchantId"}; 
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

		btnviewData.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				lblStartDate = (TextView)findViewById(R.id.lblSalesStartDate);
				lblendDate = (TextView)findViewById(R.id.lblSalesEndDate);
				
				startdatevalue = lblStartDate.getText().toString();
				enddatevalue = lblendDate.getText().toString();
				
				if (startdatevalue !="" && enddatevalue !="") {
					
					SQLiteDatabase db=openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		      	 	Cursor OrderCursor = db.rawQuery("SELECT B.ENTERED_DATE,A.CARD_TYPE,A.DENOMINATION,B.MERCHANT_ID ,SUM(A.NO_OF_CARDS)  AS CARD_COUNT FROM SALES_DETAILS A,SALES_HEADER B WHERE A.INVOICE_ID = B.INVOICE_ID GROUP BY B.ENTERED_DATE,A.CARD_TYPE,A.DENOMINATION,B.MERCHANT_ID ",null);
		      	 	listData.clear();
		      	 	
		      	 	 String mindate=dbh.GetminDate();
	                 String mindate1 =null;
	                 
					if(mindate != "")
					{
						 mindate1 = dateformatMMDDYYYY.format(Date.parse(mindate));
					}
					
					if (mindate1==null) {

				           
			      		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MysalesDateWise.this);

	            		dlgAlert.setMessage("Data Insufficient. Do you want to load from online ?");
	            		dlgAlert.setTitle("Alert");
	            		dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {

								// TODO Auto-generated method stub
								 SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
								 DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
								 User user = dbh.getUserDetails();
					      		  request.addProperty("strInputUserMobile",getMyPhoneNumber());		// mobile no,user name & password send
					              request.addProperty("strInputUserName",user.getUserName());
					              request.addProperty("strInputUserPassword",user.getPassword());
					              request.addProperty("strFromDate", lblStartDate.getText().toString());
					              request.addProperty("strToDate", lblendDate.getText().toString());
					              SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						            envelope.setOutputSoapObject(request);
						            envelope.dotNet = true;
						            try{
						            	HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
						            	androidHttpTransport.call(SOAP_ACTION, envelope);
						            	SoapObject result = (SoapObject)envelope.bodyIn; // return epf No		// check type
						           	if(result != null){
						           		HashMap<String,String> hashmap = new HashMap<String, String>();
						      	 		hashmap.put(columnTags[0], "MERCHANT");
						      	 		hashmap.put(columnTags[1], "CARD TYPE");
						      	 		hashmap.put(columnTags[2],"DENOM"); 
						      	 		hashmap.put(columnTags[3],"QTY");
						    	        listData.add(hashmap);

					            		String dbQuery5 =result.getProperty(0).toString();
										String Value5 = dbQuery5.replaceAll("\\[|\\]","");
										String[] statusarrayData5 = Value5.split("<");
										String[] firstValue = statusarrayData5[0].split(",");
							 			int statusvalue5 = Integer.parseInt(firstValue[0].toString());
										if(statusvalue5 == 1){
											listData.clear();
											for (int i = 1; i < statusarrayData5.length; i++) {
												String realValues =statusarrayData5[i].replaceAll(">", "");
												String[] statusarray5 = realValues.split("#");
												 HashMap<String,String> map = new HashMap<String, String>();
												 map.put(columnTags[0],statusarray5[3].toString().replace(",", ""));
				              	            	 map.put(columnTags[1],statusarray5[0].toString().replace(",", ""));
				              	            	 map.put(columnTags[2],statusarray5[1].toString().replace(",", ""));
				              	            	 map.put(columnTags[3],statusarray5[2].toString().replace(",", ""));
				              	            	 listData.add(map);
											}
											SimpleAdapter adapter = new SimpleAdapter(MysalesDateWise.this, listData, R.layout.activity_mysales_table,columnTags , columnIds);
									      	lv.setAdapter(adapter);
										}
					            	}
										
					            }
						        catch (Exception e){
						            e.printStackTrace();		
						        } 
							}
						})
						
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
		                		Intent intent = new Intent(getApplicationContext(),mysales.class);
		                		startActivity(intent);
		                	//	mysales.this.finish();
							}
						});

	            		dlgAlert.setCancelable(true);
	            		dlgAlert.create().show(); 
						
						
					}
					
					else if((dateformatMMDDYYYY.format(Date.parse(startdatevalue)).compareTo (mindate1))< 0) 
			      	 {
				           
			      		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MysalesDateWise.this);

	            		dlgAlert.setMessage("Data Insufficient. Do you want to load from online ?");
	            		dlgAlert.setTitle("Alert");
	            		dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {

								// TODO Auto-generated method stub
								 SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
								 
								 DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
								 User user = dbh.getUserDetails();
					      		 
					      		  request.addProperty("strInputUserMobile",getMyPhoneNumber());		// mobile no,user name & password send
					              request.addProperty("strInputUserName",user.getUserName());
					              request.addProperty("strInputUserPassword",user.getPassword());
					              request.addProperty("strFromDate", lblStartDate.getText().toString());
					              request.addProperty("strToDate", lblendDate.getText().toString());

					              SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						            envelope.setOutputSoapObject(request);
						            envelope.dotNet = true;

						            try
						              {
						            	
						            	HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
						            	androidHttpTransport.call(SOAP_ACTION, envelope);
						            	SoapObject result = (SoapObject)envelope.bodyIn; // return epf No		// check type

						           	if(result != null)
					            	{
						           		HashMap<String,String> hashmap = new HashMap<String, String>();

						      	 		hashmap.put(columnTags[0], "MERCHANT");
						      	 		hashmap.put(columnTags[1], "CARD TYPE");
						      	 		hashmap.put(columnTags[2],"DENOM"); 
						      	 		hashmap.put(columnTags[3],"QTY");
						    	        listData.add(hashmap);

					            		String dbQuery5 =result.getProperty(0).toString();
										String Value5 = dbQuery5.replaceAll("\\[|\\]","");
										String[] statusarrayData5 = Value5.split("<");
										String[] firstValue = statusarrayData5[0].split(",");
							 			int statusvalue5 = Integer.parseInt(firstValue[0].toString());
										if(statusvalue5 == 1)
										{

											listData.clear();
											for (int i = 1; i < statusarrayData5.length; i++) {
												
												String realValues =statusarrayData5[i].replaceAll(">", "");
												String[] statusarray5 = realValues.split("#");
												 HashMap<String,String> map = new HashMap<String, String>();
												 
												 map.put(columnTags[0],statusarray5[3].toString().replace(",", ""));
				              	            	 map.put(columnTags[1],statusarray5[0].toString().replace(",", ""));
				              	            	 map.put(columnTags[2],statusarray5[1].toString().replace(",", ""));
				              	            	 map.put(columnTags[3],statusarray5[2].toString().replace(",", ""));
				              	            	 listData.add(map);
												 
											}
											SimpleAdapter adapter = new SimpleAdapter(MysalesDateWise.this, listData, R.layout.activity_mysales_table,columnTags , columnIds);
									      	lv.setAdapter(adapter);
										}
					            	}
										
					            }
						        catch (Exception e)
						        {
						            e.printStackTrace();		
						        } 
							}
						})
						
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
		                		Intent intent = new Intent(getApplicationContext(),mysales.class);
		                		startActivity(intent);
		                	//	mysales.this.finish();
							}
						});

	            		dlgAlert.setCancelable(true);
	            		dlgAlert.create().show(); 
						}
					else if((dateformatMMDDYYYY.format(Date.parse(startdatevalue)).compareTo (mindate1)) >= 0)
					
					{

			      	 	if(OrderCursor!= null) 
			      	 	{

			      	 	 if(OrderCursor.moveToFirst()){
			                   for (int i = 0; i < OrderCursor.getCount(); i++){
			                  	 
			                  	 				
			                  	            	 HashMap<String,String> map = new HashMap<String, String>();
			                  	            	 
			                  	            	 String enteredDate_S = OrderCursor.getString(OrderCursor.getColumnIndex("ENTERED_DATE"));
			                  	            	 String startDate_S =  lblStartDate.getText().toString();
			                  	            	 String endDate_S = lblendDate.getText().toString();
			                  	            	 
			                  	            	Date enteredDate = null;
			                  	            	Date StartDate =null;
			                  	            	Date endDate = null;
			                  	            	 
			                  	            	try {
			                  	            		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			                  	            		enteredDate= (Date) sdf1.parse(enteredDate_S);
												} catch (ParseException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
			                  	            	
			                  	            	try {
													StartDate= (Date) dateformatMMDDYYYY.parse(startDate_S);
												} catch (ParseException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
			                  	            	
			                  	            	try {
													endDate= (Date) dateformatMMDDYYYY.parse(endDate_S);
												} catch (ParseException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}

			                  	            	 if(endDate.after(enteredDate) == true || endDate == enteredDate )        	            	
			                  	            	 {
			                  	            		if(enteredDate.after(StartDate) == true || StartDate == enteredDate )
			                  	            		{
			                  	            				{
			                  	            					map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
			                  	            					map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("CARD_TYPE")));
			                  	            					map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")));
			                  	            					map.put(columnTags[3],  OrderCursor.getString(OrderCursor.getColumnIndex("CARD_COUNT")));
			                  	            					String date_st = OrderCursor.getString(OrderCursor.getColumnIndex("ENTERED_DATE")).toString();
			                  	            					String mod_date = dateformatMMDDYYYY.format(OrderCursor.getString(OrderCursor.getColumnIndex("ENTERED_DATE")));
			                  	            					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			                  	            					Date mod_date_n = null;
			                  	            					try {
																	mod_date_n = df.parse(mod_date);
																} catch (ParseException e) {
																	e.printStackTrace();
																}
			                  	            					
			                  	            					map.put(columnTags[4],(mod_date));
			                  	            					listData.add(map);
			                  	            				}
			                  	            		}
			                  	            	 }

			                  	 OrderCursor.moveToNext();
			                     }//end of for
			              }
			      	 	OrderCursor.close();
			      	     db.close();
			      	     
			      	   SimpleAdapter adapter = new SimpleAdapter(MysalesDateWise.this, listData, R.layout.activity_mysales_table,columnTags , columnIds);
			      	   lv.setAdapter(adapter);   
			      	 }
					
					}
					
				}
				
				else {
					Erroralert("Message", "Please select start & end dates", R.drawable.icon);
				}
			}
		});
		
		
	}
	
	
	public boolean isOnline() {			// check network connection is available
		boolean connected = false;
		
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		
		    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || 
		            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
		        //we are connected to a network
		        connected = true;
		    }
		    else
		    {
		        connected = false;
		    }
		    return connected;
	}
	
	private String getMyPhoneNumber(){
		
		return Utils.getSimSerialNumber(this);
	 }
}
