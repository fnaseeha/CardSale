package com.lk.lankabell.android.activity.tsr.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

import com.lk.lankabell.android.activity.tsr.R;

public class Card_Returns_Details extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card__returns__details);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_card__returns__details, menu);
		return true;
	}

}
