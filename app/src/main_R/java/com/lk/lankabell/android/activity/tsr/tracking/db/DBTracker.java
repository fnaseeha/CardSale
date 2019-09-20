package com.lk.lankabell.android.activity.tsr.tracking.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;
import com.lk.lankabell.android.activity.tsr.tracking.domain.Locations;
import com.lk.lankabell.android.activity.tsr.tracking.domain.UserLocation;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;

public class DBTracker extends SQLiteOpenHelper {

	private UserLocation userLastLocation;

	public DBTracker(Context context) {
		super(context, DB_NAME, null, version);
		// TODO Auto-generated constructor stub
	}
	
	

	private static final String DB_NAME = "gps_track";
	private static int version = 1;

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		String sql_user_location = "create table locations (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "epf TEXT," + "lat TEXT," + "lon TEXT," + "accuracy TEXT," + "sys_date INTEGER,"+ "speed TEXT,"+ "usetime TEXT," + "synced INTEGER  DEFAULT 0)";

		arg0.execSQL(sql_user_location);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public boolean addLocation(String epf, String lat, String lon, String accuracy, long time,String speed,String usetime) {

		if (DateTimeFormatings.isInWorkingHours()) {
			 
			
			userLastLocation = getLastLocation();
			boolean isDiffrentFromLast = true;
			if (userLastLocation.getId() > 0) {
				
				if(lat.equals("" + userLastLocation.getLat()) && lon.equals("" + userLastLocation.getLon())){
					isDiffrentFromLast = false;
				}
				if (lat.equals("" + userLastLocation.getLat()) && lon.equals("" + userLastLocation.getLon()) && (time <=  userLastLocation.getSystemDate().getTime() + (1000 * 60 * 20))) {

					return false;
				} 
				if (Double.parseDouble(lat) < 5 || Double.parseDouble(lat) > 10) {
					return false;
				}
				if (Double.parseDouble(lon) < 79 || Double.parseDouble(lon) > 82) {
					return false;
				}
			}
			ContentValues cv = new ContentValues();
			cv.put("epf", epf);
			cv.put("lat", lat);
			cv.put("lon", lon);
			cv.put("accuracy", accuracy);
			cv.put("sys_date", time);
			cv.put("speed", speed);
			cv.put("usetime", usetime);
			cv.put("synced", 0);
			
			
			
			long id = 0;
			UserLocation exLoc = getLocation(lat, lon);
			SQLiteDatabase db = getWritableDatabase();
//			if (exLoc == null) {
			if (isDiffrentFromLast) {
				id = db.insert("locations", null, cv);			
				Utils.writeToLocationLogFile(String.format("Time : %s -- speed : %s -- accuracy : %s", "" + time , "" + speed , "" + accuracy));
			
			}else{
				 				
					try {
						id = db.update("locations", cv, "", new String[]{"" + exLoc.getId()});
					} catch (Exception e) {
						Utils.writeToLogFileWithTime("location update error: " + ((e != null) ? e.getLocalizedMessage() : ""));
					}
				
				 	 
			}
			db.close();
			if (id > 0) {
				return true;
			}
		}
		return false;
	}
	
	
	private boolean isLate(UserLocation exLoc) {
		 
		if(System.currentTimeMillis() - exLoc.getSystemDate().getTime()  > (1000 * 60 * 10)){
			return true;
		}
		
		 
		return false;
	}

	public UserLocation getLocation(String lat,String lon){
		UserLocation loc = null;
		String sql = "select * from locations where lat like '" + lat +"%' and lon like '"+ lon +"%' order by sys_date desc limit 1";

		SQLiteDatabase db = getWritableDatabase();

		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			loc = UserLocation.getInstance(cursor);
		}
		cursor.close();
		db.close();
		return loc;
	}

	public UserLocation getLastLocation() {
		UserLocation loc = new UserLocation();

		String sql = "select * from locations order by sys_date desc limit 1";

		SQLiteDatabase db = getWritableDatabase();

		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				loc = UserLocation.getInstance(cursor);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return loc;
	}

	public ArrayList<UserLocation> getLocations(boolean isSynced) {
		ArrayList<UserLocation> list = new ArrayList<UserLocation>();

		String sql;
		if (isSynced) {
			sql = "select * from locations where synced = 0 order by sys_date desc limit 1500";
		} else {
			sql = "select * from locations order by sys_date desc limit 1500";
		}

		SQLiteDatabase db = getWritableDatabase();

		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				list.add(UserLocation.getInstance(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return list;
	}

	public void clearLocationData() {
		// TODO Auto-generated method stub
		String sql = "delete from locations";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}

	public void updateLocation(String string) {
		// TODO Auto-generated method stub
		try {
			ContentValues cv = new ContentValues();
			cv.put("synced", 1);
			SQLiteDatabase db = getWritableDatabase();
			long syncedDateTime = DateTimeFormatings.getDateTimeWeb(string).getTime();
			long success =  db.update("locations", cv, "sys_date=?", new String[] { "" + syncedDateTime });
			//db.delete("locations", "sys_date=?", new String[] { "" + syncedDateTime });
			String msg = string + " : " + syncedDateTime + " -- " +  success + "\n";
			Log.v("location synced :",msg);
			Utils.writeLoactionSyncReport(msg);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<Locations> getLocationsUnSynced() {
		ArrayList<Locations> list = new ArrayList<Locations>();

		String sql = "select * from locations where synced = 0 order by sys_date";

		SQLiteDatabase db = getWritableDatabase();

		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				Locations tLocation = Locations.getInstance(cursor);
				if (tLocation != null) {
					list.add(tLocation);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return list;
	}

	public void clearLocationData(long timeInMillis) {
		// TODO Auto-generated method stub
		String sql = "delete from locations where sys_date <" + timeInMillis;
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}
}
