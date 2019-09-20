package com.lk.lankabell.android.activity.tsr.sync;

import java.util.List;

import android.util.Log;

import com.lk.lankabell.android.activity.tsr.beans.SynchReport;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

public class postSync extends preSync {

	public DatabaseHandler dbh;

	public void postsynch(DatabaseHandler databaseHandler, int merchant, int Version, int CardBulkSerial, int SaleData, int CardAccept, int StockDataFromRemote, int g, int UpdatedCities, int NextSerialDetails) {
		this.dbh = databaseHandler;
		int postcreatedMerchants = 0, postassignedMerchants = 0, postregisteredMerchants = 0, postcreatedAllMerchants = 0, postassignedAllMerchants = 0, postregisteredAllMerchants = 0;
		int postSynchSalesDetails = 0, postAllSalesDetails = 0, postSynchSalesHeader = 0, postAllSalesHeader = 0, postSynchBulkserials = 0, postAllBulkserials = 0, postSynchNextserials = 0, postAllNextserials = 0;
		int postSynchCitys = 0, postAllCitys = 0, postSynchInventory = 0, postAllInventory = 0;
		List<SynchReport> list = dbh.getSynchReport();

		for (SynchReport SynchReport : list) {
			
			
			Log.d("Current Equals Modle Name : ", SynchReport.getComponent());
			Log.d("Current Equals Modle Totall count : ", SynchReport.getTotalCount());
			//Log.d("Current Equals Modle Sync count : ", SynchReport.getSynchCount());
			
			if (SynchReport.getComponent().equals("Created Merchants")) {
				postcreatedAllMerchants = Integer.parseInt(SynchReport.getTotalCount());
				if (postcreatedAllMerchants > 0) {
					postcreatedMerchants = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLogUpdate("Merchant", "Created", postcreatedMerchants, postcreatedAllMerchants, merchant );
			} else if (SynchReport.getComponent().equals("Assigned Merchants")) {
				postassignedAllMerchants = Integer.parseInt(SynchReport.getTotalCount());
				if (postassignedAllMerchants > 0) {
					postassignedMerchants = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLogUpdate("Merchant", "Assigned", postassignedMerchants, postassignedAllMerchants, merchant);
			} else if (SynchReport.getComponent().equals("Register Merchants")) {
				postregisteredAllMerchants = Integer.parseInt(SynchReport.getTotalCount());
				if (postregisteredAllMerchants > 0) {
					postregisteredMerchants = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLogUpdate("Merchant", "Register", postregisteredMerchants, postregisteredAllMerchants, merchant);
			} else if (SynchReport.getComponent().equals("Sales Details")) {				
				postAllSalesDetails = Integer.parseInt(SynchReport.getTotalCount());
				if (postAllSalesDetails > 0) {
					postSynchSalesDetails = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLogUpdate("Sales", "Details", postSynchSalesDetails, postAllSalesDetails, SaleData);
			} else if (SynchReport.getComponent().equals("Sales Header")) {
				postAllSalesHeader = Integer.parseInt(SynchReport.getTotalCount());
				if (postAllSalesHeader > 0) {
					postSynchSalesHeader = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLogUpdate("Sales", "Header", postSynchSalesHeader, postAllSalesHeader, SaleData);
			} else if (SynchReport.getComponent().equals("Cards Acceptance")) {
				postAllBulkserials = Integer.parseInt(SynchReport.getTotalCount());
				if (postAllBulkserials > 0) {
					postSynchBulkserials = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLogUpdate("Cards", "Acceptance", postSynchBulkserials, postAllBulkserials, CardAccept);
			} else if (SynchReport.getComponent().equals("Next Serial")) {
				postAllNextserials = Integer.parseInt(SynchReport.getTotalCount());
				if (postAllNextserials > 0) {
					postSynchNextserials = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLogUpdate("Cards", "NextSerial", postSynchNextserials, postAllNextserials, NextSerialDetails);
			} else if (SynchReport.getComponent().equals("Cities")) {
				postAllCitys = Integer.parseInt(SynchReport.getTotalCount());
				if (postAllCitys > 0) {
					postSynchCitys = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLogUpdate("City", "My City", postSynchCitys, postAllCitys, UpdatedCities);
			} else if (SynchReport.getComponent().equals("Merchant Inventory")) {
				postAllInventory = Integer.parseInt(SynchReport.getTotalCount());
				if (postAllInventory > 0) {
					postSynchInventory = Integer.parseInt(SynchReport.getSynchCount());
				}
				dbh.insertSynchLogUpdate("Merchant", "Inventory", postSynchInventory, postAllInventory, StockDataFromRemote);
			}
		}


		int postSynchMerchants = postcreatedMerchants + postassignedMerchants + postregisteredMerchants;
		int postAllMerchants = postcreatedAllMerchants + postassignedAllMerchants + postregisteredAllMerchants;
		dbh.insertSynchLogUpdate("Merchant", "Merchant", postSynchMerchants, postAllMerchants, merchant);
	}

	
	

}
