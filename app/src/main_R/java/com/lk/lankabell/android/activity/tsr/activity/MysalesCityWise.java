package com.lk.lankabell.android.activity.tsr.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.net.NetworkInfo;
import java.util.HashMap;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;

public class MysalesCityWise extends Activity {

	private String city = null;
	private String cityValue;
	private Spinner citySpinner1;
	private DatabaseHandler dbh;

	private String startDate = null;
	private String endDate = null;
	private String startdatevalue;
	private String enddatevalue;
	DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
	TextView lblDateAndTime;
	Calendar myCalendar1 = Calendar.getInstance();
	Calendar myCalendar2 = Calendar.getInstance();
	TextView lblStartDate;
	TextView lblendDate;
	SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat dateformatMMDDYYYY1 = new SimpleDateFormat("yyyy/MM/dd");
	private static String SOAP_ACTION = "http://mainService/MySalesAllDetails";
	private static String NAMESPACE = "http://mainService";
	private static String METHOD_NAME = "MySalesAllDetails";
	//private static String URL = "http://10.12.12.181:8080/axis2/services/TSR_WEB_SERVICE?wsdl";
	private String URL = WsdlReader.getServiceUrl(true,this);
	// private static String URL =
	// "http://119.235.1.88:8080/axis2/services/TSR_WEB_SERVICE?wsdl";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void DataBindToItemTypeSpinner() {
		SQLiteDatabase DB = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		citySpinner1 = (Spinner) findViewById(R.id.citySpinner1);
		Cursor spnCursor = DB.rawQuery(
				"SELECT CITY FROM CITY_POSTAL_CODES ORDER BY CITY", null);
		int arrayLength = spnCursor.getCount() + 1;
		String[] array_spinner = new String[arrayLength];
		array_spinner[0] = "ALL";
		if (spnCursor.moveToFirst()) {
			for (int i = 1; i < arrayLength; i++) {
				array_spinner[i] = spnCursor.getString(spnCursor
						.getColumnIndex("CITY"));
				spnCursor.moveToNext();
			}
		}

