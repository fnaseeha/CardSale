package com.lk.lankabell.android.activity.tsr.sqlite;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.lk.lankabell.android.activity.tsr.beans.Attendance;
import com.lk.lankabell.android.activity.tsr.beans.CardBundles;
import com.lk.lankabell.android.activity.tsr.beans.CardReturnsConfirmData;
import com.lk.lankabell.android.activity.tsr.beans.CardReturnsData;
import com.lk.lankabell.android.activity.tsr.beans.Cashire_Card_Balance;
import com.lk.lankabell.android.activity.tsr.beans.MerchantMaster;
import com.lk.lankabell.android.activity.tsr.beans.NextSerial;
import com.lk.lankabell.android.activity.tsr.beans.NextSerialSync;
import com.lk.lankabell.android.activity.tsr.beans.SalesDetail;
import com.lk.lankabell.android.activity.tsr.beans.SalesHeader;
import com.lk.lankabell.android.activity.tsr.beans.SalesResponse;
import com.lk.lankabell.android.activity.tsr.beans.SynchCashierConfirmation;
import com.lk.lankabell.android.activity.tsr.beans.SynchReport;
import com.lk.lankabell.android.activity.tsr.beans.TSRSystemprofile;
import com.lk.lankabell.android.activity.tsr.beans.cardSalesDeatils;
import com.lk.lankabell.android.activity.tsr.beans.cardSalesHeader;
import com.lk.lankabell.android.activity.tsr.sync.CardAccept;
import com.lk.lankabell.android.activity.tsr.sync.CityRegistration;
import com.lk.lankabell.android.activity.tsr.sync.MearchActivation;
import com.lk.lankabell.android.activity.tsr.sync.SynchRemarks;
import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;

//

@SuppressLint({ "SimpleDateFormat", "UseSparseArrays" })
public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 4;

	static String ResetTabels;
	// Database Name
	private static final String DATABASE_NAME = "TSRDBNEW";

	public static final String TABLE_USER_ATTENDANCE = "USER_ATTENDANCE";

	private static final String TABLE_SERVICE_URLS = "SERVICE_URLS";

	private Context context;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		SQLiteDatabase db = this.getWritableDatabase();
		// db.beginTransaction();
		createTables(db);
		this.context = context; 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("Inside:", "onCreate");
