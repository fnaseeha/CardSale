package com.lk.lankabell.android.activity.tsr.beans;

import android.view.View.OnClickListener;

public class MainMenuItem {

	private int imageId;
	private String title;
	private OnClickListener onclick;
	
	private int id;
	
	
	
	public MainMenuItem() {
		super();
	}
	
	public MainMenuItem(int imageId, String title, OnClickListener onclick) {
		super();
		this.imageId = imageId;
		this.title = title;
		this.onclick = onclick;
	}
	public MainMenuItem(int imageId, String title) {
		super();
		this.imageId = imageId;
		this.title = title;
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public OnClickListener getOnclick() {
		return onclick;
	}

	public void setOnclick(OnClickListener onclick) {
		this.onclick = onclick;
	}


	
	
}
