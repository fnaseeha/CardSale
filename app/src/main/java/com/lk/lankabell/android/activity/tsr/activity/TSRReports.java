package com.lk.lankabell.android.activity.tsr.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.google.gson.Gson;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.DenomWiseDetails;
import com.lk.lankabell.android.activity.tsr.sqlite.SalesReport;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;
import com.lk.lankabell.android.activity.tsr.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class TSRReports extends Activity {

	private static String SOAP_ACTION = "http://mainService/MySalesAllDetails";
	private static String METHOD_NAME = "MySalesAllDetails";

	private static String SOAP_ACTION2 = "http://mainService/MerchantWiseDetails";
	private static String METHOD_NAME2 = "MerchantWiseDetails";

	private static String SOAP_ACTION3 = "http://mainService/DateWiseDetailsspinner3";
	private static String METHOD_NAME3 = "DateWiseDetails";

	private static String SOAP_ACTION4 = "http://mainService/DenomWiseDetails";
	private static String METHOD_NAME4 = "DenomWiseDetails";

	private static String NAMESPACE = "http://mainService";

	private String URL = WsdlReader.getServiceUrl(true,this);
	private Spinner selectWiseSpinner, selectDateRangeSprinner, selectDetailSummarySprinner;
	private TextView reportheaders;
	private Button btnSubmit;
	private ListView listView;
	private DatabaseHandler dbh;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tsrreports);
		URL = WsdlReader.getServiceUrl(true,this);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setText("My Sales Reports");
		}
		dbh = new DatabaseHandler(getApplicationContext());
		listView = (ListView) findViewById(R.id.tsrreportlistView);

		addItemsOnSpinner1();
		addItemsOnSpinner2();
		addItemsOnSpinner3(false);
		addListenerOnButton();
		addListenerOnSpinnerItemSelection();

		dbh.getAllSalesData();

	}

	private void addListenerOnSpinnerItemSelection() {
		selectWiseSpinner = (Spinner) findViewById(R.id.spinner1);
		selectDateRangeSprinner = (Spinner) findViewById(R.id.spinner2);
		selectWiseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(android.widget.AdapterView<?> parent, View view, int pos, long id) {

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
	}

	String epfNo = "";
	String detOrsum = "";
	Date fromDate = new Date();
	Date toDate = new Date();
	private ArrayList<String> period_list;

	private void addListenerOnButton() {

		selectWiseSpinner = (Spinner) findViewById(R.id.spinner1);
		selectDateRangeSprinner = (Spinner) findViewById(R.id.spinner2);
		selectDetailSummarySprinner = (Spinner) findViewById(R.id.spinner3);
		btnSubmit = (Button) findViewById(R.id.button1);
		reportheaders = (TextView) findViewById(R.id.entered_date);

		btnSubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				long dateVal = 0;
				long toDateVal = 0;
				if (selectWiseSpinner.getSelectedItem().equals("Merchant Wise")) {
					viewMerchantWiseReports();
				} else if (selectWiseSpinner.getSelectedItem().equals("Date Wise")) {
					viewDateWiseReports();
				} else if (selectWiseSpinner.getSelectedItem().equals("City Wise")) {
					viewCityWiseReports();
				}
			}
		});

		selectDateRangeSprinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (period_list.get(arg2).equals("This Month")) {
					addItemsOnSpinner3(true);
				} else {
					addItemsOnSpinner3(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private String getMyPhoneNumber() {
		return Utils.getSimSerialNumber(this);
	}

	private void addItemsOnSpinner3(boolean showDeno) {
		selectDetailSummarySprinner = (Spinner) findViewById(R.id.spinner3);
		List<String> list = new ArrayList<String>();
		if (!showDeno) {
			list.add("Detail");
		}
		list.add("Summary");
		if (showDeno) {
			list.add("Denom Summ");
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectDetailSummarySprinner.setAdapter(dataAdapter);
	}

	private void addItemsOnSpinner2() {
		selectDateRangeSprinner = (Spinner) findViewById(R.id.spinner2);
		period_list = new ArrayList<String>();
		period_list.add("Today");
		period_list.add("Yesterday");
		period_list.add("This Week");
		period_list.add("Last Week");
		period_list.add("This Month");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, period_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectDateRangeSprinner.setAdapter(dataAdapter);
	}

	private void addItemsOnSpinner1() {
		selectWiseSpinner = (Spinner) findViewById(R.id.spinner1);
		List<String> list = new ArrayList<String>();
		list.add("Merchant Wise");
		list.add("Date Wise");
		list.add("City Wise");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectWiseSpinner.setAdapter(dataAdapter);

	}

	private void viewMerchantWiseReports() {
		long dateVal;
		long toDateVal;
		reportheaders.setText("Merchant");
		TSRMerchantWiseReportAdapter adapter = null;
		listView.setAdapter(null);
		toDate = new Date();
		if (selectDateRangeSprinner.getSelectedItem().equals("Today")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 1;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getMerchantWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRMerchantWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}

			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 1;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getMerchantWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRMerchantWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}

		} else if (selectDateRangeSprinner.getSelectedItem().equals("Yesterday")) {
			
			fromDate.setTime(getStartEndTimes(-1, true));
			toDate.setTime(getStartEndTimes(-1, false));
			
			
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 1;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
			 
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getMerchantWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRMerchantWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}

			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
 
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getMerchantWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRMerchantWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("This Week")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getMerchantWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRMerchantWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}

			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getMerchantWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRMerchantWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}

		} else if (selectDateRangeSprinner.getSelectedItem().equals("Last Week")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 14;
				toDateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				toDate.setTime(new Date().getTime() - (toDateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getMerchantWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRMerchantWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 14;
				toDateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				toDate.setTime(new Date().getTime() - (toDateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getMerchantWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRMerchantWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("This Month")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				Toast toast = Toast.makeText(TSRReports.this, "Summary report only avaiable ! ", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				dateVal = 30;
				toDate = new Date();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				// ++++++++++++++++++++++++++++++++MERCHANT
				// WISE+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				String fromDateString = "";
				String toDateString = "";
				SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
				try {
					fromDateString = sd.format(fromDate);
					toDateString = sd.format(toDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
				DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
				User user = dbh.getUserDetails();
				request.addProperty("strInputUserMobile", getMyPhoneNumber());
				request.addProperty("strInputUserName", user.getUserName());
				request.addProperty("strInputUserPassword", user.getPassword());
				request.addProperty("strFromDate", fromDateString);
				request.addProperty("strToDate", toDateString);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
				envelope.dotNet = true;
				try {
					HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION2, envelope);
					SoapObject result = (SoapObject) envelope.bodyIn;
					if (result != null) {
						ArrayList<SalesReport> salesReportList = new ArrayList<SalesReport>();
						String dbQuery5 = result.getProperty(0).toString();
						String Value5 = dbQuery5.replaceAll("\\[|\\]", "");
						String[] statusarrayData5 = Value5.split("<");
						String[] firstValue = statusarrayData5[0].split(",");
						Long statusvalue5 = Long.parseLong(firstValue[0].toString());
						if (statusvalue5 == 1) {
							salesReportList.clear();
							for (int i = 1; i < statusarrayData5.length; i++) {
								String realValues = statusarrayData5[i].replaceAll(">", "");
								String[] statusarray5 = realValues.split("#");
								SalesReport salesReportN = new SalesReport();
								salesReportN.setMerchantId(Long.valueOf(statusarray5[0].toString().replace(",", "").trim()));
								salesReportN.setCardType(statusarray5[1].toString().replace(",", ""));
								salesReportN.setDenom(Double.valueOf(statusarray5[2].toString().replace(",", "")));
								salesReportN.setQty(Integer.valueOf(statusarray5[3].toString().replace(",", "").trim()));
								salesReportN.setAmount(Double.valueOf(statusarray5[4].toString().replace(",", "")));
								salesReportList.add(salesReportN);
							}
							adapter = new TSRMerchantWiseReportAdapter(TSRReports.this, salesReportList);
							listView.setAdapter(adapter);

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Utils.ShowMsg(this, "Error occured while loading the report");
					Utils.writeToErrLogFileWithTime(e);
				}
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			}
		}
	}

	private void viewDateWiseReports() {
		long dateVal;
		long toDateVal;
		reportheaders.setText("Date");
		TSRDateWiseReportAdapter adapter = null;
		listView.setAdapter(null);
		toDate = new Date();
		if (selectDateRangeSprinner.getSelectedItem().equals("Today")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 1;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getDateWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRDateWiseReportAdapter(TSRReports.this, salesReport);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);

			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 1;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getDateWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRDateWiseReportAdapter(TSRReports.this, salesReport);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);

			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("Yesterday")) {
			fromDate.setTime(getStartEndTimes(-1, true));
			toDate.setTime(getStartEndTimes(-1, false));
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
//				fromDate.setTime(toDate.getTime() - (1 * 24 * 60 * 60 * 1000));
//				toDate.setTime(new Date().getTime() - (2 * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getDateWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRDateWiseReportAdapter(TSRReports.this, salesReport);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);

			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
//				fromDate.setTime(toDate.getTime() - (1 * 24 * 60 * 60 * 1000));
//				toDate.setTime(new Date().getTime() - (2 * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getDateWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRDateWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("This Week")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getDateWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRDateWiseReportAdapter(TSRReports.this, salesReport);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);
				
			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getDateWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRDateWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);
				
			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("Last Week")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 14;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				toDateVal = 7;
				toDate.setTime(new Date().getTime() - (toDateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getDateWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRDateWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 14;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				toDateVal = 7;
				toDate.setTime(new Date().getTime() - (toDateVal * 24 * 60 * 60 * 1000));
				if (adapter != null) {
					salesReport.clear();
					adapter.notifyDataSetChanged();
				}
				salesReport = dbh.getDateWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRDateWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("This Month")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				Toast toast = Toast.makeText(TSRReports.this, "Summary report only avaiable ! ", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				dateVal = 30;
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				// ++++++++++++++++++++++++++++++++DATE
				// WISE+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				String fromDateString = "";
				String toDateString = "";
				SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
				try {
					fromDateString = sd.format(fromDate);
					toDateString = sd.format(toDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
				DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
				User user = dbh.getUserDetails();
				request.addProperty("strInputUserMobile", getMyPhoneNumber());
				request.addProperty("strInputUserName", user.getUserName());
				request.addProperty("strInputUserPassword", user.getPassword());
				request.addProperty("strFromDate", fromDateString);
				request.addProperty("strToDate", toDateString);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
				envelope.dotNet = true;
				try {
					HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION3, envelope);
					SoapObject result = (SoapObject) envelope.bodyIn;
					if (result != null) {
						ArrayList<SalesReport> salesReportList = new ArrayList<SalesReport>();
						String dbQuery5 = result.getProperty(0).toString();
						String Value5 = dbQuery5.replaceAll("\\[|\\]", "");
						String[] statusarrayData5 = Value5.split("<");
						String[] firstValue = statusarrayData5[0].split(",");
						
						int statusvalue5 = 0;
						try {
							statusvalue5 = Integer.parseInt(firstValue[0].toString());
						} catch (Exception e) {
							 
						}
						if (statusvalue5 == 1) {
							salesReportList.clear();
							for (int i = 1; i < statusarrayData5.length; i++) {
								String realValues = statusarrayData5[i].replaceAll(">", "");
								String[] statusarray5 = realValues.split("#");
								SalesReport salesReportN = new SalesReport();
								salesReportN.setDate(statusarray5[0].toString().replace(",", "").trim());
								salesReportN.setCardType(statusarray5[1].toString().replace(",", ""));
								salesReportN.setDenom(Double.valueOf(statusarray5[2].toString().replace(",", "").trim()));
								salesReportN.setQty(Integer.valueOf(statusarray5[3].toString().replace(",", "").trim()));
								salesReportN.setAmount(Double.valueOf(statusarray5[4].toString().replace(",", "")));
								salesReportList.add(salesReportN);
							}
							adapter = new TSRDateWiseReportAdapter(TSRReports.this, salesReportList);
							listView.setAdapter(adapter);

						}
					}
					
					Utils.ShowMsg(this, "Report Loaded");
				} catch (Exception e) {
					e.printStackTrace();
					Utils.ShowMsg(this, "Error occured while loading the report");
					Utils.writeToErrLogFileWithTime(e);
				}
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Denom Summ")) {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);
				DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
				User user = dbh.getUserDetails();
				request.addProperty("strInputUserMobile", getMyPhoneNumber());
				request.addProperty("strInputUserName", user.getUserName());
				request.addProperty("strInputUserPassword", user.getPassword());
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
				envelope.dotNet = true;
				try {
					HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION4, envelope);
					SoapObject result = (SoapObject) envelope.bodyIn;
					if (result != null) {
						ArrayList<SalesReport> salesReportList = new ArrayList<SalesReport>();
						Gson gsonResponse = new Gson();
						DenomWiseDetails[] denomWiseDetailsList = gsonResponse.fromJson(result.getProperty(0).toString(), DenomWiseDetails[].class);
						Log.w("Inventory Response", denomWiseDetailsList.toString());
						salesReportList.clear();
						for (DenomWiseDetails denomWiseDetails : denomWiseDetailsList) {
							SalesReport salesReportN = new SalesReport();
							if (denomWiseDetails.getInvoiceDate() != null && !denomWiseDetails.getInvoiceDate().equals("")) {

								SimpleDateFormat formatter = null;
								formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String dateString = denomWiseDetails.getInvoiceDate();
								Date date1 = formatter.parse(dateString);
								DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
								String output = dateFormat.format(date1);

								salesReportN.setDate(output);
							} else {
								salesReportN.setDate("Grand Total");
							}
							if (denomWiseDetails.getDenomination() != null && !denomWiseDetails.getDenomination().equals("")) {
								salesReportN.setDenom(Double.valueOf(denomWiseDetails.getDenomination()));
							} else {
								salesReportN.setDenom(0.0);
							}
							if (denomWiseDetails.getAmount() != null && !denomWiseDetails.getAmount().equals("")) {
								salesReportN.setAmount(Double.valueOf(denomWiseDetails.getAmount()));
							} else {
								salesReportN.setAmount(0.0);
							}

							salesReportList.add(salesReportN);
						}
						TSRDenomWiseReportAdapter denomAdapter = new TSRDenomWiseReportAdapter(TSRReports.this, salesReportList);
						listView.setAdapter(denomAdapter);
					}
				} catch (Exception e) {
					Utils.ShowMsg(this, "Error occured while loading the report");
					Utils.writeToErrLogFileWithTime(e);
				}
			}
			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		}
	}

	private void viewCityWiseReports() {
		long dateVal;
		long toDateVal;
		reportheaders.setText("City");
		TSRCityWiseReportAdapter adapter;
		listView.setAdapter(null);
		toDate = new Date();
		if (selectDateRangeSprinner.getSelectedItem().equals("Today")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 1;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				salesReport = dbh.getCityWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRCityWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}

			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 1;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				salesReport = dbh.getCityWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRCityWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("Yesterday")) {
			fromDate.setTime(getStartEndTimes(-1, true));
			toDate.setTime(getStartEndTimes(-1, false));
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
//				fromDate.setTime(toDate.getTime() - (1 * 24 * 60 * 60 * 1000));
//				toDate.setTime(new Date().getTime() - (2 * 24 * 60 * 60 * 1000));
				salesReport = dbh.getCityWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRCityWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}

			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
//				fromDate.setTime(toDate.getTime() - (1 * 24 * 60 * 60 * 1000));
//				toDate.setTime(new Date().getTime() - (2 * 24 * 60 * 60 * 1000));
				salesReport = dbh.getCityWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRCityWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("This Week")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				salesReport = dbh.getCityWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRCityWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);
				
			}
			
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 7;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				salesReport = dbh.getCityWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRCityWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);
				
			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("Last Week")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 14;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				toDateVal = 7;
				toDate.setTime(new Date().getTime() - (toDateVal * 24 * 60 * 60 * 1000));
				salesReport = dbh.getCityWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRCityWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				ArrayList<SalesReport> salesReport = new ArrayList<SalesReport>();
				dateVal = 14;
				detOrsum = selectDetailSummarySprinner.getSelectedItem().toString();
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				toDateVal = 7;
				toDate.setTime(new Date().getTime() - (toDateVal * 24 * 60 * 60 * 1000));
				salesReport = dbh.getCityWiseReport(epfNo, detOrsum, fromDate, toDate);
				adapter = new TSRCityWiseReportAdapter(TSRReports.this, salesReport);
				listView.setAdapter(adapter);

			}
		} else if (selectDateRangeSprinner.getSelectedItem().equals("This Month")) {
			if (selectDetailSummarySprinner.getSelectedItem().equals("Detail")) {
				Toast toast = Toast.makeText(TSRReports.this, "Summary report only avaiable ! ", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
			if (selectDetailSummarySprinner.getSelectedItem().equals("Summary")) {
				dateVal = 30;
				fromDate.setTime(toDate.getTime() - (dateVal * 24 * 60 * 60 * 1000));
				// ++++++++++++++++++++++++++++++++CITY
				// WISE+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				String fromDateString = "";
				String toDateString = "";
				SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
				try {
					fromDateString = sd.format(fromDate);
					toDateString = sd.format(toDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
				User user = dbh.getUserDetails();
				request.addProperty("strInputUserMobile", getMyPhoneNumber());
				request.addProperty("strInputUserName", user.getUserName());
				request.addProperty("strInputUserPassword", user.getPassword());
				request.addProperty("strFromDate", fromDateString);
				request.addProperty("strToDate", toDateString);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
				envelope.dotNet = true;
				try {
					HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION, envelope);
					SoapObject result = (SoapObject) envelope.bodyIn;
					if (result != null) {
						ArrayList<SalesReport> salesReportList = new ArrayList<SalesReport>();
						String dbQuery5 = result.getProperty(0).toString();
						String Value5 = dbQuery5.replaceAll("\\[|\\]", "");
						String[] statusarrayData5 = Value5.split("<");
						String[] firstValue = statusarrayData5[0].split(",");
						int statusvalue5 = 0;
						try {
							statusvalue5 = Integer.parseInt(firstValue[0].toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (statusvalue5 == 1) {
							salesReportList.clear();
							for (int i = 1; i < statusarrayData5.length; i++) {
								String realValues = statusarrayData5[i].replaceAll(">", "");
								String[] statusarray5 = realValues.split("#");
								SalesReport salesReportN = new SalesReport();
								salesReportN.setCity(statusarray5[0].toString().replace(",", "").trim());
								salesReportN.setCardType(statusarray5[1].toString().replace(",", ""));
								salesReportN.setDenom(Double.valueOf(statusarray5[2].toString().replace(",", "")));
								salesReportN.setQty(Integer.valueOf(statusarray5[3].toString().replace(",", "").trim()));
								salesReportN.setAmount(Double.valueOf(statusarray5[4].toString().replace(",", "")));
								salesReportList.add(salesReportN);
							}
							adapter = new TSRCityWiseReportAdapter(TSRReports.this, salesReportList);
							listView.setAdapter(adapter);

						}
					}
					Utils.ShowMsg(this, "Report Loaded");
				} catch (Exception e) {
					e.printStackTrace();
					Utils.ShowMsg(this, "Error occured while loading the report");
					Utils.writeToErrLogFileWithTime(e);
				}
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			}
		}
	}
	
	public long getStartEndTimes(int days,boolean isStart){
		long time = 0;
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis((new Date().getTime()));
		Log.v("times 1", "" + cal.getTimeInMillis());
		cal.add(Calendar.DAY_OF_YEAR, days);
		Log.v("times 2", "" + cal.getTimeInMillis());
		if(isStart){
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 1);
		}else{			
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		  
		time = cal.getTimeInMillis();
		
		
		return time;
	}
	
	
	

}
