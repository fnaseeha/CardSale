package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.LayoutInflater;


/**
 * @author This class is used to display records according to  Merchant Inventory Types.
 */
public class MerchantInventoryMerchantList extends Activity {

    private String longitudeValue;
    private String latitudeValue;

    Button btnSelect;
    ListView lv;
    Spinner spinner;

    final ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> listData1 = new ArrayList<HashMap<String, String>>();
    final int[] columnIds = new int[]{R.id.lblMerchantName, R.id.lblAddress, R.id.lblMerchantId, R.id.lblMeasure};
    final String[] columnTags = new String[]{"Name", "Address", "Id", "measure"};

    DatabaseHandler dbh;

    ImageView imvTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.merchantinventorylist);


        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
        if (myTitleText != null) {
            myTitleText.setText("Merchant Inventory");
        }
        final TextView appversion = findViewById(R.id.appversion);
        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        if(appversion != null){
			appversion.setText("v -"+dbh.getVersion());
		}
        lv = (ListView) findViewById(R.id.lvMerchantName);

        // Color coding

        String lati_Value = getIntent().getExtras().getString("latitudeValue");
        String lond_Value = getIntent().getExtras().getString("londitudeValue");
        String type = getIntent().getExtras().getString("type");

        btnSelect = (Button) findViewById(R.id.btnSelect);


        SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);

        //String sql="SELECT DISTINCT(A.MERCHANT_ID), B.MERCHANT_NAME, B.ADDRESS, A.MEASURE,B.LATITUDE,B.LONGITUDE FROM (SELECT MERCHANT_ID, MIN( CAST(STOCK_IN_HAND AS REAL) /REORDER_LEVEL) AS MEASURE FROM MERCHANT_INVENTORY where DENOMINATION in (50,100,200) GROUP BY MERCHANT_ID) A INNER JOIN MERCHANT_MASTER B ON A.MERCHANT_ID=B.MERCHANT_ID ORDER BY A.MEASURE";
        //String sql = "SELECT DISTINCT(A.MERCHANT_ID), B.MERCHANT_NAME, B.ADDRESS, A.MEASURE,B.LATITUDE,B.LONGITUDE FROM (SELECT t.MERCHANT_ID as MERCHANT_ID, MAX(t.MEASURE) as MEASURE from (SELECT mm.MERCHANT_ID, MAX(CAST(mm.STOCK_IN_HAND as REAL)/mm.REORDER_LEVEL) AS MEASURE  FROM MERCHANT_INVENTORY mm where DENOMINATION in (50,100,200) GROUP BY mm.MERCHANT_ID union SELECT mm.MERCHANT_ID, 2 as MEASURE FROM MERCHANT_INVENTORY mm where mm.MERCHANT_ID in (SELECT b.MERCHANT_ID FROM MERCHANT_INVENTORY b where not EXISTS (select a.MERCHANT_ID from MERCHANT_INVENTORY a where a.[MERCHANT_ID] = b.[MERCHANT_ID] AND DENOMINATION in (50,100,200))) GROUP BY mm.MERCHANT_ID) t GROUP BY t.MERCHANT_ID) A INNER JOIN MERCHANT_MASTER B ON A.MERCHANT_ID=B.MERCHANT_ID where B.[IS_ACTIVE] = 1 ORDER BY A.MEASURE";
        String sql = "SELECT B.MERCHANT_ID as MERCHANT_ID, M.[MERCHANT_NAME] as MERCHANT_NAME, M.[ADDRESS] as ADDRESS , MAX(B.MEASURE) as " +
                "MEASURE, M.LATITUDE as LATITUDE, M.LONGITUDE as LONGITUDE FROM (SELECT mm.[MERCHANT_ID] as MERCHANT_ID, 0 as MEASURE FROM " +
                "MERCHANT_INVENTORY mm where mm.[STOCK_IN_HAND] = 0 union SELECT mm.[MERCHANT_ID] as MERCHANT_ID, 2 as MEASURE FROM " +
                "MERCHANT_INVENTORY mm where mm.[STOCK_IN_HAND] > mm.[REORDER_LEVEL] union SELECT mm.[MERCHANT_ID] as MERCHANT_ID, 1.25 " +
                "as MEASURE FROM MERCHANT_INVENTORY mm where mm.[STOCK_IN_HAND] < mm.[REORDER_LEVEL] and  mm.[STOCK_IN_HAND] > 0) B " +
                "INNER JOIN MERCHANT_MASTER M ON M.MERCHANT_ID = B.MERCHANT_ID where M.[IS_ACTIVE] = 1 GROUP BY B.MERCHANT_ID ORDER BY " +
                "B.MEASURE";
        //String sql = "SELECT DISTINCT(A.MERCHANT_ID), B.MERCHANT_NAME, B.ADDRESS, A.MEASURE,B.LATITUDE,B.LONGITUDE FROM (SELECT t.MERCHANT_ID as MERCHANT_ID, MIN(t.MEASURE) as MEASURE from (SELECT mm.MERCHANT_ID, max(CAST(mm.STOCK_IN_HAND as REAL)/mm.REORDER_LEVEL) AS MEASURE  FROM MERCHANT_INVENTORY mm where DENOMINATION in (50,100,200) GROUP BY mm.MERCHANT_ID union SELECT mm.MERCHANT_ID, 1 as MEASURE FROM MERCHANT_INVENTORY mm where DENOMINATION not in (50,100,200)GROUP BY mm.MERCHANT_ID) t GROUP BY t.MERCHANT_ID) A INNER JOIN MERCHANT_MASTER B ON A.MERCHANT_ID=B.MERCHANT_ID ORDER BY A.MEASURE";
        Cursor OrderCursor = db.rawQuery(sql, null);

        listData.clear();
        if (OrderCursor != null) {

            if (OrderCursor.moveToFirst()) {
                for (int i = 0; i < OrderCursor.getCount(); i++) {
                    int DD = OrderCursor.getCount();
                    String nam = OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_NAME"));
                    if (type.equals("All")) {
                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_NAME")));
                        map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("ADDRESS")));
                        map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
                        map.put(columnTags[3], OrderCursor.getString(OrderCursor.getColumnIndex("MEASURE")));
                        listData.add(map);
                    } else if (type.equals("This Area")) {

                        String latitude_val = OrderCursor.getString(OrderCursor.getColumnIndex("LATITUDE"));
                        String londitude_val = OrderCursor.getString(OrderCursor.getColumnIndex("LONGITUDE"));

                        float latitude = Float.parseFloat(latitude_val);
                        float londitude = Float.parseFloat(londitude_val);


                        float my_Latitude = Float.parseFloat(lati_Value != null ? lati_Value : "0");
                        float my_Londitude = Float.parseFloat(lond_Value != null ? lond_Value : "0");

                        double distance = (float) Math.sqrt(((latitude - my_Latitude) * (latitude - my_Latitude) * 111 * 111) +
                                ((londitude - my_Londitude) * (londitude - my_Londitude) * 111 * 111));
                        if (distance < 5) {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(columnTags[0], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_NAME")));
                            map.put(columnTags[1], OrderCursor.getString(OrderCursor.getColumnIndex("ADDRESS")));
                            map.put(columnTags[2], OrderCursor.getString(OrderCursor.getColumnIndex("MERCHANT_ID")));
                            map.put(columnTags[3], OrderCursor.getString(OrderCursor.getColumnIndex("MEASURE")));
                            listData.add(map);
                        }
                    } else if (type.equals("By City")) {
                        Intent intent = new Intent(MerchantInventoryMerchantList.this, MerchantInventoryCitySelect.class);
                        startActivity(intent);
                        finish();
                    }
                    OrderCursor.moveToNext();
                }//end of for
            }
            OrderCursor.close();
            db.close();

            SpecialAdapter adapter = new SpecialAdapter(MerchantInventoryMerchantList.this, listData, R.layout.merchantlistview, columnTags, columnIds);
            lv.setAdapter(adapter);
        }

    }

    /**
     * @param view Selecting the merchant inventory type function.
     */


    public void OnclickSelect(View view) {
        LinearLayout vwParentRow = (LinearLayout) view.getParent();

        TextView nameValue = (TextView) vwParentRow.getChildAt(0).findViewById(R.id.lblMerchantName); //vwParentRow.getChildAt(0);
        TextView Address = (TextView) vwParentRow.getChildAt(0).findViewById(R.id.lblAddress); //vwParentRow.getChildAt(1);
        TextView IdValue = (TextView) vwParentRow.getChildAt(1);//view.findViewById(R.id.lblMerchantId);

//        TextView nameValue = (TextView) view.findViewById(R.id.)//vwParentRow.getChildAt(0);
//        TextView Address = (TextView) vwParentRow.getChildAt(1);
//        TextView IdValue = (TextView) vwParentRow.getChildAt(2);

        Intent intent = new Intent(MerchantInventoryMerchantList.this, MerchantInventoryDetails.class);
        intent.putExtra("merchantID", IdValue.getText().toString());
        intent.putExtra("merchantName", nameValue.getText().toString());
        intent.putExtra("Address", Address.getText().toString());
        startActivity(intent);
//		finish();

    }


    static class ViewHolder {
        TextView txtmerchantname, txtmerid, txtmeradd, txtmermeasure;
        Button btnselect;
    }


    public class SpecialAdapter extends SimpleAdapter {
        //    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };

        private LayoutInflater mInflater;

        public SpecialAdapter(Context ctx, List<HashMap<String, String>> listData, int resourceId, String[] columnTags, int[] columnIds) {
            super(ctx, listData, resourceId, columnTags, columnIds);


            ctx = MerchantInventoryMerchantList.this;
            listData = listData;
            resourceId = R.layout.merchantlistview;
            columnTags = columnTags;
            columnIds = columnIds;


        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return super.getItemId(position);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            HashMap<String, String> map = listData.get(position);
            String measureValue = map.get("measure").toString();
            double measure1 = Double.parseDouble(measureValue);
            if (measure1 > 1.5) {
                view.setBackgroundColor(getResources().getColor(R.color.CoolGreen));
            } else if ((1.5 >= measure1) && (measure1 > 1.0)) {
                view.setBackgroundColor(getResources().getColor(R.color.CoolYellow));
            } else if (1.0 >= measure1) {
                view.setBackgroundColor(getResources().getColor(R.color.CoolRed));
            }
            return view;
        }
    }
}