package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.MaterialEntry;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener; 

public class faultMaterial extends Activity {
	
	private DatabaseHandler dbh;
	 ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
	 int[] columnIds = new int[] {R.id.MeterialListColumn1,R.id.MeterialListColumn2,R.id.MeterialListColumn3};
	 String[] columnTags = new String[] {"Item", "RepairType","Quantity","Remarks"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fault_metirial);
		
		DatabaseHandler db  = new DatabaseHandler(getApplicationContext());
		User user = db.getUserDetails();
		
		TextView userName = (TextView)findViewById(R.id.lblUserName);
		userName.setText(user.getUserName());
		
		DataBindToItemSpinner();
		DataBindToRepairType();
		
		Button btnAdd = (Button)findViewById(R.id.btnAdd);
		
		
		
        HashMap<String,String> mappackage = new HashMap<String, String>();

        mappackage.put(columnTags[0], "Item");
        mappackage.put(columnTags[1],"Repair Type"); 
        mappackage.put(columnTags[2],"Qty");

        listData.add(mappackage);
      //  ListView lvitems=(ListView)findViewById(R.id.listFault);
		
		
		btnAdd.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				EditText txtQuantity = (EditText)findViewById(R.id.txtQuantity);
				String quantity = txtQuantity.getText().toString();
				
				EditText txtRemarks = (EditText)findViewById(R.id.txtRemarks);
				String remarks = txtQuantity.getText().toString();
				
				Spinner spnItem = (Spinner)findViewById(R.id.spnItems);
				String item  = spnItem.getSelectedItem().toString();
				
				Spinner spnRepairType = (Spinner)findViewById(R.id.spnRepairType);
				String repairType = spnRepairType.getSelectedItem().toString();
				
				
				
				if(quantity.equalsIgnoreCase(""))
				{
					AlertDialog alertDialog = new AlertDialog.Builder(faultMaterial.this).create();
					alertDialog.setTitle("Message");
					alertDialog.setMessage("Please Enter Quantity...!");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						   public void onClick(DialogInterface dialog, int which) {
						      // here you can add functions

						   }
						});
						alertDialog.setIcon(R.drawable.ic_alert);
						alertDialog.show();
				}
				else
				{
						addPackagetoList(item,repairType,quantity,remarks);

				}
			}
		});
		
		Button btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				for (int i = 1; i < listData.size(); i++) {
					dbh = new DatabaseHandler(getApplicationContext());
					HashMap<String, String> selectedFromList = (HashMap<String, String>) listData.get(i);
					
					String SelectedItem = selectedFromList.get("Item");
					String RepairType = selectedFromList.get("RepairType");
					String Quantity = selectedFromList.get("Quantity");
					String Remarks = selectedFromList.get("Remarks");
					
					String FaultTicketId=  getIntent().getExtras().getString("FaultTicketId");
					int ticketId = Integer.parseInt(FaultTicketId);
					int detailedId = GenerateFaultTicketDetailId(ticketId);
					
					MaterialEntry MaterialEntry = new MaterialEntry(SelectedItem, RepairType, Quantity, Remarks);
					dbh.SavefaultTicketMaterial(MaterialEntry, ticketId, detailedId);
				}
				
				AlertDialog alertDialog = new AlertDialog.Builder(faultMaterial.this).create();
				alertDialog.setTitle("Message");
				alertDialog.setMessage("Saved Successfully.");
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int which) {
					      // here you can add functions
						   Intent intent = new Intent(faultMaterial.this,
									SelectorActivity.class);
							startActivity(intent);
					   }
					});
					alertDialog.setIcon(R.drawable.ic_alert);
					alertDialog.show();
			}
		});
		
	}
	
	public void addPackagetoList(String item,String repairType,String quantity,String Remarks) {
		ListView listView = (ListView)findViewById(R.id.listFault);
		
		for (int a=0;a<listData.size();a++)
        {
            HashMap<String, String> selectedFromList = (HashMap<String, String>) listData.get(a);


            if (selectedFromList.get("Item").trim().equals(item))
            {
             listData.remove(a);
             break;
            }
        }

     HashMap<String,String> mappackage = new HashMap<String, String>();

     mappackage.put(columnTags[0], item);
     mappackage.put(columnTags[1],repairType); 
     mappackage.put(columnTags[2], quantity);
     mappackage.put(columnTags[3], Remarks);

     listData.add(mappackage);

     listView.setAdapter(setAdapter(listData, columnIds, columnTags));
     
	}
	
	
	
	public SimpleAdapter setAdapter(ArrayList<HashMap<String, String>> listData,int[] columnIds,String[] columnTags)
    {
        SimpleAdapter adapter =new SimpleAdapter(this, listData, R.layout.materiallistview,columnTags , columnIds);
        return adapter;
    }
	
	public void exitOnClick(View view) {

		// Switching to sub menu screen
		finish();
		System.exit(0);
	}
	
	
	public void DataBindToItemSpinner() {
		SQLiteDatabase DB = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Spinner spnItem = (Spinner)findViewById(R.id.spnItems);
		Cursor spnCursor = DB.rawQuery("SELECT ITEM_NAME FROM ITEM",null);
		
		String[] array_spinner = new String[spnCursor.getCount()];
		if(spnCursor.moveToFirst())
		{
			for (int i = 0; i < spnCursor.getCount(); i++) {
				array_spinner[i] = spnCursor.getString(spnCursor.getColumnIndex("ITEM_NAME") );
				spnCursor.moveToNext();
			}
		}
		spnCursor.close();
		DB.close();
		
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, array_spinner);
		spnItem.setAdapter(adapter);
	}
	
	public void DataBindToRepairType() {
		SQLiteDatabase DB = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		Spinner spnRepairType = (Spinner)findViewById(R.id.spnRepairType);
		Cursor spnCursor = DB.rawQuery("SELECT TYPE_NAME FROM REPIRE_TYPE",null);
		
		String[] array_spinner = new String[spnCursor.getCount()];
		if(spnCursor.moveToFirst())
		{
			for (int i = 0; i < spnCursor.getCount(); i++) {
				array_spinner[i] = spnCursor.getString(spnCursor.getColumnIndex("TYPE_NAME") );
				spnCursor.moveToNext();
			}
		}
		spnCursor.close();
		DB.close();
		
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, array_spinner);
		spnRepairType.setAdapter(adapter);
	}
	
	public int GenerateFaultTicketDetailId(int faultTicketId) {
		
		dbh = new DatabaseHandler(getApplicationContext());
		int id;
		int maxId = dbh.GetmaxFaultTicketDetailCode(faultTicketId);
		id = maxId +1;
		return id;
	}

}
