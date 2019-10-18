package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

public class MerchantViewer extends Activity {

	private AutoCompleteTextView autoMerchant;
	final ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
	final int[] columnIds = new int[] { R.id.merchantId, R.id.merchantName,R.id.merchantAddress};
	final String[] columnTags = new String[] { "Id", "Name", "Address"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.merchantviewer);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setText("Merchant Global View");
		}
		final TextView appversion = findViewById(R.id.appversion);
		DatabaseHandler dbh;
		dbh = new DatabaseHandler(getApplicationContext());
		if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}

		ArrayList<String> list = dbh.getAllMerchants();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MerchantViewer.this,android.R.layout.simple_dropdown_item_1line, list);
		autoMerchant = (AutoCompleteTextView) findViewById(R.id.autoMerchantName);
		autoMerchant.setThreshold(1);
		autoMerchant.setAdapter(adapter);
	}

	public void onClickEditProceed(View view) {
		autoMerchant = (AutoCompleteTextView) findViewById(R.id.autoMerchantName);
		String merchant = autoMerchant.getText().toString();
		ListView lv = (ListView) findViewById(R.id.lstMerchantEdit);
		SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
		String sql = "SELECT merchant_id,merchant_name,address,reload_number,latitude,longitude,telephone_no,city" +
				" FROM merchant_master  where merchant_name ='"+merchant+"' ORDER BY merchant_name";
		Cursor OrderCursor = db.rawQuery(sql, null);
		listData.clear();
		if (OrderCursor != null) {
			if (OrderCursor.moveToFirst()) {
				for (int i = 0; i < OrderCursor.getCount(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
					map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_NAME")));
					map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("ADDRESS")));//Map column Tags with Sql return column names
					listData.add(map);
					OrderCursor.moveToNext();
				}
			}
			OrderCursor.close();
			db.close();
			SimpleAdapter adapter = new SimpleAdapter(MerchantViewer.this,listData, R.layout.merchantgloballist, columnTags,columnIds);
			lv.setAdapter(adapter);
			lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			lv.setSelection(0);
			lv.smoothScrollToPosition(0);
		}
	}

	public void onClickEditMerchantfirst(View view) {//When press Edit button
		LinearLayout vwParentRow = (LinearLayout) view.getParent();
		TextView fifId = (TextView) vwParentRow.getChildAt(0);
		String test5 = fifId.getText().toString();
		Intent intent = new Intent(MerchantViewer.this, MerchantViewerFinal.class);
		intent.putExtra("merchantID", test5);
		startActivity(intent);
		finish();
	}
}