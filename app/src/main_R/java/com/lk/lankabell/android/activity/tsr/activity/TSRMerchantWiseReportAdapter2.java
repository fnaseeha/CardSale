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

import com.lk.lankabell.android.activity.tsr.sqlite.SalesReport;
import com.lk.lankabell.android.activity.tsr.R;

@SuppressLint("SimpleDateFormat")
public class TSRMerchantWiseReportAdapter2 extends BaseAdapter {

	Context context;
	ArrayList<SalesReport> salesReport;

	public TSRMerchantWiseReportAdapter2(Context context, ArrayList<SalesReport> list) {
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
			convertView=inflater.inflate(R.layout.merchantwise_list_row_summ,null );
		}
		
		TextView tvMerchantId = (TextView) convertView.findViewById(R.id.merchant_id);
		if (salesReportItems.getMerchantId() > 0) {
			tvMerchantId.setText("" + salesReportItems.getMerchantId());
		}else{
			tvMerchantId.setText("");
		}
		
		if(!salesReportItems.getMerchantName().equals("")){			
			tvMerchantId.setText((salesReportItems.getMerchantName().length() > 12)? salesReportItems.getMerchantName().substring(0, 15):salesReportItems.getMerchantName());
		} 
		
		
		
		TextView tvAmount= (TextView) convertView.findViewById(R.id.amount);
		if (salesReportItems.getAmount() !=null) {
			tvAmount.setText(salesReportItems.getAmount().toString());
		} else {
			tvAmount.setText("");
		}
		
		String date=(salesReportItems.getDate() != null) ? salesReportItems.getDate().toString() : "";
		if (date.equals("Grand Total")) {
			tvMerchantId.setText("Grand Total");
			if(position==salesReport.size()-1){ 
				convertView.setBackgroundColor(Color.parseColor("#27408B")); 
				tvMerchantId.setTextColor(Color.parseColor("#00FF00"));  
				//tvCardQty.setTextColor(Color.parseColor("#00FF00")); 
				tvAmount.setTextColor(Color.parseColor("#00FF00")); 
				 
			} 
		}
		
		return convertView;
	}

}
