package com.lk.lankabell.android.activity.tsr.sync;

import java.util.Date;
import java.util.List;

import android.util.Log;

import com.lk.lankabell.android.activity.tsr.beans.SynchReport;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

public class preSync extends SysncServices{
	
	public DatabaseHandler dbh;

	public void presynch(DatabaseHandler databaseHandler) {
		this.dbh = databaseHandler;
		int precreatedMerchants = 0, preassignedMerchants = 0, preregisteredMerchants = 0, precreatedAllMerchants = 0, preassignedAllMerchants = 0, preregisteredAllMerchants = 0;
		int preSynchSalesDetails = 0, preAllSalesDetails = 0, preSynchSalesHeader = 0, preAllSalesHeader = 0, preSynchBulkserials = 0, preAllBulkserials = 0, preSynchNextserials = 0, preAllNextserials = 0;
		int preSynchCitys = 0, preAllCitys = 0, preSynchInventory = 0, preAllInventory = 0;
		List<SynchReport> list = dbh.getSynchReport();

		for (SynchReport SynchReport : list) {
			Log.d("Current Equals Modle Name : ", SynchReport.getComponent());
			if (SynchReport.getComponent().equals("Created Merchants")) {				
				precreatedAllMerchants = Integer.parseInt(SynchReport.getTotalCount());
				if (precreatedAllMerchants > 0) {
					precreatedMerchants = Integer.parseInt(SynchReport.getSynchCount());
				} 
				dbh.insertSynchLog(new Date().toString(), "Merchant", "Created", precreatedMerchants, precreatedAllMerchants, 0, 0, 0);
			} else if (SynchReport.getComponent().equals("Assigned Merchants")) {
				preassignedAllMerchants = Integer.parseInt(SynchReport.getTotalCount());
				if (preassignedAllMerchants > 0) {
					preassignedMerchants = Integer.parseInt(SynchReport.getSynchCount());
				} 
				dbh.insertSynchLog(new Date().toString(), "Merchant", "Assigned", preassignedMerchants, preassignedAllMerchants, 0, 0, 0);
			} else if (SynchReport.getComponent().equals("Register Merchants")) {				
				preregisteredAllMerchants = Integer.parseInt(SynchReport.getTotalCount());
				if (preregisteredAllMerchants > 0) {
					preregisteredMerchants = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLog(new Date().toString(), "Merchant", "Register", preregisteredMerchants, preregisteredAllMerchants, 0, 0, 0);
			} else if (SynchReport.getComponent().equals("Sales Details")) {
				preAllSalesDetails = Integer.parseInt(SynchReport.getTotalCount());
				if (preAllSalesDetails > 0) {
					preSynchSalesDetails = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLog(new Date().toString(), "Sales", "Details", preSynchSalesDetails, preAllSalesDetails, 0, 0, 0);
			} else if (SynchReport.getComponent().equals("Sales Header")) {				
				preAllSalesHeader = Integer.parseInt(SynchReport.getTotalCount());
				if (preAllSalesHeader > 0) {
					preSynchSalesHeader = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLog(new Date().toString(), "Sales", "Header", preSynchSalesHeader, preAllSalesHeader, 0, 0, 0);
			} else if (SynchReport.getComponent().equals("Cards Acceptance")) {				
				preAllBulkserials = Integer.parseInt(SynchReport.getTotalCount());
				if (preAllBulkserials > 0) {
					preSynchBulkserials = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLog(new Date().toString(), "Cards", "Acceptance", preSynchBulkserials, preAllBulkserials, 0, 0, 0);
			} else if (SynchReport.getComponent().equals("Next Serial")) {				
				preAllNextserials = Integer.parseInt(SynchReport.getTotalCount());
				if (preAllNextserials > 0) {
					preSynchNextserials = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLog(new Date().toString(), "Cards", "NextSerial", preSynchNextserials, preAllNextserials, 0, 0, 0);
			} else if (SynchReport.getComponent().equals("Cities")) {				
				preAllCitys = Integer.parseInt(SynchReport.getTotalCount());
				if (preAllCitys > 0) {
					preSynchCitys = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLog(new Date().toString(), "City", "My City", preSynchCitys, preAllCitys, 0, 0, 0);
			} else if (SynchReport.getComponent().equals("Merchant Inventory")) {				
				preAllInventory = Integer.parseInt(SynchReport.getTotalCount());
				if (preAllInventory > 0) {
					preSynchInventory = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLog(new Date().toString(), "Merchant", "Inventory", preSynchInventory, preAllInventory, 0, 0, 0);
			}
		}

		int preSynchMerchants = precreatedMerchants + preassignedMerchants + preregisteredMerchants;
		int preAllMerchants = precreatedAllMerchants + preassignedAllMerchants + preregisteredAllMerchants;
		dbh.insertSynchLog(new Date().toString(), "Merchant", "Merchant", preSynchMerchants, preAllMerchants, 0, 0, 0);
	}

}
