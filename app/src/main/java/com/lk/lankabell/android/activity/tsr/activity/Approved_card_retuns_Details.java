package com.lk.lankabell.android.activity.tsr.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.lk.lankabell.android.activity.tsr.R;

public class Approved_card_retuns_Details extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_approved_card_retuns__details);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(
				R.menu.activity_approved_card_retuns__details, menu);
		return true;
	}

}
