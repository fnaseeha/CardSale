package com.lk.lankabell.android.activity.tsr.activity;

/**
 * @author Jei
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.PostalCode;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.R;

/**
 * @author Jei
 * Assigning available merchants to the TSR. 
 *
 */
public class AssignMerchant extends Activity {
    ListView lv;
    private DatabaseHandler dbh;
    private AutoCompleteTextView textview;

    final ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
    final int[] columnIds = new int[]{R.id.lstMerchantName, R.id.lstMerchantAddress, R.id.lstMerchantContact, R.id.lstMerchantID};
    final String[] columnTags = new String[]{"Name", "Address", "Contact", "ID", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.assignmerchant);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
        myTitleText.setText("Assign Merchant");
        dbh = new DatabaseHandler(getApplicationContext());
        ArrayList<String> list = dbh.getCities();
        for (int i = 0; i < list.size(); i++) {
            String cityName = list.get(i).toString();
            PostalCode postalCode = dbh.getPostalCodeDetailsByCityName(cityName);
            float Pos_Longitude = Float.parseFloat((postalCode.getLongitude()));
            float Pos_Latitude = Float.parseFloat((postalCode.getLatitude()));

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AssignMerchant.this, android.R.layout.simple_dropdown_item_1line, list);
        textview = (AutoCompleteTextView) findViewById(R.id.atc_AutoCompleteCities);
        textview.setThreshold(1);
        textview.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getApplicationContext(), MerchantJobs.class);
        startActivity(intent);
        finish();

        return;
    }

    public void onClickMerchantProceed(View view) {
        textview = (AutoCompleteTextView) findViewById(R.id.atc_AutoCompleteCities);
        String cityName = textview.getText().toString();
        SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
        Cursor OrderCursor = db.rawQuery(" SELECT MERCHANT_NAME,ADDRESS,TELEPHONE_NO,MERCHANT_ID FROM MERCHANT_MASTER WHERE CITY ='" + cityName + "' AND (IS_ASSIGNED is null OR IS_ASSIGNED is 0)", null);   //"CITY = " +city
        listData.clear();
        if (OrderCursor != null) {
            if (OrderCursor.moveToFirst()) {
                for (int i = 0; i < OrderCursor.getCount(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_NAME")));
                    map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("ADDRESS")));
                    map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("TELEPHONE_NO")));
                    map.put(columnTags[3], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
                    listData.add(map);
                    OrderCursor.moveToNext();
                }
            }
            OrderCursor.close();
            db.close();
            SimpleAdapter adapter = new SimpleAdapter(AssignMerchant.this, listData, R.layout.assignmerchantdata, columnTags, columnIds);
            ListView lvData = (ListView) findViewById(R.id.lv_AssignMerchant);
            lvData.setAdapter(adapter);
        }
        //}
    }

    public SimpleAdapter setAdapter(ArrayList<HashMap<String, String>> listData, int[] columnIds, String[] columnTags) {
        SimpleAdapter adapter = new SimpleAdapter(this, listData, R.layout.assignmerchantdata, columnTags, columnIds);
        return adapter;
    }

    public void onClickAssign(View view) {

        LinearLayout vwParentRow = (LinearLayout) view.getParent();

        TextView merchantid = (TextView) vwParentRow.getChildAt(0);
        TextView merchantcontact = (TextView) vwParentRow.getChildAt(1);
        TextView merchantname = (TextView) vwParentRow.getChildAt(2).findViewById(R.id.lstMerchantName);
        TextView merchantaddress = (TextView) vwParentRow.getChildAt(2).findViewById(R.id.lstMerchantAddress);
        textview = (AutoCompleteTextView) findViewById(R.id.atc_AutoCompleteCities);


//		 AbsoluteLayout vwParentRow_ = (AbsoluteLayout)view.getParent();
//		 TextView merchantid_ = (TextView)vwParentRow_.getChildAt(1);
//		 String merchant_id =merchantid_.getText().toString();
//
//
//		  AbsoluteLayout vwParentRow = (AbsoluteLayout)view.getParent();
//		  textview = (AutoCompleteTextView)findViewById(R.id.atc_AutoCompleteCities);
//	      TextView merchantid = (TextView)vwParentRow.getChildAt(1);
//	      TextView merchantname = (TextView)vwParentRow.getChildAt(3);
//	      TextView merchantaddress = (TextView)vwParentRow.getChildAt(4);
//	      TextView merchantcontact = (TextView)vwParentRow.getChildAt(2);

        String name = merchantname.getText().toString();
        String address = merchantaddress.getText().toString();
        String contact = merchantcontact.getText().toString();
        String cityName = textview.getText().toString();
        String mid = merchantid.getText().toString();

        Long id = Long.valueOf(mid);

        //dbh.updateUpdatedMerchantsAssignTempTable(id);
        dbh.SaveMerchantMasterDetails(id, name, address, contact, cityName);

        diplayAlert("Confirmation", "Successfully Assigned");

        vwParentRow.removeAllViews();

    }

    private void diplayAlert(String b_title, String b_message) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogBuilder = factory.inflate(R.layout.custom_okay_msg_dialog, null);
        final AlertDialog dialogView = new AlertDialog.Builder(this).create();
        dialogView.setView(dialogBuilder);
        TextView title = (TextView) dialogBuilder.findViewById(R.id.title);
        TextView message = (TextView) dialogBuilder.findViewById(R.id.message);
        title.setText(b_title);
        message.setText(b_message);
        dialogView.setCancelable(true);
        dialogBuilder.findViewById(R.id.okay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MerchantJobs.class);
                startActivity(intent);
                finish();
            }
        });

        dialogView.show();
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogView.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), dialogView.getWindow().getAttributes().height);
    }

    private String getMyPhoneNumber() {
        return Utils.getSimSerialNumber(this);
    }

    public boolean isOnline() {            // check network connection is available
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }
}