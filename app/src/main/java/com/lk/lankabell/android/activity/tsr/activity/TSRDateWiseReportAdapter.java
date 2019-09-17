package com.lk.lankabell.android.activity.tsr.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
public class TSRDateWiseReportAdapter extends BaseAdapter {

	Context context;
	ArrayList<SalesReport> salesReport;

	public TSRDateWiseReportAdapter(Context context, ArrayList<SalesReport> list) {
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
			convertView=inflater.inflate(R.layout.datewise_list_row,null );
		}
		
		String date=salesReportItems.getDate().toString();
        TextView tvDate = (TextView) convertView.findViewById(R.id.entered_date);
        TextView tvDenom = (TextView) convertView.findViewById(R.id.denom);
        TextView tvCardQty = (TextView) convertView.findViewById(R.id.card_qty);
        TextView tvAmount= (TextView) convertView.findViewById(R.id.amount);
        
		if (date.equals("Grand Total")) {
			tvDate.setText("Grand Total");
			if(position==salesReport.size()-1){
				
				convertView.setBackgroundColor(Color.parseColor("#27408B"));
				
				tvDate.setTextColor(Color.parseColor("#00FF00")); 
				tvDenom.setTextColor(Color.parseColor("#00FF00")); 
				tvCardQty.setTextColor(Color.parseColor("#00FF00")); 
				tvAmount.setTextColor(Color.parseColor("#00FF00")); 
				
				tvDenom.setText("");
			}
			
			
		} else {
			try {
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
			Date d = sd.parse(date);
	        sd = new SimpleDateFormat("yy/MM/dd");
			tvDate.setText(sd.format(d));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        
		

		
		tvDenom.setText((salesReportItems.getDenom() != null) ? salesReportItems.getDenom().toString() : "");
		tvCardQty.setText((salesReportItems.getQty()!= null)?salesReportItems.getQty().toString():"");
		tvAmount.setText(salesReportItems.getAmount().toString());
		
		
		 
				
		return convertView;
	}

}