//		createTables(db);
	}

	public void createTables(SQLiteDatabase db) {

		/* tables */

		String createTable = "CREATE TABLE IF NOT EXISTS TSR_LOGIN(" +
				"EPF_NO STRING(8) NOT NULL," +
				"USER_NAME TEXT(20) NOT NULL," +
				"PASSWORD TEXT(10) NOT NULL," +
				"SERVER_DATE TEXT(35)," +
				"IS_SYNCH INTEGER(1) ," +
				"VERSION DOUBLE(12,2)," +
				"MOBILE_NO TEXT(12) NOT NULL)";

		db.execSQL(createTable);

		String createLogout = "CREATE TABLE IF NOT EXISTS LOGOUT(" +
				"USER_NAME TEXT(20) NOT NULL," +
				"LOG_OUT TEXT(20) )";

		db.execSQL(createLogout);

		createTable = "CREATE TABLE IF NOT EXISTS CITY_POSTAL_CODES(" + "POSTAL_CODE TEXT(20) NOT NULL," + "CITY TEXT(25)," + "REGION TEXT(25)," + "LATITUDE TEXT(12)," + "LONGITUDE TEXT(12)," + "POST_OFFICE_NAME TEXT(50),IS_SYNCH INTEGER(1),CHANGES INTEGER(5),MAX_CHANGES INTEGER(5)," + "AREA TEXT(25))";
		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS LOCATION_TRACK(" + "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "UPDATED_DATE TEXT," + "Accuracy TEXT," + "Speed TEXT," + "UseTime TEXT," + "Provider TEXT," + "LATT TEXT," + "LONGT TEXT," + "IS_SYNCH INTEGER)";
		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS LOCATION_TRACK_ERROR(" + "ID INTEGER PRIMARY KEY AUTOINCREMENT,UPDATED_DATE TEXT," + "Accuracy TEXT, Speed TEXT, UseTime TEXT, Provider TEXT," + "LATT TEXT," + "LONGT TEXT," + "IS_SYNCH INTEGER)";
		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS MERCHANT_MASTER(" + "MERCHANT_ID LONG (19) PRIMARY KEY," // INTEGER
				+ "MERCHANT_NAME TEXT(25) NOT NULL," + "ADDRESS TEXT(100)," + "RELOAD_NUMBER TEXT(12)," + "LATITUDE TEXT(12)," + "LONGITUDE TEXT(12)," + "REGISTERED_ON TEXT," + "TELEPHONE_NO TEXT(12)," + "CITY TEXT(25) NOT NULL," + "IS_ACTIVE INTEGER(1)," + "REGISTERED_BY_EPF_NO TEXT(8)," + "IS_REGISTERED INTEGER(1)," + "IS_EDITED INTEGER(1)," + "IS_UPDATED INTEGER(1),IS_ASSIGNED INTEGER(1),IS_SYNCH INTEGER(1) )";
		//
		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS MERCHANT_TEMP_DATA_TABLE(" + "MERCHANT_ID INTEGER(8) NOT NULL," + "MERCHANT_NAME TEXT(25) NOT NULL," + "ADDRESS TEXT(100)," + "TELEPHONE_NO TEXT(12)," + "RELOAD_NUMBER TEXT(12)," + "CITY TEXT(25) NOT NULL," + "IS_UPDATED INTEGER(1)," + "IS_ASSIGNED_UPDATED INTEGER(1)," + "IS_ASSIGNED INTEGER(1))";
		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS TSR_VISIT_REMARKS(" + "REMARKS_ID INTEGER(10) NOT NULL," + "MERCHANT_ID LONG(19)," + "REMARKS TEXT(50)," + "LATITUDE TEXT(12)," + "LONGITUDE TEXT(12)," + "VISIT_DATE TEXT," + "EPF_NO TEXT(8)," + "ISUPDATED INTEGER(1))";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS NEXT_SERIAL(" + "EPF_ID INTEGER NOT NULL, " + "CARD_TYPE TEXT(10)NOT NULL," + "DENOMINATION INTEGER(6)NOT NULL,IS_ALL_SOLD INTEGER NOT NULL ," + "BULK_NO 	 TEXT(10)NOT NULL," + "START_SERIAL 	 TEXT," + "NEXT_SERIAL_VALUE 	 TEXT," + "END_SERIAL TEXT,IS_SYNCH INTEGER(1)," + "CARD_RETURNS INTEGER(2) NOT NULL DEFAULT 0)";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS SALES_HEADER(" + "INVOICE_ID LONG(19) NOT NULL," + "MERCHANT_ID INTEGER(8)," + "LATITUDE TEXT(12)," + "LONGITUDE TEXT(12)," + "INVOICE_TOTAL DOUBLE(12,2)," + "ENTERED_DATE TEXT," + "ENTERED_BY INTEGER(8)," + "TOTAL_DISCOUNT DOUBLE(12,2)," + "ISUPDATED INTEGER(1),IS_SYNCH INTEGER(1))";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS SALES_DETAILS(" + "DETAILS_ID INTEGER(20) NOT NULL," + "[INVOICE_ID] LONG(19) NOT NULL CONSTRAINT [INVOICE_ID] REFERENCES [SALES_HEADER]([INVOICE_ID]) ON DELETE CASCADE ON UPDATE CASCADE MATCH FULL,"
		// + "INVOICE_ID LONG(19) NOT NULL,"
				+ "CARD_TYPE TEXT(3)NOT NULL," + "DENOMINATION INTEGER(6)NOT NULL," + "BULK_NO 	 TEXT(10)NOT NULL," + "NO_OF_CARDS INTEGER(6)," + "START_SERIAL TEXT(10)," + "END_SERIAL TEXT(10)," + "LINE_AMOUNT DOUBLE(12,2)," + "LINE_DISCOUNT DOUBLE(12,2)," + "ISUPDATED INTEGER(1),IS_SYNCH INTEGER(1)) ";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS TSR_RECEIVED_CARD_BUNDLES(" + "CARD_TYPE TEXT(10) NOT NULL," + "DENOMINATION INTEGER(6) NOT NULL," + "BULK_NO TEXT(10) NOT NULL," + "START_SERIAL INTEGER(10) NOT NULL," + "END_SERIAL INTEGER(10) NOT NULL," + "IS_ACCEPTED INTEGER(1)," + "IS_ACCEPT_UPDATE INTEGER(1)," + "IS_ALL_SOLD INTEGER(1) NOT NULL ," + "ENTRY_DATE TEXT," + "DISCOUNT_RATE DOUBLE," + "CARD_RESPONSE INTEGER," + "IS_SYNCH INTEGER(1)," + "NO_OF_CARDS INTEGER(6) NOT NULL," + "CARD_RETURNS INTEGER(2) NOT NULL DEFAULT 0)";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS MERCHANT_INVENTORY(" + "MERCHANT_ID LONG(19)NOT NULL," + "CARD_TYPE TEXT(10)NOT NULL," + "DENOMINATION INTEGER(6)NOT NULL," + "STOCK_IN_HAND INTEGER(10),SALE_COUNT INTEGER(10),ACTIVATION_COUNT INTEGER(10)," + "STOCK_COUNTED_DATE TEXT," + "REORDER_LEVEL INTEGER(10),IS_SYNCH INTEGER(1),IS_UPDATED INTEGER(1))";
		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS TSR_INVENTORY_DETAILS(" + "EPF_NO TEXT(8) NOT NULL," + "CARD_TYPE TEXT(10)NOT NULL," + "DENOMINATION INTEGER(6)NOT NULL," + "STOCK_IN_HAND INTEGER(10) NOT NULL," + "REORDER_LEVEL INTEGER(10) NOT NULL)";

		db.execSQL(createTable);

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS TSR_TABLES_UPDATED_DATE(" + "TABLE_NAME TEXT(50) NOT NULL," + "DATE TEXT(10) )";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS TSR_UPDATE_LOG(" + "UPDATE_STATUS TEXT(80) NOT NULL," + "UPDATED_DATE TEXT(10) )";

		db.execSQL(createTable);
		createTable = "CREATE TABLE IF NOT EXISTS TSR_ERROR_LOG(" + "UPDATE_STATUS TEXT(80) NOT NULL," + "UPDATED_DATE TEXT(10) )";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS CARD_DENOMINATOIN(" + "CARD_TYPE TEXT(10)NOT NULL," + "DENOMINATION INTEGER(6)NOT NULL," + "REAL_VALUE DOUBLE(12,2)," + "DISCOUNT_VALUE DOUBLE(12,2))";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS CARD_TYPES(" + "TYPE_CODE TEXT(10)NOT NULL ," + "TYPE_DESCRIPTION TEXT(50)," + "CARD_STATUS TEXT(1)," + "ACCOUNT_CODE TEXT(14))";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS SALES_TEMP_DETAILS(" + "CARD_TYPE TEXT(10)NOT NULL," + "DENOMINATION INTEGER(6)NOT NULL," + "BULK_NO 	 TEXT(10)NOT NULL, NO_OF_CARDS INTEGER(6)," + "START_SERIAL TEXT(10),listStartSerial TEXT(10), END_SERIAL TEXT(10)," + "DISCOUNT_RATE DOUBLE, LINE_AMOUNT DOUBLE(12,2)," + "LINE_DISCOUNT DOUBLE(12,2))";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS TSR_DATA_LOAD_LOG(" + "TABLE_NAME TEXT(150)," + "ALLTOTALROWS INTEGER(5)," + "LISTROWS INTEGER(5)," + "INSERTROWS INTEGER(5)," + "codeMessage INTEGER(2) )";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS TSR_SYSTEM_PROFILE(" + "NEXT_MERCHANT_NO INTEGER(9)," + "NEXT_INVOICE_NO INTEGER(9)," + "MAX_RECORDS INTEGER(9)," + "LAST_SYNCH_DATE TEXT(150)," + "LAST_CARD_DATE TEXT(150)," + "IS_SYNCH INTEGER(1), " + "SYSTEM_AUTO_SYNCH INTEGER(10)," + "GPS_GETTING INTEGER(10)," + "GPS_AUTO_SYNCH INTEGER(10))";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS TSR_3G_Status(" + "Status_Date INTEGER(9)," + "Status INTEGER(9)," + "IS_SYNCH INTEGER(1) )";

		db.execSQL(createTable);

		// date module component pre synch count/pre total records synch
		// count/pre total records
		createTable = "CREATE TABLE IF NOT EXISTS TSR_SYNC_LOG(DATE STRING(30)," + "MODULE TEXT(20), " + "COMPONENT TEXT(30)," + "PRE_SYNCH INTEGER(5)," + "PRE_TOTAL INTEGER(5)," + "POST_SYNCH INTEGER(5)," + "POST_TOTAL INTEGER(5)," + "RESPONSE_CODE INTEGER(1))";
		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS CARD_CASH_BALANCE (" + "Denomination_Type VARCHAR(150), " + "CARD_IN_HAND INTEGER(5), " + "CARD_IN_HAND_AMOUNT DOUBLE(10, 2), " + "CARD_SALES INTEGER(5), " + "CARD_SALES_AMOUNT DOUBLE(10, 2))";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS CASHIER_CONFIRM (" + "CONFIRM_DATE DATE, " + "SALE_AMOUNT DOUBLE(10, 2), " + "IN_HAND_AMOUNT DOUBLE(10, 2), " + "ADJUST_AMOUNT DOUBLE(10, 2), " + "CONFIRM_BY VARCHAR(150), " + "ADJUST_REMARK VARCHAR(20), " + "ZINC INTEGER(2), " + "AFTER_SALES INTEGER(2))";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS CASHIER_SYNCH (" + "SYNCH_DATE DATE, " + "CONFIRMATION INTEGER(2))";

		db.execSQL(createTable);

		createTable = "CREATE TABLE IF NOT EXISTS TSR_CARD_RETURNS(" + "CARD_TYPE TEXT(10) NOT NULL," + "DENOMINATION INTEGER(6) NOT NULL," + "BULK_NO INTEGER(10) NOT NULL," + "START_SERIAL INTEGER(10) NOT NULL," + "NEXT_SERIAL INTEGER(10) NOT NULL," + "END_SERIAL INTEGER(10) NOT NULL," + "NO_OF_CARDS INTEGER(6) NOT NULL," + "RETURN_TYPE VARCHAR(20) NOT NULL DEFAULT 'Card Return'," + "IS_SYNCH INTEGER(1));";

		db.execSQL(createTable);

		String sql_user_attendace = "create table IF NOT EXISTS " + TABLE_USER_ATTENDANCE + " (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "EPF_NUMBER TEXT," + "TEAM_NUMBER TEXT," + "ATTENDANCE_DATE INTEGER," + "TYPE TEXT," + "REASON TEXT," + "STATUS_CODE TEXT," + "SMS_STATUS_CODE INTEGER," + "BIKE TEXT," + "METER TEXT," + "IS_SYNCED INTEGER," + "CREATED_ON INTEGER)";
		db.execSQL(sql_user_attendace);



		addServiceUrl(db);

	}

	private void addServiceUrl(SQLiteDatabase db) {
		String sql_service_urls = "create table IF NOT EXISTS " + TABLE_SERVICE_URLS + " (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "URL TEXT)";
		db.execSQL(sql_service_urls);
		String sql_service_url_insert = "insert into " + TABLE_SERVICE_URLS + "(URL) VALUES('')";
		db.execSQL(sql_service_url_insert);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// TODO Auto-generated method stub
		Log.d("Inside:", "onUpgrade");

		if (newVersion == 3 && oldVersion < 3) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ATTENDANCE);
			String sql_user_attendace = "create table IF NOT EXISTS " + TABLE_USER_ATTENDANCE + " (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "EPF_NUMBER TEXT," + "TEAM_NUMBER TEXT," + "ATTENDANCE_DATE INTEGER," + "TYPE TEXT," + "REASON TEXT," + "STATUS_CODE TEXT," + "SMS_STATUS_CODE INTEGER," + "BIKE TEXT," + "METER TEXT," + "IS_SYNCED INTEGER," + "CREATED_ON INTEGER)";
			db.execSQL(sql_user_attendace);
		}

		if (oldVersion < 4 && newVersion == 4) {
			addServiceUrl(db);
		}
		if (oldVersion < 3 && newVersion == 4) {

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ATTENDANCE);
			String sql_user_attendace = "create table IF NOT EXISTS " + TABLE_USER_ATTENDANCE + " (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "EPF_NUMBER TEXT," + "TEAM_NUMBER TEXT," + "ATTENDANCE_DATE INTEGER," + "TYPE TEXT," + "REASON TEXT," + "STATUS_CODE TEXT," + "SMS_STATUS_CODE INTEGER," + "BIKE TEXT," + "METER TEXT," + "IS_SYNCED INTEGER," + "CREATED_ON INTEGER)";
			db.execSQL(sql_user_attendace);

			addServiceUrl(db);
		}

	}

	public Map<String, Integer> UpdateCityLongLat(float longt, float lat, String postal_Code) {
		Map<String, Integer> response = new HashMap<String, Integer>();
		int changesCount = 0;
		int maxChangesCount = 0;
		SQLiteDatabase dbR = this.getReadableDatabase();
		Cursor cursor = dbR.rawQuery("SELECT (case when CHANGES is null then '0' else CHANGES end)CHANGES,(case when MAX_CHANGES is null then '0' else MAX_CHANGES end)MAX_CHANGES  FROM CITY_POSTAL_CODES WHERE POSTAL_CODE='" + postal_Code + "' ", null);
		if (cursor.moveToNext()) {
			changesCount = cursor.getInt(0);
			maxChangesCount = cursor.getInt(1);
		}
		cursor.close();
		dbR.close();

		SQLiteDatabase db = this.getWritableDatabase();
		int changesCountVar = 0;
		changesCountVar = changesCount + 1;
		if (changesCount < maxChangesCount) {
			String sql = "update CITY_POSTAL_CODES set CHANGES='" + changesCountVar + "',LATITUDE='" + lat + "' ,IS_SYNCH='0', LONGITUDE='" + longt + "' where POSTAL_CODE='" + postal_Code + "' ";
			db.execSQL(sql);
			response.put("success", changesCount);
			db.close();
		} else {
			response.put("failed", changesCount);
		}

		return response;

	}

	public void saveCardSale(String startSerial, String endSerial, int locId) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("START_SERIAL", startSerial);
		values.put("END_SERIAL", endSerial);
		values.put("LOC_ID", locId);

		// Inserting Row
		db.insert("TSR_SALES_INFO", null, values);
		db.close(); // Closing database connection
	}

	public void saveFault(String teamCode, String faultId, String atCode, int meter, int geoId, String signalStrength, String VehivleNo) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("TEAM_CODE", teamCode);
		values.put("FAULT_ID", faultId);
		values.put("AT_CODE", atCode);
		values.put("METER_READING", meter);
		values.put("GEO_ID", geoId);
		values.put("SIGNAL_STRENGTH", signalStrength);
		values.put("VEHICLE_NO", VehivleNo);

		// Inserting Row
		db.insert("TSR_FAULT_ENTRY", null, values);
		db.close(); // Closing database connection

	}

	public int GetItemIdByItemCode(String itemName) {
		int itemCode = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT CATEGORY_ID FROM CARD_CATEGORY WHERE CATEGORY_NAME = '" + itemName + "' ", null);
		if (cursor.moveToNext()) {
			itemCode = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return itemCode;
	}

	public ArrayList<String> getCities() {
		ArrayList<String> list = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT distinct(CITY) FROM CITY_POSTAL_CODES ORDER BY CITY", null);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return list;
	}

	public int getPostalCodeByCityName(String cityName) {

		int postalCode = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT A.POSTAL_CODE FROM CITY_POSTAL_CODES A,MERCHANT_MASTER B WHERE B.CITY= trim('" + cityName + "') AND A.CITY = B.CITY ", null);
		if (cursor.moveToNext()) {
			postalCode = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return postalCode;
	}

	public ArrayList<SerialDetails> getNextSerialDetails() {
		ArrayList<SerialDetails> list = new ArrayList<SerialDetails>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select epf_id,card_type,denomination,bulk_no,start_serial,next_serial_value,end_serial,is_all_sold from next_serial where IS_SYNCH='0' ", null);
		while (cursor.moveToNext()) {
			list.add(new SerialDetails(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public PostalCode getPostalCodeDetailsByCityName(String cityName) {
		PostalCode postalCode = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM CITY_POSTAL_CODES WHERE CITY = '" + cityName + "' ", null);
		if (cursor.moveToNext()) {
			postalCode = new PostalCode(cursor.getString(0), cursor.getString(1), cursor.getString(3), cursor.getString(4), 0, cursor.getString(2), cursor.getString(5), cursor.getString(6));
		}
		cursor.close();
		db.close();
		return postalCode;
	}

	public ArrayList<Merchant> getMerchantList(String CITY) {

		ArrayList<Merchant> list = new ArrayList<Merchant>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT A.MERCHANT_NAME,A.ADDRESS,A.TELEPHONE_NO,A.CITY,B.POSTAL_CODE,A.MERCHANT_ID FROM MERCHANT_MASTER A,CITY_POSTAL_CODES B WHERE A.CITY='" + CITY + "' AND A.CITY = B.CITY AND IS_ASSIGNED='1' AND IS_REGISTERED <> '1'";
		Cursor cursor = db.rawQuery(sql, null);
		// Cursor cursor =
		// db.rawQuery("SELECT A.MERCHANT_NAME,A.ADDRESS,A.TELEPHONE_NO,A.CITY,B.POSTAL_CODE,A.MERCHANT_ID FROM MERCHANT_MASTER A,CITY_POSTAL_CODES B WHERE A.CITY='"+
		// CITY + "'  AND A.CITY = B.CITY AND ", null);
		while (cursor.moveToNext()) {
			Log.d("Query Details : ", cursor.getInt(5) + " === " + cursor.getString(0) + "------------------ " + sql);
			list.add(new Merchant(cursor.getLong(5), cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getString(3), "0", "0", 0, 0, ""));
		}
		cursor.close();
		db.close();

		return list;
	}

	public Merchant getMerchant(String name, String CITY) {
		Merchant m = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT A.MERCHANT_NAME,A.ADDRESS,A.TELEPHONE_NO,A.CITY,B.POSTAL_CODE,A.MERCHANT_ID,A.RELOAD_NUMBER FROM MERCHANT_MASTER A,CITY_POSTAL_CODES B WHERE A.MERCHANT_NAME='" + name + "' AND A.CITY='" + CITY + "' AND A.CITY = B.CITY";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			m = new Merchant(cursor.getLong(5), cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getString(3), "0", "0", 0, 0, cursor.getString(6));// 11
			// (merchantId,name, address, phone,postalCode, city,latitude,
			// longitude, double distance,isRegistred,reloadNo)//11
		}
		cursor.close();
		db.close();

		return m;
	}

	public int getPostalCodeByMerchantId(int merchantId) {
		int postalCode = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT B.POSTAL_CODE FROM MERCHANT_MASTER A,CITY_POSTAL_CODES B WHERE A.MERCHANT_ID= " + merchantId + " AND A.CITY = B.CITY", null);
		if (cursor.moveToNext()) {

			postalCode = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return postalCode;
	}

	public Long getMerchantIdByNameANDPhone(String name, String phone) {
		long id = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT MERCHANT_ID FROM MERCHANT_MASTER WHERE MERCHANT_NAME= '" + name + "' AND TELEPHONE_NO = '" + phone + "' ", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getLong(0);

		}
		cursor.close();
		db.close();

		return id;
	}

	public ArrayList<Merchant> getMerchantListWithPostalCode(int postalCode) {

		ArrayList<Merchant> list = new ArrayList<Merchant>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT A.MERCHANT_NAME,A.ADDRESS,A.TELEPHONE_NO,A.CITY,B.POSTAL_CODE,A.MERCHANT_ID FROM MERCHANT_MASTER A,CITY_POSTAL_CODES B WHERE B.POSTAL_CODE=" + postalCode + " AND A.CITY = B.CITY", null);
		while (cursor.moveToNext()) {

			list.add(new Merchant(cursor.getLong(5), cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getString(3), "0", "0", 0, 0, ""));

		}
		cursor.close();
		db.close();

		return list;
	}

	public ArrayList<String> getRegisteredMerchantsCityList() {

		ArrayList<String> list = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery("SELECT distinct (A.CITY) FROM CITY_POSTAL_CODES A,MERCHANT_MASTER B WHERE A.CITY = B.CITY ", null);
		Cursor cursor = db.rawQuery("SELECT distinct (A.CITY) FROM CITY_POSTAL_CODES A,MERCHANT_MASTER B WHERE A.CITY = B.CITY AND B.IS_REGISTERED ='1' ", null);
		while (cursor.moveToNext()) {

			list.add(cursor.getString(0));

		}
		cursor.close();
		db.close();

		return list;
	}

	public String getMerchantNameById(Long id) {
		String name = "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT MERCHANT_NAME FROM MERCHANT_MASTER WHERE MERCHANT_ID=" + id + " ", null);
		if (cursor.moveToNext()) {

			name = cursor.getString(0);

		}
		cursor.close();
		db.close();

		return name;
	}

	public Long getMerchantIdByNameAndAddress(String name, String MearchantID) {
		long id = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT MERCHANT_ID  FROM MERCHANT_MASTER WHERE MERCHANT_ID = '" + MearchantID + "' ", null);
		if (cursor.moveToNext()) {
			id = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return id;
	}

	public double getValueByItemName(String name, int denomination) {
		SQLiteDatabase db = this.getReadableDatabase();
		double val = 0;
		Cursor cursor = db.rawQuery("SELECT REAL_VALUE  FROM CARD_DENOMINATOIN WHERE CARD_TYPE ='" + name + "' AND DENOMINATION =" + denomination + " ", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			val = cursor.getDouble(0);

		}
		cursor.close();
		db.close();
		return val;
	}

	public double getDiscountPriceByItemName(String bulkId, int denomination, String cardType) {
		SQLiteDatabase db = this.getReadableDatabase();
		double val = 0;
		Cursor cursor = db.rawQuery("SELECT DISCOUNT_RATE  FROM TSR_RECEIVED_CARD_BUNDLES WHERE CARD_TYPE ='" + cardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO = '" + bulkId + "' ", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			val = cursor.getDouble(0);

		}
		cursor.close();
		db.close();
		return val;
	}

	// Getting the max merchant id
	public int GetmaxMerchantId() {
		SQLiteDatabase db = this.getReadableDatabase();
		int id = 0;
		Cursor cursor = db.rawQuery("SELECT NEXT_MERCHANT_NO FROM TSR_SYSTEM_PROFILE", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return id;
	}

	/*** Login Info Transactions ****/

	public int GetmaxSalesId() {

		SQLiteDatabase db = this.getReadableDatabase();

		int id = 1;

		Cursor cursor = db.rawQuery("SELECT NEXT_INVOICE_NO FROM TSR_SYSTEM_PROFILE", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return id;
	}

	/* My sales GetMin date function */
	public String GetminDate() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT ENTERED_DATE FROM SALES_HEADER LIMIT 1", null);

		String mindate = "";

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			mindate = cursor.getString(0);
		}

		cursor.close();
		db.close();

		return mindate;
	}

	public int GetMeasureList() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT MIN(STOCK_IN_HAND/REORDER_LEVEL) AS MEASURE  FROM MERCHANT_INVENTORY ", null);

		int measure = 0;

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			measure = cursor.getInt(0);
		}

		cursor.close();
		db.close();

		return measure;
	}

	public int GetmaxItemSalesId(int invoiceId) {

		SQLiteDatabase db = this.getReadableDatabase();

		int id = 0;

		Cursor cursor = db.rawQuery("SELECT MAX(DETAILS_ID) FROM SALES_DETAILS WHERE INVOICE_ID = " + invoiceId + " ", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return id;
	}

	public int GetStartSerialByBulkNo(String item, int Denomination, String bulkId) {
		SQLiteDatabase db = this.getReadableDatabase();
		int startSerial = 0;
		String sql = "SELECT START_SERIAL FROM  NEXT_SERIAL WHERE CARD_TYPE ='" + item + "' AND DENOMINATION=" + Denomination + " AND IS_ALL_SOLD ='0' AND BULK_NO='" + bulkId + "' ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			startSerial = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return startSerial;
	}

	public int GetNextSerialByBulkNoFirstNonSold(String item, int Denomination, String bulkId) {
		SQLiteDatabase db = this.getReadableDatabase();
		int nextSerial = 0;
		String sql = "SELECT NEXT_SERIAL_VALUE FROM  NEXT_SERIAL WHERE CARD_TYPE ='" + item + "' AND DENOMINATION=" + Denomination + " AND BULK_NO='" + bulkId + "'  AND IS_ALL_SOLD='0' ORDER BY ROWID ASC LIMIT 1 ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			nextSerial = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return nextSerial;
	}

	public int GetStartSerialByBulkNoFirstNonSold(String item, int Denomination, String bulkId) {
		SQLiteDatabase db = this.getReadableDatabase();
		int startSerial = 0;
		String sql = "SELECT START_SERIAL FROM  NEXT_SERIAL WHERE CARD_TYPE ='" + item + "' AND DENOMINATION=" + Denomination + " AND BULK_NO='" + bulkId + "'  AND IS_ALL_SOLD='0' ORDER BY ROWID ASC LIMIT 1 ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			startSerial = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return startSerial;
	}

	public int GetEndSerialByBulkNoFirstNonSold(String item, int Denomination, String bulkId) {
		SQLiteDatabase db = this.getReadableDatabase();
		int endSerial = 0;
		String sql = "SELECT END_SERIAL FROM  NEXT_SERIAL WHERE CARD_TYPE ='" + item + "' AND DENOMINATION=" + Denomination + " AND BULK_NO='" + bulkId + "'  AND IS_ALL_SOLD='0' ORDER BY ROWID ASC LIMIT 1 ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			endSerial = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return endSerial;
	}

	public int GetEndSerialByBulkNo(String item, int Denomination, String bulkId) {
		SQLiteDatabase db = this.getReadableDatabase();
		int endSerial = 0;
		String sql = "SELECT END_SERIAL FROM  NEXT_SERIAL WHERE CARD_TYPE ='" + item + "' AND DENOMINATION=" + Denomination + " AND IS_ALL_SOLD ='0' AND BULK_NO='" + bulkId + "' ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			endSerial = cursor.getInt(0);

		}
		cursor.close();
		db.close();
		return endSerial;
	}

	public int GetmaxRemarksId() {
		SQLiteDatabase db = this.getReadableDatabase();
		int id = 0;
		Cursor cursor = db.rawQuery("SELECT MAX(REMARKS_ID) FROM TSR_VISIT_REMARKS", null);
		if (cursor.moveToFirst()) {
			id = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return id;
	}

	public String GetEmpNoByPhoneNo(String mobileNo) {
		/*** Get max login info id ***/
		SQLiteDatabase db = this.getReadableDatabase();
		String empNo = "";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT EPF_NO FROM TSR_LOGIN WHERE MOBILE_NO='" + mobileNo + "' ", null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				empNo = cursor.getString(0);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db.close();
		}
		return empNo;
	}

	public String GetNameByMobileNo(String MobileNo) {
		/*** Get max login info id ***/

		SQLiteDatabase db = this.getReadableDatabase();

		String name = "";

		Cursor cursor = db.rawQuery("SELECT USER_NAME FROM TSR_LOGIN WHERE MOBILE_NO ='" + MobileNo + "'", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			name = cursor.getString(0);

		}
		cursor.close();
		db.close();

		return name;
	}

	public String GetPasswordByUserName(String name) {
		/*** Get max login info id ***/

		SQLiteDatabase db = this.getReadableDatabase();

		String pwd = "";

		Cursor cursor = db.rawQuery("SELECT PASSWORD FROM TSR_LOGIN WHERE USER_NAME ='" + name + "'", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			pwd = cursor.getString(0);

		}
		cursor.close();
		db.close();

		return pwd;
	}

	public int GetMaxFaultTicketId() {

		SQLiteDatabase db = this.getReadableDatabase();

		int id = 0;

		Cursor cursor = db.rawQuery("SELECT MAX(FAULT_TICKET_ID) FROM FAULT_TICKET_MASTER", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return id;
	}

	public String GetLastUpdatedDate() {
		SQLiteDatabase db = this.getReadableDatabase();
		String date = "";
		Cursor cursor = null;
		try {
			 cursor = db.rawQuery("SELECT DATE FROM TSR_TABLES_UPDATED_DATE WHERE TABLE_NAME = 'TSR_RECEIVED_CARD_BUNDLES' ", null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				date = cursor.getString(0);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db.close();
		}


		return date;
	}

	public String GetCardTotal() {

		SQLiteDatabase db = this.getReadableDatabase();

		String total = "";

		Cursor cursor = db.rawQuery("SELECT SUM (LINE_AMOUNT) FROM SALES_TEMP_DETAILS", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			total = cursor.getString(0);

		}
		cursor.close();
		db.close();

		return total;
	}

	public String GetMerchantInventoryLastUpdatedDate() {
		SQLiteDatabase db = this.getReadableDatabase();
		String date = "";
		Cursor cursor = db.rawQuery("SELECT DATE FROM TSR_TABLES_UPDATED_DATE WHERE TABLE_NAME = 'MERCHANT_INVENTORY' ", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			date = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return date;
	}

	public void SaveRemaks(Remarks remarks, String userId) { // Save remarks

		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = sdf1.format(System.currentTimeMillis());

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("REMARKS_ID", remarks.remarksId);
		values.put("MERCHANT_ID", remarks.merchantId);
		values.put("REMARKS", remarks.remarks);
		values.put("LATITUDE", remarks.latitude);
		values.put("LONGITUDE", remarks.longitude);
		values.put("VISIT_DATE", currentDate);
		values.put("EPF_NO", userId);

		db.insert("TSR_VISIT_REMARKS", null, values);
		db.close();
	}

	public int GetmaxFaultTicketDetailCode(int faultTicketId) {

		SQLiteDatabase db = this.getReadableDatabase();

		int id = 0;

		Cursor cursor = db.rawQuery("SELECT MAX(FAULT_TICKET_DETAILS_CODE) FROM FAULT_TICKET_DETAILS WHERE FAULT_TICKET_ID = " + faultTicketId + " ", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return id;
	}

	public void SaveItemSale(ItemSale itemSale, String UserId, double totalDiscount) { // save
																						// ItemSales
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		String currentDate = sdf.format(System.currentTimeMillis());
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("INVOICE_ID", itemSale.itemSaleId);
		values.put("MERCHANT_ID", itemSale.merchantId);
		values.put("LATITUDE", itemSale.latitude);
		values.put("LONGITUDE", itemSale.longitude);
		values.put("INVOICE_TOTAL", itemSale.totalAmount);
		values.put("ENTERED_DATE", currentDate);
		values.put("ENTERED_BY", UserId);
		values.put("TOTAL_DISCOUNT", totalDiscount);
		values.put("IS_SYNCH", 0);
		db.insert("SALES_HEADER", null, values);
		db.close();
	}

	public void SaveItemSalesDetails(ItemSalesDetails itemSalesDetails) { // save
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("DETAILS_ID", itemSalesDetails.itemSaleId);
		values.put("INVOICE_ID", itemSalesDetails.itemSalesNo);
		values.put("CARD_TYPE", itemSalesDetails.itemid);
		values.put("DENOMINATION", itemSalesDetails.denomination);
		values.put("BULK_NO", itemSalesDetails.bulkId);
		values.put("NO_OF_CARDS", itemSalesDetails.noOfItems);
		values.put("START_SERIAL", itemSalesDetails.startSerial);
		values.put("LINE_AMOUNT", itemSalesDetails.lineAmount);
		values.put("LINE_DISCOUNT", itemSalesDetails.lineDiscount);
		values.put("IS_SYNCH", 0);
		db.insert("SALES_DETAILS", null, values);
		db.close();
	}

	public void SavefaultTicket(FaultTickets faultTickets, int faultTicketId) { // save
																				// ItemSales

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("FAULT_TICKET_ID", faultTicketId);
		values.put("TEAM_CODE", faultTickets.teamCode);
		values.put("FAULT_CODE", faultTickets.FaultId);
		values.put("ACTION_TAKEN_CODE", faultTickets.ActionTakenCode);
		values.put("SIGNAL_STRENGTH", faultTickets.signalStrength);
		values.put("METER_READING", faultTickets.MeterReading);
		values.put("VEHICLE_NO", faultTickets.vehicleNo);

		db.insert("FAULT_TICKET_MASTER", null, values);
		db.close();
	}

	public void SavefaultTicketMaterial(MaterialEntry materialEntry, int faultTicketId, int faultTicketDetailedCode) { // save
																														// ItemSales

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("FAULT_TICKET_ID", faultTicketId);
		values.put("FAULT_TICKET_DETAILS_CODE", faultTicketDetailedCode);
		values.put("ITEM", materialEntry.item);
		values.put("REPAIR_TYPE", materialEntry.repairType);
		values.put("REPAIR_REMARKS", materialEntry.remarks);
		values.put("QUANTITY", materialEntry.quantity);

		db.insert("FAULT_TICKET_DETAILS", null, values);
		db.close();
	}

	public boolean CheckEndSerialAlreadyExist(int epfNo, String item, int Denomination, String BulkId) {

		SQLiteDatabase db = this.getReadableDatabase();

		boolean isExist = false;

		Cursor cursor = db.rawQuery("SELECT END_SERIAL FROM NEXT_SERIAL WHERE CARD_TYPE='" + item + "' AND DENOMINATION =" + Denomination + " AND BULK_NO ='" + BulkId + "' AND EPF_ID =" + epfNo + " ", null);
		if (cursor.getCount() > 0) {
			isExist = true;
		} else {
			isExist = false;
		}
		cursor.close();
		db.close();

		return isExist;
	}

	public boolean CheckEndSerialAlreadyExistInTempSales(int epfNo, String item, int Denomination, String BulkId, String listStartSerial) {

		SQLiteDatabase db = this.getReadableDatabase();

		boolean isExist = false;
		String sql = "SELECT END_SERIAL FROM NEXT_SERIAL WHERE CARD_TYPE='" + item + "' AND DENOMINATION =" + Denomination + " AND BULK_NO ='" + BulkId + "' AND EPF_ID =" + epfNo + " AND START_SERIAL ='" + listStartSerial + "' AND IS_ALL_SOLD='0' ORDER BY ROWID ASC LIMIT 1 ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			isExist = true;
		} else {
			isExist = false;
		}
		cursor.close();
		db.close();

		return isExist;
	}

	public boolean CheckEndSerialAlreadyExistInSales(int epfNo, String item, int Denomination, String BulkId) {

		SQLiteDatabase db = this.getReadableDatabase();

		boolean isExist = false;
		String sql = "SELECT END_SERIAL FROM NEXT_SERIAL WHERE CARD_TYPE='" + item + "' AND DENOMINATION =" + Denomination + " AND BULK_NO ='" + BulkId + "' AND EPF_ID =" + epfNo + " AND IS_ALL_SOLD='0' ORDER BY ROWID ASC LIMIT 1 ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			isExist = true;
		} else {
			isExist = false;
		}
		cursor.close();
		db.close();

		return isExist;
	}

	public int GetEndSerial(int epfNo, String item, int Denomination, String BulkId) {

		int endSerial = 0;
		String sql = "SELECT NEXT_SERIAL_VALUE FROM NEXT_SERIAL WHERE CARD_TYPE='" + item + "' AND DENOMINATION =" + Denomination + " AND BULK_NO ='" + BulkId + "' AND EPF_ID =" + epfNo + " ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {

			endSerial = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return endSerial;
	}

	public int GetEndSerialNonSold(int epfNo, String item, int Denomination, String BulkId, String startSerial) {

		int endSerial = 0;

		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT NEXT_SERIAL_VALUE FROM NEXT_SERIAL WHERE CARD_TYPE='" + item + "' AND DENOMINATION =" + Denomination + " AND START_SERIAL='" + startSerial + "' AND BULK_NO ='" + BulkId + "' AND EPF_ID =" + epfNo + "  AND IS_ALL_SOLD='0' ORDER BY ROWID ASC LIMIT 1 ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			endSerial = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return endSerial;
	}

	public int GetEndSerialNonSoldFirst(int epfNo, String item, int Denomination, String BulkId) {

		int endSerial = 0;

		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT NEXT_SERIAL_VALUE FROM NEXT_SERIAL WHERE CARD_TYPE='" + item + "' AND DENOMINATION =" + Denomination + " AND BULK_NO ='" + BulkId + "' AND EPF_ID =" + epfNo + "  AND IS_ALL_SOLD='0' ORDER BY START_SERIAL ASC LIMIT 1 ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {

			endSerial = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return endSerial;
	}

	public ArrayList<Serials> getNonSoldStartNextAndEndSerials(int epfNo, String item, int Denomination, String BulkId, String nextSerial) {
		ArrayList<Serials> list = new ArrayList<Serials>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT START_SERIAL,END_SERIAL,NEXT_SERIAL_VALUE FROM NEXT_SERIAL WHERE CARD_TYPE='" + item + "' AND DENOMINATION =" + Denomination + " AND IS_ALL_SOLD='0' AND BULK_NO ='" + BulkId + "' AND NEXT_SERIAL_VALUE = '" + nextSerial + "' AND EPF_ID =" + epfNo + " ";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			list.add(new Serials(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public List<SalesHeaders> getAllSalesHeaders() {
		ArrayList<SalesHeaders> list = new ArrayList<SalesHeaders>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select INVOICE_ID,MERCHANT_ID,LATITUDE,LONGITUDE,INVOICE_TOTAL,ENTERED_DATE,ENTERED_BY,TOTAL_DISCOUNT from sales_header";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			list.add(new SalesHeaders(cursor.getLong(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getString(5), cursor.getInt(6), cursor.getDouble(7)));

		}
		cursor.close();
		db.close();
		return list;
	}

	public ArrayList<SalesDetails> getAllSalesDetails() {
		ArrayList<SalesDetails> list = new ArrayList<SalesDetails>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select DETAILS_ID,CARD_TYPE,DENOMINATION,BULK_NO,NO_OF_CARDS,START_SERIAL,END_SERIAL,LINE_AMOUNT,LINE_DISCOUNT,INVOICE_ID from sales_details";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			list.add(new SalesDetails(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getDouble(6), cursor.getDouble(7), cursor.getLong(8)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public void SaveEndSerial(int etfNo, String item, int denomination, String bulkId, String startSerial, String endSerial) {
		SQLiteDatabase dbR = this.getReadableDatabase();
		String sql = "select * from next_serial where card_type='" + item + "' and denomination='" + denomination + "' and bulk_no='" + bulkId + "' and start_serial='" + startSerial + "' and end_serial='" + endSerial + "'";
		Cursor cursor = dbR.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			Log.w("ALREADY EXIST", "SERIAL ALREADY EXIST");
		} else {
			SQLiteDatabase db = this.getWritableDatabase();
			String sql2 = "INSERT INTO NEXT_SERIAL (EPF_ID,CARD_TYPE,DENOMINATION,BULK_NO,START_SERIAL,END_SERIAL,NEXT_SERIAL_VALUE,IS_ALL_SOLD,IS_SYNCH) VALUES( '" + etfNo + "','" + item + "'," + denomination + ",'" + bulkId + "','" + startSerial + "','" + endSerial + "','" + startSerial + "',0,0) ";
			db.execSQL(sql2);
			db.close();
		}
	}

	public void UpdateEndSerial(int etfNo, String item, int denomination, String bulkId, String startSerial, String endSerial, String nextSerial) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE NEXT_SERIAL SET START_SERIAL= '" + startSerial + "',END_SERIAL='" + endSerial + "',NEXT_SERIAL_VALUE ='" + nextSerial + "' WHERE EPF_ID ='" + etfNo + "' AND CARD_TYPE ='" + item + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + bulkId + "' ");
		db.close();
	}

	public void UpdateEndSerial(int etfNo, String item, int denomination, String BulkId, String endSerial) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE NEXT_SERIAL SET NEXT_SERIAL_VALUE = '" + endSerial + "' WHERE EPF_ID = '" + etfNo + "' AND CARD_TYPE ='" + item + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + BulkId + "'");
		db.close();
	}

	public void validateEndSerial(int etfNo, String item, int denomination, String BulkId, String nextSerial, String startSerial) {
		SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db2 = this.getReadableDatabase();
		SQLiteDatabase db3 = this.getWritableDatabase();
		Cursor cursor = null;
		try {
			String sql = "UPDATE NEXT_SERIAL SET IS_SYNCH='0', NEXT_SERIAL_VALUE = '" + nextSerial + "' WHERE EPF_ID = '" + etfNo + "' AND CARD_TYPE ='" + item + "' AND START_SERIAL='" + startSerial + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + BulkId + "'";
			db.execSQL(sql);
			int endSerialValue = 0;

			String sql3 = "SELECT END_SERIAL FROM NEXT_SERIAL WHERE CARD_TYPE='" + item + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + BulkId + "' AND EPF_ID =" + etfNo + "  AND START_SERIAL='" + startSerial + "'  AND IS_ALL_SOLD='0' ORDER BY ROWID ASC LIMIT 1 ";
			cursor = db2.rawQuery(sql3, null);
			if (cursor.moveToNext()) {
				endSerialValue = cursor.getInt(0);
			}


			if (Integer.valueOf(nextSerial) > endSerialValue) {

				String sql2 = "UPDATE NEXT_SERIAL SET IS_ALL_SOLD='1' WHERE EPF_ID = '" + etfNo + "' AND CARD_TYPE ='" + item + "' AND START_SERIAL='" + startSerial + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + BulkId + "'";
				db3.execSQL(sql2);

			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db2.close();
			db.close();
			db3.close();
		}

	}

	// --TSR_SYNC_LOG--
	// DATE MODULE COMPONENT PRE_SYNCH PRE_TOTAL POST_SYNCH POST_TOTAL

	public void insertSynchLog(String date, String module, String component, int preSynch, int preTotal, int postSynch, int postTotal, int responseCode) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO TSR_SYNC_LOG (DATE,MODULE,COMPONENT,PRE_SYNCH,PRE_TOTAL,POST_SYNCH,POST_TOTAL,RESPONSE_CODE) VALUES('" + date + "','" + module + "','" + component + "','" + preSynch + "','" + preTotal + "','" + postSynch + "','" + postTotal + "','" + responseCode + "') ");
		db.close();
	}

	public void clearSynchLog() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "delete from TSR_SYNC_LOG";
		db.execSQL(sql);
		db.close();
	}

	public void insertStatus(String errorMessage) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO TSR_UPDATE_LOG (UPDATE_STATUS,UPDATED_DATE) VALUES('" + errorMessage + "',date('now')) ");
		db.close();
	}

	public void insertError(String errorMessage) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO TSR_ERROR_LOG (UPDATE_STATUS,UPDATED_DATE) VALUES('" + errorMessage + "',date('now')) ");
		db.close();
	}

	public void clearErrorLog() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "delete from TSR_ERROR_LOG";
		db.execSQL(sql);
		db.close();
	}

	public void clearStatusLog() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "delete from TSR_UPDATE_LOG";
		db.execSQL(sql);
		db.close();
	}

	public void SaveEndSerialWithoutEndSerial(int etfNo, String item, int denomination, String bulkId, String endSerial) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO NEXT_SERIAL (EPF_ID,CARD_TYPE,DENOMINATION,BULK_NO,END_SERIAL) VALUES( '" + etfNo + "','" + item + "'," + denomination + ",'" + bulkId + "','" + endSerial + "') ");
		db.close();
	}

	public void UpdateMerchant(String lat, String lon, String registeredOn, String registeredBy, Long merchantId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE MERCHANT_MASTER SET IS_ACTIVE = '1', IS_REGISTERED='1', IS_SYNCH='0', LATITUDE = '" + lat + "',LONGITUDE ='" + lon + "',REGISTERED_ON ='" + registeredOn + "',REGISTERED_BY_EPF_NO='" + registeredBy + "' WHERE MERCHANT_ID = " + merchantId + "");
		db.close();
	}

	public void UpdateCardBulkEndSerial(String item, int denomination, String BulkId, int StartSerial, int endSerial) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_RECEIVED_CARD_BUNDLES SET END_SERIAL = " + endSerial + " WHERE  CARD_TYPE ='" + item + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + BulkId + "' AND IS_ACCEPTED ='1' AND START_SERIAL =" + StartSerial + " ");
		db.close();
	}

	public int GetCategoryIdByName(String categoryName) {

		int endSerial = 0;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT CATEGORY_ID FROM CARD_CATEGORY WHERE CATEGORY_NAME = '" + categoryName + "' ", null);
		if (cursor.moveToNext()) {

			endSerial = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return endSerial;
	}

	public void InsertTableData() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.enableWriteAheadLogging();
		db.execSQL("INSERT INTO TSR_TABLES_UPDATED_DATE(TABLE_NAME) VALUES('TSR_LOGIN')");
		db.execSQL("INSERT INTO TSR_TABLES_UPDATED_DATE(TABLE_NAME) VALUES('CITY_POSTAL_CODES')");
		db.execSQL("INSERT INTO TSR_TABLES_UPDATED_DATE(TABLE_NAME) VALUES('MERCHANT_MASTER')");
		db.execSQL("INSERT INTO TSR_TABLES_UPDATED_DATE(TABLE_NAME) VALUES('CARD_DENOMINATOIN')");
		db.execSQL("INSERT INTO TSR_TABLES_UPDATED_DATE(TABLE_NAME) VALUES('CARD_TYPES')");
		db.execSQL("INSERT INTO TSR_TABLES_UPDATED_DATE(TABLE_NAME) VALUES('TSR_RECEIVED_CARD_BUNDLES')");
		db.execSQL("INSERT INTO TSR_TABLES_UPDATED_DATE(TABLE_NAME) VALUES('MERCHANT_INVENTORY')");
	}

	public void SetLoginDetailsUpdatedDate(String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_TABLES_UPDATED_DATE SET DATE = '" + date + "' WHERE TABLE_NAME = 'TSR_LOGIN' ");
		db.close();
	}

	public void SetCityPostalCodesUpdatedDate(String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_TABLES_UPDATED_DATE SET DATE = '" + date + "' WHERE TABLE_NAME = 'CITY_POSTAL_CODES' ");
		db.close();
	}

	public void SetMerchantMasterUpdatedDate(String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_TABLES_UPDATED_DATE SET DATE = '" + date + "' WHERE TABLE_NAME = 'MERCHANT_MASTER'  ");
		db.close();
	}

	public void SetCardDenominationUpdatedDate(String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_TABLES_UPDATED_DATE SET DATE = '" + date + "' WHERE TABLE_NAME = 'CARD_DENOMINATOIN' ");
		db.close();
	}

	public void SetCardTypesUpdatedDate(String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_TABLES_UPDATED_DATE SET DATE = '" + date + "' WHERE TABLE_NAME = 'CARD_TYPES' ");
		db.close();
	}

	public void SetBulkIdUpdatedDate(String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_TABLES_UPDATED_DATE SET DATE = '" + date + "' WHERE TABLE_NAME = 'TSR_RECEIVED_CARD_BUNDLES' ");
		db.close();
	}

	public void SetMerchantInventorydatedDate(String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_TABLES_UPDATED_DATE SET DATE = '" + date + "' WHERE TABLE_NAME = 'MERCHANT_INVENTORY' ");
		db.close();
	}

	public void InsertLoginDetails(String loginSql) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(loginSql);
		db.close();
	}

	public boolean CheckMerchantIdAndCardTypeAndDenominationIsExistsByMerchantInventoryTable(Long merchantId, int denomination, String cardType) {
		SQLiteDatabase db = this.getReadableDatabase();
		boolean isExist = false;
		String sql = "SELECT STOCK_IN_HAND FROM MERCHANT_INVENTORY WHERE MERCHANT_ID =" + merchantId + " AND CARD_TYPE='" + cardType + "' AND DENOMINATION=" + denomination + " ";
		Cursor cursor = null;
		try {
			 cursor = db.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				isExist = true;
			} else {
				isExist = false;
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db.close();
		}
		return isExist;
	}

	public boolean CheckIsCategoryIdExistsByMerchantIdAndItemAndDenomination(int merchantId, String cardType, int denomination) {

		SQLiteDatabase db = this.getReadableDatabase();

		boolean isExist = false;
		String sql = "SELECT B.STOCK_IN_HAND FROM MERCHANT_INVENTORY A,MERCHANT_INVENTORY_DETAILS B WHERE A.MERCHANT_ID = " + merchantId + " AND B.CARD_TYPE = '" + cardType + "' AND B.DENOMINATION =" + denomination + " AND A.INVENTORY_ID = B.INVENTORY_ID ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			isExist = true;
		} else {
			isExist = false;
		}
		cursor.close();
		db.close();

		return isExist;
	}

	public int GetSalesQtyBycardTypeAndDenominationFromMerchantInventory(String cardType, Long merchantId, int denomination) {
		int salesQty = 0;
		String sql = "SELECT SALE_COUNT FROM  MERCHANT_INVENTORY  WHERE MERCHANT_ID =" + merchantId + " AND CARD_TYPE ='" + cardType + "' AND DENOMINATION =" + denomination + "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			 cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				salesQty = cursor.getInt(0);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor!= null){
				cursor.close();
			}
			db.close();
		}


		return salesQty;
	}

	public int GetQuantityBycardTypeAndDenominationFromMerchantInventory(String cardType, Long merchantId, int denomination) {
		int quantity = 0;
		Cursor cursor = null;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			String sql = "SELECT STOCK_IN_HAND FROM  MERCHANT_INVENTORY  WHERE MERCHANT_ID =" + merchantId + " AND CARD_TYPE ='" + cardType + "' AND DENOMINATION =" + denomination + "";

			 cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				quantity = cursor.getInt(0);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db.close();
		}

		return quantity;
	}

	public void updateStocksInHandByMerchantIdAndCategoryId(int merchantId, String cardType, int denomination, int stocksInHand) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE MERCHANT_INVENTORY SET STOCK_IN_HAND =" + stocksInHand + " WHERE  CARD_TYPE = '" + cardType + "' AND DENOMINATION =" + denomination + " AND INVENTORY_ID = (SELECT A.INVENTORY_ID FROM MERCHANT_INVENTORY A,MERCHANT_INVENTORY B WHERE A.INVENTORY_ID = B.INVENTORY_ID AND B.MERCHANT_ID = " + merchantId + " )  ";
		db.execSQL(sql);
		db.close();
	}

	public int GetmaxMerchantInventoryDetailNoByInventoryDetailId(int inventoryId) {
		SQLiteDatabase db = this.getReadableDatabase();

		int id = 0;
		String sql = "SELECT MAX(INVENTORY_DETAILS_ID) FROM MERCHANT_INVENTORY WHERE INVENTORY_ID = " + inventoryId + "  ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return id;
	}

	public void SaveMerchantInventoryMasterDetails(int inventoryId, String date, int merchantId, String epfNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO MERCHANT_INVENTORY(INVENTORY_ID,INVENTORY_UPDATE_DATE,IS_REORDER_NOW,MERCHANT_ID,EPF_NO) VALUES (" + inventoryId + "," + date + ",'0'," + merchantId + ",'" + epfNo + "')";
		db.execSQL(sql);
		db.close();
	}

	public void SaveMerchantInventoryDetails(int inventoryId, int inventoryDetailId, String cardType, int denomination, int stocksInHand, String date, int reorderLevel) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO MERCHANT_INVENTORY_DETAILS(INVENTORY_ID,INVENTORY_DETAILS_ID,CARD_TYPE,DENOMINATION,STOCK_IN_HAND,STOCK_COUNTED_DATE,REORDER_LEVEL) VALUES (" + inventoryId + "," + inventoryDetailId + ",'" + cardType + "'," + denomination + "," + stocksInHand + ",'" + date + "'," + reorderLevel + ")";
		db.execSQL(sql);
		db.close();
	}

	public void SaveMerchantMasterDetails(Long merchantId, String merchantName, String merchantAddress, String merchantContact, String city) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "update MERCHANT_MASTER set REGISTERED_ON = '', IS_ASSIGNED='1',IS_SYNCH='0' where merchant_id='" + merchantId + "'";
		System.out.println("* update "+sql);
		db.execSQL(sql);
		db.close();
	}

	public int GetInventoryIdByMerchantId(int merchantId) {

		int inventoryId = 0;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT INVENTORY_ID FROM MERCHANT_INVENTORY WHERE MERCHANT_ID =" + merchantId + " ", null);
		if (cursor.moveToNext()) {

			inventoryId = cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return inventoryId;
	}

	public ArrayList<bulkId> getBulkNoList() {

		ArrayList<bulkId> list = new ArrayList<bulkId>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT CARD_TYPE,DENOMINATION,BULK_NO,START_SERIAL,END_SERIAL	 FROM TSR_RECEIVED_CARD_BUNDLES WHERE IS_ACCEPTED ='0' ", null);
			while (cursor.moveToNext()) {

				list.add(new bulkId(cursor.getString(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));

			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db.close();
		}

		return list;
	}

	public ArrayList<Merchant> getUpdatedMerchantDataListFromTempUpdatedDataTable() {
		ArrayList<Merchant> list = new ArrayList<Merchant>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT MERCHANT_ID,MERCHANT_NAME,ADDRESS,TELEPHONE_NO,CITY,RELOAD_NUMBER FROM MERCHANT_MASTER where IS_SYNCH='0'", null);
		while (cursor.moveToNext()) {
			list.add(new Merchant(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), 0, cursor.getString(4), "", "", 0, 0, cursor.getString(5)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public ArrayList<Merchant> getUpdatedMerchantDataListFromMerchantMasterTable() {
		ArrayList<Merchant> list = new ArrayList<Merchant>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT MERCHANT_ID,MERCHANT_NAME,ADDRESS,TELEPHONE_NO,CITY FROM MERCHANT_MASTER WHERE IS_EDITED ='1' AND IS_SYNCH='0'  ", null);
		while (cursor.moveToNext()) {
			list.add(new Merchant(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), 0, cursor.getString(4), "", "", 0, 0, ""));
		}
		cursor.close();
		db.close();
		return list;
	}

	public ArrayList<Remarks> getUpdatedRemarksList() {
		ArrayList<Remarks> list = new ArrayList<Remarks>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT REMARKS_ID,MERCHANT_ID,REMARKS,LATITUDE,LONGITUDE,VISIT_DATE FROM TSR_VISIT_REMARKS  WHERE ISUPDATED = 1 ", null);
			while (cursor.moveToNext()) {
				list.add(new Remarks(cursor.getInt(0), cursor.getLong(1), cursor.getString(2), true, cursor.getString(3), cursor.getString(4), cursor.getString(5)));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db.close();
		}

		return list;
	}

	public ArrayList<ItemSale> getUpdatedItemSaleList() {
		ArrayList<ItemSale> list = new ArrayList<ItemSale>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT INVOICE_ID,MERCHANT_ID,LATITUDE,LONGITUDE,INVOICE_TOTAL,ENTERED_DATE,ENTERED_BY,TOTAL_DISCOUNT FROM SALES_HEADER WHERE IS_SYNCH='0' AND ISUPDATED='1' ", null);
		while (cursor.moveToNext()) {
			list.add(new ItemSale(cursor.getLong(0), cursor.getLong(1), cursor.getString(3), cursor.getString(2), 0, cursor.getDouble(4), true, cursor.getString(5)));
		}
		cursor.close();
		db.close();

		return list;
	}

	public ArrayList<SalesHeaders> getSalesHeaders() {// this sales header data
														// list going to synch
														// with oracle when
														// background service is
														// run
		ArrayList<SalesHeaders> list = new ArrayList<SalesHeaders>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT INVOICE_ID,MERCHANT_ID,LATITUDE,LONGITUDE,INVOICE_TOTAL,ENTERED_DATE,ENTERED_BY,TOTAL_DISCOUNT FROM SALES_HEADER WHERE IS_SYNCH='0' AND ISUPDATED='1' ", null);
			while (cursor.moveToNext()) {
				// list.add(new SalesHeaders(cursor.getInt(0), cursor.getInt(1),
				// cursor.getString(3), cursor.getString(2), 0, cursor.getDouble(4),
				// cursor.getString(5)));
				list.add(new SalesHeaders(cursor.getLong(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getString(5), cursor.getInt(6), cursor.getDouble(7)));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db.close();

		}
		return list;
	}

	public ArrayList<SalesDetails> getUpdatedItemSalesDetailsList() {
		ArrayList<SalesDetails> list = new ArrayList<SalesDetails>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT DETAILS_ID,CARD_TYPE,DENOMINATION,BULK_NO,NO_OF_CARDS,START_SERIAL,LINE_AMOUNT,LINE_DISCOUNT,INVOICE_ID FROM SALES_DETAILS WHERE IS_SYNCH='0' ", null);
		while (cursor.moveToNext()) {
			list.add(new SalesDetails(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getDouble(6), cursor.getDouble(7), cursor.getLong(8)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public ArrayList<ItemSalesDetails> getUpdatedItemSalesDetailsList(Long invoiceId) {
		ArrayList<ItemSalesDetails> list = new ArrayList<ItemSalesDetails>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT DETAILS_ID,CARD_TYPE,DENOMINATION,BULK_NO,NO_OF_CARDS,START_SERIAL,END_SERIAL,LINE_AMOUNT,LINE_DISCOUNT FROM SALES_DETAILS WHERE IS_SYNCH='0' AND INVOICE_ID =" + invoiceId + " ", null);
		while (cursor.moveToNext()) {
			list.add(new ItemSalesDetails(invoiceId, cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4), cursor.getDouble(7), cursor.getString(5), cursor.getInt(0), cursor.getDouble(8)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public User getUserDetails() {
		User user = null;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		try {
			 cursor = db.rawQuery("SELECT EPF_NO,USER_NAME,PASSWORD,MOBILE_NO FROM TSR_LOGIN", null);
			if (cursor.moveToFirst()) {
				user = new User("", cursor.getInt(0), cursor.getString(2), 0, cursor.getString(3), cursor.getString(1), null, 0);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(cursor!= null){
				cursor.close();
			}
			db.close();
		}
		return user;
	}

	public Merchant GetMerchantByMerchantId(Long merchantId) {

		Merchant merchant = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			 cursor = db.rawQuery("SELECT MERCHANT_NAME,ADDRESS,TELEPHONE_NO,CITY,RELOAD_NUMBER FROM MERCHANT_MASTER WHERE MERCHANT_ID =" + merchantId + " ", null);
			while (cursor.moveToNext()) {
				merchant = new Merchant(merchantId, cursor.getString(0), cursor.getString(1), cursor.getString(2), 0, cursor.getString(3), "", "", 0, 1, cursor.getString(4));
				// Merchant(merchantId ,name ,address , phone
				// ,postalCode,city,latitude,longitude,distance,isRegistred,reloadNo)
				// {
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db.close();
		}


		return merchant;
	}

	public Merchant GetMerchantByMerchantIdInOffline(Long merchantId) {

		Merchant merchant = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT MERCHANT_NAME,ADDRESS,TELEPHONE_NO,CITY FROM MERCHANT_TEMP_DATA_TABLE WHERE MERCHANT_ID =" + merchantId + " ", null);
		while (cursor.moveToNext()) {
			merchant = new Merchant(merchantId, cursor.getString(0), cursor.getString(1), cursor.getString(2), 0, cursor.getString(3), "", "", 0, 1, "");
		}
		cursor.close();
		db.close();

		return merchant;
	}

	public Boolean checkBulkNoSelectedByCategoryId(int categoryId) {
		boolean isExist = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT IS_SELECT FROM TSR_RECEIVED_CARD_BUNDLES WHERE CATEGORY_ID =" + categoryId + " AND IS_ACCEPTED ='1' ", null);
		while (cursor.moveToNext()) {
			if (cursor.getInt(0) == 1) {
				isExist = true;
			}
		}
		cursor.close();
		db.close();
		return isExist;
	}

	public int getBulkNoByCategoryId(int categoryId) {

		int bulkNo = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT BULK_NO FROM TSR_RECEIVED_CARD_BUNDLES WHERE CATEGORY_ID =" + categoryId + " AND IS_ACCEPTED ='1' AND  IS_SELECT ='1' ", null);
		while (cursor.moveToNext()) {

			bulkNo = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return bulkNo;
	}

	public int getendSerialByBulkId(String cardType, int denomination, String bulkNo) {

		int endSerial = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT END_SERIAL FROM TSR_RECEIVED_CARD_BUNDLES WHERE CARD_TYPE='" + cardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + bulkNo + "'", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			endSerial = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return endSerial;
	}

	public int getStartSerialByBulkId(String cardType, int denomination, String bulkNo) {

		int startSerial = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT START_SERIAL FROM TSR_RECEIVED_CARD_BUNDLES WHERE CARD_TYPE='" + cardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + bulkNo + "'", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			startSerial = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return startSerial;
	}

	public boolean checkAcceptedRemainingQtyExists(int categoryId) {

		boolean IsExist = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT BULK_NO FROM TSR_RECEIVED_CARD_BUNDLES WHERE CATEGORY_ID =" + categoryId + " AND IS_ACCEPTED ='1' AND  IS_SELECT ='0' ", null);

		if (cursor.getCount() > 0) {
			IsExist = true;
		}
		cursor.close();
		db.close();

		return IsExist;
	}

	public String GetReloadNumberByMerchantId(int merchantId) {

		String reload_number = "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT RELOAD_NUMBER FROM MERCHANT_TEMP_DATA_TABLE WHERE MERCHANT_ID = " + merchantId + " ", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			reload_number = cursor.getString(0);

		}
		cursor.close();
		db.close();

		return reload_number;
	}

	public void updateSelectedBulkId(int categoryId, int bulkId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_RECEIVED_CARD_BUNDLES SET IS_SELECT ='1' WHERE CATEGORY_ID=" + categoryId + " AND BULK_NO = " + bulkId + " ");
		db.close();
	}

	public void updateaLLBulkIdByCategoryId(int categoryId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_RECEIVED_CARD_BUNDLES SET IS_SELECT ='0' WHERE CATEGORY_ID=" + categoryId + " AND IS_ACCEPTED = '1' ");
		db.close();
	}

	public void SaveLoginData(String epfno, String username, String password, String mobileNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO  TSR_LOGIN (EPF_NO,USER_NAME,PASSWORD,MOBILE_NO) VALUES('" + epfno + "','" + username + "','" + password + "','" + mobileNo + "') ");
		db.close();
	}

	/*
	 * public void SaveGPSData(String longt, String latt, String currentdate) {
	 * SQLiteDatabase db = this.getWritableDatabase(); Date date3 = new Date();
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	 * db.execSQL
	 * ("insert into LOCATION_TRACK(LATT ,LONGT ,IS_SYNCH,UPDATED_DATE) values('"
	 * + latt + "','" + longt + "',0,'"+sdf.format(date3)+"')"); // date('now')
	 * db.close(); }
	 */
	public void SaveGPSData(String Accuracy, String Speed, String UseTime, String Provider, String latt, String longt, String currentdate) {
		SQLiteDatabase db = this.getWritableDatabase();
		// Date date3 = new Date();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		db.execSQL("insert into LOCATION_TRACK(Accuracy,Speed,UseTime,Provider,LATT,LONGT,IS_SYNCH,UPDATED_DATE) values " + "('" + Accuracy + "','" + Speed + "','" + UseTime + "','" + Provider + "','" + latt + "','" + longt + "',0,'" + currentdate + "')"); // date('now')
		db.close();
	}

	public void SaveGPSErrorData(String Accuracy, String Speed, String UseTime, String Provider, String latt, String longt, String currentdate) {
		SQLiteDatabase db = this.getWritableDatabase();
		Date date3 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		db.execSQL("insert into LOCATION_TRACK_ERROR(Accuracy,Speed,UseTime,Provider,LATT ,LONGT ,IS_SYNCH,UPDATED_DATE) values" + "('" + Accuracy + "','" + Speed + "','" + UseTime + "','" + Provider + "','" + latt + "','" + longt + "',0,'" + sdf.format(date3) + "')"); // date('now')
		db.close();
	}


	public void UpdateLoginData(String SimNo, String username, String password,int y) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_LOGIN SET USER_NAME ='" + username + "',PASSWORD = '" + password + "'  WHERE MOBILE_NO ='" + SimNo + "' ");
		db.close();
	}
	public void UpdateLoginData(String epfno, String username, String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_LOGIN SET USER_NAME ='" + username + "',PASSWORD = '" + password + "'  WHERE EPF_NO ='" + epfno + "' ");
		db.close();
	}

	public void SaveCity(String postalCode, String cityselect) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO  CITY_POSTAL_CODES (POSTAL_CODE,CITY,POST_OFFICE_NAME) VALUES('" + postalCode + "','" + cityselect + "','" + cityselect + "')");
		db.close();
	}

	public void SaveCityData(String postalCode, String city, String region, String latitude, String longitude, String postOfficeName, String Area, String currentChanges) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO  CITY_POSTAL_CODES (POSTAL_CODE,CITY,REGION,LATITUDE,LONGITUDE,POST_OFFICE_NAME,AREA,CHANGES,MAX_CHANGES,IS_SYNCH) VALUES('" + postalCode + "','" + city + "','" + region + "','" + latitude + "','" + longitude + "','" + postOfficeName + "','" + Area + "','" + currentChanges + "','5',1)");
		db.close();
	}

	public void SaveCityData(String postalCode, String city, String region, String latitude, String longitude, String postOfficeName, String Area) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO  CITY_POSTAL_CODES (POSTAL_CODE,CITY,REGION,LATITUDE,LONGITUDE,POST_OFFICE_NAME,AREA,CHANGES,MAX_CHANGES,IS_SYNCH) VALUES('" + postalCode + "','" + city + "','" + region + "','" + latitude + "','" + longitude + "','" + postOfficeName + "','" + Area + "','0','5',1)");
		db.close();
	}

	public void SaveCardTypes(String typeCode, String typeDescription, String cardStatus, String accountCode) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO CARD_TYPES (TYPE_CODE,TYPE_DESCRIPTION,CARD_STATUS,ACCOUNT_CODE) VALUES ('" + typeCode + "','" + typeDescription + "','" + cardStatus + "','" + accountCode + "')");
		db.close();
	}

	public void SaveCardDenominations(String cardType, String denomination, double realvalue, double discountValue) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO CARD_DENOMINATOIN(CARD_TYPE,DENOMINATION,REAL_VALUE,DISCOUNT_VALUE) VALUES('" + cardType + "'," + denomination + "," + realvalue + "," + discountValue + ")");
		db.close();
	}

	public void SaveMerchantDetails(Long merchantId, String name, String address, String lat, String lon, String registeredOn, String telephone, String city, int IsActive, String RegisteredByEpf, int isRegistered, int isAssigned, int isEdited, String reloadNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO MERCHANT_MASTER (MERCHANT_ID,MERCHANT_NAME,ADDRESS,LATITUDE,LONGITUDE,REGISTERED_ON,TELEPHONE_NO,CITY,IS_ACTIVE,REGISTERED_BY_EPF_NO,IS_REGISTERED,IS_ASSIGNED,IS_EDITED,IS_SYNCH,RELOAD_NUMBER,IS_UPDATED) VALUES(" + merchantId + ",'" + name + "','" + address + "','" + lat + "','" + lon + "','" + registeredOn + "','" + telephone + "','" + city + "'," + IsActive + ",'" + RegisteredByEpf + "'," + isRegistered + "," + isAssigned + "," + isEdited + ",'1'," + reloadNo + ",'1')";
		db.execSQL(sql);
		db.close();
	}

	public void SaveSalesDetailsTemp(String cardType, int denomination, String bulkNo, int qty, String startSerial, String endSerial, Double lineAmount, Double linediscount, Double discountRate, String listStartSerial) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO SALES_TEMP_DETAILS(CARD_TYPE,DENOMINATION,BULK_NO,NO_OF_CARDS,START_SERIAL,END_SERIAL,LINE_AMOUNT,LINE_DISCOUNT,DISCOUNT_RATE,listStartSerial) VALUES('" + cardType + "'," + denomination + ",'" + bulkNo + "'," + qty + ",'" + startSerial + "','" + endSerial + "'," + lineAmount + "," + linediscount + "," + discountRate + " ,'" + listStartSerial + "')";
		db.execSQL(sql);
		db.close();
	}

	public void DeleteSalesDetailsTemp(String cardType, int denomination, String bulkNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "DELETE FROM SALES_TEMP_DETAILS WHERE CARD_TYPE ='" + cardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + bulkNo + "')";
		db.execSQL(sql);
		db.close();
	}

	public void ClearTempSalesDetails() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM SALES_TEMP_DETAILS");
		db.close();
	}

	public void updateAcceptedBulkIds(String cardType, int denomination, String bulkId, int STARTSERIAL, int ENDSERIAL) {
		SQLiteDatabase db = this.getWritableDatabase();
		int value = 1;
		db.execSQL("UPDATE TSR_RECEIVED_CARD_BUNDLES SET IS_SYNCH='0', IS_ACCEPTED =" + value + " WHERE CARD_TYPE='" + cardType + "' AND DENOMINATION = " + denomination + " AND BULK_NO ='" + bulkId + "' AND START_SERIAL =" + STARTSERIAL + " AND END_SERIAL=" + ENDSERIAL + " ");
		db.close();
	}

	public void isAcceptUpdate(String cardType, int denomination, String bulkId, int STARTSERIAL, int ENDSERIAL) {
		SQLiteDatabase db = this.getWritableDatabase();
		int value = 1;
		db.execSQL("UPDATE TSR_RECEIVED_CARD_BUNDLES SET IS_ACCEPT_UPDATE =" + value + " WHERE CARD_TYPE='" + cardType + "' AND DENOMINATION = " + denomination + " AND BULK_NO ='" + bulkId + "' AND START_SERIAL =" + STARTSERIAL + " AND END_SERIAL=" + ENDSERIAL + " ");
		db.close();
	}

	public void DeleteAcceptCardSerial(String cardType, int denomination, String bulkId, int STARTSERIAL, int ENDSERIAL) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM TSR_RECEIVED_CARD_BUNDLES WHERE IS_ACCEPTED ='0' AND CARD_TYPE='" + cardType + "' AND DENOMINATION = " + denomination + " AND BULK_NO ='" + bulkId + "' AND START_SERIAL =" + STARTSERIAL + " AND END_SERIAL=" + ENDSERIAL + " ");
		db.close();
	}

	public void SaveBulkIds(String cardType, int denomination, String bulkId, String startSerial, String endSerial, String date, Double discountRate, int isAccepted, int noOfCards) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO TSR_RECEIVED_CARD_BUNDLES(CARD_TYPE,DENOMINATION,BULK_NO,START_SERIAL,END_SERIAL,ENTRY_DATE,DISCOUNT_RATE,IS_ACCEPTED,IS_SYNCH,NO_OF_CARDS,IS_ALL_SOLD) VALUES('" + cardType + "'," + denomination + ",'" + bulkId + "','" + startSerial + "','" + endSerial + "','" + date + "'," + discountRate + "," + isAccepted + ",1,'" + noOfCards + "','0')";
		db.execSQL(sql);
		db.close();
	}

	public void DeleteFromTempList(String cardType, int denomination, int qty) {
		String sql = "DELETE FROM SALES_TEMP_DETAILS WHERE CARD_TYPE= '" + cardType + "' AND DENOMINATION=" + denomination + " AND NO_OF_CARDS=" + qty + " ";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}
	public void DeletelOGOUT() {
		String sql = "DELETE FROM LOGOUT";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}

	public void updateIsAssignedUpdatedMerchantTempTable(int merchantid, int value) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE MERCHANT_TEMP_DATA_TABLE SET IS_ASSIGNED_UPDATED = " + value + " WHERE MERCHANT_ID = " + merchantid + " ");
		db.close();
	}

	public void updateUpdatedCityTempTable(int merchantid, int value) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE MERCHANT_TEMP_DATA_TABLE SET IS_UPDATED = " + value + " WHERE MERCHANT_ID = " + merchantid + " ");
		db.close();
	}

	public void updateUpdatedMerchantTempTable(int merchantid, int value) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE MERCHANT_TEMP_DATA_TABLE SET IS_UPDATED = " + value + " WHERE MERCHANT_ID = " + merchantid + " ");
		db.close();
	}

	public void updateUpdatedMerchantFromMerchantMasterById(int merchantid, int value) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE MERCHANT_MASTER SET IS_SYNCH='1', IS_EDITED = " + value + " WHERE MERCHANT_ID = " + merchantid + " ");
		db.close();
	}

	public void DeleteUpdatedMerchantTempTable(int merchantid) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM MERCHANT_TEMP_DATA_TABLE WHERE MERCHANT_ID =" + merchantid + " ");
		db.close();
	}

	public void updateUpdatedMerchantsAssignTempTable(int merchantid) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE MERCHANT_TEMP_DATA_TABLE SET IS_ASSIGNED = '1' AND IS_ASSIGNED_UPDATED ='0' WHERE MERCHANT_ID = " + merchantid + " ");
		db.close();
	}

	public void UpdateRemarksTable(int RemarksId, int value) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_VISIT_REMARKS SET ISUPDATED = " + value + " WHERE REMARKS_ID = " + RemarksId + " ");
		db.close();
	}

	public void UpdateRemarksTable(int value) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_VISIT_REMARKS SET ISUPDATED = '" + value + "' WHERE ISUPDATED = 1 ");
		db.close();
	}

	public void UpdateSalesHeaderTable(Long invoiceId, int value) {// when card
																	// sale;
																	// after
																	// header
																	// insert;
																	// after
																	// detail
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE SALES_HEADER SET ISUPDATED = " + value + " WHERE INVOICE_ID = " + invoiceId + " ");
		db.close();
	}

	public void UpdateSalesHeaderTable(SalesResponse salesResponse) {
		SQLiteDatabase db = this.getWritableDatabase();

		if (salesResponse.getRespons() == 1) {
			db.execSQL("UPDATE SALES_HEADER SET IS_SYNCH = '1' where INVOICE_ID = '" + salesResponse.getInvoiceId() + "' ");
			db.execSQL("UPDATE SALES_DETAILS SET IS_SYNCH = '1' where INVOICE_ID = '" + salesResponse.getInvoiceId() + "' ");
			db.close();
		}

	}

	public void UpdateSalesHeaderTable(Long invoiceId, int value, int isSynch) {// when
																				// sales
																				// synch
																				// with
																				// oracle
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE SALES_HEADER SET IS_SYNCH='1',ISUPDATED = " + value + " WHERE INVOICE_ID = " + invoiceId + " ");

		db.execSQL("UPDATE SALES_DETAILS SET IS_SYNCH='1' WHERE INVOICE_ID = " + invoiceId + " ");

		db.close();
	}

	public void SaveMerchantInventoryTableAtSale(Long merchantid, String cardType, int denomination, int qty, String date, int reorderLevel) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO MERCHANT_INVENTORY(MERCHANT_ID,CARD_TYPE,DENOMINATION,STOCK_IN_HAND,STOCK_COUNTED_DATE,REORDER_LEVEL,IS_SYNCH,SALE_COUNT) VALUES(" + merchantid + ",'" + cardType + "'," + denomination + "," + qty + ",'" + date + "'," + reorderLevel + ",'0','" + qty + "')";
		db.execSQL(sql);
		db.close();
	}

	public void SaveMerchantInventoryTable(int merchantid, String cardType, int denomination, int qty, String date, int reorderLevel) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO MERCHANT_INVENTORY(MERCHANT_ID,CARD_TYPE,DENOMINATION,STOCK_IN_HAND,STOCK_COUNTED_DATE,REORDER_LEVEL,IS_SYNCH) VALUES(" + merchantid + ",'" + cardType + "'," + denomination + "," + qty + ",'" + date + "'," + reorderLevel + ",'1')";
		db.execSQL(sql);
		db.close();
	}

	public void SaveMerchantInventoryTableAtLogin(Long merchantid, String cardType, int denomination, int qty, String date, int reorderLevel, int activationCount, int saleCount) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO MERCHANT_INVENTORY(MERCHANT_ID,CARD_TYPE,DENOMINATION,STOCK_IN_HAND,STOCK_COUNTED_DATE,REORDER_LEVEL,IS_SYNCH,ACTIVATION_COUNT,SALE_COUNT) VALUES(" + merchantid + ",'" + cardType + "'," + denomination + "," + qty + ",'" + date + "'," + reorderLevel + ",'1','" + activationCount + "','" + saleCount + "')";
		db.execSQL(sql);
		db.close();
	}

	public void UpdateMerchantInventoryTable(int merchantid, String cardType, int denomination, int qty, String date, int reorderLevel) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE MERCHANT_INVENTORY SET IS_SYNCH='1',STOCK_IN_HAND =" + qty + ",REORDER_LEVEL =" + reorderLevel + ",STOCK_COUNTED_DATE ='" + date + "' WHERE MERCHANT_ID = " + merchantid + " AND CARD_TYPE='" + cardType + "' AND DENOMINATION= " + denomination + " ";
		db.execSQL(sql);
		db.close();
	}

	public void UpdateMerchantInventoryTable(int merchantid, String cardType, int denomination, int qty, int reorderLevel) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE MERCHANT_INVENTORY SET IS_SYNCH='1',IS_UPDATED='1', STOCK_IN_HAND =" + qty + ",REORDER_LEVEL =" + reorderLevel + " " + "WHERE MERCHANT_ID = " + merchantid + " AND CARD_TYPE='" + cardType + "' AND DENOMINATION= " + denomination + " ";
		db.execSQL(sql);
		db.close();
	}

	public void UpdateMerchantInventoryQty(Long merchantid, String cardType, int denomination, int qty, String date, Integer salesQty) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE MERCHANT_INVENTORY SET IS_SYNCH='0',STOCK_IN_HAND =" + qty + ",SALE_COUNT='" + salesQty + "',STOCK_COUNTED_DATE ='" + date + "' WHERE MERCHANT_ID = " + merchantid + " AND CARD_TYPE='" + cardType + "' AND DENOMINATION= " + denomination + " ";
		db.execSQL(sql);
		db.close();
	}

	public void SaveUpdatedCities(int merchantId, String name, String city) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO MERCHANT_TEMP_DATA_TABLE(MERCHANT_ID,MERCHANT_NAME,ADDRESS,TELEPHONE_NO,CITY,IS_ASSIGNED,IS_ASSIGNED_UPDATED,RELOAD_NUMBER) VALUES(" + merchantId + ",'" + name + "','" + "','" + "','" + city + "','0','0','" + "')");
		db.close();
	}

	public void SaveUpdatedMerchants(Long merchantId, String name, String address, String telephone, String city, String reloadNumber, String epfNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {

			db.execSQL("INSERT INTO MERCHANT_MASTER (MERCHANT_ID,MERCHANT_NAME,ADDRESS,LATITUDE,LONGITUDE,REGISTERED_ON,TELEPHONE_NO,CITY,IS_ACTIVE,REGISTERED_BY_EPF_NO,IS_REGISTERED,IS_SYNCH,RELOAD_NUMBER,is_assigned,IS_EDITED,IS_UPDATED) " + "VALUES(" + merchantId + ",'" + name + "','" + address + "','0','0','','" + telephone + "','" + city + "','','" + epfNo + "','0','0','" + reloadNumber + "','0','0','0')");
		} catch (Exception e) {
			Toast.makeText(context, "Error occured. Please do a data reload.", Toast.LENGTH_LONG).show();
			Utils.writeToErrLogFileWithTime(e);
		} finally {
			db.close();
		}

		// SQLiteDatabase db = this.getWritableDatabase();
		// db.execSQL("INSERT INTO MERCHANT_TEMP_DATA_TABLE(MERCHANT_ID,MERCHANT_NAME,ADDRESS,TELEPHONE_NO,CITY,IS_ASSIGNED,IS_ASSIGNED_UPDATED,RELOAD_NUMBER) VALUES("+
		// merchantId+ ",'"+ name+ "','"+ address+ "','"+ telephone+ "','"+
		// city+ "','0','0','"+ reloadNumber+ "')");
		// db.close();
	}

	public void UpdateMerchantMasterTableById(Long merchantId, String name, String address, String telephone, String city, String reloadNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE	MERCHANT_MASTER SET RELOAD_NUMBER='" + reloadNo + "',MERCHANT_NAME = '" + name + "',ADDRESS ='" + address + "',TELEPHONE_NO = '" + telephone + "',CITY ='" + city + "',IS_EDITED ='1',IS_SYNCH = '0' WHERE MERCHANT_ID =" + merchantId + "";
		db.execSQL(sql);
		db.close();
	}

	/*
	 * public void SaveLastMerchantNo(int merchantNo) { SQLiteDatabase db =
	 * this.getWritableDatabase();
	 * db.execSQL("INSERT INTO TSR_SYSTEM_PROFILE (NEXT_MERCHANT_NO) VALUES(" +
	 * merchantNo + ")"); db.close(); }
	 */
	/*
	 * public void SaveLastInvoiceNo(int invoiceNo) { SQLiteDatabase db =
	 * this.getWritableDatabase();
	 * db.execSQL("INSERT INTO TSR_SYSTEM_PROFILE (NEXT_INVOICE_NO) VALUES(" +
	 * invoiceNo + ")"); db.close(); }
	 */

	public void UpdateLastMerchantNo(int merchantNo, String Epf) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_SYSTEM_PROFILE SET IS_SYNCH = '0', NEXT_MERCHANT_NO = " + merchantNo + " ");
		db.close();
	}

	public void UpdateLastInvoiceNo(int invoiceNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_SYSTEM_PROFILE SET IS_SYNCH = '0', NEXT_INVOICE_NO = " + invoiceNo + " ");
		db.close();
	}

	public void UpdateTempTableMerchantId(int merchantid, int newMerchantId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE MERCHANT_TEMP_DATA_TABLE SET MERCHANT_ID = " + newMerchantId + " WHERE MERCHANT_ID = " + merchantid + " ");
		db.close();
	}

	public void UpdateRemarksMerchantId(int merchantId, int newMerchantId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_VISIT_REMARKS SET MERCHANT_ID =" + newMerchantId + " WHERE MERCHANT_ID = " + merchantId + " ");
		db.close();
	}

	public void UpdateSalesHeaderMerchantId(int merchantId, int newMerchantId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE SALES_HEADER SET MERCHANT_ID =" + newMerchantId + " WHERE MERCHANT_ID =" + merchantId + " ");
		db.close();
	}

	public void UpdateMerchantInventoryMerchantId(int merchantId, int newMerchantId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE MERCHANT_INVENTORY SET MERCHANT_ID =" + newMerchantId + " WHERE MERCHANT_ID =" + merchantId + " ";
		db.execSQL(sql);
		db.close();
	}

	public void UpdateMerchantMasterMerchantId(int merchantId, int newMerchantId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE MERCHANT_MASTER SET IS_SYNCH='1', MERCHANT_ID=" + newMerchantId + " WHERE MERCHANT_ID = " + merchantId + " ");
		db.close();
	}

	public void UpdateCardAcceptResponse(String CardType, int denomination, String bulkId, int StartSerial, int endSerial) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_RECEIVED_CARD_BUNDLES SET IS_SYNCH='1' WHERE CARD_TYPE='" + CardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + bulkId + "' AND START_SERIAL =" + StartSerial + " AND END_SERIAL =" + endSerial + " ");
		db.close();
	}

	/***************************************/

	public boolean Exists(String _start, String _end) {
		SQLiteDatabase db = this.getReadableDatabase();
		// SQLiteDatabase db = null;
		Cursor cursor = db.rawQuery("SELECT START_SERIAL,END_SERIAL from TSR_RECEIVED_CARD_BUNDLES", new String[] { _start, _end });
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		return exists;
	}

	public boolean SerialExists(int start) {

		boolean IsExist = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT START_SERIAL FROM TSR_RECEIVED_CARD_BUNDLES WHERE START_SERIAL =" + start/*
																													 * +
																													 * " AND IS_ACCEPTED ='1' AND  IS_SELECT ='0' "
																													 */, null);

		if (cursor.getCount() > 0) {
			IsExist = true;
		}
		cursor.close();
		db.close();

		return IsExist;
	}

	public boolean CheckbulkIdIsExistTempDataTable(String cardType, String bulkNo, int denomination) {
		String sql = "SELECT END_SERIAL FROM SALES_TEMP_DETAILS WHERE CARD_TYPE='" + cardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + bulkNo + "'";
		boolean IsExist = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.getCount() > 0) {
			IsExist = true;
		}
		cursor.close();
		db.close();

		return IsExist;
	}

	public boolean CheckCardBulkRecordIsExists(String cardType, int denomination, String bulkId, String startSerial, String endSerial) {
		boolean IsExist = false;
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT START_SERIAL FROM TSR_RECEIVED_CARD_BUNDLES WHERE CARD_TYPE ='" + cardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + bulkId + "' AND START_SERIAL ='" + startSerial + "' AND END_SERIAL ='" + endSerial + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			IsExist = true;
		}
		cursor.close();
		db.close();
		return IsExist;
	}

	public String GetSerialNumberFromTempTable(String cardType, String bulkNo, int denomination) {

		String serialNo = "";
		String sql = "SELECT END_SERIAL FROM SALES_TEMP_DETAILS WHERE CARD_TYPE='" + cardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO ='" + bulkNo + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {

			serialNo = cursor.getString(0);
		}
		cursor.close();
		db.close();

		return serialNo;
	}

	public void Serialrange() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("SELECT START_SERIAL,END_SERIAL FROM TSR_RECEIVED_CARD_BUNDLES WHERE BULK_NO BETWEEN START_SERIAL AND END_SERIAL", null);

		db.close();
	}

	public boolean CheckCardSalesDataIsExist() {

		boolean IsExist = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT INVOICE_ID FROM SALES_HEADER WHERE ISUPDATED = 1", null);

		if (cursor.getCount() > 0) {
			IsExist = true;
		}
		cursor.close();
		db.close();

		return IsExist;
	}

	public boolean CheckRemarksDataIsExist() {

		boolean IsExist = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT REMARKS_ID FROM TSR_VISIT_REMARKS WHERE ISUPDATED =1", null);

		if (cursor.getCount() > 0) {
			IsExist = true;
		}
		cursor.close();
		db.close();

		return IsExist;
	}

	public boolean CheckAcceptBulkIDsExists(String cardType, int denomination, String bulkId, int startSetrial) {

		boolean IsExist = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT ENTRY_DATE FROM TSR_RECEIVED_CARD_BUNDLES WHERE CARD_TYPE ='" + cardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO = " + bulkId + " AND START_SERIAL =" + startSetrial + " AND IS_ACCEPTED ='1'", null);

		if (cursor.getCount() > 0) {
			IsExist = true;
		}
		cursor.close();
		db.close();

		return IsExist;
	}

	public void UpdateAcceptCardSerial(String cardType, int denomination, String bulkNo, int startSerial, int endSerial) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE 	TSR_RECEIVED_CARD_BUNDLES SET IS_SYNCH='0' ,CARD_RESPONSE ='1' WHERE CARD_TYPE ='" + cardType + "' AND DENOMINATION =" + denomination + " AND BULK_NO='" + bulkNo + "' AND START_SERIAL =" + startSerial + " AND END_SERIAL=" + endSerial + "  ");
		db.close();
	}

	public ArrayList<MerchantInventorySynch> GetNonSynchMerchantInventory() {
		ArrayList<MerchantInventorySynch> list = new ArrayList<MerchantInventorySynch>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = " select merchant_id,card_type,denomination,stock_in_hand,reorder_level,SALE_COUNT from merchant_inventory where is_synch='0'  ";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			list.add(new MerchantInventorySynch(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public ArrayList<MerchantInventorySynch> GetAllMerchantInventory() {
		ArrayList<MerchantInventorySynch> list = new ArrayList<MerchantInventorySynch>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = " select merchant_id,card_type,denomination,stock_in_hand,reorder_level,SALE_COUNT from merchant_inventory";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			list.add(new MerchantInventorySynch(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public ArrayList<bulkId> getAcceptCardSerials() {
		ArrayList<bulkId> list = new ArrayList<bulkId>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT CARD_TYPE,DENOMINATION,BULK_NO,START_SERIAL,END_SERIAL FROM TSR_RECEIVED_CARD_BUNDLES WHERE IS_SYNCH='0' ", null);
		while (cursor.moveToNext()) {
			list.add(new bulkId(cursor.getString(0), cursor.getInt(1), cursor.getString(2), Integer.toString(cursor.getInt(3)), Integer.toString(cursor.getInt(4))));
		}
		cursor.close();
		db.close();
		return list;
	}

	public void ClearTables() {
		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL("DELETE FROM SALES_TEMP_DETAILS");
		db.execSQL("DELETE FROM SALES_DETAILS");
		db.execSQL("DELETE FROM SALES_HEADER");
		db.execSQL("DELETE FROM TSR_VISIT_REMARKS");
		db.execSQL("DELETE FROM TSR_INVENTORY_DETAILS");
		db.execSQL("DELETE FROM MERCHANT_INVENTORY");
		db.execSQL("DELETE FROM LAST_INVOICE_NO");
		db.execSQL("DELETE FROM NEXT_SERIAL");
		db.execSQL("DELETE FROM TSR_RECEIVED_CARD_BUNDLES");
		db.execSQL("DELETE FROM CARD_DENOMINATOIN");
		db.execSQL("DELETE FROM CARD_TYPES");
		db.execSQL("DELETE FROM MERCHANT_MASTER");
		db.execSQL("DELETE FROM CITY_POSTAL_CODES");

		db.close();
	}

	public int GetNoOfSalesRecords() {

		int noOfSalesDetails = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(DETAILS_ID) FROM SALES_DETAILS ", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			noOfSalesDetails = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return noOfSalesDetails;
	}

	public String getMinDate(String userName, String password, String mobileNo) {
		String mindate = "";
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT MIN (entered_date) AS MIN FROM sales_header WHERE entered_by IN ( SELECT epf_no FROM tsr_login WHERE user_name = '" + userName + "' " + "AND PASSWORD ='" + password + "' AND (mobile_no = '" + mobileNo + "' OR mobile_no IS NULL))";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			mindate = cursor.getString(0);
		}

		if (mindate != null) {
			cursor.close();
			db.close();
			System.out.println(mindate);
			return mindate;
		} else {
			return "NODATA";
		}

	}

	public int GetMaxrecords() {
		int maxRecords = 150;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT MAX_RECORDS FROM TSR_SYSTEM_PROFILE ", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			maxRecords = cursor.getInt(0);
		} else {
			setMerchantMaxRecords(maxRecords);
		}
		cursor.close();
		db.close();
		return maxRecords;
	}

	public int GetSalesDetailsCount() {

		int numberOfRecords = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM SALES_DETAILS", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			numberOfRecords = cursor.getInt(0);

		}
		cursor.close();
		db.close();

		return numberOfRecords;
	}

	public void DeleteSalesHeaderBySalesId(Long salesId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM SALES_HEADER WHERE 	INVOICE_ID =" + salesId + " ");
		db.close();
	}

	public void DeleteSalesDetailsBySalesId(Long salesId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM SALES_DETAILS WHERE INVOICE_ID =" + salesId + " ");
		db.close();
	}

	public Map<Long, String> getPurgeSalesList(String strInputUserMobile, String strInputUserName, String strInputUserPassword) {

		Map<Long, String> list = new HashMap<Long, String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT INVOICE_ID,entered_date,isupdated FROM SALES_HEADER WHERE IS_SYNCH = '1' ", null);
		while (cursor.moveToNext()) {
			Long invItem = cursor.getLong(0);
			String invDateitem = cursor.getString(1);
			list.put(invItem, invDateitem);
		}
		cursor.close();
		db.close();
		return list;
	}

	@SuppressWarnings({ "unused" })
	public ArrayList<String> getOfflineInvoices(String userName, String password, String mobileNo) {
		String mindate = "";
		String sql = "SELECT DISTINCT(INVOICE_ID)AS INVOICES FROM sales_header WHERE entered_by IN ( SELECT epf_no FROM tsr_login WHERE user_name = '" + userName + "' " + "AND PASSWORD ='" + password + "' AND (mobile_no = '" + mobileNo + "' OR mobile_no IS NULL))";
		ArrayList<String> list = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return list;
	}

	public void SaveSerials(Integer epfId, String cardType, Integer denomination, Integer bulkNo, Integer startSerial, Integer nextSerial, Integer endSerial, Integer isAllSold) {
		int nextSerialValue = 0;
		SQLiteDatabase dbSelect = this.getReadableDatabase();
		String sqlSelect = "select next_serial_value from next_serial n where n.CARD_TYPE='" + cardType + "' and n.DENOMINATION='" + denomination + "' and n.BULK_NO='" + bulkNo + "' and n.START_SERIAL='" + startSerial + "'";
		Cursor cursor = dbSelect.rawQuery(sqlSelect, null);
		if (cursor.moveToNext()) {
			nextSerialValue = cursor.getInt(0);
		} else {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("INSERT INTO NEXT_SERIAL(EPF_ID,CARD_TYPE,DENOMINATION,BULK_NO,START_SERIAL,NEXT_SERIAL_VALUE,END_SERIAL,IS_ALL_SOLD,IS_SYNCH) VALUES('" + epfId + "','" + cardType + "','" + denomination + "','" + bulkNo + "','" + startSerial + "','" + nextSerial + "','" + endSerial + "','" + isAllSold + "','1')");
			db.close();
		}
		System.out.println(nextSerialValue);
		cursor.close();
		dbSelect.close();
	}

	public void setMerchantMaxRecords(int max_records) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE TSR_SYSTEM_PROFILE SET MAX_RECORDS = '" + max_records + "' ");
		db.close();
	}

	// epfNo,detOrsum,fromDate, toDate
	public ArrayList<SalesReport> getDateWiseReport(String epfNo, String detOrsum, Date fromDate, Date toDate) {

		Map<Long, Date> map = new HashMap<Long, Date>();
		ArrayList<SalesReport> salesReportList = new ArrayList<SalesReport>();
		ArrayList<Long> invoiceIds = new ArrayList<Long>();
		map = selectedDatesInvoiceIdsMap(fromDate, toDate);
		boolean isSummery = false;
		for (Long key : map.keySet()) {
			invoiceIds.add(key);
		}
		String sql = "";
		if (!detOrsum.equals("Summary")) {

			sql = "SELECT h.ENTERED_DATE, d.DENOMINATION, sum(d.NO_OF_CARDS) as Qty, sum(d.LINE_AMOUNT)as amt FROM sales_details d,SALES_HEADER h where h.INVOICE_ID=d.INVOICE_ID AND h.INVOICE_ID in (" + id_data(invoiceIds) + ")GROUP BY strftime('%s','h.ENTERED_DATE'), d.DENOMINATION " + "union all SELECT 'Grand Total', 0, coalesce(sum(tst.qty),0)as qty, coalesce(sum(tst.amt),0)as amt FROM (SELECT h.ENTERED_DATE, d.DENOMINATION, sum(d.NO_OF_CARDS) as Qty, sum(d.LINE_AMOUNT)as amt FROM sales_details d,SALES_HEADER h where h.INVOICE_ID=d.INVOICE_ID AND h.INVOICE_ID in (" + id_data(invoiceIds) + ") GROUP BY strftime('%s','h.ENTERED_DATE'), d.DENOMINATION) tst";
		} else {
			isSummery = true;
			sql = "SELECT h.ENTERED_DATE, sum(d.LINE_AMOUNT)as amt  FROM sales_details d,SALES_HEADER h where h.INVOICE_ID=d.INVOICE_ID AND h.INVOICE_ID in (" + id_data(invoiceIds) + ")GROUP BY strftime('%s','h.ENTERED_DATE') order by strftime('%s','h.ENTERED_DATE')";
			// sql =
			// "SELECT h.ENTERED_DATE, d.DENOMINATION, sum(d.NO_OF_CARDS), sum(d.LINE_AMOUNT)as amt  FROM sales_details d,SALES_HEADER h where h.INVOICE_ID=d.INVOICE_ID AND h.INVOICE_ID in ("
			// + id_data(invoiceIds)
			// +
			// ")GROUP BY strftime('%s','h.ENTERED_DATE'), d.CARD_TYPE, d.DENOMINATION";
		}

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		int total_qty = 0;
		double total_amount = 0;

		if (cursor.moveToFirst()) {

			do {

				String dateBeforeFormat = "";
				String dateAfterFormat = "";
				dateBeforeFormat = cursor.getString(0);

				SalesReport salesReport = new SalesReport();

				if (dateBeforeFormat.equals("Grand Total")) {
					salesReport.setDate("Grand Total");
				} else {
					SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
					SimpleDateFormat dfn = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
					try {
						Date dateObj = df.parse(dateBeforeFormat);
						dateAfterFormat = dfn.format(dateObj);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					salesReport.setDate(dateAfterFormat);
				}
				if (!isSummery) {
					salesReport.setDenom(Double.valueOf(cursor.getString(1)));
					// salesReport.setQty(Integer.valueOf(cursor.getString(2)));
					// salesReport.setAmount(Double.valueOf(cursor.getString(3)));
					int qty = Integer.valueOf(cursor.getString(2));
					total_qty += qty;
					salesReport.setQty(qty);
					double amount = Double.valueOf(cursor.getString(3));
					total_amount += amount;
					salesReport.setAmount(amount);
				} else {
					salesReport.setDenom(null);
					salesReport.setQty(null);
					// salesReport.setAmount(Double.valueOf(cursor.getString(1)));
					double amount = Double.valueOf(cursor.getString(1));
					total_amount += amount;
					salesReport.setAmount(amount);
				}
				salesReportList.add(salesReport);
			} while (cursor.moveToNext());

			if (isSummery) {
				SalesReport sales_total = new SalesReport();
				sales_total.setDate("Grand Total");
				sales_total.setDenom(null);
				sales_total.setQty(null);
				sales_total.setAmount(total_amount);
				salesReportList.add(sales_total);
			}

		}
		return salesReportList;
	}

	public ArrayList<SalesReport> getMerchantWiseReport(String epfNo, String detOrsum, Date fromDate, Date toDate) {
		Map<Long, Date> map = new HashMap<Long, Date>();
		ArrayList<SalesReport> salesReportList = new ArrayList<SalesReport>();
		ArrayList<Long> invoiceIds = new ArrayList<Long>();
		map = selectedDatesInvoiceIdsMap(fromDate, toDate);

		for (Long key : map.keySet()) {
			invoiceIds.add(key);
		}
		String sql = "";

		boolean isSummery = false;

		if (detOrsum.equalsIgnoreCase("Summary")) {
			sql = "SELECT h.MERCHANT_ID ,sum(d.LINE_AMOUNT)as amt,m.MERCHANT_NAME FROM sales_details d,SALES_HEADER h,MERCHANT_MASTER m  WHERE m.MERCHANT_ID = h.MERCHANT_ID and  h.INVOICE_ID=d.INVOICE_ID AND h.INVOICE_ID in (" + id_data(invoiceIds) + ") GROUP BY h.MERCHANT_ID order by h.MERCHANT_ID";
			// sql =
			// "SELECT h.MERCHANT_ID , d.DENOMINATION, sum(d.NO_OF_CARDS)as qty, sum(d.LINE_AMOUNT)as amt FROM sales_details d,SALES_HEADER h WHERE h.INVOICE_ID=d.INVOICE_ID AND h.INVOICE_ID in ("
			// + id_data(invoiceIds)
			// + ") GROUP BY h.MERCHANT_ID, d.DENOMINATION ";
			isSummery = true;
		} else {
			isSummery = false;
			// sql =
			// "SELECT h.MERCHANT_ID , d.DENOMINATION, sum(d.NO_OF_CARDS), sum(d.LINE_AMOUNT)as amt FROM sales_details d,SALES_HEADER h WHERE h.INVOICE_ID=d.INVOICE_ID AND h.INVOICE_ID in ("
			// + id_data(invoiceIds)
			// + ") GROUP BY h.MERCHANT_ID, d.DENOMINATION";

			sql = "SELECT h.MERCHANT_ID , d.DENOMINATION, sum(d.NO_OF_CARDS), sum(d.LINE_AMOUNT)as amt ,m.MERCHANT_NAME FROM sales_details d,SALES_HEADER h,MERCHANT_MASTER m  WHERE m.MERCHANT_ID = h.MERCHANT_ID and h.INVOICE_ID=d.INVOICE_ID AND h.INVOICE_ID in (" + id_data(invoiceIds) + ") GROUP BY h.MERCHANT_ID,d.CARD_TYPE, d.DENOMINATION";

			// sql =
			// "SELECT h.MERCHANT_ID , d.DENOMINATION, sum(d.NO_OF_CARDS), sum(d.LINE_AMOUNT)as amt FROM sales_details d,SALES_HEADER h WHERE h.INVOICE_ID=d.INVOICE_ID AND h.INVOICE_ID in ("
			// + id_data(invoiceIds)
			// + ") GROUP BY h.MERCHANT_ID,d.CARD_TYPE, d.DENOMINATION";
		}
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		int total_qty = 0;
		double total_amount = 0;

		if (cursor.moveToFirst()) {
			do {
				SalesReport salesReport = new SalesReport();
				if (isSummery) {
					salesReport.setMerchantId(Long.valueOf(cursor.getString(0)));
					salesReport.setDenom(null);
					salesReport.setQty(null);
					double amount = Double.valueOf(cursor.getString(1));
					total_amount += amount;
					salesReport.setAmount(amount);
					salesReport.setMerchantName(cursor.getString(2));
				} else {
					salesReport.setMerchantId(Long.valueOf(cursor.getString(0)));
					salesReport.setDenom(Double.valueOf(cursor.getString(1)));
					int qty = Integer.valueOf(cursor.getString(2));
					total_qty += qty;
					salesReport.setQty(qty);
					double amount = Double.valueOf(cursor.getString(3));
					total_amount += amount;
					salesReport.setAmount(amount);
					salesReport.setMerchantName(cursor.getString(4));
				}
				salesReportList.add(salesReport);
			} while (cursor.moveToNext());

			SalesReport sales_total = new SalesReport();
			sales_total.setDate("Grand Total");
			sales_total.setDenom(null);
			if (isSummery) {
				sales_total.setQty(null);
			} else {
				sales_total.setQty((int) total_qty);
			}
			sales_total.setAmount(total_amount);
			salesReportList.add(sales_total);
		}

		return salesReportList;
	}

	public ArrayList<SalesReport> getCityWiseReport(String epfNo, String detOrsum, Date fromDate, Date toDate) {
		Map<Long, Date> map = new HashMap<Long, Date>();
		ArrayList<SalesReport> salesReportList = new ArrayList<SalesReport>();
		ArrayList<Long> invoiceIds = new ArrayList<Long>();
		map = selectedDatesInvoiceIdsMap(fromDate, toDate);
		for (Long key : map.keySet()) {
			invoiceIds.add(key);
		}
		boolean isSummery = false;

		String sql = "";
		if (detOrsum.equals("Summary")) {
			isSummery = true;
			sql = "SELECT m.CITY, sum(d.LINE_AMOUNT)  FROM sales_details d,sales_header h,merchant_master m where h.INVOICE_ID=d.INVOICE_ID AND h.MERCHANT_ID=m.MERCHANT_ID AND h.INVOICE_ID in (" + id_data(invoiceIds) + ") GROUP BY m.CITY";
			// sql =
			// "SELECT m.CITY, d.DENOMINATION, sum(d.NO_OF_CARDS), sum(d.LINE_AMOUNT)as amt  FROM sales_details d,sales_header h,merchant_master m where h.INVOICE_ID=d.INVOICE_ID AND h.MERCHANT_ID=m.MERCHANT_ID AND h.INVOICE_ID in ("
			// + id_data(invoiceIds) + ") GROUP BY m.CITY,d.DENOMINATION";

		} else {
			sql = "SELECT m.CITY, d.DENOMINATION, sum(d.NO_OF_CARDS), sum(d.LINE_AMOUNT)as amt  FROM sales_details d,sales_header h,merchant_master m where h.INVOICE_ID=d.INVOICE_ID AND h.MERCHANT_ID=m.MERCHANT_ID AND h.INVOICE_ID in (" + id_data(invoiceIds) + ") GROUP BY m.CITY,d.CARD_TYPE,d.DENOMINATION";
		}

		int total_qty = 0;
		double total_amount = 0;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				SalesReport salesReport = new SalesReport();
				if (!isSummery) {
					salesReport.setCity(cursor.getString(0));
					salesReport.setDenom(Double.valueOf(cursor.getString(1)));
					// salesReport.setQty(Integer.valueOf(cursor.getString(2)));
					// salesReport.setAmount(Double.valueOf(cursor.getString(3)));
					int qty = Integer.valueOf(cursor.getString(2));
					total_qty += qty;
					salesReport.setQty(qty);
					double amount = Double.valueOf(cursor.getString(3));
					total_amount += amount;
					salesReport.setAmount(amount);
				} else {
					salesReport.setCity(cursor.getString(0));
					salesReport.setDenom(null);
					salesReport.setQty(null);
					double amount = Double.valueOf(cursor.getString(1));
					total_amount += amount;
					salesReport.setAmount(amount);
				}
				salesReportList.add(salesReport);
			} while (cursor.moveToNext());

			SalesReport sales_total = new SalesReport();
			sales_total.setDate("Grand Total");
			sales_total.setDenom(null);
			if (!isSummery) {
				sales_total.setQty((int) total_qty);
			} else {
				sales_total.setQty(null);
			}
			sales_total.setAmount(total_amount);
			salesReportList.add(sales_total);

		}
		return salesReportList;
	}

	public boolean isWithinRange(Date testDate, Date fromDate, Date toDate) {
		return !(testDate.before(fromDate) || testDate.after(toDate));
	}

	public Map<Long, Date> selectedDatesInvoiceIdsMap(Date fromDate, Date toDate) {
		SQLiteDatabase db = this.getReadableDatabase();
		Date recordDate;
		Map<Long, Date> map = new HashMap<Long, Date>();
		String sql = "SELECT h.INVOICE_ID,h.ENTERED_DATE FROM sales_details d,SALES_HEADER h WHERE h.INVOICE_ID=d.INVOICE_ID";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				String dateString = cursor.getString(1);
				SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
				try {
					recordDate = sd.parse(dateString);
					if (isWithinRange(recordDate, fromDate, toDate)) {
						map.put(Long.valueOf(cursor.getString(0)), recordDate);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
		}
		return map;
	}

	public String id_data(ArrayList<Long> data) {
		StringBuilder val = new StringBuilder();
		for (int i = 0; i < data.size(); i++) {
			val.append(String.valueOf(data.get(i)));
			if (i < data.size() - 1) {
				val.append(",");
			}
		}
		String ids = val.toString();
		return ids;
	}

	public void updateNextSerialsAsSynch() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = " update next_serial set is_synch='1' where IS_SYNCH='0' ";
		db.execSQL(sql);
		db.close();
	}

	public void updateNextSerialsAsSynch(NextSerialSync nextSerialSync) {
		if (nextSerialSync.getResponce() == 1) {
			SQLiteDatabase db = this.getWritableDatabase();
			String sql = " update next_serial set is_synch='1' where bulk_no = '" + nextSerialSync.getBULK_NO() + "' AND start_serial = '" + nextSerialSync.getSTART_SERIAL() + "' AND end_serial = '" + nextSerialSync.getEND_SERIAL() + "' ";
			db.execSQL(sql);
			db.close();
		}
	}

	public void updateInventoryAsSynch() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = " update MERCHANT_INVENTORY set is_synch='1' where IS_SYNCH='0' ";
		db.execSQL(sql);
		db.close();
	}

	public void UpdateMerchantMasterMerchantId(int merchantId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE MERCHANT_MASTER SET IS_SYNCH='1' WHERE MERCHANT_ID = " + merchantId + " ");
		db.close();
	}

	public ArrayList<MerchantNew> GetNonSynchMerchants() {

		ArrayList<MerchantNew> list = new ArrayList<MerchantNew>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select MERCHANT_ID," + // 0
				"MERCHANT_NAME," + // 1
				"ADDRESS," + // 2
				"(case when RELOAD_NUMBER is null then 0 else RELOAD_NUMBER end)RELOAD_NUMBER," + // 3
				"LATITUDE," + // 4
				"LONGITUDE," + // 5
				"(case when REGISTERED_ON is null then date('now') else REGISTERED_ON end)REGISTERED_ON," + // 6
				"REGISTERED_BY_EPF_NO," + // 7
				"TELEPHONE_NO," + // 8
				"CITY," + // 9
				"IS_ASSIGNED," + // 10
				"IS_EDITED," + // 11
				"IS_REGISTERED," + // 12
				"IS_ACTIVE, " + // 13
				"IS_UPDATED " + // 14
				"from merchant_master where is_synch='0' ";

		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			Long id = cursor.getLong(0);
			String name = cursor.getString(1);
			String address = cursor.getString(2);
			String reloadNo = cursor.getString(3);
			Double latitude = cursor.getDouble(4);
			Double longitude = cursor.getDouble(5);
			String registeredDate = cursor.getString(6);
			System.out.println("registeredDate : " + registeredDate);
			Integer registeredBy = cursor.getInt(7);
			String telephoneNo = cursor.getString(8);
			String city = cursor.getString(9);

			Integer isAssigned = cursor.getInt(10);
			Integer isEdited = cursor.getInt(11);
			Integer isRegistered = cursor.getInt(12);
			Integer isActive = cursor.getInt(13);
			Integer isUpdate = cursor.getInt(14);// Synch Records.they need only
													// to update.[not need to
													// insert]

			list.add(new MerchantNew(id, name, address, reloadNo, latitude, longitude, registeredDate, registeredBy, telephoneNo, city, isAssigned, isEdited, isRegistered, isActive, isUpdate));
		}
		cursor.close();
		db.close();
		return list;
	}

	public void updateMerchantsAsSynch() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "update merchant_master set IS_UPDATED='1',is_synch='1' where is_synch='0'";
		db.execSQL(sql);
		db.close();
	}

	public void UpdateMerchantInventoryTableAfterActivation(Long merchantid, String cardType, Integer denomination, Integer qty, Integer reorderLevel, Integer activationCount) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE MERCHANT_INVENTORY SET ACTIVATION_COUNT='" + activationCount + "',IS_SYNCH='1',IS_UPDATED='1', STOCK_IN_HAND =" + qty + ",REORDER_LEVEL =" + reorderLevel + " " + "WHERE MERCHANT_ID = " + merchantid + " AND CARD_TYPE='" + cardType + "' AND DENOMINATION= " + denomination + " ";
		db.execSQL(sql);
		db.close();
	}

	public String checkMerchantStatus(Long merchantId) {
		SQLiteDatabase db = this.getReadableDatabase();
		String status = "";
		Integer isAssigned = 0;
		Integer isRegistered = 0;
		String sql = "SELECT (case when is_assigned is null then '0' else is_assigned end)is_assigned," + " (case when is_registered is null then '0' else is_registered end)is_registered " + " FROM MERCHANT_MASTER where merchant_id='" + merchantId + "'";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			isAssigned = cursor.getInt(0);
			isRegistered = cursor.getInt(1);
		}
		cursor.close();
		db.close();
		if (isAssigned == 0 && isRegistered == 0) {
			status = "C";
		} else if (isAssigned == 1 && isRegistered == 0) {
			status = "A";
		} else if (isAssigned == 1 && isRegistered == 1) {
			status = "R";
		}
		return status;
	}

	public void clearAllSoldSerials() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql1 = "delete from next_serial where is_all_sold='1' AND IS_SYNCH = '1' ";
		db.execSQL(sql1);
		String sql2 = "delete from TSR_RECEIVED_CARD_BUNDLES where IS_SYNCH = '1' AND IS_ACCEPTED = '1' AND IS_ALL_SOLD = '1' ";
		db.execSQL(sql2);
		db.close();
	}

	public void ClearAllLogsDetails() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql1 = "delete from TSR_3G_Status where IS_SYNCH = 1 ";
		db.execSQL(sql1);
		String sql3 = "delete from LOCATION_TRACK where IS_SYNCH = '1' ";
		db.execSQL(sql3);
		db.close();
	}

	public void UpdateMerchantsWithNewIds(MerchantNewResponse item) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (item.getRespons() == 1) {
			String sql = "update merchant_master set IS_UPDATED = '1', is_synch = '1' where merchant_id='" + item.getOldMerchantId() + "' ";
			db.execSQL(sql);
			db.close();
		}
	}

	public ArrayList<String> getAllMerchants() {
		ArrayList<String> list = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT distinct(merchant_name) FROM merchant_master ORDER BY merchant_name", null);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return list;
	}

	public void updateVersionAsIsSynch() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "update TSR_LOGIN set IS_SYNCH='1' ";
		db.execSQL(sql);
		db.close();
	}

	public void saveVersion(double version) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "update TSR_LOGIN set VERSION = '" + version + "' ";
		db.execSQL(sql);
		db.close();
	}

	public Double getVersion() {
		Double version = 0.0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT VERSION FROM TSR_LOGIN ", null);
		if (cursor.moveToNext()) {
			version = cursor.getDouble(0);
		}
		cursor.close();
		db.close();
		return version;
	}

	public void UpdateTsrCityLimitList(String postalCodes, Integer maxLimit) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = " update city_postal_codes set max_changes='" + maxLimit + "' where postal_code='" + postalCodes + "' ";
		db.execSQL(sql);
		db.close();
	}

	public ArrayList<Locations> getLocationsList() {
		ArrayList<Locations> list = new ArrayList<Locations>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = " SELECT ID,LONGT,LATT,UPDATED_DATE,Accuracy,Speed,UseTime FROM LOCATION_TRACK WHERE IS_SYNCH = 0";
		Log.d("location details getting the mobile:", sql);
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			list.add(new Locations(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
		}
		cursor.close();
		db.close();
		Log.d("location details data:", list.toString() + "");
		return list;
	}

	/*
	 * public ArrayList<Locations> getLocationsErrorList() {
	 * ArrayList<Locations> list = new ArrayList<Locations>(); SQLiteDatabase db
	 * = this.getReadableDatabase(); String sql =
	 * " SELECT ID,LONGT,LATT,UPDATED_DATE FROM LOCATION_TRACK_ERROR WHERE IS_SYNCH = 0"
	 * ; Log.d("This is the location details of the getting the mobile 222 :",
	 * sql); Cursor cursor = db.rawQuery( sql, null); while
	 * (cursor.moveToNext()) { list.add(new Locations(cursor.getInt(0),
	 * cursor.getDouble(1), cursor.getDouble(2), cursor.getString(3))); }
	 * cursor.close(); db.close();
	 * Log.d("This is the location details data 222:", list+""); return list; }
	 */

	public ArrayList<LocationsBack> getLocationsListBack() {
		ArrayList<LocationsBack> list = new ArrayList<LocationsBack>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = " SELECT ID,Accuracy,Speed,UseTime,LONGT,LATT,UPDATED_DATE FROM LOCATION_TRACK WHERE IS_SYNCH = 0";
		Cursor cursor = db.rawQuery(sql, null);
		Log.d("This is the location details of the getting the mobile 333 :", sql);
		while (cursor.moveToNext()) {
			list.add(new LocationsBack(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), "", cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6)));
		}
		cursor.close();
		db.close();
		Log.d("This is the location details data 333 :", list + "");
		return list;
	}

	public void updateSynchLocations(String string) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = " UPDATE LOCATION_TRACK SET IS_SYNCH=1 WHERE IS_SYNCH=0 AND UPDATED_DATE = '" + string + "' ";
		db.execSQL(sql);
		db.close();
	}

	public void SaveServerDate(String string) {
		System.out.println("* string "+string);
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE TSR_LOGIN SET SERVER_DATE='" + string + "'";
		db.execSQL(sql);
		db.close();
	}

	public String getServerDate() {
		String serverDate = "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT SERVER_DATE FROM TSR_LOGIN ", null);
			if (cursor.moveToNext()) {
				Log.d("Inquery date time is :", cursor.getString(0));
				serverDate = cursor.getString(0);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			cursor.close();
			db.close();
		}
		return serverDate;
	}

	public List<ErrorLog> getAllStatusDetails() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(" select UPDATE_STATUS,UPDATED_DATE from TSR_UPDATE_LOG ", null);
		List<ErrorLog> errorLogs = new ArrayList<ErrorLog>();
		while (cursor.moveToNext()) {
			ErrorLog errorLog = new ErrorLog(cursor.getString(0), cursor.getString(1));
			errorLogs.add(errorLog);
		}
		cursor.close();
		db.close();
		return errorLogs;
	}

	public List<ErrorLog> getAllErrorDetails() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select UPDATE_STATUS,UPDATED_DATE from TSR_ERROR_LOG ", null);
		List<ErrorLog> errorLogs = new ArrayList<ErrorLog>();
		while (cursor.moveToNext()) {
			ErrorLog errorLog = new ErrorLog(cursor.getString(0), cursor.getString(1));
			errorLogs.add(errorLog);
		}
		cursor.close();
		db.close();
		return errorLogs;
	}

	public List<CityRegistrationLimit> getCityRegistrationLimits() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(" select POSTAL_CODE,changes,max_changes,LATITUDE,LONGITUDE from city_postal_codes where is_synch='0' ", null);
		List<CityRegistrationLimit> limits = new ArrayList<CityRegistrationLimit>();
		while (cursor.moveToNext()) {
			CityRegistrationLimit limit = new CityRegistrationLimit(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
			limits.add(limit);
		}
		cursor.close();
		db.close();

		SQLiteDatabase dbN = this.getWritableDatabase();
		String sql = "UPDATE city_postal_codes set is_synch='1' where is_synch='0'";
		dbN.execSQL(sql);
		dbN.close();
		return limits;
	}

	public ArrayList<SynchReport> getSalesSynchReport() {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select date('now') Date,'Sales Details' as Component,sum(is_synch) as synch_count,count(*) as total_count from sales_details union select date('now') Date,'Sales Header' as Component,sum(is_synch) as synch_count,count(*) as total_count from sales_header";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<SynchReport> SynchReport = new ArrayList<SynchReport>();
		while (cursor.moveToNext()) {
			SynchReport synchReport = new SynchReport(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			SynchReport.add(synchReport);
		}
		cursor.close();
		db.close();

		return SynchReport;
	}

	public ArrayList<SynchReport> getCardsSynchReport() {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select date('now') Date,'Next Serial' as Component,sum(is_synch) as synch_count,count(is_synch) as total_count from next_serial union select date('now') Date,'Card Acceptance' as Component,sum(is_synch) as synch_count,count(*) as total_count from TSR_RECEIVED_CARD_BUNDLES";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<SynchReport> SynchReport = new ArrayList<SynchReport>();
		while (cursor.moveToNext()) {
			SynchReport synchReport = new SynchReport(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			SynchReport.add(synchReport);
		}
		cursor.close();
		db.close();

		return SynchReport;
	}

	public ArrayList<SynchReport> getMerchantsSynchReport() {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select date('now') Date,'Register Merchants' as Component,sum(is_synch) as synch_count,count(is_synch) as total_count from merchant_master where is_registered='1' union select date('now') Date,'Assigned Merchants' as Component,sum(is_synch) as synch_count,count(*) as total_count from merchant_master where is_registered='0' and is_assigned='1' union select date('now') Date,'Created Merchants' as Component,sum(is_synch) as synch_count,count(*) as total_count from merchant_master where is_registered='0' and is_assigned='0'";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<SynchReport> SynchReport = new ArrayList<SynchReport>();
		while (cursor.moveToNext()) {
			SynchReport synchReport = new SynchReport(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			SynchReport.add(synchReport);
		}
		cursor.close();
		db.close();
		return SynchReport;
	}

	public ArrayList<SynchReport> getCityiesReport() {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select date('now') Date, 'My Cities' as Component, sum(is_synch) as synch_count, count(is_synch) as total_count from CITY_POSTAL_CODES";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<SynchReport> SynchReport = new ArrayList<SynchReport>();
		while (cursor.moveToNext()) {
			SynchReport synchReport = new SynchReport(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			SynchReport.add(synchReport);
		}
		cursor.close();
		db.close();
		return SynchReport;
	}

	public List<ResultCode> getFinalResponse() {
		List<ResultCode> resultCodes = new ArrayList<ResultCode>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select response_code,count(response_code) as records from tsr_sync_log group by response_code order by response_code ASC", null);
		while (cursor.moveToNext()) {
			ResultCode synchReport = new ResultCode(cursor.getInt(0), cursor.getInt(1));
			resultCodes.add(synchReport);
		}
		cursor.close();
		db.close();
		return resultCodes;
	}

	public ArrayList<SynchReport> getSynchReport() {
		SQLiteDatabase db = this.getReadableDatabase();
		String getTotallSynch = "select date('now') Date, 'Register Merchants' as Component, sum(is_synch) as synch_count, count(is_synch) as total_count from merchant_master where is_registered='1' union " + "select date('now') Date, 'Assigned Merchants' as Component, sum(is_synch) as synch_count, count(*) as total_count from merchant_master where is_registered='0' and is_assigned='1' union " + "select date('now') Date, 'Created Merchants' as Component, sum(is_synch) as synch_count, count(*) as total_count from merchant_master where is_registered='0' and is_assigned='0' union " + "select date('now') Date, 'Sales Details' as Component, sum(is_synch) as synch_count, count(*) as total_count from sales_details union "
				+ "select date('now') Date, 'Sales Header' as Component, sum(is_synch) as synch_count, count(*) as total_count from sales_header union " + "select date('now') Date, 'Next Serial' as Component, sum(is_synch) as synch_count, count(is_synch) as total_count from next_serial union " + "select date('now') Date, 'Cards Acceptance' as Component, sum(is_synch) as synch_count, count(*) as total_count from TSR_RECEIVED_CARD_BUNDLES union " + "select date('now') Date, 'Cities' as Component, sum(is_synch) as synch_count, count(*) as total_count from CITY_POSTAL_CODES union  " + "select date('now') Date, 'Merchant Inventory' as Component, sum(is_synch) as synch_count, count(*) as total_count from MERCHANT_INVENTORY";
		Cursor cursor = db.rawQuery(getTotallSynch, null);
		ArrayList<SynchReport> SynchReport = new ArrayList<SynchReport>();
		while (cursor.moveToNext()) {
			SynchReport synchReport = new SynchReport(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			SynchReport.add(synchReport);
		}
		cursor.close();
		db.close();
		return SynchReport;
	}

	@SuppressWarnings("deprecation")
	public ArrayList<SynchReport> viewSynchReport(String Type) {
		String searchModule = Type;
		ArrayList<SynchReport> viewSyncReports = new ArrayList<SynchReport>();
		SQLiteDatabase db = this.getReadableDatabase();
		String Synchsummery = "SELECT * FROM TSR_SYNC_LOG ORDER BY MODULE ASC";
		if (searchModule.equals("Totall Synch Report")) {
			Synchsummery = "SELECT * FROM TSR_SYNC_LOG ORDER BY MODULE ASC";
		} else {
			Synchsummery = "SELECT * FROM TSR_SYNC_LOG WHERE MODULE = '" + searchModule + "' ORDER BY MODULE ASC";
		}
		Log.d("SQL Query is : ", Synchsummery);
		Cursor cursor = db.rawQuery(Synchsummery, null);
		while (cursor.moveToNext()) {
			SynchReport report = new SynchReport(new SimpleDateFormat("MM-dd").format(new Date(cursor.getString(0))), cursor.getString(1) + " -" + cursor.getString(2), cursor.getString(3) + "/" + cursor.getString(4), cursor.getString(5) + "/" + cursor.getString(6));
			viewSyncReports.add(report);
		}
		cursor.close();
		db.close();
		return viewSyncReports;
	}
	
	
	public String getLastSyncTime() throws Exception
	{
		String last_sync_time = " - ";
		Toast.makeText(context, "OK", Toast.LENGTH_LONG);
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			String SyTi = "SELECT DATE FROM TSR_SYNC_LOG";
			Cursor cursor = db.rawQuery(SyTi, null);
			if(cursor.moveToFirst()){
				last_sync_time =  cursor.getString(0);
				
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			throw e;
		}
		return last_sync_time;
		
	}

	public List<String> loadSynchReportDropdown() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		String Synchsummery = "SELECT DISTINCT MODULE FROM TSR_SYNC_LOG ORDER BY MODULE ASC;";
		Cursor cursor = db.rawQuery(Synchsummery, null);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		list.add("Totall Synch Report");
		cursor.close();
		db.close();
		return list;
	}

	public void insertSynchLogUpdate(String module, String component, int post_synch, int post_total, int responseCode) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "update tsr_sync_log set response_code='" + responseCode + "',post_synch='" + post_synch + "',post_total='" + post_total + "' where module='" + module + "' and component='" + component + "'";
		db.execSQL(sql);
		db.close();
	}

	public ArrayList<TSRSystemprofile> TSRSystemprofile(String task) {
		String Task = task;
		ArrayList<TSRSystemprofile> list = new ArrayList<TSRSystemprofile>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM TSR_SYSTEM_PROFILE WHERE IS_SYNCH = '0' ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			Integer NEXT_MERCHANT_NO = cursor.getInt(0);
			Integer NEXT_INVOICE_NO = cursor.getInt(1);
			Integer MAX_RECORDS = cursor.getInt(2);
			String LAST_SYNCH_DATE = cursor.getString(3);
			Log.d("This is the system last sysnch date time : ", LAST_SYNCH_DATE + "((((((((((((((((((((((((((((((((((((((((((((((((((9 ");
			list.add(new TSRSystemprofile(NEXT_MERCHANT_NO, NEXT_INVOICE_NO, Task, MAX_RECORDS, "", 0, 0, 0, LAST_SYNCH_DATE));
		}
		if (task.equals("LoginInishial")) {
			Log.d("This is the some missing data : ", "Data is missimg ..........................");
			list.add(new TSRSystemprofile(10001, 100001, Task, 150, "", 0, 0, 0, ""));
		}
		cursor.close();
		db.close();
		Log.d("This is the system last : ", list.toString() + "");
		return list;
	}

	public void UpdateMerchantsWithNewIds(TSRSystemprofile systemprofile, AssetManager am) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql;

		if (systemprofile.getNEXT_MERCHANT_NO() < 100) {
			systemprofile.setNEXT_MERCHANT_NO(10001);
		}
		if (systemprofile.getNEXT_INVOICE_NO() < 100) {
			systemprofile.setNEXT_INVOICE_NO(100001);
		}

		if (systemprofile.getNEXT_MERCHANT_NO() > 100 && systemprofile.getNEXT_INVOICE_NO() > 100) {
			sql = "INSERT INTO TSR_SYSTEM_PROFILE (IS_SYNCH, NEXT_MERCHANT_NO, NEXT_INVOICE_NO, MAX_RECORDS, SYSTEM_AUTO_SYNCH, GPS_GETTING, GPS_AUTO_SYNCH) VALUES ('1','" + systemprofile.getNEXT_MERCHANT_NO() + "','" + systemprofile.getNEXT_INVOICE_NO() + "','" + systemprofile.getMAX_RECORDS() + "', '" + systemprofile.getSYSTEM_AUTO_SYNCH() + "', '" + systemprofile.getGPS_GETTING() + "', '" + systemprofile.getGPS_AUTO_SYNCH() + "') ";
		} else {
			sql = "INSERT INTO TSR_SYSTEM_PROFILE (IS_SYNCH, NEXT_MERCHANT_NO, NEXT_INVOICE_NO,MAX_RECORDS, SYSTEM_AUTO_SYNCH, GPS_GETTING, GPS_AUTO_SYNCH) VALUES " + "('1','10001','100001','150', '25200000', '30000', '300000') ";
		}
		Log.d("system data query :", sql);
		db.execSQL(sql);
		db.close();
	}

	public long getSystemAutoSynch() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select SYSTEM_AUTO_SYNCH from TSR_SYSTEM_PROFILE", null);
		long Values = 0;
		while (cursor.moveToNext()) {
			Values = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return Values;
	}

	public long getGPSGetting() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select GPS_GETTING from TSR_SYSTEM_PROFILE", null);
		long Values = 0;
		while (cursor.moveToNext()) {
			Values = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return Values;
	}

	public long getGPSAutoSynch() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select GPS_AUTO_SYNCH from TSR_SYSTEM_PROFILE", null);
		long Values = 0;
		while (cursor.moveToNext()) {
			Values = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return Values;
	}

	public void UpdateMerchantsWithSysnc() {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "UPDATE TSR_SYSTEM_PROFILE SET IS_SYNCH = '1' ";
		Log.d("TThis is the system data query : ", sql);
		db.execSQL(sql);
		db.close();
	}

	public ArrayList<cardSalesHeader> cardSalesHeader() {// this sales header
															// data
		// list going to synch
		ArrayList<cardSalesHeader> list = new ArrayList<cardSalesHeader>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = null;

		try {
			cursor = db.rawQuery("SELECT INVOICE_ID,MERCHANT_ID,LATITUDE,LONGITUDE,INVOICE_TOTAL,ENTERED_DATE,ENTERED_BY,TOTAL_DISCOUNT FROM SALES_HEADER WHERE IS_SYNCH='0' ", null); // AND
			// ISUPDATED='1'
			while (cursor.moveToNext()) {
				ArrayList<cardSalesDeatils> salesDetails = new ArrayList<cardSalesDeatils>();
				Double headerTotall = cursor.getDouble(4);
				cardSalesHeader cardSalesHeader = new cardSalesHeader(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getString(5), cursor.getInt(6), cursor.getDouble(7));
				long id = cursor.getLong(0);
				SQLiteDatabase db1 = this.getReadableDatabase();
				Cursor cursor2 = db1.rawQuery("SELECT DETAILS_ID,CARD_TYPE,DENOMINATION,BULK_NO,NO_OF_CARDS,START_SERIAL,LINE_AMOUNT,LINE_DISCOUNT,INVOICE_ID FROM SALES_DETAILS WHERE IS_SYNCH='0' AND INVOICE_ID = '" + id + "' ", null);
				double detailTotall = 0.0;
				while (cursor2.moveToNext()) {
					detailTotall = detailTotall + cursor2.getDouble(6);
					salesDetails.add(new cardSalesDeatils(cursor2.getInt(0), cursor2.getString(1), cursor2.getInt(2), cursor2.getInt(3), cursor2.getInt(4), cursor2.getString(5), cursor2.getDouble(6), cursor2.getDouble(7), cursor2.getLong(8)));
				}
				cursor2.close();
				db1.close();
				cardSalesHeader.setCardSalesDeatils(salesDetails);
				if (detailTotall == headerTotall) {
					list.add(cardSalesHeader);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor!= null){
				cursor.close();
			}
			db.close();
		}


		return list;
	}

	public void deleteSalesDetails(long longInvoiceId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("SALES_DETAILS", "INVOICE_ID = " + longInvoiceId, null);
		db.delete("SALES_HEADER", "INVOICE_ID = " + longInvoiceId, null);
		db.close();
	}

	public void insertLoadData(String string, Integer count, int size, int insert, int code) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "INSERT INTO TSR_DATA_LOAD_LOG (TABLE_NAME,ALLTOTALROWS,LISTROWS,INSERTROWS,codeMessage) VALUES ('" + string + "', '" + count + "', '" + size + "', '" + insert + "', '" + code + "') ";
		Log.d("This is the TSR_DATA_LOADING data query : ", sql);
		db.execSQL(sql);
		db.close();
	}

	public void DeleteDataReloadTable() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("delete from TSR_DATA_LOAD_LOG", null);
		cursor.close();
		db.close();
	}

	public int getFinalResetDataINUpdate() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select sum(codeMessage), count(*) from TSR_DATA_LOAD_LOG group by codeMessage order by codeMessage ASC", null);
		int recode = 1;
		while (cursor.moveToNext()) {
			if (cursor.getInt(0) > 1 || cursor.getInt(1) < 1) {
				recode = 1;
			} else {
				recode = 0;
			}
		}
		cursor.close();
		db.close();
		return recode;
	}

	public int getFinalResetData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select sum(codeMessage) from TSR_DATA_LOAD_LOG group by codeMessage order by codeMessage ASC", null);
		int recode = 0;
		while (cursor.moveToNext()) {
			if (cursor.getInt(0) > 1) {
				recode = 1;
			}
		}
		cursor.close();
		db.close();
		return recode;
	}

	public String getFinalResetDataTabels() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select TABLE_NAME from TSR_DATA_LOAD_LOG where codeMessage = '0' ", null);
		while (cursor.moveToNext()) {
			if (ResetTabels == null || ResetTabels.equals("")) {
				ResetTabels = cursor.getString(0);
			} else {
				ResetTabels = ResetTabels + "&" + cursor.getString(0);
			}
		}
		cursor.close();
		db.close();
		return ResetTabels;
	}

	public void SaveLoginData(String epfno, String userName, String password, String mobileNumber, String serverDateTime) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d("_______________________", serverDateTime + " ================== ");
		db.execSQL("INSERT INTO  TSR_LOGIN (EPF_NO,USER_NAME,PASSWORD,MOBILE_NO,SERVER_DATE) VALUES('" + epfno + "','" + userName + "','" + password + "','" + mobileNumber + "','" + serverDateTime + "') ");
		db.close();

	}
	public void SaveLoginDataSp(String userName, String password, String mobileNumber,String serverDateTime) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO  TSR_LOGIN (EPF_NO,USER_NAME,PASSWORD,MOBILE_NO,SERVER_DATE) VALUES('" + userName + "','" + userName + "','" + password + "','" + mobileNumber + "','"+serverDateTime+"') ");
		db.close();
	}
	public void SaveLogout(String userName, String logout) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT LOG_OUT FROM LOGOUT WHERE USER_NAME = '"+userName+"'", null);
		if(cursor.getCount()>0){
			db.execSQL("UPDATE LOGOUT set LOG_OUT = '"+logout+"' where USER_NAME = '"+userName+"'");
		}else{
			db.execSQL("INSERT INTO  LOGOUT (USER_NAME,LOG_OUT) VALUES('" + userName + "','" + logout + "') ");
		}
		db.close();
	}

	public String GetLogout(String name) {
		String nam = "";
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT LOG_OUT FROM LOGOUT WHERE USER_NAME = '"+name+"'", null);
			while (cursor.moveToNext()) {
				nam = cursor.getString(0);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("* name "+nam);
		return nam;
	}
	public void updateLogout(String username){
		SQLiteDatabase dbN = this.getWritableDatabase();
		String sql = "UPDATE LOGOUT set LOG_OUT='N' where USER_NAME= '"+username+"'";
		dbN.execSQL(sql);
		dbN.close();
	}

	public String getLostResetData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select TABLE_NAME, codeMessage from TSR_DATA_LOAD_LOG where codeMessage = '0' ", null);
		String Tabels = null;
		while (cursor.moveToNext()) {
			if (Tabels == null || Tabels.isEmpty() || Tabels.equals("")) {
				Tabels = cursor.getString(0);
			} else {
				Tabels = Tabels + " | " + cursor.getString(0);
			}
		}
		cursor.close();
		db.close();
		return Tabels;
	}


	public void deleteSynchLocations() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "DELETE FROM LOCATION_TRACK where IS_SYNCH = '1' ";
		db.execSQL(sql);
		db.close();
	}

	public String checkmerchantsales(Long valueOf, String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String result = "";
		Cursor cursor = db.rawQuery("SELECT * FROM SALES_HEADER WHERE MERCHANT_ID = '" + valueOf + "' ", null);
		if (cursor.getCount() > 0) {
			result = "notcomplete";
		} else {
			result = "delete";
		}
		cursor.close();
		db.close();
		return result;
	}

	public String deleteMerchant(Long valueOf, String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String result = "";
		Cursor cursor = db.rawQuery("SELECT * FROM SALES_HEADER WHERE MERCHANT_ID = '" + valueOf + "' ", null);
		if (cursor.getCount() > 0) {
			result = "notcomplete";
		} else {
			cursor.close();
			String sql = "DELETE FROM MERCHANT_MASTER WHERE MERCHANT_ID = '" + valueOf + "' ";
			db.execSQL(sql);
			result = "delete";
		}
		cursor.close();
		db.close();
		return result;
	}

	public ArrayList<CardAccept> GetCardAcceptData() {
		ArrayList<CardAccept> cardAccepts = new ArrayList<CardAccept>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT CARD_TYPE,DENOMINATION,BULK_NO,START_SERIAL,END_SERIAL FROM TSR_RECEIVED_CARD_BUNDLES WHERE IS_SYNCH='0' ", null);
		while (cursor.moveToNext()) {
			cardAccepts.add(new CardAccept(cursor.getString(0), cursor.getInt(1), cursor.getString(2), Integer.toString(cursor.getInt(3)), Integer.toString(cursor.getInt(4))));
		}
		cursor.close();
		db.close();
		return cardAccepts;
	}

	public ArrayList<SynchRemarks> SyncRemarkData() {
		ArrayList<SynchRemarks> remarks = new ArrayList<SynchRemarks>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT REMARKS_ID,MERCHANT_ID,REMARKS,LATITUDE,LONGITUDE,VISIT_DATE FROM TSR_VISIT_REMARKS  WHERE ISUPDATED = 1 ", null);
		while (cursor.moveToNext()) {
			remarks.add(new SynchRemarks(cursor.getInt(0), cursor.getLong(1), cursor.getString(2), true, cursor.getString(3), cursor.getString(4), cursor.getString(5)));
		}
		cursor.close();
		db.close();
		return remarks;
	}

	public ArrayList<CityRegistration> CityRegistrationDetails() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<CityRegistration> registrations = new ArrayList<CityRegistration>();
		Cursor cursor = db.rawQuery("select CITY, changes, max_changes, POSTAL_CODE from city_postal_codes where is_synch='0' ", null);
		while (cursor.moveToNext()) {
			registrations.add(new CityRegistration(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3)));
		}
		cursor.close();
		db.close();

		SQLiteDatabase dbN = this.getWritableDatabase();
		String sql = "UPDATE city_postal_codes set is_synch='1' where is_synch='0'";
		dbN.execSQL(sql);
		dbN.close();
		return registrations;
	}

	public List<MerchantMaster> getAllMerchantMaster() {
		ArrayList<MerchantMaster> merchant = new ArrayList<MerchantMaster>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select MERCHANT_ID,MERCHANT_NAME,ADDRESS,RELOAD_NUMBER,LATITUDE,LONGITUDE,REGISTERED_ON,TELEPHONE_NO,CITY,IS_ACTIVE,REGISTERED_BY_EPF_NO,IS_REGISTERED,IS_EDITED,IS_UPDATED,IS_ASSIGNED,IS_SYNCH from MERCHANT_MASTER";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			merchant.add(new MerchantMaster(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getString(10), cursor.getInt(11), cursor.getInt(12), cursor.getInt(13), cursor.getInt(14), cursor.getInt(15)));
		}
		cursor.close();
		db.close();
		return merchant;
	}

	public List<CardBundles> getAllCardBundles() {
		ArrayList<CardBundles> bundles = new ArrayList<CardBundles>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select CARD_TYPE, DENOMINATION, BULK_NO, START_SERIAL, END_SERIAL, IS_ACCEPTED, IS_ACCEPT_UPDATE, IS_ALL_SOLD, ENTRY_DATE, DISCOUNT_RATE, CARD_RESPONSE, IS_SYNCH, NO_OF_CARDS from TSR_RECEIVED_CARD_BUNDLES";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			bundles.add(new CardBundles(cursor.getString(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getInt(10), cursor.getInt(11), cursor.getInt(12)));
		}
		cursor.close();
		db.close();
		return bundles;
	}

	public List<NextSerial> getAllCardNextSerials() {
		ArrayList<NextSerial> serials = new ArrayList<NextSerial>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select EPF_ID,CARD_TYPE,DENOMINATION,IS_ALL_SOLD,BULK_NO,START_SERIAL,NEXT_SERIAL_VALUE,END_SERIAL,IS_SYNCH from NEXT_SERIAL";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			serials.add(new NextSerial(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8)));
		}
		cursor.close();
		db.close();
		return serials;
	}

	public List<SalesHeader> getAllSalesHeader() {
		ArrayList<SalesHeader> headers = new ArrayList<SalesHeader>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select EPF_ID,CARD_TYPE,DENOMINATION,IS_ALL_SOLD,BULK_NO,START_SERIAL,NEXT_SERIAL_VALUE,END_SERIAL,IS_SYNCH from NEXT_SERIAL";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			headers.add(new SalesHeader(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8)));
		}
		cursor.close();
		db.close();
		return headers;
	}

	public List<SalesDetail> getAllSalesDetail() {
		ArrayList<SalesDetail> details = new ArrayList<SalesDetail>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select DETAILS_ID,INVOICE_ID,CARD_TYPE, DENOMINATION,BULK_NO, NO_OF_CARDS,START_SERIAL,END_SERIAL, LINE_AMOUNT, LINE_DISCOUNT, ISUPDATED, IS_SYNCH from SALES_DETAILS";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			details.add(new SalesDetail(cursor.getInt(0), cursor.getLong(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getDouble(8), cursor.getDouble(9), cursor.getInt(10), cursor.getInt(11)));
		}
		cursor.close();
		db.close();
		return details;
	}

	public void UpdateLastSysnhDate() {
		SQLiteDatabase db = this.getReadableDatabase();
		String synchdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String sql = "UPDATE TSR_SYSTEM_PROFILE SET LAST_SYNCH_DATE = '" + synchdate + "', IS_SYNCH = '0' ";
		Log.d("This is the system data query : ", sql);
		db.execSQL(sql);
		db.close();
	}

	public String CheckMerchantList(String merchant_name, String merchant_address, String city) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select MERCHANT_ID from MERCHANT_MASTER where MERCHANT_NAME COLLATE NOCASE = '" + merchant_name + "' AND ADDRESS COLLATE NOCASE = '" + merchant_address + "' AND CITY COLLATE NOCASE = '" + city + "' ";
		Cursor cursor = db.rawQuery(sql, null);
		String MerchantID = "";
		while (cursor.moveToNext()) {
			MerchantID = "" + cursor.getLong(0);
		}
		cursor.close();
		db.close();
		return MerchantID;
	}

	public String getCardsDetails() {
		SQLiteDatabase db = this.getReadableDatabase();
		String CARD_GET_DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String sql = "SELECT LAST_CARD_DATE FROM TSR_SYSTEM_PROFILE";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			CARD_GET_DATE = cursor.getString(0);
		}
		cursor.close();
		db.close();

		return CARD_GET_DATE;
	}

	public boolean ChekMerchantActivate(String name, String mid) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select IS_ACTIVE from MERCHANT_MASTER where MERCHANT_NAME = '" + name + "' AND MERCHANT_ID = '" + mid + "' ";

		Cursor cursor = db.rawQuery(sql, null);
		int Activation = 0;
		while (cursor.moveToNext()) {
			Activation = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		if (Activation == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void UpdateMerchantActivations(MearchActivation mearchActivation) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "UPDATE MERCHANT_MASTER SET IS_ACTIVE = '" + mearchActivation.getActivation() + "' WHERE MERCHANT_ID = '" + mearchActivation.getMerchantId() + "' AND MERCHANT_NAME = '" + mearchActivation.getMerchantname() + "' ";
		Log.d("This is the system data query : ", sql);
		db.execSQL(sql);
		db.close();
	}

	public void SaveNetworkStatus(String string, String format) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("insert into TSR_3G_Status(Status_Date ,Status, IS_SYNCH) values('" + format + "','" + string + "',0)");
		db.close();
	}

	public void SaveNetworkStatus(boolean isConnect) {
		String msg = "DisConnect";
		if (isConnect) {
			msg = "Connect";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("insert into TSR_3G_Status(Status_Date ,Status, IS_SYNCH) values('" + sdf.format(new Date()) + "','" + msg + "',0)");
		db.close();
	}

	public List<String> GetNetworkStatus() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT Status_Date ,Status FROM TSR_3G_Status WHERE IS_SYNCH = 0", null);
			while (cursor.moveToNext()) {
				list.add(cursor.getString(0) + " # " + cursor.getString(1));
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public void updateNetworkStatus(String string) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = " UPDATE TSR_3G_Status SET IS_SYNCH = 1 WHERE IS_SYNCH = 0 AND Status_Date = '" + string + "' ";
		db.execSQL(sql);
		db.close();
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public ArrayList<MerchantNew> GetNonSynchMerchantsList() {
		ArrayList<MerchantNew> list = new ArrayList<MerchantNew>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select MERCHANT_ID, MERCHANT_NAME, ADDRESS, (case when RELOAD_NUMBER is null then 0 else RELOAD_NUMBER end)RELOAD_NUMBER, LATITUDE, LONGITUDE, " + "(case when REGISTERED_ON is null then date('now') else REGISTERED_ON end)REGISTERED_ON, REGISTERED_BY_EPF_NO, TELEPHONE_NO, CITY, IS_ASSIGNED, IS_EDITED, IS_REGISTERED, IS_ACTIVE, IS_UPDATED " + "from merchant_master where is_synch='0' LIMIT 5";

		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			Long id = cursor.getLong(0);
			String name = cursor.getString(1);
			String address = cursor.getString(2);
			String reloadNo = cursor.getString(3);
			Double latitude = cursor.getDouble(4);
			Double longitude = cursor.getDouble(5);
			String registeredDate = cursor.getString(6);
			Integer registeredBy = cursor.getInt(7);
			String telephoneNo = cursor.getString(8);
			String city = cursor.getString(9);

			Integer isAssigned = cursor.getInt(10);
			Integer isEdited = cursor.getInt(11);
			Integer isRegistered = cursor.getInt(12);
			Integer isActive = cursor.getInt(13);
			Integer isUpdate = cursor.getInt(14);
			list.add(new MerchantNew(id, name, address, reloadNo, latitude, longitude, registeredDate, registeredBy, telephoneNo, city, isAssigned, isEdited, isRegistered, isActive, isUpdate));
		}
		cursor.close();
		db.close();
		return list;
	}

	public int GetNonSynchRegisterMerchantsCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		int rowcount = 0;
		Cursor cursor = db.rawQuery("SELECT count(*) FROM MERCHANT_MASTER WHERE IS_SYNCH = 0 AND IS_REGISTERED = 0 AND IS_ASSIGNED = 0", null);
		while (cursor.moveToNext()) {
			rowcount = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return rowcount;
	}

	public ArrayList<MerchantNew> GetNonSynchRegisterMerchants() {
		ArrayList<MerchantNew> list = new ArrayList<MerchantNew>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select MERCHANT_ID, MERCHANT_NAME, ADDRESS, (case when RELOAD_NUMBER is null then 0 else RELOAD_NUMBER end)RELOAD_NUMBER, LATITUDE, LONGITUDE, (case when REGISTERED_ON is null then date('now') else REGISTERED_ON end)REGISTERED_ON, REGISTERED_BY_EPF_NO, TELEPHONE_NO, CITY, IS_ASSIGNED, IS_EDITED, IS_REGISTERED, IS_ACTIVE, IS_UPDATED from merchant_master where is_synch='0' AND IS_REGISTERED = 0 AND IS_ASSIGNED = 0 LIMIT 5", null);
			while (cursor.moveToNext()) {
				list.add(new MerchantNew(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getInt(10), cursor.getInt(11), cursor.getInt(12), cursor.getInt(13), cursor.getInt(14)));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null){
				cursor.close();
			}
			db.close();
		}

		return list;
	}

	public ArrayList<MerchantNew> GetAllRegisteredMerchants() {
		ArrayList<MerchantNew> list = new ArrayList<MerchantNew>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select MERCHANT_ID, MERCHANT_NAME, ADDRESS, (case when RELOAD_NUMBER is null then 0 else RELOAD_NUMBER end)RELOAD_NUMBER, LATITUDE, LONGITUDE, (case when REGISTERED_ON is null then date('now') else REGISTERED_ON end)REGISTERED_ON, REGISTERED_BY_EPF_NO, TELEPHONE_NO, CITY, IS_ASSIGNED, IS_EDITED, IS_REGISTERED, IS_ACTIVE, IS_UPDATED from merchant_master", null);
		while (cursor.moveToNext()) {
			list.add(new MerchantNew(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getInt(10), cursor.getInt(11), cursor.getInt(12), cursor.getInt(13), cursor.getInt(14)));

		}
		cursor.close();
		db.close();
		return list;
	}

	public int GetNonSynchStockDataFromRemoteCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		int rowcount = 0;
		Cursor cursor = db.rawQuery("SELECT count(*) FROM merchant_inventory where is_synch='0' ", null);
		while (cursor.moveToNext()) {
			rowcount = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return rowcount;
	}

	public ArrayList<MerchantInventorySynch> GetNonSynchMerchantInventoryByLimit() {
		ArrayList<MerchantInventorySynch> list = new ArrayList<MerchantInventorySynch>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = " select merchant_id,card_type,denomination,stock_in_hand,reorder_level,SALE_COUNT from merchant_inventory where is_synch='0' LIMIT 5 ";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			list.add(new MerchantInventorySynch(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public int GetNonSynchCardSalesCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<cardSalesHeader> GetNonSynchcardSalesHeaderByLimit() {
		ArrayList<cardSalesHeader> list = new ArrayList<cardSalesHeader>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT INVOICE_ID,MERCHANT_ID,LATITUDE,LONGITUDE,INVOICE_TOTAL,ENTERED_DATE,ENTERED_BY,TOTAL_DISCOUNT FROM SALES_HEADER WHERE IS_SYNCH='0' LIMIT 5 ", null);
		while (cursor.moveToNext()) {
			ArrayList<cardSalesDeatils> salesDetails = new ArrayList<cardSalesDeatils>();
			Double headerTotall = cursor.getDouble(4);
			cardSalesHeader cardSalesHeader = new cardSalesHeader(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getString(5), cursor.getInt(6), cursor.getDouble(7));
			long id = cursor.getLong(0);
			SQLiteDatabase db1 = this.getReadableDatabase();
			Cursor cursor2 = db1.rawQuery("SELECT DETAILS_ID,CARD_TYPE,DENOMINATION,BULK_NO,NO_OF_CARDS,START_SERIAL,LINE_AMOUNT,LINE_DISCOUNT,INVOICE_ID FROM SALES_DETAILS WHERE IS_SYNCH='0' AND INVOICE_ID = '" + id + "' ", null);
			double detailTotall = 0.0;
			while (cursor2.moveToNext()) {
				detailTotall = detailTotall + cursor2.getDouble(6);
				salesDetails.add(new cardSalesDeatils(cursor2.getInt(0), cursor2.getString(1), cursor2.getInt(2), cursor2.getInt(3), cursor2.getInt(4), cursor2.getString(5), cursor2.getDouble(6), cursor2.getDouble(7), cursor2.getLong(8)));
			}
			cursor2.close();
			db1.close();
			cardSalesHeader.setCardSalesDeatils(salesDetails);
			if (detailTotall == headerTotall) {
				list.add(cardSalesHeader);
			}
		}
		cursor.close();
		db.close();
		return list;
	}

	public boolean checkNonSynchInvoices(Long valueOf) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select count(INVOICE_ID) from SALES_HEADER where MERCHANT_ID  = '" + valueOf + "' ";
		Cursor cursor = db.rawQuery(sql, null);
		Boolean Invoices = true;
		while (cursor.moveToNext()) {
			if (cursor.getInt(0) > 0) {
				Invoices = false;
			} else {
				Invoices = true;
			}
		}
		cursor.close();
		db.close();
		return Invoices;
	}

	public void SaveCARD_CASH_BALANCE(String string, int cardcount, double lineAmount, int handCardCount, Double handOnAmmount) {
		SQLiteDatabase db = this.getWritableDatabase();
		// db.execSQL("DELETE FROM CARD_CASH_BALANCE ");
		// db.execSQL("DELETE FROM CASHIER_SYNCH ");
		db.execSQL("insert into CARD_CASH_BALANCE (Denomination_Type, CARD_IN_HAND, CARD_IN_HAND_AMOUNT, CARD_SALES, CARD_SALES_AMOUNT) " + "values('" + string + "','" + cardcount + "','" + new DecimalFormat("####.00").format(lineAmount) + "','" + handCardCount + "','" + new DecimalFormat("####.00").format(handOnAmmount) + "')");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		db.execSQL("insert into CASHIER_SYNCH (SYNCH_DATE,CONFIRMATION) values ('" + df.format(new Date()) + "','0')");
		db.close();
	}

	public void SaveConfirmation(String salesamount, String Inhand_Sum, String txtAdjuestAmount, String txtRemarks, String securiry) {
		SQLiteDatabase db = this.getWritableDatabase();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// db.execSQL("DELETE FROM CARD_CASH_BALANCE ");
		db.execSQL("insert into CASHIER_CONFIRM (CONFIRM_DATE,SALE_AMOUNT,IN_HAND_AMOUNT,ADJUST_AMOUNT,ADJUST_REMARK,CONFIRM_BY,ZINC) VALUES " + "('" + df.format(new Date()) + "','" + salesamount + "','" + Inhand_Sum + "','" + txtAdjuestAmount + "','" + txtRemarks + "','" + securiry + "','0')");
		db.close();
	}

	public boolean CheckCashireSynch() {
		SQLiteDatabase db = this.getReadableDatabase();
		boolean rowcount = false;
		Cursor cursor = db.rawQuery("SELECT CONFIRMATION FROM CASHIER_SYNCH ", null);
		while (cursor.moveToNext()) {
			if (cursor.getInt(0) == 0) {
				rowcount = true;
			} else {
				rowcount = false;
			}
		}
		cursor.close();
		db.close();
		return rowcount;
	}

	public ArrayList<Cashire_Card_Balance> Get_CARD_CASH_BALANCE() {
		ArrayList<Cashire_Card_Balance> salesDetails = new ArrayList<Cashire_Card_Balance>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT Denomination_Type, CARD_IN_HAND, CARD_IN_HAND_AMOUNT, CARD_SALES, CARD_SALES_AMOUNT FROM CARD_CASH_BALANCE ", null);
		while (cursor.moveToNext()) {
			salesDetails.add(new Cashire_Card_Balance(cursor.getString(0), cursor.getInt(1), cursor.getDouble(2), cursor.getInt(3), cursor.getDouble(4)));
		}
		cursor.close();
		db.close();
		Log.w("========================", "=====" + salesDetails.toString() + "=====");
		return salesDetails;
	}

	public void Clear_CARD_CASH_BALANCE() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM CARD_CASH_BALANCE ");
		db.execSQL("DELETE FROM CASHIER_SYNCH ");
		db.close();
	}

	public ArrayList<SynchCashierConfirmation> GetCashierConfirmationDetails() {
		ArrayList<SynchCashierConfirmation> list = new ArrayList<SynchCashierConfirmation>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select CONFIRM_DATE, SALE_AMOUNT, IN_HAND_AMOUNT, ADJUST_AMOUNT, ADJUST_REMARK, CONFIRM_BY from CASHIER_CONFIRM WHERE ZINC = 0 ";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			list.add(new SynchCashierConfirmation(cursor.getString(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4), cursor.getString(5)));
		}
		cursor.close();
		db.close();
		return list;
	}

	public void ReturnCards(String Deno, String bulk, String startserial, String nextserial, String endserial, String NumberOfCard, String returntype) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (nextserial.equals("") || nextserial == null || nextserial.isEmpty()) {
			String sql = " UPDATE TSR_RECEIVED_CARD_BUNDLES SET CARD_RETURNS = 1, IS_ALL_SOLD = 1 WHERE DENOMINATION = '" + Deno + "' AND BULK_NO = '" + bulk + "' AND START_SERIAL = '" + startserial + "' AND IS_ACCEPTED = '0' AND END_SERIAL = '" + endserial + "' ";
			db.execSQL(sql);
		} else {
			String sql = " UPDATE NEXT_SERIAL SET CARD_RETURNS = 1, IS_ALL_SOLD = 1 WHERE DENOMINATION = '" + Deno + "' AND BULK_NO = '" + bulk + "' AND START_SERIAL = '" + startserial + "' AND NEXT_SERIAL_VALUE = '" + nextserial + "' AND END_SERIAL = '" + endserial + "' ";
			String sql1 = " UPDATE TSR_RECEIVED_CARD_BUNDLES SET CARD_RETURNS = 1, IS_ALL_SOLD = 1 WHERE DENOMINATION = '" + Deno + "' AND BULK_NO = '" + bulk + "' AND START_SERIAL = '" + startserial + "' AND IS_ACCEPTED = '1' AND END_SERIAL = '" + endserial + "' ";
			db.execSQL(sql);
			db.execSQL(sql1);
		}
		String sql = " insert into TSR_CARD_RETURNS (CARD_TYPE, DENOMINATION, BULK_NO, START_SERIAL,NEXT_SERIAL, END_SERIAL, NO_OF_CARDS, RETURN_TYPE, IS_SYNCH) VALUES  ('CDMA_PP','" + Deno + "','" + bulk + "','" + startserial + "','" + nextserial + "','" + endserial + "', '" + NumberOfCard + "', '" + returntype + "', '0') ";
		db.execSQL(sql);
		db.close();

	}

	public ArrayList<CardReturnsData> GetCaedReturnsDetails() {
		ArrayList<CardReturnsData> cardReturnsDatas = new ArrayList<CardReturnsData>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT CARD_TYPE, DENOMINATION, BULK_NO, START_SERIAL,NEXT_SERIAL, END_SERIAL, NO_OF_CARDS, RETURN_TYPE FROM TSR_CARD_RETURNS WHERE IS_SYNCH = 0 ", null);
		while (cursor.moveToNext()) {
			cardReturnsDatas.add(new CardReturnsData(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(7)));
		}
		cursor.close();
		db.close();
		return cardReturnsDatas;
	}

	public void ReturnCards_reject(String denomination, String bulkcode, String cardstartserial, String cardendserila, String noc, String type) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "UPDATE TSR_CARD_RETURNS SET RETURN_TYPE = 'D', IS_SYNCH = '0' WHERE DENOMINATION = '" + denomination + "' AND BULK_NO = '" + bulkcode + "' AND START_SERIAL = '" + cardstartserial + "' AND END_SERIAL = '" + cardendserila + "' ";
		String sql1 = "UPDATE NEXT_SERIAL SET CARD_RETURNS = '0', IS_ALL_SOLD = 0 WHERE DENOMINATION = '" + denomination + "' AND BULK_NO = '" + bulkcode + "' AND START_SERIAL = '" + cardstartserial + "'  AND END_SERIAL = '" + cardendserila + "' ";
		String sql2 = "UPDATE TSR_RECEIVED_CARD_BUNDLES SET CARD_RETURNS = '0', IS_ALL_SOLD = 0 WHERE DENOMINATION = '" + denomination + "' AND BULK_NO = '" + bulkcode + "' AND START_SERIAL = '" + cardstartserial + "' AND END_SERIAL = '" + cardendserila + "' ";
		String sql3 = "DELETE FROM TSR_CARD_RETURNS WHERE DENOMINATION = '" + denomination + "' AND BULK_NO = '" + bulkcode + "' AND START_SERIAL = '" + cardstartserial + "' AND END_SERIAL = '" + cardendserila + "' ";
		db.execSQL(sql3);
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql);
		db.close();
	}

	public void UpdateCardReturns(CardReturnsConfirmData returnsConfirmData) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "UPDATE TSR_CARD_RETURNS SET IS_SYNCH = '1', CARD_RETURNS = '" + returnsConfirmData.getCARD_RETURNS() + "'  WHERE CARD_TYPE = '" + returnsConfirmData.getCARD_TYPE() + "' AND DENOMINATION = '" + returnsConfirmData.getDENOMINATION() + "' AND BULK_NO = '" + returnsConfirmData.getBULK_NO() + "' AND START_SERIAL = '" + returnsConfirmData.getSTART_SERIAL() + "' AND END_SERIAL = '" + returnsConfirmData.getEND_SERIAL() + "' ";
		if (returnsConfirmData.getCARD_RETURNS() == 1) {
			String sql1 = "UPDATE NEXT_SERIAL SET CARD_RETURNS = '" + returnsConfirmData.getCARD_RETURNS() + "', IS_ALL_SOLD = 1 WHERE DENOMINATION = '" + returnsConfirmData.getDENOMINATION() + "' AND BULK_NO = '" + returnsConfirmData.getBULK_NO() + "' AND START_SERIAL = '" + returnsConfirmData.getSTART_SERIAL() + "' AND END_SERIAL = '" + returnsConfirmData.getEND_SERIAL() + "' ";
			String sql2 = "UPDATE TSR_RECEIVED_CARD_BUNDLES SET CARD_RETURNS = '" + returnsConfirmData.getCARD_RETURNS() + "', IS_ALL_SOLD = 1 WHERE DENOMINATION = '" + returnsConfirmData.getDENOMINATION() + "' AND BULK_NO = '" + returnsConfirmData.getBULK_NO() + "' AND START_SERIAL = '" + returnsConfirmData.getSTART_SERIAL() + "' AND END_SERIAL = '" + returnsConfirmData.getEND_SERIAL() + "' ";
			db.execSQL(sql1);
			db.execSQL(sql2);
		} else {
			String sql1 = "UPDATE NEXT_SERIAL SET CARD_RETURNS = '" + returnsConfirmData.getCARD_RETURNS() + "', IS_ALL_SOLD = 0 WHERE DENOMINATION = '" + returnsConfirmData.getDENOMINATION() + "' AND BULK_NO = '" + returnsConfirmData.getBULK_NO() + "' AND START_SERIAL = '" + returnsConfirmData.getSTART_SERIAL() + "'  AND END_SERIAL = '" + returnsConfirmData.getEND_SERIAL() + "' ";
			String sql2 = "UPDATE TSR_RECEIVED_CARD_BUNDLES SET CARD_RETURNS = '" + returnsConfirmData.getCARD_RETURNS() + "', IS_ALL_SOLD = 0 WHERE DENOMINATION = '" + returnsConfirmData.getDENOMINATION() + "' AND BULK_NO = '" + returnsConfirmData.getBULK_NO() + "' AND START_SERIAL = '" + returnsConfirmData.getSTART_SERIAL() + "' AND END_SERIAL = '" + returnsConfirmData.getEND_SERIAL() + "' ";
			db.execSQL(sql1);
			db.execSQL(sql2);
		}
		Log.d("sys data query:", sql);
		db.execSQL(sql);
		db.close();
	}

	public void UpdateTheCardReturns(ArrayList<CardReturnsData> cardReturnsDatas) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "DELETE FROM TSR_CARD_RETURNS WHERE IS_SYNCH = '0' ";
		ArrayList<CardReturnsData> cardReturnsData = cardReturnsDatas;
		for (CardReturnsData cardReturnsData2 : cardReturnsData) {
			if (cardReturnsData2.getCARD_RETURNS().equalsIgnoreCase("C")) {
				String sql1 = "DELETE FROM NEXT_SERIAL WHERE DENOMINATION = '" + cardReturnsData2.getDENOMINATION() + "' AND BULK_NO = '" + cardReturnsData2.getBULK_NO() + "' AND START_SERIAL = '" + cardReturnsData2.getSTART_SERIAL() + "' AND END_SERIAL = '" + cardReturnsData2.getEND_SERIAL() + "' ";
				String sql2 = "DELETE FROM TSR_RECEIVED_CARD_BUNDLES WHERE DENOMINATION = '" + cardReturnsData2.getDENOMINATION() + "' AND BULK_NO = '" + cardReturnsData2.getBULK_NO() + "' AND START_SERIAL = '" + cardReturnsData2.getSTART_SERIAL() + "' AND END_SERIAL = '" + cardReturnsData2.getEND_SERIAL() + "' ";
				db.execSQL(sql1);
				db.execSQL(sql2);
			} else if (cardReturnsData2.getCARD_RETURNS().equalsIgnoreCase("R")) {
				String sql1 = "DELETE FROM NEXT_SERIAL WHERE DENOMINATION = '" + cardReturnsData2.getDENOMINATION() + "' AND BULK_NO = '" + cardReturnsData2.getBULK_NO() + "' AND START_SERIAL = '" + cardReturnsData2.getSTART_SERIAL() + "' AND END_SERIAL = '" + cardReturnsData2.getEND_SERIAL() + "' ";
				String sql2 = "DELETE FROM TSR_RECEIVED_CARD_BUNDLES WHERE DENOMINATION = '" + cardReturnsData2.getDENOMINATION() + "' AND BULK_NO = '" + cardReturnsData2.getBULK_NO() + "' AND START_SERIAL = '" + cardReturnsData2.getSTART_SERIAL() + "' AND END_SERIAL = '" + cardReturnsData2.getEND_SERIAL() + "' ";
				db.execSQL(sql1);
				db.execSQL(sql2);
			} else if (cardReturnsData2.getCARD_RETURNS().equalsIgnoreCase("D")) {
				String sql1 = "UPDATE NEXT_SERIAL SET CARD_RETURNS = '0', IS_ALL_SOLD = 0 WHERE DENOMINATION = '" + cardReturnsData2.getDENOMINATION() + "' AND BULK_NO = '" + cardReturnsData2.getBULK_NO() + "' AND START_SERIAL = '" + cardReturnsData2.getSTART_SERIAL() + "'  AND END_SERIAL = '" + cardReturnsData2.getEND_SERIAL() + "' ";
				String sql2 = "UPDATE TSR_RECEIVED_CARD_BUNDLES SET CARD_RETURNS = '0', IS_ALL_SOLD = 0 WHERE DENOMINATION = '" + cardReturnsData2.getDENOMINATION() + "' AND BULK_NO = '" + cardReturnsData2.getBULK_NO() + "' AND START_SERIAL = '" + cardReturnsData2.getSTART_SERIAL() + "' AND END_SERIAL = '" + cardReturnsData2.getEND_SERIAL() + "' ";
				db.execSQL(sql1);
				db.execSQL(sql2);
			}
		}
		Log.d("This is the system data query : ", sql);
		db.execSQL(sql);
		db.close();
	}

	public int GetInvoiceNumber() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT NEXT_INVOICE_NO FROM TSR_SYSTEM_PROFILE", null);
		Integer id = 0;
		while (cursor.moveToNext()) {
			id = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return id;
	}

	public boolean saveAttendace(Attendance attendace) throws Exception {

		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues cv = attendace.getContentValues();
			long id = db.insert(TABLE_USER_ATTENDANCE, null, cv);
			attendace.setId((int) id);
			db.close();
			if (id > 0) {
				attendace.setId((int) id);
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		} finally {
			db.close();
		}

		return false;
	}

	public boolean updateAttendaceSMSStatus(String text, int i) {
		// TODO Auto-generated method stub
		// String sql = "update " + DBHelper.TABLE_USER_ATTENDANCE +
		// " set SMS_STATUS_CODE = ? where ID = ?";
		SQLiteDatabase db = this.getReadableDatabase();
		// db.execSQL(sql, new String[]{text , "" + i});
		ContentValues cv = new ContentValues();
		cv.put("SMS_STATUS_CODE", i);
		int rows = db.update(TABLE_USER_ATTENDANCE, cv, "ID = ?", new String[] { text });
		db.close();
		if (rows > 0) {
			return true;
		}
		return false;
	}

	public Attendance getLastAttendance() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select * from " + TABLE_USER_ATTENDANCE + " order by ID desc limit 1", null);
		if (cursor.moveToFirst()) {
			Attendance att = Attendance.getInstance(cursor);
			db.close();
			return att;
		}
		return null;
	}

	public boolean isAlreadyAttendanceMarked(Attendance attendace) throws Exception {
		// TODO Auto-generated method stub

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(attendace.getAttendaceDate().getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 1);

		long t1 = cal.getTimeInMillis();
		long t2 = t1 + (1000 * 60 * 60 * 24);

		String sql = "select * from " + TABLE_USER_ATTENDANCE + " where ATTENDANCE_DATE > ? AND ATTENDANCE_DATE < ? AND TYPE = ? AND SMS_STATUS_CODE > 0";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] { "" + t1, "" + t2, attendace.getType() });
		boolean hasAttedance = false;
		if (cursor.moveToFirst()) {
			hasAttedance = true;
		}
		cursor.close();
		db.close();
		return hasAttedance;
	}

	public boolean isAttendanceInMarked(Attendance attendace) {
		Calendar cal = Calendar.getInstance();
		if (attendace != null) {
			cal.setTimeInMillis(attendace.getAttendaceDate().getTime());
		} else {
			cal.setTimeInMillis(System.currentTimeMillis());
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 1);

		long t1 = cal.getTimeInMillis();
		long t2 = t1 + (1000 * 60 * 60 * 24);

		String sql = "select * from " + TABLE_USER_ATTENDANCE + " where ATTENDANCE_DATE > ? AND ATTENDANCE_DATE < ? AND TYPE = 'IN' AND SMS_STATUS_CODE > 0";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] { "" + t1, "" + t2 });
		boolean hasAttedance = false;
		if (cursor.moveToFirst()) {
			hasAttedance = true;
		}
		cursor.close();
		db.close();
		return hasAttedance;
	}

	public Attendance isAttendanceMarked(String type, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 1);

		long t1 = cal.getTimeInMillis();
		long t2 = t1 + (1000 * 60 * 60 * 24);

		String sql = "select * from " + TABLE_USER_ATTENDANCE + " where ATTENDANCE_DATE > " + t1 + " AND ATTENDANCE_DATE < " + t2 + " AND TYPE = '" + type + "' order by ATTENDANCE_DATE limit 1";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		Attendance hasAttedance = null;
		if (cursor.moveToFirst()) {
			// "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
			// "EPF_NUMBER TEXT," +
			// "TEAM_NUMBER TEXT," +
			// "ATTENDANCE_DATE INTEGER," +
			// "TYPE TEXT," +
			// "REASON TEXT," +
			// "STATUS_CODE TEXT," +
			// "SMS_STATUS_CODE INTEGER," +
			// "BIKE TEXT," +
			// "METER TEXT," +
			// "IS_SYNCED INTEGER," +
			// "CREATED_ON INTEGER)";

			int index = 0;
			hasAttedance = new Attendance();
			hasAttedance.setId(cursor.getInt(index++));
			hasAttedance.setEpfNO(cursor.getString(index++));
			hasAttedance.setTeamCode(cursor.getString(index++));
			hasAttedance.setAttendaceDate(new Date(cursor.getLong(index++)));
			hasAttedance.setType(cursor.getString(index++));
			hasAttedance.setReason(cursor.getString(index++));
			hasAttedance.setStatusCode(cursor.getString(index++));
			hasAttedance.setBikeNo(cursor.getString(index++));
			hasAttedance.setOdometer(cursor.getString(index++));

		}
		cursor.close();
		db.close();
		return hasAttedance;
	}

	public void getAllSalesData() {
		String sql = "select h.*,d.* from sales_details d inner join SALES_HEADER h on h.INVOICE_ID=d.INVOICE_ID";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		/*
		 * createTable = "CREATE TABLE IF NOT EXISTS SALES_HEADER(" +
		 * "INVOICE_ID LONG(19) NOT NULL," + "MERCHANT_ID INTEGER(8)," +
		 * "LATITUDE TEXT(12)," + "LONGITUDE TEXT(12)," +
		 * "INVOICE_TOTAL DOUBLE(12,2)," + "ENTERED_DATE TEXT," +
		 * "ENTERED_BY INTEGER(8)," + "TOTAL_DISCOUNT DOUBLE(12,2)," +
		 * "ISUPDATED INTEGER(1),IS_SYNCH INTEGER(1))";
		 * 
		 * db.execSQL(createTable);
		 * 
		 * createTable = "CREATE TABLE IF NOT EXISTS SALES_DETAILS(" +
		 * "DETAILS_ID INTEGER(20) NOT NULL," +
		 * "[INVOICE_ID] LONG(19) NOT NULL CONSTRAINT [INVOICE_ID] REFERENCES [SALES_HEADER]([INVOICE_ID]) ON DELETE CASCADE ON UPDATE CASCADE MATCH FULL,"
		 * //+ "INVOICE_ID LONG(19) NOT NULL," + "CARD_TYPE TEXT(3)NOT NULL," +
		 * "DENOMINATION INTEGER(6)NOT NULL," + "BULK_NO 	 TEXT(10)NOT NULL," +
		 * "NO_OF_CARDS INTEGER(6)," + "START_SERIAL TEXT(10)," +
		 * "END_SERIAL TEXT(10)," + "LINE_AMOUNT DOUBLE(12,2)," +
		 * "LINE_DISCOUNT DOUBLE(12,2)," +
		 * "ISUPDATED INTEGER(1),IS_SYNCH INTEGER(1)) ";
		 */
		if (cursor.moveToFirst()) {
			Log.v("Sales Report", "All sale data");
			do {
				int index = 0;

				String a = cursor.getString(index++);
				String s = cursor.getString(index++);
				String d = cursor.getString(index++);
				String f = cursor.getString(index++);
				String g = cursor.getString(index++);

				String t = a + " " + s + " " + d + " " + f + " " + g;
				Log.v("sales", t);

			} while (cursor.moveToNext());
		}

		db.close();

	}

	public boolean isInvoiceNumberExists(long invoiceId) {
		boolean isExist = false;
		String sql = "select * from SALES_HEADER h where h.INVOICE_ID=" + invoiceId;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				isExist = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.writeToErrLogFileWithTime(e);

		} finally {
			db.close();
		}
		return isExist;

	}

	public long getValiedInvoiceNo(long invoiceId) {
		// TODO Auto-generated method stub

		for (int index = 0; index < 1000; index++) {
			String sql = "select * from SALES_HEADER h where h.INVOICE_ID=" + invoiceId;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				Cursor cursor = db.rawQuery(sql, null);
				if (cursor.moveToNext()) {
					invoiceId++;
				} else {
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.writeToErrLogFileWithTime(e);

			} finally {
				db.close();
			}
		}
		return invoiceId;
	}

	public int getValiedMerchantId(int MerchatId) {
		// TODO Auto-generated method stub

		for (int index = 0; index < 1000; index++) {
			String sql = "select * from MERCHANT_MASTER h where h.MERCHANT_ID=" + MerchatId;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				Cursor cursor = db.rawQuery(sql, null);
				if (cursor.moveToNext()) {
					MerchatId++;
				} else {
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.writeToErrLogFileWithTime(e);

			} finally {
				db.close();
			}
		}
		return MerchatId;
	}

	public void updateServerTodayAttendance(String type, String date) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 1);

		Date attDate = DateTimeFormatings.getDateTimeWeb(date);
		// TODO Auto-generated method stub
		if (isAttendanceMarked(type, attDate) != null) {
			// db.execSQL(sql, new String[]{text , "" + i});
			SQLiteDatabase db = this.getReadableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("SMS_STATUS_CODE", 1);
			int rows = db.update(TABLE_USER_ATTENDANCE, cv, "TYPE = ? AND ATTENDANCE_DATE > ?", new String[] { type, "" + cal.getTimeInMillis() });
			db.close();
			if (rows > 0) {
				Utils.ShowMsg(context, "Updating system attendance");
			}
		} else {
			User user = getUserDetails();
			String epfNo = GetEmpNoByPhoneNo(user.getmobileNo());
			Attendance attendace = new Attendance(epfNo, attDate, type, "", "0", 0, new Date());

			attendace.setSmsStatusCode(2);

			try {
				saveAttendace(attendace);
			} catch (Exception e) {
				Utils.writeToErrLogFileWithTime(e);
			}

		}
	}

	public String getServiceUrl() {
		String url = "";
		SQLiteDatabase db = getWritableDatabase();

		try {
			String sql = "select URL from " + TABLE_SERVICE_URLS + " where ID = 1";
			Cursor cursor =  db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				url = cursor.getString(0).trim();
			}  
		} catch (Exception e) {
			Utils.writeToErrLogFileWithTime(e);
		} finally {
			db.close();
		}

		return url;
	}
	public boolean updateServiceUrl(String url) { 
		SQLiteDatabase db = getWritableDatabase();
		boolean isSuccess = false;
		try {
			ContentValues cv = new ContentValues();
			url = url;
			cv.put("URL", url);
			int rows =  db.update(TABLE_SERVICE_URLS, cv, "ID = ?", new String[]{"1"});
			if (rows > 0) {
				isSuccess = true;
			}
			  
		} catch (Exception e) {
			Utils.writeToErrLogFileWithTime(e);
		} finally {
			db.close();
		}
		
		return isSuccess;
	}

}