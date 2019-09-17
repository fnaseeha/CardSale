package com.lk.lankabell.android.activity.tsr.beans;

public class SynchReportView {

	// Date,component,percentage [synch count/total count]
			private String Date;
			private String component;
			private String presynch;
			private String postsynch;
			
			public SynchReportView(String date, String component, String presynch, String postsynch) {
				super();
				Date = date;
				this.component = component;
				this.presynch = presynch;
				this.postsynch = postsynch;
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
			
			public String getpresyncht() {
				return presynch;
			}
			
			public void setpresynch(String presynch) {
				this.presynch = presynch;
			}
			
			public String getpostsynch() {
				return postsynch;
			}
			
			public void setpostsynch(String postsynch) {
				this.postsynch = postsynch;
			}
				
}
