package com.lk.lankabell.android.activity.tsr.beans;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;

import android.content.ContentValues;
import android.database.Cursor;

public class Attendance {

	int id;
	String epfNO;
	Date attendaceDate;
	String type;
	String reason;
	String statusCode;
	int smsStatusCode;
	int isSyncd;
	Date created_on;
	String teamCode;
	
	private String bikeNo;
	private String odometer;
	
	
	
	public Attendance() {
		super();
	}


	public Attendance(String epfNO, Date attendaceDate, String type,
			String reason, String statusCode, int isSyncd, Date created_on) {
		super();
		this.epfNO = epfNO;
		this.attendaceDate = attendaceDate;
		this.type = type;
		this.reason = reason;
		this.statusCode = statusCode;
		this.isSyncd = isSyncd;
		this.created_on = created_on;
	}
	
	
	public String getBikeNo() {
		return bikeNo;
	}


	public void setBikeNo(String bikeNo) {
		this.bikeNo = bikeNo;
	}


	public String getOdometer() {
		return odometer;
	}


	public void setOdometer(String odometer) {
		this.odometer = odometer;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEpfNO() {
		return epfNO;
	}
	public void setEpfNO(String epfNO) {
		this.epfNO = epfNO;
	}
	public Date getAttendaceDate() {
		return attendaceDate;
	}
	public void setAttendaceDate(Date attendaceDate) {
		this.attendaceDate = attendaceDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public int getIsSyncd() {
		return isSyncd;
	}
	public void setIsSyncd(int isSyncd) {
		this.isSyncd = isSyncd;
	}
	public Date getCreated_on() {
		return created_on;
	}
	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}
	 
	public void setDate(long milis){
		this.attendaceDate =  new Date(milis);
	}
 	public int getSmsStatusCode() {
		return smsStatusCode;
	}
 	public void setSmsStatusCode(int smsStatusCode) {
		this.smsStatusCode = smsStatusCode;
	}
 	
 	
 	
 	
 	public String getTeamCode() {
		return teamCode;
	}


	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public int getInorOut(){
		return (type.equalsIgnoreCase("IN")) ? 1 : 2;
	}
	
	

	public ContentValues getContentValues() {
		 ContentValues cv = new ContentValues();
		 cv.put("EPF_NUMBER", this.epfNO);
		 cv.put("TEAM_NUMBER", this.teamCode);
		 cv.put("ATTENDANCE_DATE", attendaceDate.getTime());
		 cv.put("TYPE", this.type);
		 cv.put("REASON", this.reason);
		 cv.put("STATUS_CODE", this.statusCode);
		 cv.put("SMS_STATUS_CODE", this.smsStatusCode);
		 cv.put("IS_SYNCED", this.isSyncd);
		 cv.put("CREATED_ON", this.created_on.getTime());
		   
		return cv;
	}


	public static Attendance getInstance(Cursor cursor) {
		Attendance attendance = new Attendance();
		int index = 0;
		attendance.setId(cursor.getInt(index++));
		attendance.setEpfNO(cursor.getString( index++));
		attendance.setTeamCode(cursor.getString( index++));
		Date atDate = new Date();
		atDate.setTime(cursor.getLong(index++));		
		attendance.setAttendaceDate(atDate);
		attendance.setType(cursor.getString(index++));
		attendance.setReason(cursor.getString(index++));
		attendance.setStatusCode(cursor.getString(index++).equals("0") ? "Pending" : "Done");
		attendance.setSmsStatusCode(cursor.getInt(index++));
		attendance.setIsSyncd(cursor.getInt(index++));
		long mili_create = cursor.getLong(index++);
		attendance.setCreated_on(new Date(mili_create));
		return attendance;
	}


	@Override
	public String toString() {
		return "Attendance [id=" + id + ", epfNO=" + epfNO + ", attendaceDate="
				+ attendaceDate + ", type=" + type + ", reason=" + reason
				+ ", statusCode=" + statusCode + ", smsStatusCode="
				+ smsStatusCode + ", isSyncd=" + isSyncd + ", created_on="
				+ created_on + ", teamCode=" + teamCode + ", bikeNo=" + bikeNo
				+ ", odometer=" + odometer + "]";
	}


	public String toJsonRequest() throws JSONException {
		JSONObject obj = new JSONObject(); 
		obj.put("id", id);
		obj.put("epf", epfNO);
		obj.put("team_code", teamCode);
		obj.put("type", type); 
		obj.put("reason", reason); 
		obj.put("bike", bikeNo); 
		obj.put("meter", odometer); 
		obj.put("date", DateTimeFormatings.getDateTime(attendaceDate));
		return obj.toString();
	}
 
	
}
