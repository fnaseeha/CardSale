package com.lk.lankabell.android.activity.tsr.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "TSRDB";

	// table names
	private static final String TABLE_LOGIN = "TSR_LOGIN_INFO";
	private static final String TABLE_GEO = "TSR_GEO_INFO";
	private static final String TABLE_LOC = "TSR_LOCATION_INFO";

	// Table Columns names
	private static final String KEY_ID = "EMP_ID";
	private static final String KEY_NAME = "TSR_NAME";
	private static final String KEY_PW = "PASSWORD";

	private static final String GEO_ID = "GEO_ID";
	private static final String GEO_LAT = "GEO_LATITUDE";
	private static final String GEO_LON = "GEO_LONGITUDE";
	private static final String GEO_TIME = "GEO_TIME";

	private SqliteHandler(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		createTables(this.getWritableDatabase());
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	private static SqliteHandler instance;

	public static SqliteHandler getInstance(Context context) {

		if (instance == null) {

			instance = new SqliteHandler(context.getApplicationContext());
		}
		return instance;
	}

	public void createTables(SQLiteDatabase db) {

		String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PW + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);

		CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GEO + "(" + GEO_ID
				+ " INTEGER PRIMARY KEY," + GEO_LAT + " TEXT," + GEO_TIME
				+ " TEXT," + GEO_LON + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);

		CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOC + "("
				+ "LOC_ID" + " INTEGER PRIMARY KEY," + "LOCATION" + " TEXT,"
				+ "TSR_ID" + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);
	}

	public void save(String tableName, ContentValues values) {
		SQLiteDatabase db = this.getWritableDatabase();
		// Inserting Row
		db.insert(tableName, null, values);
		db.close(); // Closing database connection
	}

}
