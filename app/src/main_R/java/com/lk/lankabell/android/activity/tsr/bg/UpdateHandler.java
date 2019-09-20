package com.lk.lankabell.android.activity.tsr.bg;

import android.content.Context;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.ws.ServiceTransaction;

public class UpdateHandler {

	private DatabaseHandler dbh;
	private ServiceTransaction st;
	
	

	public UpdateHandler(Context context) {
		dbh = new DatabaseHandler(context);
		st = new ServiceTransaction();
	}
/*
	public ArrayList<User> updateLoginInfo() {

		ArrayList<User> list = new ArrayList<User>();

		//int maxId = dbh.maxTransIdOfUser();
		
		//st.fetchLoginInfo(maxId);

		for (Iterator<User> iterator = list.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			
			dbh.addUser(user);
			
		}
		
		return list;

	}
	*/
}
