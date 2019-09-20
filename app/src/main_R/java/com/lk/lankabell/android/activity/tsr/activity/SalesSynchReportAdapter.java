package com.lk.lankabell.android.activity.tsr.activity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.SalesSynchReport;

public class SalesSynchReportAdapter extends BaseAdapter {

	Context context;
	ArrayList<SalesSynchReport> synchReports;

	public SalesSynchReportAdapter(Context context,
			ArrayList<SalesSynchReport> list) {
		this.context = context;
		synchReports = list;
	}

	public int getCount() {
		return synchReports.size();
	}

	public Object getItem(int position) {
		return synchReports.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup group) {
		SalesSynchReport synchReportsItems = synchReports.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.salessynch_list_row, null);
		}
		TextView tvStatus = (TextView) convertView.findViewById(R.id.synchDate);
		if (synchReportsItems.getDate() != null) {
			tvStatus.setText(synchReportsItems.getDate().toString());
		} else {
			tvStatus.setText("None");
		}

		TextView tvComponent = (TextView) convertView.findViewById(R.id.textComponent);
		if (synchReportsItems.getComponent() != null) {
			tvComponent.setText(String.valueOf(synchReportsItems.getComponent()));
		} else {
			tvComponent.setText("None");
		}

		TextView tvTotalRecords = (TextView) convertView.findViewById(R.id.totalRecords);
		if (synchReportsItems.getTotalCount() != null) {
			tvTotalRecords.setText("/ " +String.valueOf(synchReportsItems.getTotalCount()));
		} else {
			tvTotalRecords.setText("None");
		}

		TextView tvSynchRecords = (TextView) convertView.findViewById(R.id.synchRecords);
		if (synchReportsItems.getSynchCount() != null) {
			tvSynchRecords.setText(String.valueOf(synchReportsItems.getSynchCount()));
		} else {
			tvSynchRecords.setText("None");
		}

		return convertView;
	}

}
