/**
 *
 */
package com.lk.lankabell.android.activity.tsr.activity;

/**
 *
 */

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.*;
import android.os.Bundle;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

/**
 * @author
 *
 *Creating table structure for Merchant Inventory
 */
public class CreateTable extends Activity {

    private DatabaseHandler dbh;

    public String merchants1[];
    public String card50[];
    public String cardRL50[];
    public String card100[];
    public String cardRL100[];
    public String card200[];
    public String cardRL200[];
    public String card500[];
    public String cardRL500[];


    String one_hundred[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

    TableLayout tl;
    TableRow tr;
    TextView Merchant, five_card, three_card, one_card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.table_main);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
        if (myTitleText != null) {
            myTitleText.setText("Merchant Inventory");
        }

        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
//dbh.InsertData();

        SQLiteDatabase db = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
        Cursor InventoryDetailCursor1 = db.rawQuery("SELECT C.MERCHANT_NAME,A.STOCK_IN_HAND,A.REORDER_LEVEL  FROM MERCHANT_INVENTORY_DETAILS A,MERCHANT_INVENTORY B,MERCHANT_MASTER C WHERE A.INVENTORY_ID = B.INVENTORY_ID AND B.MERCHANT_ID = C.MERCHANT_ID ORDER BY A.STOCK_IN_HAND DESC", null);
// ,A.CATEGORY_ID
/*
int aaaa = InventoryDetailCursor1.getCount();
int ss = InventoryDetailCursor1.getColumnIndex("MERCHANT_NAME");
*/
        int count = InventoryDetailCursor1.getCount();

        merchants1 = new String[count];
        card50 = new String[count];
        cardRL50 = new String[count];
        card100 = new String[count];
        cardRL100 = new String[count];
        card200 = new String[count];
        cardRL200 = new String[count];
        card500 = new String[count];
        cardRL500 = new String[count];

        if (InventoryDetailCursor1.moveToNext()) {
            for (int i = 0; i < InventoryDetailCursor1.getCount(); i++) {
                String mercharntName = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("MERCHANT_NAME"));


                boolean isExist = false;
                int x = 0;
                for (int j = 0; j < InventoryDetailCursor1.getCount(); j++) {

                    if (merchants1[j] != null) {

                        String A = merchants1[j].toString();
                        if (merchants1[j].equalsIgnoreCase(mercharntName)) {
                            isExist = true;
                            x = j;


                        } else {
                            isExist = false;
                        }
                    }
                }

                if (isExist == true) {
                    if (InventoryDetailCursor1.getInt(InventoryDetailCursor1.getColumnIndex("CATEGORY_ID")) == 601) {
                        cardRL50[x] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("REORDER_LEVEL")).toString();
                        card50[x] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("STOCK_IN_HAND")).toString();
                    }
                    if (InventoryDetailCursor1.getInt(InventoryDetailCursor1.getColumnIndex("CATEGORY_ID")) == 602) {
                        cardRL100[x] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("REORDER_LEVEL")).toString();
                        card100[x] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("STOCK_IN_HAND")).toString();
                    }
                    if (InventoryDetailCursor1.getInt(InventoryDetailCursor1.getColumnIndex("CATEGORY_ID")) == 603) {
                        cardRL200[x] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("REORDER_LEVEL")).toString();
                        card200[x] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("STOCK_IN_HAND")).toString();
                    }
                    if (InventoryDetailCursor1.getInt(InventoryDetailCursor1.getColumnIndex("CATEGORY_ID")) == 604) {
                        cardRL500[x] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("REORDER_LEVEL")).toString();
                        card500[x] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("STOCK_IN_HAND")).toString();
                    }
                } else {
                    merchants1[i] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("MERCHANT_NAME")).toString();


                    if (InventoryDetailCursor1.getInt(InventoryDetailCursor1.getColumnIndex("CATEGORY_ID")) == 601) {
                        cardRL50[i] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("REORDER_LEVEL")).toString();
                        card50[i] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("STOCK_IN_HAND")).toString();
                    }
                    if (InventoryDetailCursor1.getInt(InventoryDetailCursor1.getColumnIndex("CATEGORY_ID")) == 602) {
                        cardRL100[i] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("REORDER_LEVEL")).toString();
                        card100[i] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("STOCK_IN_HAND")).toString();
                    }
                    if (InventoryDetailCursor1.getInt(InventoryDetailCursor1.getColumnIndex("CATEGORY_ID")) == 603) {
                        cardRL200[i] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("REORDER_LEVEL")).toString();
                        card200[i] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("STOCK_IN_HAND")).toString();
                    }
                    if (InventoryDetailCursor1.getInt(InventoryDetailCursor1.getColumnIndex("CATEGORY_ID")) == 604) {
                        cardRL500[i] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("REORDER_LEVEL")).toString();
                        card500[i] = InventoryDetailCursor1.getString(InventoryDetailCursor1.getColumnIndex("STOCK_IN_HAND")).toString();
                    }

                }

                InventoryDetailCursor1.moveToNext();
            }
        }


        tl = (TableLayout) findViewById(R.id.maintable);
        addHeaders();
        addData();
    }
