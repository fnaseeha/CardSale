package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.SalesReport;

@SuppressLint("SimpleDateFormat")
public class TSRDenomWiseReportAdapter  extends BaseAdapter {

	Context context;
	ArrayList<SalesReport> salesReport;

	public TSRDenomWiseReportAdapter(Context context, ArrayList<SalesReport> list) {
		this.context = context;
		salesReport = list;
	}

	public int getCount() {
		return salesReport.size();
	}

	public Object getItem(int position) {
		return salesReport.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

   	public View getView(int position, View convertView, ViewGroup arg2) {
		SalesReport salesReportItems = salesReport.get(position);
		if (convertView==null){
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.denomwise_list_row,null );
		}
		TextView tvDate = (TextView) convertView.findViewById(R.id.date);
		tvDate.setText(salesReportItems.getDate().toString());
		TextView tvCardDenom = (TextView) convertView.findViewById(R.id.card_denom);
		tvCardDenom.setText(salesReportItems.getDenom().toString());
		TextView tvAmount= (TextView) convertView.findViewById(R.id.amount);
		tvAmount.setText(salesReportItems.getAmount().toString());
		
		if (salesReportItems.getDate().toString().equals("Grand Total")) {
			tvDate.setText("Grand Total");
			if(position==salesReport.size()-1){
				convertView.setBackgroundColor(Color.parseColor("#27408B"));
				tvDate.setTextColor(Color.parseColor("#00FF00")); 
				tvCardDenom.setTextColor(Color.parseColor("#00FF00")); 
				tvAmount.setTextColor(Color.parseColor("#00FF00")); 
				tvCardDenom.setText("");
			}
		}
		return convertView;
	}
}
