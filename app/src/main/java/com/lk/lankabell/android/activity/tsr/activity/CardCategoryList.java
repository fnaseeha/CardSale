package com.lk.lankabell.android.activity.tsr.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.ItemSale;
import com.lk.lankabell.android.activity.tsr.sqlite.ItemSalesDetails;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.R;

/**
 * This class is used to Perform Card Sales functionalities.
 * 
 */
@SuppressLint("SimpleDateFormat")
@SuppressWarnings("deprecation")
public class CardCategoryList extends Activity {

	ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> listHeader = new ArrayList<HashMap<String, String>>();
	int[] columnIds = new int[] { R.id.lst_Row_CardType, R.id.lst_Row_Quantity, R.id.lst_Row_Denomination, R.id.lst_Row_Total, R.id.lst_Row_Discount_Percentage, R.id.btnDelete };
	int[] header_columnIds = new int[] { R.id.lst_Header_Cardtype, R.id.lst_Header_Quantity, R.id.lst_Header_Denom, R.id.lst_Header_Total, R.id.header_column6 };

	String[] columnTags = new String[] { "type", "denomination", "bulkId", "qty", "lineAmount", "startSerial", "endSerial", "lineDiscount", "discountRate", "listStartSerial" };

	private DatabaseHandler dbh;
	private TextView tv;
	private String londitudeValue;
	private String latitudeValue;
	private String city;
	private TextView lblUserName;
	private EditText etnoofcards;
	private ListView lvData;
	private ListView lvHeader;
	private Button btnSave;
	private String merchant;
	private String merchantId;
	private String startSerialInt;

	private TextView txtcardtype, txtnocards, txtdenom, txtcardtotal;