		spnCursor.close();
		DB.close();
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_dropdown_item, array_spinner);

		citySpinner1.setAdapter(adapter);
	}

	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int month, int day) {
			myCalendar1.set(Calendar.YEAR, year);
			myCalendar1.set(Calendar.MONTH, month);
			myCalendar1.set(Calendar.DAY_OF_MONTH, day);
			updateLabel1();
		}
	};

	private void updateLabel1() {
		startDate = myCalendar1.getTime().toString();
		lblStartDate = (TextView) findViewById(R.id.lblSalesStartDate);
		lblStartDate.setText((dateformatMMDDYYYY.format(myCalendar1.getTime()))
				.toString());
	}

	DatePickerDialog.OnDateSetListener e = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			myCalendar2.set(Calendar.YEAR, year);
			myCalendar2.set(Calendar.MONTH, monthOfYear);
			myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel2();
		}
	};

	private void updateLabel2() {
		endDate = myCalendar2.getTime().toString();
		lblendDate = (TextView) findViewById(R.id.lblSalesEndDate);
		lblendDate.setText((dateformatMMDDYYYY.format(myCalendar2.getTime()))
				.toString());
	}

	private String getMyPhoneNumber() {
		
		return Utils.getSimSerialNumber(this);
	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysales_citywise);
		Button btnstartdate = (Button) findViewById(R.id.btnSalesStartdate);
		btnstartdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new DatePickerDialog(MysalesCityWise.this, d, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		Button btnenddate = (Button) findViewById(R.id.btnSalesEnddate);
		btnenddate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {new DatePickerDialog(MysalesCityWise.this, e, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		Button btnviewData = (Button) findViewById(R.id.btnViewSalesData);
		final ListView lv = (ListView) findViewById(R.id.lvCityitems);
		final ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
		final int[] columnIds = new int[] { R.id.txtViewListCity,R.id.txtViewListMerchant, R.id.txtViewListCard,R.id.txtViewListDenom, R.id.txtViewListQty };
		final String[] columnTags = new String[] { "city", "merchant", "card","denomination", "qty" };
		SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		final Spinner citySprinner = (Spinner) findViewById(R.id.citySpinner1);
		DataBindToItemTypeSpinner();
		Log.w("OnCreate", "No Issue");
		citySprinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> adapterView,View view, int i, long l) {
						DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
						// DataBindToItemTypeSpinner();
					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		btnviewData.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				lblStartDate = (TextView) findViewById(R.id.lblSalesStartDate);
				lblendDate = (TextView) findViewById(R.id.lblSalesEndDate);
				startdatevalue = lblStartDate.getText().toString();
				enddatevalue = lblendDate.getText().toString();
				if (startdatevalue != "" && enddatevalue != "") {
					SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW",
							MODE_PRIVATE, null);
					Cursor OrderCursor = db
							.rawQuery(
									"SELECT tsr.merchant_id AS merchant, sd.card_type AS card, "
											+ "sd.denomination AS denom, SUM (sd.no_of_cards) AS qty "
											+ "FROM sales_details sd JOIN sales_header tsr ON sd.invoice_id =tsr.invoice_id "
											+ "WHERE tsr.merchant_id IN (SELECT m.merchant_id "
											+ "FROM merchant_master m WHERE m.city IN ( "
											+ "SELECT c.city FROM city_postal_codes c "
											+ "WHERE c.city ='"+city+"')) and "
											+ "ENTERED_DATE between strftime('%m/%d/%Y','"+startdatevalue+"') "
											+ " and strftime('%m/%d/%Y','"+enddatevalue+"') "
											+ "GROUP BY tsr.merchant_id, sd.card_type, sd.denomination",
									null);
					String fromdate = lblStartDate.getText().toString();
					String mindate = dbh.GetminDate();
					String mindate1 = null;
					if (mindate != "") {
						mindate1 = dateformatMMDDYYYY.format(Date.parse(mindate));
					}
	
					if ((dateformatMMDDYYYY.format(Date.parse(fromdate)).compareTo(mindate1)) <= 0) {
						AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MysalesCityWise.this);
						dlgAlert.setMessage("Data is Insufficient. Do you want to load it from online ?");
						dlgAlert.setTitle("Alert");
						dlgAlert.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int which) {
										SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
										DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
										User user = dbh.getUserDetails();
										request.addProperty("strInputUserMobile",getMyPhoneNumber());
										request.addProperty("strInputUserName",user.getUserName());
										request.addProperty("strInputUserPassword",user.getPassword());
										request.addProperty("strFromDate",lblStartDate.getText().toString());
										request.addProperty("strToDate",lblendDate.getText().toString());
										Log.w("city : ", "city : "+ citySpinner1.getSelectedItem().toString());
										request.addProperty("city",citySpinner1.getSelectedItem().toString());
										SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
										envelope.setOutputSoapObject(request);
										envelope.dotNet = true;
										try {
											HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
											androidHttpTransport.call(SOAP_ACTION, envelope);
											SoapObject result = (SoapObject) envelope.bodyIn;
											if (result != null) {
												HashMap<String, String> hashmap = new HashMap<String, String>();
												hashmap.put(columnTags[0],"city");
												hashmap.put(columnTags[1],"merchant");
												hashmap.put(columnTags[2],"card");
												hashmap.put(columnTags[3],"denomination");
												hashmap.put(columnTags[4],"qty");
												listData.add(hashmap);
												String dbQuery5 = result.getProperty(0).toString();
												String Value5 = dbQuery5.replaceAll("\\[|\\]","");
												String[] statusarrayData5 = Value5.split("<");
												String[] firstValue = statusarrayData5[0].split(",");
												int statusvalue5 = Integer.parseInt(firstValue[0].toString());
												if (statusvalue5 == 1) {
													listData.clear();
													for (int i = 1; i < statusarrayData5.length; i++) {
														String realValues = statusarrayData5[i].replaceAll(">", "");
														String[] statusarray5 = realValues.split("#");
														HashMap<String, String> map = new HashMap<String, String>();
														map.put(columnTags[0],statusarray5[0].toString());
														map.put(columnTags[1],statusarray5[1].toString());
														map.put(columnTags[2],statusarray5[2].toString());
														map.put(columnTags[3],statusarray5[3].toString());
														map.put(columnTags[4],statusarray5[4].toString());
														listData.add(map);
													}
													SimpleAdapter adapter = new SimpleAdapter(MysalesCityWise.this,listData,R.layout.activity_mysales_citywise_list,columnTags,columnIds);
													ListView lvData = (ListView) findViewById(R.id.lvCityitems);
													lvData.setAdapter(adapter);
												}
											}

										} catch (Exception ex) {

										}
									}
								})

						.setNegativeButton("No",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int which) {
										Intent intent = new Intent(getApplicationContext(),MysalesCityWise.class);
										startActivity(intent);
									}
								});
						dlgAlert.setCancelable(true);
						dlgAlert.create().show();
					} else if ((dateformatMMDDYYYY.format(Date.parse(fromdate)).compareTo(mindate1)) > 0) {
						int a = OrderCursor.getCount();
						Log.d("curser ok", "Priyan");
						listData.clear();
						if (OrderCursor != null) {
							if (OrderCursor.moveToFirst()) {
								for (int i = 0; i < OrderCursor.getCount(); i++) {
									Log.d("aa", "Priyan");
									HashMap<String, String> map = new HashMap<String, String>();
									String enteredDate_S = OrderCursor.getString(OrderCursor.getColumnIndex("city"));
									String startDate_S = lblStartDate.getText().toString();
									String endDate_S = lblendDate.getText().toString();
									Date enteredDate = null;
									Date StartDate = null;
									Date endDate = null;
									try {
										SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
										enteredDate = (Date) sdf1.parse(enteredDate_S);
									} catch (ParseException e) {
										e.printStackTrace();
									}
									try {
										StartDate = (Date) dateformatMMDDYYYY.parse(startDate_S);
									} catch (ParseException e) {
										e.printStackTrace();
									}
									try {
										endDate = (Date) dateformatMMDDYYYY.parse(endDate_S);
									} catch (ParseException e) {
										e.printStackTrace();
									}
									if (endDate.after(enteredDate) == true|| endDate == enteredDate) {
										if (enteredDate.after(StartDate) == true|| StartDate == enteredDate) {
											if (listData.size() > 0) {
												for (int j = 0; j < listData.size(); j++) {
													int ss = listData.size();
													HashMap<String, String> map1 = new HashMap<String, String>();
													map1 = listData.get(j);

													
													if (map1.get("date").toString() == (OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")))&& (map1.get("cardtype")).toString() == (OrderCursor.getString(OrderCursor.getColumnIndex("CARD_TYPE")))&& (map1.get("denom")).toString() == (OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")))) {
														String cc = map1.get(columnTags[3]).toString();
														int totalCards = Integer.parseInt(OrderCursor.getString(OrderCursor.getColumnIndex("NO_OF_CARDS")))+ Integer.parseInt(map1.get(columnTags[3]).toString());
														listData.remove(map1);
														map.put(columnTags[0],OrderCursor.getString(OrderCursor.getColumnIndex("city")));
														map.put(columnTags[1],OrderCursor.getString(OrderCursor.getColumnIndex("merchant")));
														map.put(columnTags[2],OrderCursor.getString(OrderCursor.getColumnIndex("card")));
														map.put(columnTags[3],OrderCursor.getString(OrderCursor.getColumnIndex("denomination")));
														map.put(columnTags[4],OrderCursor.getString(OrderCursor.getColumnIndex("qty")));
														listData.add(map);
													} else {
														map.put(columnTags[0],OrderCursor.getString(OrderCursor.getColumnIndex("city")));
														map.put(columnTags[1],OrderCursor.getString(OrderCursor.getColumnIndex("merchant")));
														map.put(columnTags[2],OrderCursor.getString(OrderCursor.getColumnIndex("card")));
														map.put(columnTags[3],OrderCursor.getString(OrderCursor.getColumnIndex("denomination")));
														map.put(columnTags[4],OrderCursor.getString(OrderCursor.getColumnIndex("qty")));
														listData.add(map);
													}
												}
											} else {
												map.put(columnTags[0],OrderCursor.getString(OrderCursor.getColumnIndex("city")));
												map.put(columnTags[1],OrderCursor.getString(OrderCursor.getColumnIndex("merchant")));
												map.put(columnTags[2],OrderCursor.getString(OrderCursor.getColumnIndex("card")));
												map.put(columnTags[3],OrderCursor.getString(OrderCursor.getColumnIndex("denomination")));
												map.put(columnTags[4],OrderCursor.getString(OrderCursor.getColumnIndex("qty")));
												listData.add(map);
											}//city merchant card  denomination qty
										}
									}
									OrderCursor.moveToNext();
								}
							}
							OrderCursor.close();
							db.close();
							SimpleAdapter adapter = new SimpleAdapter(
									MysalesCityWise.this, listData,
									R.layout.activity_mysales_citywise_list,
									columnTags, columnIds);
							lv.setAdapter(adapter);
						}
					}

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

}
