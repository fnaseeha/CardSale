package com.lk.lankabell.android.activity.tsr.activity;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.Merchant;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;
import com.lk.lankabell.android.activity.tsr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Jei
 *
 *Editing Merchant Details.
 */
public class EditMerchantFinal extends Activity{

DatabaseHandler dbh;


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.editmerchantfinal);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		final TextView myTextTitle=(TextView)findViewById(R.id.myTitle);
		myTextTitle.setText("Edit Merchant");
		
		String merchantId =  getIntent().getExtras().getString("merchantID");
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		Merchant merchant = dbh.GetMerchantByMerchantId(Long.valueOf(merchantId));
		String status = dbh.checkMerchantStatus(Long.valueOf(merchantId));
		EditText txtMerchantName =(EditText)findViewById(R.id.txtEdit_MerchantName);
		EditText txtReload_number=(EditText)findViewById(R.id.txtReloadNumber);
		EditText txtAddress =(EditText)findViewById(R.id.txtEdd_MerchantAddress);
		EditText txtPhone =(EditText)findViewById(R.id.txtEditMerchantContact);
		AutoCompleteTextView txtCity =(AutoCompleteTextView)findViewById(R.id.atc_Editautocity);
		
		txtMerchantName.setText(merchant.getName().toString());
		txtAddress.setText(merchant.getAddress().toString());
		txtCity.setText(merchant.getCity().toString());
		txtPhone.setText(merchant.getPhone().toString());
		txtReload_number.setText(merchant.getreloadNo().toString());
		
		if (status.equals("R")) {
			txtMerchantName.setEnabled(false);
			txtReload_number.setEnabled(false);
			txtAddress.setEnabled(false);
			txtPhone.setEnabled(true);
			txtReload_number.setEnabled(true);
		}
		//x != null ? x : y 

		
		//------------------Auto Load Cities-----------------------
		ArrayList<String> list = dbh.getCities();
  	    ArrayAdapter<String> adapter = new ArrayAdapter(EditMerchantFinal.this,android.R.layout.simple_dropdown_item_1line, list);
	  	txtCity = (AutoCompleteTextView)findViewById(R.id.atc_Editautocity);
	  	txtCity.setThreshold(1);
	  	txtCity.setAdapter(adapter);
		
	}
	
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MerchantJobs.class);
		startActivity(intent);
		finish(); 
        return;
	}
	
	/**
	 * @param view
	 * 
	 * Edit the Merchant details.
	 */
	public void onClickMerchantEdit(View view) {
		
		 dbh = new DatabaseHandler(getApplicationContext());
		 ArrayList<String> list = dbh.getCities();
		
	
		 final	EditText txtMerchantName =(EditText)findViewById(R.id.txtEdit_MerchantName);
		 final	EditText txtAddress =(EditText)findViewById(R.id.txtEdd_MerchantAddress);
		 final	EditText txtPhone =(EditText)findViewById(R.id.txtEditMerchantContact);
		 final AutoCompleteTextView txtCity =(AutoCompleteTextView)findViewById(R.id.atc_Editautocity);
		 final EditText txtReload_number=(EditText)findViewById(R.id.txtReloadNumber);;
		 String merchantId =  getIntent().getExtras().getString("merchantID");
		
		 final String name = txtMerchantName.getText().toString();
		 final String address = txtAddress.getText().toString();
		 final String phone = txtPhone.getText().toString();
		 final String city = txtCity.getText().toString();
		 final String Reload_number=txtReload_number.getText().toString();
		
		 ArrayAdapter<String> adapter = new ArrayAdapter(EditMerchantFinal.this,android.R.layout.simple_dropdown_item_1line, list);   
		 txtCity.setAdapter(adapter);

		 if(name.equalsIgnoreCase("")){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Please Enter name");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					 txtMerchantName.setText( txtMerchantName.getText());
					 txtAddress.setText( txtAddress.getText());
					 txtPhone.setText( txtPhone.getText());
					 txtReload_number.setText(txtReload_number.getText());
				}
			});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
		}
		else if( address.equalsIgnoreCase("")){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Please Enter Address");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtMerchantName.setText( txtMerchantName.getText());
					txtCity.setText(txtCity.getText());
					txtPhone.setText( txtPhone.getText());
					txtReload_number.setText(txtReload_number.getText());
				}
			});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
		}
		else if(phone.equalsIgnoreCase("")){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Enter Phone Number");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					 txtMerchantName.setText( txtMerchantName.getText());
					 txtAddress.setText( txtAddress.getText());
					 txtPhone.setText( txtPhone.getText());
					 txtCity.setText(txtCity.getText());
					 txtReload_number.setText(txtReload_number.getText());
				}
			});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
		}
		else if(city.equalsIgnoreCase("")){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Enter City");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					 txtMerchantName.setText( txtMerchantName.getText());
					 txtAddress.setText( txtAddress.getText());
					 txtPhone.setText( txtPhone.getText());
					 txtCity.setText(txtCity.getText());
					 txtReload_number.setText(txtReload_number.getText());
				}
			});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
		}
		else if(phone.length()<10|| phone.length()>10){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Entered Phone number is incorrect");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setIcon(R.drawable.ic_alert);
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtMerchantName.setText(txtMerchantName.getText());
					txtAddress.setText(txtAddress.getText());
					txtPhone.setText("");
					txtCity.setText(txtCity.getText());
					txtReload_number.setText(txtReload_number.getText());
				}
			});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
		}
		else if(!list.contains(city)){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("City doesn't exists in the System");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtMerchantName.setText(txtMerchantName.getText());
					txtAddress.setText(txtAddress.getText());
					txtPhone.setText(txtPhone.getText());
					txtCity.setText("");
					txtReload_number.setText(txtReload_number.getText());
				}
			});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
		}
		else if(Reload_number.length()<10|| Reload_number.length()>10){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
    		dlgAlert.setMessage("Entered Reload number is incorrect");
    		dlgAlert.setTitle("Alert");
    		dlgAlert.setIcon(R.drawable.ic_alert);
    		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					txtMerchantName.setText(txtMerchantName.getText());
					txtAddress.setText(txtAddress.getText());
					txtPhone.setText(txtPhone.getText());
					txtCity.setText("");
					txtReload_number.setText(txtReload_number.getText());
				}});
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
		} else{
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		dbh.UpdateMerchantMasterTableById(Long.valueOf(merchantId), name, address, phone, city,Reload_number);
		alert("Message", "Merchant Updated Successfully.", R.drawable.icon);
		}
	}
	
	
	public void onClickMerchantDelete(View view){
		boolean network_enable = isOnline();
		if (network_enable) {
			dbh = new DatabaseHandler(getApplicationContext());
			final EditText txtMerchantName =(EditText)findViewById(R.id.txtEdit_MerchantName);
			String merchantId =  getIntent().getExtras().getString("merchantID");
			final String name = txtMerchantName.getText().toString();
			 if(name.equalsIgnoreCase("")){
				 AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
				 dlgAlert.setMessage("Please Enter name");
				 dlgAlert.setTitle("Alert");
				 dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					 public void onClick(DialogInterface dialog, int which) {
					 }
				 });
				 dlgAlert.setCancelable(true);
				 dlgAlert.create().show();
			} else {
				//*********************************
				boolean invoices = dbh.checkNonSynchInvoices(Long.valueOf(merchantId));
				if (invoices) {
					//*************************************************************************
					String checkmerchantsales = dbh.checkmerchantsales(Long.valueOf(merchantId), name);
					if (checkmerchantsales.equals("delete")) {
						User user = dbh.getUserDetails();
						String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
						SoapObject request = new SoapObject("http://mainService", "tsrmerchantdelete");
						request.addProperty("strInputUserMobile", user.getmobileNo());
						request.addProperty("strInputUserName", user.getUserName());
						request.addProperty("strInputUserPassword", user.getPassword());
						request.addProperty("strInputUserEpfNo", epfNo);
						request.addProperty("merchantID", merchantId);
						request.addProperty("merchantname", name);

						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.setOutputSoapObject(request);
						envelope.dotNet = true;
						//if (name != null && name != "" && !name.isEmpty() && !merchantId.isEmpty() && merchantId != "" && merchantId != null) {
							try {				
								HttpTransportSE androidHttpTransport = new HttpTransportSE(WsdlReader.getServiceUrl(true,this));
								androidHttpTransport.call("http://mainService/tsrmerchantdelete", envelope);
								SoapObject result = (SoapObject) envelope.bodyIn;
								Log.d("+++++++++++", result.getProperty(0).toString() );
								//
								if (result != null) {
									Log.d("Return responses in the json objet deatils_________ :", result.toString());
									String message =  result.getProperty(0).toString();
									Log.d("_______________________", message);
									if (message.equals("allreadyhave")) {
										alert("Message", "This Merchant can't delete.", R.drawable.icon);
									}
									if (message.equals("sucess")) {
										String messages = dbh.deleteMerchant(Long.valueOf(merchantId), name);
										if (messages.equals("delete")) {
											Intent intent = new Intent(getApplicationContext(), EditMerchant.class);
											startActivity(intent);
											finish(); 
											alert("Message", "Merchant delete is Successfully.", R.drawable.icon);
										} else {
											alert("Message", "This Merchant can't delete.", R.drawable.icon);
										}
									}
									if (message.equals("error")) {
										alert("Message", "This Merchant can't delete.", R.drawable.icon);
									}
								}
							} catch (Exception e) {
								dbh.insertError("Merchants is not synced.Error : "+ e.getMessage());
								e.printStackTrace();
							}
						//}
					} else {
						alert("Message", "This Merchant can't delete.", R.drawable.icon);
					}
					//*************************************************************************
				} else {
					alert("Message", "This Merchant can't delete.", R.drawable.icon);
				}
				//********************************		
			}
		}	else {
			alert("Message", "Please check the Internet connection.", R.drawable.icon);
		}	
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
	/**
	 * @param title
	 * @param msg
	 * @param resourceID
	 * 
	 * Alert function if any error occurs.
	 * 
	 */
	public void alert(String title,String msg,int resourceID)
	{
		
		 AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(msg);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      // here you can add functions
				   Intent intent = new Intent(EditMerchantFinal.this,MerchantJobs.class);
					startActivity(intent);
					finish();
			   }
			});
			alertDialog.setIcon(resourceID);
			alertDialog.show();
	}
	
}
