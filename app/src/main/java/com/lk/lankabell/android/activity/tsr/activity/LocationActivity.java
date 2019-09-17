package com.lk.lankabell.android.activity.tsr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import com.lk.lankabell.android.activity.tsr.R;

public class LocationActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);

		// Set View to location.xml
		setContentView(R.layout.location);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);

		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		myTitleText.setText("Enter location");

	}

	public void buttonClickHandler(View view) {

		EditText txtLoc = (EditText) findViewById(R.id.loc);
		String loc = txtLoc.getText().toString();

		TextView error = (TextView) findViewById(R.id.errormsg);

		if (loc.equalsIgnoreCase("")) {

			error.setText("Location can not be empty");

		}

	}

	public void locationOnClickBack(View view) {

		Intent i = new Intent(this.getApplicationContext(),
				SelectorActivity.class);
		startActivity(i);

	}

}
