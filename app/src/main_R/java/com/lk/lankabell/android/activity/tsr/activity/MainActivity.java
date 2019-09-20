package com.lk.lankabell.android.activity.tsr.activity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;


import com.lk.lankabell.android.activity.tsr.R;



/**
 * @author Jei
 * 
 * Card Bulk Acceptance function.
 *
 */
public class MainActivity extends ExpandableListActivity {
	ExpAdapter exp;
	private int currentHourPosition = -1;
	static final String arrGroupelements[] = 
		    {
		   "Batch-01",
		   "Batch-02",
		   "Batch-03",
		   "Batch-04"
		 };
	 
	static final String arrChildelements[][] = 
		 {
		   {
		  "Card Type:Visa",
		  "Card Value:100",
		  "Start Serial No-123",
		  "End Serial N0-456"
		  
		   },
		 
		   };
	 
	 DisplayMetrics metrics;
	 int width;
	 ExpandableListView expList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_main);
		boolean network_enable= isOnline();

		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		dbh.getBulkNoList();
		
		 expList = getExpandableListView();
	        metrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        width = metrics.widthPixels;
	        //this code for adjusting the group indicator into right side of the view
	        // previous 50 & 10.  
	        //   expList.setIndicatorBounds(width - GetDipsFromPixel(50), width - GetDipsFromPixel(10));
	        expList.setAdapter(new ExpAdapter(this));
	        
	        expList.setOnGroupExpandListener(new OnGroupExpandListener(){
	        	public void onGroupExpand(int groupPosition) {
	                   if(currentHourPosition != -1 && currentHourPosition !=groupPosition){
	                           expList.collapseGroup(currentHourPosition);
	                   }
	                   currentHourPosition = groupPosition;
	                   }
	        	  });
	        
	        	expList.setOnGroupCollapseListener(new OnGroupCollapseListener(){
	         public void onGroupCollapse(int groupPosition){}
	        	});
	        expList.setOnChildClickListener(new OnChildClickListener(){
	        
	        	public boolean onChildClick(ExpandableListView parent, View v,
	        			int groupPosition, int childPosition, long id) {
	        		  Log.e("OnChildClickListener", "OK");
	        		return false;
	        	}
			
	        });
	}

	  public int GetDipsFromPixel(float pixels){
	     // Get the screen's density scale
	     final float scale = getResources().getDisplayMetrics().density;
	     // Convert the dps to pixels, based on density scale
	     return (int) (pixels * scale + 0.5f);
	    }
	  
	 /**
	 * @return connected or not.
	 * 
	 * Checking whether the Internet connection is available or not.
	 */
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
	  
	
	  /**
	 * @return Sim Serial Number
	 * 
	 * Method for Getting the phone Sim Serial Number from the Mobile.
	 */

}
