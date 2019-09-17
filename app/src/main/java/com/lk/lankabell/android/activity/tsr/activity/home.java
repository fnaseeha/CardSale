/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

/**
 * @author 
 *
 */

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Jei
 *
 *This is the Card Sales Menu class.
 */
public class home extends Activity{
	
	boolean network_enable = false;
	private static String SOAP_ACTION = "http://mainService/ReturnCardsSashireLoad";
	private static String METHOD_NAME = "ReturnCardsSashireLoad";	
	private String URL = WsdlReader.getServiceUrl(true,this);
	private static String NAMESPACE = "http://mainService";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.home);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		final TextView myTitleText=(TextView)findViewById(R.id.myTitle);
		if(myTitleText!=null) {
			myTitleText.setText("Cards");
		}
	}


	/**
	 * @param view
	 * 
	 * Logout function.
	 */

	public void onClickExit(View view) {
		// click logout button

		LayoutInflater factory = LayoutInflater.from(this);
		final View dialogBuilder = factory.inflate(R.layout.custom_msg_dialog, null);
		final AlertDialog dialogView = new AlertDialog.Builder(this).create();
		dialogView.setView(dialogBuilder);
		TextView title = (TextView) dialogBuilder.findViewById(R.id.title);
		title.setText("Do you want to exit?");
		dialogBuilder.findViewById(R.id.btn_yes).setOnClickListener(new  View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				System.exit(0);
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
			}
		});
		dialogBuilder.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogView.dismiss();
			}
		});
		dialogView.show();
		Rect displayRectangle = new Rect();
		Window window = this.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		dialogView.getWindow().setLayout((int) (displayRectangle.width() *
				0.8f), dialogView.getWindow().getAttributes().height);

//		Builder inputDialog = new AlertDialog.Builder(this);
//		inputDialog.setMessage("Do you want to exit?");
//		inputDialog.setPositiveButton("Yes", new OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				Intent i = new Intent(getApplicationContext(), LoginPage.class);
//				startActivity(i);
//				finish();
//				System.exit(0);
//			}
//		});
//		inputDialog.setNegativeButton("No", new OnClickListener() {
//
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		});
//		inputDialog.show();
	}


	/**
	 * @param view
	 * 
	 * Navigate to Card Sales.
	 */
	public void onClickSell(View view) {	// select card sale button
		Intent intent = new Intent(getApplicationContext(),SelectMerchant.class);
		startActivity(intent);
	}


	/**
	 * @param view
	 * 
	 * Navigate to Merchant Inventory page.
	 */
	public void onClickInventory(View view){	// select merchant inventory button
		//	Intent intent=new Intent(getApplicationContext(),MerchantInventoryMerchantList.class);
		Intent intent=new Intent(home.this,MerchantInventorySelectType.class);
		startActivity(intent);
	}


	/**
	 * @param view
	 * 
	 * Navigate to My Sales page.invalid
	 */
	public void onClickMySales(View view){	// select mysales button
		Intent intent=new Intent(getApplicationContext(),TSRReports.class);
		startActivity(intent);
	}
	
	/**
	 * @param view
	 * 
	 * Navigate to My Sales page.invalid
	 */
	public void onClickCardReturns(View view){	// select card returns button
		Intent intent = new Intent(getApplicationContext(),Card_Returns.class);
		startActivity(intent);
	}
	
	public void onClickCardReturnsApproved(View view){
		network_enable = isOnline();
    	if (network_enable){
    	//	DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
    	//	User user = dbh.getUserDetails();			
    	//	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);				
    	//	request.addProperty("strInputUserMobile", getMyPhoneNumber());
    	//	request.addProperty("strInputUserName", user.getUserName());
    	//	request.addProperty("strInputUserPassword", user.getPassword());
    	//		
    	//	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	//	envelope.setOutputSoapObject(request);
    	//	envelope.dotNet = true;
    	//	try {
    	//		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    	//		androidHttpTransport.call(SOAP_ACTION, envelope);
    	//		SoapObject result = (SoapObject) envelope.bodyIn; // SoapFault
    	//		if (result != null) {    				
    	//			//------------------------------------------------------------------------------------------------------------------------
    				Intent intent = new Intent(getApplicationContext(),Approved_card_returns.class);
    	//			intent.putExtra("Return_Cards", result.getProperty(0).toString());
    	    		startActivity(intent);
    	//		}
    	//	} catch (Exception e) {
    	//		Toast.makeText(getApplicationContext(), "Please try again.",Toast.LENGTH_LONG).show();
    	//		dbh.insertError("Send CashierConfirmation : "+ e.getMessage());
    	//		e.printStackTrace();
    	//	}
    	} else {
    		Toast.makeText(getApplicationContext(), "No network connetion. Please enable the network connetion.",Toast.LENGTH_LONG).show();
    	}		
	}
	

 

	/**
	 * @param view
	 * 
	 * Navigate to Card Bulk Acceptance page.
	 */
	public void onClickCardReg(View view){	// select mysales button
		Intent intent=new Intent(getApplicationContext(),bulkAccept.class);
		startActivity(intent);
	}


	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), SelectorActivity.class);
		startActivity(intent);
		finish(); 
        return;
	}
	
	public boolean isOnline() { // check network connection is available
		boolean connected = false;
		final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			connected = true;
		} else {
			connected = false;
		} 
		return connected;
	}
	
}
