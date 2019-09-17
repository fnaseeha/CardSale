/**
 * 
 */
package com.lk.lankabell.android.activity.tsr.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.lk.lankabell.android.activity.tsr.R;

/**
 * @author Jei
 * 
 * This class is for Card Bulk Acceptance. 
 *
 */
public class ExpAdapter extends BaseExpandableListAdapter {
	
	
	 private Context myContext;
	  public ExpAdapter(Context context) {
	   myContext = context;
	  }
	 
	  
	  public Object getChild(int groupPosition, int childPosition) {
	   return null;
	  }
	 
	  public long getChildId(int groupPosition, int childPosition) {
	   return 0;
	  }
	 
	  public View getChildView(int groupPosition, int childPosition,
	    boolean isLastChild, View convertView, ViewGroup parent) {
	    
		 
			
	   if (convertView == null) {
	    LayoutInflater inflater =  (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.child_row_second, null);
	   }
	   convertView.setOnClickListener(null);
	   convertView.setOnClickListener(null);
		  
		 
			  
	   return convertView;
	  }
	  public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		  
		return 1;
	}

	 
	  
	  public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	  
	  
	  public int getGroupCount() {
		// TODO Auto-generated method stub
		return MainActivity.arrGroupelements.length;
	}
	  
	  public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	 
	  public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		  if (convertView == null) {
			    LayoutInflater inflater =  (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    convertView = inflater.inflate(R.layout.group_row, null);

			   }
			   TextView tvGroupName = (TextView) convertView.findViewById(R.id.tvGroupName);
			   tvGroupName.setText(MainActivity.arrGroupelements[groupPosition]);
			   return convertView;
	}

	  public boolean hasStableIds() {
		  return false;
	  };

	  public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