	int flag = 1;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardsale_listview);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		merchant = getIntent().getExtras().getString("merchantName");
		merchantId = getIntent().getExtras().getString("merchantID");
		// startSerialInt = getIntent().getExtras().getString("startSerial");

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (flag == 1) {
					btnSave.setEnabled(false);
					Log.d("Button", "Disabled the button.");
				}

				if (listData.isEmpty()) {
					AlertDialog.Builder dlgAlert1 = new AlertDialog.Builder(CardCategoryList.this);
					dlgAlert1.setMessage("Invalid Action");
					dlgAlert1.setTitle("Alert");
					dlgAlert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					dlgAlert1.setCancelable(true);
					dlgAlert1.create().show();
				} else {
					onCardSales();
				}
			}
		});

		HashMap<String, String> mappackage = new HashMap<String, String>(); // add
																			// header

		mappackage.put(columnTags[0], "Type");
		mappackage.put(columnTags[1], "Denom.");
		mappackage.put(columnTags[2], "Qty");
		mappackage.put(columnTags[3], "Total");
		mappackage.put(columnTags[4], "DIsc(%)");

		SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Cursor OrderCursor = db.query("SALES_TEMP_DETAILS", new String[] { "CARD_TYPE", "DENOMINATION", "NO_OF_CARDS", "LINE_AMOUNT", "DISCOUNT_RATE" }, null, null, null, null, null);
		listData.clear();
		if (OrderCursor != null) {

			if (OrderCursor.moveToFirst()) {
				for (int i = 0; i < OrderCursor.getCount(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("CARD_TYPE")));
					map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")));
					map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("NO_OF_CARDS")));
					map.put(columnTags[3], OrderCursor.getString(OrderCursor.getColumnIndex("LINE_AMOUNT")));
					map.put(columnTags[4], OrderCursor.getString(OrderCursor.getColumnIndex("DISCOUNT_RATE")));
					listData.add(map);

					OrderCursor.moveToNext();

				}// end of for
			}
			OrderCursor.close();
			db.close();

			SimpleAdapter adapter = new SimpleAdapter(CardCategoryList.this, listData, R.layout.row_layout_white, columnTags, columnIds);
			lvData = (ListView) findViewById(R.id.lv_Cardsale_Data);
			lvData.setAdapter(adapter);

			DecimalFormat fmt = new DecimalFormat("0.00");

			TextView txttotal = (TextView) findViewById(R.id.txttotalcard);

			dbh = new DatabaseHandler(getApplicationContext());

			String data = dbh.GetCardTotal();
			double data_value = 0;

			if (data == "" || data == null) {
				data_value = 0;
			} else {
				data_value = Double.parseDouble(data);
			}

			txttotal.setText(" Grand Total " + " " + ":" + "  " + "Rs" + " " + fmt.format(data_value) + " ");
			txttotal.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		}
	}

	/**
	 * @param cardType
	 * @param denomination
	 * @param qty
	 * @param lineAmount
	 * @param discountRate
	 * 
	 *            Adding the package to the listview.
	 */
	public void addPackagetoList(String cardType, String denomination, String qty, String lineAmount, String discountRate) {
		HashMap<String, String> mappackage_Data = new HashMap<String, String>();

		mappackage_Data.put(columnTags[0], cardType); // add details
		mappackage_Data.put(columnTags[1], denomination);
		mappackage_Data.put(columnTags[3], qty);
		mappackage_Data.put(columnTags[4], lineAmount);
		mappackage_Data.put(columnTags[2], discountRate);
		// mappackage_Data.put("cardType", cardType);

		listData.add(mappackage_Data);
		lvData = (ListView) findViewById(R.id.lv_Cardsale_Data);
		lvData.setAdapter(setDataAdapter(listData, columnIds, columnTags));
	}

	public SimpleAdapter setAdapter(ArrayList<HashMap<String, String>> listData, int[] columnIds, String[] columnTags) {
		SimpleAdapter adapter = new SimpleAdapter(this, listData, R.layout.cardsale_header, columnTags, columnIds);
		return adapter;
	}

	public SimpleAdapter setDataAdapter(ArrayList<HashMap<String, String>> listData, int[] columnIds, String[] columnTags) {
		SimpleAdapter adapter = new SimpleAdapter(this, listData, R.layout.row_layout_white, columnTags, columnIds);
		return adapter;
	}

	// Add function for card Sales.

	/**
	 * @param view
	 *            Adding the card sale details
	 */
	public void OnclickAdd(View view) {

		Intent intent = new Intent(getApplicationContext(), CardSaleActivity.class);
		intent.putExtra("merchantName", merchant);
		intent.putExtra("merchantID", merchantId);
		intent.putExtra("city", city);
		startActivity(intent);
		finish();
	}

	// Cancellation function for card sales.

	/**
	 * @param view
	 *            Canceling the card sales
	 */
	public void OnclickCancel(View view) {
		Intent intent = new Intent(getApplicationContext(), home.class);
		intent.putExtra("merchantName", merchant);
		intent.putExtra("merchantID", merchantId);
		intent.putExtra("city", city);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		// outState.putString("TEXT", (String)text.getText());
		for (int i = 0; i < listData.size(); i++) {
			HashMap<String, String> listdatavalues = new HashMap<String, String>();
			listdatavalues = listData.get(0);
		}

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		// text.setText(savedInstanceState.getString("TEXT"));
	}

	/**
	 * @param invoiceId
	 * @return id Generating Item sales ID for card sales.
	 */
	public int GenerateItemSalesId(int invoiceId) {

		dbh = new DatabaseHandler(getApplicationContext());
		int id;
		int maxId = dbh.GetmaxItemSalesId(invoiceId);
		id = maxId + 1;
		return id;
	}

	/**
	 * @author Jei
	 * 
	 *         Geo locations and coordinates gathering from Mobile GPS and
	 *         Performing card sales.
	 */
	public void onCardSales() {
		// startSerialInt = getIntent().getExtras().getString("startSerial");
		// TODO Auto-generated method stub
		SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Cursor OrderCursor = db.query("SALES_TEMP_DETAILS", new String[] { "CARD_TYPE", "DENOMINATION", "BULK_NO", "NO_OF_CARDS", "LINE_AMOUNT", "START_SERIAL", "END_SERIAL", "LINE_DISCOUNT", "listStartSerial" }, null, null, null, null, null);

		listData.clear();
		if (OrderCursor != null) {

			if (OrderCursor.moveToFirst()) {
				for (int i = 0; i < OrderCursor.getCount(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("CARD_TYPE")));
					map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("DENOMINATION")));
					map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("BULK_NO")));
					map.put(columnTags[3], OrderCursor.getString(OrderCursor.getColumnIndex("NO_OF_CARDS")));
					map.put(columnTags[4], OrderCursor.getString(OrderCursor.getColumnIndex("LINE_AMOUNT")));
					map.put(columnTags[5], OrderCursor.getString(OrderCursor.getColumnIndex("START_SERIAL")));
					map.put(columnTags[6], OrderCursor.getString(OrderCursor.getColumnIndex("END_SERIAL")));
					map.put(columnTags[7], OrderCursor.getString(OrderCursor.getColumnIndex("LINE_DISCOUNT")));
					map.put(columnTags[9], OrderCursor.getString(OrderCursor.getColumnIndex("listStartSerial")));
					listData.add(map);

					OrderCursor.moveToNext();
				}// end of for
			}
			OrderCursor.close();
			db.close();

			double total = 0;
			double discountAmount = 0;

			for (int a = 0; a < listData.size(); a++) {
				HashMap<String, String> selectedFromList = (HashMap<String, String>) listData.get(a);
				total = total + Double.parseDouble(selectedFromList.get("lineAmount"));
				discountAmount = discountAmount + Double.parseDouble(selectedFromList.get("lineDiscount"));

			}

			DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
			int salesid = dbh.GetmaxSalesId();
			User user = dbh.getUserDetails();
			// if(salesid == 1) {
			// dbh.SaveLastInvoiceNo( (salesid+1) );
			// }

			String epfNo = "" + user.getId();
			Log.d("New Invoice ID id the : ", salesid + " ********* " + epfNo);
			String year = new SimpleDateFormat("yy").format(new Date());
			String FormatEpfNo = StringUtils.leftPad(epfNo, 7, "0");
			Log.d("Epf Number is the : ", FormatEpfNo);
			String idInvoice = "";
			StringBuilder builder = new StringBuilder(String.valueOf(salesid));
			builder.append(FormatEpfNo);
			// builder.append(year);
			idInvoice = builder.toString();
			long longInvoiceId = Long.valueOf(idInvoice);
			
			longInvoiceId = dbh.getValiedInvoiceNo(longInvoiceId);
			
			if(idInvoice.length() < 11 || dbh.isInvoiceNumberExists(longInvoiceId)){
				AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle("Error on invoice number")
				.setMessage(String.format("Incorrect invoice number or duplicate invoice number. Please contact TSR support service now. \nInvoice No : %s", idInvoice))
				.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				})
				.create();
				alert.show();
				return;
			}
			
			

			long merchantId = Long.parseLong(getIntent().getExtras().getString("merchantID"));
			Log.d("New mearchat ID id the : ", "********************************* ********* " + merchantId);
			londitudeValue = "0";
			latitudeValue = "0";

			ItemSale itemSale = new ItemSale(longInvoiceId, merchantId, londitudeValue, latitudeValue, (listData.size() - 1), total, true, null);
			dbh.SaveItemSale(itemSale, Integer.toString(user.getId()), discountAmount);
			dbh.UpdateLastInvoiceNo(salesid + 1);

			int a = 0;
			double Totall = 0.0;
			for (a = 0; a < listData.size(); a++) {
				HashMap<String, String> selectedFromList = (HashMap<String, String>) listData.get(a); // id
																										// ,total
																										// line
																										// discount

				String itemId = selectedFromList.get("type");
				String denomination = selectedFromList.get("denomination");
				String bulkId = selectedFromList.get("bulkId");
				String noOfItems = selectedFromList.get("qty");
				String lineAmount = selectedFromList.get("lineAmount");
				String modifiedstartSerial = ("0000000" + selectedFromList.get("startSerial")).substring(selectedFromList.get("startSerial").length());
				String startSerial = bulkId.concat(modifiedstartSerial);
				String lineDiscount = selectedFromList.get("lineDiscount");
				Totall = Totall + Double.parseDouble(lineAmount);
				ItemSalesDetails ItemSalesDetails = new ItemSalesDetails(longInvoiceId, itemId, Integer.parseInt(denomination), bulkId, Integer.parseInt(noOfItems), Double.parseDouble(lineAmount), startSerial, a + 1, Double.parseDouble(lineDiscount));
				dbh.SaveItemSalesDetails(ItemSalesDetails);
			}

			if ((a == listData.size()) && (Totall == total)) {
				dbh.UpdateSalesHeaderTable(longInvoiceId, 1);
				Date lastModifiedDate = new Date();
				try {
					String dateString = dbh.getServerDate();
					SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
					lastModifiedDate = formatter.parse(dateString);
				} catch (Exception e) {
					Log.w("date parse error", e.getMessage());
				}
				dbh.SaveServerDate(new Date().toString());// New Date()
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
				String dateValue = sdf1.format(System.currentTimeMillis());

				for (a = 0; a < listData.size(); a++) {
					HashMap<String, String> selectedFromList = (HashMap<String, String>) listData.get(a); // id
																											// ,total
																											// line
																											// discount

					String itemId = selectedFromList.get("type");
					String denomination = selectedFromList.get("denomination");
					String bulkId = selectedFromList.get("bulkId");
					String noOfItems = selectedFromList.get("qty");
					String lineAmount = selectedFromList.get("lineAmount");
					String modifiedstartSerial = ("0000000" + selectedFromList.get("startSerial")).substring(selectedFromList.get("startSerial").length());
					String startSerial = bulkId.concat(modifiedstartSerial);
					String lineDiscount = selectedFromList.get("lineDiscount");
					String listStartSerial = selectedFromList.get("listStartSerial");

					if (dbh.CheckMerchantIdAndCardTypeAndDenominationIsExistsByMerchantInventoryTable(merchantId, Integer.parseInt(denomination), itemId)) {
						int qty = dbh.GetQuantityBycardTypeAndDenominationFromMerchantInventory(itemId, merchantId, Integer.parseInt(denomination)) + Integer.parseInt(noOfItems);
						int salesQty = dbh.GetSalesQtyBycardTypeAndDenominationFromMerchantInventory(itemId, merchantId, Integer.parseInt(denomination)) + Integer.parseInt(noOfItems);
						dbh.UpdateMerchantInventoryQty(merchantId, itemId, Integer.parseInt(denomination), qty, dateValue, salesQty);
					} else {
						int reorderLevel = (int) (Integer.parseInt(noOfItems) * 0.5);
						if (reorderLevel == 0) {
							reorderLevel = 1;
						}
						dbh.SaveMerchantInventoryTableAtSale(merchantId, itemId, Integer.parseInt(denomination), Integer.parseInt(noOfItems), dateValue, reorderLevel);
					}
					int endSerial = 0;
					if (dbh.CheckEndSerialAlreadyExistInTempSales(user.getId(), itemId, Integer.parseInt(denomination), bulkId, listStartSerial) == true) {
						endSerial = dbh.GetEndSerialNonSold(user.getId(), itemId, Integer.parseInt(denomination), bulkId, listStartSerial);
						int nextSerial = endSerial + Integer.parseInt(noOfItems);
						dbh.validateEndSerial(user.getId(), itemId, Integer.parseInt(denomination), bulkId, Integer.toString(nextSerial), String.valueOf(listStartSerial));
					} else {
						endSerial = dbh.GetEndSerialNonSold(user.getId(), itemId, Integer.parseInt(denomination), bulkId, listStartSerial);
						int nextSerial = endSerial + Integer.parseInt(noOfItems);
						dbh.SaveEndSerialWithoutEndSerial(user.getId(), itemId, Integer.parseInt(denomination), bulkId, Integer.toString(nextSerial));
					}
				}
				dbh.ClearTempSalesDetails();
				listData.clear();
				alert("Message", "Saved Successfully. " + "Your Invoice No. is " + salesid, R.drawable.icon);
			} else {
				dbh.deleteSalesDetails(longInvoiceId);
				dbh.ClearTempSalesDetails();
				listData.clear();
				alert("Message", "Sorry Invoice Is Not Save Successfully. ", R.drawable.icon);
			}
		}

	}

	public void alert(String title, String msg, int resourceID) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String merchantName = getIntent().getExtras().getString("merchantName");
				city = getIntent().getExtras().getString("city");
				String merchantId = getIntent().getExtras().getString("merchantID");

				Intent intent = new Intent(CardCategoryList.this, home.class);
				intent.putExtra("merchantName", merchantName);
				intent.putExtra("merchantID", merchantId);
				intent.putExtra("city", city);
				startActivity(intent);
				finish();
			}
		});
		alertDialog.setIcon(resourceID);
		alertDialog.show();
	}

	// Error Message Alert function.
	public void Erroralert(String title, String msg, int resourceID) {

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

	public void onClickDelete(View view) {
		AbsoluteLayout vwParentRow = (AbsoluteLayout) view.getParent();
		final TextView itemCode = (TextView) vwParentRow.getChildAt(3);
		final TextView qty = (TextView) vwParentRow.getChildAt(2);
		final TextView denomination = (TextView) vwParentRow.getChildAt(5);

		AlertDialog.Builder dlgAlert1 = new AlertDialog.Builder(this);

		dlgAlert1.setMessage("Do you want to delete the record?");
		dlgAlert1.setTitle("Confirmation");
		dlgAlert1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				// TODO Auto-generated method stub

				int quantity = Integer.parseInt(qty.getText().toString());
				int denominatinValue = Integer.parseInt(denomination.getText().toString());
				String item_codee = itemCode.getText().toString();
				dbh = new DatabaseHandler(getApplicationContext());

				String merchant_name = getIntent().getExtras().getString("merchantName");
				String merchantId_Value = getIntent().getExtras().getString("merchantID");

				dbh.DeleteFromTempList(itemCode.getText().toString(), denominatinValue, quantity);

				Intent intent = new Intent(CardCategoryList.this, CardCategoryList.class);
				intent.putExtra("merchantName", merchant_name);
				intent.putExtra("merchantID", merchantId_Value);
				startActivity(intent);
				finish();
			}
		});

		dlgAlert1.setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dlgAlert1.create().show();

	}

	private String getMyPhoneNumber() {

		return Utils.getSimSerialNumber(this);

	}
}
