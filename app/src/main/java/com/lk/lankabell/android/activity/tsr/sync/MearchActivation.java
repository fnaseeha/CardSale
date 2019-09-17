package com.lk.lankabell.android.activity.tsr.sync;

public class MearchActivation {
	
	private Long merchantId;
	private String merchantname;
	private Integer Activation;
	
	public MearchActivation(Long mId,String mname,Integer Active) {
		this.merchantId = mId;
		this.merchantname = mname;
		this.Activation = Active;
	}
	
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantname() {
		return merchantname;
	}
	public void setMerchantname(String merchantname) {
		this.merchantname = merchantname;
	}
	public Integer getActivation() {
		return Activation;
	}
	public void setActivation(Integer activation) {
		Activation = activation;
	}
	
}
