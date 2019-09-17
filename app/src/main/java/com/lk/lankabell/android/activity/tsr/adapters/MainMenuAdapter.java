package com.lk.lankabell.android.activity.tsr.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.beans.MainMenuItem;

public class MainMenuAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<MainMenuItem> menuitems;

	public MainMenuAdapter(Context context, ArrayList<MainMenuItem> menuitems) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.menuitems = menuitems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuitems.size();
	}

	@Override
	public MainMenuItem getItem(int arg0) {
		// TODO Auto-generated method stub
		return menuitems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		if(view == null){
			LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.grid_item_main, null);
		}
		ImageView img = (ImageView)view.findViewById(R.id.grid_item_main_img);
		TextView tvTitle = (TextView)view.findViewById(R.id.grid_item_main_title);
		
		MainMenuItem item = getItem(arg0);
		if (item.getId() > 0) {
			view.setId(item.getId());
		}
		img.setImageResource(item.getImageId());
		tvTitle.setText(item.getTitle());
		view.setOnClickListener(item.getOnclick());
		//view.setBackgroundResource(R.drawable.home_icon_selector);
		
		return view;
	}

}
