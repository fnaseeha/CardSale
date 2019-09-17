/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

/**
 * @author Administrator
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;

/**
 * @author Jei
 * 
 *         This class is used to display the merchant Menu.
 * 
 */
public class MerchantJobs extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.merchant_main);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);

		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setText("Merchants");

		}
	}

	/**
	 * @param view
	 * 
	 *            Navigate to Add New merchant page.
	 */
	public void onClickAddMerchant(View view) {
		Intent intent = new Intent(getApplicationContext(), AddMerchant.class);
		startActivity(intent);
		finish();
	}

	public void onClickCityRegister(View view) {
		Intent intent = new Intent(getApplicationContext(), AddCity.class);
		startActivity(intent);
		finish();
	}

	
	// onClickMerchantAssign

	/**
	 * @param view
	 * 
	 *            Navigate to Assign Merchant page.
	 */
	public void onClickMerchantAssign(View view) {
		Intent intent = new Intent(getApplicationContext(),AssignMerchant.class);
		startActivity(intent);
		finish();
	}

	@SuppressWarnings("resource")
	public void onClickPurge(View view) {
		//synchMerchants();
	}

	 	
	
	
	// onClickMerchantEdit

	/**
	 * @param view
	 * 
	 *            Navigate to Merchant edit page.
	 */
	public void onClickMerchantEdit(View view) {
		Intent intent = new Intent(getApplicationContext(), EditMerchant.class);
		startActivity(intent);
		finish();
	}

	/**
	 * @param view
	 * 
	 *            Navigate to Merchant Location Registration page.
	 */
	public void onClickMerchantReg(View view){
		Intent intent = new Intent(getApplicationContext(),MerchantActivity.class);
		startActivity(intent);
		finish();
	}

	 

}
