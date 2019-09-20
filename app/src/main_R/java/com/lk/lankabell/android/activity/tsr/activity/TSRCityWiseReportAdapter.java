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
public class TSRCityWiseReportAdapter extends BaseAdapter {

	Context context;
	ArrayList<SalesReport> salesReport;

	public TSRCityWiseReportAdapter(Context context, ArrayList<SalesReport> list) {
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
			convertView=inflater.inflate(R.layout.citywise_list_row,null );
		}
		TextView tvCity = (TextView) convertView.findViewById(R.id.city);
		if (salesReportItems.getCity()!=null) {
			tvCity.setText(salesReportItems.getCity().toString());
		} else {
			tvCity.setText("");
		}
		
		TextView tvCardDenom = (TextView) convertView.findViewById(R.id.card_denom);
		if (salesReportItems.getDenom()!=null) {
			tvCardDenom.setText(salesReportItems.getDenom().toString());
		} else {
			tvCardDenom.setText("");
		}
		TextView tvCardQty = (TextView) convertView.findViewById(R.id.card_qty);
		if (salesReportItems.getQty()!=null) {
			tvCardQty.setText(salesReportItems.getQty().toString());
		} else {
			tvCardQty.setText("");
		}
		TextView tvAmount= (TextView) convertView.findViewById(R.id.amount);
		if (salesReportItems.getAmount()!=null) {
			tvAmount.setText(salesReportItems.getAmount().toString());
		} else {
			tvAmount.setText("");
		}
		
		
		String date=(salesReportItems.getDate() != null) ? salesReportItems.getDate().toString() : "";
		if (date.equals("Grand Total")) {
			tvCity.setText("Grand Total");
			if(position==salesReport.size()-1){ 
				convertView.setBackgroundColor(Color.parseColor("#27408B")); 
				tvCity.setTextColor(Color.parseColor("#00FF00"));  
				tvCardQty.setTextColor(Color.parseColor("#00FF00")); 
				tvAmount.setTextColor(Color.parseColor("#00FF00")); 
				 
			} 
		}
		
		return convertView;
	}

}
