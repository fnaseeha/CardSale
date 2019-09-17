package com.lk.lankabell.android.activity.tsr.beans;

public class SynchReport {
	
	// Date,component,percentage [synch count/total count]
		private String Date;
		private String component;
		private String synchCount;
		private String totalCount;

		public SynchReport(String date, String component, String synchCount, String totalCount) {
			super();
			Date = date;
			this.component = component;
			this.synchCount = synchCount;
			this.totalCount = totalCount;
		}

		public String getDate() {
			return Date;
		}

		public void setDate(String date) {
			Date = date;
		}

		public String getComponent() {
			return component;
		}

		public void setComponent(String component) {
			this.component = component;
		}

		public String getSynchCount() {
			return synchCount;
		}

		public void setSynchCount(String synchCount) {
			this.synchCount = synchCount;
		}

		public String getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(String totalCount) {
			this.totalCount = totalCount;
		}

		
}
