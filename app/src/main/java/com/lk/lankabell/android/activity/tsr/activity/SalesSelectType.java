/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.lk.lankabell.android.activity.tsr.R;

/**
 * @author Administrator
 *
 */
public class SalesSelectType extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mysales_select_type);
		
		DataBindToItemTypeSpinner();
	}
	
	
	public void DataBindToItemTypeSpinner() {
		
		Spinner spnType = (Spinner)findViewById(R.id.spnSalesType);
		String[] array_spinner = new String[4];
		
		array_spinner[0] = "Merchant Wise";
		array_spinner[1] = "Date Wise";
		array_spinner[2] = "City Wise";
		array_spinner[3] = "TSR Reports";
		
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, array_spinner);
		spnType.setAdapter(adapter);
	
	}
	
	public void OnclickSalesShow(View view) {
		
		Spinner spnType = (Spinner)findViewById(R.id.spnSalesType);
		String selectType = spnType.getSelectedItem().toString();
		
		if (selectType.equalsIgnoreCase("Merchant Wise")) {
			
			Intent intent = new Intent(SalesSelectType.this,mysales.class);	
			startActivity(intent);
			
		}
		else if (selectType.equalsIgnoreCase("Date Wise")) {
			
			Intent intent = new Intent(SalesSelectType.this,MysalesDateWise.class);	
			startActivity(intent);
		}
		else if (selectType.equalsIgnoreCase("City Wise")) {
			
			Intent intent = new Intent(SalesSelectType.this,MysalesCityWise.class);	
			startActivity(intent);
		}
		else if (selectType.equalsIgnoreCase("TSR Reports")) {
			
			Intent intent = new Intent(SalesSelectType.this,TSRReports.class);	
			startActivity(intent);
		}
		
	}

}