//  Source Code modified by Jei *******************************************************

    /** This function add the headers to the <span id="IL_AD1" class="IL_AD">table</span> **/
    public void addHeaders() {

/** Create a TableRow dynamically **/
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

/** Creating a TextView to add to the row **/
        TextView Merchant = new TextView(this);
        Merchant.setText("Merchant");
        Merchant.setTextColor(Color.WHITE);
        Merchant.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Merchant.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        Merchant.setPadding(1, 1, 1, 0);
        tr.addView(Merchant);  // Adding textView to tablerow.

//************************* Source Code added by Jei***************************************

        TextView CardType = new TextView(this);
        CardType.setText("Card Type");
        CardType.setTextColor(Color.WHITE);
        CardType.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        CardType.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        CardType.setPadding(1, 1, 1, 0);
        tr.addView(CardType);

/** Creating another textview **/
        TextView five_card = new TextView(this);
        five_card.setText(" 50 Rs");
        five_card.setTextColor(Color.WHITE);
        five_card.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        five_card.setPadding(1, 1, 1, 0);
        five_card.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(five_card); // Adding textView to tablerow.


        TextView three_card = new TextView(this);
        three_card.setText(" 100 Rs ");
        three_card.setTextColor(Color.WHITE);
        three_card.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        three_card.setPadding(1, 1, 1, 0);
        three_card.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(three_card); // Adding textView to tablerow.


        TextView one_card = new TextView(this);
        one_card.setText(" 200 Rs ");
        one_card.setTextColor(Color.WHITE);
        one_card.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        one_card.setPadding(1, 1, 1, 0);
        one_card.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(one_card); // Adding textView to tablerow.

// Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

// we are adding two textviews for the divider because we have two columns
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

/** Creating another textview **/
        TextView divider = new TextView(this);
        divider.setText("---------");
//divider.setTextColor(Color.<span id="IL_AD8" class="IL_AD">GREEN</span>);
        divider.setTextColor(Color.GRAY);
        divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        divider.setPadding(3, 0, 0, 0);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.

        TextView divider2 = new TextView(this);
        divider2.setText("---------");
        divider2.setTextColor(Color.GRAY);
        divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        divider2.setPadding(3, 0, 0, 0);
        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider2); // Adding textView to tablerow.

        TextView divider3 = new TextView(this);
        divider3.setText("--------");
//divider.setTextColor(Color.<span id="IL_AD8" class="IL_AD">GREEN</span>);
        divider3.setTextColor(Color.GRAY);
        divider3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        divider3.setPadding(3, 0, 0, 0);
        divider3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider3); // Adding textView to tablerow.

        TextView divider4 = new TextView(this);
        divider4.setText("---------");
//divider.setTextColor(Color.<span id="IL_AD8" class="IL_AD">GREEN</span>);
        divider4.setTextColor(Color.GRAY);
        divider4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        divider4.setPadding(3, 0, 0, 0);
        divider4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider4); // Adding textView to tablerow.


        TextView divider5 = new TextView(this);
        divider5.setText("---------");
//divider.setTextColor(Color.<span id="IL_AD8" class="IL_AD">GREEN</span>);
        divider5.setTextColor(Color.GRAY);
        divider5.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        divider5.setPadding(3, 0, 0, 0);
        divider5.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider5); // Adding textView to tablerow.

// Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    }


/** This function add the data to the table **/


    /**
     * Adding data to the merchant inventory table
     */
    public void addData() {

        if (merchants1 != null) {
            for (int i = 0; i < merchants1.length; i++) {
/** Create a TableRow dynamically **/
                tr = new TableRow(this);
                tr.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));


/** Creating a TextView to add to the row **/
                Merchant = new TextView(this);
                Merchant.setText(merchants1[i]);
                Merchant.setTextColor(Color.WHITE);
                Merchant.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Merchant.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                Merchant.setPadding(5, 5, 5, 5);
                tr.addView(Merchant);  // Adding textView to tablerow.


/** Creating another textview **/
                five_card = new TextView(this);


                if (card50[i] != null) {
                    five_card.setText(card50[i] + "/" + cardRL50[i]);
                    double test1 = Double.parseDouble(card50[i]) / Double.parseDouble(cardRL50[i]);
                    if (test1 > 2) {
                        five_card.setTextColor(Color.GREEN);
                    } else if (test1 > 1.5) {
                        five_card.setTextColor(Color.YELLOW);
                    } else {
                        five_card.setTextColor(Color.RED);
                    }

                }
                five_card.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                five_card.setPadding(5, 5, 5, 5);
                five_card.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(five_card); // Adding textView to tablerow.


/** Creating another textview **/
                three_card = new TextView(this);

                if (card100[i] != null) {
                    three_card.setText(card100[i] + "/" + cardRL100[i]);
                    double test2 = Double.parseDouble(card100[i]) / Double.parseDouble(cardRL100[i]);
                    if (test2 > 2) {
                        three_card.setTextColor(Color.GREEN);
                    } else if (test2 > 1.5) {
                        three_card.setTextColor(Color.YELLOW);
                    } else {
                        three_card.setTextColor(Color.RED);
                    }

                }
                three_card.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                three_card.setPadding(5, 5, 5, 5);
                three_card.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(three_card); // Adding textView to tablerow.


/** Creating another textview **/
                one_card = new TextView(this);
                one_card.setText(one_hundred[i]);
                one_card.setTextColor(Color.GREEN);
                one_card.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                one_card.setPadding(5, 5, 5, 5);
                one_card.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(one_card);                 // Adding textView to tablerow.

// Add the TableRow to the TableLayout
                tl.addView(tr, new TableLayout.LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }
        }

    }

}
