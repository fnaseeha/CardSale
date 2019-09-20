package com.lk.lankabell.android.activity.tsr.sqlite;

public class DenomWiseDetails {
	private String invoiceDate;
	private String denomination;
	private String amount;

	public DenomWiseDetails() {
		super();
	}

	public DenomWiseDetails(String invoiceDate, String denomination,
			String amount) {
		super();
		this.invoiceDate = invoiceDate;
		this.denomination = denomination;
		this.amount = amount;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
